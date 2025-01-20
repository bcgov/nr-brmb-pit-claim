package ca.bc.gov.mal.cirras.claims.persistence.v1.dto;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class ClaimCalculationPlantUnitsDto extends BaseDto<ClaimCalculationPlantUnitsDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ClaimCalculationPlantUnitsDto.class);

	//Claims Calculation Plant Units
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
	private Integer revisionCount;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;
	
	public ClaimCalculationPlantUnitsDto() {
	}
	
	
	public ClaimCalculationPlantUnitsDto(ClaimCalculationPlantUnitsDto dto) {
		this.claimCalcPlantUnitsGuid = dto.claimCalcPlantUnitsGuid;
		this.claimCalculationGuid = dto.claimCalculationGuid;
		this.insuredUnits = dto.insuredUnits;
		this.lessAdjustmentReason = dto.lessAdjustmentReason;
		this.lessAdjustmentUnits = dto.lessAdjustmentUnits;
		this.adjustedUnits = dto.adjustedUnits;
		this.deductibleLevel = dto.deductibleLevel;
		this.deductibleUnits = dto.deductibleUnits;
		this.totalCoverageUnits = dto.totalCoverageUnits;
		this.insurableValue = dto.insurableValue;
		this.coverageAmount = dto.coverageAmount;
		this.damagedUnits = dto.damagedUnits;
		this.lessAssessmentReason = dto.lessAssessmentReason;
		this.lessAssessmentUnits = dto.lessAssessmentUnits;
		this.totalDamagedUnits = dto.totalDamagedUnits;
		this.revisionCount = dto.revisionCount;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;
	}
	

	@Override
	public boolean equalsBK(ClaimCalculationPlantUnitsDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(ClaimCalculationPlantUnitsDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			
			// equalsAll is called by dto.isDirty. The other DTO is created using the "copy" constructor
			int decimalPrecision = 4;
			result = result&&dtoUtils.equals("claimCalcPlantUnitsGuid", claimCalcPlantUnitsGuid, other.claimCalcPlantUnitsGuid);
			result = result&&dtoUtils.equals("claimCalculationGuid", claimCalculationGuid, other.claimCalculationGuid);
			result = result&&dtoUtils.equals("insuredUnits", insuredUnits, other.insuredUnits, decimalPrecision);
			result = result&&dtoUtils.equals("lessAdjustmentReason", lessAdjustmentReason, other.lessAdjustmentReason);
			result = result&&dtoUtils.equals("lessAdjustmentUnits", lessAdjustmentUnits, other.lessAdjustmentUnits);
			result = result&&dtoUtils.equals("adjustedUnits", adjustedUnits, other.adjustedUnits, decimalPrecision);
			result = result&&dtoUtils.equals("deductibleLevel", deductibleLevel, other.deductibleLevel);
			result = result&&dtoUtils.equals("deductibleUnits", deductibleUnits, other.deductibleUnits, decimalPrecision);
			result = result&&dtoUtils.equals("totalCoverageUnits", totalCoverageUnits, other.totalCoverageUnits, decimalPrecision);
			result = result&&dtoUtils.equals("insurableValue", insurableValue, other.insurableValue, decimalPrecision);
			result = result&&dtoUtils.equals("coverageAmount", coverageAmount, other.coverageAmount, decimalPrecision);
			result = result&&dtoUtils.equals("damagedUnits", damagedUnits, other.damagedUnits);
			result = result&&dtoUtils.equals("lessAssessmentReason", lessAssessmentReason, other.lessAssessmentReason);
			result = result&&dtoUtils.equals("lessAssessmentUnits", lessAssessmentUnits, other.lessAssessmentUnits);
			result = result&&dtoUtils.equals("totalDamagedUnits", totalDamagedUnits, other.totalDamagedUnits, decimalPrecision);
			result = result&&dtoUtils.equals("revisionCount", revisionCount, other.revisionCount);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public ClaimCalculationPlantUnitsDto copy() {
		return new ClaimCalculationPlantUnitsDto(this);
	}
	
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
