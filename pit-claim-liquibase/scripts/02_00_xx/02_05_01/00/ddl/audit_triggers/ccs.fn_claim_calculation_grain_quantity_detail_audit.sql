CREATE OR REPLACE FUNCTION ccs.fn_claim_calculation_grain_quantity_detail_audit()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO ccs.claim_calculation_grain_quantity_detail_audit (
            claim_calculation_grain_quantity_detail_audit_id, audit_transaction_type_code, audit_time_stamp,
            claim_calc_grain_quantity_detail_guid, claim_calculation_guid, insured_acres, probable_yield,
            deductible, production_guarantee_weight, insurable_value, coverage_value,
            total_yield_to_count, assessed_yield, early_est_deemed_yield_value, damaged_acres,
            seeded_acres, fifty_percent_production_guarantee, calc_early_est_yield,
            insp_early_est_yield, yield_value, yield_value_with_early_est_deemed_yield,
            revision_count, create_user, create_date, update_user, update_date
        )
        VALUES (
            nextval('ccs.ccgqda_seq'), 'DELETE', CURRENT_TIMESTAMP,
            OLD.claim_calc_grain_quantity_detail_guid, OLD.claim_calculation_guid, OLD.insured_acres, OLD.probable_yield,
            OLD.deductible, OLD.production_guarantee_weight, OLD.insurable_value, OLD.coverage_value,
            OLD.total_yield_to_count, OLD.assessed_yield, OLD.early_est_deemed_yield_value, OLD.damaged_acres,
            OLD.seeded_acres, OLD.fifty_percent_production_guarantee, OLD.calc_early_est_yield,
            OLD.insp_early_est_yield, OLD.yield_value, OLD.yield_value_with_early_est_deemed_yield,
            OLD.revision_count, OLD.create_user, OLD.create_date, OLD.update_user, OLD.update_date
        );
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE' OR TG_OP = 'INSERT') THEN
        INSERT INTO ccs.claim_calculation_grain_quantity_detail_audit (
            claim_calculation_grain_quantity_detail_audit_id, audit_transaction_type_code, audit_time_stamp,
            claim_calc_grain_quantity_detail_guid, claim_calculation_guid, insured_acres, probable_yield,
            deductible, production_guarantee_weight, insurable_value, coverage_value,
            total_yield_to_count, assessed_yield, early_est_deemed_yield_value, damaged_acres,
            seeded_acres, fifty_percent_production_guarantee, calc_early_est_yield,
            insp_early_est_yield, yield_value, yield_value_with_early_est_deemed_yield,
            revision_count, create_user, create_date, update_user, update_date
        )
        VALUES (
            nextval('ccs.ccgqda_seq'), TG_OP, CURRENT_TIMESTAMP,
            NEW.claim_calc_grain_quantity_detail_guid, NEW.claim_calculation_guid, NEW.insured_acres, NEW.probable_yield,
            NEW.deductible, NEW.production_guarantee_weight, NEW.insurable_value, NEW.coverage_value,
            NEW.total_yield_to_count, NEW.assessed_yield, NEW.early_est_deemed_yield_value, NEW.damaged_acres,
            NEW.seeded_acres, NEW.fifty_percent_production_guarantee, NEW.calc_early_est_yield,
            NEW.insp_early_est_yield, NEW.yield_value, NEW.yield_value_with_early_est_deemed_yield,
            NEW.revision_count, NEW.create_user, NEW.create_date, NEW.update_user, NEW.update_date
        );
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_claim_calculation_grain_quantity_detail_audit
AFTER INSERT OR UPDATE OR DELETE ON ccs.claim_calculation_grain_quantity_detail
FOR EACH ROW
EXECUTE FUNCTION ccs.fn_claim_calculation_grain_quantity_detail_audit();