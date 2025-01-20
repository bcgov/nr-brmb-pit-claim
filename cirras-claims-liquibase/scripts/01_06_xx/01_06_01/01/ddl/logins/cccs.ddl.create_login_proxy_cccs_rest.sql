-- Role: "proxy_cccs_rest"
-- DROP ROLE "proxy_cccs_rest";

CREATE ROLE "proxy_cccs_rest" WITH
  LOGIN
  NOSUPERUSER
  INHERIT
  NOCREATEDB
  NOCREATEROLE
  NOREPLICATION
  PASSWORD 'PLACEHOLDER';
  
ALTER ROLE proxy_cccs_rest SET search_path TO cccs;

COMMENT ON ROLE "proxy_cccs_rest" IS 'Proxy account for Cirras Claims Calculator.';