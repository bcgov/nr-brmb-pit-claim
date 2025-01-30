CREATE TABLE ccs.peril_code (
  peril_code           varchar(32)  NOT NULL,
  description          varchar(256) NOT NULL,
  effective_date       date         NOT NULL,
  expiry_date          date         NOT NULL,
  create_user          varchar(64)  NOT NULL,
  create_date          timestamp(0) NOT NULL,
  update_user          varchar(64)  NOT NULL,
  update_date          timestamp(0) NOT NULL,
  data_sync_trans_date timestamp(0) NOT NULL
) TABLESPACE pg_default
;



COMMENT ON TABLE ccs.peril_code IS 'The table contains all perils from CIRR_PERIL_TYPES'
;
COMMENT ON COLUMN ccs.peril_code.peril_code IS 'Peril Code is the code of a peril from CIRR_PERIL_TYPES.PERIL_TYPE_CODE'
;
COMMENT ON COLUMN ccs.peril_code.description IS 'Description is the name of the peril from CIRR_PERIL_TYPES.DESCRIPTION'
;
COMMENT ON COLUMN ccs.peril_code.effective_date IS 'Effective Date is the date when the record was first active'
;
COMMENT ON COLUMN ccs.peril_code.expiry_date IS 'Expiry Date is the date when the record was last active'
;
COMMENT ON COLUMN ccs.peril_code.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN ccs.peril_code.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN ccs.peril_code.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN ccs.peril_code.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON COLUMN ccs.peril_code.data_sync_trans_date IS 'Data Sync Trans Date is the date and time when the data has been updated in the source system. This prevents out of date updates'
;

ALTER TABLE ccs.peril_code ADD
    CONSTRAINT pk_pc PRIMARY KEY (peril_code) USING INDEX TABLESPACE pg_default
;
