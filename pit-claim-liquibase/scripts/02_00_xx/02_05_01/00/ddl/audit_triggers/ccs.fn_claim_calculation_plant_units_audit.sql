CREATE OR REPLACE FUNCTION ccs.fn_claim_calculation_plant_units_audit()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO ccs.claim_calculation_plant_units_audit (
            claim_calculation_plant_units_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            claim_calc_plant_units_guid,
            claim_calculation_guid,
            insured_units,
            less_adjustment_reason,
            less_adjustment_units,
            adjusted_units,
            deductible_level,
            deductible_units,
            total_coverage_units,
            insurable_value,
            coverage_amount,
            damaged_units,
            less_assessment_reason,
            less_assessment_units,
            total_damaged_units,
            revision_count,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('ccs.ccpua_seq'),
            'DELETE',
            CURRENT_TIMESTAMP,
            OLD.claim_calc_plant_units_guid,
            OLD.claim_calculation_guid,
            OLD.insured_units,
            OLD.less_adjustment_reason,
            OLD.less_adjustment_units,
            OLD.adjusted_units,
            OLD.deductible_level,
            OLD.deductible_units,
            OLD.total_coverage_units,
            OLD.insurable_value,
            OLD.coverage_amount,
            OLD.damaged_units,
            OLD.less_assessment_reason,
            OLD.less_assessment_units,
            OLD.total_damaged_units,
            OLD.revision_count,
            OLD.create_user,
            OLD.create_date,
            OLD.update_user,
            OLD.update_date
        );
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE' OR TG_OP = 'INSERT') THEN
        INSERT INTO ccs.claim_calculation_plant_units_audit (
            claim_calculation_plant_units_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            claim_calc_plant_units_guid,
            claim_calculation_guid,
            insured_units,
            less_adjustment_reason,
            less_adjustment_units,
            adjusted_units,
            deductible_level,
            deductible_units,
            total_coverage_units,
            insurable_value,
            coverage_amount,
            damaged_units,
            less_assessment_reason,
            less_assessment_units,
            total_damaged_units,
            revision_count,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('ccs.ccpua_seq'),
            TG_OP,
            CURRENT_TIMESTAMP,
            NEW.claim_calc_plant_units_guid,
            NEW.claim_calculation_guid,
            NEW.insured_units,
            NEW.less_adjustment_reason,
            NEW.less_adjustment_units,
            NEW.adjusted_units,
            NEW.deductible_level,
            NEW.deductible_units,
            NEW.total_coverage_units,
            NEW.insurable_value,
            NEW.coverage_amount,
            NEW.damaged_units,
            NEW.less_assessment_reason,
            NEW.less_assessment_units,
            NEW.total_damaged_units,
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


CREATE TRIGGER trg_claim_calculation_plant_units_audit
AFTER INSERT OR UPDATE OR DELETE
ON ccs.claim_calculation_plant_units
FOR EACH ROW
EXECUTE FUNCTION ccs.fn_claim_calculation_plant_units_audit();

