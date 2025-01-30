CREATE TABLE ccs.coverage_peril (
  coverage_peril_id       numeric(9)    NOT NULL,
  peril_code              varchar(32)   NOT NULL,
  commodity_coverage_code varchar(10)   NOT NULL,
  crop_commodity_id       numeric(9)    NOT NULL,
  effective_date          date          NOT NULL,
  expiry_date             date          NOT NULL,
  data_sync_trans_date    timestamp(0)  NOT NULL,
  create_user             varchar(64)   NOT NULL,
  create_date             timestamp(0)  NOT NULL,
  update_user             varchar(64)   NOT NULL,
  update_date             timestamp(0)  NOT NULL
) TABLESPACE pg_default
;



COMMENT ON TABLE ccs.coverage_peril IS 'Table contains all perils with the coverage and commodity association from CIRR COVERAGE PERILS'
;
COMMENT ON COLUMN ccs.coverage_peril.coverage_peril_id IS 'Coverage Peril Id is a unique Id of a coverage peril record from CIRR_COVERAGE_PERIL'
;
COMMENT ON COLUMN ccs.coverage_peril.peril_code IS 'Peril Code is the code of a peril from CIRR PERIL TYPES.PERIL TYPE CODE'
;
COMMENT ON COLUMN ccs.coverage_peril.commodity_coverage_code IS 'Commodity Coverage Code is a unique code for a coverage from CIRR CROP COVERAGE TYPE.CC TYPE'
;
COMMENT ON COLUMN ccs.coverage_peril.crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from CIRR CROP TYPES.CRPT ID'
;
COMMENT ON COLUMN ccs.coverage_peril.effective_date IS 'Effective Date is the date when the record was first active'
;
COMMENT ON COLUMN ccs.coverage_peril.expiry_date IS 'Expiry Date is the date when the record was last active'
;
COMMENT ON COLUMN ccs.coverage_peril.data_sync_trans_date IS 'Data Sync Trans Date is the date and time when the data has been updated in the source system. This prevents out of date updates'
;
COMMENT ON COLUMN ccs.coverage_peril.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN ccs.coverage_peril.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN ccs.coverage_peril.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN ccs.coverage_peril.update_date IS 'Update Date is the date when the record was updated last.'
;

CREATE INDEX ix_cp_ccc ON ccs.coverage_peril(commodity_coverage_code)
 TABLESPACE pg_default
;

CREATE INDEX ix_cp_cco ON ccs.coverage_peril(crop_commodity_id)
 TABLESPACE pg_default
;

CREATE INDEX ix_cp_pc ON ccs.coverage_peril(peril_code)
 TABLESPACE pg_default
;

ALTER TABLE ccs.coverage_peril ADD
    CONSTRAINT pk_cp PRIMARY KEY (coverage_peril_id) USING INDEX TABLESPACE pg_default
;

ALTER TABLE ccs.coverage_peril ADD CONSTRAINT fk_cp_ccc
    FOREIGN KEY (commodity_coverage_code)
    REFERENCES ccs.commodity_coverage_code(commodity_coverage_code)
;

ALTER TABLE ccs.coverage_peril ADD CONSTRAINT fk_cp_cco
    FOREIGN KEY (crop_commodity_id)
    REFERENCES ccs.crop_commodity(crop_commodity_id)
;

ALTER TABLE ccs.coverage_peril ADD CONSTRAINT fk_cp_pc
    FOREIGN KEY (peril_code)
    REFERENCES ccs.peril_code(peril_code)
;
