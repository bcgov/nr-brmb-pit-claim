package ca.bc.gov.mal.cirras.claims.data.entities;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class ClaimCalculationGrapesDto extends BaseDto<ClaimCalculationGrapesDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ClaimCalculationGrapesDto.class);

	//Claims Calculation Plant Acres
	private String claimCalculationGrapesGuid;
	private String claimCalculationGuid;
	private Double insurableValueSelected;
	private Double insurableValueHundredPercent;
	private Double coverageAmount;
	private Double coverageAmountAssessed;
	private String coverageAssessedReason;
	private Double coverageAmountAdjusted;
	private Double totalProductionValue;
	private Integer revisionCount;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	
	public ClaimCalculationGrapesDto() {
	}
	
	
	public ClaimCalculationGrapesDto(ClaimCalculationGrapesDto dto) {
		this.claimCalculationGrapesGuid = dto.claimCalculationGrapesGuid;
		this.claimCalculationGuid = dto.claimCalculationGuid;
		this.insurableValueSelected = dto.insurableValueSelected;
		this.insurableValueHundredPercent = dto.insurableValueHundredPercent;
		this.coverageAmount = dto.coverageAmount;
		this.coverageAmountAssessed = dto.coverageAmountAssessed;
		this.coverageAssessedReason = dto.coverageAssessedReason;
		this.coverageAmountAdjusted = dto.coverageAmountAdjusted;
		this.totalProductionValue = dto.totalProductionValue;
		this.revisionCount = dto.revisionCount;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;

	}
	

	@Override
	public boolean equalsBK(ClaimCalculationGrapesDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(ClaimCalculationGrapesDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			
			// equalsAll is called by dto.isDirty. The other DTO is created using the "copy" constructor
			int decimalPrecision = 4;
			result = result&&dtoUtils.equals("claimCalculationGrapesGuid", claimCalculationGrapesGuid, other.claimCalculationGrapesGuid);
			result = result&&dtoUtils.equals("claimCalculationGuid", claimCalculationGuid, other.claimCalculationGuid);
			result = result&&dtoUtils.equals("insurableValueSelected", insurableValueSelected, other.insurableValueSelected, decimalPrecision);
			result = result&&dtoUtils.equals("insurableValueHundredPercent", insurableValueHundredPercent, other.insurableValueHundredPercent, decimalPrecision);
			result = result&&dtoUtils.equals("coverageAmount", coverageAmount, other.coverageAmount, decimalPrecision);
			result = result&&dtoUtils.equals("coverageAmountAssessed", coverageAmountAssessed, other.coverageAmountAssessed, decimalPrecision);
			result = result&&dtoUtils.equals("coverageAssessedReason", coverageAssessedReason, other.coverageAssessedReason);
			result = result&&dtoUtils.equals("coverageAmountAdjusted", coverageAmountAdjusted, other.coverageAmountAdjusted, decimalPrecision);
			result = result&&dtoUtils.equals("totalProductionValue", totalProductionValue, other.totalProductionValue, decimalPrecision);
			result = result&&dtoUtils.equals("revisionCount", revisionCount, other.revisionCount);

		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public ClaimCalculationGrapesDto copy() {
		return new ClaimCalculationGrapesDto(this);
	}
	
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
