-- SCHEMA: cccs

-- DROP SCHEMA "cccs" ;

CREATE SCHEMA "cccs"
    AUTHORIZATION postgres;

GRANT ALL ON SCHEMA "cccs" TO "app_cccs";

GRANT USAGE ON SCHEMA "cccs" TO "app_cccs_rest_proxy";

GRANT ALL ON SCHEMA "cccs" TO postgres;