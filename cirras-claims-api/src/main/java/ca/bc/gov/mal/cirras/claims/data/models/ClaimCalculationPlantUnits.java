package ca.bc.gov.mal.cirras.claims.data.models;

import java.io.Serializable;

//
// This is not going to be a resource.
//
public class ClaimCalculationPlantUnits implements Serializable {
	private static final long serialVersionUID = 5726905902897809547L;

	
	private String claimCalcPlantUnitsGuid;
	private String claimCalculationGuid;
	private Double insuredUnits;
	private String lessAdjustmentReason;
	private Integer lessAdjustmentUnits;
	private Double adjustedUnits;
	private Integer deductibleLevel;
	private Double deductibleUnits;
	private Double totalCoverageUnits;
	private Double insurableValue;
	private Double coverageAmount;
	private Integer damagedUnits;
	private String lessAssessmentReason;
	private Integer lessAssessmentUnits;
	private Double totalDamagedUnits;

	// Out of sync flags
	private Boolean isOutOfSyncInsuredUnits;
	private Boolean isOutOfSyncDeductibleLevel;
	private Boolean isOutOfSyncInsurableValue;


	public String getClaimCalcPlantUnitsGuid() {
		return claimCalcPlantUnitsGuid;
	}
	
	public void setClaimCalcPlantUnitsGuid(String claimCalcPlantUnitsGuid) {
		this.claimCalcPlantUnitsGuid = claimCalcPlantUnitsGuid;
	}

	public String getClaimCalculationGuid() {
		return claimCalculationGuid;
	}
	
	public void setClaimCalculationGuid(String claimCalculationGuid) {
		this.claimCalculationGuid = claimCalculationGuid;
	}

	public Double getInsuredUnits() {
		return insuredUnits;
	}
	
	public void setInsuredUnits(Double insuredUnits) {
		this.insuredUnits = insuredUnits;
	}
	
	public String getLessAdjustmentReason() {
		return lessAdjustmentReason;
	}
	
	public void setLessAdjustmentReason(String lessAdjustmentReason) {
		this.lessAdjustmentReason = lessAdjustmentReason;
	}
	
	public Integer getLessAdjustmentUnits() {
		return lessAdjustmentUnits;
	}
	
	public void setLessAdjustmentUnits(Integer lessAdjustmentUnits) {
		this.lessAdjustmentUnits = lessAdjustmentUnits;
	}

	public Double getAdjustedUnits() {
		return adjustedUnits;
	}
	
	public void setAdjustedUnits(Double adjustedUnits) {
		this.adjustedUnits = adjustedUnits;
	}
	
	public Integer getDeductibleLevel() {
		return deductibleLevel;
	}
	
	public void setDeductibleLevel(Integer deductibleLevel) {
		this.deductibleLevel = deductibleLevel;
	}

	public Double getDeductibleUnits() {
		return deductibleUnits;
	}

	public void setDeductibleUnits(Double deductibleUnits) {
		this.deductibleUnits = deductibleUnits;
	}
	
	public Double getTotalCoverageUnits() {
		return totalCoverageUnits;
	}

	public void setTotalCoverageUnits(Double totalCoverageUnits) {
		this.totalCoverageUnits = totalCoverageUnits;
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
	
	public Integer getDamagedUnits() {
		return damagedUnits;
	}

	public void setDamagedUnits(Integer damagedUnits) {
		this.damagedUnits = damagedUnits;
	}
	
	public String getLessAssessmentReason() {
		return lessAssessmentReason;
	}

	public void setLessAssessmentReason(String lessAssessmentReason) {
		this.lessAssessmentReason = lessAssessmentReason;
	}
	
	public Integer getLessAssessmentUnits() {
		return lessAssessmentUnits;
	}

	public void setLessAssessmentUnits(Integer lessAssessmentUnits) {
		this.lessAssessmentUnits = lessAssessmentUnits;
	}
	
	public Double getTotalDamagedUnits() {
		return totalDamagedUnits;
	}

	public void setTotalDamagedUnits(Double totalDamagedUnits) {
		this.totalDamagedUnits = totalDamagedUnits;
	}

	public Boolean getIsOutOfSyncInsuredUnits() {
		return isOutOfSyncInsuredUnits;
	}

	public void setIsOutOfSyncInsuredUnits(Boolean isOutOfSyncInsuredUnits) {
		this.isOutOfSyncInsuredUnits = isOutOfSyncInsuredUnits;
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
