CREATE OR REPLACE FUNCTION ccs.fn_claim_calculation_grain_spot_loss_audit()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO ccs.claim_calculation_grain_spot_loss_audit (
            claim_calculation_grain_spot_loss_audit_id, audit_transaction_type_code, audit_time_stamp,
            claim_calc_grain_spot_loss_guid, claim_calculation_guid, insured_acres,
            coverage_amt_per_acre, coverage_value, adjusted_acres,
            percent_yield_reduction, eligible_yield_reduction, spot_loss_reduction_value,
            deductible, revision_count, create_user, create_date, update_user, update_date
        )
        VALUES (
            nextval('ccs.ccgspa_seq'), 'DELETE', CURRENT_TIMESTAMP,
            OLD.claim_calc_grain_spot_loss_guid, OLD.claim_calculation_guid, OLD.insured_acres,
            OLD.coverage_amt_per_acre, OLD.coverage_value, OLD.adjusted_acres,
            OLD.percent_yield_reduction, OLD.eligible_yield_reduction, OLD.spot_loss_reduction_value,
            OLD.deductible, OLD.revision_count, OLD.create_user, OLD.create_date, 
            OLD.update_user, OLD.update_date
        );
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE' OR TG_OP = 'INSERT') THEN
        INSERT INTO ccs.claim_calculation_grain_spot_loss_audit (
            claim_calculation_grain_spot_loss_audit_id, audit_transaction_type_code, audit_time_stamp,
            claim_calc_grain_spot_loss_guid, claim_calculation_guid, insured_acres,
            coverage_amt_per_acre, coverage_value, adjusted_acres,
            percent_yield_reduction, eligible_yield_reduction, spot_loss_reduction_value,
            deductible, revision_count, create_user, create_date, update_user, update_date
        )
        VALUES (
            nextval('ccs.ccgspa_seq'), TG_OP, CURRENT_TIMESTAMP,
            NEW.claim_calc_grain_spot_loss_guid, NEW.claim_calculation_guid, NEW.insured_acres,
            NEW.coverage_amt_per_acre, NEW.coverage_value, NEW.adjusted_acres,
            NEW.percent_yield_reduction, NEW.eligible_yield_reduction, NEW.spot_loss_reduction_value,
            NEW.deductible, NEW.revision_count, NEW.create_user, NEW.create_date, 
            NEW.update_user, NEW.update_date
        );
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_claim_calculation_grain_spot_loss_audit
AFTER INSERT OR UPDATE OR DELETE ON ccs.claim_calculation_grain_spot_loss
FOR EACH ROW
EXECUTE FUNCTION ccs.fn_claim_calculation_grain_spot_loss_audit();



