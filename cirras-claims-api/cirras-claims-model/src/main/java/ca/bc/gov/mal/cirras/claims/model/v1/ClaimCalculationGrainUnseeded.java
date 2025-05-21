package ca.bc.gov.mal.cirras.claims.model.v1;

import java.io.Serializable;

//
// This is not going to be a resource.
//
public class ClaimCalculationGrainUnseeded implements Serializable {
	private static final long serialVersionUID = 5726905902897809547L;
	
	private String claimCalculationGrainUnseededGuid;
	private String claimCalculationGuid;
	private Double insuredAcres;
	private Double lessAdjustmentAcres;
	private Double adjustedAcres;
	private Integer deductibleLevel;
	private Double deductibleAcres;
	private Double maxEligibleAcres;
	private Double insurableValue;
	private Double coverageValue;
	private Double unseededAcres;
	private Double lessAssessmentAcres;
	private Double eligibleUnseededAcres;

	// Out of sync flags
	private Boolean isOutOfSyncInsuredAcres;
	private Boolean isOutOfSyncDeductibleLevel;
	private Boolean isOutOfSyncInsurableValue;
	
	public String getClaimCalculationGrainUnseededGuid() {
		return claimCalculationGrainUnseededGuid;
	}

	public void setClaimCalculationGrainUnseededGuid(String claimCalculationGrainUnseededGuid) {
		this.claimCalculationGrainUnseededGuid = claimCalculationGrainUnseededGuid;
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

	public Double getLessAdjustmentAcres() {
		return lessAdjustmentAcres;
	}

	public void setLessAdjustmentAcres(Double lessAdjustmentAcres) {
		this.lessAdjustmentAcres = lessAdjustmentAcres;
	}

	public Double getAdjustedAcres() {
		return adjustedAcres;
	}

	public void setAdjustedAcres(Double adjustedAcres) {
		this.adjustedAcres = adjustedAcres;
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

	public Double getMaxEligibleAcres() {
		return maxEligibleAcres;
	}

	public void setMaxEligibleAcres(Double maxEligibleAcres) {
		this.maxEligibleAcres = maxEligibleAcres;
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

	public Double getUnseededAcres() {
		return unseededAcres;
	}

	public void setUnseededAcres(Double unseededAcres) {
		this.unseededAcres = unseededAcres;
	}

	public Double getLessAssessmentAcres() {
		return lessAssessmentAcres;
	}

	public void setLessAssessmentAcres(Double lessAssessmentAcres) {
		this.lessAssessmentAcres = lessAssessmentAcres;
	}

	public Double getEligibleUnseededAcres() {
		return eligibleUnseededAcres;
	}

	public void setEligibleUnseededAcres(Double eligibleUnseededAcres) {
		this.eligibleUnseededAcres = eligibleUnseededAcres;
	}

	public Boolean getIsOutOfSyncInsuredAcres() {
		return isOutOfSyncInsuredAcres;
	}

	public void setIsOutOfSyncInsuredAcres(Boolean isOutOfSyncInsuredAcres) {
		this.isOutOfSyncInsuredAcres = isOutOfSyncInsuredAcres;
	}

	public Boolean getIsOutOfSyncDeductibleLevel() {
		return isOutOfSyncDeductibleLevel;
	}

	public void setIsOutOfSyncDeductibleLevel(Boolean isOutOfSyncDeductibleLevel) {
		this.isOutOfSyncDeductibleLevel = isOutOfSyncDeductibleLevel;
	}

	public Boolean getIsOutOfSyncInsurableValue() {
		return isOutOfSyncInsurableValue;
	}

	public void setIsOutOfSyncInsurableValue(Boolean isOutOfSyncInsurableValue) {
		this.isOutOfSyncInsurableValue = isOutOfSyncInsurableValue;
	}
		
}
