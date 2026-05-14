
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


\qecho Insert claim_calculation_grain_quantity_audit
WITH t2 AS (
    SELECT claim_calc_grain_quantity_guid, update_user
    FROM claim_calculation_grain_quantity 
)
UPDATE claim_calculation_grain_quantity
SET update_user = t2.update_user
FROM t2
WHERE claim_calculation_grain_quantity.claim_calc_grain_quantity_guid = t2.claim_calc_grain_quantity_guid;


\qecho Insert claim_calculation_grain_quantity_detail_audit
WITH t2 AS (
    SELECT claim_calc_grain_quantity_detail_guid, update_user
    FROM claim_calculation_grain_quantity_detail 
)
UPDATE claim_calculation_grain_quantity_detail
SET update_user = t2.update_user
FROM t2
WHERE claim_calculation_grain_quantity_detail.claim_calc_grain_quantity_detail_guid = t2.claim_calc_grain_quantity_detail_guid;


\qecho Insert claim_calculation_grain_spot_loss_audit
WITH t2 AS (
    SELECT claim_calc_grain_spot_loss_guid, update_user
    FROM claim_calculation_grain_spot_loss 
)
UPDATE claim_calculation_grain_spot_loss
SET update_user = t2.update_user
FROM t2
WHERE claim_calculation_grain_spot_loss.claim_calc_grain_spot_loss_guid = t2.claim_calc_grain_spot_loss_guid;


\qecho Insert claim_calculation_grain_unseeded_audit
WITH t2 AS (
    SELECT claim_calc_grain_unseeded_guid, update_user
    FROM claim_calculation_grain_unseeded 
)
UPDATE claim_calculation_grain_unseeded
SET update_user = t2.update_user
FROM t2
WHERE claim_calculation_grain_unseeded.claim_calc_grain_unseeded_guid = t2.claim_calc_grain_unseeded_guid;


\qecho Insert claim_calculation_grapes_audit
WITH t2 AS (
    SELECT claim_calculation_grapes_guid, update_user
    FROM claim_calculation_grapes 
)
UPDATE claim_calculation_grapes
SET update_user = t2.update_user
FROM t2
WHERE claim_calculation_grapes.claim_calculation_grapes_guid = t2.claim_calculation_grapes_guid;


\qecho Insert claim_calculation_plant_acres_audit
WITH t2 AS (
    SELECT claim_calc_plant_acres_guid, update_user
    FROM claim_calculation_plant_acres 
)
UPDATE claim_calculation_plant_acres
SET update_user = t2.update_user
FROM t2
WHERE claim_calculation_plant_acres.claim_calc_plant_acres_guid = t2.claim_calc_plant_acres_guid;


\qecho Insert claim_calculation_plant_units_audit
WITH t2 AS (
    SELECT claim_calc_plant_units_guid, update_user
    FROM claim_calculation_plant_units 
)
UPDATE claim_calculation_plant_units
SET update_user = t2.update_user
FROM t2
WHERE claim_calculation_plant_units.claim_calc_plant_units_guid = t2.claim_calc_plant_units_guid;


\qecho Insert claim_calculation_variety_audit
WITH t2 AS (
    SELECT claim_calculation_variety_guid, update_user
    FROM claim_calculation_variety 
)
UPDATE claim_calculation_variety
SET update_user = t2.update_user
FROM t2
WHERE claim_calculation_variety.claim_calculation_variety_guid = t2.claim_calculation_variety_guid;


\o 