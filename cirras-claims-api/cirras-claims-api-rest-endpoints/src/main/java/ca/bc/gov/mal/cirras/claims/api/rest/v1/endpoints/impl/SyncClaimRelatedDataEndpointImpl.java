package ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.impl;

import java.net.URI;

import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.SyncClaimRelatedDataEndpoint;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.SyncClaimRsrc;
import ca.bc.gov.mal.cirras.claims.service.api.v1.CirrasDataSyncService;

public class SyncClaimRelatedDataEndpointImpl extends BaseEndpointsImpl implements SyncClaimRelatedDataEndpoint {
	
	private static final Logger logger = LoggerFactory.getLogger(SyncClaimRelatedDataEndpointImpl.class);
	
	@Autowired
	private CirrasDataSyncService cirrasDataSyncService; 

	@Override
	public Response updateSyncClaimRelatedData(SyncClaimRsrc resource) {
		logger.debug("<updateSyncClaimRelatedData");
		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.UPDATE_SYNC_CLAIM)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {
			
			cirrasDataSyncService.updateSyncClaimRelatedData(
					resource, 
					getFactoryContext(), 
					getWebAdeAuthentication());

			response = Response.status(200).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">updateSyncClaimRelatedData " + response);
		return response;
	}	

}
