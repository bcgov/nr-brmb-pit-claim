-- Role: "PROXY_DAP_PICC"
-- DROP ROLE "PROXY_DAP_PICC";

CREATE ROLE "PROXY_DAP_PICC" WITH
  LOGIN
  NOSUPERUSER
  INHERIT
  NOCREATEDB
  NOCREATEROLE
  NOREPLICATION
  PASSWORD '${POSTGRES_PROXY_DAP_PASSWORD}';
  
ALTER ROLE "PROXY_DAP_PICC" SET search_path TO ccs;

COMMENT ON ROLE "PROXY_DAP_PICC" IS 'Proxy account for DAP read only access to Claim Calculator System';

--GRANT "app_ccs_readonly" TO "PROXY_DAP_PICC"; --Grant has to be applied in the terminal in the master POD