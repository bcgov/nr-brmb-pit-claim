CREATE TABLE ccs.claim (
  col_id                      numeric(9)    NOT NULL,
  claim_number                numeric(9)    NOT NULL,
  claim_status_code           varchar(16)   NOT NULL,
  submitted_by_userid         varchar(32),
  submitted_by_name           varchar(100),
  submitted_by_date           date,
  recommended_by_userid       varchar(32),
  recommended_by_name         varchar(100),
  recommended_by_date         date,
  approved_by_userid          varchar(32),
  approved_by_name            varchar(100),
  approved_by_date            date,
  ipp_id                      numeric(9)    NOT NULL,
  commodity_coverage_code     varchar(10)   NOT NULL,
  crop_commodity_id           numeric(9)    NOT NULL,
  ipl_id                      numeric(9)    NOT NULL,
  policy_number               varchar(20)   NOT NULL,
  insurance_plan_id           numeric(9)    NOT NULL,
  crop_year                   numeric(4)    NOT NULL,
  contract_id                 numeric(9)    NOT NULL,
  ig_id                       numeric(9)    NOT NULL,
  grower_name                 varchar(100)  NOT NULL,
  claim_data_sync_trans_date  timestamp(0)  NOT NULL,
  policy_data_sync_trans_date timestamp(0)  NOT NULL,
  grower_data_sync_trans_date timestamp(0)  NOT NULL,
  create_user                 varchar(64)   NOT NULL,
  create_date                 timestamp(0)  NOT NULL,
  update_user                 varchar(64)   NOT NULL,
  update_date                 timestamp(0)  NOT NULL,
  has_cheque_req_ind          varchar(1)    NOT NULL
) TABLESPACE pg_default
;



COMMENT ON TABLE ccs.claim IS 'The table contains all claims and related data from CIRR_CLAIM_OF_LOSSES and it''s used for synchronizing data'
;
COMMENT ON COLUMN ccs.claim.col_id IS 'Col Id is a unique number identifying a record from CIRR_CLAIM_OF_LOSSES.COL_ID'
;
COMMENT ON COLUMN ccs.claim.claim_number IS 'Claim Number is the number of the claim from CIRR_CLAIM_OF_LOSSES.CLAIM_NUMBER'
;
COMMENT ON COLUMN ccs.claim.claim_status_code IS 'Claim Status Code is the status of the claim from CIRR_CLAIM_OF_LOSSES.CLSTA_CLAIM_STATUS_CODE'
;
COMMENT ON COLUMN ccs.claim.submitted_by_userid IS 'Submitted By UserID is the user id of the user that monitors the claim from CIRR_CLAIM_OF_LOSSES.USR_USR_ID_MONITORED_BY'
;
COMMENT ON COLUMN ccs.claim.submitted_by_name IS 'Submitted By Name is the name of the user that monitors the claim from CIRR_CLAIM_OF_LOSSES.USR_USR_ID_MONITORED_BY'
;
COMMENT ON COLUMN ccs.claim.submitted_by_date IS 'Submitted By Date is the date when the claim was submitted from CIRR_CLAIM_OF_LOSSES.MONITORED_DATE'
;
COMMENT ON COLUMN ccs.claim.recommended_by_userid IS 'Recommended By UserID is the user id of the user that recommended the claim from CIRR_CLAIM_OF_LOSSES.USR_USR_ID_RECOMMENDED_BY'
;
COMMENT ON COLUMN ccs.claim.recommended_by_name IS 'Recommended By Name is the name of the user that recommended the claim from CIRR_CLAIM_OF_LOSSES.USR_USR_ID_RECOMMENDED_BY'
;
COMMENT ON COLUMN ccs.claim.recommended_by_date IS 'Recommended By Date is the date when the claim was recommended from CIRR_CLAIM_OF_LOSSES.RECOMMENDED_DATE'
;
COMMENT ON COLUMN ccs.claim.approved_by_userid IS 'Approved By UserID is the user id of the user that approved the claim from CIRR_CLAIM_OF_LOSSES.USR_USR_ID_APPROVED_BY'
;
COMMENT ON COLUMN ccs.claim.approved_by_name IS 'Approved By Name is the name of the user that approved the claim from CIRR_CLAIM_OF_LOSSES.USR_USR_ID_APPROVED_BY'
;
COMMENT ON COLUMN ccs.claim.approved_by_date IS 'Approved By Date is the date when the claim was approved from CIRR_CLAIM_OF_LOSSES.APPROVED_DATE'
;
COMMENT ON COLUMN ccs.claim.ipp_id IS 'IPP ID is a surrogate unique identifier generated for an INSRNC PRDCT PRCHSE from CIRR_INSRNC_PRDCT_PRCHSES.IPP_ID'
;
COMMENT ON COLUMN ccs.claim.commodity_coverage_code IS 'Commodity Coverage Code is the coverage of the notice of loss from cirr_insrnc_prdct_prchses.cc_cc_id'
;
COMMENT ON COLUMN ccs.claim.crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from CIRR_CROP_TYPES.CRPT_ID'
;
COMMENT ON COLUMN ccs.claim.ipl_id IS 'Ipl Id is a unique key of a policy from cirr_insrnc_prdct_prchses.ipl_ipl_id '
;
COMMENT ON COLUMN ccs.claim.policy_number IS 'Policy Number is the number of the policy from CIRR_CONTRACT_NUMBERS.CONTRACT_NUMBER-CIRR_INSURANCE_POLICIES.CROP_YEAR'
;
COMMENT ON COLUMN ccs.claim.insurance_plan_id IS 'Insurance Plan Id is the insurance plan of the policy from cirr_insurance_policies.ip_ip_id'
;
COMMENT ON COLUMN ccs.claim.crop_year IS 'Crop Year is the year of the policy from cirr_insurance_policies.crop_year'
;
COMMENT ON COLUMN ccs.claim.contract_id IS 'Contract Id is a unique identifier of a claim from CIRR_CONTRACT_NUMBERS.CN_ID'
;
COMMENT ON COLUMN ccs.claim.ig_id IS 'Ig Id is a unique key of a grower from cirr_insurance_policies.ig_ig_id'
;
COMMENT ON COLUMN ccs.claim.grower_name IS 'Grower Name from CIRR_INSURED_GROWERS.GROWER_LEGAL_NAME'
;
COMMENT ON COLUMN ccs.claim.claim_data_sync_trans_date IS 'Claim Data Sync Trans Date is the date and time when the data has been updated in the source system. This prevents out of date updates'
;
COMMENT ON COLUMN ccs.claim.policy_data_sync_trans_date IS 'Policy Data Sync Trans Date is the date and time when the data has been updated in the source system. This prevents out of date updates'
;
COMMENT ON COLUMN ccs.claim.grower_data_sync_trans_date IS 'Grower Data Sync Trans Date is the date and time when the data has been updated in the source system. This prevents out of date updates'
;
COMMENT ON COLUMN ccs.claim.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN ccs.claim.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN ccs.claim.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN ccs.claim.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON COLUMN ccs.claim.has_cheque_req_ind IS 'HAS CHEQUE REQ IND indicates whether the claim is on one or more cheque requisitions (Y) or No (N).'
;

CREATE INDEX ix_clmnbr ON ccs.claim(claim_number)
 TABLESPACE pg_default
;

CREATE INDEX ix_ig ON ccs.claim(ig_id)
 TABLESPACE pg_default
;

CREATE INDEX ix_ipl ON ccs.claim(ipl_id)
 TABLESPACE pg_default
;

CREATE INDEX ix_ipp ON ccs.claim(ipp_id)
 TABLESPACE pg_default
;

ALTER TABLE ccs.claim ADD
    CONSTRAINT pk_c primary key (col_id) USING INDEX TABLESPACE pg_default
;
