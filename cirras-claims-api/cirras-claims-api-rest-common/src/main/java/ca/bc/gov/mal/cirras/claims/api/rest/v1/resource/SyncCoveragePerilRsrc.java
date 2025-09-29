package ca.bc.gov.mal.cirras.claims.api.rest.v1.resource;

import java.util.Date;


import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.claims.model.v1.SyncCoveragePeril;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.SYNC_COVERAGE_PERIL_NAME)
@XmlSeeAlso({ SyncCoveragePerilRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class SyncCoveragePerilRsrc extends BaseResource implements SyncCoveragePeril {

	private static final long serialVersionUID = 1L;

	private Integer coveragePerilId;
	private Integer cropCommodityId;
	private String perilCode;
	private String commodityCoverageCode;
	private Boolean isActive;
	private Date dataSyncTransDate;
	private String transactionType;


	public Integer getCoveragePerilId() {
		return coveragePerilId;
	}
	
	public void setCoveragePerilId(Integer coveragePerilId) {
		this.coveragePerilId = coveragePerilId;
	}
	
 	public Integer getCropCommodityId() {
		return cropCommodityId;
	}

	public void setCropCommodityId(Integer cropCommodityId) {
		this.cropCommodityId = cropCommodityId;
	}
 
 	public String getPerilCode() {
		return perilCode;
	}

	public void setPerilCode(String perilCode) {
		this.perilCode = perilCode;
	}
 
 	public String getCommodityCoverageCode() {
		return commodityCoverageCode;
	}

	public void setCommodityCoverageCode(String commodityCoverageCode) {
		this.commodityCoverageCode = commodityCoverageCode;
	}
 
 	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
	public Date getDataSyncTransDate() {
		return dataSyncTransDate;
	}
	
	public void setDataSyncTransDate(Date dataSyncTransDate) {
		this.dataSyncTransDate = dataSyncTransDate;
	}

	@Override
	public String getTransactionType() {
		return transactionType;
	}

	@Override
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
}
