package ca.bc.gov.mal.cirras.claims.data.entities;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class ClaimCalculationBerriesDto extends BaseDto<ClaimCalculationBerriesDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ClaimCalculationBerriesDto.class);

	//Claims Calculation Berries
	private String claimCalculationBerriesGuid;
	private String claimCalculationGuid;
	private Double totalProbableYield;
	private Integer deductibleLevel;
	private Double productionGuarantee;
	private Double declaredAcres;
	private Double confirmedAcres;
	private Double adjustmentFactor;
	private Double adjustedProductionGuarantee;
	private Double insurableValueSelected;
	private Double insurableValueHundredPercent;
	private Double coverageAmountAdjusted;
	private Double maxCoverageAmount;
	private Double harvestedYield;
	private Double appraisedYield;
	private Double abandonedYield;
	private Double totalYieldFromDop;
	private Double totalYieldFromAdjuster;
	private Double yieldAssessment;
	private Double totalYieldForCalculation;
	private Double yieldLossEligible;
	private Integer revisionCount;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;
	
	public ClaimCalculationBerriesDto() {
	}
	
	
	public ClaimCalculationBerriesDto(ClaimCalculationBerriesDto dto) {
		this.claimCalculationBerriesGuid = dto.claimCalculationBerriesGuid;
		this.claimCalculationGuid = dto.claimCalculationGuid;
		this.totalProbableYield = dto.totalProbableYield;
		this.deductibleLevel = dto.deductibleLevel;
		this.productionGuarantee = dto.productionGuarantee;
		this.declaredAcres = dto.declaredAcres;
		this.confirmedAcres = dto.confirmedAcres;
		this.adjustmentFactor = dto.adjustmentFactor;
		this.adjustedProductionGuarantee = dto.adjustedProductionGuarantee;
		this.insurableValueSelected = dto.insurableValueSelected;
		this.insurableValueHundredPercent = dto.insurableValueHundredPercent;
		this.coverageAmountAdjusted = dto.coverageAmountAdjusted;
		this.maxCoverageAmount = dto.maxCoverageAmount;
		this.harvestedYield = dto.harvestedYield;
		this.appraisedYield = dto.appraisedYield;
		this.abandonedYield = dto.abandonedYield;
		this.totalYieldFromDop = dto.totalYieldFromDop;
		this.totalYieldFromAdjuster = dto.totalYieldFromAdjuster;
		this.yieldAssessment = dto.yieldAssessment;
		this.totalYieldForCalculation = dto.totalYieldForCalculation;
		this.yieldLossEligible = dto.yieldLossEligible;
		this.revisionCount = dto.revisionCount;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;
	}
	

	@Override
	public boolean equalsBK(ClaimCalculationBerriesDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(ClaimCalculationBerriesDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			
			// equalsAll is called by dto.isDirty. The other DTO is created using the "copy" constructor
			int decimalPrecision = 4;
			result = result&&dtoUtils.equals("claimCalculationBerriesGuid", claimCalculationBerriesGuid, other.claimCalculationBerriesGuid);
			result = result&&dtoUtils.equals("claimCalculationGuid", claimCalculationGuid, other.claimCalculationGuid);
			result = result&&dtoUtils.equals("totalProbableYield", totalProbableYield, other.totalProbableYield, decimalPrecision);
			result = result&&dtoUtils.equals("deductibleLevel", deductibleLevel, other.deductibleLevel);
			result = result&&dtoUtils.equals("productionGuarantee", productionGuarantee, other.productionGuarantee, decimalPrecision);
			result = result&&dtoUtils.equals("declaredAcres", declaredAcres, other.declaredAcres, decimalPrecision);
			result = result&&dtoUtils.equals("confirmedAcres", confirmedAcres, other.confirmedAcres, decimalPrecision);
			result = result&&dtoUtils.equals("adjustmentFactor", adjustmentFactor, other.adjustmentFactor, decimalPrecision);
			result = result&&dtoUtils.equals("adjustedProductionGuarantee", adjustedProductionGuarantee, other.adjustedProductionGuarantee, decimalPrecision);
			result = result&&dtoUtils.equals("insurableValueSelected", insurableValueSelected, other.insurableValueSelected, 4);
			result = result&&dtoUtils.equals("insurableValueHundredPercent", insurableValueHundredPercent, other.insurableValueHundredPercent, 4);
			result = result&&dtoUtils.equals("coverageAmountAdjusted", coverageAmountAdjusted, other.coverageAmountAdjusted, 2);
			result = result&&dtoUtils.equals("maxCoverageAmount", maxCoverageAmount, other.maxCoverageAmount, 2);
			result = result&&dtoUtils.equals("harvestedYield", harvestedYield, other.harvestedYield, decimalPrecision);
			result = result&&dtoUtils.equals("appraisedYield", appraisedYield, other.appraisedYield, decimalPrecision);
			result = result&&dtoUtils.equals("abandonedYield", abandonedYield, other.abandonedYield, decimalPrecision);
			result = result&&dtoUtils.equals("totalYieldFromDop", totalYieldFromDop, other.totalYieldFromDop, decimalPrecision);
			result = result&&dtoUtils.equals("totalYieldFromAdjuster", totalYieldFromAdjuster, other.totalYieldFromAdjuster, decimalPrecision);
			result = result&&dtoUtils.equals("yieldAssessment", yieldAssessment, other.yieldAssessment, decimalPrecision);
			result = result&&dtoUtils.equals("totalYieldForCalculation", totalYieldForCalculation, other.totalYieldForCalculation, decimalPrecision);
			result = result&&dtoUtils.equals("yieldLossEligible", yieldLossEligible, other.yieldLossEligible, decimalPrecision);
			result = result&&dtoUtils.equals("revisionCount", revisionCount, other.revisionCount);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public ClaimCalculationBerriesDto copy() {
		return new ClaimCalculationBerriesDto(this);
	}
	
	public String getClaimCalculationBerriesGuid() {
		return claimCalculationBerriesGuid;
	}

	public void setClaimCalculationBerriesGuid(String claimCalculationBerriesGuid) {
		this.claimCalculationBerriesGuid = claimCalculationBerriesGuid;
	}

	public String getClaimCalculationGuid() {
		return claimCalculationGuid;
	}
	
	public void setClaimCalculationGuid(String claimCalculationGuid) {
		this.claimCalculationGuid = claimCalculationGuid;
	}
	
	public Double getTotalProbableYield() {
		return totalProbableYield;
	}
	
	public void setTotalProbableYield(Double totalProbableYield) {
		this.totalProbableYield = totalProbableYield;
	}
	
	public Integer getDeductibleLevel() {
		return deductibleLevel;
	}
	
	public void setDeductibleLevel(Integer deductibleLevel) {
		this.deductibleLevel = deductibleLevel;
	}
	
	public Double getProductionGuarantee() {
		return productionGuarantee;
	}
		
	public void setProductionGuarantee(Double productionGuarantee) {
		this.productionGuarantee = productionGuarantee;
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
	
	public Double getAdjustmentFactor() {
		return adjustmentFactor;
	}
		
	public void setAdjustmentFactor(Double adjustmentFactor) {
		this.adjustmentFactor = adjustmentFactor;
	}
	
	public Double getAdjustedProductionGuarantee() {
		return adjustedProductionGuarantee;
	}
		
	public void setAdjustedProductionGuarantee(Double adjustedProductionGuarantee) {
		this.adjustedProductionGuarantee = adjustedProductionGuarantee;
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

	public Double getCoverageAmountAdjusted() {
		return coverageAmountAdjusted;
	}

	public void setCoverageAmountAdjusted(Double coverageAmountAdjusted) {
		this.coverageAmountAdjusted = coverageAmountAdjusted;
	}
	
	public Double getMaxCoverageAmount() {
		return maxCoverageAmount;
	}

	public void setMaxCoverageAmount(Double maxCoverageAmount) {
		this.maxCoverageAmount = maxCoverageAmount;
	}
	
	public Double getHarvestedYield() {
		return harvestedYield;
	}
		
	public void setHarvestedYield(Double harvestedYield) {
		this.harvestedYield = harvestedYield;
	}
	
	public Double getAppraisedYield() {
		return appraisedYield;
	}
		
	public void setAppraisedYield(Double appraisedYield) {
		this.appraisedYield = appraisedYield;
	}
	
	public Double getAbandonedYield() {
		return abandonedYield;
	}
		
	public void setAbandonedYield(Double abandonedYield) {
		this.abandonedYield = abandonedYield;
	}
	
	public Double getTotalYieldFromDop() {
		return totalYieldFromDop;
	}
	
	public void setTotalYieldFromDop(Double totalYieldFromDop) {
		this.totalYieldFromDop = totalYieldFromDop;
	}
	
	public Double getTotalYieldFromAdjuster() {
		return totalYieldFromAdjuster;
	}
		
	public void setTotalYieldFromAdjuster(Double totalYieldFromAdjuster) {
		this.totalYieldFromAdjuster = totalYieldFromAdjuster;
	}
	
	public Double getYieldAssessment() {
		return yieldAssessment;
	}
		
	public void setYieldAssessment(Double yieldAssessment) {
		this.yieldAssessment = yieldAssessment;
	}
	
	public Double getTotalYieldForCalculation() {
		return totalYieldForCalculation;
	}
		
	public void setTotalYieldForCalculation(Double totalYieldForCalculation) {
		this.totalYieldForCalculation = totalYieldForCalculation;
	}
	
	public Double getYieldLossEligible() {
		return yieldLossEligible;
	}
		
	public void setYieldLossEligible(Double yieldLossEligible) {
		this.yieldLossEligible = yieldLossEligible;
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
