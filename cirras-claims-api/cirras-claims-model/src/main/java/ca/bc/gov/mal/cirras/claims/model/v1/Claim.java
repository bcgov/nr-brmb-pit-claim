package ca.bc.gov.mal.cirras.claims.model.v1;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public interface Claim extends Serializable {

	public Integer getClaimNumber();
	public void setClaimNumber(Integer claimNumber);

	public String getPolicyNumber();
	public void setPolicyNumber(String policyNumber);

	public Integer getGrowerNumber();
	public void setGrowerNumber(Integer growerNumber);

	public String getGrowerName();
	public void setGrowerName(String growerName) ;

	public Integer getInsurancePlanId();
	public void setInsurancePlanId(Integer insurancePlanId);

	public String getInsurancePlanName();
	public void setInsurancePlanName(String insurancePlanName);

	public Integer getContractId();
	public void setContractId(Integer contractId);

	public String getCoverageName();
	public void setCoverageName(String coverageName);
	
	public String getCommodityCoverageCode();
	public void setCommodityCoverageCode(String commodityCoverageCode);
	
	public String getCommodityName();
	public void setCommodityName(String commodityName);
	
	public Integer getCropCommodityId();
	public void setCropCommodityId(Integer cropCommodityId);
	
	public String getClaimStatus();
	public void setClaimStatus(String claimStatus);
	
	public String getClaimStatusCode();
	public void setClaimStatusCode(String claimStatusCode);
	
	public String getClaimType();
	public void setClaimType(String claimType);

	public String getGrowerAddressLine1();
	public void setGrowerAddressLine1(String growerAddressLine1) ;

	public String getGrowerAddressLine2();
	public void setGrowerAddressLine2(String growerAddressLine2);

	public String getGrowerPostalCode() ;
	public void setGrowerPostalCode(String growerPostalCode);

	public String getGrowerCity();
	public void setGrowerCity(String growerCity);

	public String getGrowerProvince();
	public void setGrowerProvince(String growerProvince);

	public Integer getCropYear();
	public void setCropYear(Integer cropYear);

	public Double getInsurableValueSelected() ;
	public void setInsurableValueSelected(Double insurableValueSelected);

	public Double getInsurableValueHundredPercent();
	public void setInsurableValueHundredPercent(Double insurableValueHundredPercent);
	
	public Double getCoverageAmount();
	public void setCoverageAmount(Double coverageAmount);

	public String getPrimaryPerilCode();
	public void setPrimaryPerilCode(String primaryPerilCode);

	public String getSecondaryPerilCode();
	public void setSecondaryPerilCode(String secondaryPerilCode);

	public String getSubmittedByUserid();
	public void setSubmittedByUserid(String submittedByUserid);
	
	public String getSubmittedByName();
	public void setSubmittedByName(String submittedByName);
	
	public Date getSubmittedByDate();
	public void setSubmittedByDate(Date submittedByDate);
	
	public String getRecommendedByUserid();
	public void setRecommendedByUserid(String recommendedByUserid);
	
	public String getRecommendedByName();
	public void setRecommendedByName(String recommendedByName);
	
	public Date getRecommendedByDate();
	public void setRecommendedByDate(Date recommendedByDate);
	
	public String getApprovedByUserid();
	public void setApprovedByUserid(String approvedByUserid);
	
	public String getApprovedByName();
	public void setApprovedByName(String approvedByName);
	
	public Date getApprovedByDate();
	public void setApprovedByDate(Date approvedByDate);

	public String getCalculateIivInd();
	public void setCalculateIivInd(String calculateIivInd);
	
	List<ClaimCalculationVariety> getVarieties();
	void setVarieties(List<ClaimCalculationVariety> value);
	
	public String getClaimCalculationGuid();
	public void setClaimCalculationGuid(String claimCalculationGuid);

	public String getCalculationVersion();
	public void setCalculationVersion(String calculationVersion);

	public String getCalculationStatusCode();
	public void setCalculationStatusCode(String calculationStatusCode);

	public String getCalculationStatus();
	public void setCalculationStatus(String calculationStatus);

}
