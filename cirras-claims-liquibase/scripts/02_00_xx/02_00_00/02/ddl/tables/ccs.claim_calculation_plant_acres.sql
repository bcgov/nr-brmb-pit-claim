CREATE TABLE ccs.claim_calculation_plant_acres (
  claim_calc_plant_acres_guid varchar(32)   NOT NULL,
  claim_calculation_guid      varchar(32)   NOT NULL,
  declared_acres              numeric(14,4),
  confirmed_acres             numeric(14,4),
  insured_acres               numeric(14,4),
  deductible_level            numeric(3),
  deductible_acres            numeric(14,4),
  total_coverage_acres        numeric(14,4),
  damaged_acres               numeric(14,4),
  acres_loss_covered          numeric(14,4),
  insurable_value             numeric(12,4),
  coverage_amount             numeric(10,2),
  less_assessment_reason      varchar(1000),
  less_assessment_amount      numeric(14,4),
  revision_count              numeric(10),
  create_user                 varchar(64)   NOT NULL,
  create_date                 timestamp(0)  NOT NULL,
  update_user                 varchar(64)   NOT NULL,
  update_date                 timestamp(0)  NOT NULL
) TABLESPACE pg_default
;



COMMENT ON TABLE ccs.claim_calculation_plant_acres IS 'The table contains the claim calculation data specific to plant loss insured by acres coverage'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres.claim_calc_plant_acres_guid IS 'Claim Calc Plant Acres Guid is a unique key of the record'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres.claim_calculation_guid IS 'Claim Calculation Guid is a unique key of a claims calculation'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres.declared_acres IS 'Declared Acres is the total acres from the product purchase from cirr_insurable_crop_units.insa_total_insured_acres'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres.confirmed_acres IS 'Confirmed Acres is the total acres entered in the calculation'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres.insured_acres IS 'Insured Acres is the lessor value of DECLARED_ACRES OR CONFIRMED_ACRES'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres.deductible_level IS 'Deductible Level is the deductible expressed as a percent, which is applied to claims at which the insurance policy is insured from cirr_deductible_rates.ded_level'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres.deductible_acres IS 'Deductible Acres is the acres deducted from the claim. It is calculated: INSURED_ACRES * DEDUCTIBLE_LEVEL'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres.total_coverage_acres IS 'Total Coverage Acres is the covered acres for the claim. It is calculated: INSURED_ACRES - DEDUCTIBLE_ACRES'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres.damaged_acres IS 'Damaged Acres are the lost units in the claim event'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres.acres_loss_covered IS 'Acres Loss Covered is the covered acres for the claim. It is calculated: DAMAGED_ACRES - DEDUCTIBLE_ACRES'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres.insurable_value IS 'Insurable Value is the IV in the product purchase from ipp.p_insurable_value * ipp.p_insurable_value_level / 100'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres.coverage_amount IS 'Coverage Amount is the adjusted coverage calculated by ACRES_LOSS_COVERED * INSURABLE_VALUE'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres.less_assessment_reason IS 'Less Assessment Reason is the reason why the total claim amount assessed is lower than the calculated amount'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres.less_assessment_amount IS 'Less Assessment Amount is the amount the total claim amount is lower than the calculated amount'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres.revision_count IS 'Revision Count is the number of updates of the record'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres.update_date IS 'Update Date is the date when the record was updated last.'
;

CREATE INDEX ix_ccpa_cc ON ccs.claim_calculation_plant_acres(claim_calculation_guid)
 TABLESPACE pg_default
;

ALTER TABLE ccs.claim_calculation_plant_acres ADD
    CONSTRAINT pk_ccpa PRIMARY KEY (claim_calc_plant_acres_guid) USING INDEX TABLESPACE pg_default
;

ALTER TABLE ccs.claim_calculation_plant_acres ADD CONSTRAINT fk_ccpa_cc
    FOREIGN KEY (claim_calculation_guid)
    REFERENCES ccs.claim_calculation(claim_calculation_guid)
;
