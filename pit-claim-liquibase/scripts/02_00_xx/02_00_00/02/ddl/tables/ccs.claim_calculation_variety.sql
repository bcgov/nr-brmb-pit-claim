CREATE TABLE ccs.claim_calculation_variety (
  claim_calculation_variety_guid varchar(32)    NOT NULL,
  claim_calculation_guid         varchar(32)    NOT NULL,
  crop_variety_id                numeric(9)     NOT NULL,
  revision_count                 numeric(10),
  average_price                  numeric(8,4),
  insurable_value                numeric(12,4),
  yield_assessed_reason          varchar(1000),
  yield_assessed                 numeric(10,2),
  yield_total                    numeric(12,4),
  yield_actual                   numeric(12,4),
  variety_production_value       numeric(10,2),
  create_user                    varchar(64)    NOT NULL,
  create_date                    timestamp(0)   NOT NULL,
  update_user                    varchar(64)    NOT NULL,
  update_date                    timestamp(0)   NOT NULL,
  average_price_override         numeric(8,4),
  average_price_final            numeric(8,4)
) TABLESPACE pg_default
;



COMMENT ON TABLE ccs.claim_calculation_variety IS 'The table contains the detailed variety data used to calculate the production value of the harvest to be deducted from the claim amount'
;
COMMENT ON COLUMN ccs.claim_calculation_variety.claim_calculation_variety_guid IS 'Claim Calculation Variety Guid is a unique key of a claims calculation variety record'
;
COMMENT ON COLUMN ccs.claim_calculation_variety.claim_calculation_guid IS 'Claim Calculation Guid is a unique key of a claims calculation'
;
COMMENT ON COLUMN ccs.claim_calculation_variety.crop_variety_id IS 'Crop Variety Id is a unique Id of a variety from CROP_VARIETY'
;
COMMENT ON COLUMN ccs.claim_calculation_variety.average_price IS 'Average Price is the average contracted price of the last 5 years. If there are no contracted prices it''s the 100% IV. Imported from CIRRAS'
;
COMMENT ON COLUMN ccs.claim_calculation_variety.insurable_value IS 'Insurable Value is the IV per Lb and is calculated AVERAGE_PRICE * (CLAIM_CALCULATION.INSURABLE_VALUE_SELECTED / INSURABLE_VALUE_HUNDRED_PERCENT)'
;
COMMENT ON COLUMN ccs.claim_calculation_variety.yield_assessed_reason IS 'Yield Assessed Reason is the reason why the yield is different from the actual yield'
;
COMMENT ON COLUMN ccs.claim_calculation_variety.yield_assessed IS 'Yield Assessed is the difference between the Actual Yield and the yield found in the assessment'
;
COMMENT ON COLUMN ccs.claim_calculation_variety.yield_total IS 'Yield Total is the sum of YIELD_ACTUAL and YIELD_ASSESSED'
;
COMMENT ON COLUMN ccs.claim_calculation_variety.yield_actual IS 'Yield Actual the yield for that variety and is entered by the user'
;
COMMENT ON COLUMN ccs.claim_calculation_variety.variety_production_value IS 'Variety Production Value is the total production dollars of the variety. YIELD_TOTAL * INSURABLE_VALUE'
;
COMMENT ON COLUMN ccs.claim_calculation_variety.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN ccs.claim_calculation_variety.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN ccs.claim_calculation_variety.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN ccs.claim_calculation_variety.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON COLUMN ccs.claim_calculation_variety.average_price_override IS 'Average Price Override is the average contracted price of the last 5 years. Entered by the user in the claims calculator'
;
COMMENT ON COLUMN ccs.claim_calculation_variety.average_price_final IS 'Average Price Final is the average contracted price of the last 5 years. This is the value used in the calculations. It''s equal to AVERAGE_PRICE_OVERRIDE if a value exists else it''s AVERAGE_PRICE'
;

CREATE INDEX ix_ccv_cc ON ccs.claim_calculation_variety(claim_calculation_guid)
 TABLESPACE pg_default
;

CREATE INDEX ix_ccv_cva ON ccs.claim_calculation_variety(crop_variety_id)
 TABLESPACE pg_default
;

ALTER TABLE ccs.claim_calculation_variety ADD
    CONSTRAINT pk_ccv PRIMARY KEY (claim_calculation_variety_guid) USING INDEX TABLESPACE pg_default
;

ALTER TABLE ccs.claim_calculation_variety ADD
    CONSTRAINT uk_ccg_cvi UNIQUE (claim_calculation_guid, crop_variety_id) USING INDEX TABLESPACE pg_default
;

ALTER TABLE ccs.claim_calculation_variety ADD CONSTRAINT fk_ccv_cc
    FOREIGN KEY (claim_calculation_guid)
    REFERENCES ccs.claim_calculation(claim_calculation_guid)
;

ALTER TABLE ccs.claim_calculation_variety ADD CONSTRAINT fk_ccv_cva
    FOREIGN KEY (crop_variety_id)
    REFERENCES ccs.crop_variety(crop_variety_id)
;
