CREATE TABLE CCS.CLAIM_CALCULATION_GRAIN_QUANTITY_DETAIL(
    claim_calc_grain_quantity_detail_guid      varchar(32)       NOT NULL,
    claim_calculation_guid                     varchar(32)       NOT NULL,
    insured_acres                              numeric(14, 4)    NOT NULL,
    probable_yield                             numeric(14, 4)    NOT NULL,
    deductible                                 numeric(3, 0)     NOT NULL,
    production_guarantee_weight                numeric(10, 4)    NOT NULL,
    insurable_value                            numeric(14, 4)    NOT NULL,
    coverage_value                             numeric(14, 4)    NOT NULL,
    total_yield_to_count                       numeric(14, 4),
    assessed_yield                             numeric(14, 4),
    early_est_deemed_yield_value               numeric(14, 4),
    damaged_acres                              numeric(14, 4),
    seeded_acres                               numeric(14, 4),
    fifty_percent_production_guarantee         numeric(14, 4),
    calc_early_est_yield                       numeric(14, 4),
    insp_early_est_yield                       numeric(14, 4),
    yield_value                                numeric(14, 4)    NOT NULL,
    yield_value_with_early_est_deemed_yield    numeric(14, 4)    NOT NULL,
    revision_count                             numeric(10, 0)    NOT NULL,
    create_user                                varchar(64)       NOT NULL,
    create_date                                timestamp(0)      NOT NULL,
    update_user                                varchar(64)       NOT NULL,
    update_date                                timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_QUANTITY_DETAIL.claim_calc_grain_quantity_detail_guid IS 'Claim Calc Grain Quantity Details Guid is a unique key of the record
'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_QUANTITY_DETAIL.claim_calculation_guid IS 'Claim Calculation Guid is a unique key of a claims calculation'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_QUANTITY_DETAIL.insured_acres IS 'Insured Acres is taken from CIRR_INSURABLE_CROP_UNITS.INSC_TOTAL_INSURED_ACRES'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_QUANTITY_DETAIL.probable_yield IS 'Probable Yield is taken from CIRR_INSURABLE_CROP_UNITS.PROBABLE_YIELD'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_QUANTITY_DETAIL.deductible IS 'Deductible is taken from CIRR_DEDUCTIBLE_RATES.DEDUCTIBLE_LEVEL'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_QUANTITY_DETAIL.production_guarantee_weight IS 'Production Guarantee Weight is taken from CIRR_INSRNC_PRDCT_PRCHSES.Q_PRODUCTION_GUARANTEE'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_QUANTITY_DETAIL.insurable_value IS 'Insurable Value is the IV in the product purchase (cirr_insrnc_prdct_prchses.q_insurable_value)'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_QUANTITY_DETAIL.coverage_value IS 'Coverage Value is taken from CIRR_INSRNC_PRDCT_PRCHSES.Q_COVERAGE_DOLLARS'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_QUANTITY_DETAIL.total_yield_to_count IS 'Total Yield To Count is taken from CUWS.VERIFIED_YIELD_SUMMARY.YIELD_TO_COUNT'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_QUANTITY_DETAIL.assessed_yield IS 'Assessed Yield is taken from CUWS.VERIFIED_YIELD_SUMMARY.ASSESSED_YIELD'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_QUANTITY_DETAIL.early_est_deemed_yield_value IS 'Early Establishment Deemed Yield Value is user entered'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_QUANTITY_DETAIL.damaged_acres IS 'Damaged Acres is user entered'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_QUANTITY_DETAIL.seeded_acres IS 'Seeded Acres is user entered'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_QUANTITY_DETAIL.fifty_percent_production_guarantee IS 'Fifty Percent Production Guarantee is calculated as Production Guarantee Weight * 50%'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_QUANTITY_DETAIL.calc_early_est_yield IS 'Calculated Early Estabilshment Yield  is calculated as: 50% Production Guarantee Weight * (Damaged Acres / Acres Seeded)'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_QUANTITY_DETAIL.insp_early_est_yield IS 'Inspected Early Establishment Yield is user entered'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_QUANTITY_DETAIL.yield_value IS 'Yield Value is calculated as: Total Yield To Count * Insurable Value '
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_QUANTITY_DETAIL.yield_value_with_early_est_deemed_yield IS 'Yield Value With Early Establishment Deemed Yield is calculated as: Yield Value + Early Establishment Deemed Yield Value '
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_QUANTITY_DETAIL.revision_count IS 'Revision Count is the number of updates of the record'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_QUANTITY_DETAIL.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_QUANTITY_DETAIL.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN CCS.CLAIM_CALCULATION_GRAIN_QUANTITY_DETAIL.update_user IS 'Update User is the user id of the user that updated the record last.'
;
COMMENT ON TABLE CCS.CLAIM_CALCULATION_GRAIN_QUANTITY_DETAIL IS 'The table contains the claim calculation data specific to Grain Quantity Loss for pedigreed an non-pedigreed commodities'
;

CREATE INDEX IX_CCGQD_CC ON CCS.CLAIM_CALCULATION_GRAIN_QUANTITY_DETAIL(CLAIM_CALCULATION_GUID)
 TABLESPACE pg_default
;
ALTER TABLE CCS.CLAIM_CALCULATION_GRAIN_QUANTITY_DETAIL ADD 
    CONSTRAINT PK_CCGQD PRIMARY KEY (claim_calc_grain_quantity_detail_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE CCS.CLAIM_CALCULATION_GRAIN_QUANTITY_DETAIL ADD CONSTRAINT FK_CCGQD_CC  
    FOREIGN KEY (CLAIM_CALCULATION_GUID)
    REFERENCES CCS.CLAIM_CALCULATION(CLAIM_CALCULATION_GUID)
;


