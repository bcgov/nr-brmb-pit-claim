CREATE OR REPLACE FUNCTION ccs.fn_claim_calculation_grain_basket_audit()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO ccs.claim_calculation_grain_basket_audit (
            claim_calculation_grain_basket_audit_id, audit_transaction_type_code, audit_time_stamp,
            claim_calculation_grain_basket_guid, claim_calculation_guid, grain_basket_coverage_value,
            grain_basket_deductible, grain_basket_harvested_value, quantity_total_coverage_value,
            quantity_total_yield_value, quantity_total_claim_amount, quantity_total_yield_loss_indemnity,
            total_yield_coverage_value, total_yield_loss, revision_count, 
            create_user, create_date, update_user, update_date
        )
        VALUES (
            nextval('ccs.ccgba_seq'), 'DELETE', CURRENT_TIMESTAMP,
            OLD.claim_calculation_grain_basket_guid, OLD.claim_calculation_guid, OLD.grain_basket_coverage_value,
            OLD.grain_basket_deductible, OLD.grain_basket_harvested_value, OLD.quantity_total_coverage_value,
            OLD.quantity_total_yield_value, OLD.quantity_total_claim_amount, OLD.quantity_total_yield_loss_indemnity,
            OLD.total_yield_coverage_value, OLD.total_yield_loss, OLD.revision_count,
            OLD.create_user, OLD.create_date, OLD.update_user, OLD.update_date
        );
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE' OR TG_OP = 'INSERT') THEN
        INSERT INTO ccs.claim_calculation_grain_basket_audit (
            claim_calculation_grain_basket_audit_id, audit_transaction_type_code, audit_time_stamp,
            claim_calculation_grain_basket_guid, claim_calculation_guid, grain_basket_coverage_value,
            grain_basket_deductible, grain_basket_harvested_value, quantity_total_coverage_value,
            quantity_total_yield_value, quantity_total_claim_amount, quantity_total_yield_loss_indemnity,
            total_yield_coverage_value, total_yield_loss, revision_count, 
            create_user, create_date, update_user, update_date
        )
        VALUES (
            nextval('ccs.ccgba_seq'), TG_OP, CURRENT_TIMESTAMP,
            NEW.claim_calculation_grain_basket_guid, NEW.claim_calculation_guid, NEW.grain_basket_coverage_value,
            NEW.grain_basket_deductible, NEW.grain_basket_harvested_value, NEW.quantity_total_coverage_value,
            NEW.quantity_total_yield_value, NEW.quantity_total_claim_amount, NEW.quantity_total_yield_loss_indemnity,
            NEW.total_yield_coverage_value, NEW.total_yield_loss, NEW.revision_count,
            NEW.create_user, NEW.create_date, NEW.update_user, NEW.update_date
        );
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_claim_calculation_grain_basket_audit
AFTER INSERT OR UPDATE OR DELETE ON ccs.claim_calculation_grain_basket
FOR EACH ROW
EXECUTE FUNCTION ccs.fn_claim_calculation_grain_basket_audit();
