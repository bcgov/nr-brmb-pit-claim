
ALTER TABLE CCS.CLAIM_CALCULATION
ADD claim_calc_grain_quantity_guid varchar(32) NOT NULL;

COMMENT ON COLUMN CCS.CLAIM_CALCULATION.claim_calc_grain_quantity_guid IS 'Claim Calculation Grain Quantity Guid is a unique key of the record'
;

ALTER TABLE CCS.CLAIM_CALCULATION ADD CONSTRAINT FK_CCGQ_CC 
    FOREIGN KEY (claim_calc_grain_quantity_guid)
    REFERENCES CCS.CLAIM_CALCULATION_GRAIN_QUANTITY(claim_calc_grain_quantity_guid)
;