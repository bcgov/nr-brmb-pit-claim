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
import ca.bc.gov.mal.cirras.claims.data.resources.SyncDopYieldContractSimpleRsrc;
import ca.bc.gov.mal.cirras.claims.listener.service.api.v1.CirrasClaimsUnderwritingListenerService;
import ca.bc.gov.mal.cirras.claims.listener.service.api.v1.model.factory.SyncClaimsUnderwritingFactory;
import ca.bc.gov.mal.cirras.underwriting.data.resources.DopYieldContractSimpleRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.UnderwritingEventTypes;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;

public class CirrasClaimsUnderwritingListenerServiceImpl implements CirrasClaimsUnderwritingListenerService {

	private static final Logger logger = LoggerFactory.getLogger(CirrasClaimsUnderwritingListenerServiceImpl.class);

	private static ObjectMapper mapper = new ObjectMapper();

	// services
	private CirrasClaimService cirrasClaimService;

	// factories
	private SyncClaimsUnderwritingFactory syncClaimsUwFactory;

	private Date eventDate;
	private String eventType;

	public void setCirrasClaimService(CirrasClaimService cirrasClaimService) {
		this.cirrasClaimService = cirrasClaimService;
	}

	public void setSyncClaimsUnderwritingFactory(SyncClaimsUnderwritingFactory syncClaimsUwFactory) {
		this.syncClaimsUwFactory = syncClaimsUwFactory;
	}

	public void processCirrasClaimEvent(String eventMessageString, FactoryContext factoryContext) throws Throwable {
		logger.debug("<processCirrasClaimEvent");

		try {
			Map<String, Object> uwEventObjMap = null;
			try {
				uwEventObjMap = mapper.readValue(eventMessageString, new TypeReference<Map<String, Object>>() {
				});
			} catch (Throwable t) {
				logger.error("When attempting to unmarshal " + eventMessageString + ", saw " + t.getMessage(), t);
				throw (t);
			}

			eventType = (String) uwEventObjMap.get("eventType");
			Instant eventTimestamp = Instant.parse((String) uwEventObjMap.get("eventTimestamp"));
			eventDate = Date.from(eventTimestamp);

			@SuppressWarnings("unchecked")
			Map<String, String> sourceIdentifiers = (Map<String, String>) uwEventObjMap.get("sourceIdentifiers");

			switch (eventType) {
			case UnderwritingEventTypes.DopYieldContractCommodityBerriesCreated:
			case UnderwritingEventTypes.DopYieldContractCommodityBerriesUpdated:
				DopYieldContractSimpleRsrc dopYieldContractSimpleRsrc = loadDopYieldContractSimpleResource(uwEventObjMap);
				synchronizeDopYieldContract(dopYieldContractSimpleRsrc);
				break;
			case UnderwritingEventTypes.DopYieldContractCommodityBerriesDeleted:
				String declaredYieldContractCommodityBerriesGuid = sourceIdentifiers.get("declaredYieldContractCommodityBerriesGuid");
				deleteDopYieldContract(declaredYieldContractCommodityBerriesGuid);
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

	private void synchronizeDopYieldContract(DopYieldContractSimpleRsrc dopYieldContractSimpleRsrc)
			throws CirrasClaimServiceException, ValidationException, CirrasClaimServiceException, ValidationException {
		
		SyncDopYieldContractSimpleRsrc syncDopYieldContractSimpleRsrc = syncClaimsUwFactory.getSyncDopYieldContractSimpleRsrc(dopYieldContractSimpleRsrc, eventDate, eventType);
		
		cirrasClaimService.synchronizeDopYieldContractSimple(syncDopYieldContractSimpleRsrc);
	}
	
	private void deleteDopYieldContract(String declaredYieldContractCommodityBerriesGuid)
			throws CirrasClaimServiceException, ValidationException {
		
		SyncDopYieldContractSimpleRsrc syncDopYieldContractSimpleRsrc = syncClaimsUwFactory.getDeleteSyncDopYieldContractSimpleRsrc(declaredYieldContractCommodityBerriesGuid, eventDate, eventType);
		
		cirrasClaimService.synchronizeDopYieldContractSimple(syncDopYieldContractSimpleRsrc);
	}

	private DopYieldContractSimpleRsrc loadDopYieldContractSimpleResource(Map<String, Object> uwEventObjMap)
			throws JsonProcessingException {

		return mapper.readValue(loadResourceAfterUpdateFromEventObject(uwEventObjMap), DopYieldContractSimpleRsrc.class);
	}

	private String loadResourceAfterUpdateFromEventObject(Map<String, Object> uwEventObjMap) throws JsonProcessingException {
		@SuppressWarnings("unchecked")
		Map<String, Object> resourceAfterUpdate = (Map<String, Object>) uwEventObjMap.get("resourceAfterUpdate");
		String resourceAfterUpdateJson = mapper.writeValueAsString(resourceAfterUpdate);
		return resourceAfterUpdateJson;
	}

}