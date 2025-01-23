-- Database: pitcc${ENV}

-- DROP DATABASE "pitcc${ENV}";

CREATE DATABASE "pitcc${ENV}"
    WITH 
    OWNER = postgres
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

COMMENT ON DATABASE "pitcc${ENV}"
    IS 'pitcc${ENV} database containing schemas used by production insurance applications and services.';

GRANT TEMPORARY, CONNECT ON DATABASE "pitcc${ENV}" TO PUBLIC;

GRANT ALL ON DATABASE "pitcc${ENV}" TO postgres;