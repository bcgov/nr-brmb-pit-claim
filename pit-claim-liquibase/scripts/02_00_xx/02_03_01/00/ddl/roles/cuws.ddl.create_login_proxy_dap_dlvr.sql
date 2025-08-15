-- Role: "PROXY_DAP_PICC_DEV"
-- DROP ROLE "PROXY_DAP_PICC_DEV";

CREATE ROLE "PROXY_DAP_PICC_DEV" WITH
  LOGIN
  NOSUPERUSER
  INHERIT
  NOCREATEDB
  NOCREATEROLE
  NOREPLICATION
  PASSWORD '${POSTGRES_PROXY_DAP_PASSWORD}';
  
ALTER ROLE "PROXY_DAP_PICC_DEV" SET search_path TO cuws;

COMMENT ON ROLE "PROXY_DAP_PICC_DEV" IS 'Proxy account for DAP read only access to Claim Calculator System in DVLR';

--GRANT "app_ccs_readonly" TO "PROXY_DAP_PICC_DEV"; --Grant has to be applied in the terminal in the master POD