package ca.bc.gov.mal.cirras.claims.persistence.v1.dto;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class ClaimCalculationPlantAcresDto extends BaseDto<ClaimCalculationPlantAcresDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ClaimCalculationPlantAcresDto.class);

	//Claims Calculation Plant Acres
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
	private Integer revisionCount;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	
	public ClaimCalculationPlantAcresDto() {
	}
	
	
	public ClaimCalculationPlantAcresDto(ClaimCalculationPlantAcresDto dto) {
		this.claimCalcPlantAcresGuid = dto.claimCalcPlantAcresGuid;
		this.claimCalculationGuid = dto.claimCalculationGuid;
		this.declaredAcres = dto.declaredAcres;
		this.confirmedAcres = dto.confirmedAcres;
		this.insuredAcres = dto.insuredAcres;
		this.deductibleLevel = dto.deductibleLevel;
		this.deductibleAcres = dto.deductibleAcres;
		this.totalCoverageAcres = dto.totalCoverageAcres;
		this.damagedAcres = dto.damagedAcres;
		this.acresLossCovered = dto.acresLossCovered;
		this.insurableValue = dto.insurableValue;
		this.coverageAmount = dto.coverageAmount;
		this.lessAssessmentReason = dto.lessAssessmentReason;
		this.lessAssessmentAmount = dto.lessAssessmentAmount;
		this.revisionCount = dto.revisionCount;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;
	}
	

	@Override
	public boolean equalsBK(ClaimCalculationPlantAcresDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(ClaimCalculationPlantAcresDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			
			// equalsAll is called by dto.isDirty. The other DTO is created using the "copy" constructor
			int decimalPrecision = 4;
			result = result&&dtoUtils.equals("claimCalcPlantAcresGuid", claimCalcPlantAcresGuid, other.claimCalcPlantAcresGuid);
			result = result&&dtoUtils.equals("claimCalculationGuid", claimCalculationGuid, other.claimCalculationGuid);
			result = result&&dtoUtils.equals("declaredAcres", declaredAcres, other.declaredAcres, decimalPrecision);
			result = result&&dtoUtils.equals("confirmedAcres", confirmedAcres, other.confirmedAcres, decimalPrecision);
			result = result&&dtoUtils.equals("insuredAcres", insuredAcres, other.insuredAcres, decimalPrecision);
			result = result&&dtoUtils.equals("deductibleLevel", deductibleLevel, other.deductibleLevel);
			result = result&&dtoUtils.equals("deductibleAcres", deductibleAcres, other.deductibleAcres, decimalPrecision);
			result = result&&dtoUtils.equals("totalCoverageAcres", totalCoverageAcres, other.totalCoverageAcres, decimalPrecision);
			result = result&&dtoUtils.equals("damagedAcres", damagedAcres, other.damagedAcres, decimalPrecision);
			result = result&&dtoUtils.equals("acresLossCovered", acresLossCovered, other.acresLossCovered, decimalPrecision);
			result = result&&dtoUtils.equals("insurableValue", insurableValue, other.insurableValue, decimalPrecision);
			result = result&&dtoUtils.equals("coverageAmount", coverageAmount, other.coverageAmount, decimalPrecision);
			result = result&&dtoUtils.equals("lessAssessmentReason", lessAssessmentReason, other.lessAssessmentReason);
			result = result&&dtoUtils.equals("lessAssessmentAmount", lessAssessmentAmount, other.lessAssessmentAmount, decimalPrecision);
			result = result&&dtoUtils.equals("revisionCount", revisionCount, other.revisionCount);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public ClaimCalculationPlantAcresDto copy() {
		return new ClaimCalculationPlantAcresDto(this);
	}
	
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
