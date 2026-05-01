package ca.bc.gov.mal.cirras.claims.controllers;


import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ca.bc.gov.mal.cirras.claims.clients.CirrasClaimService;
import ca.bc.gov.mal.cirras.claims.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.claims.data.resources.EndpointsRsrc;
import ca.bc.gov.mal.cirras.claims.test.EndpointsTest;
import ca.bc.gov.nrs.common.wfone.rest.resource.HealthCheckResponseRsrc;
import ca.bc.gov.nrs.wfone.common.rest.client.RestClientServiceException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.resource.CheckedToken;


public class CheckControllerTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(CheckControllerTest.class);
	
	private static final String[] SCOPES = {
			Scopes.GET_TOP_LEVEL
		};
	
	@Test
	public void testCheckToken() throws Exception { 
		logger.debug("<testCheckToken");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasClaimService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		// should return some token as we logged in in getService(SCOPES);
		
		CheckedToken results = service.checkToken( topLevelEndpoints);
		Assert.assertNotNull("Result should not be null", results);
		
		logger.debug(">testCheckToken");
	}
	
	// TODO: test with no token
	@Test
	public void testNoAuthorizationUI() throws RestClientServiceException {
		logger.debug("<testNoAuthorization");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		CirrasClaimService service = new CirrasClaimService();
		((CirrasClaimService) service).setTopLevelRestURL(topLevelRestURL);
		
		CheckedToken healthCheckResponse = service.getCheckTokenNoAuth();
		
		Assert.assertNotNull(healthCheckResponse);
		
		logger.debug(">testNoAuthorization");
	}
}
