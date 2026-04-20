GRANT SELECT, INSERT, UPDATE, DELETE ON ccs.declared_yield_contract_commodity_berries_sync TO "app_ccs_rest_proxy";

--Grant read only access to all tables for the readonly role
GRANT SELECT ON ALL TABLES IN SCHEMA ccs TO "app_ccs_readonly";
