package ca.bc.gov.mal.cirras.claims.data.models;

import java.io.Serializable;

//
// This is not going to be a resource.
//
public class ClaimCalculationVariety implements Serializable {
	private static final long serialVersionUID = 5726905902897809547L;

	private String claimCalculationVarietyGuid;
	private String claimCalculationGuid;
	private String varietyName;
	private Integer cropVarietyId;

	private Double averagePrice;
	private Double averagePriceOverride;
	private Double averagePriceFinal;
	
	private Double insurableValue;
	private Double yieldAssessed;
	private Double yieldTotal;
	private Double yieldActual;
	private Double varietyProductionValue;
	private String yieldAssessedReason;

	// Out of sync flags
	private Boolean isOutOfSyncVarietyRemoved;
	private Boolean isOutOfSyncAvgPrice;

	// TODO: Probably not needed.
	//private Boolean isOutOfSyncInsurableValue;
	
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

	public String getClaimCalculationVarietyGuid() {
		return claimCalculationVarietyGuid;
	}

	public void setClaimCalculationVarietyGuid(String claimCalculationVarietyGuid) {
		this.claimCalculationVarietyGuid = claimCalculationVarietyGuid;
	}

	public String getClaimCalculationGuid() {
		return claimCalculationGuid;
	}

	public void setClaimCalculationGuid(String claimCalculationGuid) {
		this.claimCalculationGuid = claimCalculationGuid;
	}

	public String getVarietyName() {
		return varietyName;
	}

	public void setVarietyName(String varietyName) {
		this.varietyName = varietyName;
	}
	
	public Integer getCropVarietyId() {
		return cropVarietyId;
	}

	public void setCropVarietyId(Integer cropVarietyId) {
		this.cropVarietyId = cropVarietyId;
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

	public Boolean getIsOutOfSyncVarietyRemoved() {
		return isOutOfSyncVarietyRemoved;
	}
	
	public void setIsOutOfSyncVarietyRemoved(Boolean isOutOfSyncVarietyRemoved) {
		this.isOutOfSyncVarietyRemoved = isOutOfSyncVarietyRemoved;
	}

	public Boolean getIsOutOfSyncAvgPrice() {
		return isOutOfSyncAvgPrice;
	}
	
	public void setIsOutOfSyncAvgPrice(Boolean isOutOfSyncAvgPrice) {
		this.isOutOfSyncAvgPrice = isOutOfSyncAvgPrice;
	}
}
