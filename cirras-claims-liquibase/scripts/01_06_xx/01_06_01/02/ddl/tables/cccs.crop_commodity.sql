CREATE TABLE cccs.crop_commodity (
  crop_commodity_id    numeric(9)   NOT NULL,
  commodity_name       varchar(50)  NOT NULL,
  effective_date       date         NOT NULL,
  expiry_date          date         NOT NULL,
  create_user          varchar(64)  NOT NULL,
  create_date          timestamp(0) NOT NULL,
  update_user          varchar(64)  NOT NULL,
  update_date          timestamp(0) NOT NULL,
  data_sync_trans_date timestamp(0) NOT NULL
) TABLESPACE pg_default
;



COMMENT ON TABLE cccs.crop_commodity IS 'The table contains all commodities that are product insurable. Data is taken from CIRR_CROP_TYPES where crpt_crpt_id is null and product_insurable_flag = Y'
;
COMMENT ON COLUMN cccs.crop_commodity.crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from CIRR_CROP_TYPES.CRPT_ID'
;
COMMENT ON COLUMN cccs.crop_commodity.commodity_name IS 'Commodity Name is the name of the commodity from CIRR_CROP_TYPES.NAME'
;
COMMENT ON COLUMN cccs.crop_commodity.effective_date IS 'Effective Date is the date when the record was first active'
;
COMMENT ON COLUMN cccs.crop_commodity.expiry_date IS 'Expiry Date is the date when the record was last active'
;
COMMENT ON COLUMN cccs.crop_commodity.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cccs.crop_commodity.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cccs.crop_commodity.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cccs.crop_commodity.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON COLUMN cccs.crop_commodity.data_sync_trans_date IS 'Data Sync Trans Date is the date and time when the data has been updated in the source system. This prevents out of date updates'
;

ALTER TABLE cccs.crop_commodity ADD
    CONSTRAINT pk_cco PRIMARY KEY (crop_commodity_id) USING INDEX TABLESPACE pg_default
;
