package ca.bc.gov.mal.cirras.claims.model.v1;

import java.io.Serializable;

//
// This is not going to be a resource.
//
public class ClaimCalculationGrainSpotLoss implements Serializable {
	private static final long serialVersionUID = 5726905902897809547L;
	
	
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

	// Out of sync flags
	
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
}
