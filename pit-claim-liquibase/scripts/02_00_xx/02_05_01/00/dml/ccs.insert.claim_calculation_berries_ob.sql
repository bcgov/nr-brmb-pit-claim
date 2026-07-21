INSERT INTO ccs.claim_calculation_berries_ob
		( ccb_ob_id, claim_calculation_berries_guid, audit_transaction_type_code, create_user, create_date, update_user, update_date )
	SELECT
		nextval('ccs.ccb_ob_seq'), claim_calculation_berries_guid, 'UPDATE', 'CCS_02_05_01', current_timestamp, 'CCS_02_05_01', current_timestamp 
	FROM claim_calculation_berries;