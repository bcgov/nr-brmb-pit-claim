CREATE OR REPLACE FUNCTION ccs.fn_claim_calculation_variety_audit()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO ccs.claim_calculation_variety_audit (
            claim_calculation_variety_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            claim_calculation_variety_guid,
            claim_calculation_guid,
            crop_variety_id,
            revision_count,
            average_price,
            average_price_override,
            average_price_final,
            insurable_value,
            yield_assessed_reason,
            yield_assessed,
            yield_total,
            yield_actual,
            variety_production_value,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('ccs.ccva_seq'),
            'DELETE',
            CURRENT_TIMESTAMP,
            OLD.claim_calculation_variety_guid,
            OLD.claim_calculation_guid,
            OLD.crop_variety_id,
            OLD.revision_count,
            OLD.average_price,
            OLD.average_price_override,
            OLD.average_price_final,
            OLD.insurable_value,
            OLD.yield_assessed_reason,
            OLD.yield_assessed,
            OLD.yield_total,
            OLD.yield_actual,
            OLD.variety_production_value,
            OLD.create_user,
            OLD.create_date,
            OLD.update_user,
            OLD.update_date
        );
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE' OR TG_OP = 'INSERT') THEN
        INSERT INTO ccs.claim_calculation_variety_audit (
            claim_calculation_variety_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            claim_calculation_variety_guid,
            claim_calculation_guid,
            crop_variety_id,
            revision_count,
            average_price,
            average_price_override,
            average_price_final,
            insurable_value,
            yield_assessed_reason,
            yield_assessed,
            yield_total,
            yield_actual,
            variety_production_value,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('ccs.ccva_seq'),
            TG_OP,
            CURRENT_TIMESTAMP,
            NEW.claim_calculation_variety_guid,
            NEW.claim_calculation_guid,
            NEW.crop_variety_id,
            NEW.revision_count,
            NEW.average_price,
            NEW.average_price_override,
            NEW.average_price_final,
            NEW.insurable_value,
            NEW.yield_assessed_reason,
            NEW.yield_assessed,
            NEW.yield_total,
            NEW.yield_actual,
            NEW.variety_production_value,
            NEW.create_user,
            NEW.create_date,
            NEW.update_user,
            NEW.update_date
        );
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_claim_calculation_variety_audit
AFTER INSERT OR UPDATE OR DELETE
ON ccs.claim_calculation_variety
FOR EACH ROW
EXECUTE FUNCTION ccs.fn_claim_calculation_variety_audit();

