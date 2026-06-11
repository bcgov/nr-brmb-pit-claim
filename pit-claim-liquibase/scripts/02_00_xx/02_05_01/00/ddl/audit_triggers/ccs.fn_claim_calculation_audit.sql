CREATE OR REPLACE FUNCTION ccs.fn_claim_calculation_audit()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO ccs.claim_calculation_audit (
            claim_calculation_audit_id, audit_transaction_type_code, audit_time_stamp,
            claim_calculation_guid, primary_peril_code, secondary_peril_code,
            claim_status_code, commodity_coverage_code, calculation_status_code,
            insurance_plan_id, crop_commodity_id, create_claim_calc_user_guid,
            update_claim_calc_user_guid, claim_calc_grain_quantity_guid, calculate_iiv_ind,
            has_cheque_req_ind, crop_year, insured_by_meas_type, contract_id,
            policy_number, claim_number, calculation_version, revision_count,
            grower_number, grower_name, grower_address_line1, grower_address_line2,
            grower_postal_code, grower_city, grower_province, total_claim_amount,
            calculation_comment, submitted_by_userid, submitted_by_name,
            submitted_by_date, recommended_by_userid, recommended_by_name,
            recommended_by_date, approved_by_userid, approved_by_name,
            approved_by_date, create_user, create_date, update_user, update_date
        )
        VALUES (
            nextval('ccs.cca_seq'), 'DELETE', CURRENT_TIMESTAMP,
            OLD.claim_calculation_guid, OLD.primary_peril_code, OLD.secondary_peril_code,
            OLD.claim_status_code, OLD.commodity_coverage_code, OLD.calculation_status_code,
            OLD.insurance_plan_id, OLD.crop_commodity_id, OLD.create_claim_calc_user_guid,
            OLD.update_claim_calc_user_guid, OLD.claim_calc_grain_quantity_guid, OLD.calculate_iiv_ind,
            OLD.has_cheque_req_ind, OLD.crop_year, OLD.insured_by_meas_type, OLD.contract_id,
            OLD.policy_number, OLD.claim_number, OLD.calculation_version, OLD.revision_count,
            OLD.grower_number, OLD.grower_name, OLD.grower_address_line1, OLD.grower_address_line2,
            OLD.grower_postal_code, OLD.grower_city, OLD.grower_province, OLD.total_claim_amount,
            OLD.calculation_comment, OLD.submitted_by_userid, OLD.submitted_by_name,
            OLD.submitted_by_date, OLD.recommended_by_userid, OLD.recommended_by_name,
            OLD.recommended_by_date, OLD.approved_by_userid, OLD.approved_by_name,
            OLD.approved_by_date, OLD.create_user, OLD.create_date, OLD.update_user, OLD.update_date
        );
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE' OR TG_OP = 'INSERT') THEN
        INSERT INTO ccs.claim_calculation_audit (
            claim_calculation_audit_id, audit_transaction_type_code, audit_time_stamp,
            claim_calculation_guid, primary_peril_code, secondary_peril_code,
            claim_status_code, commodity_coverage_code, calculation_status_code,
            insurance_plan_id, crop_commodity_id, create_claim_calc_user_guid,
            update_claim_calc_user_guid, claim_calc_grain_quantity_guid, calculate_iiv_ind,
            has_cheque_req_ind, crop_year, insured_by_meas_type, contract_id,
            policy_number, claim_number, calculation_version, revision_count,
            grower_number, grower_name, grower_address_line1, grower_address_line2,
            grower_postal_code, grower_city, grower_province, total_claim_amount,
            calculation_comment, submitted_by_userid, submitted_by_name,
            submitted_by_date, recommended_by_userid, recommended_by_name,
            recommended_by_date, approved_by_userid, approved_by_name,
            approved_by_date, create_user, create_date, update_user, update_date
        )
        VALUES (
            nextval('ccs.cca_seq'), TG_OP, CURRENT_TIMESTAMP,
            NEW.claim_calculation_guid, NEW.primary_peril_code, NEW.secondary_peril_code,
            NEW.claim_status_code, NEW.commodity_coverage_code, NEW.calculation_status_code,
            NEW.insurance_plan_id, NEW.crop_commodity_id, NEW.create_claim_calc_user_guid,
            NEW.update_claim_calc_user_guid, NEW.claim_calc_grain_quantity_guid, NEW.calculate_iiv_ind,
            NEW.has_cheque_req_ind, NEW.crop_year, NEW.insured_by_meas_type, NEW.contract_id,
            NEW.policy_number, NEW.claim_number, NEW.calculation_version, NEW.REVISION_COUNT,
            NEW.grower_number, NEW.grower_name, NEW.grower_address_line1, NEW.grower_address_line2,
            NEW.grower_postal_code, NEW.grower_city, NEW.grower_province, NEW.total_claim_amount,
            NEW.calculation_comment, NEW.submitted_by_userid, NEW.submitted_by_name,
            NEW.submitted_by_date, NEW.recommended_by_userid, NEW.recommended_by_name,
            NEW.recommended_by_date, NEW.approved_by_userid, NEW.approved_by_name,
            NEW.approved_by_date, NEW.create_user, NEW.create_date, NEW.update_user, NEW.update_date
        );
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_claim_calculation_audit
AFTER INSERT OR UPDATE OR DELETE ON ccs.claim_calculation
FOR EACH ROW
EXECUTE FUNCTION ccs.fn_claim_calculation_audit();
