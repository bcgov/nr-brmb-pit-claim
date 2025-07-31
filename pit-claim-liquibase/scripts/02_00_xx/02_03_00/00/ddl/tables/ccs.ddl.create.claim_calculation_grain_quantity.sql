CREATE TABLE CCS.CLAIM_CALCULATION_GRAIN_QUANTITY(
    claim_calc_grain_quantity_guid    varchar(32)       NOT NULL,
    total_coverage_value              numeric(14, 4)    NOT NULL,
    production_guarantee_amount       numeric(14, 4)    NOT NULL,
    total_yield_loss_value            numeric(14, 4)    NOT NULL,
    reseed_claim                      numeric(14, 4),
    max_claim_payable                 numeric(14, 4)    NOT NULL,
    advanced_claim                    numeric(14, 4),
    quantity_loss_claim               numeric(14, 4)    NOT NULL,
    revision_count                    numeric(10, 0)    NOT NULL,
    create_user                       varchar(64)       NOT NULL,
    create_date                       timestamp(0)      NOT NULL,
    update_user                       varchar(64)       NOT NULL,
    update_date                       timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_QUANTITY.claim_calc_grain_quantity_guid IS 'Claim Calculation Grain Quantity Guid is a unique key of the record'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_QUANTITY.total_coverage_value IS 'Total Coverage Value is calculated as sum of pedigreed and non pedigreed coverage values'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_QUANTITY.production_guarantee_amount IS 'Production Guarantee Amount is calculated as: sum of (Production Guarantee - Assessed Yield) * Insurable Value for pedigreed and non-pedigreed commodities'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_QUANTITY.total_yield_loss_value IS 'Total Yield Loss Value is calculated as: (Production Guarantee Amount ) - sum of (Yield Value + Early Establishment ) for pedigreed and non-pedigreed commodities '
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_QUANTITY.reseed_claim IS 'Reseed Claim is user entered'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_QUANTITY.max_claim_payable IS 'Max Claim Payable is calculated as: Total Pedigreed and Non-Pedigreed Seeds Coverage Value - Reseed Claim'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_QUANTITY.advanced_claim IS 'Advanced Claim is user entered'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_QUANTITY.quantity_loss_claim IS 'Quantity Loss Claim is calculated as: lesser of Maximum Claim Payable or Total Quantity Loss'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_QUANTITY.revision_count IS 'Revision Count is the number of updates of the record'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_QUANTITY.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_QUANTITY.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_QUANTITY.update_user IS 'Update User is the user id of the user that updated the record last.'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_QUANTITY.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE CCS.CLAIM_CALCULATION_GRAIN_QUANTITY IS 'The table contains Grain Quantity Loss claim calculation data combined for pedigreed an non-pedigreed commodities'
;

ALTER TABLE CCS.CLAIM_CALCULATION_GRAIN_QUANTITY ADD 
    CONSTRAINT PK_CCGQ PRIMARY KEY (claim_calc_grain_quantity_guid) USING INDEX TABLESPACE pg_default 
;

