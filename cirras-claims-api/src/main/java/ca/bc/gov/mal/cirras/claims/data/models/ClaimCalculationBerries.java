package ca.bc.gov.mal.cirras.claims.data.models;

import java.io.Serializable;

//
// This is not going to be a resource.
//
public class ClaimCalculationBerries implements Serializable {
	private static final long serialVersionUID = 5726905902897809547L;

	private String claimCalculationBerriesGuid;
	private String claimCalculationGuid;
	private Double totalProbableYield;
	private Integer deductibleLevel;
	private Double productionGuarantee;
	private Double declaredAcres;
	private Double confirmedAcres;
	private Double adjustmentFactor;
	private Double adjustedProductionGuarantee;
	private Double insurableValueSelected;
	private Double insurableValueHundredPercent;
	private Double coverageAmountAdjusted;
	private Double maxCoverageAmount;
	private Double harvestedYield;
	private Double appraisedYield;
	private Double abandonedYield;
	private Double totalYieldFromDop;
	private Double totalYieldFromAdjuster;
	private Double yieldAssessment;
	private Double totalYieldForCalculation;
	private Double yieldLossEligible;

	// Out of sync flags
	private Boolean isOutOfSyncTotalProbableYield;
	private Boolean isOutOfSyncDeductibleLevel;
	private Boolean isOutOfSyncProductionGuarantee;
	private Boolean isOutOfSyncDeclaredAcres;
	private Boolean isOutOfSyncInsurableValueSelected;
	private Boolean isOutOfSyncInsurableValueHundredPct;
	private Boolean isOutOfSyncMaxCoverageAmount;


	public String getClaimCalculationBerriesGuid() {
		return claimCalculationBerriesGuid;
	}

	public void setClaimCalculationBerriesGuid(String claimCalculationBerriesGuid) {
		this.claimCalculationBerriesGuid = claimCalculationBerriesGuid;
	}

	public String getClaimCalculationGuid() {
		return claimCalculationGuid;
	}
	
	public void setClaimCalculationGuid(String claimCalculationGuid) {
		this.claimCalculationGuid = claimCalculationGuid;
	}
	
	public Double getTotalProbableYield() {
		return totalProbableYield;
	}
	
	public void setTotalProbableYield(Double totalProbableYield) {
		this.totalProbableYield = totalProbableYield;
	}
	
	public Integer getDeductibleLevel() {
		return deductibleLevel;
	}
	
	public void setDeductibleLevel(Integer deductibleLevel) {
		this.deductibleLevel = deductibleLevel;
	}
	
	public Double getProductionGuarantee() {
		return productionGuarantee;
	}
		
	public void setProductionGuarantee(Double productionGuarantee) {
		this.productionGuarantee = productionGuarantee;
	}
	
	public Double getDeclaredAcres() {
		return declaredAcres;
	}
		
	public void setDeclaredAcres(Double declaredAcres) {
		this.declaredAcres = declaredAcres;
	}
	
	public Double getConfirmedAcres() {
		return confirmedAcres;
	}
		
	public void setConfirmedAcres(Double confirmedAcres) {
		this.confirmedAcres = confirmedAcres;
	}
	
	public Double getAdjustmentFactor() {
		return adjustmentFactor;
	}
		
	public void setAdjustmentFactor(Double adjustmentFactor) {
		this.adjustmentFactor = adjustmentFactor;
	}
	
	public Double getAdjustedProductionGuarantee() {
		return adjustedProductionGuarantee;
	}
		
	public void setAdjustedProductionGuarantee(Double adjustedProductionGuarantee) {
		this.adjustedProductionGuarantee = adjustedProductionGuarantee;
	}
	
	public Double getInsurableValueSelected() {
		return insurableValueSelected;
	}

	public void setInsurableValueSelected(Double insurableValueSelected) {
		this.insurableValueSelected = insurableValueSelected;
	}

	public Double getInsurableValueHundredPercent() {
		return insurableValueHundredPercent;
	}

	public void setInsurableValueHundredPercent(Double insurableValueHundredPercent) {
		this.insurableValueHundredPercent = insurableValueHundredPercent;
	}

	public Double getCoverageAmountAdjusted() {
		return coverageAmountAdjusted;
	}

	public void setCoverageAmountAdjusted(Double coverageAmountAdjusted) {
		this.coverageAmountAdjusted = coverageAmountAdjusted;
	}

	public Double getMaxCoverageAmount() {
		return maxCoverageAmount;
	}

	public void setMaxCoverageAmount(Double maxCoverageAmount) {
		this.maxCoverageAmount = maxCoverageAmount;
	}
	
	public Double getHarvestedYield() {
		return harvestedYield;
	}
		
	public void setHarvestedYield(Double harvestedYield) {
		this.harvestedYield = harvestedYield;
	}
	
	public Double getAppraisedYield() {
		return appraisedYield;
	}
		
	public void setAppraisedYield(Double appraisedYield) {
		this.appraisedYield = appraisedYield;
	}
	
	public Double getAbandonedYield() {
		return abandonedYield;
	}
		
	public void setAbandonedYield(Double abandonedYield) {
		this.abandonedYield = abandonedYield;
	}
	
	public Double getTotalYieldFromDop() {
		return totalYieldFromDop;
	}
	
	public void setTotalYieldFromDop(Double totalYieldFromDop) {
		this.totalYieldFromDop = totalYieldFromDop;
	}
	
	public Double getTotalYieldFromAdjuster() {
		return totalYieldFromAdjuster;
	}
		
	public void setTotalYieldFromAdjuster(Double totalYieldFromAdjuster) {
		this.totalYieldFromAdjuster = totalYieldFromAdjuster;
	}
	
	public Double getYieldAssessment() {
		return yieldAssessment;
	}
		
	public void setYieldAssessment(Double yieldAssessment) {
		this.yieldAssessment = yieldAssessment;
	}
	
	public Double getTotalYieldForCalculation() {
		return totalYieldForCalculation;
	}
		
	public void setTotalYieldForCalculation(Double totalYieldForCalculation) {
		this.totalYieldForCalculation = totalYieldForCalculation;
	}
	
	public Double getYieldLossEligible() {
		return yieldLossEligible;
	}
		
	public void setYieldLossEligible(Double yieldLossEligible) {
		this.yieldLossEligible = yieldLossEligible;
	}
	
	public Boolean getIsOutOfSyncTotalProbableYield() {
		return isOutOfSyncTotalProbableYield;
	}

	public void setIsOutOfSyncTotalProbableYield(Boolean totalProbableYield) {
		this.isOutOfSyncTotalProbableYield = totalProbableYield;
	}

	public Boolean getIsOutOfSyncDeductibleLevel() {
		return isOutOfSyncDeductibleLevel;
	}

	public void setIsOutOfSyncDeductibleLevel(Boolean deductibleLevel) {
		this.isOutOfSyncDeductibleLevel = deductibleLevel;
	}

	public Boolean getIsOutOfSyncProductionGuarantee() {
		return isOutOfSyncProductionGuarantee;
	}
		
	public void setIsOutOfSyncProductionGuarantee(Boolean productionGuarantee) {
		this.isOutOfSyncProductionGuarantee = productionGuarantee;
	}

	public Boolean getIsOutOfSyncDeclaredAcres() {
		return isOutOfSyncDeclaredAcres;
	}
		
	public void setIsOutOfSyncDeclaredAcres(Boolean declaredAcres) {
		this.isOutOfSyncDeclaredAcres = declaredAcres;
	}	
	
	public Boolean getIsOutOfSyncInsurableValueSelected() {
		return isOutOfSyncInsurableValueSelected;
	}
	
	public void setIsOutOfSyncInsurableValueSelected(Boolean isOutOfSyncInsurableValueSelected) {
		this.isOutOfSyncInsurableValueSelected = isOutOfSyncInsurableValueSelected;
	}	

	public Boolean getIsOutOfSyncInsurableValueHundredPct() {
		return isOutOfSyncInsurableValueHundredPct;
	}
	
	public void setIsOutOfSyncInsurableValueHundredPct(Boolean isOutOfSyncInsurableValueHundredPct) {
		this.isOutOfSyncInsurableValueHundredPct = isOutOfSyncInsurableValueHundredPct;
	}
}
