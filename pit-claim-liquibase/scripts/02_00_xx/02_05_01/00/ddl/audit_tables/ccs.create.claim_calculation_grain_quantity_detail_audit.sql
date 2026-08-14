CREATE TABLE ccs.claim_calculation_grain_quantity_detail_audit(
    claim_calculation_grain_quantity_detail_audit_id    numeric(9, 0)     NOT NULL,
    audit_transaction_type_code                         varchar(10)       NOT NULL,
    audit_time_stamp                                    timestamp(6)      NOT NULL,
    claim_calc_grain_quantity_detail_guid               varchar(32)       NOT NULL,
    claim_calculation_guid                              varchar(32),
    insured_acres                                       numeric(14, 4),
    probable_yield                                      numeric(14, 4),
    deductible                                          numeric(3, 0),
    production_guarantee_weight                         numeric(10, 4),
    insurable_value                                     numeric(14, 4),
    coverage_value                                      numeric(14, 4),
    total_yield_to_count                                numeric(14, 4),
    assessed_yield                                      numeric(14, 4),
    early_est_deemed_yield_value                        numeric(14, 4),
    damaged_acres                                       numeric(14, 4),
    seeded_acres                                        numeric(14, 4),
    fifty_percent_production_guarantee                  numeric(14, 4),
    calc_early_est_yield                                numeric(14, 4),
    insp_early_est_yield                                numeric(14, 4),
    yield_value                                         numeric(14, 4),
    yield_value_with_early_est_deemed_yield             numeric(14, 4),
    revision_count                                      numeric(10, 0),
    create_user                                         varchar(64)       NOT NULL,
    create_date                                         timestamp(0)      NOT NULL,
    update_user                                         varchar(64)       NOT NULL,
    update_date                                         timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_detail_audit.claim_calculation_grain_quantity_detail_audit_id IS 'Claim Calculation Grain Quantity Detail Audit Id is the ID of the Claim Calculation Grain Quantity Detail Audit table, comes from a sequence.'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_detail_audit.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_detail_audit.audit_time_stamp IS 'Audit Time Stamp denotes when the record was inserted in the audit table.'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_detail_audit.claim_calc_grain_quantity_detail_guid IS 'Claim Calc Grain Quantity Details Guid is a unique key of the record'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_detail_audit.claim_calculation_guid IS 'Claim Calculation Guid is a unique key of a claims calculation'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_detail_audit.insured_acres IS 'Insured Acres is taken from CIRR_INSURABLE_CROP_UNITS.INSC_TOTAL_INSURED_ACRES'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_detail_audit.probable_yield IS 'Probable Yield is taken from CIRR_INSURABLE_CROP_UNITS.PROBABLE_YIELD'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_detail_audit.deductible IS 'Deductible is taken from CIRR_DEDUCTIBLE_RATES.DEDUCTIBLE_LEVEL'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_detail_audit.production_guarantee_weight IS 'Production Guarantee Weight is taken from CIRR_INSRNC_PRDCT_PRCHSES.Q_PRODUCTION_GUARANTEE'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_detail_audit.insurable_value IS 'Insurable Value is the IV in the product purchase (cirr_insrnc_prdct_prchses.q_insurable_value)'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_detail_audit.coverage_value IS 'Coverage Value is taken from CIRR_INSRNC_PRDCT_PRCHSES.Q_COVERAGE_DOLLARS'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_detail_audit.total_yield_to_count IS 'Total Yield To Count is taken from CUWS.VERIFIED_YIELD_SUMMARY.YIELD_TO_COUNT'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_detail_audit.assessed_yield IS 'Assessed Yield is taken from CUWS.VERIFIED_YIELD_SUMMARY.ASSESSED_YIELD'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_detail_audit.early_est_deemed_yield_value IS 'Early Establishment Deemed Yield Value is user entered'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_detail_audit.damaged_acres IS 'Damaged Acres is user entered'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_detail_audit.seeded_acres IS 'Seeded Acres is user entered'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_detail_audit.fifty_percent_production_guarantee IS 'Fifty Percent Production Guarantee is calculated as Production Guarantee Weight * 50%'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_detail_audit.calc_early_est_yield IS 'Calculated Early Estabilshment Yield  is calculated as: 50% Production Guarantee Weight * (Damaged Acres / Acres Seeded)'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_detail_audit.insp_early_est_yield IS 'Inspected Early Establishment Yield is user entered'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_detail_audit.yield_value IS 'Yield Value is calculated as: Total Yield To Count * Insurable Value '
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_detail_audit.yield_value_with_early_est_deemed_yield IS 'Yield Value With Early Establishment Deemed Yield is calculated as: Yield Value + Early Establishment Deemed Yield Value '
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_detail_audit.revision_count IS 'Revision Count is the number of updates of the record'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_detail_audit.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_detail_audit.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_detail_audit.update_user IS 'Update User is the user id of the user that updated the record last.'
;
COMMENT ON TABLE ccs.claim_calculation_grain_quantity_detail_audit IS 'Claim Calculation Grain Quantity Detail Audit table is the audit table for claim_calculation_grain_quantity_detail'
;

ALTER TABLE ccs.claim_calculation_grain_quantity_detail_audit ADD 
    CONSTRAINT pk_ccgqda PRIMARY KEY (claim_calculation_grain_quantity_detail_audit_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE ccs.claim_calculation_grain_quantity_detail_audit ADD CONSTRAINT fk_ccgqda_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES ccs.audit_transaction_type_code(audit_transaction_type_code)
;


