CREATE TABLE ccs.declared_yield_contract_commodity_berries_sync(
    declared_yield_contract_commodity_berries_sync_guid    varchar(32)       NOT NULL,
    crop_commodity_id                                      numeric(9, 0)     NOT NULL,
    contract_id                                            numeric(9, 0)     NOT NULL,
    crop_year                                              numeric(4, 0)     NOT NULL,
    declared_yield_contract_commodity_berries_guid         varchar(32)       NOT NULL,
    total_production                                       numeric(14, 4),
    total_production_override                              numeric(14, 4),
    total_sold_shipped_yield                               numeric(14, 4),
    total_sales_yield                                      numeric(14, 4),
    total_abandonment_yield                                numeric(14, 4),
    data_sync_trans_date                                   timestamp(0)      NOT NULL,
    create_user                                            varchar(64)       NOT NULL,
    create_date                                            timestamp(0)      NOT NULL,
    update_user                                            varchar(64)       NOT NULL,
    update_date                                            timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN ccs.declared_yield_contract_commodity_berries_sync.declared_yield_contract_commodity_berries_sync_guid IS 'Declared Yield Contract Commodity Berries Sync Guid  is the primary key used to identify the record'
;
COMMENT ON COLUMN ccs.declared_yield_contract_commodity_berries_sync.crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from CIRR CROP TYPES.CRPT ID'
;
COMMENT ON COLUMN ccs.declared_yield_contract_commodity_berries_sync.contract_id IS 'Contract Id is a unique identifier of a claim from CIRR_CONTRACT_NUMBERS.CN_ID'
;
COMMENT ON COLUMN ccs.declared_yield_contract_commodity_berries_sync.crop_year IS 'Crop Year is the year of the policy from cirr_insurance_policies.crop_year'
;
COMMENT ON COLUMN ccs.declared_yield_contract_commodity_berries_sync.declared_yield_contract_commodity_berries_guid IS 'Declared Yield Field Commodity Berries Guid  is the primary key of the source table used to identify the record'
;
COMMENT ON COLUMN ccs.declared_yield_contract_commodity_berries_sync.total_production IS 'Total Production is the calculated total pounds of yield by contract and commodity'
;
COMMENT ON COLUMN ccs.declared_yield_contract_commodity_berries_sync.total_production_override IS 'Total Production Override is the manually entered total pounds of yield by contract and commodity'
;
COMMENT ON COLUMN ccs.declared_yield_contract_commodity_berries_sync.total_sold_shipped_yield IS 'Total Sold Shipped Yield is the calculated total pounds of yield sold and shipped from field variety'
;
COMMENT ON COLUMN ccs.declared_yield_contract_commodity_berries_sync.total_sales_yield IS 'Total Sales Yield is the calculated total pounds of yield private and direct sold from field variety'
;
COMMENT ON COLUMN ccs.declared_yield_contract_commodity_berries_sync.total_abandonment_yield IS 'Total Abandonment Yield is the calculated total pounds of abandonment yield from field variety'
;
COMMENT ON COLUMN ccs.declared_yield_contract_commodity_berries_sync.data_sync_trans_date IS 'Data Sync Trans Date is the date and time when the data has been updated in the source system. This prevents out of date updates'
;
COMMENT ON COLUMN ccs.declared_yield_contract_commodity_berries_sync.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN ccs.declared_yield_contract_commodity_berries_sync.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN ccs.declared_yield_contract_commodity_berries_sync.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN ccs.declared_yield_contract_commodity_berries_sync.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE ccs.declared_yield_contract_commodity_berries_sync IS 'The table contains synchronized dop berries data from cuws'
;

CREATE INDEX "IX_DYCCBS_CCO" ON ccs.declared_yield_contract_commodity_berries_sync(crop_commodity_id)
 TABLESPACE pg_default
;
ALTER TABLE ccs.declared_yield_contract_commodity_berries_sync ADD 
    CONSTRAINT PK_DYCCBS PRIMARY KEY (declared_yield_contract_commodity_berries_sync_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE ccs.declared_yield_contract_commodity_berries_sync ADD 
    CONSTRAINT UK_DYCCBS UNIQUE (crop_commodity_id, contract_id, crop_year) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE ccs.declared_yield_contract_commodity_berries_sync ADD CONSTRAINT FK_DYCCBS_CCO 
    FOREIGN KEY (crop_commodity_id)
    REFERENCES ccs.CROP_COMMODITY(CROP_COMMODITY_ID)
;


