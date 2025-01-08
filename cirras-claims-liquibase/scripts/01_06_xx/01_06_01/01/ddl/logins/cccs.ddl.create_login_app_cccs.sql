-- Role: "app_cccs"
-- DROP ROLE "app_cccs";

CREATE ROLE "app_cccs" WITH
  LOGIN
  NOSUPERUSER
  INHERIT
  NOCREATEDB
  NOCREATEROLE
  NOREPLICATION
  PASSWORD 'PLACEHOLDER';
  
ALTER ROLE app_cccs SET search_path TO cccs;

COMMENT ON ROLE "app_cccs" IS 'Cirras Claims Calculator.';
