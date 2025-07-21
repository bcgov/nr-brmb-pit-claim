package ca.bc.gov.mal.cirras.claims.api.rest.v1.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculation;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationBerries;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationGrainBasket;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationGrainBasketProduct;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationGrainQuantity;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationGrainQuantityDetail;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationGrainSpotLoss;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationGrainUnseeded;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationGrapes;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationPlantAcres;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationPlantUnits;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationVariety;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.CLAIM_CALCULATION_NAME)
@XmlSeeAlso({ ClaimCalculationRsrc.class })
//@JsonSubTypes({ @Type(value = ResourceContactRsrc.class, name = ResourceTypes.RESOURCE_CONTACT) })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class ClaimCalculationRsrc extends BaseResource implements ClaimCalculation {

	private static final long serialVersionUID = 1L;

	private String claimCalculationGuid;

	// calculation
	private String claimCalculationGrainQuantityGuid;
	private Integer calculationVersion;
	private Double totalClaimAmount;
	private String calculationStatusCode;
	private String calculationComment;
	private Integer revisionCount;
	private String createClaimCalcUserGuid;
	private String createClaimCalcUserName;
	private String createUser;
	private Date createDate;
	private String updateClaimCalcUserGuid;
	private String updateClaimCalcUserName;
	private String updateUser;
	private Date updateDate;


	// claim
	private Integer claimNumber;
	private String commodityCoverageCode;
	private String coverageName;
	private Integer cropCommodityId;
	private String commodityName;
	private Boolean isPedigreeInd;
	private String primaryPerilCode;
	private String secondaryPerilCode;
	private String claimStatusCode;
	private String claimCurrentStatusCode;
	private Boolean hasChequeReqInd;
	private Boolean currentHasChequeReqInd;
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
	

	// grower
	private Integer growerNumber;
	private String growerName;
	private String growerAddressLine1;
	private String growerAddressLine2;
	private String growerPostalCode;
	private String growerCity;
	private String growerProvince;
	
	// policy
	private Integer insurancePlanId;
	private String insurancePlanName;
	private Integer cropYear;
	private String policyNumber;
	private Integer contractId;
	
	// coverage
	private String insuredByMeasurementType;

	// Linked Product and Claim
	private Integer linkedProductId;
	private Integer linkedClaimNumber;
	private String linkedClaimCalculationGuid;
	private String linkedLatestClaimCalculationGuid;
	private Integer latestLinkedCalculationVersion;
	
	
	// Out of sync flags
	private Boolean isOutOfSync;
	private Boolean isOutOfSyncGrowerNumber;
	private Boolean isOutOfSyncGrowerName;
	private Boolean isOutOfSyncGrowerAddrLine1;
	private Boolean isOutOfSyncGrowerAddrLine2;
	private Boolean isOutOfSyncGrowerPostalCode;
	private Boolean isOutOfSyncGrowerCity;
	private Boolean isOutOfSyncGrowerProvince;
	private Boolean isOutOfSyncCoverageAmount;
	private Boolean isOutOfSyncVarietyAdded;
	
	//Sub table models specific values
	private ClaimCalculationBerries claimCalculationBerries;
	private ClaimCalculationPlantUnits claimCalculationPlantUnits;
	private ClaimCalculationPlantAcres claimCalculationPlantAcres;
	private ClaimCalculationGrapes claimCalculationGrapes;
	private ClaimCalculationGrainUnseeded claimCalculationGrainUnseeded;
	private ClaimCalculationGrainSpotLoss claimCalculationGrainSpotLoss;
	private ClaimCalculationGrainQuantity claimCalculationGrainQuantity;
	private ClaimCalculationGrainQuantityDetail claimCalculationGrainQuantityDetail;
	private ClaimCalculationGrainBasket claimCalculationGrainBasket;
	
	private List<ClaimCalculationVariety> varieties = new ArrayList<ClaimCalculationVariety>();	
	private List<ClaimCalculationGrainBasketProduct> claimCalculationGrainBasketProducts = new ArrayList<ClaimCalculationGrainBasketProduct>();	
	

	public String getClaimCalculationGuid() {
		return claimCalculationGuid;
	}

	public void setClaimCalculationGuid(String claimCalculationGuid) {
		this.claimCalculationGuid = claimCalculationGuid;
	}

	public String getClaimCalculationGrainQuantityGuid() {
		return claimCalculationGrainQuantityGuid;
	}

	public void setClaimCalculationGrainQuantityGuid(String claimCalculationGrainQuantityGuid) {
		this.claimCalculationGrainQuantityGuid = claimCalculationGrainQuantityGuid;
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

	public Integer getInsurancePlanId() {
		return insurancePlanId;
	}

	public void setInsurancePlanId(Integer insurancePlanId) {
		this.insurancePlanId = insurancePlanId;
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

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
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

	public String getInsuredByMeasurementType() {
		return insuredByMeasurementType;
	}

	public void setInsuredByMeasurementType(String insuredByMeasurementType) {
		this.insuredByMeasurementType = insuredByMeasurementType;
	}

	public Integer getClaimNumber() {
		return claimNumber;
	}

	public void setClaimNumber(Integer claimNumber) {
		this.claimNumber = claimNumber;
	}

	public Integer getCalculationVersion() {
		return calculationVersion;
	}

	public void setCalculationVersion(Integer calculationVersion) {
		this.calculationVersion = calculationVersion;
	}

	public Integer getRevisionCount() {
		return revisionCount;
	}

	public void setRevisionCount(Integer revisionCount) {
		this.revisionCount = revisionCount;
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
	
	public String getCurrentClaimStatusCode() {
		return claimCurrentStatusCode;
	}

	public void setCurrentClaimStatusCode(String claimCurrentStatusCode) {
		this.claimCurrentStatusCode = claimCurrentStatusCode;
	}
	
	public String getClaimStatusCode() {
		return claimStatusCode;
	}

	public void setClaimStatusCode(String claimStatusCode) {
		this.claimStatusCode = claimStatusCode;
	}

	public Boolean getHasChequeReqInd() {
		return hasChequeReqInd;
	}
	
	public void setHasChequeReqInd(Boolean hasChequeReqInd) {
		this.hasChequeReqInd = hasChequeReqInd;
	}

	public Boolean getCurrentHasChequeReqInd() {
		return currentHasChequeReqInd;
	}
	
	public void setCurrentHasChequeReqInd(Boolean currentHasChequeReqInd) {
		this.currentHasChequeReqInd = currentHasChequeReqInd;
	}
	
	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	
	public String getCalculationStatusCode() {
		return calculationStatusCode;
	}

	public void setCalculationStatusCode(String calculationStatusCode) {
		this.calculationStatusCode = calculationStatusCode;
	}

	public String getCalculationComment() {
		return calculationComment;
	}

	public void setCalculationComment(String calculationComment) {
		this.calculationComment = calculationComment;
	}
	
	public Double getTotalClaimAmount() {
		return totalClaimAmount;
	}

	public void setTotalClaimAmount(Double totalClaimAmount) {
		this.totalClaimAmount = totalClaimAmount;
	}

	public List<ClaimCalculationVariety> getVarieties() {
		return varieties;
	}

	public void setVarieties(List<ClaimCalculationVariety> varieties) {
		this.varieties = varieties;
	}

	public ClaimCalculationBerries getClaimCalculationBerries() {
		return claimCalculationBerries;
	}

	public void setClaimCalculationBerries(ClaimCalculationBerries claimCalculationBerries) {
		this.claimCalculationBerries = claimCalculationBerries;
	}

	public ClaimCalculationPlantUnits getClaimCalculationPlantUnits() {
		return claimCalculationPlantUnits;
	}

	public void setClaimCalculationPlantUnits(ClaimCalculationPlantUnits claimCalculationPlantUnits) {
		this.claimCalculationPlantUnits = claimCalculationPlantUnits;
	}

	public ClaimCalculationPlantAcres getClaimCalculationPlantAcres() {
		return claimCalculationPlantAcres;
	}

	public void setClaimCalculationPlantAcres(ClaimCalculationPlantAcres claimCalculationPlantAcres) {
		this.claimCalculationPlantAcres = claimCalculationPlantAcres;
	}

	public ClaimCalculationGrapes getClaimCalculationGrapes() {
		return claimCalculationGrapes;
	}

	public void setClaimCalculationGrapes(ClaimCalculationGrapes claimCalculationGrapes) {
		this.claimCalculationGrapes = claimCalculationGrapes;
	}

	public ClaimCalculationGrainUnseeded getClaimCalculationGrainUnseeded() {
		return claimCalculationGrainUnseeded;
	}

	public void setClaimCalculationGrainUnseeded(ClaimCalculationGrainUnseeded claimCalculationGrainUnseeded) {
		this.claimCalculationGrainUnseeded = claimCalculationGrainUnseeded;
	}

	public ClaimCalculationGrainSpotLoss getClaimCalculationGrainSpotLoss() {
		return claimCalculationGrainSpotLoss;
	}

	public void setClaimCalculationGrainSpotLoss(ClaimCalculationGrainSpotLoss claimCalculationGrainSpotLoss) {
		this.claimCalculationGrainSpotLoss = claimCalculationGrainSpotLoss;
	}

	public ClaimCalculationGrainQuantity getClaimCalculationGrainQuantity() {
		return claimCalculationGrainQuantity;
	}

	public void setClaimCalculationGrainQuantity(ClaimCalculationGrainQuantity claimCalculationGrainQuantity) {
		this.claimCalculationGrainQuantity = claimCalculationGrainQuantity;
	}

	public ClaimCalculationGrainQuantityDetail getClaimCalculationGrainQuantityDetail() {
		return claimCalculationGrainQuantityDetail;
	}

	public void setClaimCalculationGrainQuantityDetail(ClaimCalculationGrainQuantityDetail claimCalculationGrainQuantityDetail) {
		this.claimCalculationGrainQuantityDetail = claimCalculationGrainQuantityDetail;
	}

	public ClaimCalculationGrainBasket getClaimCalculationGrainBasket() {
		return claimCalculationGrainBasket;
	}

	public void setClaimCalculationGrainBasket(ClaimCalculationGrainBasket claimCalculationGrainBasket) {
		this.claimCalculationGrainBasket = claimCalculationGrainBasket;
	}

	public List<ClaimCalculationGrainBasketProduct> getClaimCalculationGrainBasketProducts() {
		return claimCalculationGrainBasketProducts;
	}

	public void setClaimCalculationGrainBasketProducts(List<ClaimCalculationGrainBasketProduct> claimCalculationGrainBasketProducts) {
		this.claimCalculationGrainBasketProducts = claimCalculationGrainBasketProducts;
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

	public String getCreateClaimCalcUserGuid() {
		return this.createClaimCalcUserGuid;
	}

	public void setCreateClaimCalcUserGuid(String createClaimCalcUserGuid) {
		this.createClaimCalcUserGuid = createClaimCalcUserGuid;
	}

	public String getCreateClaimCalcUserName() {
		return this.createClaimCalcUserName;
	}

	public void setCreateClaimCalcUserName(String createClaimCalcUserName) {
		this.createClaimCalcUserName = createClaimCalcUserName;
	}
		
	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}	

	public String getUpdateClaimCalcUserGuid() {
		return this.updateClaimCalcUserGuid;
	}

	public void setUpdateClaimCalcUserGuid(String updateClaimCalcUserGuid) {
		this.updateClaimCalcUserGuid = updateClaimCalcUserGuid;
	}

	public String getUpdateClaimCalcUserName() {
		return this.updateClaimCalcUserName;
	}

	public void setUpdateClaimCalcUserName(String updateClaimCalcUserName) {
		this.updateClaimCalcUserName = updateClaimCalcUserName;
	}
	
	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}	

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	public Boolean getIsPedigreeInd() {
		return isPedigreeInd;
	}

	public void setIsPedigreeInd(Boolean isPedigreeInd) {
		this.isPedigreeInd = isPedigreeInd;
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
	
	public String getCalculateIivInd() {
		return calculateIivInd;
	}
	
	public void setCalculateIivInd(String calculateIivInd) {
		this.calculateIivInd = calculateIivInd;
	}
	
	public Integer getLinkedProductId() {
		return linkedProductId;
	}

	public void setLinkedProductId(Integer linkedProductId) {
		this.linkedProductId = linkedProductId;
	}

	public Integer getLinkedClaimNumber() {
		return linkedClaimNumber;
	}

	public void setLinkedClaimNumber(Integer linkedClaimNumber) {
		this.linkedClaimNumber = linkedClaimNumber;
	}

	public String getLinkedClaimCalculationGuid() {
		return linkedClaimCalculationGuid;
	}

	public void setLinkedClaimCalculationGuid(String linkedClaimCalculationGuid) {
		this.linkedClaimCalculationGuid = linkedClaimCalculationGuid;
	}

	public String getLatestLinkedClaimCalculationGuid() {
		return linkedLatestClaimCalculationGuid;
	}

	public void setLatestLinkedClaimCalculationGuid(String linkedLatestClaimCalculationGuid) {
		this.linkedLatestClaimCalculationGuid = linkedLatestClaimCalculationGuid;
	}

	public Integer getLatestLinkedCalculationVersion() {
		return latestLinkedCalculationVersion;
	}

	public void setLatestLinkedCalculationVersion(Integer latestLinkedCalculationVersion) {
		this.latestLinkedCalculationVersion = latestLinkedCalculationVersion;
	}
	
	public Boolean getIsOutOfSync() {
		return isOutOfSync;
	}
	
	public void setIsOutOfSync(Boolean isOutOfSync) {
		this.isOutOfSync = isOutOfSync;
	}

	public Boolean getIsOutOfSyncGrowerNumber() {
		return isOutOfSyncGrowerNumber;
	}
	
	public void setIsOutOfSyncGrowerNumber(Boolean isOutOfSyncGrowerNumber) {
		this.isOutOfSyncGrowerNumber = isOutOfSyncGrowerNumber;
	}

	public Boolean getIsOutOfSyncGrowerName() {
		return isOutOfSyncGrowerName;
	}
	
	public void setIsOutOfSyncGrowerName(Boolean isOutOfSyncGrowerName) {
		this.isOutOfSyncGrowerName = isOutOfSyncGrowerName;
	}
	
	public Boolean getIsOutOfSyncGrowerAddrLine1() {
		return isOutOfSyncGrowerAddrLine1;
	}
	
	public void setIsOutOfSyncGrowerAddrLine1(Boolean isOutOfSyncGrowerAddrLine1) {
		this.isOutOfSyncGrowerAddrLine1 = isOutOfSyncGrowerAddrLine1;
	}

	public Boolean getIsOutOfSyncGrowerAddrLine2() {
		return isOutOfSyncGrowerAddrLine2;
	}
	
	public void setIsOutOfSyncGrowerAddrLine2(Boolean isOutOfSyncGrowerAddrLine2) {
		this.isOutOfSyncGrowerAddrLine2 = isOutOfSyncGrowerAddrLine2;
	}

	public Boolean getIsOutOfSyncGrowerPostalCode() {
		return isOutOfSyncGrowerPostalCode;
	}
	
	public void setIsOutOfSyncGrowerPostalCode(Boolean isOutOfSyncGrowerPostalCode) {
		this.isOutOfSyncGrowerPostalCode = isOutOfSyncGrowerPostalCode;
	}

	public Boolean getIsOutOfSyncGrowerCity() {
		return isOutOfSyncGrowerCity;
	}
	
	public void setIsOutOfSyncGrowerCity(Boolean isOutOfSyncGrowerCity) {
		this.isOutOfSyncGrowerCity = isOutOfSyncGrowerCity;
	}

	public Boolean getIsOutOfSyncGrowerProvince() {
		return isOutOfSyncGrowerProvince;
	}
	
	public void setIsOutOfSyncGrowerProvince(Boolean isOutOfSyncGrowerProvince) {
		this.isOutOfSyncGrowerProvince = isOutOfSyncGrowerProvince;
	}

	public Boolean getIsOutOfSyncVarietyAdded() {
		return isOutOfSyncVarietyAdded;
	}
	
	public void setIsOutOfSyncVarietyAdded(Boolean isOutOfSyncVarietyAdded) {
		this.isOutOfSyncVarietyAdded = isOutOfSyncVarietyAdded;
	}

}
