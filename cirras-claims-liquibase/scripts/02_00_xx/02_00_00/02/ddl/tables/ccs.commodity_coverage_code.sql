CREATE TABLE ccs.commodity_coverage_code (
  commodity_coverage_code varchar(10)   NOT NULL,
  description             varchar(100)  NOT NULL,
  effective_date          date          NOT NULL,
  expiry_date             date          NOT NULL,
  create_user             varchar(64)   NOT NULL,
  create_date             timestamp(0)  NOT NULL,
  update_user             varchar(64)   NOT NULL,
  update_date             timestamp(0)  NOT NULL,
  data_sync_trans_date    timestamp(0)  NOT NULL
) TABLESPACE pg_default
;



COMMENT ON TABLE ccs.commodity_coverage_code IS 'The table contains all crop coverage codes from CIRR_CROP_COVERAGE_TYPE'
;
COMMENT ON COLUMN ccs.commodity_coverage_code.commodity_coverage_code IS 'Commodity Coverage Code is a unique code for a coverage from CIRR_CROP_COVERAGE_TYPE.CC_TYPE'
;
COMMENT ON COLUMN ccs.commodity_coverage_code.description IS 'Description is the name of the claim status from CIRR_CLAIM_STATUSES.DESCRIPTION'
;
COMMENT ON COLUMN ccs.commodity_coverage_code.effective_date IS 'Effective Date is the date when the record was first active'
;
COMMENT ON COLUMN ccs.commodity_coverage_code.expiry_date IS 'Expiry Date is the date when the record was last active'
;
COMMENT ON COLUMN ccs.commodity_coverage_code.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN ccs.commodity_coverage_code.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN ccs.commodity_coverage_code.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN ccs.commodity_coverage_code.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON COLUMN ccs.commodity_coverage_code.data_sync_trans_date IS 'Data Sync Trans Date is the date and time when the data has been updated in the source system. This prevents out of date updates'
;

ALTER TABLE ccs.commodity_coverage_code ADD
    CONSTRAINT pk_ccc PRIMARY KEY (commodity_coverage_code) USING INDEX TABLESPACE pg_default
;
