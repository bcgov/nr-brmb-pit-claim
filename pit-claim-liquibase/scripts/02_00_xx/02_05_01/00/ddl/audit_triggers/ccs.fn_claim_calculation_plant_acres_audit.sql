CREATE OR REPLACE FUNCTION ccs.fn_claim_calculation_plant_acres_audit()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO ccs.claim_calculation_plant_acres_audit (
            claim_calculation_plant_acres_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            claim_calc_plant_acres_guid,
            claim_calculation_guid,
            declared_acres,
            confirmed_acres,
            insured_acres,
            deductible_level,
            deductible_acres,
            total_coverage_acres,
            damaged_acres,
            acres_loss_covered,
            insurable_value,
            coverage_amount,
            less_assessment_reason,
            less_assessment_amount,
            revision_count,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('ccs.ccpaa_seq'),
            'DELETE',
            CURRENT_TIMESTAMP,
            OLD.claim_calc_plant_acres_guid,
            OLD.claim_calculation_guid,
            OLD.declared_acres,
            OLD.confirmed_acres,
            OLD.insured_acres,
            OLD.deductible_level,
            OLD.deductible_acres,
            OLD.total_coverage_acres,
            OLD.damaged_acres,
            OLD.acres_loss_covered,
            OLD.insurable_value,
            OLD.coverage_amount,
            OLD.less_assessment_reason,
            OLD.less_assessment_amount,
            OLD.revision_count,
            OLD.create_user,
            OLD.create_date,
            OLD.update_user,
            OLD.update_date
        );
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE' OR TG_OP = 'INSERT') THEN
        INSERT INTO ccs.claim_calculation_plant_acres_audit (
            claim_calculation_plant_acres_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            claim_calc_plant_acres_guid,
            claim_calculation_guid,
            declared_acres,
            confirmed_acres,
            insured_acres,
            deductible_level,
            deductible_acres,
            total_coverage_acres,
            damaged_acres,
            acres_loss_covered,
            insurable_value,
            coverage_amount,
            less_assessment_reason,
            less_assessment_amount,
            revision_count,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('ccs.ccpaa_seq'),
            TG_OP,
            CURRENT_TIMESTAMP,
            NEW.claim_calc_plant_acres_guid,
            NEW.claim_calculation_guid,
            NEW.declared_acres,
            NEW.confirmed_acres,
            NEW.insured_acres,
            NEW.deductible_level,
            NEW.deductible_acres,
            NEW.total_coverage_acres,
            NEW.damaged_acres,
            NEW.acres_loss_covered,
            NEW.insurable_value,
            NEW.coverage_amount,
            NEW.less_assessment_reason,
            NEW.less_assessment_amount,
            NEW.revision_count,
            NEW.create_user,
            NEW.create_date,
            NEW.update_user,
            NEW.update_date
        );
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_claim_calculation_plant_acres_audit
AFTER INSERT OR UPDATE OR DELETE
ON ccs.claim_calculation_plant_acres
FOR EACH ROW
EXECUTE FUNCTION ccs.fn_claim_calculation_plant_acres_audit();

