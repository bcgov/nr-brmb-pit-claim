CREATE TABLE ccs.calculation_status_code (
  calculation_status_code varchar(16)   NOT NULL,
  description             varchar(100)  NOT NULL,
  effective_date          date          NOT NULL,
  expiry_date             date          NOT NULL,
  create_user             varchar(64)   NOT NULL,
  create_date             timestamp(0)  NOT NULL,
  update_user             varchar(64)   NOT NULL,
  update_date             timestamp(0)  NOT NULL
) TABLESPACE pg_default
;



COMMENT ON TABLE ccs.calculation_status_code IS 'The table contains all calculation status codes'
;
COMMENT ON COLUMN ccs.calculation_status_code.calculation_status_code IS 'Calculation Status Cod IS the status code of the calculation'
;
COMMENT ON COLUMN ccs.calculation_status_code.description IS 'Description is the name of the calculation status'
;
COMMENT ON COLUMN ccs.calculation_status_code.effective_date IS 'Effective Date is the date when the record was first active'
;
COMMENT ON COLUMN ccs.calculation_status_code.expiry_date IS 'Expiry Date is the date when the record was last active'
;
COMMENT ON COLUMN ccs.calculation_status_code.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN ccs.calculation_status_code.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN ccs.calculation_status_code.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN ccs.calculation_status_code.update_date IS 'Update Date is the date when the record was updated last.'
;

ALTER TABLE ccs.calculation_status_code ADD
    CONSTRAINT pk_casc PRIMARY KEY (calculation_status_code) USING INDEX TABLESPACE pg_default
;
