CREATE TABLE ccs.claim_calculation_grain_basket_product(
    claim_calc_grain_basket_product_guid    varchar(32)       NOT NULL,
    CLAIM_CALCULATION_GUID                varchar(32)       NOT NULL,
    CROP_COMMODITY_ID                     numeric(9, 0)     NOT NULL,
    hundred_percent_insurable_value         numeric(14, 4)    NOT NULL,
    insurable_value                         numeric(14, 4)    NOT NULL,
    production_guarantee                    numeric(14, 4)    NOT NULL,
    coverage_value                          numeric(14, 4)    NOT NULL,
    total_yield_to_count                    numeric(14, 4),
    assessed_yield                          numeric(14, 4),
    quantity_claim_amount                   numeric(10, 2),
    yield_value                             numeric(14, 4),
    revision_count                          numeric(10, 0)    NOT NULL,
    create_user                             varchar(64)       NOT NULL,
    create_date                             timestamp(0)      NOT NULL,
    update_user                             varchar(64)       NOT NULL,
    update_date                             timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN ccs.claim_calculation_grain_basket_product.claim_calc_grain_basket_product_guid IS 'Claim Calc Grain Basket Product Guid is a unique key of the record'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket_product.CLAIM_CALCULATION_GUID IS 'Claim Calculation Guid is a unique key of a claims calculation'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket_product.CROP_COMMODITY_ID IS 'Crop Commodity Id is a unique Id of a commodity from CIRR CROP TYPES.CRPT ID'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket_product.hundred_percent_insurable_value IS 'hundred percent insurable value is the 100% IV of the quantity product: q_insurable_value'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket_product.insurable_value IS 'insurable value is the selected IV from the quantity product: q_selected_insurable_value'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket_product.production_guarantee IS 'production guarantee is the production guarantee from the quantity product: q_production_guarantee'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket_product.coverage_value IS 'coverage value is the coverage dollars from the quantity product: coverage_amount'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket_product.total_yield_to_count IS 'total yield to count is the total yield to count from verified yield: verified_yield_summary.yield_to_count'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket_product.assessed_yield IS 'assessed yield is the total assessed yield from the approved quantity claim: claim_calculation_grain_quantity_detail.assessed_yield'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket_product.quantity_claim_amount IS 'quantity claim amount is the total claim amount from the approved quantity claim: claim_calculation.total_claim_amount'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket_product.yield_value IS 'yield value is calculated: total_yield_to_count x hundred_percent_insurable_value'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket_product.revision_count IS 'Revision Count is the number of updates of the record'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket_product.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket_product.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket_product.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket_product.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE ccs.claim_calculation_grain_basket_product IS 'The table contains the claim calculation data specific of Grain Basket Quantity Products'
;

CREATE INDEX IX_CCGBP_CC ON ccs.claim_calculation_grain_basket_product(CLAIM_CALCULATION_GUID)
 TABLESPACE pg_default
;
CREATE INDEX IX_CCGBP_CCO ON ccs.claim_calculation_grain_basket_product(CROP_COMMODITY_ID)
 TABLESPACE pg_default
;
ALTER TABLE ccs.claim_calculation_grain_basket_product ADD 
    CONSTRAINT PK_CCGBP PRIMARY KEY (claim_calc_grain_basket_product_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE ccs.claim_calculation_grain_basket_product ADD 
    CONSTRAINT UK_CCGBP UNIQUE (CLAIM_CALCULATION_GUID, CROP_COMMODITY_ID) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE ccs.claim_calculation_grain_basket_product ADD CONSTRAINT FK_CCGBP_CC 
    FOREIGN KEY (CLAIM_CALCULATION_GUID)
    REFERENCES ccs.CLAIM_CALCULATION(CLAIM_CALCULATION_GUID)
;

ALTER TABLE ccs.claim_calculation_grain_basket_product ADD CONSTRAINT FK_CCGBP_CCO 
    FOREIGN KEY (CROP_COMMODITY_ID)
    REFERENCES ccs.CROP_COMMODITY(CROP_COMMODITY_ID)
;
