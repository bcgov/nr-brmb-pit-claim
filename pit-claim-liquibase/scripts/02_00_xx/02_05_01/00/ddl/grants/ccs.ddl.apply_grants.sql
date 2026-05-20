GRANT SELECT, INSERT, UPDATE, DELETE ON ccs.declared_yield_contract_commodity_berries_sync TO "app_ccs_rest_proxy";

-- Grant permissios to the audit tables
GRANT SELECT, INSERT ON ccs.audit_transaction_type_code TO "app_ccs_rest_proxy";
GRANT SELECT, INSERT ON ccs.claim_calculation_audit TO "app_ccs_rest_proxy";
GRANT SELECT, INSERT ON ccs.claim_calculation_berries_audit TO "app_ccs_rest_proxy";
GRANT SELECT, INSERT ON ccs.claim_calculation_grain_basket_audit TO "app_ccs_rest_proxy";
GRANT SELECT, INSERT ON ccs.claim_calculation_grain_basket_product_audit TO "app_ccs_rest_proxy";
GRANT SELECT, INSERT ON ccs.claim_calculation_grain_quantity_audit TO "app_ccs_rest_proxy";
GRANT SELECT, INSERT ON ccs.claim_calculation_grain_quantity_detail_audit TO "app_ccs_rest_proxy";
GRANT SELECT, INSERT ON ccs.claim_calculation_grain_spot_loss_audit TO "app_ccs_rest_proxy";
GRANT SELECT, INSERT ON ccs.claim_calculation_grain_unseeded_audit TO "app_ccs_rest_proxy";
GRANT SELECT, INSERT ON ccs.claim_calculation_grapes_audit TO "app_ccs_rest_proxy";
GRANT SELECT, INSERT ON ccs.claim_calculation_plant_acres_audit TO "app_ccs_rest_proxy";
GRANT SELECT, INSERT ON ccs.claim_calculation_plant_units_audit TO "app_ccs_rest_proxy";
GRANT SELECT, INSERT ON ccs.claim_calculation_variety_audit TO "app_ccs_rest_proxy";

--Grant access to sequences to proxy
GRANT USAGE ON ALL SEQUENCES IN SCHEMA ccs TO "app_ccs_rest_proxy";

--Grant read only access to all tables for the readonly role
GRANT SELECT ON ALL TABLES IN SCHEMA ccs TO "app_ccs_readonly";
