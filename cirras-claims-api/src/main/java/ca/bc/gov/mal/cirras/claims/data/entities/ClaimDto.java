package ca.bc.gov.mal.cirras.claims.data.entities;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;



public class ClaimDto extends BaseDto<ClaimDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ClaimDto.class);
	
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
	
	private Date claimDataSyncTransDate;
	private Date policyDataSyncTransDate;
	private Date growerDataSyncTransDate;
	
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;
	
	//Claims List
	private String claimGrowerName;
	private String calculationGrowerName;
	private String planName;
	private String commodityName;
	private String cropCoverage;
	private String claimCalculationGuid;
	private Integer calculationVersion;
	private String calculationStatusCode;
	private String calculationStatus;

	private ClaimCalculationDto claimCalculationDto;
	
	
	public ClaimDto() {
	}
	
	
	public ClaimDto(ClaimDto dto) {
		this.colId = dto.colId;
		this.claimNumber = dto.claimNumber;
		this.claimStatusCode = dto.claimStatusCode;
		this.submittedByUserid = dto.submittedByUserid;
		this.submittedByName = dto.submittedByName;
		this.submittedByDate = dto.submittedByDate;
		this.recommendedByUserid = dto.recommendedByUserid;
		this.recommendedByName = dto.recommendedByName;
		this.recommendedByDate = dto.recommendedByDate;
		this.approvedByUserid = dto.approvedByUserid;
		this.approvedByName = dto.approvedByName;
		this.approvedByDate = dto.approvedByDate;
		this.hasChequeReqInd = dto.hasChequeReqInd;
		this.ippId = dto.ippId;
		this.commodityCoverageCode = dto.commodityCoverageCode;
		this.cropCommodityId = dto.cropCommodityId;
		this.iplId = dto.iplId;
		this.policyNumber = dto.policyNumber;
		this.insurancePlanId = dto.insurancePlanId;
		this.cropYear = dto.cropYear;
		this.contractId = dto.contractId;
		this.igId = dto.igId;
		this.growerName = dto.growerName;
		this.claimDataSyncTransDate = dto.claimDataSyncTransDate;
		this.policyDataSyncTransDate = dto.policyDataSyncTransDate;
		this.growerDataSyncTransDate = dto.growerDataSyncTransDate;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;

		if ( dto.claimCalculationDto != null ) {
			this.claimCalculationDto = dto.claimCalculationDto.copy();
		}
	
	}
	

	@Override
	public boolean equalsBK(ClaimDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(ClaimDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("colId", colId, other.colId);
			result = result&&dtoUtils.equals("claimNumber", claimNumber, other.claimNumber);
			result = result&&dtoUtils.equals("claimStatusCode", claimStatusCode, other.claimStatusCode);
			result = result&&dtoUtils.equals(submittedByUserid, submittedByUserid, other.submittedByUserid);
			result = result&&dtoUtils.equals(submittedByName, submittedByName, other.submittedByName);
			result = result&&dtoUtils.equals(recommendedByUserid, recommendedByUserid, other.recommendedByUserid);
			result = result&&dtoUtils.equals(recommendedByName, recommendedByName, other.recommendedByName);
			result = result&&dtoUtils.equals(approvedByUserid, approvedByUserid, other.approvedByUserid);
			result = result&&dtoUtils.equals(approvedByName, approvedByName, other.approvedByName);
			result = result&&dtoUtils.equals("hasChequeReqInd", hasChequeReqInd, other.hasChequeReqInd);
			result = result&&dtoUtils.equals("ippId", ippId, other.ippId);
			result = result&&dtoUtils.equals("commodityCoverageCode", commodityCoverageCode, other.commodityCoverageCode);
			result = result&&dtoUtils.equals("cropCommodityId", cropCommodityId, other.cropCommodityId);
			result = result&&dtoUtils.equals("iplId", iplId, other.iplId);
			result = result&&dtoUtils.equals("policyNumber", policyNumber, other.policyNumber);
			result = result&&dtoUtils.equals("insurancePlanId", insurancePlanId, other.insurancePlanId);
			result = result&&dtoUtils.equals("cropYear", cropYear, other.cropYear);
			result = result&&dtoUtils.equals("contractId", contractId, other.contractId);
			result = result&&dtoUtils.equals("igId", igId, other.igId);
			result = result&&dtoUtils.equals("growerName", growerName, other.growerName);
			result = result&&dtoUtils.equals("claimDataSyncTransDate", claimDataSyncTransDate, other.claimDataSyncTransDate);
			result = result&&dtoUtils.equals("policyDataSyncTransDate", policyDataSyncTransDate, other.policyDataSyncTransDate);
			result = result&&dtoUtils.equals("growerDataSyncTransDate", growerDataSyncTransDate, other.growerDataSyncTransDate);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public ClaimDto copy() {
		return new ClaimDto(this);
	}

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

	//Claim List Properties only need a getter
	public String getClaimGrowerName() {
		return claimGrowerName;
	}

	public String getCalculationGrowerName() {
		return calculationGrowerName;
	}

	public String getPlanName() {
		return planName;
	}

	public String getCommodityName() {
		return commodityName;
	}

	public String getCropCoverage() {
		return cropCoverage;
	}

	public String getClaimCalculationGuid() {
		return claimCalculationGuid;
	}

	public Integer getCalculationVersion() {
		return calculationVersion;
	}

	public String getCalculationStatusCode() {
		return calculationStatusCode;
	}

	public String getCalculationStatus() {
		return calculationStatus;
	}

	public ClaimCalculationDto getClaimCalculationDto() {
		return claimCalculationDto;
	}

	public void setClaimCalculationDto(ClaimCalculationDto claimCalculationDto) {
		this.claimCalculationDto = claimCalculationDto;
	}

}
