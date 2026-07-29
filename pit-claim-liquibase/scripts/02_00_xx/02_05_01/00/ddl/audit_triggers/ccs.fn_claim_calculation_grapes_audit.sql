CREATE OR REPLACE FUNCTION ccs.fn_claim_calculation_grapes_audit()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO ccs.claim_calculation_grapes_audit (
            claim_calculation_grapes_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            claim_calculation_grapes_guid,
            claim_calculation_guid,
            insurable_value_selected,
            insurable_value_hundred_percent,
            coverage_amount,
            coverage_amount_assessed,
            coverage_assessed_reason,
            coverage_amount_adjusted,
            total_production_value,
            revision_count,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('ccs.ccga_seq'),
            'DELETE',
            CURRENT_TIMESTAMP,
            OLD.claim_calculation_grapes_guid,
            OLD.claim_calculation_guid,
            OLD.insurable_value_selected,
            OLD.insurable_value_hundred_percent,
            OLD.coverage_amount,
            OLD.coverage_amount_assessed,
            OLD.coverage_assessed_reason,
            OLD.coverage_amount_adjusted,
            OLD.total_production_value,
            OLD.revision_count,
            OLD.create_user,
            OLD.create_date,
            OLD.update_user,
            OLD.update_date
        );
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE' OR TG_OP = 'INSERT') THEN
        INSERT INTO ccs.claim_calculation_grapes_audit (
            claim_calculation_grapes_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            claim_calculation_grapes_guid,
            claim_calculation_guid,
            insurable_value_selected,
            insurable_value_hundred_percent,
            coverage_amount,
            coverage_amount_assessed,
            coverage_assessed_reason,
            coverage_amount_adjusted,
            total_production_value,
            revision_count,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('ccs.ccga_seq'),
            TG_OP, -- Stores 'INSERT' or 'UPDATE'
            CURRENT_TIMESTAMP,
            NEW.claim_calculation_grapes_guid,
            NEW.claim_calculation_guid,
            NEW.insurable_value_selected,
            NEW.insurable_value_hundred_percent,
            NEW.coverage_amount,
            NEW.coverage_amount_assessed,
            NEW.coverage_assessed_reason,
            NEW.coverage_amount_adjusted,
            NEW.total_production_value,
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

CREATE TRIGGER trg_claim_calculation_grapes_audit
AFTER INSERT OR UPDATE OR DELETE
ON ccs.claim_calculation_grapes
FOR EACH ROW
EXECUTE FUNCTION ccs.fn_claim_calculation_grapes_audit();

