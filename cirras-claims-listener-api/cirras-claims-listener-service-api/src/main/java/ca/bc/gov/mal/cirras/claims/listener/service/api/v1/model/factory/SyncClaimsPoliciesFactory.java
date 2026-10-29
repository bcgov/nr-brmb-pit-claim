package ca.bc.gov.mal.cirras.claims.listener.service.api.v1.model.factory;

import java.util.Date;

import ca.bc.gov.mal.cirras.claims.data.resources.SyncClaimRsrc;
import ca.bc.gov.mal.cirras.claims.data.resources.SyncCommodityVarietyRsrc;
import ca.bc.gov.mal.cirras.claims.data.resources.SyncCoveragePerilRsrc;
import ca.bc.gov.mal.cirras.claims.data.resources.SyncCodeRsrc;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.CodeSyncRsrc;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.CommodityVarietySyncRsrc;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.CoveragePerilSyncRsrc;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.InsurancePolicyRsrc;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.InsuredGrowerRsrc;
import ca.bc.gov.mal.cirras.policies.model.v1.InsuranceClaim;

public interface SyncClaimsPoliciesFactory {

	void loadSyncClaimFromPolicyClaim(SyncClaimRsrc syncClaimRsrc, InsuranceClaim policyClaim, Date eventDate, String eventType);

	SyncClaimRsrc getSyncClaimRsrc(InsuranceClaim policyClaim, Date eventDate, String eventType);

	SyncClaimRsrc getSyncClaimPolicyUpdateRsrc(InsurancePolicyRsrc insurancePolicy, Date eventDate, String eventType);
	SyncClaimRsrc getSyncClaimGrowerUpdateRsrc(InsuredGrowerRsrc insuredGrower, Date eventDate, String eventType);
	
	//CommodityVariety
	SyncCommodityVarietyRsrc getSyncCommodityVarietyRsrc(CommodityVarietySyncRsrc commodityVarietySyncRsrc, Date eventDate, String eventType);
	SyncCommodityVarietyRsrc getDeleteSyncCommodityVarietyRsrc(String cropId, Date eventDate, String eventType);

	//Generic resource
	SyncCodeRsrc getSyncCodeRsrc(CodeSyncRsrc codeSyncRsrc, Date eventDate, String eventType);

	//Coverage Peril
	SyncCoveragePerilRsrc getSyncCoveragePerilRsrc(CoveragePerilSyncRsrc coveragePerilSyncRsrc, Date eventDate, String eventType);
	SyncCoveragePerilRsrc getDeleteSyncCoveragePerilRsrc(String coveragePerilId, Date eventDate, String eventType);
	
}
