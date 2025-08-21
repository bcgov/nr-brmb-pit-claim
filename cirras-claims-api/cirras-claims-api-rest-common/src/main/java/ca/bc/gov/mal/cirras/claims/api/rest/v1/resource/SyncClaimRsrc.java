package ca.bc.gov.mal.cirras.claims.api.rest.v1.resource;

import java.util.Date;


import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.claims.model.v1.SyncClaim;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.SYNC_CLAIM_NAME)
@XmlSeeAlso({ SyncClaimRsrc.class })
//@JsonSubTypes({ @Type(value = ResourceContactRsrc.class, name = ResourceTypes.RESOURCE_CONTACT) })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class SyncClaimRsrc extends BaseResource implements SyncClaim {

	private static final long serialVersionUID = 1L;

	//Claim
	private Integer colId;
	private Integer claimNumber;
	private String claimStatusCode;
	private String submittedByUserid;
	private String submittedByName;
	private Date submittedByDate;
	private String recommendedByUserid;
	private String recommendedByName;
	private Date recommendedByDate;
	private String approvedByUserid;
	private String approvedByName;
	private Date approvedByDate;
	private Boolean hasChequeReqInd;
	
	//Purchase
	private Integer ippId;
	private String commodityCoverageCode;
	private Integer cropCommodityId;
	
	//Policy
	private Integer iplId;
	private String policyNumber;
	private Integer insurancePlanId;
	private Integer cropYear;
	private Integer contractId;
	private Integer igId;
	
	//Grower
	private String growerName;

	//Message Properties
	private Date claimDataSyncTransDate;
	private Date policyDataSyncTransDate;
	private Date growerDataSyncTransDate;
	private String transactionType;

	public Integer getColId() {
		return colId;
	}
	
	public void setColId(Integer colId) {
		this.colId = colId;
	}
	
	public Integer getClaimNumber() {
		return claimNumber;
	}
	
	public void setClaimNumber(Integer claimNumber) {
		this.claimNumber = claimNumber;
	}

	public String getClaimStatusCode() {
		return claimStatusCode;
	}
		
	public void setClaimStatusCode(String claimStatusCode) {
		this.claimStatusCode = claimStatusCode;
	}
	public String getSubmittedByUserid() {
		return submittedByUserid;
	}

	public void setSubmittedByUserid(String submittedByUserid) {
		this.submittedByUserid = submittedByUserid;
	}

	public String getSubmittedByName() {
		return submittedByName;
	}

	public void setSubmittedByName(String submittedByName) {
		this.submittedByName = submittedByName;
	}

	public Date getSubmittedByDate() {
		return submittedByDate;
	}

	public void setSubmittedByDate(Date submittedByDate) {
		this.submittedByDate = submittedByDate;
	}	

	public String getRecommendedByUserid() {
		return recommendedByUserid;
	}

	public void setRecommendedByUserid(String recommendedByUserid) {
		this.recommendedByUserid = recommendedByUserid;
	}

	public String getRecommendedByName() {
		return recommendedByName;
	}

	public void setRecommendedByName(String recommendedByName) {
		this.recommendedByName = recommendedByName;
	}

	public Date getRecommendedByDate() {
		return recommendedByDate;
	}

	public void setRecommendedByDate(Date recommendedByDate) {
		this.recommendedByDate = recommendedByDate;
	}


	public String getApprovedByUserid() {
		return approvedByUserid;
	}

	public void setApprovedByUserid(String approvedByUserid) {
		this.approvedByUserid = approvedByUserid;
	}

	public String getApprovedByName() {
		return approvedByName;
	}

	public void setApprovedByName(String approvedByName) {
		this.approvedByName = approvedByName;
	}

	public Date getApprovedByDate() {
		return approvedByDate;
	}

	public void setApprovedByDate(Date approvedByDate) {
		this.approvedByDate = approvedByDate;
	}

	public Boolean getHasChequeReqInd() {
		return hasChequeReqInd;
	}
	
	public void setHasChequeReqInd(Boolean hasChequeReqInd) {
		this.hasChequeReqInd = hasChequeReqInd;
	}
	
	public Integer getIppId() {
		return ippId;
	}
		
	public void setIppId(Integer ippId) {
		this.ippId = ippId;
	}
	
	public String getCommodityCoverageCode() {
		return commodityCoverageCode;
	}
		
	public void setCommodityCoverageCode(String commodityCoverageCode) {
		this.commodityCoverageCode = commodityCoverageCode;
	}
	
	public Integer getCropCommodityId() {
		return cropCommodityId;
	}
		
	public void setCropCommodityId(Integer cropCommodityId) {
		this.cropCommodityId = cropCommodityId;
	}
	
	public Integer getIplId() {
		return iplId;
	}
		
	public void setIplId(Integer iplId) {
		this.iplId = iplId;
	}
	
	public String getPolicyNumber() {
		return policyNumber;
	}
		
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	
	public Integer getInsurancePlanId() {
		return insurancePlanId;
	}
		
	public void setInsurancePlanId(Integer insurancePlanId) {
		this.insurancePlanId = insurancePlanId;
	}
	
	public Integer getCropYear() {
		return cropYear;
	}
		
	public void setCropYear(Integer cropYear) {
		this.cropYear = cropYear;
	}
	
	public Integer getContractId() {
		return contractId;
	}
		
	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}
	
	public Integer getIgId() {
		return igId;
	}
		
	public void setIgId(Integer igId) {
		this.igId = igId;
	}
	
	public String getGrowerName() {
		return growerName;
	}
		
	public void setGrowerName(String growerName) {
		this.growerName = growerName;
	}

	public Date getClaimDataSyncTransDate() {
		return claimDataSyncTransDate;
	}
	
	public void setClaimDataSyncTransDate(Date claimDataSyncTransDate) {
		this.claimDataSyncTransDate = claimDataSyncTransDate;
	}
	
	public Date getPolicyDataSyncTransDate() {
		return policyDataSyncTransDate;
	}
	
	public void setPolicyDataSyncTransDate(Date policyDataSyncTransDate) {
		this.policyDataSyncTransDate = policyDataSyncTransDate;
	}
	
	public Date getGrowerDataSyncTransDate() {
		return growerDataSyncTransDate;
	}
	
	public void setGrowerDataSyncTransDate(Date growerDataSyncTransDate) {
		this.growerDataSyncTransDate = growerDataSyncTransDate;
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
