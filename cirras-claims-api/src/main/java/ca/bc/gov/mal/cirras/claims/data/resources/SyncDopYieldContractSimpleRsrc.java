package ca.bc.gov.mal.cirras.claims.data.resources;

import java.util.Date;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.claims.data.models.SyncDopYieldContractCommodityBerries;
import ca.bc.gov.mal.cirras.claims.data.resources.types.ResourceTypes;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.SYNC_DOP_YIELD_CONTRACT_SIMPLE_NAME)
@XmlSeeAlso({ SyncDopYieldContractSimpleRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class SyncDopYieldContractSimpleRsrc extends BaseResource {

	private static final long serialVersionUID = 1L;

	private String declaredYieldContractGuid;
	private Integer contractId;
	private Integer cropYear;

	private Date dataSyncTransDate;
	private String transactionType;

	private SyncDopYieldContractCommodityBerries syncDopYieldContractCommodityBerries;
	
	public String getDeclaredYieldContractGuid() {
		return declaredYieldContractGuid;
	}
	public void setDeclaredYieldContractGuid(String declaredYieldContractGuid) {
		this.declaredYieldContractGuid = declaredYieldContractGuid;
	}

	public Integer getContractId() {
		return contractId;
	}
	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getCropYear() {
		return cropYear;
	}
	public void setCropYear(Integer cropYear) {
		this.cropYear = cropYear;
	}

	public Date getDataSyncTransDate() {
		return dataSyncTransDate;
	}
	public void setDataSyncTransDate(Date dataSyncTransDate) {
		this.dataSyncTransDate = dataSyncTransDate;
	}

	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public SyncDopYieldContractCommodityBerries getSyncDopYieldContractCommodityBerries() {
		return syncDopYieldContractCommodityBerries;
	}
	public void setSyncDopYieldContractCommodityBerries(SyncDopYieldContractCommodityBerries syncDopYieldContractCommodityBerries) {
		this.syncDopYieldContractCommodityBerries = syncDopYieldContractCommodityBerries;
	}
	
}
