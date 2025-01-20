package ca.bc.gov.mal.cirras.claims.model.v1;

import java.io.Serializable;

//
// This is not going to be a resource.
//
public class ClaimCalculationGrapes implements Serializable {
	private static final long serialVersionUID = 5726905902897809547L;
	
	private String claimCalculationGrapesGuid;
	private String claimCalculationGuid;
	private Double insurableValueSelected;
	private Double insurableValueHundredPercent;
	private Double coverageAmount;
	private Double coverageAmountAssessed;
	private String coverageAssessedReason;
	private Double coverageAmountAdjusted;
	private Double totalProductionValue;

	// Out of sync flags
	private Boolean isOutOfSyncInsurableValueSelected;
	private Boolean isOutOfSyncInsurableValueHundredPct;
	private Boolean isOutOfSyncCoverageAmount;
	
	
	public String getClaimCalculationGrapesGuid() {
		return claimCalculationGrapesGuid;
	}

	public void setClaimCalculationGrapesGuid(String claimCalculationGrapesGuid) {
		this.claimCalculationGrapesGuid = claimCalculationGrapesGuid;
	}

	public String getClaimCalculationGuid() {
		return claimCalculationGuid;
	}

	public void setClaimCalculationGuid(String claimCalculationGuid) {
		this.claimCalculationGuid = claimCalculationGuid;
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

	public Double getCoverageAmount() {
		return coverageAmount;
	}

	public void setCoverageAmount(Double coverageAmount) {
		this.coverageAmount = coverageAmount;
	}

	public Double getCoverageAmountAssessed() {
		return coverageAmountAssessed;
	}

	public void setCoverageAmountAssessed(Double coverageAmountAssessed) {
		this.coverageAmountAssessed = coverageAmountAssessed;
	}

	public String getCoverageAssessedReason() {
		return coverageAssessedReason;
	}

	public void setCoverageAssessedReason(String coverageAssessedReason) {
		this.coverageAssessedReason = coverageAssessedReason;
	}

	public Double getCoverageAmountAdjusted() {
		return coverageAmountAdjusted;
	}

	public void setCoverageAmountAdjusted(Double coverageAmountAdjusted) {
		this.coverageAmountAdjusted = coverageAmountAdjusted;
	}

	public Double getTotalProductionValue() {
		return totalProductionValue;
	}

	public void setTotalProductionValue(Double totalProductionValue) {
		this.totalProductionValue = totalProductionValue;
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

	public Boolean getIsOutOfSyncCoverageAmount() {
		return isOutOfSyncCoverageAmount;
	}
	
	public void setIsOutOfSyncCoverageAmount(Boolean isOutOfSyncCoverageAmount) {
		this.isOutOfSyncCoverageAmount = isOutOfSyncCoverageAmount;
	}
		
}
