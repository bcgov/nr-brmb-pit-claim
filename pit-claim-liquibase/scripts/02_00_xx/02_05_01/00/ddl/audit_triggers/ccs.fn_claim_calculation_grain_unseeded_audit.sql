CREATE OR REPLACE FUNCTION ccs.fn_claim_calculation_grain_unseeded_audit()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO ccs.claim_calculation_grain_unseeded_audit (
            claim_calculation_grain_unseeded_audit_id, audit_transaction_type_code, audit_time_stamp,
            claim_calc_grain_unseeded_guid, claim_calculation_guid, insured_acres,
            less_adjustment_acres, adjusted_acres, deductible_level,
            deductible_acres, max_eligible_acres, insurable_value,
            coverage_value, unseeded_acres, less_assessment_acres,
            eligible_unseeded_acres, revision_count, create_user,
            create_date, update_user, update_date
        )
        VALUES (
            nextval('ccs.ccgua_seq'), 'DELETE', CURRENT_TIMESTAMP,
            OLD.claim_calc_grain_unseeded_guid, OLD.claim_calculation_guid, OLD.insured_acres,
            OLD.less_adjustment_acres, OLD.adjusted_acres, OLD.deductible_level,
            OLD.deductible_acres, OLD.max_eligible_acres, OLD.insurable_value,
            OLD.coverage_value, OLD.unseeded_acres, OLD.less_assessment_acres,
            OLD.eligible_unseeded_acres, OLD.revision_count, OLD.create_user,
            OLD.create_date, OLD.update_user, OLD.update_date
        );
        RETURN OLD;
    ELSE
        -- Handles INSERT and UPDATE
        INSERT INTO ccs.claim_calculation_grain_unseeded_audit (
            claim_calculation_grain_unseeded_audit_id, audit_transaction_type_code, audit_time_stamp,
            claim_calc_grain_unseeded_guid, claim_calculation_guid, insured_acres,
            less_adjustment_acres, adjusted_acres, deductible_level,
            deductible_acres, max_eligible_acres, insurable_value,
            coverage_value, unseeded_acres, less_assessment_acres,
            eligible_unseeded_acres, revision_count, create_user,
            create_date, update_user, update_date
        )
        VALUES (
            nextval('ccs.ccgua_seq'), TG_OP, CURRENT_TIMESTAMP,
            NEW.claim_calc_grain_unseeded_guid, NEW.claim_calculation_guid, NEW.insured_acres,
            NEW.less_adjustment_acres, NEW.adjusted_acres, NEW.deductible_level,
            NEW.deductible_acres, NEW.max_eligible_acres, NEW.insurable_value,
            NEW.coverage_value, NEW.unseeded_acres, NEW.less_assessment_acres,
            NEW.eligible_unseeded_acres, NEW.revision_count, NEW.create_user,
            NEW.create_date, NEW.update_user, NEW.update_date
        );
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_claim_calculation_grain_unseeded_audit
AFTER INSERT OR UPDATE OR DELETE ON ccs.claim_calculation_grain_unseeded
FOR EACH ROW
EXECUTE FUNCTION ccs.fn_claim_calculation_grain_unseeded_audit();

