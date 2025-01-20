CREATE TABLE cccs.claim_calculation_grapes (
  claim_calculation_grapes_guid   varchar(32)   NOT NULL,
  claim_calculation_guid          varchar(32)   NOT NULL,
  insurable_value_selected        numeric(12,4) NOT NULL,
  insurable_value_hundred_percent numeric(12,4) NOT NULL,
  coverage_amount                 numeric(10,2) NOT NULL,
  coverage_amount_assessed        numeric(10,2),
  coverage_assessed_reason        varchar(1000),
  coverage_amount_adjusted        numeric(10,2),
  total_production_value          numeric(10,2),
  revision_count                  numeric(10),
  create_user                     varchar(64)   NOT NULL,
  create_date                     timestamp(0)  NOT NULL,
  update_user                     varchar(64)   NOT NULL,
  update_date                     timestamp(0)  NOT NULL
) TABLESPACE pg_default
;



COMMENT ON TABLE cccs.claim_calculation_grapes IS 'The table contains the claim calculation data specific to the insurance plan Grapes'
;
COMMENT ON COLUMN cccs.claim_calculation_grapes.claim_calculation_grapes_guid IS 'Claim Calculation Grapes Guid is a unique key of a claims calculation berries record'
;
COMMENT ON COLUMN cccs.claim_calculation_grapes.claim_calculation_guid IS 'Claim Calculation Guid is a unique key of a claims calculation'
;
COMMENT ON COLUMN cccs.claim_calculation_grapes.insurable_value_selected IS 'Insurable Value Selected is the selected IV in the product purchase'
;
COMMENT ON COLUMN cccs.claim_calculation_grapes.insurable_value_hundred_percent IS 'Insurable Value Hundred Percent is the 100% IV of the product'
;
COMMENT ON COLUMN cccs.claim_calculation_grapes.coverage_amount IS 'Coverage Amount is the total coverage from the product purchase'
;
COMMENT ON COLUMN cccs.claim_calculation_grapes.coverage_amount_assessed IS 'Coverage Amount Assessed is the difference of the total coverage amount (COVERAGE_AMOUNT) and the adjusted coverage amount (COVERAGE_AMOUNT_ADJUSTED)'
;
COMMENT ON COLUMN cccs.claim_calculation_grapes.coverage_assessed_reason IS 'Coverage Assessed Reason is the reason why the coverage is assessed lower than in the purchase'
;
COMMENT ON COLUMN cccs.claim_calculation_grapes.coverage_amount_adjusted IS 'Coverage Amount Adjusted is the adjusted coverage COVERAGE_AMOUNT - COVERAGE_AMOUNT_ASSESSED'
;
COMMENT ON COLUMN cccs.claim_calculation_grapes.total_production_value IS 'Total Production Value is the total value of all yield. It''s deducted from the adjusted coverage amount to get the claim amount'
;
COMMENT ON COLUMN cccs.claim_calculation_grapes.revision_count IS 'Revision Count is the number of updates of the record'
;
COMMENT ON COLUMN cccs.claim_calculation_grapes.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cccs.claim_calculation_grapes.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cccs.claim_calculation_grapes.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cccs.claim_calculation_grapes.update_date IS 'Update Date is the date when the record was updated last.'
;

CREATE INDEX ix_ccg_cc ON cccs.claim_calculation_grapes(claim_calculation_guid)
 TABLESPACE pg_default
;

ALTER TABLE cccs.claim_calculation_grapes ADD
    CONSTRAINT pk_ccg PRIMARY KEY (claim_calculation_grapes_guid) USING INDEX TABLESPACE pg_default
;

ALTER TABLE cccs.claim_calculation_grapes ADD CONSTRAINT fk_ccg_cc
    FOREIGN KEY (claim_calculation_guid)
    REFERENCES cccs.claim_calculation(claim_calculation_guid)
;
