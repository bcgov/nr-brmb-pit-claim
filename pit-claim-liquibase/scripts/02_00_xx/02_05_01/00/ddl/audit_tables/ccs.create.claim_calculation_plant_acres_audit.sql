CREATE TABLE ccs.claim_calculation_plant_acres_audit(
    claim_calculation_plant_acres_audit_id    numeric(9, 0)     NOT NULL,
    audit_transaction_type_code               varchar(10)       NOT NULL,
    audit_time_stamp                          timestamp(6)      NOT NULL,
    claim_calc_plant_acres_guid               varchar(32)       NOT NULL,
    claim_calculation_guid                    varchar(32),
    declared_acres                            numeric(14, 4),
    confirmed_acres                           numeric(14, 4),
    insured_acres                             numeric(14, 4),
    deductible_level                          numeric(3, 0),
    deductible_acres                          numeric(14, 4),
    total_coverage_acres                      numeric(14, 4),
    damaged_acres                             numeric(14, 4),
    acres_loss_covered                        numeric(14, 4),
    insurable_value                           numeric(12, 4),
    coverage_amount                           numeric(10, 2),
    less_assessment_reason                    varchar(1000),
    less_assessment_amount                    numeric(14, 4),
    revision_count                            numeric(10, 0),
    create_user                               varchar(64)       NOT NULL,
    create_date                               timestamp(0)      NOT NULL,
    update_user                               varchar(64)       NOT NULL,
    update_date                               timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN ccs.claim_calculation_plant_acres_audit.claim_calculation_plant_acres_audit_id IS 'Claim Calculation Plant Acres Audit Id is the ID of the Claim Calculation Plant Acres Audit table, comes from a sequence.'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres_audit.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres_audit.audit_time_stamp IS 'Audit Time Stamp denotes when the record was inserted in the audit table.'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres_audit.claim_calc_plant_acres_guid IS 'Claim Calc Plant Acres Guid is a unique key of the record'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres_audit.claim_calculation_guid IS 'Claim Calculation Guid is a unique key of a claims calculation'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres_audit.declared_acres IS 'Declared Acres is the total acres from the product purchase from cirr_insurable_crop_units.insa_total_insured_acres'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres_audit.confirmed_acres IS 'Confirmed Acres is the total acres entered in the calculation'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres_audit.insured_acres IS 'Insured Acres is the lessor value of DECLARED_ACRES OR CONFIRMED_ACRES'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres_audit.deductible_level IS 'Deductible Level is the deductible expressed as a percent, which is applied to claims at which the insurance policy is insured from cirr_deductible_rates.ded_level'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres_audit.deductible_acres IS 'Deductible Acres is the acres deducted from the claim. It is calculated: INSURED_ACRES * DEDUCTIBLE_LEVEL'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres_audit.total_coverage_acres IS 'Total Coverage Acres is the covered acres for the claim. It is calculated: INSURED_ACRES - DEDUCTIBLE_ACRES'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres_audit.damaged_acres IS 'Damaged Acres are the lost units in the claim event'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres_audit.acres_loss_covered IS 'Acres Loss Covered is the covered acres for the claim. It is calculated: DAMAGED_ACRES - DEDUCTIBLE_ACRES'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres_audit.insurable_value IS 'Insurable Value is the IV in the product purchase from ipp.p_insurable_value * ipp.p_insurable_value_level / 100'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres_audit.coverage_amount IS 'Coverage Amount is the adjusted coverage calculated by ACRES_LOSS_COVERED * INSURABLE_VALUE'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres_audit.less_assessment_reason IS 'Less Assessment Amount Reason is the reason why the total claim amount assessed is lower than the calculated amount'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres_audit.less_assessment_amount IS 'Less Assessment Amount is the amount the total claim amount is lower than the calculated amount'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres_audit.revision_count IS 'Revision Count is the number of updates of the record'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres_audit.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres_audit.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres_audit.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN ccs.claim_calculation_plant_acres_audit.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE ccs.claim_calculation_plant_acres_audit IS 'Claim Calculation Plant Acres Audit table is the audit table for claim_calculation_plant_acres'
;

ALTER TABLE ccs.claim_calculation_plant_acres_audit ADD 
    CONSTRAINT pk_ccpaa PRIMARY KEY (claim_calculation_plant_acres_audit_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE ccs.claim_calculation_plant_acres_audit ADD CONSTRAINT fk_ccpaa_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES ccs.audit_transaction_type_code(audit_transaction_type_code)
;


