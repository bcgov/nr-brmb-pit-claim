package ca.bc.gov.mal.cirras.claims.data.entities;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class ClaimCalculationGrainUnseededDto extends BaseDto<ClaimCalculationGrainUnseededDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ClaimCalculationGrainUnseededDto.class);

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
	private Integer revisionCount;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	
	public ClaimCalculationGrainUnseededDto() {
	}
	
	
	public ClaimCalculationGrainUnseededDto(ClaimCalculationGrainUnseededDto dto) {
		this.claimCalculationGrainUnseededGuid = dto.claimCalculationGrainUnseededGuid;
		this.claimCalculationGuid = dto.claimCalculationGuid;
		this.insuredAcres = dto.insuredAcres;
		this.lessAdjustmentAcres = dto.lessAdjustmentAcres;
		this.adjustedAcres = dto.adjustedAcres;
		this.deductibleLevel = dto.deductibleLevel;
		this.deductibleAcres = dto.deductibleAcres;
		this.maxEligibleAcres = dto.maxEligibleAcres;
		this.insurableValue = dto.insurableValue;
		this.coverageValue = dto.coverageValue;
		this.unseededAcres = dto.unseededAcres;
		this.lessAssessmentAcres = dto.lessAssessmentAcres;
		this.eligibleUnseededAcres = dto.eligibleUnseededAcres;
		this.revisionCount = dto.revisionCount;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;

	}
	

	@Override
	public boolean equalsBK(ClaimCalculationGrainUnseededDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(ClaimCalculationGrainUnseededDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			
			// equalsAll is called by dto.isDirty. The other DTO is created using the "copy" constructor
			int decimalPrecision = 4;
			result = result&&dtoUtils.equals("claimCalcGrainUnseededGuid", claimCalculationGrainUnseededGuid, other.claimCalculationGrainUnseededGuid);
			result = result&&dtoUtils.equals("claimCalculationGuid", claimCalculationGuid, other.claimCalculationGuid);
			result = result&&dtoUtils.equals("insuredAcres", insuredAcres, other.insuredAcres, decimalPrecision);
			result = result&&dtoUtils.equals("lessAdjustmentAcres", lessAdjustmentAcres, other.lessAdjustmentAcres, decimalPrecision);
			result = result&&dtoUtils.equals("adjustedAcres", adjustedAcres, other.adjustedAcres, decimalPrecision);
			result = result&&dtoUtils.equals("deductibleLevel", deductibleLevel, other.deductibleLevel);
			result = result&&dtoUtils.equals("deductibleAcres", deductibleAcres, other.deductibleAcres, decimalPrecision);
			result = result&&dtoUtils.equals("maxEligibleAcres", maxEligibleAcres, other.maxEligibleAcres, decimalPrecision);
			result = result&&dtoUtils.equals("insurableValue", insurableValue, other.insurableValue, decimalPrecision);
			result = result&&dtoUtils.equals("coverageValue", coverageValue, other.coverageValue, 2);
			result = result&&dtoUtils.equals("unseededAcres", unseededAcres, other.unseededAcres, decimalPrecision);
			result = result&&dtoUtils.equals("lessAssessmentAcres", lessAssessmentAcres, other.lessAssessmentAcres, decimalPrecision);
			result = result&&dtoUtils.equals("eligibleUnseededAcres", eligibleUnseededAcres, other.eligibleUnseededAcres, decimalPrecision);
			result = result&&dtoUtils.equals("revisionCount", revisionCount, other.revisionCount);

		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public ClaimCalculationGrainUnseededDto copy() {
		return new ClaimCalculationGrainUnseededDto(this);
	}

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

	public Integer getRevisionCount() {
		return revisionCount;
	}

	public void setRevisionCount(Integer revisionCount) {
		this.revisionCount = revisionCount;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}


}
