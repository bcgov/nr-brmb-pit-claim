CREATE TABLE ccs.claim_calculation_grain_basket_product_audit(
    claim_calculation_grain_basket_product_audit_id    numeric(9, 0)     NOT NULL,
    audit_transaction_type_code                        varchar(10)       NOT NULL,
    audit_time_stamp                                   timestamp(6)      NOT NULL,
    claim_calc_grain_basket_product_guid               varchar(32)       NOT NULL,
    claim_calculation_guid                             varchar(32),
    crop_commodity_id                                  numeric(9, 0),
    hundred_percent_insurable_value                    numeric(14, 4),
    insurable_value                                    numeric(14, 4),
    production_guarantee                               numeric(14, 4),
    coverage_value                                     numeric(14, 4),
    total_yield_to_count                               numeric(14, 4),
    assessed_yield                                     numeric(14, 4),
    quantity_claim_amount                              numeric(10, 2),
    yield_value                                        numeric(14, 4),
    revision_count                                     numeric(10, 0),
    create_user                                        varchar(64)       NOT NULL,
    create_date                                        timestamp(0)      NOT NULL,
    update_user                                        varchar(64)       NOT NULL,
    update_date                                        timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN ccs.claim_calculation_grain_basket_product_audit.claim_calculation_grain_basket_product_audit_id IS 'Claim Calculation Grain Basket Product Audit Id is the ID of the Claim Calculation Grain Basket Product Audit table, comes from a sequence.'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket_product_audit.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket_product_audit.audit_time_stamp IS 'Audit Time Stamp denotes when the record was inserted in the audit table.'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket_product_audit.claim_calc_grain_basket_product_guid IS 'Claim Calc Grain Basket Product Guid is a unique key of the record'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket_product_audit.claim_calculation_guid IS 'Claim Calculation Guid is a unique key of a claims calculation'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket_product_audit.crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from CIRR CROP TYPES.CRPT ID'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket_product_audit.hundred_percent_insurable_value IS 'hundred percent insurable value is the 100% IV of the quantity product: q_insurable_value'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket_product_audit.insurable_value IS 'insurable value is the selected IV from the quantity product: q_selected_insurable_value'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket_product_audit.production_guarantee IS 'production guarantee is the production guarantee from the quantity product: q_production_guarantee'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket_product_audit.coverage_value IS 'coverage value is the coverage dollars from the quantity product: coverage_amount'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket_product_audit.total_yield_to_count IS 'total yield to count is the total yield to count from verified yield: verified_yield_summary.yield_to_count'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket_product_audit.assessed_yield IS 'assessed yield is the total assessed yield from the approved quantity claim: claim_calculation_grain_quantity_detail.assessed_yield'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket_product_audit.yield_value IS 'yield value is calculated: total_yield_to_count x hundred_percent_insurable_value'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket_product_audit.revision_count IS 'Revision Count is the number of updates of the record'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket_product_audit.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket_product_audit.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket_product_audit.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN ccs.claim_calculation_grain_basket_product_audit.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE ccs.claim_calculation_grain_basket_product_audit IS 'Claim Calculation Grain Basket Product Audit is the audit table for claim_calculation_grain_basket_product'
;

ALTER TABLE ccs.claim_calculation_grain_basket_product_audit ADD 
    CONSTRAINT pk_ccgbpa PRIMARY KEY (claim_calculation_grain_basket_product_audit_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE ccs.claim_calculation_grain_basket_product_audit ADD CONSTRAINT fk_ccgbpa_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES ccs.audit_transaction_type_code(audit_transaction_type_code)
;


