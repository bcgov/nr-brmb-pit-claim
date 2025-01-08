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
import ca.bc.gov.nrs.wfone.common.service.api.ConflictException;
import ca.bc.gov.nrs.wfone.common.service.api.ForbiddenException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.SyncCommodityVarietyEndpoint;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.SyncClaimRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.SyncCommodityVarietyRsrc;
import ca.bc.gov.mal.cirras.claims.service.api.v1.CirrasDataSyncService;

public class SyncCommodityVarietyEndpointImpl extends BaseEndpointsImpl implements SyncCommodityVarietyEndpoint {
	
	private static final Logger logger = LoggerFactory.getLogger(SyncCommodityVarietyEndpointImpl.class);
	
	@Autowired
	private CirrasDataSyncService cirrasDataSyncService; 

	@Override
	public Response synchronizeCommodityVariety(SyncCommodityVarietyRsrc resource) {
		logger.debug("<updateSyncClaimRelatedData");
		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.UPDATE_SYNC_CLAIM)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {
			
			cirrasDataSyncService.synchronizeCommodityVariety(
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

	@Override
	public Response deleteSyncCommodityVariety(String crptId) {
		logger.debug("<deleteSyncClaim");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.DELETE_SYNC_CLAIM)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			SyncCommodityVarietyRsrc resource = (SyncCommodityVarietyRsrc) cirrasDataSyncService.getSyncCommodityVariety(
					toInteger(crptId),
					getFactoryContext(), 
					getWebAdeAuthentication());
			
			if(resource != null) {
				cirrasDataSyncService.deleteSyncCommodityVariety(toInteger(crptId), getFactoryContext(), getWebAdeAuthentication());
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
