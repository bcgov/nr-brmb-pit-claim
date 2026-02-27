package ca.bc.gov.mal.cirras.claims.data.entities;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class ClaimCalculationGrainQuantityDto extends BaseDto<ClaimCalculationGrainQuantityDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ClaimCalculationGrainQuantityDto.class);
	
	private String claimCalculationGrainQuantityGuid;
	private Double totalCoverageValue;
	private Double productionGuaranteeAmount;
	private Double totalYieldLossValue;
	private Double reseedClaim;
	private Double maxClaimPayable;
	private Double advancedClaim;
	private Double quantityLossClaim;
	
	private Integer revisionCount;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	
	public ClaimCalculationGrainQuantityDto() {
	}
	
	
	public ClaimCalculationGrainQuantityDto(ClaimCalculationGrainQuantityDto dto) {
		
		this.claimCalculationGrainQuantityGuid = dto.claimCalculationGrainQuantityGuid;
		this.totalCoverageValue = dto.totalCoverageValue;
		this.productionGuaranteeAmount = dto.productionGuaranteeAmount;
		this.totalYieldLossValue = dto.totalYieldLossValue;
		this.reseedClaim = dto.reseedClaim;
		this.maxClaimPayable = dto.maxClaimPayable;
		this.advancedClaim = dto.advancedClaim;
		this.quantityLossClaim = dto.quantityLossClaim;
		
		this.revisionCount = dto.revisionCount;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;

	}
	

	@Override
	public boolean equalsBK(ClaimCalculationGrainQuantityDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(ClaimCalculationGrainQuantityDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			
			// equalsAll is called by dto.isDirty. The other DTO is created using the "copy" constructor
			int decimalPrecision = 4;
			result = result&&dtoUtils.equals("claimCalcGrainQuantityGuid", claimCalculationGrainQuantityGuid, other.claimCalculationGrainQuantityGuid);
			result = result&&dtoUtils.equals("totalCoverageValue", totalCoverageValue, other.totalCoverageValue, decimalPrecision);
			result = result&&dtoUtils.equals("productionGuaranteeAmount", productionGuaranteeAmount, other.productionGuaranteeAmount, decimalPrecision);
			result = result&&dtoUtils.equals("totalYieldLossValue", totalYieldLossValue, other.totalYieldLossValue, decimalPrecision);
			result = result&&dtoUtils.equals("reseedClaim", reseedClaim, other.reseedClaim, decimalPrecision);
			result = result&&dtoUtils.equals("maxClaimPayable", maxClaimPayable, other.maxClaimPayable, decimalPrecision);
			result = result&&dtoUtils.equals("advancedClaim", advancedClaim, other.advancedClaim, decimalPrecision);
			result = result&&dtoUtils.equals("quantityLossClaim", quantityLossClaim, other.quantityLossClaim, decimalPrecision);
			result = result&&dtoUtils.equals("revisionCount", revisionCount, other.revisionCount);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public ClaimCalculationGrainQuantityDto copy() {
		return new ClaimCalculationGrainQuantityDto(this);
	}

	public String getClaimCalculationGrainQuantityGuid() {
		return claimCalculationGrainQuantityGuid;
	}
	public void setClaimCalculationGrainQuantityGuid(String claimCalculationGrainQuantityGuid) {
		this.claimCalculationGrainQuantityGuid = claimCalculationGrainQuantityGuid;
	}

	public Double getTotalCoverageValue() {
		return totalCoverageValue;
	}
	public void setTotalCoverageValue(Double totalCoverageValue) {
		this.totalCoverageValue = totalCoverageValue;
	}

	public Double getProductionGuaranteeAmount() {
		return productionGuaranteeAmount;
	}
	public void setProductionGuaranteeAmount(Double productionGuaranteeAmount) {
		this.productionGuaranteeAmount = productionGuaranteeAmount;
	}

	public Double getTotalYieldLossValue() {
		return totalYieldLossValue;
	}
	public void setTotalYieldLossValue(Double totalYieldLossValue) {
		this.totalYieldLossValue = totalYieldLossValue;
	}

	public Double getReseedClaim() {
		return reseedClaim;
	}
	public void setReseedClaim(Double reseedClaim) {
		this.reseedClaim = reseedClaim;
	}

	public Double getMaxClaimPayable() {
		return maxClaimPayable;
	}
	public void setMaxClaimPayable(Double maxClaimPayable) {
		this.maxClaimPayable = maxClaimPayable;
	}

	public Double getAdvancedClaim() {
		return advancedClaim;
	}
	public void setAdvancedClaim(Double advancedClaim) {
		this.advancedClaim = advancedClaim;
	}

	public Double getQuantityLossClaim() {
		return quantityLossClaim;
	}
	public void setQuantityLossClaim(Double quantityLossClaim) {
		this.quantityLossClaim = quantityLossClaim;
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
