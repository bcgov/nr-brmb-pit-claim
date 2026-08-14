package ca.bc.gov.mal.cirras.claims.controllers.publisher;

import java.util.Map;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.mal.cirras.claims.clients.CirrasClaimService;
import ca.bc.gov.mal.cirras.claims.clients.CirrasClaimServiceException;
import ca.bc.gov.mal.cirras.claims.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.claims.data.resources.EndpointsRsrc;
import ca.bc.gov.mal.cirras.claims.data.models.ClaimCalculationBerries;
import ca.bc.gov.mal.cirras.claims.data.resources.ClaimCalculationSimpleRsrc;
import ca.bc.gov.mal.cirras.claims.data.resources.ClaimEventTypes;
import ca.bc.gov.mal.cirras.claims.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class EventPublisherTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(EventPublisherTest.class);

	private static ObjectMapper mapper = new ObjectMapper();
	
	private static final String[] SCOPES = {
			Scopes.GET_TOP_LEVEL, 
	};

	private CirrasClaimService service;
	private EndpointsRsrc topLevelEndpoints;

		
	@Before
	public void prepareTests() throws CirrasClaimServiceException, Oauth2ClientException {

		service = getService(SCOPES);
		topLevelEndpoints = service.getTopLevelEndpoints();
	}	
	
	@Test
	public void testSimplePublish() throws EventPublisherException {
		logger.debug("<testSimplePublish");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		EventPublisher eventPublisher = (EventPublisher)webApplicationContext.getBean("eventPublisher");
		eventPublisher.publish("HelloWorld", null, null, null);
				
		logger.debug(">testSimplePublish");		
	}
	
	@Test
	public void testClaimCalculationBerriesEventPublish() throws Oauth2ClientException, EventPublisherException {
		logger.debug("<testClaimCalculationBerriesEventPublish");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//The unit test ClaimClaimsListenerTest.testClaimClaimsEventConsume in the Claims-Listener-api
		// is processing this message

		String claimEventType = ClaimEventTypes.ClaimCalculationBerriesUpdated;
		String claimCalculationGuid = "testClaimCalculationGuid";
		String claimCalculationBerriesGuid = "testClaimCalculationBerriesGuid";
		Integer contractId = 987654;
		Integer cropYear = 2026;
		Integer calculationVersion = 1;
		Integer cropCommodityId = 11111;
		String calculationStatusCode = "DRAFT";
		Double totalYieldForCalculation = 100.0;


		//Declared Yield Contract
		ClaimCalculationSimpleRsrc resource = new ClaimCalculationSimpleRsrc();
		resource.setContractId(contractId);
		resource.setCropYear(cropYear);
		resource.setClaimCalculationGuid(claimCalculationGuid);
		resource.setCalculationVersion(calculationVersion);
		resource.setCropCommodityId(cropCommodityId);
		resource.setCalculationStatusCode(calculationStatusCode);
		

		// Declared Yield Contract Commodity Berries
		ClaimCalculationBerries model = new ClaimCalculationBerries();

		model.setClaimCalculationBerriesGuid(claimCalculationBerriesGuid);
		model.setTotalYieldForCalculation(totalYieldForCalculation);

		resource.setClaimCalculationBerries(model);

		Map<String, String> sourceIdentifiers = new HashMap<>();
		sourceIdentifiers.put("claimCalculationBerriesGuid", model.getClaimCalculationBerriesGuid().toString());
		
		EventPublisher eventPublisher = (EventPublisher)webApplicationContext.getBean("eventPublisher");
		eventPublisher.publish(claimEventType, null, resource, sourceIdentifiers);
				
		logger.debug(">testClaimCalculationBerriesEventPublish");		
	}
}
