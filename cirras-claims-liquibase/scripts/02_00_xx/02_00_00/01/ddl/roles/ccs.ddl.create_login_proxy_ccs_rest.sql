-- Role: "proxy_ccs_rest"
-- DROP ROLE "proxy_ccs_rest";

CREATE ROLE "proxy_ccs_rest" WITH
  LOGIN
  NOSUPERUSER
  INHERIT
  NOCREATEDB
  NOCREATEROLE
  NOREPLICATION
  PASSWORD '${POSTGRES_PROXY_USER_PASSWORD}';
  
ALTER ROLE proxy_ccs_rest SET search_path TO ccs;

ALTER USER proxy_ccs_rest set TIMEZONE to 'America/New_York';

COMMENT ON ROLE "proxy_ccs_rest" IS 'Proxy account for Claims Calculator System.';

GRANT "app_ccs_rest_proxy" TO "proxy_ccs_rest";

GRANT USAGE ON SCHEMA "ccs" TO "app_ccs_rest_proxy";
