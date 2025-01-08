package ca.bc.gov.mal.cirras.claims.persistence.v1.dto;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;




public class ClaimCalculationVarietyDto extends BaseDto<ClaimCalculationVarietyDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ClaimCalculationVarietyDto.class);

	//Claims Calculation Variety
	private String claimCalculationVarietyGuid;
	private String claimCalculationGuid;
	private Double yieldAssessed;
	private Double yieldTotal;
	private Double yieldActual;
	private Double varietyProductionValue;
	private String yieldAssessedReason;
	private Integer revisionCount;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;
	
	// from CIRRAS
	private Integer cropVarietyId;
	private String varietyName;
	private Double averagePrice;
	private Double averagePriceOverride;
	private Double averagePriceFinal;
	private Double insurableValue;
	
	public ClaimCalculationVarietyDto() {
	}
	
	
	public ClaimCalculationVarietyDto(ClaimCalculationVarietyDto dto) {
		this.claimCalculationGuid = dto.claimCalculationGuid;
		this.claimCalculationVarietyGuid = dto.claimCalculationVarietyGuid;
		this.cropVarietyId = dto.cropVarietyId;
		this.varietyName = dto.varietyName;
		this.averagePrice = dto.averagePrice;
		this.averagePriceOverride = dto.averagePriceOverride;
		this.averagePriceFinal = dto.averagePriceFinal;
		this.insurableValue = dto.insurableValue;
		this.yieldAssessed = dto.yieldAssessed;
		this.yieldTotal = dto.yieldTotal;
		this.yieldActual = dto.yieldActual;
		this.varietyProductionValue = dto.varietyProductionValue;
		this.yieldAssessedReason = dto.yieldAssessedReason;
		this.revisionCount = dto.revisionCount;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;
	}
	

	@Override
	public boolean equalsBK(ClaimCalculationVarietyDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(ClaimCalculationVarietyDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			
			// equalsAll is called by dto.isDirty. The other DTO is created using the "copy" constructor
			int decimalPrecision = 4;
			result = result&&dtoUtils.equals("claimCalculationVarietyGuid", claimCalculationVarietyGuid, other.claimCalculationVarietyGuid);
			result = result&&dtoUtils.equals("claimCalculationGuid", claimCalculationGuid, other.claimCalculationGuid);
			result = result&&dtoUtils.equals("cropVarietyId", cropVarietyId, other.cropVarietyId);
			result = result&&dtoUtils.equals("varietyName", varietyName, other.varietyName);
			result = result&&dtoUtils.equals("averagePrice", averagePrice, other.averagePrice, decimalPrecision);
			result = result&&dtoUtils.equals("averagePriceOverride", averagePriceOverride, other.averagePriceOverride, decimalPrecision);
			result = result&&dtoUtils.equals("averagePriceFinal", averagePriceFinal, other.averagePriceFinal, decimalPrecision);
			result = result&&dtoUtils.equals("insurableValue", insurableValue, other.insurableValue, decimalPrecision);
			result = result&&dtoUtils.equals("yieldAssessed", yieldAssessed, other.yieldAssessed, 2);
			result = result&&dtoUtils.equals("yieldTotal", yieldTotal, other.yieldTotal, decimalPrecision);
			result = result&&dtoUtils.equals("yieldActual", yieldActual, other.yieldActual, decimalPrecision);
			result = result&&dtoUtils.equals("varietyProductionValue", varietyProductionValue, other.varietyProductionValue, 2);
			result = result&&dtoUtils.equals("yieldAssessedReason", yieldAssessedReason, other.yieldAssessedReason);
			result = result&&dtoUtils.equals("revisionCount", revisionCount, other.revisionCount);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public ClaimCalculationVarietyDto copy() {
		return new ClaimCalculationVarietyDto(this);
	}

	public Double getAveragePrice() {
		return averagePrice;
	}

	public void setAveragePrice(Double averagePrice) {
		this.averagePrice = averagePrice;
	}

 	public Double getAveragePriceOverride() {
		return averagePriceOverride;
	}

	public void setAveragePriceOverride(Double averagePriceOverride) {
		this.averagePriceOverride = averagePriceOverride;
	}
 
 	public Double getAveragePriceFinal() {
		return averagePriceFinal;
	}

	public void setAveragePriceFinal(Double averagePriceFinal) {
		this.averagePriceFinal = averagePriceFinal;
	}

	public String getClaimCalculationGuid() {
		return claimCalculationGuid;
	}

	public void setClaimCalculationGuid(String claimCalculationGuid) {
		this.claimCalculationGuid = claimCalculationGuid;
	}

	public String getClaimCalculationVarietyGuid() {
		return claimCalculationVarietyGuid;
	}

	public void setClaimCalculationVarietyGuid(String claimCalculationVarietyGuid) {
		this.claimCalculationVarietyGuid = claimCalculationVarietyGuid;
	}

	public Integer getCropVarietyId() {
		return cropVarietyId;
	}

	public void setCropVarietyId(Integer cropVarietyId) {
		this.cropVarietyId = cropVarietyId;
	}

	public String getVarietyName() {
		return varietyName;
	}

	public void setVarietyName(String varietyName) {
		this.varietyName = varietyName;
	}

	public Double getInsurableValue() {
		return insurableValue;
	}

	public void setInsurableValue(Double insurableValue) {
		this.insurableValue = insurableValue;
	}

	public Double getYieldAssessed() {
		return yieldAssessed;
	}

	public void setYieldAssessed(Double yieldAssessed) {
		this.yieldAssessed = yieldAssessed;
	}

	public Double getYieldTotal() {
		return yieldTotal;
	}
	public void setYieldTotal(Double yieldTotal) {
		this.yieldTotal = yieldTotal;
	}


	public String getYieldAssessedReason() {
		return yieldAssessedReason;
	}

	public void setYieldAssessedReason(String yieldAssessedReason) {
		this.yieldAssessedReason = yieldAssessedReason;
	}


	public Double getYieldActual() {
		return yieldActual;
	}


	public void setYieldActual(Double yieldActual) {
		this.yieldActual = yieldActual;
	}


	public Double getVarietyProductionValue() {
		return varietyProductionValue;
	}


	public void setVarietyProductionValue(Double varietyProductionValue) {
		this.varietyProductionValue = varietyProductionValue;
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
