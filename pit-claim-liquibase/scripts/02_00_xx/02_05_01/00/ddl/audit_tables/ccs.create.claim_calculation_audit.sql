CREATE TABLE ccs.claim_calculation_audit(
    claim_calculation_audit_id        numeric(9, 0)     NOT NULL,
    audit_transaction_type_code       varchar(10)       NOT NULL,
    audit_time_stamp                  timestamp(6)      NOT NULL,
    claim_calculation_guid            varchar(32)       NOT NULL,
    primary_peril_code                varchar(32),
    secondary_peril_code              varchar(32),
    claim_status_code                 varchar(16),
    commodity_coverage_code           varchar(10),
    calculation_status_code           varchar(16),
    insurance_plan_id                 numeric(9, 0),
    crop_commodity_id                 numeric(9, 0),
    create_claim_calc_user_guid       varchar(32),
    update_claim_calc_user_guid       varchar(32),
    claim_calc_grain_quantity_guid    varchar(32),
    calculate_iiv_ind                 varchar(1),
    has_cheque_req_ind                varchar(1),
    crop_year                         numeric(4, 0),
    insured_by_meas_type              varchar(10),
    contract_id                       numeric(9, 0),
    policy_number                     varchar(20),
    claim_number                      numeric(8, 0),
    calculation_version               numeric(2, 0),
    revision_count                    numeric(10, 0),
    grower_number                     numeric(10, 0),
    grower_name                       varchar(100),
    grower_address_line1              varchar(200),
    grower_address_line2              varchar(200),
    grower_postal_code                varchar(10),
    grower_city                       varchar(40),
    grower_province                   varchar(2),
    total_claim_amount                numeric(10, 2),
    calculation_comment               varchar(1000),
    submitted_by_userid               varchar(32),
    submitted_by_name                 varchar(100),
    submitted_by_date                 date,
    recommended_by_userid             varchar(32),
    recommended_by_name               varchar(100),
    recommended_by_date               date,
    approved_by_userid                varchar(32),
    approved_by_name                  varchar(100),
    approved_by_date                  date,
    create_user                       varchar(64)       NOT NULL,
    create_date                       timestamp(0)      NOT NULL,
    update_user                       varchar(64)       NOT NULL,
    update_date                       timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN ccs.claim_calculation_audit.claim_calculation_audit_id IS 'Claim Calculation Audit Id is the ID of the Claim Calculation Audit table, comes from a sequence.'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.audit_time_stamp IS 'Audit Time Stamp denotes when the record was inserted in the audit table.'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.claim_calculation_guid IS 'Claim Calculation Guid is a unique key of a claims calculation'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.primary_peril_code IS 'Primary Peril Code is the primary peril of the notice of loss. Originaly pulled from CIRRAS it''s editable'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.secondary_peril_code IS 'Secondary Peril Code is the primary peril of the notice of loss. Originaly pulled from CIRRAS it''s editable'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.claim_status_code IS 'Claim Status Code is the status of the claim'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.commodity_coverage_code IS 'Commodity Coverage Code is the coverage of the notice of loss from CIRR_COVERAGE_PERILS.CC_CC_ID'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.calculation_status_code IS 'Calculation Status is the status of the claim calculation'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.insurance_plan_id IS 'Insurance Plan Id foreign key to INSURANCE_PLAN'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from CIRR_CROP_TYPES.CRPT_ID'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.create_claim_calc_user_guid IS 'Create Claim Calculation User Guid is a unique key of a claims calculation user who created this record. Not set if this record was created via an automated process.'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.update_claim_calc_user_guid IS 'Update Claim Calculation User Guid is a unique key of a claims calculation user who updated this record last. Not updated if the record was changed via an automated process'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.claim_calc_grain_quantity_guid IS 'Claim Calculation Grain Quantity Guid is a unique key of the record'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.calculate_iiv_ind IS 'CALCULATE IIV IND determines whether a IIV calculation is performed (Y) or not (N) from CIRR_UW_CMDTY_CROP_COVERAGE.calculate_iiv_flag'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.has_cheque_req_ind IS 'HAS CHEQUE REQ IND indicates whether the claim is on one or more cheque requisitions (Y) or No (N).'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.crop_year IS 'Crop Year is the year of the Claim'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.insured_by_meas_type IS 'Insured By Meas Type determines how the coverage is calculated (ACRES, UNITS, UNKNOWN)'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.contract_id IS 'Contract Id is a unique identifier of a claim from CIRR_CONTRACT_NUMBERS.CN_ID'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.policy_number IS 'Policy Number is the number of the policy from CIRR_CONTRACT_NUMBERS.CONTRACT_NUMBER-CIRR_INSURANCE_POLICIES.CROP_YEAR'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.claim_number IS 'Claim Number is the number of the claim in CIRRAS'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.calculation_version IS 'Calculation Version is the version number of the calculation of a claim'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.grower_number IS 'Grower Number from CIRR_INSURED_GROWERS.GROWER_NUMBER'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.grower_name IS 'Grower Name from CIRR_INSURED_GROWERS.GROWER_LEGAL_NAME'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.grower_address_line1 IS 'Grower Address Line 1 from CIRR_INSURED_GROWERS.LEGAL_ADDRESS_LINE1'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.grower_address_line2 IS 'Grower Address Line 2 from CIRR_INSURED_GROWERS.LEGAL_ADDRESS_LINE2'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.grower_postal_code IS 'Grower Postal Code from CIRR_INSURED_GROWERS.LEGAL_POSTAL_CODE'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.grower_city IS 'Grower City from CIRR_INSURED_GROWERS.CITY_CITY_ID (CIRR_CITIES.NAME)'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.grower_province IS 'Grower Province is the abbreviation of Province or State from CIRR_PROVINCES.CODE'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.total_claim_amount IS 'Total Claim Amount is the calculated amount pushed to CIRRAS. COVERAGE_AMOUNT_ADJUSTED - TOTAL_PRODUCTION_VALUE'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.calculation_comment IS 'Calculation Comment is a comment of a calculation added by the user'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.submitted_by_userid IS 'Submitted By UserID is the user id of the user that monitors the claim from CIRR_CLAIM_OF_LOSSES.USR_USR_ID_MONITORED_BY'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.submitted_by_name IS 'Submitted By Name is the name of the user that monitors the claim from CIRR_CLAIM_OF_LOSSES.USR_USR_ID_MONITORED_BY'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.submitted_by_date IS 'Submitted By Date is the date when the claim was submitted from CIRR_CLAIM_OF_LOSSES.MONITORED_DATE'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.recommended_by_userid IS 'Recommended By UserID is the user id of the user that recommended the claim from CIRR_CLAIM_OF_LOSSES.USR_USR_ID_RECOMMENDED_BY'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.recommended_by_name IS 'Recommended By Name is the name of the user that recommended the claim from CIRR_CLAIM_OF_LOSSES.USR_USR_ID_RECOMMENDED_BY'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.recommended_by_date IS 'Recommended By Date is the date when the claim was recommended from CIRR_CLAIM_OF_LOSSES.RECOMMENDED_DATE'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.approved_by_userid IS 'Approved By UserID is the user id of the user that approved the claim from CIRR_CLAIM_OF_LOSSES.USR_USR_ID_APPROVED_BY'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.approved_by_name IS 'Approved By Name is the name of the user that approved the claim from CIRR_CLAIM_OF_LOSSES.USR_USR_ID_APPROVED_BY'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.approved_by_date IS 'Approved By Date is the date when the claim was approved from CIRR_CLAIM_OF_LOSSES.APPROVED_DATE'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN ccs.claim_calculation_audit.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE ccs.claim_calculation_audit IS 'Claim Calculation Audit table is the audit table for claim_calculation '
;

ALTER TABLE ccs.claim_calculation_audit ADD 
    CONSTRAINT pk_cca PRIMARY KEY (claim_calculation_audit_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE ccs.claim_calculation_audit ADD CONSTRAINT fk_cca_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES ccs.audit_transaction_type_code(audit_transaction_type_code)
;


