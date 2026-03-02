package ca.bc.gov.mal.cirras.claims.data.models;

import java.io.Serializable;
import java.util.Date;


public interface SyncClaim extends Serializable {

	public Integer getColId();
	public void setColId(Integer colId);
	
	public Integer getClaimNumber();
	public void setClaimNumber(Integer claimNumber);

	public String getClaimStatusCode();
	public void setClaimStatusCode(String claimStatusCode);
	
	public String getSubmittedByUserid();
	public void setSubmittedByUserid(String submittedByUserid);

	public String getSubmittedByName();
	public void setSubmittedByName(String submittedByName);

	public Date getSubmittedByDate();
	public void setSubmittedByDate(Date submittedByDate);

	public String getRecommendedByUserid();
	public void setRecommendedByUserid(String value);
	
	public String getRecommendedByName();
	public void setRecommendedByName(String value);
	
	public Date getRecommendedByDate();
	public void setRecommendedByDate(Date value);

	public String getApprovedByUserid();
	public void setApprovedByUserid(String approvedByUserid);

	public String getApprovedByName();
	public void setApprovedByName(String approvedByName);

	public Date getApprovedByDate();
	public void setApprovedByDate(Date approvedByDate);

	public Boolean getHasChequeReqInd();
	public void setHasChequeReqInd(Boolean hasChequeReqInd);
	
	public Integer getIppId();
	public void setIppId(Integer ippId);
	
	public String getCommodityCoverageCode();
	public void setCommodityCoverageCode(String commodityCoverageCode);

	public Integer getCropCommodityId();
	public void setCropCommodityId(Integer cropCommodityId);
	
	public Integer getIplId();
	public void setIplId(Integer iplId);
	
	public String getPolicyNumber();
	public void setPolicyNumber(String policyNumber);
	
	public Integer getInsurancePlanId();
	public void setInsurancePlanId(Integer insurancePlanId);

	public Integer getCropYear();
	public void setCropYear(Integer cropYear);
	
	public Integer getContractId();
	public void setContractId(Integer contractId);	

	public Integer getIgId();
	public void setIgId(Integer igId);
	
	public String getGrowerName();
	public void setGrowerName(String growerName);

	public Date getClaimDataSyncTransDate();
	public void setClaimDataSyncTransDate(Date claimDataSyncTransDate);
	
	public Date getPolicyDataSyncTransDate();
	public void setPolicyDataSyncTransDate(Date policyDataSyncTransDate);
	
	public Date getGrowerDataSyncTransDate();
	public void setGrowerDataSyncTransDate(Date growerDataSyncTransDate);
	
	public String getTransactionType();
	public void setTransactionType(String transactionType);

}
