package ca.bc.gov.mal.cirras.claims.model.v1;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public interface ClaimCalculation extends Serializable {
	
	public String getClaimCalculationGuid();
	public void setClaimCalculationGuid(String claimCalculationGuid);

	public Integer getGrowerNumber();
	public void setGrowerNumber(Integer growerNumber);

	public String getGrowerName();
	public void setGrowerName(String growerName) ;

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

	public Integer getInsurancePlanId();
	public void setInsurancePlanId(Integer insurancePlanId);

	public Integer getCropYear();
	public void setCropYear(Integer cropYear);
	
	public String getInsuredByMeasurementType();
	public void setInsuredByMeasurementType(String insuredByMeasurementType);

	public String getPolicyNumber();
	public void setPolicyNumber(String policyNumber);

	public Integer getContractId();
	public void setContractId(Integer contractId);

	public String getCommodityCoverageCode();
	public void setCommodityCoverageCode(String commodityCoverageCode);

	public Integer getCropCommodityId();
	public void setCropCommodityId(Integer cropCommodityId);

	public Integer getClaimNumber();
	public void setClaimNumber(Integer claimNumber) ;

	public Integer getCalculationVersion();
	public void setCalculationVersion(Integer calculationVersion);

	public Integer getRevisionCount();
	public void setRevisionCount(Integer revisionCount);

	public String getPrimaryPerilCode();
	public void setPrimaryPerilCode(String primaryPerilCode);

	public String getSecondaryPerilCode();
	public void setSecondaryPerilCode(String secondaryPerilCode);

	public String getCurrentClaimStatusCode();
	public void setCurrentClaimStatusCode(String claimCurrentStatusCode);

	public String getClaimStatusCode();
	public void setClaimStatusCode(String claimStatusCode);

	public Boolean getCurrentHasChequeReqInd();
	public void setCurrentHasChequeReqInd(Boolean currentHasChequeReqInd);

	public Boolean getHasChequeReqInd();
	public void setHasChequeReqInd(Boolean hasChequeReqInd);
	
	public String getClaimType();
	public void setClaimType(String claimType);

	public String getCalculationStatusCode();
	public void setCalculationStatusCode(String calculationStatusCode);

	public String getCalculationComment();
	public void setCalculationComment(String calculationComment);
	
	public Double getTotalClaimAmount();
	public void setTotalClaimAmount(Double totalClaimAmount);

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

	public String getCalculateIivInd();
	public void setCalculateIivInd(String calculateIivInd);
	
	public String getCreateClaimCalcUserGuid();
	public void setCreateClaimCalcUserGuid(String createClaimCalcUserGuid);

	public String getCreateClaimCalcUserName();
	public void setCreateClaimCalcUserName(String createClaimCalcUserName);
	
	public String getCreateUser();
	public void setCreateUser(String createUser);

	public Date getCreateDate();
	public void setCreateDate(Date createDate);

	public String getUpdateClaimCalcUserGuid();
	public void setUpdateClaimCalcUserGuid(String updateClaimCalcUserGuid);

	public String getUpdateClaimCalcUserName();
	public void setUpdateClaimCalcUserName(String updateClaimCalcUserName);
	
	public String getUpdateUser();
	public void setUpdateUser(String updateUser);

	public Date getUpdateDate();
	public void setUpdateDate(Date updateDate);

	public String getCommodityName();
	public void setCommodityName(String commodityName);

	public String getCoverageName();
	public void setCoverageName(String coverageName);

	public String getInsurancePlanName();
	public void setInsurancePlanName(String insurancePlanName);

	public Boolean getIsOutOfSync();	
	public void setIsOutOfSync(Boolean isOutOfSync);

	public Boolean getIsOutOfSyncGrowerNumber();	
	public void setIsOutOfSyncGrowerNumber(Boolean isOutOfSyncGrowerNumber);

	public Boolean getIsOutOfSyncGrowerName();	
	public void setIsOutOfSyncGrowerName(Boolean isOutOfSyncGrowerName);
	
	public Boolean getIsOutOfSyncGrowerAddrLine1();	
	public void setIsOutOfSyncGrowerAddrLine1(Boolean isOutOfSyncGrowerAddrLine1);

	public Boolean getIsOutOfSyncGrowerAddrLine2();	
	public void setIsOutOfSyncGrowerAddrLine2(Boolean isOutOfSyncGrowerAddrLine2);

	public Boolean getIsOutOfSyncGrowerPostalCode();	
	public void setIsOutOfSyncGrowerPostalCode(Boolean isOutOfSyncGrowerPostalCode);

	public Boolean getIsOutOfSyncGrowerCity();	
	public void setIsOutOfSyncGrowerCity(Boolean isOutOfSyncGrowerCity);

	public Boolean getIsOutOfSyncGrowerProvince();	
	public void setIsOutOfSyncGrowerProvince(Boolean isOutOfSyncGrowerProvince);

	public Boolean getIsOutOfSyncVarietyAdded();	
	public void setIsOutOfSyncVarietyAdded(Boolean isOutOfSyncVarietyAdded);
	
	List<ClaimCalculationVariety> getVarieties();
	void setVarieties(List<ClaimCalculationVariety> value);
	
	public ClaimCalculationBerries getClaimCalculationBerries();
	public void setClaimCalculationBerries(ClaimCalculationBerries berries);

	public ClaimCalculationPlantUnits getClaimCalculationPlantUnits();
	public void setClaimCalculationPlantUnits(ClaimCalculationPlantUnits claimCalculationPlantUnits);

	public ClaimCalculationPlantAcres getClaimCalculationPlantAcres();
	public void setClaimCalculationPlantAcres(ClaimCalculationPlantAcres claimCalculationPlantAcres);

	public ClaimCalculationGrapes getClaimCalculationGrapes();
	public void setClaimCalculationGrapes(ClaimCalculationGrapes claimCalculationGrapes);
}
