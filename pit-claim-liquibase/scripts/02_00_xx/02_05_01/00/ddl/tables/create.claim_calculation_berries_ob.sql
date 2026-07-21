CREATE TABLE ccs.claim_calculation_berries_ob(
    ccb_ob_id                         numeric(10, 0)    NOT NULL,
    claim_calculation_berries_guid    varchar(32)       NOT NULL,
    audit_transaction_type_code       varchar(10)       NOT NULL,
    create_user                       varchar(64)       NOT NULL,
    create_date                       timestamp(0)      NOT NULL,
    update_user                       varchar(64)       NOT NULL,
    update_date                       timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN ccs.claim_calculation_berries_ob.ccb_ob_id IS 'CCB OB ID is a surrogate unique identifier generated for a claim calculation berries outbox record.'
;
COMMENT ON COLUMN ccs.claim_calculation_berries_ob.claim_calculation_berries_guid IS 'Claim Calculation Berries Guid is a unique key of a claims calculation berries record'
;
COMMENT ON COLUMN ccs.claim_calculation_berries_ob.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN ccs.claim_calculation_berries_ob.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN ccs.claim_calculation_berries_ob.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN ccs.claim_calculation_berries_ob.update_user IS 'Update User is the user id of the user that updated the record last.'
;
COMMENT ON COLUMN ccs.claim_calculation_berries_ob.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE ccs.claim_calculation_berries_ob IS 'Claim Calculation Berries OB is the outbox table for claim calculation berries'
;

ALTER TABLE ccs.claim_calculation_berries_ob ADD 
    CONSTRAINT pk_ccbo PRIMARY KEY (ccb_ob_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE ccs.claim_calculation_berries_ob ADD CONSTRAINT fk_ccbo_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES ccs.audit_transaction_type_code(audit_transaction_type_code)
;


