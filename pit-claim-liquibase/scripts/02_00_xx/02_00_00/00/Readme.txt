Application:          CCS
Version:              2.0.0-00
Author:               Vivid Solutions


Description
-------------------------------------------------------------------------------
Initial deploy of Claims Calculator to OpenShift.


Changelog
-------------------------------------------------------------------------------
Feb 28 2025    Initial Commit


-------------------------------------------------------------------------------
1. DBA Scripts
-------------------------------------------------------------------------------

1.1 Change to the pit-claim-liquibase/db_preconditions directory.
    
1.2 Connect to the default database as super user in psql.
    
1.3 Manually run the following scripts. Replace any placeholders with suitable values.

database/pitcc.ddl.create_database.sql
logins/pitcc.ddl.create_login_app_ccs.sql
roles/ccs.ddl.create_roles.sql

1.4 Switch to the newly created database:

/c pitcc<env>

1.5 Run the schema creation script.

schema/pitcc.ddl.create_ccs_schema.sql
