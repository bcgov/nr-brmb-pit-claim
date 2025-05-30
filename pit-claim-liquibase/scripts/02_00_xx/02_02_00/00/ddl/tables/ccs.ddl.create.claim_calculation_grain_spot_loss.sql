CREATE TABLE CCS.CLAIM_CALCULATION_GRAIN_SPOT_LOSS(
    claim_calc_grain_spot_loss_guid    varchar(32)       NOT NULL,
    claim_calculation_guid             varchar(32)       NOT NULL,
    insured_acres                      numeric(14, 4)    NOT NULL,
    coverage_amt_per_acre              numeric(14, 4)    NOT NULL,
    coverage_value                     numeric(14, 4)    NOT NULL,
    adjusted_acres                     numeric(14, 4),
    percent_yield_reduction            numeric(5, 2),
    eligible_yield_reduction           numeric(14, 4),
    spot_loss_reduction_value          numeric(14, 4),
    deductible                         numeric(3, 0)     NOT NULL,
    revision_count                     numeric(10, 0),
    create_user                        varchar(64)       NOT NULL,
    create_date                        timestamp(0)      NOT NULL,
    update_user                        varchar(64)       NOT NULL,
    update_date                        timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_SPOT_LOSS.claim_calc_grain_spot_loss_guid IS 'Claim Calculation Grain Spot Loss Guid is a unique key of the record'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_SPOT_LOSS.claim_calculation_guid IS 'Claim Calculation Guid is a unique key of a claims calculation'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_SPOT_LOSS.insured_acres IS 'Insured Acres are the total acres from the purchase: cirr_insrnc_prdct_prchses.s_insured_acres'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_SPOT_LOSS.coverage_amt_per_acre IS 'Coverage Amount per Acre is calculated as the sum of cirr_grain_spot_loss_purchases.coverage_dollars for the spot loss purchase'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_SPOT_LOSS.coverage_value IS 'Coverage Value is a calculated value: insured_acres * coverage_amt_per_acre'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_SPOT_LOSS.adjusted_acres IS 'Adjusted Acres is a user entered value'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_SPOT_LOSS.percent_yield_reduction IS 'Percent Yield Reduction is a user entered value'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_SPOT_LOSS.eligible_yield_reduction IS 'Eligible Yield Reduction is calculated as (Adjusted Acres x Percent Yield Reduction) '
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_SPOT_LOSS.spot_loss_reduction_value IS 'Spot Loss Reduction Value is calculated as ( Coverage Dollars per Acre x Eligible Yield Reduction)'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_SPOT_LOSS.deductible IS 'Deductible is the deductible expressed as a percent, always set to 5%'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_SPOT_LOSS.revision_count IS 'Revision Count is the number of updates of the record'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_SPOT_LOSS.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_SPOT_LOSS.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_SPOT_LOSS.update_user IS 'Update User is the user id of the user that updated the record last.'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_SPOT_LOSS.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE CCS.CLAIM_CALCULATION_GRAIN_SPOT_LOSS IS 'The table contains the claim calculation data specific of Grain Spot Loss'
;

CREATE INDEX IX_CCGSL_CC ON CCS.CLAIM_CALCULATION_GRAIN_SPOT_LOSS(CLAIM_CALCULATION_GUID)
 TABLESPACE pg_default
;
ALTER TABLE CCS.CLAIM_CALCULATION_GRAIN_SPOT_LOSS ADD 
    CONSTRAINT PK_CCGSL PRIMARY KEY (claim_calc_grain_spot_loss_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE CCS.CLAIM_CALCULATION_GRAIN_SPOT_LOSS ADD CONSTRAINT FK_CCGSL_CC 
    FOREIGN KEY (CLAIM_CALCULATION_GUID)
    REFERENCES CCS.CLAIM_CALCULATION(CLAIM_CALCULATION_GUID)
;


