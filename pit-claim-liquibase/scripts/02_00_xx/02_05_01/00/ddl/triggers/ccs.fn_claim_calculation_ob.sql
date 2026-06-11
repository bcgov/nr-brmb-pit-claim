CREATE OR REPLACE FUNCTION ccs.fn_claim_calculation_ob()
RETURNS TRIGGER AS $$
BEGIN
	-- If multiple triggers of the same kind are defined for the same event, they will be fired in alphabetical order by name.
	-- This trigger is run after the audit trigger: trg_claim_calculation_audit
	
    IF (TG_OP = 'UPDATE' AND NEW.insurance_plan_id = 3 AND NEW.commodity_coverage_code = 'CQNT' AND NEW.calculation_status_code <> OLD.calculation_status_code ) THEN
		INSERT INTO ccs.claim_calculation_berries_ob( ccb_ob_id, claim_calculation_berries_guid, audit_transaction_type_code,
			create_user, create_date, update_user, update_date )
		VALUES ( nextval('ccs.ccb_ob_seq'), NEW.claim_calculation_berries_guid, TG_OP, 
			NEW.create_user, NEW.create_date, NEW.update_user, NEW.update_date );
		
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_claim_calculation_ob
AFTER INSERT OR UPDATE OR DELETE ON ccs.claim_calculation
FOR EACH ROW
EXECUTE FUNCTION ccs.fn_claim_calculation_ob();
