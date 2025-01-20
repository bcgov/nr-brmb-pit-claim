CREATE TABLE cccs.claim_calculation (
  claim_calculation_guid        varchar(32)   NOT NULL,
  primary_peril_code            varchar(32),
  secondary_peril_code          varchar(32),
  claim_status_code             varchar(16)   NOT NULL,
  commodity_coverage_code       varchar(10)   NOT NULL,
  calculation_status_code       varchar(16)   NOT NULL,
  insurance_plan_id             numeric(9)    NOT NULL,
  crop_commodity_id             numeric(9)    NOT NULL,
  create_claim_calc_user_guid   varchar(32),
  update_claim_calc_user_guid   varchar(32),
  crop_year                     numeric(4)    NOT NULL,
  contract_id                   numeric(9)    NOT NULL,
  policy_number                 varchar(20)   NOT NULL,
  claim_number                  numeric(8)    NOT NULL,
  calculation_version           numeric(2)    NOT NULL,
  revision_count                numeric(10),
  grower_number                 numeric(10)   NOT NULL,
  grower_name                   varchar(100)  NOT NULL,
  grower_address_line1          varchar(200),
  grower_address_line2          varchar(200),
  grower_postal_code            varchar(10),
  grower_city                   varchar(40),
  total_claim_amount            numeric(10,2),
  calculation_comment           varchar(1000),
  submitted_by_userid           varchar(32),
  submitted_by_name             varchar(100),
  submitted_by_date             date,
  recommended_by_userid         varchar(32),
  recommended_by_name           varchar(100),
  recommended_by_date           date,
  approved_by_userid            varchar(32),
  approved_by_name              varchar(100),
  approved_by_date              date,
  create_user                   varchar(64)   NOT NULL,
  create_date                   timestamp(0)  NOT NULL,
  update_user                   varchar(64)   NOT NULL,
  update_date                   timestamp(0)  NOT NULL,
  calculate_iiv_ind             varchar(1)    NOT NULL,
  insured_by_meas_type          varchar(10)   NOT NULL,
  grower_province               varchar(2),
  has_cheque_req_ind            varchar(1)    NOT NULL
) TABLESPACE pg_default
;



COMMENT ON TABLE cccs.claim_calculation IS 'The table contains the claim calculation data'
;
COMMENT ON COLUMN cccs.claim_calculation.claim_calculation_guid IS 'Claim Calculation Guid is a unique key of a claims calculation'
;
COMMENT ON COLUMN cccs.claim_calculation.primary_peril_code IS 'Primary Peril Code is the primary peril of the notice of loss. Originaly pulled from CIRRAS it''s editable'
;
COMMENT ON COLUMN cccs.claim_calculation.secondary_peril_code IS 'Secondary Peril Code is the primary peril of the notice of loss. Originaly pulled from CIRRAS it''s editable'
;
COMMENT ON COLUMN cccs.claim_calculation.claim_status_code IS 'Claim Status Code is the status of the claim'
;
COMMENT ON COLUMN cccs.claim_calculation.commodity_coverage_code IS 'Commodity Coverage Code is the coverage of the notice of loss from CIRR_COVERAGE_PERILS.CC_CC_ID'
;
COMMENT ON COLUMN cccs.claim_calculation.calculation_status_code IS 'Calculation Status is the status of the claim calculation'
;
COMMENT ON COLUMN cccs.claim_calculation.insurance_plan_id IS 'Insurance Plan Id FOREIGN KEY to INSURANCE_PLAN'
;
COMMENT ON COLUMN cccs.claim_calculation.crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from CIRR_CROP_TYPES.CRPT_ID'
;
COMMENT ON COLUMN cccs.claim_calculation.create_claim_calc_user_guid IS 'Create Claim Calculation User Guid is a unique key of a claims calculation user who created this record. Not set if this record was created via an automated process.'
;
COMMENT ON COLUMN cccs.claim_calculation.update_claim_calc_user_guid IS 'Update Claim Calculation User Guid is a unique key of a claims calculation user who updated this record last. Not updated if the record was changed via an automated process.'
;
COMMENT ON COLUMN cccs.claim_calculation.crop_year IS 'Crop Year is the year of the Claim'
;
COMMENT ON COLUMN cccs.claim_calculation.contract_id IS 'Contract Id is a unique identifier of a claim from CIRR_CONTRACT_NUMBERS.CN_ID'
;
COMMENT ON COLUMN cccs.claim_calculation.policy_number IS 'Policy Number is the number of the policy from CIRR_CONTRACT_NUMBERS.CONTRACT_NUMBER-CIRR_INSURANCE_POLICIES.CROP_YEAR'
;
COMMENT ON COLUMN cccs.claim_calculation.claim_number IS 'Claim Number is the number of the claim in CIRRAS'
;
COMMENT ON COLUMN cccs.claim_calculation.calculation_version IS 'Calculation Version is the version number of the calculation of a claim'
;
COMMENT ON COLUMN cccs.claim_calculation.grower_number IS 'Grower Number from CIRR_INSURED_GROWERS.GROWER_NUMBER'
;
COMMENT ON COLUMN cccs.claim_calculation.grower_name IS 'Grower Name from CIRR_INSURED_GROWERS.GROWER_LEGAL_NAME'
;
COMMENT ON COLUMN cccs.claim_calculation.grower_address_line1 IS 'Grower Address Line 1 from CIRR_INSURED_GROWERS.LEGAL_ADDRESS_LINE1'
;
COMMENT ON COLUMN cccs.claim_calculation.grower_address_line2 IS 'Grower Address Line 2 from CIRR_INSURED_GROWERS.LEGAL_ADDRESS_LINE2'
;
COMMENT ON COLUMN cccs.claim_calculation.grower_postal_code IS 'Grower Postal Code from CIRR_INSURED_GROWERS.LEGAL_POSTAL_CODE'
;
COMMENT ON COLUMN cccs.claim_calculation.grower_city IS 'Grower City from CIRR_INSURED_GROWERS.CITY_CITY_ID (CIRR_CITIES.NAME)'
;
COMMENT ON COLUMN cccs.claim_calculation.total_claim_amount IS 'Total Claim Amount is the calculated amount pushed to CIRRAS. COVERAGE_AMOUNT_ADJUSTED - TOTAL_PRODUCTION_VALUE'
;
COMMENT ON COLUMN cccs.claim_calculation.calculation_comment IS 'Calculation Comment is a comment of a calculation added by the user'
;
COMMENT ON COLUMN cccs.claim_calculation.submitted_by_userid IS 'Submitted By UserID is the user id of the user that monitors the claim from CIRR_CLAIM_OF_LOSSES.USR_USR_ID_MONITORED_BY'
;
COMMENT ON COLUMN cccs.claim_calculation.submitted_by_name IS 'Submitted By Name is the name of the user that monitors the claim from CIRR_CLAIM_OF_LOSSES.USR_USR_ID_MONITORED_BY'
;
COMMENT ON COLUMN cccs.claim_calculation.submitted_by_date IS 'Submitted By Date is the date when the claim was submitted from CIRR_CLAIM_OF_LOSSES.MONITORED_DATE'
;
COMMENT ON COLUMN cccs.claim_calculation.recommended_by_userid IS 'Recommended By UserID is the user id of the user that recommended the claim from CIRR_CLAIM_OF_LOSSES.USR_USR_ID_RECOMMENDED_BY'
;
COMMENT ON COLUMN cccs.claim_calculation.recommended_by_name IS 'Recommended By Name is the name of the user that recommended the claim from CIRR_CLAIM_OF_LOSSES.USR_USR_ID_RECOMMENDED_BY'
;
COMMENT ON COLUMN cccs.claim_calculation.recommended_by_date IS 'Recommended By Date is the date when the claim was recommended from CIRR_CLAIM_OF_LOSSES.RECOMMENDED_DATE'
;
COMMENT ON COLUMN cccs.claim_calculation.approved_by_userid IS 'Approved By UserID is the user id of the user that approved the claim from CIRR_CLAIM_OF_LOSSES.USR_USR_ID_APPROVED_BY'
;
COMMENT ON COLUMN cccs.claim_calculation.approved_by_name IS 'Approved By Name is the name of the user that approved the claim from CIRR_CLAIM_OF_LOSSES.USR_USR_ID_APPROVED_BY'
;
COMMENT ON COLUMN cccs.claim_calculation.approved_by_date IS 'Approved By Date is the date when the claim was approved from CIRR_CLAIM_OF_LOSSES.APPROVED_DATE'
;
COMMENT ON COLUMN cccs.claim_calculation.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cccs.claim_calculation.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cccs.claim_calculation.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cccs.claim_calculation.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON COLUMN cccs.claim_calculation.calculate_iiv_ind IS 'CALCULATE IIV IND determines whether a IIV calculation is performed (Y) or not (N) from CIRR_UW_CMDTY_CROP_COVERAGE.calculate_iiv_flag'
;
COMMENT ON COLUMN cccs.claim_calculation.insured_by_meas_type IS 'Insured By Meas Type determines how the coverage is calculated (ACRES, UNITS, UNKNOWN)'
;
COMMENT ON COLUMN cccs.claim_calculation.grower_province IS 'Grower Province is the abbreviation of Province or State from CIRR_PROVINCES.CODE'
;
COMMENT ON COLUMN cccs.claim_calculation.has_cheque_req_ind IS 'HAS CHEQUE REQ IND indicates whether the claim is on one or more cheque requisitions (Y) or No (N).'
;

CREATE INDEX ix_cc_casc ON cccs.claim_calculation(calculation_status_code)
 TABLESPACE pg_default
;

CREATE INDEX ix_cc_ccc ON cccs.claim_calculation(commodity_coverage_code)
 TABLESPACE pg_default
;

CREATE INDEX ix_cc_cco ON cccs.claim_calculation(crop_commodity_id)
 TABLESPACE pg_default
;

CREATE INDEX ix_cc_ccu_01 ON cccs.claim_calculation(create_claim_calc_user_guid)
 TABLESPACE pg_default
;

CREATE INDEX ix_cc_ccu_02 ON cccs.claim_calculation(update_claim_calc_user_guid)
 TABLESPACE pg_default
;

CREATE INDEX ix_cc_cn ON cccs.claim_calculation(claim_number)
 TABLESPACE pg_default
;

CREATE INDEX ix_cc_csc ON cccs.claim_calculation(claim_status_code)
 TABLESPACE pg_default
;

CREATE INDEX ix_cc_cy ON cccs.claim_calculation(crop_year)
 TABLESPACE pg_default
;

CREATE INDEX ix_cc_ip ON cccs.claim_calculation(insurance_plan_id)
 TABLESPACE pg_default
;

CREATE INDEX ix_cc_pc_01 ON cccs.claim_calculation(primary_peril_code)
 TABLESPACE pg_default
;

CREATE INDEX ix_cc_pc_02 ON cccs.claim_calculation(secondary_peril_code)
 TABLESPACE pg_default
;

CREATE INDEX ix_cc_pn ON cccs.claim_calculation(policy_number)
 TABLESPACE pg_default
;

ALTER TABLE cccs.claim_calculation ADD
    CONSTRAINT pk_cc PRIMARY KEY (claim_calculation_guid) USING INDEX TABLESPACE pg_default
;

ALTER TABLE cccs.claim_calculation ADD
    CONSTRAINT uk_cn_cv UNIQUE (claim_number, calculation_version) USING INDEX TABLESPACE pg_default
;

ALTER TABLE cccs.claim_calculation ADD CONSTRAINT fk_cc_casc
    FOREIGN KEY (calculation_status_code)
    REFERENCES cccs.calculation_status_code(calculation_status_code)
;

ALTER TABLE cccs.claim_calculation ADD CONSTRAINT fk_cc_ccc
    FOREIGN KEY (commodity_coverage_code)
    REFERENCES cccs.commodity_coverage_code(commodity_coverage_code)
;

ALTER TABLE cccs.claim_calculation ADD CONSTRAINT fk_cc_cco
    FOREIGN KEY (crop_commodity_id)
    REFERENCES cccs.crop_commodity(crop_commodity_id)
;

ALTER TABLE cccs.claim_calculation ADD CONSTRAINT fk_cc_ccu_01
    FOREIGN KEY (create_claim_calc_user_guid)
    REFERENCES cccs.claim_calculation_user(claim_calculation_user_guid)
;

ALTER TABLE cccs.claim_calculation ADD CONSTRAINT fk_cc_ccu_02
    FOREIGN KEY (update_claim_calc_user_guid)
    REFERENCES cccs.claim_calculation_user(claim_calculation_user_guid)
;

ALTER TABLE cccs.claim_calculation ADD CONSTRAINT fk_cc_csc
    FOREIGN KEY (claim_status_code)
    REFERENCES cccs.claim_status_code(claim_status_code)
;

ALTER TABLE cccs.claim_calculation ADD CONSTRAINT fk_cc_ip
    FOREIGN KEY (insurance_plan_id)
    REFERENCES cccs.insurance_plan(insurance_plan_id)
;

ALTER TABLE cccs.claim_calculation ADD CONSTRAINT fk_cc_pc_01
    FOREIGN KEY (primary_peril_code)
    REFERENCES cccs.peril_code(peril_code)
;

ALTER TABLE cccs.claim_calculation ADD CONSTRAINT fk_cc_pc_02
    FOREIGN KEY (secondary_peril_code)
    REFERENCES cccs.peril_code(peril_code)
;
