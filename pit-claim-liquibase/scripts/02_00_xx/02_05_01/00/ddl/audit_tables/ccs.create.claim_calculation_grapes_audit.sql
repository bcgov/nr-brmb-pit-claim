CREATE TABLE ccs.claim_calculation_grapes_audit(
    claim_calculation_grapes_audit_id    numeric(9, 0)     NOT NULL,
    audit_transaction_type_code          varchar(10)       NOT NULL,
    audit_time_stamp                     timestamp(6)      NOT NULL,
    claim_calculation_grapes_guid        varchar(32)       NOT NULL,
    claim_calculation_guid               varchar(32),
    insurable_value_selected             numeric(12, 4),
    insurable_value_hundred_percent      numeric(12, 4),
    coverage_amount                      numeric(10, 2),
    coverage_amount_assessed             numeric(10, 2),
    coverage_assessed_reason             varchar(1000),
    coverage_amount_adjusted             numeric(10, 2),
    total_production_value               numeric(10, 2),
    revision_count                       numeric(10, 0),
    create_user                          varchar(64)       NOT NULL,
    create_date                          timestamp(0)      NOT NULL,
    update_user                          varchar(64)       NOT NULL,
    update_date                          timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN ccs.claim_calculation_grapes_audit.claim_calculation_grapes_audit_id IS 'Claim Calculation Grapes Audit Id is the ID of the Claim Calculation Grapes Audit table, comes from a sequence.'
;
COMMENT ON COLUMN ccs.claim_calculation_grapes_audit.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN ccs.claim_calculation_grapes_audit.audit_time_stamp IS 'Audit Time Stamp denotes when the record was inserted in the audit table.'
;
COMMENT ON COLUMN ccs.claim_calculation_grapes_audit.claim_calculation_grapes_guid IS 'Claim Calculation Grapes Guid is a unique key of a claims calculation berries record'
;
COMMENT ON COLUMN ccs.claim_calculation_grapes_audit.claim_calculation_guid IS 'Claim Calculation Guid is a unique key of a claims calculation'
;
COMMENT ON COLUMN ccs.claim_calculation_grapes_audit.insurable_value_selected IS 'Insurable Value Selected is the selected IV in the product purchase'
;
COMMENT ON COLUMN ccs.claim_calculation_grapes_audit.insurable_value_hundred_percent IS 'Insurable Value Hundred Percent is the 100% IV of the product'
;
COMMENT ON COLUMN ccs.claim_calculation_grapes_audit.coverage_amount IS 'Coverage Amount is the total coverage from the product purchase'
;
COMMENT ON COLUMN ccs.claim_calculation_grapes_audit.coverage_amount_assessed IS 'Coverage Amount Assessed is the difference of the total coverage amount (COVERAGE_AMOUNT) and the adjusted coverage amount (COVERAGE_AMOUNT_ADJUSTED)'
;
COMMENT ON COLUMN ccs.claim_calculation_grapes_audit.coverage_assessed_reason IS 'Coverage Assessed Reason is the reason why the coverage is assessed lower than in the purchase'
;
COMMENT ON COLUMN ccs.claim_calculation_grapes_audit.coverage_amount_adjusted IS 'Coverage Amount Adjusted is the adjusted coverage COVERAGE_AMOUNT - COVERAGE_AMOUNT_ASSESSED'
;
COMMENT ON COLUMN ccs.claim_calculation_grapes_audit.total_production_value IS 'Total Production Value is the total value of all yield. It''s deducted from the adjusted coverage amount to get the claim amount'
;
COMMENT ON COLUMN ccs.claim_calculation_grapes_audit.revision_count IS 'Revision Count is the number of updates of the record'
;
COMMENT ON COLUMN ccs.claim_calculation_grapes_audit.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN ccs.claim_calculation_grapes_audit.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN ccs.claim_calculation_grapes_audit.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN ccs.claim_calculation_grapes_audit.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE ccs.claim_calculation_grapes_audit IS 'Claim Calculation Grapes Audit table is the audit table for claim_calculation_grapes'
;

ALTER TABLE ccs.claim_calculation_grapes_audit ADD 
    CONSTRAINT pk_ccga PRIMARY KEY (claim_calculation_grapes_audit_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE ccs.claim_calculation_grapes_audit ADD CONSTRAINT fk_ccga_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES ccs.audit_transaction_type_code(audit_transaction_type_code)
;


