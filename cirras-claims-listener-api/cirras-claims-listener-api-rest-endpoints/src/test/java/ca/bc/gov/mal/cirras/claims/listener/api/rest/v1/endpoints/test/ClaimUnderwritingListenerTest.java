package ca.bc.gov.mal.cirras.claims.listener.api.rest.v1.endpoints.test;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.mal.cirras.claims.data.models.SyncDopYieldContractCommodityBerries;
import ca.bc.gov.mal.cirras.claims.data.resources.SyncDopYieldContractSimpleRsrc;
import ca.bc.gov.mal.cirras.claims.data.resources.UnderwritingSyncEventTypes;
import ca.bc.gov.mal.cirras.claims.listener.api.rest.test.EndpointsTest;
import ca.bc.gov.mal.cirras.claims.listener.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.claims.listener.policies.event.listener.v1.CirrasClaimsPoliciesEventListenerImpl;
import ca.bc.gov.mal.cirras.claims.listener.service.api.v1.CirrasClaimsPoliciesListenerService;
import ca.bc.gov.mal.cirras.claims.listener.service.api.v1.CirrasClaimsUnderwritingListenerService;
import ca.bc.gov.mal.cirras.claims.listener.service.api.v1.impl.CirrasClaimsPoliciesListenerServiceImpl;
import ca.bc.gov.mal.cirras.claims.listener.service.api.v1.impl.CirrasClaimsUnderwritingListenerServiceImpl;
import ca.bc.gov.mal.cirras.claims.listener.service.api.v1.model.factory.SyncClaimsUnderwritingFactory;
import ca.bc.gov.mal.cirras.claims.listener.underwriting.event.listener.v1.CirrasClaimsUnderwritingEventListenerImpl;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.InsuranceClaimRsrc;
import ca.bc.gov.mal.cirras.policies.model.v1.PoliciesEventTypes;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldContractCommodityBerries;
import ca.bc.gov.mal.cirras.underwriting.data.resources.DopYieldContractSimpleRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.UnderwritingEventTypes;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;

public class ClaimUnderwritingListenerTest extends EndpointsTest {
	
	private static final Logger logger = LoggerFactory.getLogger(ClaimUnderwritingListenerTest.class);

	private static ObjectMapper mapper = new ObjectMapper();
	
	private static final String[] SCOPES = {
			Scopes.GET_TOP_LEVEL,
			Scopes.CIRRAS_CLAIMS_SYNC_REST,
			Scopes.GET_SYNCRONIZATION_STATUS
		};

	private Object receivedLock = null;     // Use to synchronize access to receivedMessage and receivedCount by multiple threads.
	private String receivedMessage = null;
	private int receivedCount = 0;
	
	@Before
	public void prepareTests() {

		receivedLock = new Object();
		receivedMessage = null;
		receivedCount = 0;

	}
	
	@Test
	public void testClaimUnderwritingEventConsume() throws Throwable {
		logger.debug("<testClaimUnderwritingEventConsume");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//The unit test EventPublisherTest.testDopContractCommodityBerriesEventPublish in the underwriting-api
		// is publishing this message
		
		//Expected Values
		String underwritingEventType = UnderwritingEventTypes.DopYieldContractCommodityBerriesUpdated;
		Integer contractId = 987654;
		Integer cropYear = 2026;
		String declaredYieldContractGuid = "testDeclaredYieldContractGuid";
		String declaredYieldContractCommodityBerriesGuid = "testDopContractCommodityBerriesGuid";
		Integer cropCommodityId = 11111;
		String cropCommodityName = "testCommodity";
		Double totalProduction = 100.0;
		Double totalProductionOverride = 200.0;
		Double totalPlantedAcres = 10.0;
		Double totalMatureEquivalentAcres = 20.0;
		Double totalSoldShippedYield = 300.0;
		Double totalSalesYield = 400.0;
		Double totalAbandonmentYield = 500.0;
		
		String messageText = getMessage();

		Assert.assertNotNull("No message received", messageText);
		
		Map<String, Object> uwEventObjMap = mapper.readValue(messageText, new TypeReference<Map<String, Object>>() {});

		String eventType = (String)uwEventObjMap.get("eventType");
		Assert.assertEquals("Wrong event type", underwritingEventType, eventType);
		
		String eventTimestamp = (String)uwEventObjMap.get("eventTimestamp");
		Assert.assertNotNull(eventTimestamp);
			
		@SuppressWarnings("unchecked")
		Map<String, String> sourceIdentifiers = (Map<String, String>)uwEventObjMap.get("sourceIdentifiers");

		String dopContractCommodityBerriesGuid = sourceIdentifiers.get("declaredYieldContractCommodityBerriesGuid");
		Assert.assertEquals("Wrong Declared Yield Contract Commodity Berries Guid", declaredYieldContractCommodityBerriesGuid, dopContractCommodityBerriesGuid);

		Object resourceBeforeUpdate = (String)uwEventObjMap.get("resourceBeforeUpdate");
		Assert.assertNull(resourceBeforeUpdate);
		
		@SuppressWarnings("unchecked")
		Map<String, Object> resourceAfterUpdate = (Map<String, Object>) uwEventObjMap.get("resourceAfterUpdate");
		String resourceAfterUpdateJson = mapper.writeValueAsString(resourceAfterUpdate);

		DopYieldContractSimpleRsrc resource = mapper.readValue(resourceAfterUpdateJson, DopYieldContractSimpleRsrc.class);
		Assert.assertEquals("Wrong DopYieldContractSimple Resource", declaredYieldContractCommodityBerriesGuid, resource.getDopYieldContractCommodityBerries().getDeclaredYieldContractCommodityBerriesGuid());
		
		DopYieldContractCommodityBerries model = resource.getDopYieldContractCommodityBerries();

		//Check all values
		//dop yield contract
		Assert.assertEquals("DeclaredYieldContractGuid", resource.getDeclaredYieldContractGuid(), declaredYieldContractGuid);
		Assert.assertEquals("ContractId", resource.getContractId(), contractId);
		Assert.assertEquals("CropYear", resource.getCropYear(), cropYear);

		//dop yield contract commodity berries
		Assert.assertEquals("DeclaredYieldContractCommodityBerriesGuid", model.getDeclaredYieldContractCommodityBerriesGuid(), declaredYieldContractCommodityBerriesGuid);
		Assert.assertEquals("DeclaredYieldContractGuid", model.getDeclaredYieldContractGuid(), declaredYieldContractGuid);
		Assert.assertEquals("CropCommodityId", model.getCropCommodityId(), cropCommodityId);
		Assert.assertEquals("CropCommodityName", model.getCropCommodityName(), cropCommodityName);
		Assert.assertEquals("TotalProduction", model.getTotalProduction(), totalProduction);
		Assert.assertEquals("TotalProductionOverride", model.getTotalProductionOverride(), totalProductionOverride);
		Assert.assertEquals("TotalPlantedAcres", model.getTotalPlantedAcres(), totalPlantedAcres);
		Assert.assertEquals("TotalMatureEquivalentAcres", model.getTotalMatureEquivalentAcres(), totalMatureEquivalentAcres);
		Assert.assertEquals("TotalSoldShippedYield", model.getTotalSoldShippedYield(), totalSoldShippedYield);
		Assert.assertEquals("TotalSalesYield", model.getTotalSalesYield(), totalSalesYield);
		Assert.assertEquals("TotalAbandonmentYield", model.getTotalAbandonmentYield(), totalAbandonmentYield);
		
		logger.debug(">testClaimUnderwritingEventConsume");
	}
	
	
	@Test
	public void testSyncClaimsUnderwritingFactoryUpdate() throws Throwable {
		
		logger.debug("<testSyncClaimsUnderwritingFactoryUpdate");

		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		String underwritingEventType = UnderwritingEventTypes.DopYieldContractCommodityBerriesUpdated;
		String claimsUnderwritingEventType =  UnderwritingSyncEventTypes.DopYieldContractCommodityBerriesUpdated;
		Integer contractId = 987654;
		Integer cropYear = 2026;
		String declaredYieldContractGuid = "testDeclaredYieldContractGuid";
		String declaredYieldContractCommodityBerriesGuid = "testDopContractCommodityBerriesGuid";
		Integer cropCommodityId = 11111;
		String cropCommodityName = "testCommodity";
		Double totalProduction = 100.0;
		Double totalProductionOverride = 200.0;
		Double totalPlantedAcres = 10.0;
		Double totalMatureEquivalentAcres = 20.0;
		Double totalSoldShippedYield = 300.0;
		Double totalSalesYield = 400.0;
		Double totalAbandonmentYield = 500.0;

		//Declared Yield Contract
		DopYieldContractSimpleRsrc resource = new DopYieldContractSimpleRsrc();
		resource.setContractId(contractId);
		resource.setCropYear(cropYear);
		resource.setDeclaredYieldContractGuid(declaredYieldContractGuid);

		// Declared Yield Contract Commodity Berries
		DopYieldContractCommodityBerries model = new DopYieldContractCommodityBerries();
		model.setDeclaredYieldContractCommodityBerriesGuid(declaredYieldContractCommodityBerriesGuid);
		model.setCropCommodityId(cropCommodityId);
		model.setCropCommodityName(cropCommodityName);
		model.setDeclaredYieldContractGuid(declaredYieldContractGuid);
		model.setTotalProduction(totalProduction);
		model.setTotalProductionOverride(totalProductionOverride);
		model.setTotalPlantedAcres(totalPlantedAcres);
		model.setTotalMatureEquivalentAcres(totalMatureEquivalentAcres);
		model.setTotalSoldShippedYield(totalSoldShippedYield);
		model.setTotalSalesYield(totalSalesYield);
		model.setTotalAbandonmentYield(totalAbandonmentYield);

		resource.setDopYieldContractCommodityBerries(model);
		
		SyncClaimsUnderwritingFactory syncClaimsUwFactory = (SyncClaimsUnderwritingFactory)webApplicationContext.getBean("syncClaimsUnderwritingRsrcFactory");

		Instant timestamp = Instant.now();
		Date eventDate = Date.from(timestamp);
		SyncDopYieldContractSimpleRsrc syncDopYieldContractSimpleRsrc = syncClaimsUwFactory.getSyncDopYieldContractSimpleRsrc(resource, eventDate, underwritingEventType);

		//dop yield contract simple
		Assert.assertEquals("DeclaredYieldContractGuid", syncDopYieldContractSimpleRsrc.getDeclaredYieldContractGuid(), declaredYieldContractGuid);
		Assert.assertEquals("ContractId", syncDopYieldContractSimpleRsrc.getContractId(), contractId);
		Assert.assertEquals("CropYear", syncDopYieldContractSimpleRsrc.getCropYear(), cropYear);
		Assert.assertEquals("DataSyncTransDate", syncDopYieldContractSimpleRsrc.getDataSyncTransDate(), eventDate);
		Assert.assertEquals("TransactionType", syncDopYieldContractSimpleRsrc.getTransactionType(), claimsUnderwritingEventType);

		//dop yield contract commodity berries
		SyncDopYieldContractCommodityBerries sdyccbModel = syncDopYieldContractSimpleRsrc.getSyncDopYieldContractCommodityBerries();
		Assert.assertEquals("DeclaredYieldContractCommodityBerriesGuid", sdyccbModel.getDeclaredYieldContractCommodityBerriesGuid(), declaredYieldContractCommodityBerriesGuid);
		Assert.assertEquals("CropCommodityId", sdyccbModel.getCropCommodityId(), cropCommodityId);
		Assert.assertEquals("CropCommodityName", sdyccbModel.getCropCommodityName(), cropCommodityName);
		Assert.assertEquals("TotalProduction", sdyccbModel.getTotalProduction(), totalProduction);
		Assert.assertEquals("TotalProductionOverride", sdyccbModel.getTotalProductionOverride(), totalProductionOverride);
		Assert.assertEquals("TotalSoldShippedYield", sdyccbModel.getTotalSoldShippedYield(), totalSoldShippedYield);
		Assert.assertEquals("TotalSalesYield", sdyccbModel.getTotalSalesYield(), totalSalesYield);
		Assert.assertEquals("TotalAbandonmentYield", sdyccbModel.getTotalAbandonmentYield(), totalAbandonmentYield);
		
		
		logger.debug(">testSyncClaimsUnderwritingFactoryUpdate");
	}
	
	@Test
	public void testSyncClaimsUnderwritingFactoryDelete() throws Throwable {
		
		logger.debug("<testSyncClaimsUnderwritingFactoryDelete");

		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		String underwritingEventType = UnderwritingEventTypes.DopYieldContractCommodityBerriesDeleted;
		String claimsUnderwritingEventType =  UnderwritingSyncEventTypes.DopYieldContractCommodityBerriesDeleted;
		String declaredYieldContractCommodityBerriesGuid = "testDopContractCommodityBerriesGuid";
		
		SyncClaimsUnderwritingFactory syncClaimsUwFactory = (SyncClaimsUnderwritingFactory)webApplicationContext.getBean("syncClaimsUnderwritingRsrcFactory");

		Instant timestamp = Instant.now();
		Date eventDate = Date.from(timestamp);
		SyncDopYieldContractSimpleRsrc syncDopYieldContractSimpleRsrc = syncClaimsUwFactory.getDeleteSyncDopYieldContractSimpleRsrc(declaredYieldContractCommodityBerriesGuid, eventDate, underwritingEventType);

		//dop yield contract simple
		Assert.assertEquals("DataSyncTransDate", syncDopYieldContractSimpleRsrc.getDataSyncTransDate(), eventDate);
		Assert.assertEquals("TransactionType", syncDopYieldContractSimpleRsrc.getTransactionType(), claimsUnderwritingEventType);

		//dop yield contract commodity berries
		Assert.assertEquals("DeclaredYieldContractCommodityBerriesGuid", syncDopYieldContractSimpleRsrc.getSyncDopYieldContractCommodityBerries().getDeclaredYieldContractCommodityBerriesGuid(), declaredYieldContractCommodityBerriesGuid);
		
		logger.debug(">testSyncClaimsUnderwritingFactoryDelete");
	}
	
	// Starts the event listener in another thread, waits for it to consume the message from the queue, stops the listener and returns the received 
	// message. It will not be sent to the Claims API.
	private String getMessage() throws InterruptedException {

		String messageStr = null;

		CirrasClaimsUnderwritingEventListenerImpl listener = (CirrasClaimsUnderwritingEventListenerImpl)webApplicationContext.getBean("underwritingEventListener");

		Assert.assertNotNull(listener);

		// Replace service with stub that just receives the message so it can be checked, rather than actually process it.
		listener.setCirrasClaimsUnderwritingListenerService(new CirrasClaimsUnderwritingListenerService() {
			
			@Override
			public void processCirrasClaimEvent(String eventMessageString, FactoryContext factoryContext)
					throws ServiceException, Throwable {
				synchronized (receivedLock) {
					receivedMessage = eventMessageString;
					receivedCount++;
				}
			}
		});
		
		listener.startListening();

		// Wait 15 seconds for the event listener to receive the message in its thread then transfer it here.
		synchronized (this) {
			this.wait(15*1000); //15 seconds
			//this.wait(10*60*1000); //10 minutes
		}
		
		listener.stopListening();

		synchronized (receivedLock) {
			Assert.assertEquals(1, receivedCount);			
			Assert.assertNotNull(receivedMessage);
			messageStr = receivedMessage;
		}		

		return messageStr;
	}

}
