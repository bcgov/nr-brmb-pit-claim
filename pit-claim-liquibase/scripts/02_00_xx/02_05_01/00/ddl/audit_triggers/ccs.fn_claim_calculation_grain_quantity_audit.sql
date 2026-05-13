CREATE OR REPLACE FUNCTION ccs.fn_claim_calculation_grain_quantity_audit()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO ccs.claim_calculation_grain_quantity_audit (
            claim_calculation_grain_quantity_audit_id, audit_transaction_type_code, audit_time_stamp,
            claim_calc_grain_quantity_guid, total_coverage_value, production_guarantee_amount,
            total_yield_loss_value, reseed_claim, max_claim_payable, advanced_claim,
            quantity_loss_claim, revision_count, create_user, create_date, update_user, update_date
        )
        VALUES (
            nextval('ccs.ccgqa_seq'), 'DELETE', CURRENT_TIMESTAMP,
            OLD.claim_calc_grain_quantity_guid, OLD.total_coverage_value, OLD.production_guarantee_amount,
            OLD.total_yield_loss_value, OLD.reseed_claim, OLD.max_claim_payable, OLD.advanced_claim,
            OLD.quantity_loss_claim, OLD.revision_count, OLD.create_user, OLD.create_date, 
            OLD.update_user, OLD.update_date
        );
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE' OR TG_OP = 'INSERT') THEN
        INSERT INTO ccs.claim_calculation_grain_quantity_audit (
            claim_calculation_grain_quantity_audit_id, audit_transaction_type_code, audit_time_stamp,
            claim_calc_grain_quantity_guid, total_coverage_value, production_guarantee_amount,
            total_yield_loss_value, reseed_claim, max_claim_payable, advanced_claim,
            quantity_loss_claim, revision_count, create_user, create_date, update_user, update_date
        )
        VALUES (
            nextval('ccs.ccgqa_seq'), TG_OP, CURRENT_TIMESTAMP,
            NEW.claim_calc_grain_quantity_guid, NEW.total_coverage_value, NEW.production_guarantee_amount,
            NEW.total_yield_loss_value, NEW.reseed_claim, NEW.max_claim_payable, NEW.advanced_claim,
            NEW.quantity_loss_claim, NEW.revision_count, NEW.create_user, NEW.create_date, 
            NEW.update_user, NEW.update_date
        );
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_claim_calculation_grain_quantity_audit
AFTER INSERT OR UPDATE OR DELETE ON ccs.claim_calculation_grain_quantity
FOR EACH ROW
EXECUTE FUNCTION ccs.fn_claim_calculation_grain_quantity_audit();
