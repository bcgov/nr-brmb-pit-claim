package ca.bc.gov.mal.cirras.claims.controllers.impl;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.mal.cirras.claims.controllers.SyncCodeEndpoint;
import ca.bc.gov.mal.cirras.claims.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.claims.data.resources.SyncCodeRsrc;
import ca.bc.gov.mal.cirras.claims.services.CirrasDataSyncService;

public class SyncCodeEndpointImpl extends BaseEndpointsImpl implements SyncCodeEndpoint {
	
	private static final Logger logger = LoggerFactory.getLogger(SyncCodeEndpointImpl.class);
	
	@Autowired
	private CirrasDataSyncService cirrasDataSyncService; 

	@Override
	public Response synchronizeCode(SyncCodeRsrc resource) {
		logger.debug("<synchronizeCode");
		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.UPDATE_SYNC_CLAIM)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {
			
			cirrasDataSyncService.synchronizeCode(
					resource, 
					getFactoryContext(), 
					getWebAdeAuthentication());

			response = Response.status(200).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">synchronizeCode " + response);
		return response;
	}

	@Override
	public Response deleteSyncCode(String codeTableType, String uniqueKey) {
		logger.debug("<deleteSyncCode");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.DELETE_SYNC_CLAIM)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			SyncCodeRsrc resource = (SyncCodeRsrc) cirrasDataSyncService.getSyncCode(
					codeTableType,
					uniqueKey,
					getFactoryContext(), 
					getWebAdeAuthentication());
			
			if(resource != null) {
				cirrasDataSyncService.deleteSyncCode(codeTableType, uniqueKey, getFactoryContext(), getWebAdeAuthentication());
			}
			response = Response.status(204).build();
		} catch (NotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">deleteSyncClaim " + response);
		return response;
	}	

}
