package ca.bc.gov.mal.cirras.claims.model.v1;

import java.io.Serializable;

//
// This is not going to be a resource.
//
public class ClaimCalculationGrainBasketProduct implements Serializable {
	private static final long serialVersionUID = 5726905902897809547L;

	private String claimCalcGrainBasketProductGuid;
	private String claimCalculationGuid;
	private Integer cropCommodityId;
	private Double hundredPercentInsurableValue;
	private Double insurableValue;
	private Double productionGuarantee;
	private Double coverageValue;
	private Double totalYieldToCount;
	private Double assessedYield;
	private Double quantityClaimAmount;
	private Double yieldValue;

	//Extended fields
	private String cropCommodityName;
	private Boolean isPedigreeInd;

	//Set from latest quantity claim and calculation.
	private Integer quantityColId;
	private Integer quantityClaimNumber;
	private String quantityClaimStatusCode;
	private String quantityCommodityCoverageCode;
	private String quantityLatestClaimCalculationGuid;
	private String quantityLatestCalculationStatusCode;
	
	// Out of sync flags
	// TODO
	
	public String getClaimCalcGrainBasketProductGuid() {
		return claimCalcGrainBasketProductGuid;
	}
	public void setClaimCalcGrainBasketProductGuid(String claimCalcGrainBasketProductGuid) {
		this.claimCalcGrainBasketProductGuid = claimCalcGrainBasketProductGuid;
	}

	public String getClaimCalculationGuid() {
		return claimCalculationGuid;
	}
	public void setClaimCalculationGuid(String claimCalculationGuid) {
		this.claimCalculationGuid = claimCalculationGuid;
	}

	public Integer getCropCommodityId() {
		return cropCommodityId;
	}
	public void setCropCommodityId(Integer cropCommodityId) {
		this.cropCommodityId = cropCommodityId;
	}

	public Double getHundredPercentInsurableValue() {
		return hundredPercentInsurableValue;
	}
	public void setHundredPercentInsurableValue(Double hundredPercentInsurableValue) {
		this.hundredPercentInsurableValue = hundredPercentInsurableValue;
	}

	public Double getInsurableValue() {
		return insurableValue;
	}
	public void setInsurableValue(Double insurableValue) {
		this.insurableValue = insurableValue;
	}

	public Double getProductionGuarantee() {
		return productionGuarantee;
	}
	public void setProductionGuarantee(Double productionGuarantee) {
		this.productionGuarantee = productionGuarantee;
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

	public Double getQuantityClaimAmount() {
		return quantityClaimAmount;
	}
	public void setQuantityClaimAmount(Double quantityClaimAmount) {
		this.quantityClaimAmount = quantityClaimAmount;
	}

	public Double getYieldValue() {
		return yieldValue;
	}
	public void setYieldValue(Double yieldValue) {
		this.yieldValue = yieldValue;
	}

	public String getCropCommodityName() {
		return cropCommodityName;
	}
	public void setCropCommodityName(String cropCommodityName) {
		this.cropCommodityName = cropCommodityName;
	}

	public Boolean getIsPedigreeInd() {
		return isPedigreeInd;
	}
	public void setIsPedigreeInd(Boolean isPedigreeInd) {
		this.isPedigreeInd = isPedigreeInd;
	}

	public Integer getQuantityColId() {
		return quantityColId;
	}
	public void setQuantityColId(Integer quantityColId) {
		this.quantityColId = quantityColId;
	}

	public Integer getQuantityClaimNumber() {
		return quantityClaimNumber;
	}
	public void setQuantityClaimNumber(Integer quantityClaimNumber) {
		this.quantityClaimNumber = quantityClaimNumber;
	}

	public String getQuantityClaimStatusCode() {
		return quantityClaimStatusCode;
	}
	public void setQuantityClaimStatusCode(String quantityClaimStatusCode) {
		this.quantityClaimStatusCode = quantityClaimStatusCode;
	}

	public String getQuantityCommodityCoverageCode() {
		return quantityCommodityCoverageCode;
	}
	public void setQuantityCommodityCoverageCode(String quantityCommodityCoverageCode) {
		this.quantityCommodityCoverageCode = quantityCommodityCoverageCode;
	}

	public String getQuantityLatestClaimCalculationGuid() {
		return quantityLatestClaimCalculationGuid;
	}
	public void setQuantityLatestClaimCalculationGuid(String quantityLatestClaimCalculationGuid) {
		this.quantityLatestClaimCalculationGuid = quantityLatestClaimCalculationGuid;
	}

	public String getQuantityLatestCalculationStatusCode() {
		return quantityLatestCalculationStatusCode;
	}
	public void setQuantityLatestCalculationStatusCode(String quantityLatestCalculationStatusCode) {
		this.quantityLatestCalculationStatusCode = quantityLatestCalculationStatusCode;
	}
}
