CREATE TABLE ccs.claim_calculation_plant_units (
  claim_calc_plant_units_guid varchar(32)   NOT NULL,
  claim_calculation_guid      varchar(32)   NOT NULL,
  insured_units               numeric(14,4),
  less_adjustment_reason      varchar(1000),
  less_adjustment_units       numeric(10),
  adjusted_units              numeric(14,4),
  deductible_level            numeric(3),
  deductible_units            numeric(14,4),
  total_coverage_units        numeric(14,4),
  insurable_value             numeric(12,4),
  coverage_amount             numeric(10,2),
  damaged_units               numeric(10),
  less_assessment_reason      varchar(1000),
  less_assessment_units       numeric(10),
  total_damaged_units         numeric(14,4),
  revision_count              numeric(10),
  create_user                 varchar(64)   NOT NULL,
  create_date                 timestamp(0)  NOT NULL,
  update_user                 varchar(64)   NOT NULL,
  update_date                 timestamp(0)  NOT NULL
) TABLESPACE pg_default
;



COMMENT ON TABLE ccs.claim_calculation_plant_units IS 'The table contains the claim calculation data specific to plant loss insured by units coverage'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_units.claim_calc_plant_units_guid IS 'Claim Calc Plant Units Guid is a unique key of the record'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_units.claim_calculation_guid IS 'Claim Calculation Guid is a unique key of a claims calculation'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_units.insured_units IS 'Insured Units is the total insured units from CIRRAS. cirr_insurable_crop_units.icubu_number_of_insured_plants'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_units.less_adjustment_reason IS 'Less Adjustment Reason is the reason why the units assessed is different than in the purchase'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_units.less_adjustment_units IS 'Less Adjustment Units are assessed lower than in the purchase'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_units.adjusted_units IS 'Adjusted Units is the total units the coverage is based ON. It is calculated: INSURED_UNITS - CVRGE_LESS_ASSESSMENT_UNITS'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_units.deductible_level IS 'Deductible Level is the deductible expressed as a percent, which is applied to claims at which the insurance policy is insured from cirr_deductible_rates.ded_level'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_units.deductible_units IS 'Deductible Units is a calculated value: ADJUSTED_UNITS * DEDUCTIBLE_VALUE'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_units.total_coverage_units IS 'Total Coverage Units is the maximum number of plants eligible for the claim calculated by ADJUSTED_UNITS - DEDUCTIBLE_UNITS'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_units.insurable_value IS 'Insurable Value is the IV in the product purchase from ipp.p_insurable_value * ipp.p_insurable_value_level / 100'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_units.coverage_amount IS 'Coverage Amount is the adjusted coverage calculated by TOTAL_COVERAGE_UNITS * INSURABLE_VALUE'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_units.damaged_units IS 'Damaged Units are the lost units in the claim event'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_units.less_assessment_reason IS 'Less Assessment Reason is the reason why the units assessed is different than the ONes claimed in DAMAGE_UNITS'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_units.less_assessment_units IS 'Less Assessment Units are assessed lower than stated in the claim in DAMAGE_UNITS'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_units.total_damaged_units IS 'Total Damaged Units is the total units used to calculate the claim amount. It is calculated: DAMAGED_UNITS - LESS_ASSESSMENT_UNITS - DEDUCTIBLE_UNITS'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_units.revision_count IS 'Revision Count is the number of updates of the record'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_units.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_units.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_units.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_units.update_date IS 'Update Date is the date when the record was updated last.'
;

CREATE INDEX ix_ccpu_cc ON ccs.claim_calculation_plant_units(claim_calculation_guid)
 TABLESPACE pg_default
;

ALTER TABLE ccs.claim_calculation_plant_units ADD
    CONSTRAINT pk_ccpu PRIMARY KEY (claim_calc_plant_units_guid) USING INDEX TABLESPACE pg_default
;

ALTER TABLE ccs.claim_calculation_plant_units ADD CONSTRAINT fk_ccpu_cc
    FOREIGN KEY (claim_calculation_guid)
    REFERENCES ccs.claim_calculation(claim_calculation_guid)
;
