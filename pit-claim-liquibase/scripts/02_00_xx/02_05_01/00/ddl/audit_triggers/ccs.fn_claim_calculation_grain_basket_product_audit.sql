CREATE OR REPLACE FUNCTION ccs.fn_claim_calculation_grain_basket_product_audit()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO ccs.claim_calculation_grain_basket_product_audit (
            claim_calculation_grain_basket_product_audit_id, audit_transaction_type_code, audit_time_stamp,
            claim_calc_grain_basket_product_guid, claim_calculation_guid, crop_commodity_id,
            hundred_percent_insurable_value, insurable_value, production_guarantee,
            coverage_value, total_yield_to_count, assessed_yield, quantity_claim_amount,
            yield_value, revision_count, create_user, create_date, update_user, update_date
        )
        VALUES (
            nextval('ccs.ccgbpa_seq'), 'DELETE', CURRENT_TIMESTAMP,
            OLD.claim_calc_grain_basket_product_guid, OLD.claim_calculation_guid, OLD.crop_commodity_id,
            OLD.hundred_percent_insurable_value, OLD.insurable_value, OLD.production_guarantee,
            OLD.coverage_value, OLD.total_yield_to_count, OLD.assessed_yield, OLD.quantity_claim_amount,
            OLD.yield_value, OLD.revision_count, OLD.create_user, OLD.create_date, OLD.update_user, OLD.update_date
        );
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE' OR TG_OP = 'INSERT') THEN
        INSERT INTO ccs.claim_calculation_grain_basket_product_audit (
            claim_calculation_grain_basket_product_audit_id, audit_transaction_type_code, audit_time_stamp,
            claim_calc_grain_basket_product_guid, claim_calculation_guid, crop_commodity_id,
            hundred_percent_insurable_value, insurable_value, production_guarantee,
            coverage_value, total_yield_to_count, assessed_yield, quantity_claim_amount,
            yield_value, revision_count, create_user, create_date, update_user, update_date
        )
        VALUES (
            nextval('ccs.ccgbpa_seq'), TG_OP, CURRENT_TIMESTAMP,
            NEW.claim_calc_grain_basket_product_guid, NEW.claim_calculation_guid, NEW.crop_commodity_id,
            NEW.hundred_percent_insurable_value, NEW.insurable_value, NEW.production_guarantee,
            NEW.coverage_value, NEW.total_yield_to_count, NEW.assessed_yield, NEW.quantity_claim_amount,
            NEW.yield_value, NEW.revision_count, NEW.create_user, NEW.create_date, NEW.update_user, NEW.update_date
        );
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_claim_calculation_gbp_audit
AFTER INSERT OR UPDATE OR DELETE ON ccs.claim_calculation_grain_basket_product
FOR EACH ROW
EXECUTE FUNCTION ccs.fn_claim_calculation_grain_basket_product_audit();
