package ca.bc.gov.mal.cirras.claims.persistence.v1.dto;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class ClaimCalculationGrainSpotLossDto extends BaseDto<ClaimCalculationGrainSpotLossDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ClaimCalculationGrainSpotLossDto.class);

	private String claimCalculationGrainSpotLossGuid;
	private String claimCalculationGuid;
	private Double insuredAcres;
	private Double coverageAmtPerAcre;
	private Double coverageValue;
	private Double adjustedAcres;
	private Double percentYieldReduction;
	private Double eligibleYieldReduction;
	private Double spotLossReductionValue;
	private Integer deductible;
	private Integer revisionCount;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	
	public ClaimCalculationGrainSpotLossDto() {
	}
	
	
	public ClaimCalculationGrainSpotLossDto(ClaimCalculationGrainSpotLossDto dto) {
		this.claimCalculationGrainSpotLossGuid = dto.claimCalculationGrainSpotLossGuid;
		this.claimCalculationGuid = dto.claimCalculationGuid;
		this.insuredAcres = dto.insuredAcres;
		this.coverageAmtPerAcre = dto.coverageAmtPerAcre;
		this.coverageValue = dto.coverageValue;
		this.adjustedAcres = dto.adjustedAcres;
		this.percentYieldReduction = dto.percentYieldReduction;
		this.eligibleYieldReduction = dto.eligibleYieldReduction;
		this.spotLossReductionValue = dto.spotLossReductionValue;
		this.deductible = dto.deductible;
		this.revisionCount = dto.revisionCount;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;

	}
	

	@Override
	public boolean equalsBK(ClaimCalculationGrainSpotLossDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(ClaimCalculationGrainSpotLossDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			
			// equalsAll is called by dto.isDirty. The other DTO is created using the "copy" constructor
			int decimalPrecision = 4;
			result = result&&dtoUtils.equals("claimCalculationGrainSpotLossGuid", claimCalculationGrainSpotLossGuid, other.claimCalculationGrainSpotLossGuid);
			result = result&&dtoUtils.equals("claimCalculationGuid", claimCalculationGuid, other.claimCalculationGuid);
			result = result&&dtoUtils.equals("insuredAcres", insuredAcres, other.insuredAcres, decimalPrecision);
			result = result&&dtoUtils.equals("coverageAmtPerAcre", coverageAmtPerAcre, other.coverageAmtPerAcre, decimalPrecision);
			result = result&&dtoUtils.equals("coverageValue", coverageValue, other.coverageValue, decimalPrecision);
			result = result&&dtoUtils.equals("adjustedAcres", adjustedAcres, other.adjustedAcres, decimalPrecision);
			result = result&&dtoUtils.equals("percentYieldReduction", percentYieldReduction, other.percentYieldReduction, decimalPrecision);
			result = result&&dtoUtils.equals("eligibleYieldReduction", eligibleYieldReduction, other.eligibleYieldReduction, decimalPrecision);
			result = result&&dtoUtils.equals("spotLossReductionValue", spotLossReductionValue, other.spotLossReductionValue, decimalPrecision);
			result = result&&dtoUtils.equals("deductible", deductible, other.deductible);
			result = result&&dtoUtils.equals("revisionCount", revisionCount, other.revisionCount);

		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public ClaimCalculationGrainSpotLossDto copy() {
		return new ClaimCalculationGrainSpotLossDto(this);
	}

	public String getClaimCalculationGrainSpotLossGuid() {
		return claimCalculationGrainSpotLossGuid;
	}

	public void setClaimCalculationGrainSpotLossGuid(String claimCalculationGrainSpotLossGuid) {
		this.claimCalculationGrainSpotLossGuid = claimCalculationGrainSpotLossGuid;
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

	public Double getCoverageAmtPerAcre() {
		return coverageAmtPerAcre;
	}

	public void setCoverageAmtPerAcre(Double coverageAmtPerAcre) {
		this.coverageAmtPerAcre = coverageAmtPerAcre;
	}

	public Double getCoverageValue() {
		return coverageValue;
	}

	public void setCoverageValue(Double coverageValue) {
		this.coverageValue = coverageValue;
	}

	public Double getAdjustedAcres() {
		return adjustedAcres;
	}

	public void setAdjustedAcres(Double adjustedAcres) {
		this.adjustedAcres = adjustedAcres;
	}

	public Double getPercentYieldReduction() {
		return percentYieldReduction;
	}

	public void setPercentYieldReduction(Double percentYieldReduction) {
		this.percentYieldReduction = percentYieldReduction;
	}

	public Double getEligibleYieldReduction() {
		return eligibleYieldReduction;
	}

	public void setEligibleYieldReduction(Double eligibleYieldReduction) {
		this.eligibleYieldReduction = eligibleYieldReduction;
	}

	public Double getSpotLossReductionValue() {
		return spotLossReductionValue;
	}

	public void setSpotLossReductionValue(Double spotLossReductionValue) {
		this.spotLossReductionValue = spotLossReductionValue;
	}

	public Integer getDeductible() {
		return deductible;
	}

	public void setDeductible(Integer deductible) {
		this.deductible = deductible;
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
