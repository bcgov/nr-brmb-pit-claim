package ca.bc.gov.mal.cirras.claims.model.v1;

import java.io.Serializable;

//
// This is not going to be a resource.
//
public class ClaimCalculationGrainQuantityDetail implements Serializable {
	private static final long serialVersionUID = 5726905902897809547L;
	
	private String claimCalculationGrainQuantityDetailGuid;
	private String claimCalculationGuid;
	private Double insuredAcres;
	private Double probableYield;
	private Integer deductible;
	private Double productionGuaranteeWeight;
	private Double insurableValue;
	private Double coverageValue;
	private Double totalYieldToCount;
	private Double assessedYield;
	private Double earlyEstDeemedYieldValue;
	private Double damagedAcres;
	private Double seededAcres;
	private Double fiftyPercentProductionGuarantee;
	private Double calcEarlyEstYield;
	private Double inspEarlyEstYield;
	private Double yieldValue;
	private Double yieldValueWithEarlyEstDeemedYield;
	

	// Out of sync flags
	private Boolean isOutOfSyncInsuredAcres;
	private Boolean isOutOfSyncProbableYield;
	private Boolean isOutOfSyncDeductible;
	private Boolean isOutOfSyncProductionGuaranteeWeight;
	private Boolean isOutOfSyncInsurableValue;
	private Boolean isOutOfSyncCoverageValue;
	private Boolean isOutOfSyncTotalYieldToCount;

	public String getClaimCalculationGrainQuantityDetailGuid() {
		return claimCalculationGrainQuantityDetailGuid;
	}
	public void setClaimCalculationGrainQuantityDetailGuid(String claimCalculationGrainQuantityDetailGuid) {
		this.claimCalculationGrainQuantityDetailGuid = claimCalculationGrainQuantityDetailGuid;
	}

	public String getClaimCalculationGuid() {
		return claimCalculationGuid;
	}
	public void setClaimCalculationGuid(String claimCalculationGuid) {
		this.claimCalculationGuid = claimCalculationGuid;
	}

	public Double getInsuredAcres() {
		return insuredAcres;
	}
	public void setInsuredAcres(Double insuredAcres) {
		this.insuredAcres = insuredAcres;
	}

	public Double getProbableYield() {
		return probableYield;
	}
	public void setProbableYield(Double probableYield) {
		this.probableYield = probableYield;
	}

	public Integer getDeductible() {
		return deductible;
	}
	public void setDeductible(Integer deductible) {
		this.deductible = deductible;
	}

	public Double getProductionGuaranteeWeight() {
		return productionGuaranteeWeight;
	}
	public void setProductionGuaranteeWeight(Double productionGuaranteeWeight) {
		this.productionGuaranteeWeight = productionGuaranteeWeight;
	}

	public Double getInsurableValue() {
		return insurableValue;
	}
	public void setInsurableValue(Double insurableValue) {
		this.insurableValue = insurableValue;
	}

	public Double getCoverageValue() {
		return coverageValue;
	}
	public void setCoverageValue(Double coverageValue) {
		this.coverageValue = coverageValue;
	}

	public Double getTotalYieldToCount() {
		return totalYieldToCount;
	}
	public void setTotalYieldToCount(Double totalYieldToCount) {
		this.totalYieldToCount = totalYieldToCount;
	}

	public Double getAssessedYield() {
		return assessedYield;
	}
	public void setAssessedYield(Double assessedYield) {
		this.assessedYield = assessedYield;
	}

	public Double getEarlyEstDeemedYieldValue() {
		return earlyEstDeemedYieldValue;
	}
	public void setEarlyEstDeemedYieldValue(Double earlyEstDeemedYieldValue) {
		this.earlyEstDeemedYieldValue = earlyEstDeemedYieldValue;
	}

	public Double getDamagedAcres() {
		return damagedAcres;
	}
	public void setDamagedAcres(Double damagedAcres) {
		this.damagedAcres = damagedAcres;
	}

	public Double getSeededAcres() {
		return seededAcres;
	}
	public void setSeededAcres(Double seededAcres) {
		this.seededAcres = seededAcres;
	}

	public Double getFiftyPercentProductionGuarantee() {
		return fiftyPercentProductionGuarantee;
	}
	public void setFiftyPercentProductionGuarantee(Double fiftyPercentProductionGuarantee) {
		this.fiftyPercentProductionGuarantee = fiftyPercentProductionGuarantee;
	}

	public Double getCalcEarlyEstYield() {
		return calcEarlyEstYield;
	}
	public void setCalcEarlyEstYield(Double calcEarlyEstYield) {
		this.calcEarlyEstYield = calcEarlyEstYield;
	}

	public Double getInspEarlyEstYield() {
		return inspEarlyEstYield;
	}
	public void setInspEarlyEstYield(Double inspEarlyEstYield) {
		this.inspEarlyEstYield = inspEarlyEstYield;
	}

	public Double getYieldValue() {
		return yieldValue;
	}
	public void setYieldValue(Double yieldValue) {
		this.yieldValue = yieldValue;
	}

	public Double getYieldValueWithEarlyEstDeemedYield() {
		return yieldValueWithEarlyEstDeemedYield;
	}
	public void setYieldValueWithEarlyEstDeemedYield(Double yieldValueWithEarlyEstDeemedYield) {
		this.yieldValueWithEarlyEstDeemedYield = yieldValueWithEarlyEstDeemedYield;
	}

	public Boolean getIsOutOfSyncInsuredAcres() {
		return isOutOfSyncInsuredAcres;
	}

	public void setIsOutOfSyncInsuredAcres(Boolean isOutOfSyncInsuredAcres) {
		this.isOutOfSyncInsuredAcres = isOutOfSyncInsuredAcres;
	}

	public Boolean getIsOutOfSyncProbableYield() {
		return isOutOfSyncProbableYield;
	}

	public void setIsOutOfSyncProbableYield(Boolean isOutOfSyncProbableYield) {
		this.isOutOfSyncProbableYield = isOutOfSyncProbableYield;
	}

	public Boolean getIsOutOfSyncDeductible() {
		return isOutOfSyncDeductible;
	}

	public void setIsOutOfSyncDeductible(Boolean isOutOfSyncDeductible) {
		this.isOutOfSyncDeductible = isOutOfSyncDeductible;
	}

	public Boolean getIsOutOfSyncProductionGuaranteeWeight() {
		return isOutOfSyncProductionGuaranteeWeight;
	}

	public void setIsOutOfSyncProductionGuaranteeWeight(Boolean isOutOfSyncProductionGuaranteeWeight) {
		this.isOutOfSyncProductionGuaranteeWeight = isOutOfSyncProductionGuaranteeWeight;
	}

	public Boolean getIsOutOfSyncInsurableValue() {
		return isOutOfSyncInsurableValue;
	}

	public void setIsOutOfSyncInsurableValue(Boolean isOutOfSyncInsurableValue) {
		this.isOutOfSyncInsurableValue = isOutOfSyncInsurableValue;
	}

	public Boolean getIsOutOfSyncCoverageValue() {
		return isOutOfSyncCoverageValue;
	}

	public void setIsOutOfSyncCoverageValue(Boolean isOutOfSyncCoverageValue) {
		this.isOutOfSyncCoverageValue = isOutOfSyncCoverageValue;
	}

	public Boolean getIsOutOfSyncTotalYieldToCount() {
		return isOutOfSyncTotalYieldToCount;
	}

	public void setIsOutOfSyncTotalYieldToCount(Boolean isOutOfSyncTotalYieldToCount) {
		this.isOutOfSyncTotalYieldToCount = isOutOfSyncTotalYieldToCount;
	}

}
