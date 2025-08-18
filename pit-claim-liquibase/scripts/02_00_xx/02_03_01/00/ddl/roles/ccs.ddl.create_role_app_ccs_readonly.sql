-- Role: "app_ccs_readonly"
-- DROP ROLE "app_ccs_readonly";

CREATE ROLE "app_ccs_readonly";

GRANT USAGE ON SCHEMA "ccs" TO "app_ccs_readonly";

--Grant read only access to all tables for the readonly role
GRANT SELECT ON ALL TABLES IN SCHEMA ccs TO "app_ccs_readonly";
