CREATE TABLE ccs.claim_calculation_grain_quantity_audit(
    claim_calculation_grain_quantity_audit_id    numeric(9, 0)     NOT NULL,
    audit_transaction_type_code                  varchar(10)       NOT NULL,
    audit_time_stamp                             timestamp(6)      NOT NULL,
    claim_calc_grain_quantity_guid               varchar(32)       NOT NULL,
    total_coverage_value                         numeric(14, 4),
    production_guarantee_amount                  numeric(14, 4),
    total_yield_loss_value                       numeric(14, 4),
    reseed_claim                                 numeric(14, 4),
    max_claim_payable                            numeric(14, 4),
    advanced_claim                               numeric(14, 4),
    quantity_loss_claim                          numeric(14, 4),
    revision_count                               numeric(10, 0),
    create_user                                  varchar(64)       NOT NULL,
    create_date                                  timestamp(0)      NOT NULL,
    update_user                                  varchar(64)       NOT NULL,
    update_date                                  timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_audit.claim_calculation_grain_quantity_audit_id IS 'Claim Calculation Grain Quantity Audit Id is the ID of the Claim Calculation Grain Quantity Audit table, comes from a sequence.'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_audit.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_audit.audit_time_stamp IS 'Audit Time Stamp denotes when the record was inserted in the audit table.'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_audit.claim_calc_grain_quantity_guid IS 'Claim Calculation Grain Quantity Guid is a unique key of the record'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_audit.total_coverage_value IS 'Total Coverage Value is calculated as sum of pedigreed and non pedigreed coverage values'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_audit.production_guarantee_amount IS 'Production Guarantee Amount is calculated as: sum of (Production Guarantee - Assessed Yield) * Insurable Value for pedigreed and non-pedigreed commodities'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_audit.total_yield_loss_value IS 'Total Yield Loss Value is calculated as: (Production Guarantee Amount ) - sum of (Yield Value + Early Establishment ) for pedigreed and non-pedigreed commodities '
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_audit.reseed_claim IS 'Reseed Claim is user entered'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_audit.max_claim_payable IS 'Max Claim Payable is calculated as: Total Pedigreed and Non-Pedigreed Seeds Coverage Value - Reseed Claim'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_audit.advanced_claim IS 'Advanced Claim is user entered'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_audit.quantity_loss_claim IS 'Quantity Loss Claim is calculated as: lesser of Maximum Claim Payable or Total Quantity Loss'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_audit.revision_count IS 'Revision Count is the number of updates of the record'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_audit.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_audit.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_audit.update_user IS 'Update User is the user id of the user that updated the record last.'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_quantity_audit.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE ccs.claim_calculation_grain_quantity_audit IS 'Claim Calculation Grain Quantity Audit table is the audit table for claim_calculation_grain_quantity'
;

ALTER TABLE ccs.claim_calculation_grain_quantity_audit ADD 
    CONSTRAINT pk_ccgqa PRIMARY KEY (claim_calculation_grain_quantity_audit_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE ccs.claim_calculation_grain_quantity_audit ADD CONSTRAINT fk_ccgqa_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES ccs.audit_transaction_type_code(audit_transaction_type_code)
;
