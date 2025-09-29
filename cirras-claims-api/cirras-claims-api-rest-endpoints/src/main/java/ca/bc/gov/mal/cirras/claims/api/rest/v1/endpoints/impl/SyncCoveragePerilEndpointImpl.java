package ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.impl;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.SyncCoveragePerilEndpoint;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.SyncCoveragePerilRsrc;
import ca.bc.gov.mal.cirras.claims.service.api.v1.CirrasDataSyncService;

public class SyncCoveragePerilEndpointImpl extends BaseEndpointsImpl implements SyncCoveragePerilEndpoint {
	
	private static final Logger logger = LoggerFactory.getLogger(SyncCoveragePerilEndpointImpl.class);
	
	@Autowired
	private CirrasDataSyncService cirrasDataSyncService; 

	@Override
	public Response synchronizeCoveragePeril(SyncCoveragePerilRsrc resource) {
		logger.debug("<synchronizeCoveragePeril");
		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.UPDATE_SYNC_CLAIM)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {
			
			cirrasDataSyncService.synchronizeCoveragePeril(
					resource, 
					getFactoryContext(), 
					getWebAdeAuthentication());

			response = Response.status(200).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">synchronizeCoveragePeril " + response);
		return response;
	}

	@Override
	public Response deleteSyncCoveragePeril(String coveragePerilId) {
		logger.debug("<deleteSyncCoveragePeril");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.DELETE_SYNC_CLAIM)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			SyncCoveragePerilRsrc resource = (SyncCoveragePerilRsrc) cirrasDataSyncService.getSyncCoveragePeril(
					toInteger(coveragePerilId), 
					getFactoryContext(), 
					getWebAdeAuthentication());
			
			if(resource != null) {
				cirrasDataSyncService.deleteSyncCoveragePeril(
						toInteger(coveragePerilId), 
						getFactoryContext(), 
						getWebAdeAuthentication());
			}
			response = Response.status(204).build();
		} catch (NotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">deleteSyncCoveragePeril " + response);
		return response;
	}	

}
