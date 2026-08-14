CREATE TABLE ccs.claim_calculation_grain_spot_loss_audit(
    claim_calculation_grain_spot_loss_audit_id numeric(9, 0)     NOT NULL,
    audit_transaction_type_code        varchar(10)       NOT NULL,
    audit_time_stamp                   timestamp(6)      NOT NULL,
    claim_calc_grain_spot_loss_guid    varchar(32)       NOT NULL,
    claim_calculation_guid             varchar(32),
    insured_acres                      numeric(14, 4),
    coverage_amt_per_acre              numeric(14, 4),
    coverage_value                     numeric(14, 4),
    adjusted_acres                     numeric(14, 4),
    percent_yield_reduction            numeric(5, 2),
    eligible_yield_reduction           numeric(14, 4),
    spot_loss_reduction_value          numeric(14, 4),
    deductible                         numeric(3, 0),
    revision_count                     numeric(10, 0),
    create_user                        varchar(64)       NOT NULL,
    create_date                        timestamp(0)      NOT NULL,
    update_user                        varchar(64)       NOT NULL,
    update_date                        timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN ccs.claim_calculation_grain_spot_loss_audit.claim_calculation_grain_spot_loss_audit_id IS 'Claim Calculation Grain Spot Loss Audit Id is the ID of the Claim Calculation Grain Quantity Spot Loss Audit table, comes from a sequence.'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_spot_loss_audit.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_spot_loss_audit.audit_time_stamp IS 'Audit Time Stamp denotes when the record was inserted in the audit table.'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_spot_loss_audit.claim_calc_grain_spot_loss_guid IS 'Claim Calculation Grain Spot Loss Guid is a unique key of the record'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_spot_loss_audit.claim_calculation_guid IS 'Claim Calculation Guid is a unique key of a claims calculation'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_spot_loss_audit.insured_acres IS 'Insured Acres are the total acres from the purchase: cirr_insrnc_prdct_prchses.s_insured_acres'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_spot_loss_audit.coverage_amt_per_acre IS 'Coverage Amount per Acre is calculated as the sum of cirr_grain_spot_loss_purchases.coverage_dollars for the spot loss purchase'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_spot_loss_audit.coverage_value IS 'Coverage Value is a calculated value: insured_acres * coverage_amt_per_acre'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_spot_loss_audit.adjusted_acres IS 'Adjusted Acres is a user entered value'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_spot_loss_audit.percent_yield_reduction IS 'Percent Yield Reduction is a user entered value'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_spot_loss_audit.eligible_yield_reduction IS 'Eligible Yield Reduction is calculated as (Adjusted Acres x Percent Yield Reduction) '
;
COMMENT ON COLUMN ccs.claim_calculation_grain_spot_loss_audit.spot_loss_reduction_value IS 'Spot Loss Reduction Value is calculated as ( Coverage Dollars per Acre x Eligible Yield Reduction)'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_spot_loss_audit.deductible IS 'Deductible is the deductible expressed as a percent, always set to 5%'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_spot_loss_audit.revision_count IS 'Revision Count is the number of updates of the record'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_spot_loss_audit.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_spot_loss_audit.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_spot_loss_audit.update_user IS 'Update User is the user id of the user that updated the record last.'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_spot_loss_audit.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE ccs.claim_calculation_grain_spot_loss_audit IS 'Claim Calculation Grain Spot Loss Audit table is the audit table for claim_calculation_grain_spot_loss '
;

ALTER TABLE ccs.claim_calculation_grain_spot_loss_audit ADD 
    CONSTRAINT pk_ccgsla PRIMARY KEY (claim_calculation_grain_spot_loss_audit_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE ccs.claim_calculation_grain_spot_loss_audit ADD CONSTRAINT fk_ccgsla_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES ccs.audit_transaction_type_code(audit_transaction_type_code)
;


