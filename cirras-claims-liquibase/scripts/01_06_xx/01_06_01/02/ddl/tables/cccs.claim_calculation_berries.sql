CREATE TABLE cccs.claim_calculation_berries (
  claim_calculation_berries_guid  varchar(32)   NOT NULL,
  claim_calculation_guid          varchar(32)   NOT NULL,
  total_probable_yield            numeric(14,4),
  deductible_level                numeric(3),
  production_guarantee            numeric(14,4),
  insurable_value_selected        numeric(12,4) NOT NULL,
  insurable_value_hundred_percent numeric(12,4) NOT NULL,
  coverage_amount_adjusted        numeric(10,2),
  declared_acres                  numeric(14,4),
  confirmed_acres                 numeric(14,4),
  adjustment_factor               numeric(14,4),
  adjusted_production_guarantee   numeric(14,4),
  harvested_yield                 numeric(14,4),
  appraised_yield                 numeric(14,4),
  abandoned_yield                 numeric(14,4),
  total_yield_from_dop            numeric(14,4),
  total_yield_from_adjuster       numeric(14,4),
  yield_assessment                numeric(14,4),
  total_yield_for_calculation     numeric(14,4),
  yield_loss_eligible             numeric(14,4),
  revision_count                  numeric(10),
  create_user                     varchar(64)   NOT NULL,
  create_date                     timestamp(0)  NOT NULL,
  update_user                     varchar(64)   NOT NULL,
  update_date                     timestamp(0)  NOT NULL,
  max_coverage_amount             numeric(10,2)
) TABLESPACE pg_default
;



COMMENT ON TABLE cccs.claim_calculation_berries IS 'The table contains the claim calculation data specific to the insurance plan Berries'
;
COMMENT ON COLUMN cccs.claim_calculation_berries.claim_calculation_berries_guid IS 'Claim Calculation Berries Guid is a unique key of a claims calculation berries record'
;
COMMENT ON COLUMN cccs.claim_calculation_berries.claim_calculation_guid IS 'Claim Calculation Guid is a unique key of a claims calculation'
;
COMMENT ON COLUMN cccs.claim_calculation_berries.total_probable_yield IS 'Probable Yield is the total year from CIRRAS. icu.insc_total_insured_acres * icu.probable_yield'
;
COMMENT ON COLUMN cccs.claim_calculation_berries.deductible_level IS 'Deductible Level is the deductible expressed as a percent, which is applied to claims at which the insurance policy is insured from cirr_deductible_rates.ded_level'
;
COMMENT ON COLUMN cccs.claim_calculation_berries.production_guarantee IS 'The guaranteed production this coverage provides for (expressed as a percentage) from cirr_insrnc_prdct_prchses.q_production_guarantee'
;
COMMENT ON COLUMN cccs.claim_calculation_berries.insurable_value_selected IS 'Insurable Value Selected is the selected IV in the product purchase'
;
COMMENT ON COLUMN cccs.claim_calculation_berries.insurable_value_hundred_percent IS 'Insurable Value Hundred Percent is the 100% IV of the product'
;
COMMENT ON COLUMN cccs.claim_calculation_berries.coverage_amount_adjusted IS 'Coverage Amount Adjusted is the adjusted coverage INSURABLE_VALUE_SELECTED * ADJUSTED_PRODUCTION_GUARANTEE'
;
COMMENT ON COLUMN cccs.claim_calculation_berries.declared_acres IS 'Declared Acres is the total acres from the product purchase from cirr_insurable_crop_units.insc_total_insured_acres'
;
COMMENT ON COLUMN cccs.claim_calculation_berries.confirmed_acres IS 'Confirmed Acres is the total acres entered in the calculation'
;
COMMENT ON COLUMN cccs.claim_calculation_berries.adjustment_factor IS 'Adjustment Factor is a calculated value: Confirmed Acres / Declared Acres'
;
COMMENT ON COLUMN cccs.claim_calculation_berries.adjusted_production_guarantee IS 'Adjusted Production Guarantee is a calculated value: Production Guarantee * Adjustment Factor'
;
COMMENT ON COLUMN cccs.claim_calculation_berries.harvested_yield IS 'Harvested Yield is the yield that was harvested'
;
COMMENT ON COLUMN cccs.claim_calculation_berries.appraised_yield IS 'Appraised Yield is the amount of crop production not harvested'
;
COMMENT ON COLUMN cccs.claim_calculation_berries.abandoned_yield IS 'Abandoned Yield is the declared amount of yield that was abandoned by the grower.'
;
COMMENT ON COLUMN cccs.claim_calculation_berries.total_yield_from_dop IS 'Total Yield From Dop is the total yield declared by the grower'
;
COMMENT ON COLUMN cccs.claim_calculation_berries.total_yield_from_adjuster IS 'Total Yield From Adjuster is the total yield declared by an adjuster'
;
COMMENT ON COLUMN cccs.claim_calculation_berries.yield_assessment IS 'Yield Assessment is the yield that is added to the total yield from dop or from the adjuster (from adjuster if it''s NOT NULL)'
;
COMMENT ON COLUMN cccs.claim_calculation_berries.total_yield_for_calculation IS 'Total Yield For Calculation is the total yield for the claim calculation. Calculated from TOTAL_YIELD_FROM_DOP/ADJUSTER + YIELD_ASSESSMENT'
;
COMMENT ON COLUMN cccs.claim_calculation_berries.yield_loss_eligible IS 'Yield Loss Eligible is the yield loss eligible for the claim. Calculated from ADJUSTED_PRODUCTION_GUARANTEE - TOTAL_YIELD_FOR_CALCULATION'
;
COMMENT ON COLUMN cccs.claim_calculation_berries.revision_count IS 'Revision Count is the numeric of updates of the record'
;
COMMENT ON COLUMN cccs.claim_calculation_berries.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cccs.claim_calculation_berries.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cccs.claim_calculation_berries.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cccs.claim_calculation_berries.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON COLUMN cccs.claim_calculation_berries.max_coverage_amount IS 'Max Coverage Amount is the coverage amount from the product purchase CIRR_INSRNC_PRDCT_PRCHSES.Q_COVERAGE_DOLLARS. The claim coverage (COVERAGE_AMOUNT_ADJUSTED) cannot be higher than this amount'
;

CREATE INDEX ix_ccb_cc ON cccs.claim_calculation_berries(claim_calculation_guid)
 TABLESPACE pg_default
;

ALTER TABLE cccs.claim_calculation_berries ADD
    CONSTRAINT pk_ccb PRIMARY KEY (claim_calculation_berries_guid) USING INDEX TABLESPACE pg_default
;

ALTER TABLE cccs.claim_calculation_berries ADD CONSTRAINT fk_ccb_cc
    FOREIGN KEY (claim_calculation_guid)
    REFERENCES cccs.claim_calculation(claim_calculation_guid)
;
