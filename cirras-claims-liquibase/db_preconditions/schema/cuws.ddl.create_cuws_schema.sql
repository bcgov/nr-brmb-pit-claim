-- SCHEMA: ccs

-- DROP SCHEMA "ccs" ;

CREATE SCHEMA "ccs"
    AUTHORIZATION postgres;

GRANT ALL ON SCHEMA "ccs" TO "app_ccs";

GRANT ALL ON SCHEMA "ccs" TO postgres;

ALTER SCHEMA "ccs" OWNER TO "app_ccs";