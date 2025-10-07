package ca.bc.gov.mal.cirras.claims.model.v1;

import java.io.Serializable;

//
// This is not going to be a resource.
//
public class ClaimCalculationGrainBasket implements Serializable {
	private static final long serialVersionUID = 5726905902897809547L;


	private String claimCalculationGrainBasketGuid;
	private String claimCalculationGuid;
	private Double grainBasketCoverageValue;
	private Integer grainBasketDeductible;
	private Double grainBasketHarvestedValue;
	private Double quantityTotalCoverageValue;
	private Double quantityTotalYieldValue;
	private Double quantityTotalClaimAmount;
	private Double quantityTotalYieldLossIndemnity;
	private Double totalYieldCoverageValue;
	private Double totalYieldLoss;

	// Out of sync flags
	// From CIRRAS
	private Boolean isOutOfSyncGrainBasketCoverageValue;
	private Boolean isOutOfSyncGrainBasketDeductible;

	// From CUWS
	private Boolean isOutOfSyncGrainBasketHarvestedValue;
	

	public String getClaimCalculationGrainBasketGuid() {
		return claimCalculationGrainBasketGuid;
	}
	public void setClaimCalculationGrainBasketGuid(String claimCalculationGrainBasketGuid) {
		this.claimCalculationGrainBasketGuid = claimCalculationGrainBasketGuid;
	}

	public String getClaimCalculationGuid() {
		return claimCalculationGuid;
	}
	public void setClaimCalculationGuid(String claimCalculationGuid) {
		this.claimCalculationGuid = claimCalculationGuid;
	}

	public Double getGrainBasketCoverageValue() {
		return grainBasketCoverageValue;
	}
	public void setGrainBasketCoverageValue(Double grainBasketCoverageValue) {
		this.grainBasketCoverageValue = grainBasketCoverageValue;
	}

	public Integer getGrainBasketDeductible() {
		return grainBasketDeductible;
	}
	public void setGrainBasketDeductible(Integer grainBasketDeductible) {
		this.grainBasketDeductible = grainBasketDeductible;
	}

	public Double getGrainBasketHarvestedValue() {
		return grainBasketHarvestedValue;
	}
	public void setGrainBasketHarvestedValue(Double grainBasketHarvestedValue) {
		this.grainBasketHarvestedValue = grainBasketHarvestedValue;
	}

	public Double getQuantityTotalCoverageValue() {
		return quantityTotalCoverageValue;
	}
	public void setQuantityTotalCoverageValue(Double quantityTotalCoverageValue) {
		this.quantityTotalCoverageValue = quantityTotalCoverageValue;
	}

	public Double getQuantityTotalYieldValue() {
		return quantityTotalYieldValue;
	}
	public void setQuantityTotalYieldValue(Double quantityTotalYieldValue) {
		this.quantityTotalYieldValue = quantityTotalYieldValue;
	}

	public Double getQuantityTotalClaimAmount() {
		return quantityTotalClaimAmount;
	}
	public void setQuantityTotalClaimAmount(Double quantityTotalClaimAmount) {
		this.quantityTotalClaimAmount = quantityTotalClaimAmount;
	}

	public Double getQuantityTotalYieldLossIndemnity() {
		return quantityTotalYieldLossIndemnity;
	}
	public void setQuantityTotalYieldLossIndemnity(Double quantityTotalYieldLossIndemnity) {
		this.quantityTotalYieldLossIndemnity = quantityTotalYieldLossIndemnity;
	}

	public Double getTotalYieldCoverageValue() {
		return totalYieldCoverageValue;
	}
	public void setTotalYieldCoverageValue(Double totalYieldCoverageValue) {
		this.totalYieldCoverageValue = totalYieldCoverageValue;
	}

	public Double getTotalYieldLoss() {
		return totalYieldLoss;
	}
	public void setTotalYieldLoss(Double totalYieldLoss) {
		this.totalYieldLoss = totalYieldLoss;
	}

	public Boolean getIsOutOfSyncGrainBasketCoverageValue() {
		return isOutOfSyncGrainBasketCoverageValue;
	}
	public void setIsOutOfSyncGrainBasketCoverageValue(Boolean isOutOfSyncGrainBasketCoverageValue) {
		this.isOutOfSyncGrainBasketCoverageValue = isOutOfSyncGrainBasketCoverageValue;
	}

	public Boolean getIsOutOfSyncGrainBasketDeductible() {
		return isOutOfSyncGrainBasketDeductible;
	}
	public void setIsOutOfSyncGrainBasketDeductible(Boolean isOutOfSyncGrainBasketDeductible) {
		this.isOutOfSyncGrainBasketDeductible = isOutOfSyncGrainBasketDeductible;
	}

	public Boolean getIsOutOfSyncGrainBasketHarvestedValue() {
		return isOutOfSyncGrainBasketHarvestedValue;
	}
	public void setIsOutOfSyncGrainBasketHarvestedValue(Boolean isOutOfSyncGrainBasketHarvestedValue) {
		this.isOutOfSyncGrainBasketHarvestedValue = isOutOfSyncGrainBasketHarvestedValue;
	}
}
