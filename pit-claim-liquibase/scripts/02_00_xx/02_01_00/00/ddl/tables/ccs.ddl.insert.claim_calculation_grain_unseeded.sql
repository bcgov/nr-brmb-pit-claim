CREATE TABLE ccs.claim_calculation_grain_unseeded(
    claim_calc_grain_unseeded_guid    varchar(32)       NOT NULL,
    claim_calculation_guid	          varchar(32)       NOT NULL,
    insured_acres                     numeric(14, 4)    NOT NULL,
    less_adjustment_acres             numeric(14, 4),
    adjusted_acres                    numeric(14, 4),
    deductible_level                  numeric(3, 0)     NOT NULL,
    deductible_acres                  numeric(14, 4),
    max_eligible_acres                numeric(14, 4),
    insurable_value                   numeric(12, 4)    NOT NULL,
    coverage_value                    numeric(10, 2)    NOT NULL,
    unseeded_acres                    numeric(14, 4),
    less_assessment_acres             numeric(14, 4),
    eligible_unseeded_acres           numeric(14, 4),
    revision_count                    numeric(10, 0),
    create_user                       varchar(64)       NOT NULL,
    create_date                       timestamp(0)      NOT NULL,
    update_user                       varchar(64)       NOT NULL,
    update_date                       timestamp(0)      NOT NULL
) TABLESPACE pg_default
;

COMMENT ON COLUMN ccs.claim_calculation_grain_unseeded.claim_calc_grain_unseeded_guid IS 'Claim Calc Grain Unseeded Guid is a unique key of the record'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_unseeded.claim_calculation_guid IS 'Claim Calculation Guid is a unique key of a claims calculation'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_unseeded.insured_acres IS 'Insured Acres are the total acres from the purchase: cirr_insurable_crop_units.insa_total_insured_acres'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_unseeded.less_adjustment_acres IS 'Less Adjustment Acres are assessed lower than in the purchase'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_unseeded.adjusted_acres IS 'Adjusted Acres is the total acres the coverage is based on. It is calculated: insured_acres - less_adjustment_acres'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_unseeded.deductible_level IS 'Deductible Level is the deductible expressed as a percent, which is applied to claims at which the insurance policy is insured from cirr_deductible_rates.ded_level'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_unseeded.deductible_acres IS 'Deductible Acres is a calculated value: adjusted_acres * deductible_level '
;
COMMENT ON COLUMN ccs.claim_calculation_grain_unseeded.max_eligible_acres IS 'Max Eligible Acres are the maximum acres eligible for the claim: adjusted_acres - deductible_acres'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_unseeded.insurable_value IS 'Insurable Value is the IV in the product purchase (cirr_insrnc_prdct_prchses.u_insurable_value)'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_unseeded.coverage_value IS 'Coverage Value is a calculated value: max_eligible_acres * insurable_value'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_unseeded.unseeded_acres IS 'Unseeded Acres is the total unseeded acres'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_unseeded.less_assessment_acres IS 'Less Assessment Acres are assessed lower than stated in the claim in unseeded_acres'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_unseeded.eligible_unseeded_acres IS 'Eligible Unseeded Acres is calculated: unseeded_acres - less_assessment_acres - deductible_acres'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_unseeded.revision_count IS 'Revision Count is the number of updates of the record'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_unseeded.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_unseeded.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_unseeded.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_unseeded.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE ccs.claim_calculation_grain_unseeded IS 'The table contains the claim calculation data specific of Grain Unseeded'
;

CREATE INDEX IX_CCGU_CC ON ccs.claim_calculation_grain_unseeded(claim_calculation_guid)
 TABLESPACE pg_default
;
ALTER TABLE ccs.claim_calculation_grain_unseeded ADD 
    CONSTRAINT PK_CCGU PRIMARY KEY (claim_calc_grain_unseeded_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE ccs.claim_calculation_grain_unseeded ADD CONSTRAINT FK_CCGU_CC 
    FOREIGN KEY (claim_calculation_guid)
    REFERENCES ccs.claim_calculation(claim_calculation_guid)
;

