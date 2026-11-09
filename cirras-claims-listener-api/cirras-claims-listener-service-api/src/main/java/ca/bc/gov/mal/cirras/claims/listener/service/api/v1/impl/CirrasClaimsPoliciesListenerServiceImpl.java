package ca.bc.gov.mal.cirras.claims.listener.service.api.v1.impl;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.mal.cirras.claims.clients.CirrasClaimService;
import ca.bc.gov.mal.cirras.claims.clients.CirrasClaimServiceException;
import ca.bc.gov.mal.cirras.claims.clients.ValidationException;
import ca.bc.gov.mal.cirras.claims.data.resources.EndpointsRsrc;
import ca.bc.gov.mal.cirras.claims.data.resources.SyncClaimRsrc;
import ca.bc.gov.mal.cirras.claims.data.resources.SyncCodeRsrc;
import ca.bc.gov.mal.cirras.claims.data.resources.SyncCommodityVarietyRsrc;
import ca.bc.gov.mal.cirras.claims.data.resources.SyncCoveragePerilRsrc;
import ca.bc.gov.mal.cirras.claims.listener.service.api.v1.CirrasClaimsPoliciesListenerService;
import ca.bc.gov.mal.cirras.claims.listener.service.api.v1.model.factory.SyncClaimsPoliciesFactory;
import ca.bc.gov.mal.cirras.policies.api.rest.client.v1.CirrasPolicyService;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.CodeSyncRsrc;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.CommodityVarietySyncRsrc;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.CoveragePerilSyncRsrc;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.InsuranceClaimRsrc;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.InsurancePolicyRsrc;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.InsuredGrowerRsrc;
import ca.bc.gov.mal.cirras.policies.model.v1.PoliciesEventTypes;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;

public class CirrasClaimsPoliciesListenerServiceImpl implements CirrasClaimsPoliciesListenerService {

	private static final Logger logger = LoggerFactory.getLogger(CirrasClaimsPoliciesListenerServiceImpl.class);

	private static ObjectMapper mapper = new ObjectMapper();

	// services
	private CirrasPolicyService cirrasPolicyService;
	private CirrasClaimService cirrasClaimService;

	// factories
	private SyncClaimsPoliciesFactory syncClaimFactory;

	private Date eventDate;
	private String eventType;

	public void setCirrasPolicyService(CirrasPolicyService cirrasPolicyService) {
		this.cirrasPolicyService = cirrasPolicyService;
	}

	public void setCirrasClaimService(CirrasClaimService cirrasClaimService) {
		this.cirrasClaimService = cirrasClaimService;
	}

	public void setSyncClaimFactory(SyncClaimsPoliciesFactory syncClaimFactory) {
		this.syncClaimFactory = syncClaimFactory;
	}

	public void processCirrasClaimEvent(String eventMessageString, FactoryContext factoryContext) throws Throwable {
		logger.debug("<processCirrasClaimEvent");

		try {
			Map<String, Object> policiesEventObjMap = null;
			try {
				policiesEventObjMap = mapper.readValue(eventMessageString, new TypeReference<Map<String, Object>>() {
				});
			} catch (Throwable t) {
				logger.error("When attempting to unmarshal " + eventMessageString + ", saw " + t.getMessage(), t);
				throw (t);
			}

			eventType = (String) policiesEventObjMap.get("eventType");
			Instant eventTimestamp = Instant.parse((String) policiesEventObjMap.get("eventTimestamp"));
			eventDate = Date.from(eventTimestamp);

			InsuranceClaimRsrc insuranceClaimRsrc = null;

			Map<String, String> sourceIdentifiers = (Map<String, String>) policiesEventObjMap.get("sourceIdentifiers");

			switch (eventType) {
			case PoliciesEventTypes.ClaimCreated:
				insuranceClaimRsrc = loadInsuranceClaimResource(policiesEventObjMap);
				createClaim(insuranceClaimRsrc);
				break;
			case PoliciesEventTypes.ClaimUpdated:
				insuranceClaimRsrc = loadInsuranceClaimResource(policiesEventObjMap);
				updateClaim(insuranceClaimRsrc);
				break;
			case PoliciesEventTypes.ClaimDeleted:
				String claimId = sourceIdentifiers.get("claimId");
				deleteClaim(claimId);
				break;
			case PoliciesEventTypes.PolicyUpdated:
				InsurancePolicyRsrc insurancePolicyRsrc = loadInsurancePolicyResource(policiesEventObjMap);
				updatePolicyData(insurancePolicyRsrc);
				break;
			case PoliciesEventTypes.GrowerUpdated:
				InsuredGrowerRsrc insuredGrowerRsrc = loadInsuredGrowerResource(policiesEventObjMap);
				updateGrowerData(insuredGrowerRsrc);
				break;
			case PoliciesEventTypes.CommodityVarietyCreated:
			case PoliciesEventTypes.CommodityVarietyUpdated:
				CommodityVarietySyncRsrc commodityVarietySyncRsrc = loadCommodityVarietyResource(policiesEventObjMap);
				synchronizeCommodityVariety(commodityVarietySyncRsrc);
				break;
			case PoliciesEventTypes.CommodityVarietyDeleted:
				String cropId = sourceIdentifiers.get("cropId");
				deleteCommodityVariety(cropId);
				break;
			case PoliciesEventTypes.CodeCreated:
			case PoliciesEventTypes.CodeUpdated:
			case PoliciesEventTypes.CodeDeleted:
				CodeSyncRsrc codeSyncRsrc = loadCodeResource(policiesEventObjMap);
				synchronizeCode(codeSyncRsrc);
				break;
			case PoliciesEventTypes.CoveragePerilCreated:
			case PoliciesEventTypes.CoveragePerilUpdated:
				CoveragePerilSyncRsrc coveragePerilSyncRsrc = loadCoveragePerilResource(policiesEventObjMap);
				synchronizeCoveragePeril(coveragePerilSyncRsrc);
				break;
			case PoliciesEventTypes.CoveragePerilDeleted:
				String coveragePerilId = sourceIdentifiers.get("coveragePerilId");
				deleteCoveragePeril(coveragePerilId);
				break;
			default:
				logger.debug("Ignoring message of type " + eventType);
				break;
			}

		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
			throw (t);
		}
		logger.debug(">processCirrasClaimEvent");
	}

	private void updatePolicyData(InsurancePolicyRsrc insurancePolicyRsrc)
			throws CirrasClaimServiceException, ValidationException {

		SyncClaimRsrc syncClaimRsrc = syncClaimFactory.getSyncClaimPolicyUpdateRsrc(insurancePolicyRsrc, eventDate,
				eventType);
		cirrasClaimService.updateSyncClaimRelatedData(syncClaimRsrc);

	}

	private void updateGrowerData(InsuredGrowerRsrc insuredGrowerRsrc)
			throws CirrasClaimServiceException, ValidationException {

		SyncClaimRsrc syncClaimRsrc = syncClaimFactory.getSyncClaimGrowerUpdateRsrc(insuredGrowerRsrc, eventDate,
				eventType);
		cirrasClaimService.updateSyncClaimRelatedData(syncClaimRsrc);

	}

	private void synchronizeCommodityVariety(CommodityVarietySyncRsrc commodityVarietySyncRsrc)
			throws CirrasClaimServiceException, ValidationException {
		
		SyncCommodityVarietyRsrc syncCommodityVarietyRsrc = syncClaimFactory.getSyncCommodityVarietyRsrc(commodityVarietySyncRsrc, eventDate, eventType);
		
		cirrasClaimService.synchronizeCommodityVariety(syncCommodityVarietyRsrc);
	}
	
	private void deleteCommodityVariety(String cropId)
			throws CirrasClaimServiceException, ValidationException {
		
		SyncCommodityVarietyRsrc syncCommodityVarietyRsrc = syncClaimFactory.getDeleteSyncCommodityVarietyRsrc(cropId, eventDate, eventType);
		
		cirrasClaimService.synchronizeCommodityVariety(syncCommodityVarietyRsrc);
	}

	private void synchronizeCode(CodeSyncRsrc codeSyncRsrc)
			throws CirrasClaimServiceException, ValidationException {
		
		SyncCodeRsrc syncCodeRsrc = syncClaimFactory.getSyncCodeRsrc(codeSyncRsrc, eventDate, eventType);
		
		cirrasClaimService.synchronizeCode(syncCodeRsrc);
	}

	private void synchronizeCoveragePeril(CoveragePerilSyncRsrc coveragePerilSyncRsrc)
			throws CirrasClaimServiceException, ValidationException {
		
		SyncCoveragePerilRsrc syncCoveragePerilRsrc = syncClaimFactory.getSyncCoveragePerilRsrc(coveragePerilSyncRsrc, eventDate, eventType);
		
		cirrasClaimService.synchronizeCoveragePeril(syncCoveragePerilRsrc);
	}
	
	private void deleteCoveragePeril(String coveragePerilId)
			throws CirrasClaimServiceException, ValidationException {
		
		SyncCoveragePerilRsrc syncCoveragePerilRsrc = syncClaimFactory.getDeleteSyncCoveragePerilRsrc(coveragePerilId, eventDate, eventType);
		
		cirrasClaimService.synchronizeCoveragePeril(syncCoveragePerilRsrc);
	}

	private void createClaim(InsuranceClaimRsrc insuranceClaimRsrc)
			throws CirrasClaimServiceException, ValidationException {

		SyncClaimRsrc syncClaimRsrc = syncClaimFactory.getSyncClaimRsrc(insuranceClaimRsrc, eventDate, eventType);
		cirrasClaimService.createSyncClaim(syncClaimRsrc);
	}

	private void updateClaim(InsuranceClaimRsrc insuranceClaimRsrc)
			throws CirrasClaimServiceException, ValidationException {

		// Get claims api resource to check if it exists
		SyncClaimRsrc syncClaimRsrc = getClaimFromCalculator(insuranceClaimRsrc.getClaimId().toString());

		if (syncClaimRsrc == null) {
			// no claim found = create new claim
			createClaim(insuranceClaimRsrc);
		} else {
			// Update existing claim
			syncClaimFactory.loadSyncClaimFromPolicyClaim(syncClaimRsrc, insuranceClaimRsrc, eventDate, eventType);
			syncClaimRsrc = cirrasClaimService.updateSyncClaim(syncClaimRsrc);
		}
	}

	private void deleteClaim(String claimId) throws CirrasClaimServiceException {
		SyncClaimRsrc syncClaimRsrc = getClaimFromCalculator(claimId);

		if (syncClaimRsrc != null) {
			// Delete existing claim and associated calculations
			cirrasClaimService.deleteSyncClaim(syncClaimRsrc);
		}
	}

	private InsuranceClaimRsrc loadInsuranceClaimResource(Map<String, Object> policiesEventObjMap)
			throws JsonProcessingException {

		return mapper.readValue(loadResourceAfterUpdateFromEventObject(policiesEventObjMap), InsuranceClaimRsrc.class);
	}

	private InsurancePolicyRsrc loadInsurancePolicyResource(Map<String, Object> policiesEventObjMap)
			throws JsonProcessingException {

		return mapper.readValue(loadResourceAfterUpdateFromEventObject(policiesEventObjMap), InsurancePolicyRsrc.class);
	}

	private InsuredGrowerRsrc loadInsuredGrowerResource(Map<String, Object> policiesEventObjMap)
			throws JsonProcessingException {

		return mapper.readValue(loadResourceAfterUpdateFromEventObject(policiesEventObjMap), InsuredGrowerRsrc.class);
	}

	private CommodityVarietySyncRsrc loadCommodityVarietyResource(Map<String, Object> policiesEventObjMap)
			throws JsonProcessingException {

		return mapper.readValue(loadResourceAfterUpdateFromEventObject(policiesEventObjMap), CommodityVarietySyncRsrc.class);
	}
	
	private CodeSyncRsrc loadCodeResource(Map<String, Object> policiesEventObjMap)
			throws JsonProcessingException {

		if(eventType.equals(PoliciesEventTypes.CodeDeleted)) {
			//Delete reads the before object
			return mapper.readValue(loadResourceBeforeUpdateFromEventObject(policiesEventObjMap), CodeSyncRsrc.class);
		} else {
			return mapper.readValue(loadResourceAfterUpdateFromEventObject(policiesEventObjMap), CodeSyncRsrc.class);
		}
	}	
	
	private CoveragePerilSyncRsrc loadCoveragePerilResource(Map<String, Object> policiesEventObjMap)
			throws JsonProcessingException {

		return mapper.readValue(loadResourceAfterUpdateFromEventObject(policiesEventObjMap), CoveragePerilSyncRsrc.class);
	}

	private String loadResourceAfterUpdateFromEventObject(Map<String, Object> policiesEventObjMap) throws JsonProcessingException {
		@SuppressWarnings("unchecked")
		Map<String, Object> resourceAfterUpdate = (Map<String, Object>) policiesEventObjMap.get("resourceAfterUpdate");
		String resourceAfterUpdateJson = mapper.writeValueAsString(resourceAfterUpdate);
		return resourceAfterUpdateJson;
	}

	private String loadResourceBeforeUpdateFromEventObject(Map<String, Object> policiesEventObjMap) throws JsonProcessingException {
		@SuppressWarnings("unchecked")
		Map<String, Object> resourceBeforeUpdate = (Map<String, Object>) policiesEventObjMap.get("resourceBeforeUpdate");
		String resourceBeforeUpdateJson = mapper.writeValueAsString(resourceBeforeUpdate);
		return resourceBeforeUpdateJson;
	}

	private SyncClaimRsrc getClaimFromCalculator(String claimId) throws CirrasClaimServiceException {
		// Get claims api resource to check if it exists
		return cirrasClaimService.getSyncClaim(getClaimServiceTopLevelEndpoints(), claimId);
	}

	private EndpointsRsrc getClaimServiceTopLevelEndpoints()
			throws CirrasClaimServiceException {
		return cirrasClaimService.getTopLevelEndpoints();
	}

}