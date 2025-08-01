package ca.bc.gov.mal.cirras.claims.service.api.v1.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculation;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationGrainBasket;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationGrainBasketProduct;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationGrainQuantityDetail;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationGrainUnseeded;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationGrainSpotLoss;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationVariety;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.CropCommodityDto;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.ProductRsrc;
import ca.bc.gov.mal.cirras.policies.model.v1.InsuranceClaim;
import ca.bc.gov.mal.cirras.policies.model.v1.Product;
import ca.bc.gov.mal.cirras.policies.model.v1.Variety;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldContractSimple;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldGrainBasket;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldSummary;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class OutOfSync {
	
	private static final Logger logger = LoggerFactory.getLogger(OutOfSync.class);
	private DtoUtils dtoUtils;
	

	public void calculateOutOfSyncFlags(
			ClaimCalculation claimCalculation, 
			InsuranceClaim insuranceClaim, 
			Product product,
			VerifiedYieldSummary verifiedSummary) {
		logger.debug("<calculateOutOfSyncFlags");

		if (claimCalculation == null || insuranceClaim == null) {
			logger.warn("<claimCalculation or insuranceClaim was null. Out of sync flags not set.");
			return;
		} else if (product == null && claimCalculation.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAIN.toString())
				&& (claimCalculation.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.CropUnseeded.getCode())
						|| claimCalculation.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.GrainSpotLoss.getCode())
						|| claimCalculation.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.QuantityGrain.getCode()))) {
			logger.warn("<product was null. Out of sync flags not set.");
			return;
		} else if (verifiedSummary == null 
					&& claimCalculation.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.QuantityGrain.getCode())) {
			logger.warn("<verified yield was null. Out of sync flags not set.");
			return;
		}
		// TODO: Add additional checks.

		dtoUtils = new DtoUtils(logger);

		boolean isOutOfSync = false;

		// Set Claim Calculation flags
		isOutOfSync = claimDataOutOfSync(claimCalculation, insuranceClaim, isOutOfSync);
		
		// Set Grapes Quantity flags
		isOutOfSync = grapesQuantityDataOutOfSync(claimCalculation, insuranceClaim, isOutOfSync);

		// Set Varieties flags
		isOutOfSync = varietiesDataOutOfSync(claimCalculation, insuranceClaim, isOutOfSync);

		// Set Berries Quantity flags
		isOutOfSync = berriesQuantityDataOutOfSync(claimCalculation, insuranceClaim, isOutOfSync);
		
		// Set Plant By Units Data flags
		isOutOfSync = plantByUnitsDataOutOfSync(claimCalculation, insuranceClaim, isOutOfSync);

		// Set Plant By Acres Data flags
		isOutOfSync = plantByAcresDataOutOfSync(claimCalculation, insuranceClaim, isOutOfSync);

		// Set Grain Unseeded Data flags
		isOutOfSync = grainUnseededDataOutOfSync(claimCalculation, product, isOutOfSync);
		
		// Set Grain Spot Loss Data flags
		isOutOfSync = grainSpotLossDataOutOfSync(claimCalculation, product, isOutOfSync);
		
		// Set Grain Quantity Loss Data flags
		isOutOfSync = grainQuantityDetailDataOutOfSync(claimCalculation, product, verifiedSummary, isOutOfSync);

		// Set Grain Basket Data flags
		isOutOfSync = grainBasketDataOutOfSync(claimCalculation, product, null, isOutOfSync); // TODO: Add missing params.

		// Set Grain Basket Product Data flags
		isOutOfSync = grainBasketProductsDataOutOfSync(claimCalculation, null, null, null, null, null, isOutOfSync);  // TODO: Add missing params.
		
		claimCalculation.setIsOutOfSync(isOutOfSync);
		
		logger.debug(">calculateOutOfSyncFlags");
	}

	private boolean plantByUnitsDataOutOfSync(ClaimCalculation claimCalculation, InsuranceClaim insuranceClaim, boolean isOutOfSync) {

		if (claimCalculation.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.Plant.getCode())
				&&claimCalculation.getInsuredByMeasurementType().equalsIgnoreCase(ClaimsServiceEnums.InsuredByMeasurementType.UNITS.toString())
				&& claimCalculation.getClaimCalculationPlantUnits() != null) {
				
			//Plant units

			if (dtoUtils.equals("plantDeclaredUnits", insuranceClaim.getPlantDeclaredUnits(),
					claimCalculation.getClaimCalculationPlantUnits().getInsuredUnits(), 4)) {
				claimCalculation.getClaimCalculationPlantUnits().setIsOutOfSyncInsuredUnits(false);
			} else {
				claimCalculation.getClaimCalculationPlantUnits().setIsOutOfSyncInsuredUnits(true);
				isOutOfSync = true;
			}

			if (dtoUtils.equals("deductibleLevel", insuranceClaim.getDeductibleLevel(),
					claimCalculation.getClaimCalculationPlantUnits().getDeductibleLevel())) {
				claimCalculation.getClaimCalculationPlantUnits().setIsOutOfSyncDeductibleLevel(false);
			} else {
				claimCalculation.getClaimCalculationPlantUnits().setIsOutOfSyncDeductibleLevel(true);
				isOutOfSync = true;
			}

			if (dtoUtils.equals("plantInsurableValue", insuranceClaim.getPlantInsurableValue(),
					claimCalculation.getClaimCalculationPlantUnits().getInsurableValue(), 4)) {
				claimCalculation.getClaimCalculationPlantUnits().setIsOutOfSyncInsurableValue(false);
			} else {
				claimCalculation.getClaimCalculationPlantUnits().setIsOutOfSyncInsurableValue(true);
				isOutOfSync = true;
			}
			
		}
		return isOutOfSync;
	}

	private boolean plantByAcresDataOutOfSync(ClaimCalculation claimCalculation, InsuranceClaim insuranceClaim, boolean isOutOfSync) {

		if (claimCalculation.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.Plant.getCode())
			&&claimCalculation.getInsuredByMeasurementType().equalsIgnoreCase(ClaimsServiceEnums.InsuredByMeasurementType.ACRES.toString())
			&& claimCalculation.getClaimCalculationPlantAcres() != null) {
			
			//Plant acres

			if (dtoUtils.equals("plantDeclaredAcres", insuranceClaim.getPlantDeclaredAcres(),
					claimCalculation.getClaimCalculationPlantAcres().getDeclaredAcres(), 4)) {
				claimCalculation.getClaimCalculationPlantAcres().setIsOutOfSyncDeclaredAcres(false);
			} else {
				claimCalculation.getClaimCalculationPlantAcres().setIsOutOfSyncDeclaredAcres(true);
				isOutOfSync = true;
			}

			if (dtoUtils.equals("deductibleLevel", insuranceClaim.getDeductibleLevel(),
					claimCalculation.getClaimCalculationPlantAcres().getDeductibleLevel())) {
				claimCalculation.getClaimCalculationPlantAcres().setIsOutOfSyncDeductibleLevel(false);
			} else {
				claimCalculation.getClaimCalculationPlantAcres().setIsOutOfSyncDeductibleLevel(true);
				isOutOfSync = true;
			}

			if (dtoUtils.equals("plantInsurableValue", insuranceClaim.getPlantInsurableValue(),
					claimCalculation.getClaimCalculationPlantAcres().getInsurableValue(), 4)) {
				claimCalculation.getClaimCalculationPlantAcres().setIsOutOfSyncInsurableValue(false);
			} else {
				claimCalculation.getClaimCalculationPlantAcres().setIsOutOfSyncInsurableValue(true);
				isOutOfSync = true;
			}
		}
		return isOutOfSync;
	}
	
	private boolean berriesQuantityDataOutOfSync(ClaimCalculation claimCalculation, InsuranceClaim insuranceClaim, boolean isOutOfSync) {
		if (claimCalculation.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.BERRIES.toString())
				&& claimCalculation.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.Quantity.getCode())) {
			if (claimCalculation.getClaimCalculationBerries() != null) {

				if (dtoUtils.equals("totalProbableYield", insuranceClaim.getTotalProbableYield(),
						claimCalculation.getClaimCalculationBerries().getTotalProbableYield(), 4)) {
					claimCalculation.getClaimCalculationBerries().setIsOutOfSyncTotalProbableYield(false);
				} else {
					claimCalculation.getClaimCalculationBerries().setIsOutOfSyncTotalProbableYield(true);
					isOutOfSync = true;
				}

				if (dtoUtils.equals("deductibleLevel", insuranceClaim.getDeductibleLevel(),
						claimCalculation.getClaimCalculationBerries().getDeductibleLevel())) {
					claimCalculation.getClaimCalculationBerries().setIsOutOfSyncDeductibleLevel(false);
				} else {
					claimCalculation.getClaimCalculationBerries().setIsOutOfSyncDeductibleLevel(true);
					isOutOfSync = true;
				}

				if (dtoUtils.equals("productionGuarantee", insuranceClaim.getProductionGuarantee(),
						claimCalculation.getClaimCalculationBerries().getProductionGuarantee(), 4)) {
					claimCalculation.getClaimCalculationBerries().setIsOutOfSyncProductionGuarantee(false);
				} else {
					claimCalculation.getClaimCalculationBerries().setIsOutOfSyncProductionGuarantee(true);
					isOutOfSync = true;
				}

				if (dtoUtils.equals("declaredAcres", insuranceClaim.getDeclaredAcres(),
						claimCalculation.getClaimCalculationBerries().getDeclaredAcres(), 4)) {
					claimCalculation.getClaimCalculationBerries().setIsOutOfSyncDeclaredAcres(false);
				} else {
					claimCalculation.getClaimCalculationBerries().setIsOutOfSyncDeclaredAcres(true);
					isOutOfSync = true;
				}

				if (dtoUtils.equals("insurableValueSelected", insuranceClaim.getInsurableValueSelected(),
						claimCalculation.getClaimCalculationBerries().getInsurableValueSelected(), 4)) {
					claimCalculation.getClaimCalculationBerries().setIsOutOfSyncInsurableValueSelected(false);
				} else {
					claimCalculation.getClaimCalculationBerries().setIsOutOfSyncInsurableValueSelected(true);
					isOutOfSync = true;
				}
		
				if (dtoUtils.equals("insurableValueHundredPercent", insuranceClaim.getInsurableValueHundredPercent(),
						claimCalculation.getClaimCalculationBerries().getInsurableValueHundredPercent(), 4)) {
					claimCalculation.getClaimCalculationBerries().setIsOutOfSyncInsurableValueHundredPct(false);
				} else {
					claimCalculation.getClaimCalculationBerries().setIsOutOfSyncInsurableValueHundredPct(true);
					isOutOfSync = true;
				}
				
			}
		}
		return isOutOfSync;
	}

	private boolean varietiesDataOutOfSync(ClaimCalculation claimCalculation, InsuranceClaim insuranceClaim, boolean isOutOfSync) {

		//Only used for Grapes Quantity claims at the moment
		if (claimCalculation.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAPES.toString())
				&& claimCalculation.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.Quantity.getCode())) {
		
			// Claim Calculation Variety flags
			Set<Integer> claimCalcVarietyIds = new HashSet<Integer>();
			Map<Integer, Variety> insClaimVarietyMap = new HashMap<Integer, Variety>();
	
			// Store calculation varieties
			if (claimCalculation.getVarieties() != null && claimCalculation.getVarieties().size() > 0) {
				for (ClaimCalculationVariety variety : claimCalculation.getVarieties()) {
					claimCalcVarietyIds.add(variety.getCropVarietyId());
				}
			}
	
			// Store CIRRAS varieties
			if (insuranceClaim.getVarieties() != null && insuranceClaim.getVarieties().size() > 0) {
				for (Variety variety : insuranceClaim.getVarieties()) {
					insClaimVarietyMap.put(variety.getCropVarietyId(), variety);
				}
			}
	
			// Checks if the varieties in CIRRAS match the varieties in the calculation
			if (claimCalcVarietyIds.containsAll(insClaimVarietyMap.keySet())) {
				claimCalculation.setIsOutOfSyncVarietyAdded(false);
			} else {
				claimCalculation.setIsOutOfSyncVarietyAdded(true);
				isOutOfSync = true;
			}
	
			if (claimCalculation.getVarieties() != null && claimCalculation.getVarieties().size() > 0) {
				// Iterating through calculation varieties
				for (ClaimCalculationVariety claimCalcVariety : claimCalculation.getVarieties()) {
	
					// Cirras variety
					Variety insClaimVariety = insClaimVarietyMap.get(claimCalcVariety.getCropVarietyId());
	
					if (insClaimVariety != null) {
						claimCalcVariety.setIsOutOfSyncVarietyRemoved(false);
					} else {
						claimCalcVariety.setIsOutOfSyncVarietyRemoved(true);
						isOutOfSync = true;
					}
	
					// CIRRAS only provides an average price if the commodity uses IIV
					if (claimCalculation.getCalculateIivInd().equalsIgnoreCase("Y")) {
						if (insClaimVariety == null || 
							dtoUtils.equals("averagePrice", insClaimVariety.getAveragePrice(), claimCalcVariety.getAveragePrice(), 4)) {
							claimCalcVariety.setIsOutOfSyncAvgPrice(false);
						} else {
							claimCalcVariety.setIsOutOfSyncAvgPrice(true);
							isOutOfSync = true;
						}
					} else {
						//If it doesn't use IIV set to false. No other checks needed
						claimCalcVariety.setIsOutOfSyncAvgPrice(false);
					}
	
				}
			}
		}
		return isOutOfSync;
	}

	private boolean grapesQuantityDataOutOfSync(ClaimCalculation claimCalculation, InsuranceClaim insuranceClaim, boolean isOutOfSync) {
		if (claimCalculation.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAPES.toString())
				&& claimCalculation.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.Quantity.getCode())) {
			if (claimCalculation.getClaimCalculationGrapes() != null) {

				if (dtoUtils.equals("insurableValueSelected", insuranceClaim.getInsurableValueSelected(),
						claimCalculation.getClaimCalculationGrapes().getInsurableValueSelected(), 4)) {
					claimCalculation.getClaimCalculationGrapes().setIsOutOfSyncInsurableValueSelected(false);
				} else {
					claimCalculation.getClaimCalculationGrapes().setIsOutOfSyncInsurableValueSelected(true);
					isOutOfSync = true;
				}
		
				if (dtoUtils.equals("insurableValueHundredPercent", insuranceClaim.getInsurableValueHundredPercent(),
						claimCalculation.getClaimCalculationGrapes().getInsurableValueHundredPercent(), 4)) {
					claimCalculation.getClaimCalculationGrapes().setIsOutOfSyncInsurableValueHundredPct(false);
				} else {
					claimCalculation.getClaimCalculationGrapes().setIsOutOfSyncInsurableValueHundredPct(true);
					isOutOfSync = true;
				}
		
				if (dtoUtils.equals("coverageAmount", insuranceClaim.getCoverageAmount(), claimCalculation.getClaimCalculationGrapes().getCoverageAmount(), 2)) {
					claimCalculation.getClaimCalculationGrapes().setIsOutOfSyncCoverageAmount(false);
				} else {
					claimCalculation.getClaimCalculationGrapes().setIsOutOfSyncCoverageAmount(true);
					isOutOfSync = true;
				}
			}
		}
		return isOutOfSync;
	}

	private boolean grainUnseededDataOutOfSync(ClaimCalculation claimCalculation, Product product, boolean isOutOfSync) {

		if (claimCalculation.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAIN.toString())
				&& claimCalculation.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.CropUnseeded.getCode())
				&& claimCalculation.getClaimCalculationGrainUnseeded() != null ) {
			
			ClaimCalculationGrainUnseeded unseeded = claimCalculation.getClaimCalculationGrainUnseeded();
			
			if (dtoUtils.equals("InsuredAcres", product.getAcres(), unseeded.getInsuredAcres(), 4)) {
				unseeded.setIsOutOfSyncInsuredAcres(false);
			} else {
				unseeded.setIsOutOfSyncInsuredAcres(true);
				isOutOfSync = true;
			}

			if (dtoUtils.equals("DeductibleLevel", product.getDeductibleLevel(), unseeded.getDeductibleLevel())) {
				unseeded.setIsOutOfSyncDeductibleLevel(false);
			} else {
				unseeded.setIsOutOfSyncDeductibleLevel(true);
				isOutOfSync = true;
			}

			if (dtoUtils.equals("InsurableValue", product.getUnseededSelectedInsurableValue(), unseeded.getInsurableValue(), 4)) {
				unseeded.setIsOutOfSyncInsurableValue(false);
			} else {
				unseeded.setIsOutOfSyncInsurableValue(true);
				isOutOfSync = true;
			}
			
		}

		return isOutOfSync;
	}
	
	private boolean grainSpotLossDataOutOfSync(ClaimCalculation claimCalculation, Product product, boolean isOutOfSync) {

		if (claimCalculation.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAIN.toString())
				&& claimCalculation.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.GrainSpotLoss.getCode())
				&& claimCalculation.getClaimCalculationGrainSpotLoss() != null ) {
			
			ClaimCalculationGrainSpotLoss spotLoss = claimCalculation.getClaimCalculationGrainSpotLoss();
			
			if (dtoUtils.equals("InsuredAcres", product.getAcres(), spotLoss.getInsuredAcres(), 4)) {
				spotLoss.setIsOutOfSyncInsuredAcres(false);
			} else {
				spotLoss.setIsOutOfSyncInsuredAcres(true);
				isOutOfSync = true;
			}

			if (dtoUtils.equals("CoverageAmtPerAcre", product.getSpotLossCoverageAmountPerAcre(), spotLoss.getCoverageAmtPerAcre(), 4)) {
				spotLoss.setIsOutOfSyncCoverageAmtPerAcre(false);
			} else {
				spotLoss.setIsOutOfSyncCoverageAmtPerAcre(true);
				isOutOfSync = true;
			}

			if (dtoUtils.equals("CoverageValue", product.getCoverageDollars(), spotLoss.getCoverageValue(), 4)) {
				spotLoss.setIsOutOfSyncCoverageValue(false);
			} else {
				spotLoss.setIsOutOfSyncCoverageValue(true);
				isOutOfSync = true;
			}
			
		}

		return isOutOfSync;
	}	
	
	private boolean grainQuantityDetailDataOutOfSync(
			ClaimCalculation claimCalculation, 
			Product product,
			VerifiedYieldSummary verifiedSummary,
			boolean isOutOfSync) {

		if (claimCalculation.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAIN.toString())
				&& claimCalculation.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.QuantityGrain.getCode())
				&& claimCalculation.getClaimCalculationGrainQuantityDetail() != null ) {
			
			ClaimCalculationGrainQuantityDetail quantityDetail = claimCalculation.getClaimCalculationGrainQuantityDetail();
			
			if (dtoUtils.equals("InsuredAcres", product.getAcres(), quantityDetail.getInsuredAcres(), 4)) {
				quantityDetail.setIsOutOfSyncInsuredAcres(false);
			} else {
				quantityDetail.setIsOutOfSyncInsuredAcres(true);
				isOutOfSync = true;
			}

			if (dtoUtils.equals("ProbableYield", product.getProbableYield(), quantityDetail.getProbableYield(), 4)) {
				quantityDetail.setIsOutOfSyncProbableYield(false);
			} else {
				quantityDetail.setIsOutOfSyncProbableYield(true);
				isOutOfSync = true;
			}

			if (dtoUtils.equals("Deductible", product.getDeductibleLevel(), quantityDetail.getDeductible())) {
				quantityDetail.setIsOutOfSyncDeductible(false);
			} else {
				quantityDetail.setIsOutOfSyncDeductible(true);
				isOutOfSync = true;
			}

			if (dtoUtils.equals("InsurableValue", product.getSelectedInsurableValue(), quantityDetail.getInsurableValue(), 4)) {
				quantityDetail.setIsOutOfSyncInsurableValue(false);
			} else {
				quantityDetail.setIsOutOfSyncInsurableValue(true);
				isOutOfSync = true;
			}

			if (dtoUtils.equals("ProductionGuaranteeWeight", product.getProductionGuarantee(), quantityDetail.getProductionGuaranteeWeight(), 4)) {
				quantityDetail.setIsOutOfSyncProductionGuaranteeWeight(false);
			} else {
				quantityDetail.setIsOutOfSyncProductionGuaranteeWeight(true);
				isOutOfSync = true;
			}

			
			if (dtoUtils.equals("CoverageValue", product.getCoverageDollars(), quantityDetail.getCoverageValue(), 4)) {
				quantityDetail.setIsOutOfSyncCoverageValue(false);
			} else {
				quantityDetail.setIsOutOfSyncCoverageValue(true);
				isOutOfSync = true;
			}
			
			Double verifiedYieldToCount = getVerifiedYieldToCount(verifiedSummary);

			if (dtoUtils.equals("TotalYieldToCount", verifiedYieldToCount, quantityDetail.getTotalYieldToCount(), 4)) {
				quantityDetail.setIsOutOfSyncTotalYieldToCount(false);
			} else {
				quantityDetail.setIsOutOfSyncTotalYieldToCount(true);
				isOutOfSync = true;
			}

			
		}

		return isOutOfSync;
	}
	
	private Double getVerifiedYieldToCount(VerifiedYieldSummary vys) {
		
		Double verifiedYieldToCount = 0.0;
		if ( vys != null && vys.getYieldToCount() != null) { 
			verifiedYieldToCount = vys.getYieldToCount();
		}
		return verifiedYieldToCount;
	}

	private boolean grainBasketDataOutOfSync(
			ClaimCalculation claimCalculation, 
			Product product,
			VerifiedYieldContractSimple verifiedYield,
			boolean isOutOfSync) {

		if (claimCalculation.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAIN.toString())
				&& claimCalculation.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.GrainBasket.getCode())
				&& claimCalculation.getClaimCalculationGrainBasket() != null ) {
			
			ClaimCalculationGrainBasket grainBasket = claimCalculation.getClaimCalculationGrainBasket();
			
			if (dtoUtils.equals("GrainBasketCoverageValue", product.getCoverageDollars(), grainBasket.getGrainBasketCoverageValue(), 4)) {
				grainBasket.setIsOutOfSyncGrainBasketCoverageValue(false);
			} else {
				grainBasket.setIsOutOfSyncGrainBasketCoverageValue(true);
				isOutOfSync = true;
			}

			if (dtoUtils.equals("GrainBasketDeductible", product.getDeductibleLevel(), grainBasket.getGrainBasketDeductible())) {
				grainBasket.setIsOutOfSyncGrainBasketDeductible(false);
			} else {
				grainBasket.setIsOutOfSyncGrainBasketDeductible(true);
				isOutOfSync = true;
			}

			VerifiedYieldGrainBasket vygb = verifiedYield.getVerifiedYieldGrainBasket();
			if (vygb != null && dtoUtils.equals("GrainBasketHarvestedValue", vygb.getHarvestedValue(), grainBasket.getGrainBasketHarvestedValue(), 4)) {
				grainBasket.setIsOutOfSyncGrainBasketHarvestedValue(false);
			} else {
				grainBasket.setIsOutOfSyncGrainBasketHarvestedValue(true);
				isOutOfSync = true;
			}
		}

		return isOutOfSync;
	}

	private boolean grainBasketProductsDataOutOfSync(ClaimCalculation claimCalculation, 
			                                         VerifiedYieldContractSimple verifiedYield,
			                                         List<ProductRsrc> quantityProducts,
			                                         Map<Integer, ClaimDto> quantityClaimMap,
			                                         Map<Integer, CropCommodityDto> quantityCropMap,
			                                         Map<Integer, CropCommodityDto> quantityLinkedCropMap,
			                                         boolean isOutOfSync) {

		if (claimCalculation.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAIN.toString())
				&& claimCalculation.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.GrainBasket.getCode())
				&& claimCalculation.getClaimCalculationGrainBasketProducts() != null ) {
		
			Set<Integer> claimCalcProductCommodityIds = new HashSet<Integer>();
			Map<Integer, ProductRsrc> insProductCommodityMap = new HashMap<Integer, ProductRsrc>();
	
			// Store calculation grain basket products
			for (ClaimCalculationGrainBasketProduct product : claimCalculation.getClaimCalculationGrainBasketProducts()) {
				claimCalcProductCommodityIds.add(product.getCropCommodityId());
			}
	
			// Store CIRRAS products
			for (ProductRsrc prd : quantityProducts) {
				insProductCommodityMap.put(prd.getCropCommodityId(), prd);
			}
	
			// Checks if the products in CIRRAS match the products in the calculation
			if (claimCalcProductCommodityIds.containsAll(insProductCommodityMap.keySet())) {
				claimCalculation.setIsOutOfSyncGrainBasketProductAdded(false);
			} else {
				claimCalculation.setIsOutOfSyncGrainBasketProductAdded(true);
				isOutOfSync = true;
			}
	
			for (ClaimCalculationGrainBasketProduct claimCalcPrd : claimCalculation.getClaimCalculationGrainBasketProducts()) {

				ProductRsrc insPrd = insProductCommodityMap.get(claimCalcPrd.getCropCommodityId());

				ClaimDto quantityClaimDto = quantityClaimMap.get(claimCalcPrd.getCropCommodityId());
				CropCommodityDto quantityCrpDto = quantityCropMap.get(claimCalcPrd.getCropCommodityId());
				CropCommodityDto quantityLinkedCrpDto = quantityLinkedCropMap.get(claimCalcPrd.getCropCommodityId());
				

				if (insPrd != null) {
					claimCalcPrd.setIsOutOfSyncProductRemoved(false);
				} else {
					claimCalcPrd.setIsOutOfSyncProductRemoved(true);
					isOutOfSync = true;
				}

				// From CIRRAS
				if (insPrd != null && dtoUtils.equals("CoverageValue", insPrd.getCoverageDollars(), claimCalcPrd.getCoverageValue(), 4)) {
					claimCalcPrd.setIsOutOfSyncCoverageValue(false);
				} else {
					claimCalcPrd.setIsOutOfSyncCoverageValue(true);
					isOutOfSync = true;
				}

				if (insPrd != null && dtoUtils.equals("HundredPercentInsurableValue", insPrd.getInsurableValueHundredPercent(), claimCalcPrd.getHundredPercentInsurableValue(), 4)) {
					claimCalcPrd.setIsOutOfSyncHundredPercentInsurableValue(false);
				} else {
					claimCalcPrd.setIsOutOfSyncHundredPercentInsurableValue(true);
					isOutOfSync = true;
				}

				if (insPrd != null && dtoUtils.equals("InsurableValue", insPrd.getSelectedInsurableValue(), claimCalcPrd.getInsurableValue(), 4)) {
					claimCalcPrd.setIsOutOfSyncInsurableValue(false);
				} else {
					claimCalcPrd.setIsOutOfSyncInsurableValue(true);
					isOutOfSync = true;
				}

				if (insPrd != null && dtoUtils.equals("ProductionGuarantee", insPrd.getProductionGuarantee(), claimCalcPrd.getProductionGuarantee(), 4)) {
					claimCalcPrd.setIsOutOfSyncProductionGuarantee(false);
				} else {
					claimCalcPrd.setIsOutOfSyncProductionGuarantee(true);
					isOutOfSync = true;
				}

				// From CUWS
				// CUWS stores yield data always using the non-pedigree crop id, whereas CCS stores Calculations for pedigree commodities using that crop id. So we have to account for this mis-match 
				// here when filtering for Verified Yield.
				Integer vysCropCommodityId = null;
				if ( quantityCrpDto.getIsPedigreeInd() ) {
					vysCropCommodityId = quantityLinkedCrpDto.getCropCommodityId();
				} else {
					vysCropCommodityId = quantityCrpDto.getCropCommodityId();
				}
				
				VerifiedYieldSummary vys = null;
				if ( verifiedYield.getVerifiedYieldSummaries() != null ) {
					for ( VerifiedYieldSummary currVys : verifiedYield.getVerifiedYieldSummaries() ) {
						if ( currVys.getCropCommodityId().equals(vysCropCommodityId) && currVys.getIsPedigreeInd().equals(quantityCrpDto.getIsPedigreeInd()) ) {
							vys = currVys;
							break;
						}
					}
				}
				
				if (vys != null && dtoUtils.equals("TotalYieldToCount", vys.getYieldToCount(), claimCalcPrd.getTotalYieldToCount(), 4)) {
					claimCalcPrd.setIsOutOfSyncTotalYieldToCount(false);
				} else {
					claimCalcPrd.setIsOutOfSyncTotalYieldToCount(true);
					isOutOfSync = true;
				}

				// From CCS
				Double qtyTotalClaimAmount = null;
				Double qtyAssessedYield = null;
				if ( quantityClaimDto != null && ClaimsServiceEnums.CalculationStatusCodes.APPROVED.toString().equals(quantityClaimDto.getCalculationStatusCode())) {
					qtyTotalClaimAmount = quantityClaimDto.getClaimCalculationDto().getTotalClaimAmount();
					qtyAssessedYield = quantityClaimDto.getClaimCalculationDto().getClaimCalculationGrainQuantityDetail().getAssessedYield();
				}
				
				if (dtoUtils.equals("QuantityClaimAmount", qtyTotalClaimAmount, claimCalcPrd.getQuantityClaimAmount(), 2)) {
					claimCalcPrd.setIsOutOfSyncQuantityClaimAmount(false);
				} else {
					claimCalcPrd.setIsOutOfSyncQuantityClaimAmount(true);
					isOutOfSync = true;
				}

				if (dtoUtils.equals("AssessedYield", qtyAssessedYield, claimCalcPrd.getAssessedYield(), 4)) {
					claimCalcPrd.setIsOutOfSyncAssessedYield(false);
				} else {
					claimCalcPrd.setIsOutOfSyncAssessedYield(true);
					isOutOfSync = true;
				}
			}
		}
		return isOutOfSync;
	}
	
	
	//
	//Checks if general claim data is out of sync
	//
	private boolean claimDataOutOfSync(ClaimCalculation claimCalculation, InsuranceClaim insuranceClaim, boolean isOutOfSync) {
		
		if (dtoUtils.equals("growerNumber", insuranceClaim.getGrowerNumber(), claimCalculation.getGrowerNumber())) {
			claimCalculation.setIsOutOfSyncGrowerNumber(false);
		} else {
			claimCalculation.setIsOutOfSyncGrowerNumber(true);
			isOutOfSync = true;
		}

		if (dtoUtils.equals("growerName", insuranceClaim.getGrowerName(), claimCalculation.getGrowerName())) {
			claimCalculation.setIsOutOfSyncGrowerName(false);
		} else {
			claimCalculation.setIsOutOfSyncGrowerName(true);
			isOutOfSync = true;
		}

		if (dtoUtils.equals("growerAddressLine1", insuranceClaim.getGrowerAddressLine1(),
				claimCalculation.getGrowerAddressLine1())) {
			claimCalculation.setIsOutOfSyncGrowerAddrLine1(false);
		} else {
			claimCalculation.setIsOutOfSyncGrowerAddrLine1(true);
			isOutOfSync = true;
		}

		if (dtoUtils.equals("growerAddressLine2", insuranceClaim.getGrowerAddressLine2(),
				claimCalculation.getGrowerAddressLine2())) {
			claimCalculation.setIsOutOfSyncGrowerAddrLine2(false);
		} else {
			claimCalculation.setIsOutOfSyncGrowerAddrLine2(true);
			isOutOfSync = true;
		}

		if (dtoUtils.equals("growerPostalCode", insuranceClaim.getGrowerPostalCode(),
				claimCalculation.getGrowerPostalCode())) {
			claimCalculation.setIsOutOfSyncGrowerPostalCode(false);
		} else {
			claimCalculation.setIsOutOfSyncGrowerPostalCode(true);
			isOutOfSync = true;
		}

		if (dtoUtils.equals("growerCity", insuranceClaim.getGrowerCity(), claimCalculation.getGrowerCity())) {
			claimCalculation.setIsOutOfSyncGrowerCity(false);
		} else {
			claimCalculation.setIsOutOfSyncGrowerCity(true);
			isOutOfSync = true;
		}

		if (dtoUtils.equals("growerProvince", insuranceClaim.getGrowerProvince(), claimCalculation.getGrowerProvince())) {
			claimCalculation.setIsOutOfSyncGrowerProvince(false);
		} else {
			claimCalculation.setIsOutOfSyncGrowerProvince(true);
			isOutOfSync = true;
		}

		return isOutOfSync;
	}
}
