package ca.bc.gov.mal.cirras.claims.persistence.v1.dto;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class ClaimCalculationGrainBasketProductDto extends BaseDto<ClaimCalculationGrainBasketProductDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ClaimCalculationGrainBasketProductDto.class);

	private String claimCalcGrainBasketProductGuid;
	private String claimCalculationGuid;
	private Integer cropCommodityId;
	private Double hundredPercentInsurableValue;
	private Double insurableValue;
	private Double productionGuarantee;
	private Double coverageValue;
	private Double totalYieldToCount;
	private Double assessedYield;
	private Double quantityClaimAmount;
	private Double yieldValue;
	private Integer revisionCount;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	private String cropCommodityName;
	private Boolean isPedigreeInd;
	
	public ClaimCalculationGrainBasketProductDto() {
	}
	
	
	public ClaimCalculationGrainBasketProductDto(ClaimCalculationGrainBasketProductDto dto) {
		this.claimCalcGrainBasketProductGuid = dto.claimCalcGrainBasketProductGuid;
		this.claimCalculationGuid = dto.claimCalculationGuid;
		this.cropCommodityId = dto.cropCommodityId;
		this.hundredPercentInsurableValue = dto.hundredPercentInsurableValue;
		this.insurableValue = dto.insurableValue;
		this.productionGuarantee = dto.productionGuarantee;
		this.coverageValue = dto.coverageValue;
		this.totalYieldToCount = dto.totalYieldToCount;
		this.assessedYield = dto.assessedYield;
		this.quantityClaimAmount = dto.quantityClaimAmount;
		this.yieldValue = dto.yieldValue;
		this.revisionCount = dto.revisionCount;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;
		
		this.cropCommodityName = dto.cropCommodityName;
		this.isPedigreeInd = dto.isPedigreeInd;

	}
	

	@Override
	public boolean equalsBK(ClaimCalculationGrainBasketProductDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(ClaimCalculationGrainBasketProductDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			
			// equalsAll is called by dto.isDirty. The other DTO is created using the "copy" constructor
			int decimalPrecision = 4;
			result = result&&dtoUtils.equals("claimCalcGrainBasketProductGuid", claimCalcGrainBasketProductGuid, other.claimCalcGrainBasketProductGuid);
			result = result&&dtoUtils.equals("claimCalculationGuid", claimCalculationGuid, other.claimCalculationGuid);
			result = result&&dtoUtils.equals("cropCommodityId", cropCommodityId, other.cropCommodityId);
			result = result&&dtoUtils.equals("hundredPercentInsurableValue", hundredPercentInsurableValue, other.hundredPercentInsurableValue, decimalPrecision);
			result = result&&dtoUtils.equals("insurableValue", insurableValue, other.insurableValue, decimalPrecision);
			result = result&&dtoUtils.equals("productionGuarantee", productionGuarantee, other.productionGuarantee, decimalPrecision);
			result = result&&dtoUtils.equals("coverageValue", coverageValue, other.coverageValue, decimalPrecision);
			result = result&&dtoUtils.equals("totalYieldToCount", totalYieldToCount, other.totalYieldToCount, decimalPrecision);
			result = result&&dtoUtils.equals("assessedYield", assessedYield, other.assessedYield, decimalPrecision);
			result = result&&dtoUtils.equals("quantityClaimAmount", quantityClaimAmount, other.quantityClaimAmount, 2);
			result = result&&dtoUtils.equals("yieldValue", yieldValue, other.yieldValue, decimalPrecision);
			result = result&&dtoUtils.equals("revisionCount", revisionCount, other.revisionCount);

		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public ClaimCalculationGrainBasketProductDto copy() {
		return new ClaimCalculationGrainBasketProductDto(this);
	}

	public String getClaimCalcGrainBasketProductGuid() {
		return claimCalcGrainBasketProductGuid;
	}

	public void setClaimCalcGrainBasketProductGuid(String claimCalcGrainBasketProductGuid) {
		this.claimCalcGrainBasketProductGuid = claimCalcGrainBasketProductGuid;
	}

	public String getClaimCalculationGuid() {
		return claimCalculationGuid;
	}

	public void setClaimCalculationGuid(String claimCalculationGuid) {
		this.claimCalculationGuid = claimCalculationGuid;
	}

	public Integer getCropCommodityId() {
		return cropCommodityId;
	}

	public void setCropCommodityId(Integer cropCommodityId) {
		this.cropCommodityId = cropCommodityId;
	}

	public Double getHundredPercentInsurableValue() {
		return hundredPercentInsurableValue;
	}

	public void setHundredPercentInsurableValue(Double hundredPercentInsurableValue) {
		this.hundredPercentInsurableValue = hundredPercentInsurableValue;
	}

	public Double getInsurableValue() {
		return insurableValue;
	}

	public void setInsurableValue(Double insurableValue) {
		this.insurableValue = insurableValue;
	}

	public Double getProductionGuarantee() {
		return productionGuarantee;
	}

	public void setProductionGuarantee(Double productionGuarantee) {
		this.productionGuarantee = productionGuarantee;
	}

	public Double getCoverageValue() {
		return coverageValue;
	}

	public void setCoverageValue(Double coverageValue) {
		this.coverageValue = coverageValue;
	}

	public Double getTotalYieldToCount() {
		return totalYieldToCount;
	}

	public void setTotalYieldToCount(Double totalYieldToCount) {
		this.totalYieldToCount = totalYieldToCount;
	}

	public Double getAssessedYield() {
		return assessedYield;
	}

	public void setAssessedYield(Double assessedYield) {
		this.assessedYield = assessedYield;
	}

	public Double getQuantityClaimAmount() {
		return quantityClaimAmount;
	}

	public void setQuantityClaimAmount(Double quantityClaimAmount) {
		this.quantityClaimAmount = quantityClaimAmount;
	}

	public Double getYieldValue() {
		return yieldValue;
	}

	public void setYieldValue(Double yieldValue) {
		this.yieldValue = yieldValue;
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

	public String getCropCommodityName() {
		return cropCommodityName;
	}

	public void setCropCommodityName(String cropCommodityName) {
		this.cropCommodityName = cropCommodityName;
	}

	public Boolean getIsPedigreeInd() {
		return isPedigreeInd;
	}

	public void setIsPedigreeInd(Boolean isPedigreeInd) {
		this.isPedigreeInd = isPedigreeInd;
	}

}
