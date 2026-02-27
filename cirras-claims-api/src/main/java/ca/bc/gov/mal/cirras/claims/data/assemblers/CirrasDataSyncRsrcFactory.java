package ca.bc.gov.mal.cirras.claims.data.assemblers;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.core.UriBuilder;

import ca.bc.gov.nrs.common.wfone.rest.resource.RelLink;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;
import ca.bc.gov.mal.cirras.claims.controllers.SyncClaimEndpoint;
import ca.bc.gov.mal.cirras.claims.controllers.SyncClaimRelatedDataEndpoint;
import ca.bc.gov.mal.cirras.claims.controllers.SyncCodeEndpoint;
import ca.bc.gov.mal.cirras.claims.controllers.SyncCommodityVarietyEndpoint;
import ca.bc.gov.mal.cirras.claims.controllers.SyncCoveragePerilEndpoint;
import ca.bc.gov.mal.cirras.claims.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.claims.data.resources.SyncClaimRsrc;
import ca.bc.gov.mal.cirras.claims.data.resources.SyncCommodityVarietyRsrc;
import ca.bc.gov.mal.cirras.claims.data.resources.SyncCoveragePerilRsrc;
import ca.bc.gov.mal.cirras.claims.data.resources.types.ResourceTypes;
import ca.bc.gov.mal.cirras.claims.data.models.SyncClaim;
import ca.bc.gov.mal.cirras.claims.data.models.SyncCode;
import ca.bc.gov.mal.cirras.claims.data.models.SyncCommodityVariety;
import ca.bc.gov.mal.cirras.claims.data.models.SyncCoveragePeril;
import ca.bc.gov.mal.cirras.claims.data.entities.ClaimDto;
import ca.bc.gov.mal.cirras.claims.data.entities.ClaimStatusCodeDto;
import ca.bc.gov.mal.cirras.claims.data.entities.CommodityCoverageCodeDto;
import ca.bc.gov.mal.cirras.claims.data.entities.CoveragePerilDto;
import ca.bc.gov.mal.cirras.claims.data.entities.CropCommodityDto;
import ca.bc.gov.mal.cirras.claims.data.entities.CropVarietyDto;
import ca.bc.gov.mal.cirras.claims.data.entities.InsurancePlanDto;
import ca.bc.gov.mal.cirras.claims.data.entities.PerilCodeDto;
import ca.bc.gov.mal.cirras.claims.services.utils.CirrasServiceHelper;
import ca.bc.gov.mal.cirras.policies.model.v1.InsuranceClaim;

public class CirrasDataSyncRsrcFactory extends BaseResourceFactory {
	
	
	public SyncClaim getSyncClaim(
			ClaimDto dto, 
			FactoryContext context, 
			WebAdeAuthentication authentication
		) throws FactoryException {
		SyncClaimRsrc resource = new SyncClaimRsrc();
		
		resource.setColId(dto.getColId());
		resource.setClaimNumber(dto.getClaimNumber());
		resource.setClaimStatusCode(dto.getClaimStatusCode());
		resource.setSubmittedByUserid(dto.getSubmittedByUserid());
		resource.setSubmittedByName(dto.getSubmittedByName());
		resource.setSubmittedByDate(dto.getSubmittedByDate());
		resource.setRecommendedByUserid(dto.getRecommendedByUserid());
		resource.setRecommendedByName(dto.getRecommendedByName());
		resource.setRecommendedByDate(dto.getRecommendedByDate());
		resource.setApprovedByUserid(dto.getApprovedByUserid());
		resource.setApprovedByName(dto.getApprovedByName());
		resource.setApprovedByDate(dto.getApprovedByDate());
		resource.setHasChequeReqInd(dto.getHasChequeReqInd());
		resource.setIppId(dto.getIppId());
		resource.setCommodityCoverageCode(dto.getCommodityCoverageCode());
		resource.setCropCommodityId(dto.getCropCommodityId());
		resource.setIplId(dto.getIplId());
		resource.setPolicyNumber(dto.getPolicyNumber());
		resource.setInsurancePlanId(dto.getInsurancePlanId());
		resource.setCropYear(dto.getCropYear());
		resource.setContractId(dto.getContractId());
		resource.setIgId(dto.getIgId());
		resource.setGrowerName(dto.getGrowerName());
		resource.setClaimDataSyncTransDate(dto.getClaimDataSyncTransDate());
		resource.setPolicyDataSyncTransDate(dto.getPolicyDataSyncTransDate());
		resource.setGrowerDataSyncTransDate(dto.getGrowerDataSyncTransDate());
		
		String eTag = getEtag(resource);
		resource.setETag(eTag);
		
		URI baseUri = getBaseURI(context);
		setClaimDataLinks(resource, baseUri, authentication);
		
		return resource;

	}
	
	
	public ClaimDto createClaimDto(SyncClaim model) {
		ClaimDto dto = new ClaimDto();
		
		dto.setColId(model.getColId());
		dto.setClaimNumber(model.getClaimNumber());
		dto.setClaimStatusCode(model.getClaimStatusCode());
		dto.setSubmittedByUserid(model.getSubmittedByUserid()) ;
		dto.setSubmittedByName(model.getSubmittedByName()) ;
		dto.setSubmittedByDate(model.getSubmittedByDate()) ;
		dto.setRecommendedByUserid(model.getRecommendedByUserid()) ;
		dto.setRecommendedByName(model.getRecommendedByName()) ;
		dto.setRecommendedByDate(model.getRecommendedByDate()) ;
		dto.setApprovedByUserid(model.getApprovedByUserid()) ;
		dto.setApprovedByName(model.getApprovedByName()) ;
		dto.setApprovedByDate(model.getApprovedByDate()) ;
		dto.setHasChequeReqInd(model.getHasChequeReqInd()) ;
		dto.setIppId(model.getIppId());
		dto.setCommodityCoverageCode(model.getCommodityCoverageCode());
		dto.setCropCommodityId(model.getCropCommodityId());
		dto.setIplId(model.getIplId());
		dto.setPolicyNumber(model.getPolicyNumber());
		dto.setInsurancePlanId(model.getInsurancePlanId());
		dto.setCropYear(model.getCropYear());
		dto.setContractId(model.getContractId());
		dto.setIgId(model.getIgId());
		dto.setGrowerName(model.getGrowerName());
		dto.setClaimDataSyncTransDate(model.getClaimDataSyncTransDate());
		dto.setPolicyDataSyncTransDate(model.getPolicyDataSyncTransDate());
		dto.setGrowerDataSyncTransDate(model.getGrowerDataSyncTransDate());
	
		return dto;
	}

	
	public void updateClaimDataDto(ClaimDto dto, SyncClaim model) {
		
		dto.setColId(model.getColId());
		dto.setClaimStatusCode(model.getClaimStatusCode());
		dto.setSubmittedByUserid(model.getSubmittedByUserid());
		dto.setSubmittedByName(model.getSubmittedByName());
		dto.setSubmittedByDate(model.getSubmittedByDate());
		dto.setRecommendedByUserid(model.getRecommendedByUserid());
		dto.setRecommendedByName(model.getRecommendedByName());
		dto.setRecommendedByDate(model.getRecommendedByDate());
		dto.setApprovedByUserid(model.getApprovedByUserid());
		dto.setApprovedByName(model.getApprovedByName());
		dto.setApprovedByDate(model.getApprovedByDate());
		dto.setHasChequeReqInd(model.getHasChequeReqInd());
		dto.setClaimDataSyncTransDate(model.getClaimDataSyncTransDate());
	
	}

	
	public ClaimDto updatePolicyDataDto(SyncClaim model) {
		ClaimDto dto = new ClaimDto();
		
		dto.setIplId(model.getIplId());
		dto.setIgId(model.getIgId());
		dto.setGrowerName(model.getGrowerName());
		dto.setPolicyDataSyncTransDate(model.getPolicyDataSyncTransDate());
	
		return dto;
	}

	
	public ClaimDto updateGrowerDataDto(SyncClaim model) {
		ClaimDto dto = new ClaimDto();
		
		dto.setIgId(model.getIgId());
		dto.setGrowerName(model.getGrowerName());
		dto.setGrowerDataSyncTransDate(model.getGrowerDataSyncTransDate());		
	
		return dto;
	}	
	
	
	public SyncClaim getSyncClaimFromPolicyClaim(InsuranceClaim policyClaim) {
		SyncClaimRsrc resource = new SyncClaimRsrc();
		
		resource.setColId(policyClaim.getClaimId());
		resource.setClaimNumber(policyClaim.getClaimNumber());
		resource.setClaimStatusCode(policyClaim.getClaimStatusCode());
		resource.setSubmittedByUserid(policyClaim.getSubmittedByUserid());
		resource.setSubmittedByName(policyClaim.getSubmittedByName());
		resource.setSubmittedByDate(policyClaim.getSubmittedByDate());
		resource.setRecommendedByUserid(policyClaim.getRecommendedByUserid());
		resource.setRecommendedByName(policyClaim.getRecommendedByName());
		resource.setRecommendedByDate(policyClaim.getRecommendedByDate());
		resource.setApprovedByUserid(policyClaim.getApprovedByUserid());
		resource.setApprovedByName(policyClaim.getApprovedByName());
		resource.setApprovedByDate(policyClaim.getApprovedByDate());
		resource.setHasChequeReqInd(policyClaim.getHasChequeReqInd());
		
		return resource;
	}

	
	public CropCommodityDto createCropCommodity(SyncCommodityVariety model) {
		CropCommodityDto dto = new CropCommodityDto();
		
		CirrasServiceHelper helper = new CirrasServiceHelper();
		
		dto.setCropCommodityId(model.getCropId());
		dto.setCommodityName(helper.capitalizeEachWord(model.getCropName()));
		dto.setEffectiveDate(new Date());
		dto.setExpiryDate(getMaxExpiryDate());
		dto.setDataSyncTransDate(model.getDataSyncTransDate());
		
		return dto;
	}

	
	public void updateCropCommodity(CropCommodityDto dto, SyncCommodityVariety model) {

		CirrasServiceHelper helper = new CirrasServiceHelper();

		dto.setCommodityName(helper.capitalizeEachWord(model.getCropName()));
		dto.setDataSyncTransDate(model.getDataSyncTransDate());
		
	}

	
	public void updateCropCommodityExpiryDate(CropCommodityDto dto, Date dataSyncTransDate) {
		//Don't delete the record but set expiry date to today
		dto.setExpiryDate(new Date());
		dto.setDataSyncTransDate(dataSyncTransDate);

	}

	
	public SyncCommodityVarietyRsrc getSyncClaimFromCropCommodity(CropCommodityDto dto) {
		SyncCommodityVarietyRsrc resource = new SyncCommodityVarietyRsrc();
		
		resource.setCropId(dto.getCropCommodityId());
		
		return resource;
	}


	
	public CropVarietyDto createCropVariety(SyncCommodityVariety model) {
		CropVarietyDto dto = new CropVarietyDto();
		
		CirrasServiceHelper helper = new CirrasServiceHelper();

		dto.setCropVarietyId(model.getCropId());
		dto.setCropCommodityId(model.getParentCropId());
		dto.setVarietyName(helper.capitalizeEachWord(model.getCropName()));
		dto.setEffectiveDate(new Date());
		dto.setExpiryDate(getMaxExpiryDate());
		dto.setDataSyncTransDate(model.getDataSyncTransDate());
		
		return dto;
	
	}

	
	public void updateCropVariety(CropVarietyDto dto, SyncCommodityVariety model) {

		CirrasServiceHelper helper = new CirrasServiceHelper();

		dto.setCropCommodityId(model.getParentCropId());
		dto.setVarietyName(helper.capitalizeEachWord(model.getCropName()));
		dto.setDataSyncTransDate(model.getDataSyncTransDate());
		
	}
	
	
	public void updateCropVarietyExpiryDate(CropVarietyDto dto, Date dataSyncTransDate) {
		//Don't delete the record but set expiry date to today
		dto.setExpiryDate(new Date());
		dto.setDataSyncTransDate(dataSyncTransDate);
		
	}

	
	public SyncCommodityVarietyRsrc getSyncClaimFromVariety(CropVarietyDto dto) {
		SyncCommodityVarietyRsrc resource = new SyncCommodityVarietyRsrc();
		
		resource.setCropId(dto.getCropVarietyId());
		resource.setParentCropId(dto.getCropCommodityId());
		
		return resource;
	}
	
	
	public void updatePerilCodeExpiryDate(PerilCodeDto dto, Date dataSyncTransDate) {
		//Don't delete the record but set expiry date to today
		dto.setExpiryDate(new Date());
		dto.setDataSyncTransDate(dataSyncTransDate);
	}
	
	
	public PerilCodeDto createPerilCode(SyncCode model) {
		
		PerilCodeDto dto = new PerilCodeDto();
		
		calculateDates(model.getIsActive(), new Date(), getMaxExpiryDate());

		
		dto.setPerilCode(model.getUniqueKeyString());
		dto.setDescription(model.getDescription());
		dto.setEffectiveDate(this.newEffectiveDate);
		dto.setExpiryDate(this.newExpiryDate);
		dto.setDataSyncTransDate(model.getDataSyncTransDate());
		
		return dto;

	}

	
	public void updatePerilCode(PerilCodeDto dto, SyncCode model) {

		calculateDates(model.getIsActive(), dto.getEffectiveDate(), dto.getExpiryDate());
		
		dto.setDescription(model.getDescription());
		dto.setEffectiveDate(this.newEffectiveDate);
		dto.setExpiryDate(this.newExpiryDate);
		dto.setDataSyncTransDate(model.getDataSyncTransDate());
	}
	
	
	public void updateClaimStatusCode(ClaimStatusCodeDto dto, SyncCode model) {
		calculateDates(model.getIsActive(), dto.getEffectiveDate(), dto.getExpiryDate());
		
		dto.setDescription(model.getDescription());
		dto.setEffectiveDate(this.newEffectiveDate);
		dto.setExpiryDate(this.newExpiryDate);
		dto.setDataSyncTransDate(model.getDataSyncTransDate());
	}

	
	public ClaimStatusCodeDto createClaimStatusCode(SyncCode model) {
		ClaimStatusCodeDto dto = new ClaimStatusCodeDto();
		
		calculateDates(model.getIsActive(), new Date(), getMaxExpiryDate());
		
		dto.setClaimStatusCode(model.getUniqueKeyString());
		dto.setDescription(model.getDescription());
		dto.setEffectiveDate(this.newEffectiveDate);
		dto.setExpiryDate(this.newExpiryDate);
		dto.setDataSyncTransDate(model.getDataSyncTransDate());
		
		return dto;	
	}

	
	public void updateClaimStatusExpiryDate(ClaimStatusCodeDto dto, Date dataSyncTransDate) {
		//Don't delete the record but set expiry date to today
		dto.setExpiryDate(new Date());
		dto.setDataSyncTransDate(dataSyncTransDate);
	}
	
	
	
	public SyncCoveragePerilRsrc getSyncCoveragePeril(CoveragePerilDto dto) {
		SyncCoveragePerilRsrc resource = new SyncCoveragePerilRsrc();
		
		resource.setCoveragePerilId(dto.getCoveragePerilId());
		resource.setCropCommodityId(dto.getCropCommodityId());
		resource.setPerilCode(dto.getPerilCode());
		resource.setCommodityCoverageCode(dto.getCommodityCoverageCode());
		resource.setDataSyncTransDate(dto.getDataSyncTransDate());
		
		return resource;
	}

	
	public void updateCoveragePerilExpiryDate(CoveragePerilDto dto, Date dataSyncTransDate) {
		//Don't delete the record but set expiry date to today
		dto.setExpiryDate(new Date());
		dto.setDataSyncTransDate(dataSyncTransDate);
	}

	
	public CoveragePerilDto createCoveragePeril(SyncCoveragePeril model) {
		CoveragePerilDto dto = new CoveragePerilDto();
		
		calculateDates(model.getIsActive(), new Date(), getMaxExpiryDate());
		
		dto.setCoveragePerilId(model.getCoveragePerilId());
		dto.setCropCommodityId(model.getCropCommodityId());
		dto.setPerilCode(model.getPerilCode());
		dto.setCommodityCoverageCode(model.getCommodityCoverageCode());
		dto.setEffectiveDate(this.newEffectiveDate);
		dto.setExpiryDate(this.newExpiryDate);
		dto.setDataSyncTransDate(model.getDataSyncTransDate());
		
		return dto;

	}

	
	public void updateCoveragePeril(CoveragePerilDto dto, SyncCoveragePeril model) {

		calculateDates(model.getIsActive(), dto.getEffectiveDate(), dto.getExpiryDate());
		
		dto.setEffectiveDate(this.newEffectiveDate);
		dto.setExpiryDate(this.newExpiryDate);
		dto.setDataSyncTransDate(model.getDataSyncTransDate());
		
	}
	
	
	public void updateInsurancePlanExpiryDate(InsurancePlanDto dto, Date dataSyncTransDate) {
		
		//Don't delete the record but set expiry date to today
		dto.setExpiryDate(new Date());
		dto.setDataSyncTransDate(dataSyncTransDate);

	}

	
	public InsurancePlanDto createInsurancePlan(SyncCode model) {
		InsurancePlanDto dto = new InsurancePlanDto();
		
		calculateDates(model.getIsActive(), new Date(), getMaxExpiryDate());

		CirrasServiceHelper helper = new CirrasServiceHelper();

		dto.setInsurancePlanId(model.getUniqueKeyInteger());
		dto.setInsurancePlanOriginalName(model.getDescription());
		dto.setInsurancePlanName(helper.capitalizeEachWord(model.getDescription()));
		dto.setEffectiveDate(this.newEffectiveDate);
		dto.setExpiryDate(this.newExpiryDate);
		dto.setDataSyncTransDate(model.getDataSyncTransDate());
		
		return dto;
	}

	
	public void updateInsurancePlan(InsurancePlanDto dto, SyncCode model) {

		calculateDates(model.getIsActive(), dto.getEffectiveDate(), dto.getExpiryDate());
		
		CirrasServiceHelper helper = new CirrasServiceHelper();

		dto.setInsurancePlanOriginalName(model.getDescription());
		dto.setInsurancePlanName(helper.capitalizeEachWord(model.getDescription()));
		dto.setEffectiveDate(this.newEffectiveDate);
		dto.setExpiryDate(this.newExpiryDate);
		dto.setDataSyncTransDate(model.getDataSyncTransDate());
		
	}	


	
	public void updateCommodityCoverageCode(CommodityCoverageCodeDto dto, SyncCode model) {
		calculateDates(model.getIsActive(), dto.getEffectiveDate(), dto.getExpiryDate());
		
		dto.setDescription(model.getDescription());
		dto.setEffectiveDate(this.newEffectiveDate);
		dto.setExpiryDate(this.newExpiryDate);
		dto.setDataSyncTransDate(model.getDataSyncTransDate());
	}

	
	public CommodityCoverageCodeDto createCommodityCoverageCode(SyncCode model) {
		CommodityCoverageCodeDto dto = new CommodityCoverageCodeDto();
		
		calculateDates(model.getIsActive(), new Date(), getMaxExpiryDate());
		
		dto.setCommodityCoverageCode(model.getUniqueKeyString());
		dto.setDescription(model.getDescription());
		dto.setEffectiveDate(this.newEffectiveDate);
		dto.setExpiryDate(this.newExpiryDate);
		dto.setDataSyncTransDate(model.getDataSyncTransDate());
		
		return dto;	
	}

	
	public void updateCommodityCoverageCodeExpiryDate(CommodityCoverageCodeDto dto, Date dataSyncTransDate) {
		//Don't delete the record but set expiry date to today
		dto.setExpiryDate(new Date());
		dto.setDataSyncTransDate(dataSyncTransDate);
	}	
	
	//Sets effective and expiry date according to the isActive flag in CIRRAS
	private void calculateDates(Boolean isActive, Date effectiveDate, Date expiryDate) {
		
		LocalDateTime currentDate = LocalDateTime.now(ZoneId.systemDefault());
		LocalDateTime currentExpiryDate = LocalDateTime.ofInstant(expiryDate.toInstant(), ZoneId.systemDefault());
		
		//Default
		this.newEffectiveDate = effectiveDate;
		this.newExpiryDate = expiryDate;
		
		if(isActive && currentDate.isAfter(currentExpiryDate)) {
			//Set to active
			//Inactive to Active: set effective date to now and expiry date to max expiry date 
			this.newEffectiveDate = new Date();
			this.newExpiryDate = getMaxExpiryDate();
		} else if (isActive == false && currentDate.isBefore(currentExpiryDate)) {
			//Set to inactive
			//Active to Inactive: set expiry date to now
			this.newExpiryDate = new Date();
		}
	}
	
	private Date newEffectiveDate;
	private Date newExpiryDate;

	public static String getSyncClaimSelfUri(
			Integer colId,
			URI baseUri) {

		String result = UriBuilder.fromUri(baseUri)
			.path(SyncClaimEndpoint.class)
			.queryParam("colId", nvl(colId, ""))
			.build()
			.toString();

		return result;
	}
	
	private static void setClaimDataLinks(
			SyncClaimRsrc resource, 
			URI baseUri, 
			WebAdeAuthentication authentication) {
			
		if (authentication.hasAuthority(Scopes.UPDATE_SYNC_CLAIM)) {
			String result = UriBuilder
				.fromUri(baseUri)
				.path(SyncClaimEndpoint.class)
				.queryParam("colId", nvl(resource.getColId(), ""))
				.build().toString();
			resource.getLinks().add(new RelLink(ResourceTypes.UPDATE_SYNC_CLAIM, result, HttpMethod.PUT));
		}
		if (authentication.hasAuthority(Scopes.DELETE_SYNC_CLAIM)) {

			String result = UriBuilder
				.fromUri(baseUri)
				.path(SyncClaimEndpoint.class)
				.queryParam("colId", nvl(resource.getColId(), ""))
				.build().toString();
			resource.getLinks().add(new RelLink(ResourceTypes.DELETE_SYNC_CLAIM, result, HttpMethod.DELETE));
		}
	}

	public static String getSyncClaimRelatedDataSelfUri(
			Integer colId,
			URI baseUri) {

		String result = UriBuilder.fromUri(baseUri)
			.path(SyncClaimRelatedDataEndpoint.class)
			.build()
			.toString();

		return result;
	}
	
	public static String getSyncCommodityVarietySelfUri(
			URI baseUri) {

		String result = UriBuilder.fromUri(baseUri)
			.path(SyncCommodityVarietyEndpoint.class)
			.build()
			.toString();

		return result;
	}	
	
	public static String getSyncCodeSelfUri(
			URI baseUri) {

		String result = UriBuilder.fromUri(baseUri)
			.path(SyncCodeEndpoint.class)
			.build()
			.toString();

		return result;
	}

	
	public static String getSyncCoveragePerilSelfUri(
			URI baseUri) {

		String result = UriBuilder.fromUri(baseUri)
			.path(SyncCoveragePerilEndpoint.class)
			.build()
			.toString();

		return result;
	}
	protected static String nvl(Integer value, String defaultValue) {
		return (value==null)?defaultValue:value.toString();
	}
	
	protected static Date getMaxExpiryDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(9000,12,31);
		
		return cal.getTime();
		
	}

}
