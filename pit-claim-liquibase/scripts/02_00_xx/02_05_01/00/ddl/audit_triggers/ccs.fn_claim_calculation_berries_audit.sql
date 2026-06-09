CREATE OR REPLACE FUNCTION ccs.fn_claim_calculation_berries_audit()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO ccs.claim_calculation_berries_audit (
            claim_calculation_berries_audit_id, audit_transaction_type_code, audit_time_stamp,
            claim_calculation_berries_guid, claim_calculation_guid, total_probable_yield,
            deductible_level, production_guarantee, declared_acres, confirmed_acres,
            adjustment_factor, adjusted_production_guarantee, insurable_value_selected,
            insurable_value_hundred_percent, coverage_amount_adjusted, max_coverage_amount,
            harvested_yield, appraised_yield, abandoned_yield, total_yield_from_dop,
            total_yield_from_adjuster, yield_assessment, total_yield_for_calculation,
            yield_loss_eligible, revision_count, create_user, create_date, update_user, update_date
        )
        VALUES (
            nextval('ccs.ccba_seq'), 'DELETE', CURRENT_TIMESTAMP,
            OLD.claim_calculation_berries_guid, OLD.claim_calculation_guid, OLD.total_probable_yield,
            OLD.deductible_level, OLD.production_guarantee, OLD.declared_acres, OLD.confirmed_acres,
            OLD.adjustment_factor, OLD.adjusted_production_guarantee, OLD.insurable_value_selected,
            OLD.insurable_value_hundred_percent, OLD.coverage_amount_adjusted, OLD.max_coverage_amount,
            OLD.harvested_yield, OLD.appraised_yield, OLD.abandoned_yield, OLD.total_yield_from_dop,
            OLD.total_yield_from_adjuster, OLD.yield_assessment, OLD.total_yield_for_calculation,
            OLD.yield_loss_eligible, OLD.revision_count, OLD.create_user, OLD.create_date, 
            OLD.update_user, OLD.update_date
        );
		
		INSERT INTO ccs.claim_calculation_berries_ob( ccb_ob_id, claim_calculation_berries_guid, audit_transaction_type_code,
				create_user, create_date, update_user, update_date )
		VALUES ( nextval('ccs.ccb_ob_seq'), OLD.claim_calculation_berries_guid, 'DELETE', 
				OLD.create_user, OLD.create_date, OLD.update_user, OLD.update_date );
		
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE' OR TG_OP = 'INSERT') THEN
        INSERT INTO ccs.claim_calculation_berries_audit (
            claim_calculation_berries_audit_id, audit_transaction_type_code, audit_time_stamp,
            claim_calculation_berries_guid, claim_calculation_guid, total_probable_yield,
            deductible_level, production_guarantee, declared_acres, confirmed_acres,
            adjustment_factor, adjusted_production_guarantee, insurable_value_selected,
            insurable_value_hundred_percent, coverage_amount_adjusted, max_coverage_amount,
            harvested_yield, appraised_yield, abandoned_yield, total_yield_from_dop,
            total_yield_from_adjuster, yield_assessment, total_yield_for_calculation,
            yield_loss_eligible, revision_count, create_user, create_date, update_user, update_date
        )
        VALUES (
            nextval('ccs.ccba_seq'), TG_OP, CURRENT_TIMESTAMP,
            NEW.claim_calculation_berries_guid, NEW.claim_calculation_guid, NEW.total_probable_yield,
            NEW.deductible_level, NEW.production_guarantee, NEW.declared_acres, NEW.confirmed_acres,
            NEW.adjustment_factor, NEW.adjusted_production_guarantee, NEW.insurable_value_selected,
            NEW.insurable_value_hundred_percent, NEW.coverage_amount_adjusted, NEW.max_coverage_amount,
            NEW.harvested_yield, NEW.appraised_yield, NEW.abandoned_yield, NEW.total_yield_from_dop,
            NEW.total_yield_from_adjuster, NEW.yield_assessment, NEW.total_yield_for_calculation,
            NEW.yield_loss_eligible, NEW.revision_count, NEW.create_user, NEW.create_date, 
            NEW.update_user, NEW.update_date
        );
		
		INSERT INTO ccs.claim_calculation_berries_ob( ccb_ob_id, claim_calculation_berries_guid, audit_transaction_type_code,
				create_user, create_date, update_user, update_date )
		VALUES ( nextval('ccs.ccb_ob_seq'), NEW.claim_calculation_berries_guid, TG_OP, 
				NEW.create_user, NEW.create_date, NEW.update_user, NEW.update_date );
	
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_claim_calculation_berries_audit
AFTER INSERT OR UPDATE OR DELETE ON ccs.claim_calculation_berries
FOR EACH ROW
EXECUTE FUNCTION ccs.fn_claim_calculation_berries_audit();
