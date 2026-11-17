package ca.bc.gov.mal.cirras.claims.listener.api.rest.v1.resource.factory;

import java.util.Date;

import ca.bc.gov.mal.cirras.claims.data.resources.SyncClaimRsrc;
import ca.bc.gov.mal.cirras.claims.data.resources.SyncCodeRsrc;
import ca.bc.gov.mal.cirras.claims.data.resources.SyncCommodityVarietyRsrc;
import ca.bc.gov.mal.cirras.claims.data.resources.SyncCoveragePerilRsrc;
import ca.bc.gov.mal.cirras.claims.listener.service.api.v1.model.factory.SyncClaimsPoliciesFactory;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.CodeSyncRsrc;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.CommodityVarietySyncRsrc;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.CoveragePerilSyncRsrc;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.InsurancePolicyRsrc;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.InsuredGrowerRsrc;
import ca.bc.gov.mal.cirras.policies.model.v1.InsuranceClaim;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;

public class SyncClaimsPoliciesRsrcFactory extends BaseResourceFactory implements SyncClaimsPoliciesFactory {
	
	@Override
	public void loadSyncClaimFromPolicyClaim(SyncClaimRsrc syncClaimRsrc, InsuranceClaim policyClaim, Date eventDate, String eventType) {
		
		syncClaimRsrc.setColId(policyClaim.getClaimId());
		syncClaimRsrc.setClaimNumber(policyClaim.getClaimNumber());
		syncClaimRsrc.setClaimStatusCode(policyClaim.getClaimStatusCode());
		syncClaimRsrc.setSubmittedByUserid(policyClaim.getSubmittedByUserid());
		syncClaimRsrc.setSubmittedByName(policyClaim.getSubmittedByName());
		syncClaimRsrc.setSubmittedByDate(policyClaim.getSubmittedByDate());
		syncClaimRsrc.setRecommendedByUserid(policyClaim.getRecommendedByUserid());
		syncClaimRsrc.setRecommendedByName(policyClaim.getRecommendedByName());
		syncClaimRsrc.setRecommendedByDate(policyClaim.getRecommendedByDate());
		syncClaimRsrc.setApprovedByUserid(policyClaim.getApprovedByUserid());
		syncClaimRsrc.setApprovedByName(policyClaim.getApprovedByName());
		syncClaimRsrc.setApprovedByDate(policyClaim.getApprovedByDate());
		syncClaimRsrc.setHasChequeReqInd(policyClaim.getHasChequeReqInd());
		syncClaimRsrc.setClaimDataSyncTransDate(eventDate);
		syncClaimRsrc.setTransactionType(eventType);

	}
	
	@Override
	public SyncClaimRsrc getSyncClaimRsrc(InsuranceClaim policyClaim, Date eventDate, String eventType) {

		SyncClaimRsrc resource = new SyncClaimRsrc();
		
		resource.setColId(policyClaim.getClaimId());
		resource.setClaimNumber(policyClaim.getClaimNumber());
		resource.setClaimStatusCode(policyClaim.getClaimStatusCode());
		resource.setSubmittedByUserid(policyClaim.getSubmittedByUserid()) ;
		resource.setSubmittedByName(policyClaim.getSubmittedByName()) ;
		resource.setSubmittedByDate(policyClaim.getSubmittedByDate()) ;
		resource.setRecommendedByUserid(policyClaim.getRecommendedByUserid()) ;
		resource.setRecommendedByName(policyClaim.getRecommendedByName()) ;
		resource.setRecommendedByDate(policyClaim.getRecommendedByDate()) ;
		resource.setApprovedByUserid(policyClaim.getApprovedByUserid()) ;
		resource.setApprovedByName(policyClaim.getApprovedByName()) ;
		resource.setApprovedByDate(policyClaim.getApprovedByDate()) ;
		resource.setHasChequeReqInd(policyClaim.getHasChequeReqInd()) ;
		resource.setIppId(policyClaim.getPurchaseId());
		resource.setCommodityCoverageCode(policyClaim.getCommodityCoverageCode());
		resource.setCropCommodityId(policyClaim.getCropCommodityId());
		resource.setIplId(policyClaim.getInsurancePolicyId());
		resource.setPolicyNumber(policyClaim.getPolicyNumber());
		resource.setInsurancePlanId(policyClaim.getInsurancePlanId());
		resource.setCropYear(policyClaim.getCropYear());
		resource.setContractId(policyClaim.getContractId());
		resource.setIgId(policyClaim.getGrowerId());
		resource.setGrowerName(policyClaim.getGrowerName());
		resource.setClaimDataSyncTransDate(eventDate);
		resource.setPolicyDataSyncTransDate(eventDate);
		resource.setGrowerDataSyncTransDate(eventDate);
		resource.setTransactionType(eventType);
	
		return resource;
	}

	@Override
	public SyncClaimRsrc getSyncClaimPolicyUpdateRsrc(InsurancePolicyRsrc insurancePolicy, Date eventDate, String eventType) {
		SyncClaimRsrc resource = new SyncClaimRsrc();
		
		resource.setIplId(insurancePolicy.getInsurancePolicyId());
		resource.setIgId(insurancePolicy.getGrowerId());
		resource.setGrowerName(insurancePolicy.getGrowerName());
		resource.setPolicyDataSyncTransDate(eventDate);
		resource.setTransactionType(eventType);
	
		return resource;

	}

	@Override
	public SyncClaimRsrc getSyncClaimGrowerUpdateRsrc(InsuredGrowerRsrc insuredGrower, Date eventDate, String eventType) {
		SyncClaimRsrc resource = new SyncClaimRsrc();
		
		resource.setIgId(insuredGrower.getGrowerId());
		resource.setGrowerName(insuredGrower.getGrowerName());
		resource.setGrowerDataSyncTransDate(eventDate);
		resource.setTransactionType(eventType);
	
		return resource;

	}

	@Override
	public SyncCommodityVarietyRsrc getSyncCommodityVarietyRsrc(CommodityVarietySyncRsrc commodityVarietySyncRsrc,
			Date eventDate, String eventType) {
		SyncCommodityVarietyRsrc resource = new SyncCommodityVarietyRsrc();

		resource.setCropId(commodityVarietySyncRsrc.getCropId());
		resource.setCropName(commodityVarietySyncRsrc.getCropName());
		resource.setParentCropId(commodityVarietySyncRsrc.getParentCropId());
		resource.setIsProductInsurable(commodityVarietySyncRsrc.getIsProductInsurable());
		resource.setIsInventoryCrop(commodityVarietySyncRsrc.getIsInventoryCrop());
		resource.setDataSyncTransDate(eventDate);
		resource.setTransactionType(eventType);
		
		return resource;
	}

	@Override
	public SyncCommodityVarietyRsrc getDeleteSyncCommodityVarietyRsrc(String cropId, Date eventDate, String eventType) {
		SyncCommodityVarietyRsrc resource = new SyncCommodityVarietyRsrc();

		resource.setCropId(Integer.valueOf(cropId));
		resource.setDataSyncTransDate(eventDate);
		resource.setTransactionType(eventType);
		
		return resource;
	}

	@Override
	public SyncCodeRsrc getSyncCodeRsrc(CodeSyncRsrc codeSyncRsrc, Date eventDate, String eventType) {
		SyncCodeRsrc resource = new SyncCodeRsrc();
		
		resource.setCodeTableType(codeSyncRsrc.getCodeTableType());
		resource.setUniqueKeyString(codeSyncRsrc.getUniqueKeyString());
		resource.setUniqueKeyInteger(codeSyncRsrc.getUniqueKeyInteger());
		resource.setDescription(codeSyncRsrc.getDescription());
		resource.setIsActive(codeSyncRsrc.getIsActive());
		resource.setDataSyncTransDate(eventDate);
		resource.setTransactionType(eventType);
		
		return resource;
	}

	@Override
	public SyncCoveragePerilRsrc getSyncCoveragePerilRsrc(CoveragePerilSyncRsrc coveragePerilSyncRsrc, Date eventDate, String eventType) {
		SyncCoveragePerilRsrc resource = new SyncCoveragePerilRsrc();
		
		resource.setCoveragePerilId(coveragePerilSyncRsrc.getCoveragePerilId());
		resource.setCropCommodityId(coveragePerilSyncRsrc.getCropCommodityId());
		resource.setPerilCode(coveragePerilSyncRsrc.getPerilCode());
		resource.setCommodityCoverageCode(coveragePerilSyncRsrc.getCommodityCoverageCode());
		resource.setIsActive(coveragePerilSyncRsrc.getIsActive());
		resource.setDataSyncTransDate(eventDate);
		resource.setTransactionType(eventType);
		
		return resource;
	}

	@Override
	public SyncCoveragePerilRsrc getDeleteSyncCoveragePerilRsrc(String coveragePerilId, Date eventDate,
			String eventType) {

		SyncCoveragePerilRsrc resource = new SyncCoveragePerilRsrc();

		resource.setCoveragePerilId(Integer.valueOf(coveragePerilId));
		resource.setDataSyncTransDate(eventDate);
		resource.setTransactionType(eventType);
		
		return resource;

	}

}
