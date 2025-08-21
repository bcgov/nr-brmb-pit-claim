package ca.bc.gov.mal.cirras.claims.api.rest.v1.resource;

import java.util.Date;


import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.claims.model.v1.SyncCommodityVariety;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.SYNC_COMMODITY_VARIETY_NAME)
@XmlSeeAlso({ SyncCommodityVarietyRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class SyncCommodityVarietyRsrc extends BaseResource implements SyncCommodityVariety {

	private static final long serialVersionUID = 1L;

	private Integer cropId;
	private String cropName;
	private Integer parentCropId;
	private Boolean isProductInsurable;
	private Boolean isInventoryCrop;
	private Date dataSyncTransDate;
	private String transactionType;

	public Integer getCropId() {
		return cropId;
	}

	public void setCropId(Integer cropId) {
		this.cropId = cropId;
	}


	public String getCropName() {
		return cropName;
	}
	
	public void setCropName(String cropName) {
		this.cropName = cropName;
	}
	

	public Integer getParentCropId() {
		return parentCropId;
	}
	
	public void setParentCropId(Integer parentCropId) {
		this.parentCropId = parentCropId;
	}


	public Boolean getIsProductInsurable() {
		return isProductInsurable;
	}
	
	public void setIsProductInsurable(Boolean isProductInsurable) {
		this.isProductInsurable = isProductInsurable;
	}


	public Boolean getIsInventoryCrop() {
		return isInventoryCrop;
	}

	public void setIsInventoryCrop(Boolean isInventoryCrop) {
		this.isInventoryCrop = isInventoryCrop;
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
