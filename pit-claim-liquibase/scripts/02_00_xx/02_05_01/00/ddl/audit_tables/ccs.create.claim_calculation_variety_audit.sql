CREATE TABLE ccs.claim_calculation_variety_audit(
    claim_calculation_variety_audit_id    numeric(9, 0)     NOT NULL,
    audit_transaction_type_code           varchar(10)       NOT NULL,
    audit_time_stamp                      timestamp(6)      NOT NULL,
    claim_calculation_variety_guid        varchar(32)       NOT NULL,
    claim_calculation_guid                varchar(32),
    crop_variety_id                       numeric(9, 0),
    revision_count                        numeric(10, 0),
    average_price                         numeric(8, 4),
    average_price_override                numeric(8, 4),
    average_price_final                   numeric(8, 4),
    insurable_value                       numeric(12, 4),
    yield_assessed_reason                 varchar(1000),
    yield_assessed                        numeric(10, 2),
    yield_total                           numeric(12, 4),
    yield_actual                          numeric(12, 4),
    variety_production_value              numeric(10, 2),
    create_user                           varchar(64)       NOT NULL,
    create_date                           timestamp(0)      NOT NULL,
    update_user                           varchar(64)       NOT NULL,
    update_date                           timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN ccs.claim_calculation_variety_audit.claim_calculation_variety_audit_id IS 'Claim Calculation Variety Audit Id is the ID of the Claim Calculation Variety Audit table, comes from a sequence.'
;
COMMENT ON COLUMN ccs.claim_calculation_variety_audit.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN ccs.claim_calculation_variety_audit.audit_time_stamp IS 'Audit Time Stamp denotes when the record was inserted in the audit table.'
;
COMMENT ON COLUMN ccs.claim_calculation_variety_audit.claim_calculation_variety_guid IS 'Claim Calculation Variety Guid is a unique key of a claims calculation variety record'
;
COMMENT ON COLUMN ccs.claim_calculation_variety_audit.claim_calculation_guid IS 'Claim Calculation Guid is a unique key of a claims calculation'
;
COMMENT ON COLUMN ccs.claim_calculation_variety_audit.crop_variety_id IS 'Crop Variety Id is a unique Id of a variety from CROP VARIETY'
;
COMMENT ON COLUMN ccs.claim_calculation_variety_audit.revision_count IS 'Revision Count is the number of updates of the record'
;
COMMENT ON COLUMN ccs.claim_calculation_variety_audit.average_price IS 'Average Price is the average contracted price of the last 5 years. If there are no contracted prices it''s the 100% IV. Imported from CIRRAS'
;
COMMENT ON COLUMN ccs.claim_calculation_variety_audit.average_price_override IS 'Average Price Override is the average contracted price of the last 5 years. Entered by the user in the claims calculator'
;
COMMENT ON COLUMN ccs.claim_calculation_variety_audit.average_price_final IS 'Average Price Final is the average contracted price of the last 5 years. This is the value used in the calculations. It''s equal to AVERAGE_PRICE_OVERRIDE if a value exists else it''s AVERAGE_PRICE'
;
COMMENT ON COLUMN ccs.claim_calculation_variety_audit.insurable_value IS 'Insurable Value is the IV per Lb and is calculated AVERAGE PRICE * (CLAIM CALCULATION.INSURABLE VALUE SELECTED / INSURABLE VALUE HUNDRED PERCENT)'
;
COMMENT ON COLUMN ccs.claim_calculation_variety_audit.yield_assessed_reason IS 'Yield Assessed Reason is the reason why the yield is different from the actual yield'
;
COMMENT ON COLUMN ccs.claim_calculation_variety_audit.yield_assessed IS 'Yield Assessed is the difference between the Actual Yield and the yield found in the assessment'
;
COMMENT ON COLUMN ccs.claim_calculation_variety_audit.yield_total IS 'Yield Total is the sum of YIELD ACTUAL and YIELD ASSESSED'
;
COMMENT ON COLUMN ccs.claim_calculation_variety_audit.yield_actual IS 'Yield Actual the yield for that variety and is entered by the user'
;
COMMENT ON COLUMN ccs.claim_calculation_variety_audit.variety_production_value IS 'Variety Production Value is the total production dollars of the variety. YIELD TOTAL * INSURABLE VALUE'
;
COMMENT ON COLUMN ccs.claim_calculation_variety_audit.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN ccs.claim_calculation_variety_audit.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN ccs.claim_calculation_variety_audit.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN ccs.claim_calculation_variety_audit.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE ccs.claim_calculation_variety_audit IS 'Claim Calculation Variety Audit table is the audit table for claim_calculation_variety'
;

ALTER TABLE ccs.claim_calculation_variety_audit ADD 
    CONSTRAINT pk_ccva PRIMARY KEY (claim_calculation_variety_audit_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE ccs.claim_calculation_variety_audit ADD CONSTRAINT fk_ccva_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES ccs.audit_transaction_type_code(audit_transaction_type_code)
;


