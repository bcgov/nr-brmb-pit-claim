package ca.bc.gov.mal.cirras.claims.listener.api.rest.v1.endpoints.test;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.claims.listener.api.rest.client.v1.CirrasClaimsListenerService;
import ca.bc.gov.mal.cirras.claims.listener.api.rest.client.v1.impl.CirrasClaimsListenerServiceImpl;
import ca.bc.gov.mal.cirras.claims.listener.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.common.wfone.rest.resource.HealthCheckResponseRsrc;
import ca.bc.gov.nrs.wfone.common.rest.client.RestClientServiceException;

public class CheckHealthEndpointsTest extends EndpointsTest {
	
	private static final Logger logger = LoggerFactory.getLogger(CheckHealthEndpointsTest.class);
	
	//@Test
	public void testHealthCheckEndpointDoesNotRequireCredentials() throws RestClientServiceException {
		logger.debug("<testHealthCheckEndpointDoesNotRequireCredentials");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		CirrasClaimsListenerService service = new CirrasClaimsListenerServiceImpl();
		((CirrasClaimsListenerServiceImpl) service).setTopLevelRestURL(topLevelRestURL);
		
		HealthCheckResponseRsrc healthCheckResponse = service.getHealthCheck("test");
		
		Assert.assertEquals("All dependencies reporting Green.", healthCheckResponse.getStatusDetails());
		
		logger.debug(">testHealthCheckEndpointDoesNotRequireCredentials");
	}
}
