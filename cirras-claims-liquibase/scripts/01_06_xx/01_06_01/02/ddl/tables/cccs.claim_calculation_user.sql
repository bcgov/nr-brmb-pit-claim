CREATE TABLE cccs.claim_calculation_user (
  claim_calculation_user_guid varchar(32)   NOT NULL,
  login_user_guid             varchar(32)   NOT NULL,
  login_user_id               varchar(64)   NOT NULL,
  login_user_type             varchar(64)   NOT NULL,
  given_name                  varchar(50),
  family_name                 varchar(50),
  create_user                 varchar(64)   NOT NULL,
  create_date                 timestamp(0)  NOT NULL,
  update_user                 varchar(64)   NOT NULL,
  update_date                 timestamp(0)  NOT NULL
) TABLESPACE pg_default
;



COMMENT ON TABLE cccs.claim_calculation_user IS 'The table contains info for users that have created or updated claim calculations.'
;
COMMENT ON COLUMN cccs.claim_calculation_user.claim_calculation_user_guid IS 'Claim Calculation User Guid is a unique key of a claims calculation user'
;
COMMENT ON COLUMN cccs.claim_calculation_user.login_user_guid IS 'Login User Guid is a unique key from the authentication for a user'
;
COMMENT ON COLUMN cccs.claim_calculation_user.login_user_id IS 'Login User Id is the userid from the authentication for a user'
;
COMMENT ON COLUMN cccs.claim_calculation_user.login_user_type IS 'Login User Type is the user type from the authentication for a user'
;
COMMENT ON COLUMN cccs.claim_calculation_user.given_name IS 'Given Name is the given name from the authentication for a user'
;
COMMENT ON COLUMN cccs.claim_calculation_user.family_name IS 'Family Name is the family name from the authentication for a user'
;
COMMENT ON COLUMN cccs.claim_calculation_user.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cccs.claim_calculation_user.create_date IS 'Create Date is the date when the record was created'
;
COMMENT ON COLUMN cccs.claim_calculation_user.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cccs.claim_calculation_user.update_date IS 'Update Date is the date when the record was updated last'
;

ALTER TABLE cccs.claim_calculation_user ADD
    CONSTRAINT pk_ccu PRIMARY KEY (claim_calculation_user_guid) USING INDEX TABLESPACE pg_default
;

ALTER TABLE cccs.claim_calculation_user ADD
    CONSTRAINT uk_ccu UNIQUE (login_user_guid) USING INDEX TABLESPACE pg_default
;
