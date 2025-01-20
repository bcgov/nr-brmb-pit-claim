package ca.bc.gov.mal.cirras.claims.service.api.v1.model.factory;

import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

import java.util.Date;

import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.SyncCommodityVarietyRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.SyncCoveragePerilRsrc;
import ca.bc.gov.mal.cirras.claims.model.v1.SyncClaim;
import ca.bc.gov.mal.cirras.claims.model.v1.SyncCode;
import ca.bc.gov.mal.cirras.claims.model.v1.SyncCommodityVariety;
import ca.bc.gov.mal.cirras.claims.model.v1.SyncCoveragePeril;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimStatusCodeDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.CommodityCoverageCodeDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.CropCommodityDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.CropVarietyDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.InsurancePlanDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.PerilCodeDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.CoveragePerilDto;
import ca.bc.gov.mal.cirras.policies.model.v1.InsuranceClaim;

public interface CirrasDataSyncFactory {

	SyncClaim getSyncClaim(
			ClaimDto dto, 
			FactoryContext context, 
			WebAdeAuthentication authentication
		) throws FactoryException;
	
	ClaimDto createClaimDto(SyncClaim model);
	void updateClaimDataDto(ClaimDto dto, SyncClaim model);
	ClaimDto updatePolicyDataDto(SyncClaim model);
	ClaimDto updateGrowerDataDto(SyncClaim model);
	
	SyncClaim getSyncClaimFromPolicyClaim(InsuranceClaim policyClaim);
	
	//Commodities
	CropCommodityDto createCropCommodity(SyncCommodityVariety model);
	void updateCropCommodity(CropCommodityDto dto, SyncCommodityVariety model);
	SyncCommodityVarietyRsrc getSyncClaimFromCropCommodity(CropCommodityDto dto);
	void updateCropCommodityExpiryDate(CropCommodityDto dto, Date dataSyncTransDate);
	
	//Varieties
	CropVarietyDto createCropVariety(SyncCommodityVariety model);
	void updateCropVariety(CropVarietyDto dto, SyncCommodityVariety model);
	SyncCommodityVarietyRsrc getSyncClaimFromVariety(CropVarietyDto dto);
	void updateCropVarietyExpiryDate(CropVarietyDto dto, Date dataSyncTransDate);

	//PerilCode
	void updatePerilCodeExpiryDate(PerilCodeDto dto, Date dataSyncTransDate);
	PerilCodeDto createPerilCode(SyncCode model);
	void updatePerilCode(PerilCodeDto dto, SyncCode model);

	//CoveragePeril
	void updateCoveragePerilExpiryDate(CoveragePerilDto dto, Date dataSyncTransDate);
	CoveragePerilDto createCoveragePeril(SyncCoveragePeril model);
	SyncCoveragePerilRsrc getSyncCoveragePeril(CoveragePerilDto dto);
	void updateCoveragePeril(CoveragePerilDto dto, SyncCoveragePeril model);
	
	//InsurancePlan
	void updateInsurancePlanExpiryDate(InsurancePlanDto dto, Date dataSyncTransDate);
	InsurancePlanDto createInsurancePlan(SyncCode model);
	void updateInsurancePlan(InsurancePlanDto dto, SyncCode model);

	//ClaimStatusCode
	void updateClaimStatusCode(ClaimStatusCodeDto dto, SyncCode model);
	ClaimStatusCodeDto createClaimStatusCode(SyncCode model);
	void updateClaimStatusExpiryDate(ClaimStatusCodeDto dto, Date dataSyncTransDate);

	//CommodityCoverageCode
	void updateCommodityCoverageCode(CommodityCoverageCodeDto dto, SyncCode model);
	CommodityCoverageCodeDto createCommodityCoverageCode(SyncCode model);
	void updateCommodityCoverageCodeExpiryDate(CommodityCoverageCodeDto dto, Date dataSyncTransDate);


}
