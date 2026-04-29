package ca.bc.gov.mal.cirras.claims.data.models;

import java.io.Serializable;

//
// This is not going to be a resource.
//
public class SyncDopYieldContractCommodityBerries implements Serializable {
	private static final long serialVersionUID = 1L;

	private String declaredYieldContractCommodityBerriesGuid;
	private Integer cropCommodityId;
	private String cropCommodityName;
	private Double totalProduction;
	private Double totalProductionOverride;
	private Double totalSoldShippedYield;
	private Double totalSalesYield;
	private Double totalAbandonmentYield;

	public String getDeclaredYieldContractCommodityBerriesGuid() {
		return declaredYieldContractCommodityBerriesGuid;
	}
	public void setDeclaredYieldContractCommodityBerriesGuid(String declaredYieldContractCommodityBerriesGuid) {
		this.declaredYieldContractCommodityBerriesGuid = declaredYieldContractCommodityBerriesGuid;
	}

	public Integer getCropCommodityId() {
		return cropCommodityId;
	}
	public void setCropCommodityId(Integer cropCommodityId) {
		this.cropCommodityId = cropCommodityId;
	}

	public String getCropCommodityName() {
		return cropCommodityName;
	}
	public void setCropCommodityName(String cropCommodityName) {
		this.cropCommodityName = cropCommodityName;
	}

	public Double getTotalProduction() {
		return totalProduction;
	}
	public void setTotalProduction(Double totalProduction) {
		this.totalProduction = totalProduction;
	}

	public Double getTotalProductionOverride() {
		return totalProductionOverride;
	}
	public void setTotalProductionOverride(Double totalProductionOverride) {
		this.totalProductionOverride = totalProductionOverride;
	}

	public Double getTotalSoldShippedYield() {
		return totalSoldShippedYield;
	}

	public void setTotalSoldShippedYield(Double totalSoldShippedYield) {
		this.totalSoldShippedYield = totalSoldShippedYield;
	}

	public Double getTotalSalesYield() {
		return totalSalesYield;
	}

	public void setTotalSalesYield(Double totalSalesYield) {
		this.totalSalesYield = totalSalesYield;
	}

	public Double getTotalAbandonmentYield() {
		return totalAbandonmentYield;
	}

	public void setTotalAbandonmentYield(Double totalAbandonmentYield) {
		this.totalAbandonmentYield = totalAbandonmentYield;
	}

}
