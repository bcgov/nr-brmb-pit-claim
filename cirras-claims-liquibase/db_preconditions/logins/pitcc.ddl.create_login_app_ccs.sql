-- Role: "app_ccs"
-- DROP ROLE "app_ccs";

CREATE ROLE "app_ccs" WITH
  LOGIN
  NOSUPERUSER
  INHERIT
  NOCREATEDB
  CREATEROLE
  NOREPLICATION
  PASSWORD '${POSTGRES_ADMIN_PASSWORD}';
  
ALTER ROLE app_ccs SET search_path TO ccs;

ALTER USER app_ccs set TIMEZONE to 'America/New_York';

COMMENT ON ROLE "app_ccs" IS 'Claims Calculator System.';
