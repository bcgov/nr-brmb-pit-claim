package ca.bc.gov.mal.cirras.claims.model.v1;

import java.io.Serializable;

//
// This is not going to be a resource.
//
public class ClaimCalculationGrainQuantity implements Serializable {
	private static final long serialVersionUID = 5726905902897809547L;
	
	private String claimCalculationGrainQuantityGuid;
	private Double totalCoverageValue;
	private Double productionGuaranteeAmount;
	private Double totalYieldLossValue;
	private Double reseedClaim;
	private Double maxClaimPayable;
	private Double advancedClaim;
	private Double quantityLossClaim;
	

	// Out of sync flags
	// TODO

	public String getClaimCalculationGrainQuantityGuid() {
		return claimCalculationGrainQuantityGuid;
	}
	public void setClaimCalculationGrainQuantityGuid(String claimCalculationGrainQuantityGuid) {
		this.claimCalculationGrainQuantityGuid = claimCalculationGrainQuantityGuid;
	}

	public Double getTotalCoverageValue() {
		return totalCoverageValue;
	}
	public void setTotalCoverageValue(Double totalCoverageValue) {
		this.totalCoverageValue = totalCoverageValue;
	}

	public Double getProductionGuaranteeAmount() {
		return productionGuaranteeAmount;
	}
	public void setProductionGuaranteeAmount(Double productionGuaranteeAmount) {
		this.productionGuaranteeAmount = productionGuaranteeAmount;
	}

	public Double getTotalYieldLossValue() {
		return totalYieldLossValue;
	}
	public void setTotalYieldLossValue(Double totalYieldLossValue) {
		this.totalYieldLossValue = totalYieldLossValue;
	}

	public Double getReseedClaim() {
		return reseedClaim;
	}
	public void setReseedClaim(Double reseedClaim) {
		this.reseedClaim = reseedClaim;
	}

	public Double getMaxClaimPayable() {
		return maxClaimPayable;
	}
	public void setMaxClaimPayable(Double maxClaimPayable) {
		this.maxClaimPayable = maxClaimPayable;
	}

	public Double getAdvancedClaim() {
		return advancedClaim;
	}
	public void setAdvancedClaim(Double advancedClaim) {
		this.advancedClaim = advancedClaim;
	}

	public Double getQuantityLossClaim() {
		return quantityLossClaim;
	}
	public void setQuantityLossClaim(Double quantityLossClaim) {
		this.quantityLossClaim = quantityLossClaim;
	}
}
