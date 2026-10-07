# BC Permanent Pacific Time (PCT, UTC-7): Impact Report for nr-brmb-pit-claim

| | |
|---|---|
| **Prepared** | 2026-10-07 |
| **Repo state** | public GitHub `bcgov/nr-brmb-pit-claim`, branch `master` @ `02ca862` (2026-08-14, merge of `release/2.5.1`). Newer feature and Renovate branches exist but are not merged. |
| **Components** | `cirras-claims-api` (Java 21, Spring, MyBatis, PostgreSQL; 239 Java files), `pit-claim-war` (Angular 19 on Tomcat), `cirras-claims-ngclient-lib` (generated Angular client), `pit-claim-liquibase` (262 `date` and 105 `timestamp` columns, no `timestamptz`), `crunchy-postgres` (Crunchy PostgreSQL 17 Helm chart), `openshift` and `.github/workflows` |
| **Deadline** | **Sunday 2026-11-01, 02:00 local (09:00 UTC)**, about 3.5 weeks away |
| **Bottom line** | **Medium risk, on the platform.** The code contains no fixed PDT or PST offsets and correctly runs in `America/Vancouver`, but both containers are pinned to `tomcat:10.1.44-jre21` (August 2025), whose JRE predates tzdata 2026b. **From Nov 1 that JRE applies PST (UTC-8)**, so claim dates synced from CIRRAS can be stored **one day early or late** whenever this API and the CIRRAS sender disagree (F1), and database audit times can be one hour off if PostgreSQL and the JRE disagree (F2). **Fix: move to a Tomcat image on JRE 21.0.12 or later, set `-Duser.timezone=America/Vancouver`, confirm the Crunchy PostgreSQL image carries tzdata 2026b, and patch in the same window as CIRRAS.** |

---

## 1. What changed (same basis as the earlier reports)

- **Government rule.** BC stopped changing clocks after 2026-03-08. On **2026-11-01 clocks do not fall back.** BC stays at **UTC-7** all year, named *Pacific time (PCT)*.
- **IANA tzdata 2026b** models `America/Vancouver` as permanent UTC-7 from 2026-11-01 02:00.
- **JDK builds with the rule:** 8u501, 11.0.32, 17.0.20, 21.0.12 and 25.0.4 or later. Earlier builds treat BC winter as PST (UTC-8).
- **moment-timezone ships its own tz data.** Only 0.6.2 and later contain the BC rule; every 0.5.x release is stale.

---

## 2. Summary of findings

| # | Area | Severity | Fails on Nov 1? | Fix |
|---|---|---|---|---|
| F1 (C1, C2, C3) | Claim calendar dates from CIRRAS converted through a stale JRE (`tomcat:10.1.44-jre21`); sync dirty check flags them as changed | **High** | **Yes**, if this API and the CIRRAS sender are not both patched: dates stored one day early or late | R1, R2, R4 |
| F2 (C4) | `CREATE_DATE` / `UPDATE_DATE` from PostgreSQL `CURRENT_TIMESTAMP` / `now()` in `timestamp` columns | **Medium** | If the PostgreSQL image and the JRE carry different tzdata: stored one hour off | R1, R3 |
| F3 (C5) | Optimistic sync ordering on wall-clock `*DataSyncTransDate` columns | Low | Only during a mixed or late-patched period: an out-of-order message up to one hour older can win | R5 |
| C6 | Outbox polling first-run time in `ZoneId.systemDefault()` | Low | Starts one hour off on an unpatched JRE | R1 |
| C7 | Active and expiry calculation in `CirrasDataSyncRsrcFactory` | Low | Only within one hour of an expiry boundary | R1 |
| C8 | Front end (`moment` 2.29.4, Angular `date` pipe; unused `moment-timezone` 0.5.43) | Low | Only for users whose browser or OS has outdated tz data | R7 |
| C9 | Crunchy backup schedule in UTC (comment assumes 08:00 UTC is midnight) | None | No; backups start at 01:00 local all year | R8 |
| D1 | `AsyncMasterTask` random start offset discarded (pre-existing) | Low | Not related to the change | R9 |
| D2 | `getMaxExpiryDate()` returns 9001-01-31 at the current time (pre-existing) | Medium | Not related to the change | R9 |
| D3 | `CirrasServiceHelper.equals` uses the JVM zone implicitly (pre-existing) | Low | Covered by F1 | R6 |

---

## 3. Overview

Both containers use `tomcat:10.1.44-jre21`, set `/etc/localtime` to `America/Vancouver`, and the API receives `TZ` from the `TIME_ZONE` configuration value. The JVM therefore runs in `America/Vancouver`, which is the right choice for this application, but it applies the rules in the JRE's own time zone database. The pinned tag dates from August 2025 and carries a Temurin 21 build (21.0.8 at the time of that tag) that predates tzdata 2026b. Until the image moves to JRE 21.0.12 or later, the JVM will apply PST (UTC-8) after 2026-11-01.

The application stores calendar dates in PostgreSQL `date` columns and timestamps in `timestamp` (without time zone) columns. Both are converted through the JVM zone, and the database also writes audit timestamps with `CURRENT_TIMESTAMP` and `now()`, which use the session zone and PostgreSQL's own time zone data. The main risks are therefore a stale JRE, a JRE and database that disagree, and a CIRRAS sender that disagrees with this API during the transition.

## 4. Areas of Concern

### C1. Pinned runtime image with stale tzdata (platform)

- `cirras-claims-api/Dockerfile` line 1 and `pit-claim-war/Dockerfile` line 1: `FROM tomcat:10.1.44-jre21`.
- Lines 25 and 22 respectively link `/etc/localtime` to `America/Vancouver`. That link uses the operating system tzdata in the image, which only affects operating system tools. The JVM uses its own `tzdb.dat`.
- `openshift/cirras-claims-api-deployment.yaml` lines 126-130 set `TZ` from `TIME_ZONE` (`.github/workflows/openshift-deploy.yml` line 122 reads it from the GitHub environment variable `vars.TIME_ZONE`). The team should confirm that the value is `America/Vancouver` in every environment.
- `cirras-claims-api/deploy-tools/setenv.sh` does not set `-Duser.timezone`, so the zone depends on `TZ` and `/etc/localtime`.

### C2. Calendar dates converted through the JVM zone

- MyBatis binds 43 `java.util.Date` values as `jdbcType=DATE`: `effectiveDate`, `expiryDate`, `submittedByDate`, `recommendedByDate` and `approvedByDate`.
- `submitted_by_date` and the related columns come from CIRRAS (`ccs.claim_calculation.sql` line 100: "from CIRR_CLAIM_OF_LOSSES.MONITORED_DATE"). They arrive through the sync endpoints as `java.util.Date` values serialized as epoch milliseconds, which represent local midnight in the sender's JVM.
- This API converts each epoch value to a calendar day in its own JVM zone. If the CIRRAS sender and this API disagree on the rules after 2026-11-01, the day shifts: a patched sender sends 07:00Z, and an unpatched receiver reads it as 23:00 PST on the previous day.

### C3. Date comparison in the sync dirty check

`services/utils/CirrasServiceHelper.java` lines 238-250 compare two `Date` values by formatting both with `SimpleDateFormat("yyyy-MM-dd")` in the JVM zone. `CirrasDataSyncService.java` lines 300-304 use it for the submitted, recommended and approved dates. When C2 occurs, the check reports the record as changed and writes the shifted date.

### C4. Timestamp columns and three clocks

- `dataSyncTransDate`, `claimDataSyncTransDate`, `policyDataSyncTransDate` and `growerDataSyncTransDate` (35 bindings) are `jdbcType=TIMESTAMP` into `timestamp` columns. pgJDBC writes them as wall time in the JVM zone.
- `CREATE_DATE` and `UPDATE_DATE` come from `CURRENT_TIMESTAMP` or `now()` in the mappers (for example `ClaimMapper.xml` lines 181, 183, 215 and 235, and `ClaimCalculationGrainBasketMapper.xml` lines 94, 96 and 114). Stored in `timestamp` columns, these values become wall time in the session `TimeZone`, which pgJDBC sets from the JVM zone. PostgreSQL then applies its own tzdata, from the Crunchy PostgreSQL 17 image.
- The application then reads both kinds of value back through the JVM zone. If the JRE and the PostgreSQL image carry different tzdata after 2026-11-01, `CREATE_DATE` and `UPDATE_DATE` are stored one hour off, while the sync timestamps remain consistent. The screens show these values as dates only ("Created On" and "Updated On" in `calculation-detail-header.component.html` lines 139 and 166, formatted `yyyy-MM-dd`), so the visible effect is limited to records saved within one hour of midnight.

### C5. Optimistic sync ordering on wall-clock timestamps

- `ClaimMapper.xml` lines 249 and 261, `CropVarietyMapper.xml` line 80 and `CoveragePerilMapper.xml` line 82 update a row only when the stored sync date is earlier than the incoming one. `CirrasDataSyncService.java` lines 366-367 apply the same rule in Java.
- Because the columns hold wall time without an offset, values written by an unpatched JVM after 2026-11-01 are one hour earlier than values written by a patched JVM for the same instant. During a mixed period, or after a late patch, an out-of-order message up to one hour older could overwrite newer data.

### C6. Outbox polling start time (low)

`controllers/async/AsyncMasterTask.java` lines 90-108 schedule the first outbox fetch at a configured `LocalTime` in `ZoneId.systemDefault()`. An unpatched JVM starts it one hour off local time after 2026-11-01. The repeat interval is fixed in seconds and is not affected.

### C7. Active and expiry calculation (low)

`data/assemblers/CirrasDataSyncRsrcFactory.java` lines 545-563 compare "now" with the expiry date, both as `LocalDateTime` in the JVM zone. Both sides use the same zone, so the result changes only within one hour of an expiry boundary.

### C8. Front end (low)

- The Angular application uses plain `moment` 2.29.4 and the Angular `date` pipe (for example `| date:'yyyy-MM-dd'` in the calculation detail components). Both follow the browser's own time zone data, which current browsers update automatically.
- `moment-timezone` 0.5.43 is listed in `package.json` and `angular.json`, and that version carries stale data. The only references in `src/app/utils/index.ts` (lines 166 and 170, `convertToAPITime` helpers using `.tz("America/Vancouver")`) are commented out, so the stale data is unused in this application's code. The `@wf1/wfcc-application-ui` dependency was reviewed separately and showed no impact; branch `feature/PIM-2666-second-attempt-remove-wf-application-ui-dep` is removing it.
- Calendar dates reach the browser as epoch values at the JVM's local midnight. A patched JVM sends 07:00Z, which a browser with stale rules would display as the previous day. This affects only users on unpatched operating systems or browsers.

### C9. Database backup schedule (behaviour change)

`crunchy-postgres/charts/crunchy-postgres/values.yaml` lines 60-69 schedule backups in UTC. The comment says that 08:00 UTC is midnight. After 2026-11-01, 08:00 UTC is 01:00 local time all year. Backups still run; only the comment and the local start time change.

## 5. Areas of Failure

### F1. Calendar dates stored one day early (High likelihood if the JRE is not updated)

**Scenario:** After 2026-11-01, CIRRAS sends a claim whose monitored date is 2026-12-01. If the CIRRAS sender is patched and this API still runs JRE 21.0.8, the API reads `2026-12-01T07:00Z` as 2026-11-30 23:00 PST and stores `2026-11-30` in `submitted_by_date`. The same shift applies in reverse if this API is patched and the sender is not. The dirty check in C3 also marks the record as changed.

**Effect:** Submitted, recommended, approved, effective and expiry dates can be one day early or late, and printouts show the wrong date.

### F2. Audit times one hour off (Medium)

If the JRE and the PostgreSQL image do not both carry tzdata 2026b, `CREATE_DATE` and `UPDATE_DATE` written by the database are stored one hour off. Screens show the wrong date only for records saved within one hour of midnight, but any report or query that uses the time of day is affected (C4).

### F3. Sync ordering during a mixed period (Low)

An out-of-order message can overwrite newer data within a one-hour window (C5).

## 6. Pre-existing Defects Found (independent of this change)

- **D1.** `AsyncMasterTask.java` line 94: `pollingTime.plusSeconds(getRandomSeconds());` discards its result because `LocalTime` is immutable. The random offset meant to stagger nodes is never applied, so all nodes start the outbox task at the same second. The fix is `pollingTime = pollingTime.plusSeconds(getRandomSeconds());`.
- **D2.** `CirrasDataSyncRsrcFactory.java` lines 668-673: `getMaxExpiryDate()` calls `cal.set(9000, 12, 31)`. `Calendar` months are zero-based, so month 12 rolls over to January 9001, and the time of day is not cleared. The result is 9001-01-31 at the current time, rather than 9000-12-31 or the `9999-12-31` maximum defined in `data/utils/DateUtils.java`. The team should confirm which maximum the business expects.
- **D3.** `CirrasServiceHelper.equals` (C3) creates a new `SimpleDateFormat` on every call. This is thread-safe but uses the JVM zone implicitly.

## 7. Potential Resolutions

| # | Action | Owner | Priority |
|---|--------|-------|----------|
| R1 | Move both Dockerfiles to a Tomcat image on JRE 21.0.12 or later (for example a current `tomcat:10.1.x-jre21` tag), confirm with `java -version`, and check `ZoneId.of("America/Vancouver").getRules().getOffset(Instant.parse("2026-12-01T12:00:00Z"))` returns `-07:00`. | Development and Platform | Before 2026-11-01 |
| R2 | Add `-Duser.timezone=America/Vancouver` to `CATALINA_OPTS` in `setenv.sh`, and confirm that `vars.TIME_ZONE` is `America/Vancouver` in every GitHub environment. | Development | Before 2026-11-01 |
| R3 | Confirm that the Crunchy PostgreSQL 17 image carries tzdata 2026b (`SELECT now() AT TIME ZONE 'America/Vancouver'` after 2026-11-01, or `pg_timezone_names` for the abbreviation and offset). Branch `feature/PIM-2711-CrunchyDB-image-update` is a natural place to include this. | Platform | Before 2026-11-01 |
| R4 | Coordinate with the CIRRAS team so that the CIRRAS sync sender and this API are patched in the same window (F1). | Development | Before 2026-11-01 |
| R5 | Deploy all API pods from the same image so that no mixed period exists (F3). | Platform | Before 2026-11-01 |
| R6 | Longer term, exchange calendar dates as `YYYY-MM-DD` strings mapped to `LocalDate`, and audit timestamps as `Instant` values in `timestamptz` columns. This removes the dependency on the JVM zone. | Development | Optional |
| R7 | Remove the unused `moment-timezone` dependency from `package.json` and `angular.json`, or upgrade it to 0.6.5 or later if any future code needs it. | Development | Optional |
| R8 | Update the backup schedule comment in `values.yaml` (C9). | Development | Optional |
| R9 | Fix defects D1 and D2 (Section 6). | Development | Independent |

## 8. Verification Checklist

1. In the API pod, run `java -version` and confirm 21.0.12 or later; confirm `TimeZone.getDefault().getID()` is `America/Vancouver`.
2. In a test environment with the clock set after 2026-11-01, sync a claim with a monitored date of 2026-12-01 and confirm that `submitted_by_date` stores `2026-12-01` and that the sync does not report the record as changed on a second run.
3. Save a calculation and confirm that `CREATE_DATE` and `UPDATE_DATE` in the database match the wall-clock time.
4. Confirm that the outbox task starts at the configured local time.

## 9. Cross-References

- **wfone-common-lib** report: this API uses `wfone-common` modules (`wfone-common-rest-endpoints`, `wfone-common-model` 1.5.0) and their `DateUtils` for parameter validation.
- **wfcc-application-ui-ng-lib** and **wfcc-core-ng-lib** reports: the front-end libraries used by `pit-claim-war`.
