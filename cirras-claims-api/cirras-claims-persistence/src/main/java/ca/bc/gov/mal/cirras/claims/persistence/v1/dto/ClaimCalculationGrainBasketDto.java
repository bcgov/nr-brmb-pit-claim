package ca.bc.gov.mal.cirras.claims.persistence.v1.dto;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class ClaimCalculationGrainBasketDto extends BaseDto<ClaimCalculationGrainBasketDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ClaimCalculationGrainBasketDto.class);

	private String claimCalculationGrainBasketGuid;
	private String claimCalculationGuid;
	private Double grainBasketCoverageValue;
	private Integer grainBasketDeductible;
	private Double grainBasketHarvestedValue;
	private Double quantityTotalCoverageValue;
	private Double quantityTotalYieldValue;
	private Double quantityTotalClaimAmount;
	private Double quantityTotalYieldLossIndemnity;
	private Double totalYieldCoverageValue;
	private Double totalYieldLoss;
	private Integer revisionCount;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	
	public ClaimCalculationGrainBasketDto() {
	}
	
	
	public ClaimCalculationGrainBasketDto(ClaimCalculationGrainBasketDto dto) {
		this.claimCalculationGrainBasketGuid = dto.claimCalculationGrainBasketGuid;
		this.claimCalculationGuid = dto.claimCalculationGuid;
		this.grainBasketCoverageValue = dto.grainBasketCoverageValue;
		this.grainBasketDeductible = dto.grainBasketDeductible;
		this.grainBasketHarvestedValue = dto.grainBasketHarvestedValue;
		this.quantityTotalCoverageValue = dto.quantityTotalCoverageValue;
		this.quantityTotalYieldValue = dto.quantityTotalYieldValue;
		this.quantityTotalClaimAmount = dto.quantityTotalClaimAmount;
		this.quantityTotalYieldLossIndemnity = dto.quantityTotalYieldLossIndemnity;
		this.totalYieldCoverageValue = dto.totalYieldCoverageValue;
		this.totalYieldLoss = dto.totalYieldLoss;
		this.revisionCount = dto.revisionCount;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;

	}
	

	@Override
	public boolean equalsBK(ClaimCalculationGrainBasketDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(ClaimCalculationGrainBasketDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			
			// equalsAll is called by dto.isDirty. The other DTO is created using the "copy" constructor
			int decimalPrecision = 4;
			result = result&&dtoUtils.equals("claimCalculationGrainBasketGuid", claimCalculationGrainBasketGuid, other.claimCalculationGrainBasketGuid);
			result = result&&dtoUtils.equals("claimCalculationGuid", claimCalculationGuid, other.claimCalculationGuid);
			result = result&&dtoUtils.equals("grainBasketCoverageValue", grainBasketCoverageValue, other.grainBasketCoverageValue, decimalPrecision);
			result = result&&dtoUtils.equals("grainBasketDeductible", grainBasketDeductible, other.grainBasketDeductible);
			result = result&&dtoUtils.equals("grainBasketHarvestedValue", grainBasketHarvestedValue, other.grainBasketHarvestedValue, decimalPrecision);
			result = result&&dtoUtils.equals("quantityTotalCoverageValue", quantityTotalCoverageValue, other.quantityTotalCoverageValue, decimalPrecision);
			result = result&&dtoUtils.equals("quantityTotalYieldValue", quantityTotalYieldValue, other.quantityTotalYieldValue, decimalPrecision);
			result = result&&dtoUtils.equals("quantityTotalClaimAmount", quantityTotalClaimAmount, other.quantityTotalClaimAmount, 2);
			result = result&&dtoUtils.equals("quantityTotalYieldLossIndemnity", quantityTotalYieldLossIndemnity, other.quantityTotalYieldLossIndemnity, decimalPrecision);
			result = result&&dtoUtils.equals("totalYieldCoverageValue", totalYieldCoverageValue, other.totalYieldCoverageValue, decimalPrecision);
			result = result&&dtoUtils.equals("totalYieldLoss", totalYieldLoss, other.totalYieldLoss, decimalPrecision);
			result = result&&dtoUtils.equals("revisionCount", revisionCount, other.revisionCount);

		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public ClaimCalculationGrainBasketDto copy() {
		return new ClaimCalculationGrainBasketDto(this);
	}

	public String getClaimCalculationGrainBasketGuid() {
		return claimCalculationGrainBasketGuid;
	}

	public void setClaimCalculationGrainBasketGuid(String claimCalculationGrainBasketGuid) {
		this.claimCalculationGrainBasketGuid = claimCalculationGrainBasketGuid;
	}

	public String getClaimCalculationGuid() {
		return claimCalculationGuid;
	}

	public void setClaimCalculationGuid(String claimCalculationGuid) {
		this.claimCalculationGuid = claimCalculationGuid;
	}

	public Double getGrainBasketCoverageValue() {
		return grainBasketCoverageValue;
	}

	public void setGrainBasketCoverageValue(Double grainBasketCoverageValue) {
		this.grainBasketCoverageValue = grainBasketCoverageValue;
	}

	public Integer getGrainBasketDeductible() {
		return grainBasketDeductible;
	}

	public void setGrainBasketDeductible(Integer grainBasketDeductible) {
		this.grainBasketDeductible = grainBasketDeductible;
	}

	public Double getGrainBasketHarvestedValue() {
		return grainBasketHarvestedValue;
	}

	public void setGrainBasketHarvestedValue(Double grainBasketHarvestedValue) {
		this.grainBasketHarvestedValue = grainBasketHarvestedValue;
	}

	public Double getQuantityTotalCoverageValue() {
		return quantityTotalCoverageValue;
	}

	public void setQuantityTotalCoverageValue(Double quantityTotalCoverageValue) {
		this.quantityTotalCoverageValue = quantityTotalCoverageValue;
	}

	public Double getQuantityTotalYieldValue() {
		return quantityTotalYieldValue;
	}

	public void setQuantityTotalYieldValue(Double quantityTotalYieldValue) {
		this.quantityTotalYieldValue = quantityTotalYieldValue;
	}

	public Double getQuantityTotalClaimAmount() {
		return quantityTotalClaimAmount;
	}

	public void setQuantityTotalClaimAmount(Double quantityTotalClaimAmount) {
		this.quantityTotalClaimAmount = quantityTotalClaimAmount;
	}

	public Double getQuantityTotalYieldLossIndemnity() {
		return quantityTotalYieldLossIndemnity;
	}

	public void setQuantityTotalYieldLossIndemnity(Double quantityTotalYieldLossIndemnity) {
		this.quantityTotalYieldLossIndemnity = quantityTotalYieldLossIndemnity;
	}

	public Double getTotalYieldCoverageValue() {
		return totalYieldCoverageValue;
	}

	public void setTotalYieldCoverageValue(Double totalYieldCoverageValue) {
		this.totalYieldCoverageValue = totalYieldCoverageValue;
	}

	public Double getTotalYieldLoss() {
		return totalYieldLoss;
	}

	public void setTotalYieldLoss(Double totalYieldLoss) {
		this.totalYieldLoss = totalYieldLoss;
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
