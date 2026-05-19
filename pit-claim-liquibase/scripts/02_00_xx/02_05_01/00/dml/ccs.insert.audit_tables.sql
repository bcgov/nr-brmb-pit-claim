
\o cirras.ccs.02_05_01_00.insert_audit_tables.dml.log

\qecho Insert claim_calculation_audit
WITH t2 AS (
    SELECT claim_calculation_guid, update_user
    FROM claim_calculation 
)
UPDATE claim_calculation
SET update_user = t2.update_user
FROM t2
WHERE claim_calculation.claim_calculation_guid = t2.claim_calculation_guid;

\qecho Insert claim_calculation_berries_audit
WITH t2 AS (
    SELECT claim_calculation_berries_guid, update_user
    FROM claim_calculation_berries 
)
UPDATE claim_calculation_berries
SET update_user = t2.update_user
FROM t2
WHERE claim_calculation_berries.claim_calculation_berries_guid = t2.claim_calculation_berries_guid;


\qecho Insert claim_calculation_grain_basket_audit
WITH t2 AS (
    SELECT claim_calculation_grain_basket_guid, update_user
    FROM claim_calculation_grain_basket 
)
UPDATE claim_calculation_grain_basket
SET update_user = t2.update_user
FROM t2
WHERE claim_calculation_grain_basket.claim_calculation_grain_basket_guid = t2.claim_calculation_grain_basket_guid;


\qecho Insert claim_calculation_grain_basket_product_audit
WITH t2 AS (
    SELECT claim_calc_grain_basket_product_guid, update_user
    FROM claim_calculation_grain_basket_product 
)
UPDATE claim_calculation_grain_basket_product
SET update_user = t2.update_user
FROM t2
WHERE claim_calculation_grain_basket_product.claim_calc_grain_basket_product_guid = t2.claim_calc_grain_basket_product_guid;



\o 