package ca.bc.gov.mal.cirras.claims.listener.api.rest.client.v1;

import ca.bc.gov.nrs.common.wfone.rest.resource.HealthCheckResponseRsrc;
import ca.bc.gov.nrs.wfone.common.rest.client.RestClientServiceException;

public interface CirrasClaimsListenerService {
	
	String getSwaggerString() throws RestClientServiceException;

	HealthCheckResponseRsrc getHealthCheck(String callstack) throws RestClientServiceException;
}
