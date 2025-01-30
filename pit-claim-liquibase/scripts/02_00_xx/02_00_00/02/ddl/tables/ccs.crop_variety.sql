CREATE TABLE ccs.crop_variety (
  crop_variety_id      numeric(9)   NOT NULL,
  crop_commodity_id    numeric(9)   NOT NULL,
  variety_name         varchar(50)  NOT NULL,
  effective_date       date         NOT NULL,
  expiry_date          date         NOT NULL,
  create_user          varchar(64)  NOT NULL,
  create_date          timestamp(0) NOT NULL,
  update_user          varchar(64)  NOT NULL,
  update_date          timestamp(0) NOT NULL,
  data_sync_trans_date timestamp(0) NOT NULL
) TABLESPACE pg_default
;



COMMENT ON TABLE ccs.crop_variety IS 'The table contains all varieties from CIRR_CROP_TYPES where crpt_crpt_id is not null'
;
COMMENT ON COLUMN ccs.crop_variety.crop_variety_id IS 'Crop Variety Id is a unique Id of a variety from CIRR_CROP_TYPES.CRPT_ID'
;
COMMENT ON COLUMN ccs.crop_variety.crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from CROP_COMMODITY'
;
COMMENT ON COLUMN ccs.crop_variety.effective_date IS 'Effective Date is the date when the record was first active'
;
COMMENT ON COLUMN ccs.crop_variety.expiry_date IS 'Expiry Date is the date when the record was last active'
;
COMMENT ON COLUMN ccs.crop_variety.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN ccs.crop_variety.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN ccs.crop_variety.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN ccs.crop_variety.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON COLUMN ccs.crop_variety.data_sync_trans_date IS 'Data Sync Trans Date is the date and time when the data has been updated in the source system. This prevents out of date updates'
;

CREATE INDEX ix_cva_cco ON ccs.crop_variety(crop_commodity_id)
 TABLESPACE pg_default
;

ALTER TABLE ccs.crop_variety ADD
    CONSTRAINT pk_cva PRIMARY KEY (crop_variety_id) USING INDEX TABLESPACE pg_default
;

ALTER TABLE ccs.crop_variety ADD CONSTRAINT fk_cva_cco
    FOREIGN KEY (crop_commodity_id)
    REFERENCES ccs.crop_commodity(crop_commodity_id)
;
