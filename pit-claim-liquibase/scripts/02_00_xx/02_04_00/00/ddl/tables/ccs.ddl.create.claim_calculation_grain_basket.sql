CREATE TABLE ccs.claim_calculation_grain_basket(
    claim_calculation_grain_basket_guid    varchar(32)       NOT NULL,
    CLAIM_CALCULATION_GUID               varchar(32)       NOT NULL,
    grain_basket_coverage_value            numeric(14, 4)    NOT NULL,
    grain_basket_deductible                numeric(3, 0)     NOT NULL,
    grain_basket_harvested_value           numeric(14, 4),
    quantity_total_coverage_value          numeric(14, 4),
    quantity_total_yield_value             numeric(14, 4),
    quantity_total_claim_amount            numeric(10, 2),
    quantity_total_yield_loss_indemnity    numeric(14, 4),
    total_yield_coverage_value             numeric(14, 4),
    total_yield_loss                       numeric(14, 4),
    revision_count                         numeric(10, 0)    NOT NULL,
    create_user                            varchar(64)       NOT NULL,
    create_date                            timestamp(0)      NOT NULL,
    update_user                            varchar(64)       NOT NULL,
    update_date                            timestamp(0)      NOT NULL
) TABLESPACE pg_default
;

COMMENT ON COLUMN ccs.claim_calculation_grain_basket.claim_calculation_grain_basket_guid IS 'Claim Calculation Grain Basket Guid is a unique key of the record'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket.CLAIM_CALCULATION_GUID IS 'Claim Calculation Guid is a unique key of a claims calculation'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket.grain_basket_coverage_value IS 'grain basket coverage value is the total coverage value from the purchase: ProductRsrc.CoverageDollars'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket.grain_basket_deductible IS 'grain basket deductible is the deductible level from the purchase: ProductRsrc.DeductibleLevel'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket.grain_basket_harvested_value IS 'grain basket harvested value is the value from Inventory: verified_yield_grain_basket.harvested_value'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket.quantity_total_coverage_value IS 'quantity total coverage valueis calculated: Sum of claim_calculation_grain_basket_product.coverage_value'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket.quantity_total_yield_value IS 'quantity total yield valueis calculated: Sum of claim_calculation_grain_basket_product.yield_value'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket.quantity_total_claim_amount IS 'quantity total claim amountis calculated: Sum of claim_calculation_grain_basket_product.quantity_claim_amount'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket.quantity_total_yield_loss_indemnity IS 'quantity total yield loss indemnityis calculated: sum((production_quarantee - assessed_yield - total_yield_to_count) x insurable_value) from claim_calculation_grain_basket_product'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket.total_yield_coverage_value IS 'total yield coverage valueis calculated: Sum of grain_basket_coverage_value and quantity_total_coverage_value'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket.total_yield_loss IS 'total yield lossis calculated: total_yield_coverage_value - quantity_total_yield_value'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket.revision_count IS 'Revision Count is the number of updates of the record'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE ccs.claim_calculation_grain_basket IS 'The table contains the claim calculation data specific of Grain Basket'
;

ALTER TABLE ccs.claim_calculation_grain_basket ADD 
    CONSTRAINT PK_CCGB PRIMARY KEY (claim_calculation_grain_basket_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE ccs.claim_calculation_grain_basket ADD 
    CONSTRAINT IX_CCGB_CC UNIQUE (CLAIM_CALCULATION_GUID) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE ccs.claim_calculation_grain_basket ADD CONSTRAINT FK_CCGB_CC 
    FOREIGN KEY (CLAIM_CALCULATION_GUID)
    REFERENCES ccs.CLAIM_CALCULATION(CLAIM_CALCULATION_GUID)
;
