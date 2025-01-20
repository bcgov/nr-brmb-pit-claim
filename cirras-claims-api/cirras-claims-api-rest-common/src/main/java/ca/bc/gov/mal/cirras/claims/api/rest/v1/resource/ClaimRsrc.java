package ca.bc.gov.mal.cirras.claims.api.rest.v1.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.claims.model.v1.Claim;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationVariety;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.CLAIM_NAME)
@XmlSeeAlso({ ClaimRsrc.class })
//@JsonSubTypes({ @Type(value = ResourceContactRsrc.class, name = ResourceTypes.RESOURCE_CONTACT) })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class ClaimRsrc extends BaseResource implements Claim {

	private static final long serialVersionUID = 1L;

	// grower
	private Integer growerNumber;
	private String growerName;
	private String growerAddressLine1;
	private String growerAddressLine2;
	private String growerPostalCode;
	private String growerCity;
	private String growerProvince;
	
	// policy
	private Integer cropYear;
	private String policyNumber;
	private Integer insurancePlanId;
	private String insurancePlanName;
	private Integer contractId;

	
	// coverage
	private String commodityCoverageCode;
	private String coverageName;
	private Double insurableValueSelected;
	private Double insurableValueHundredPercent;
	private Double coverageAmount;

	// claim
	private Integer claimNumber;
	private String primaryPerilCode;
	private String secondaryPerilCode;
	private Integer cropCommodityId;
	private String commodityName;
	private String claimStatusCode;
	private String claimStatus;
	private String claimType;
	private String submittedByUserid;
	private String submittedByName;
	private Date submittedByDate;
	private String recommendedByUserid;
	private String recommendedByName;
	private Date recommendedByDate;
	private String approvedByUserid;
	private String approvedByName;
	private Date approvedByDate;
	private String calculateIivInd;

	
	// calculation
	private String claimCalculationGuid;
	private String calculationVersion;
	private String calculationStatusCode;
	private String calculationStatus;
	
	

	private List<ClaimCalculationVariety> varieties = new ArrayList<ClaimCalculationVariety>();
	

	public Integer getClaimNumber() {
		return claimNumber;
	}
	
	public void setClaimNumber(Integer claimNumber) {
		this.claimNumber = claimNumber;
	}

	public Integer getGrowerNumber() {
		return growerNumber;
	}

	public void setGrowerNumber(Integer growerNumber) {
		this.growerNumber = growerNumber;
	}

	public String getGrowerName() {
		return growerName;
	}

	public void setGrowerName(String growerName) {
		this.growerName = growerName;
	}

	public String getGrowerAddressLine1() {
		return growerAddressLine1;
	}

	public void setGrowerAddressLine1(String growerAddressLine1) {
		this.growerAddressLine1 = growerAddressLine1;
	}

	public String getGrowerAddressLine2() {
		return growerAddressLine2;
	}

	public void setGrowerAddressLine2(String growerAddressLine2) {
		this.growerAddressLine2 = growerAddressLine2;
	}

	public String getGrowerPostalCode() {
		return growerPostalCode;
	}

	public void setGrowerPostalCode(String growerPostalCode) {
		this.growerPostalCode = growerPostalCode;
	}

	public String getGrowerCity() {
		return growerCity;
	}
	
	public void setGrowerCity(String growerCity) {
		this.growerCity = growerCity;
	}

	public String getGrowerProvince() {
		return growerProvince;
	}
	
	public void setGrowerProvince(String growerProvince) {
		this.growerProvince = growerProvince;
	}
	
	public Integer getCropYear() {
		return cropYear;
	}

	public void setCropYear(Integer cropYear) {
		this.cropYear = cropYear;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public Double getInsurableValueSelected() {
		return insurableValueSelected;
	}

	public void setInsurableValueSelected(Double insurableValueSelected) {
		this.insurableValueSelected = insurableValueSelected;
	}

	public Double getInsurableValueHundredPercent() {
		return insurableValueHundredPercent;
	}
	
	public void setInsurableValueHundredPercent(Double insurableValueHundredPercent) {
		this.insurableValueHundredPercent = insurableValueHundredPercent;
	}

	public Double getCoverageAmount() {
		return coverageAmount;
	}

	public void setCoverageAmount(Double coverageAmount) {
		this.coverageAmount = coverageAmount;
	}

	public String getPrimaryPerilCode() {
		return primaryPerilCode;
	}

	public void setPrimaryPerilCode(String primaryPerilCode) {
		this.primaryPerilCode = primaryPerilCode;
	}

	public String getSecondaryPerilCode() {
		return secondaryPerilCode;
	}

	public void setSecondaryPerilCode(String secondaryPerilCode) {
		this.secondaryPerilCode = secondaryPerilCode;
	}

	public Integer getInsurancePlanId() {
		return insurancePlanId;
	}

	public void setInsurancePlanId(Integer insurancePlanId) {
		this.insurancePlanId = insurancePlanId;
	}

	public List<ClaimCalculationVariety> getVarieties() {
		return varieties;
	}

	public void setVarieties(List<ClaimCalculationVariety> varieties) {
		this.varieties = varieties;
	}

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	public String getCoverageName() {
		return coverageName;
	}

	public void setCoverageName(String coverageName) {
		this.coverageName = coverageName;
	}	

	public String getInsurancePlanName() {
		return insurancePlanName;
	}

	public void setInsurancePlanName(String insurancePlanName) {
		this.insurancePlanName = insurancePlanName;
	}

	public Integer getContractId() {
		return contractId;
	}
	
	public void setContractId(Integer contractId) {
		this.contractId = contractId;
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
	
	public String getClaimStatusCode() {
		return claimStatusCode;
	}

	public void setClaimStatusCode(String claimStatusCode) {
		this.claimStatusCode = claimStatusCode;
	}
	
	public String getClaimStatus() {
		return claimStatus;
	}

	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}
	
	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
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
	
	public String getClaimCalculationGuid() {
		return claimCalculationGuid;
	}

	public void setClaimCalculationGuid(String claimCalculationGuid) {
		this.claimCalculationGuid = claimCalculationGuid;
	}

	public String getCalculationVersion() {
		return calculationVersion;
	}

	public void setCalculationVersion(String calculationVersion) {
		this.calculationVersion = calculationVersion;
	}

	
	public String getCalculationStatusCode() {
		return calculationStatusCode;
	}

	public void setCalculationStatusCode(String calculationStatusCode) {
		this.calculationStatusCode = calculationStatusCode;
	}

	
	public String getCalculationStatus() {
		return calculationStatus;
	}

	public void setCalculationStatus(String calculationStatus) {
		this.calculationStatus = calculationStatus;
	}
	
	public String getCalculateIivInd() {
		return calculateIivInd;
	}
	
	public void setCalculateIivInd(String calculateIivInd) {
		this.calculateIivInd = calculateIivInd;
	}

}
