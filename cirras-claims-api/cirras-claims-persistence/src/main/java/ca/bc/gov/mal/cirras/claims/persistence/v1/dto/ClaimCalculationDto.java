package ca.bc.gov.mal.cirras.claims.persistence.v1.dto;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;



public class ClaimCalculationDto extends BaseDto<ClaimCalculationDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ClaimCalculationDto.class);
	
	private String claimCalculationGuid;

	// calculation
	private Integer calculationVersion;
	private Double totalClaimAmount;
	private String calculationStatusCode;
	private String calculationStatus;
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
	private String primaryPerilCode;
	private String secondaryPerilCode;
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
	private String calculateIivInd;
	private Boolean hasChequeReqInd;
	

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
	
	private ClaimCalculationBerriesDto berries;
	private ClaimCalculationPlantUnitsDto claimCalculationPlantUnitsDto; 
	private ClaimCalculationPlantAcresDto claimCalculationPlantAcresDto; 
	private ClaimCalculationGrapesDto claimCalculationGrapesDto;
	private List<ClaimCalculationVarietyDto> varieties = new ArrayList<ClaimCalculationVarietyDto>();
	private ClaimCalculationGrainUnseededDto claimCalculationGrainUnseededDto;
	
	public ClaimCalculationDto() {
	}
	
	
	public ClaimCalculationDto(ClaimCalculationDto dto) {
		this.growerNumber = dto.growerNumber;
		this.growerName = dto.growerName;
		this.growerAddressLine1 = dto.growerAddressLine1;
		this.growerAddressLine2 = dto.growerAddressLine2;
		this.growerPostalCode = dto.growerPostalCode;
		this.growerCity = dto.growerCity;
		this.growerProvince = dto.growerProvince;
		this.insurancePlanId = dto.insurancePlanId;
		this.insurancePlanName = dto.insurancePlanName;
		this.cropYear = dto.cropYear;
		this.policyNumber = dto.policyNumber;
		this.contractId = dto.contractId;
		this.commodityCoverageCode = dto.commodityCoverageCode;
		this.coverageName = dto.coverageName;
		this.cropCommodityId = dto.cropCommodityId;
		this.commodityName = dto.commodityName;
		this.insuredByMeasurementType = dto.insuredByMeasurementType;
		this.claimNumber = dto.claimNumber;
		this.calculationVersion = dto.calculationVersion;
		this.revisionCount = dto.revisionCount;
		this.primaryPerilCode = dto.primaryPerilCode;
		this.secondaryPerilCode = dto.secondaryPerilCode;
		this.claimStatusCode = dto.claimStatusCode;
		this.calculationStatusCode = dto.calculationStatusCode;
		this.calculationStatus = dto.calculationStatus;
		this.calculationComment = dto.calculationComment;
		this.totalClaimAmount = dto.totalClaimAmount;
		this.submittedByUserid = dto.submittedByUserid;
		this.submittedByName = dto.submittedByName;
		this.submittedByDate = dto.submittedByDate;
		this.recommendedByUserid = dto.recommendedByUserid;
		this.recommendedByName = dto.recommendedByName;
		this.recommendedByDate = dto.recommendedByDate;
		this.approvedByUserid = dto.approvedByUserid;
		this.approvedByName = dto.approvedByName;
		this.approvedByDate = dto.approvedByDate;
		this.calculateIivInd = dto.calculateIivInd;
		this.hasChequeReqInd = dto.hasChequeReqInd;
		this.createClaimCalcUserGuid = dto.createClaimCalcUserGuid;
		this.createClaimCalcUserName = dto.createClaimCalcUserName;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateClaimCalcUserGuid = dto.updateClaimCalcUserGuid;
		this.updateClaimCalcUserName = dto.updateClaimCalcUserName;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;
		this.berries = dto.berries;
		this.claimCalculationPlantUnitsDto = dto.claimCalculationPlantUnitsDto;
		this.claimCalculationPlantAcresDto = dto.claimCalculationPlantAcresDto;
		this.claimCalculationGrainUnseededDto = dto.claimCalculationGrainUnseededDto;
		
		this.varieties = new ArrayList<ClaimCalculationVarietyDto>();
		
		if (dto.varieties != null) {
			for(ClaimCalculationVarietyDto vDto: dto.varieties) {
				this.varieties.add(vDto.copy());
			}
		}
	}
	

	@Override
	public boolean equalsBK(ClaimCalculationDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(ClaimCalculationDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("claimCalculationGuid", claimCalculationGuid, other.claimCalculationGuid);
			result = result&&dtoUtils.equals("growerNumber", growerNumber, other.growerNumber);
			result = result&&dtoUtils.equals("growerName", growerName, other.growerName);
			result = result&&dtoUtils.equals("growerAddressLine1", growerAddressLine1, other.growerAddressLine1);
			result = result&&dtoUtils.equals("growerAddressLine2", growerAddressLine2, other.growerAddressLine2);
			result = result&&dtoUtils.equals("growerPostalCode", growerPostalCode, other.growerPostalCode);
			result = result&&dtoUtils.equals("growerCity", growerCity, other.growerCity);
			result = result&&dtoUtils.equals("growerProvince", growerProvince, other.growerProvince);
			result = result&&dtoUtils.equals("insurancePlanId", insurancePlanId, other.insurancePlanId);
			result = result&&dtoUtils.equals("insurancePlanName", insurancePlanName, other.insurancePlanName);
			result = result&&dtoUtils.equals("cropYear", cropYear, other.cropYear);
			result = result&&dtoUtils.equals("policyNumber", policyNumber, other.policyNumber);
			result = result&&dtoUtils.equals("contractId", contractId, other.contractId);
			result = result&&dtoUtils.equals("commodityCoverageCode", commodityCoverageCode, other.commodityCoverageCode);
			result = result&&dtoUtils.equals("coverageName", coverageName, other.coverageName);
			result = result&&dtoUtils.equals("cropCommodityId", cropCommodityId, other.cropCommodityId);
			result = result&&dtoUtils.equals("commodityName", commodityName, other.commodityName);
			result = result&&dtoUtils.equals("insuredByMeasurementType", insuredByMeasurementType, other.insuredByMeasurementType);
			result = result&&dtoUtils.equals("claimNumber", claimNumber, other.claimNumber);
			result = result&&dtoUtils.equals("calculationVersion", calculationVersion, other.calculationVersion);
			result = result&&dtoUtils.equals("revisionCount", revisionCount, other.revisionCount);
			result = result&&dtoUtils.equals("primaryPerilCode", primaryPerilCode, other.primaryPerilCode);
			result = result&&dtoUtils.equals("secondaryPerilCode", secondaryPerilCode, other.secondaryPerilCode);
			result = result&&dtoUtils.equals("claimStatusCode", claimStatusCode, other.claimStatusCode);
			result = result&&dtoUtils.equals("hasChequeReqInd", hasChequeReqInd, other.hasChequeReqInd);
			result = result&&dtoUtils.equals("calculationStatusCode", calculationStatusCode, other.calculationStatusCode);
			result = result&&dtoUtils.equals("calculationComment", calculationComment, other.calculationComment);
			result = result&&dtoUtils.equals("totalClaimAmount", totalClaimAmount, other.totalClaimAmount, 2);
			result = result&&dtoUtils.equals("createClaimCalcUserGuid", createClaimCalcUserGuid, other.createClaimCalcUserGuid);
			result = result&&dtoUtils.equals("createClaimCalcUserName", createClaimCalcUserName, other.createClaimCalcUserName);
			result = result&&dtoUtils.equals("updateClaimCalcUserGuid", updateClaimCalcUserGuid, other.updateClaimCalcUserGuid);
			result = result&&dtoUtils.equals("updateClaimCalcUserName", updateClaimCalcUserName, other.updateClaimCalcUserName);

		
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public ClaimCalculationDto copy() {
		return new ClaimCalculationDto(this);
	}

	public String getClaimCalculationGuid() {
		return claimCalculationGuid;
	}

	public void setClaimCalculationGuid(String claimCalculationGuid) {
		this.claimCalculationGuid = claimCalculationGuid;
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

	public String getClaimStatusCode() {
		return claimStatusCode;
	}

	public void setClaimStatusCode(String claimStatusCode) {
		this.claimStatusCode = claimStatusCode;
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

	public List<ClaimCalculationVarietyDto> getVarieties() {
		return varieties;
	}

	public void setVarieties(List<ClaimCalculationVarietyDto> varieties) {
		this.varieties = varieties;
	}

	public ClaimCalculationBerriesDto getClaimCalculationBerries() {
		return berries;
	}

	public void setClaimCalculationBerries(ClaimCalculationBerriesDto berries) {
		this.berries = berries;
	}
	
	public ClaimCalculationPlantUnitsDto getClaimCalculationPlantUnits() {
		return claimCalculationPlantUnitsDto;
	}

	public void setClaimCalculationPlantUnits(ClaimCalculationPlantUnitsDto claimCalculationPlantUnitsDto) {
		this.claimCalculationPlantUnitsDto = claimCalculationPlantUnitsDto;
	}
	
	public ClaimCalculationPlantAcresDto getClaimCalculationPlantAcres() {
		return claimCalculationPlantAcresDto;
	}

	public void setClaimCalculationPlantAcres(ClaimCalculationPlantAcresDto claimCalculationPlantAcresDto) {
		this.claimCalculationPlantAcresDto = claimCalculationPlantAcresDto;
	}	
	
	public ClaimCalculationGrapesDto getClaimCalculationGrapes() {
		return claimCalculationGrapesDto;
	}

	public void setClaimCalculationGrapes(ClaimCalculationGrapesDto claimCalculationGrapesDto) {
		this.claimCalculationGrapesDto = claimCalculationGrapesDto;
	}
	
	public ClaimCalculationGrainUnseededDto getClaimCalculationGrainUnseeded() {
		return claimCalculationGrainUnseededDto;
	}

	public void setClaimCalculationGrainUnseeded(ClaimCalculationGrainUnseededDto claimCalculationGrainUnseededDto) {
		this.claimCalculationGrainUnseededDto = claimCalculationGrainUnseededDto;
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

	public String getCalculateIivInd() {
		return calculateIivInd;
	}
	
	public void setCalculateIivInd(String calculateIivInd) {
		this.calculateIivInd = calculateIivInd;
	}

	public Boolean getHasChequeReqInd() {
		return hasChequeReqInd;
	}
		
	public void setHasChequeReqInd(Boolean hasChequeReqInd) {
		this.hasChequeReqInd = hasChequeReqInd;
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
}
