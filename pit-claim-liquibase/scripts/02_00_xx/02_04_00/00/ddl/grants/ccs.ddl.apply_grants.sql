GRANT SELECT, INSERT, UPDATE, DELETE ON ccs.claim_calculation_grain_basket TO "app_ccs_rest_proxy";
GRANT SELECT, INSERT, UPDATE, DELETE ON ccs.claim_calculation_grain_basket_product TO "app_ccs_rest_proxy";

--Grant read only access to all tables for the readonly role
GRANT SELECT ON ALL TABLES IN SCHEMA ccs TO "app_ccs_readonly";
