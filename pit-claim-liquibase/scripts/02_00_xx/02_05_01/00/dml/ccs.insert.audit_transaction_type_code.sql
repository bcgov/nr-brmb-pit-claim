INSERT INTO ccs.audit_transaction_type_code(
	audit_transaction_type_code, 
	description,
	create_user, 
	create_date, 
	update_user, 
	update_date
) VALUES (
	'INSERT', 
	'Insert', 
	'CCS_02_05_01', 
	now(), 
	'CCS_02_05_01', 
	now()
);

INSERT INTO ccs.audit_transaction_type_code(
	audit_transaction_type_code, 
	description,
	create_user, 
	create_date, 
	update_user, 
	update_date
) VALUES (
	'UPDATE', 
	'Update',
	'CCS_02_05_01', 
	now(), 
	'CCS_02_05_01', 
	now()
);

INSERT INTO ccs.audit_transaction_type_code(
	audit_transaction_type_code,
	description,
	create_user, 
	create_date, 
	update_user, 
	update_date
) VALUES (
	'DELETE', 
	'Delete',
	'CCS_02_05_01', 
	now(), 
	'CCS_02_05_01', 
	now()
);