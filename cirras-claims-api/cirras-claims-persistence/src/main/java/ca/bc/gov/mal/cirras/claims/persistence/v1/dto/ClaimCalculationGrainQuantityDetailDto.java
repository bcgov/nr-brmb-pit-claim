package ca.bc.gov.mal.cirras.claims.persistence.v1.dto;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class ClaimCalculationGrainQuantityDetailDto extends BaseDto<ClaimCalculationGrainQuantityDetailDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ClaimCalculationGrainQuantityDetailDto.class);

	private String claimCalculationGrainQuantityDetailGuid;
	private String claimCalculationGuid;
	private Double insuredAcres;
	private Double probableYield;
	private Integer deductible;
	private Double productionGuaranteeWeight;
	private Double insurableValue;
	private Double coverageValue;
	private Double totalYieldToCount;
	private Double assessedYield;
	private Double earlyEstDeemedYieldValue;
	private Double damagedAcres;
	private Double seededAcres;
	private Double fiftyPercentProductionGuarantee;
	private Double calcEarlyEstYield;
	private Double inspEarlyEstYield;
	private Double yieldValue;
	private Double yieldValueWithEarlyEstDeemedYield;
	
	private Integer revisionCount;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	
	public ClaimCalculationGrainQuantityDetailDto() {
	}
	
	
	public ClaimCalculationGrainQuantityDetailDto(ClaimCalculationGrainQuantityDetailDto dto) {

		this.claimCalculationGrainQuantityDetailGuid = dto.claimCalculationGrainQuantityDetailGuid;
		this.claimCalculationGuid = dto.claimCalculationGuid;
		this.insuredAcres = dto.insuredAcres;
		this.probableYield = dto.probableYield;
		this.deductible = dto.deductible;
		this.productionGuaranteeWeight = dto.productionGuaranteeWeight;
		this.insurableValue = dto.insurableValue;
		this.coverageValue = dto.coverageValue;
		this.totalYieldToCount = dto.totalYieldToCount;
		this.assessedYield = dto.assessedYield;
		this.earlyEstDeemedYieldValue = dto.earlyEstDeemedYieldValue;
		this.damagedAcres = dto.damagedAcres;
		this.seededAcres = dto.seededAcres;
		this.fiftyPercentProductionGuarantee = dto.fiftyPercentProductionGuarantee;
		this.calcEarlyEstYield = dto.calcEarlyEstYield;
		this.inspEarlyEstYield = dto.inspEarlyEstYield;
		this.yieldValue = dto.yieldValue;
		this.yieldValueWithEarlyEstDeemedYield = dto.yieldValueWithEarlyEstDeemedYield;
		
		this.revisionCount = dto.revisionCount;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;

	}
	
	@Override
	public boolean equalsBK(ClaimCalculationGrainQuantityDetailDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(ClaimCalculationGrainQuantityDetailDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			
			// equalsAll is called by dto.isDirty. The other DTO is created using the "copy" constructor
			int decimalPrecision = 4;
			result = result&&dtoUtils.equals("claimCalculationGrainQuantityDetailGuid", claimCalculationGrainQuantityDetailGuid, other.claimCalculationGrainQuantityDetailGuid);
			result = result&&dtoUtils.equals("claimCalculationGuid", claimCalculationGuid, other.claimCalculationGuid);
			result = result&&dtoUtils.equals("insuredAcres", insuredAcres, other.insuredAcres, decimalPrecision);
			result = result&&dtoUtils.equals("probableYield", probableYield, other.probableYield, decimalPrecision);			
			result = result&&dtoUtils.equals("deductible", deductible, other.deductible);
			result = result&&dtoUtils.equals("productionGuaranteeWeight", productionGuaranteeWeight, other.productionGuaranteeWeight, decimalPrecision);			
			result = result&&dtoUtils.equals("insurableValue", insurableValue, other.insurableValue, decimalPrecision);			
			result = result&&dtoUtils.equals("coverageValue", coverageValue, other.coverageValue, decimalPrecision);			
			result = result&&dtoUtils.equals("totalYieldToCount", totalYieldToCount, other.totalYieldToCount, decimalPrecision);			
			result = result&&dtoUtils.equals("assessedYield", assessedYield, other.assessedYield, decimalPrecision);			
			result = result&&dtoUtils.equals("earlyEstDeemedYieldValue", earlyEstDeemedYieldValue, other.earlyEstDeemedYieldValue, decimalPrecision);			
			result = result&&dtoUtils.equals("damagedAcres", damagedAcres, other.damagedAcres, decimalPrecision);			
			result = result&&dtoUtils.equals("seededAcres", seededAcres, other.seededAcres, decimalPrecision);			
			result = result&&dtoUtils.equals("fiftyPercentProductionGuarantee", fiftyPercentProductionGuarantee, other.fiftyPercentProductionGuarantee, decimalPrecision);			
			result = result&&dtoUtils.equals("calcEarlyEstYield", calcEarlyEstYield, other.calcEarlyEstYield, decimalPrecision);			
			result = result&&dtoUtils.equals("inspEarlyEstYield", inspEarlyEstYield, other.inspEarlyEstYield, decimalPrecision);			
			result = result&&dtoUtils.equals("yieldValue", yieldValue, other.yieldValue, decimalPrecision);			
			result = result&&dtoUtils.equals("yieldValueWithEarlyEstDeemedYield", yieldValueWithEarlyEstDeemedYield, other.yieldValueWithEarlyEstDeemedYield, decimalPrecision);
			result = result&&dtoUtils.equals("revisionCount", revisionCount, other.revisionCount);

		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public ClaimCalculationGrainQuantityDetailDto copy() {
		return new ClaimCalculationGrainQuantityDetailDto(this);
	}

	public String getClaimCalculationGrainQuantityDetailGuid() {
		return claimCalculationGrainQuantityDetailGuid;
	}
	public void setClaimCalculationGrainQuantityDetailGuid(String claimCalculationGrainQuantityDetailGuid) {
		this.claimCalculationGrainQuantityDetailGuid = claimCalculationGrainQuantityDetailGuid;
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

	public Double getProbableYield() {
		return probableYield;
	}
	public void setProbableYield(Double probableYield) {
		this.probableYield = probableYield;
	}

	public Integer getDeductible() {
		return deductible;
	}
	public void setDeductible(Integer deductible) {
		this.deductible = deductible;
	}

	public Double getProductionGuaranteeWeight() {
		return productionGuaranteeWeight;
	}
	public void setProductionGuaranteeWeight(Double productionGuaranteeWeight) {
		this.productionGuaranteeWeight = productionGuaranteeWeight;
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

	public Double getEarlyEstDeemedYieldValue() {
		return earlyEstDeemedYieldValue;
	}
	public void setEarlyEstDeemedYieldValue(Double earlyEstDeemedYieldValue) {
		this.earlyEstDeemedYieldValue = earlyEstDeemedYieldValue;
	}

	public Double getDamagedAcres() {
		return damagedAcres;
	}
	public void setDamagedAcres(Double damagedAcres) {
		this.damagedAcres = damagedAcres;
	}

	public Double getSeededAcres() {
		return seededAcres;
	}
	public void setSeededAcres(Double seededAcres) {
		this.seededAcres = seededAcres;
	}

	public Double getFiftyPercentProductionGuarantee() {
		return fiftyPercentProductionGuarantee;
	}
	public void setFiftyPercentProductionGuarantee(Double fiftyPercentProductionGuarantee) {
		this.fiftyPercentProductionGuarantee = fiftyPercentProductionGuarantee;
	}

	public Double getCalcEarlyEstYield() {
		return calcEarlyEstYield;
	}
	public void setCalcEarlyEstYield(Double calcEarlyEstYield) {
		this.calcEarlyEstYield = calcEarlyEstYield;
	}

	public Double getInspEarlyEstYield() {
		return inspEarlyEstYield;
	}
	public void setInspEarlyEstYield(Double inspEarlyEstYield) {
		this.inspEarlyEstYield = inspEarlyEstYield;
	}

	public Double getYieldValue() {
		return yieldValue;
	}
	public void setYieldValue(Double yieldValue) {
		this.yieldValue = yieldValue;
	}

	public Double getYieldValueWithEarlyEstDeemedYield() {
		return yieldValueWithEarlyEstDeemedYield;
	}
	public void setYieldValueWithEarlyEstDeemedYield(Double yieldValueWithEarlyEstDeemedYield) {
		this.yieldValueWithEarlyEstDeemedYield = yieldValueWithEarlyEstDeemedYield;
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
