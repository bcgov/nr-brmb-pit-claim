package ca.bc.gov.mal.cirras.claims.model.v1;

import java.io.Serializable;

//
// This is not going to be a resource.
//
public class ClaimCalculationPlantAcres implements Serializable {
	private static final long serialVersionUID = 5726905902897809547L;
	
	private String claimCalcPlantAcresGuid;
	private String claimCalculationGuid;
	private Double declaredAcres;
	private Double confirmedAcres;
	private Double insuredAcres;
	private Integer deductibleLevel;
	private Double deductibleAcres;
	private Double totalCoverageAcres;
	private Double damagedAcres;
	private Double acresLossCovered;
	private Double insurableValue;
	private Double coverageAmount;
	private String lessAssessmentReason;
	private Double lessAssessmentAmount;

	// Out of sync flags
	private Boolean isOutOfSyncDeclaredAcres;
	private Boolean isOutOfSyncDeductibleLevel;
	private Boolean isOutOfSyncInsurableValue;
	
	
	public String getClaimCalcPlantAcresGuid() {
		return claimCalcPlantAcresGuid;
	}
	
	public void setClaimCalcPlantAcresGuid(String claimCalcPlantAcresGuid) {
		this.claimCalcPlantAcresGuid = claimCalcPlantAcresGuid;
	}

	public String getClaimCalculationGuid() {
		return claimCalculationGuid;
	}
	
	public void setClaimCalculationGuid(String claimCalculationGuid) {
		this.claimCalculationGuid = claimCalculationGuid;
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

	public Double getInsuredAcres() {
		return insuredAcres;
	}

	public void setInsuredAcres(Double insuredAcres) {
		this.insuredAcres = insuredAcres;
	}

	public Integer getDeductibleLevel() {
		return deductibleLevel;
	}
	
	public void setDeductibleLevel(Integer deductibleLevel) {
		this.deductibleLevel = deductibleLevel;
	}

	public Double getDeductibleAcres() {
		return deductibleAcres;
	}

	public void setDeductibleAcres(Double deductibleAcres) {
		this.deductibleAcres = deductibleAcres;
	}

	public Double getTotalCoverageAcres() {
		return totalCoverageAcres;
	}

	public void setTotalCoverageAcres(Double totalCoverageAcres) {
		this.totalCoverageAcres = totalCoverageAcres;
	}

	public Double getDamagedAcres() {
		return damagedAcres;
	}

	public void setDamagedAcres(Double damagedAcres) {
		this.damagedAcres = damagedAcres;
	}

	public Double getAcresLossCovered() {
		return acresLossCovered;
	}

	public void setAcresLossCovered(Double acresLossCovered) {
		this.acresLossCovered = acresLossCovered;
	}

	public Double getInsurableValue() {
		return insurableValue;
	}
	
	public void setInsurableValue(Double insurableValue) {
		this.insurableValue = insurableValue;
	}
	
	public Double getCoverageAmount() {
		return coverageAmount;
	}
	
	public void setCoverageAmount(Double coverageAmount) {
		this.coverageAmount = coverageAmount;
	}
	
	public String getLessAssessmentReason() {
		return lessAssessmentReason;
	}

	public void setLessAssessmentReason(String lessAssessmentReason) {
		this.lessAssessmentReason = lessAssessmentReason;
	}

	public Double getLessAssessmentAmount() {
		return lessAssessmentAmount;
	}

	public void setLessAssessmentAmount(Double lessAssessmentAmount) {
		this.lessAssessmentAmount = lessAssessmentAmount;
	}

	public Boolean getIsOutOfSyncDeclaredAcres() {
		return isOutOfSyncDeclaredAcres;
	}

	public void setIsOutOfSyncDeclaredAcres(Boolean isOutOfSyncDeclaredAcres) {
		this.isOutOfSyncDeclaredAcres = isOutOfSyncDeclaredAcres;
	}

	public Boolean getIsOutOfSyncDeductibleLevel() {
		return isOutOfSyncDeductibleLevel;
	}

	public void setIsOutOfSyncDeductibleLevel(Boolean deductibleLevel) {
		this.isOutOfSyncDeductibleLevel = deductibleLevel;
	}

	public Boolean getIsOutOfSyncInsurableValue() {
		return isOutOfSyncInsurableValue;
	}

	public void setIsOutOfSyncInsurableValue(Boolean isOutOfSyncInsurableValue) {
		this.isOutOfSyncInsurableValue = isOutOfSyncInsurableValue;
	}

}
