SPOOL cirras.01_06_03_00.ddl.lst

--Remove database access to proxy user
@@ddl/revoke_grants.sql

SPOOL OFF
