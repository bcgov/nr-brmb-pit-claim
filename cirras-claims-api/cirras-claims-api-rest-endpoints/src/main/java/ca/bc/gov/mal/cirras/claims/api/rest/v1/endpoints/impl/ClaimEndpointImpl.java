package ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.impl;

import java.net.URI;

import jakarta.ws.rs.core.EntityTag;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.common.wfone.rest.resource.MessageListRsrc;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.ConflictException;
import ca.bc.gov.nrs.wfone.common.service.api.ForbiddenException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ValidationFailureException;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.ClaimEndpoint;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.ClaimCalculationRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.ClaimRsrc;
import ca.bc.gov.mal.cirras.claims.service.api.v1.CirrasClaimService;

public class ClaimEndpointImpl extends BaseEndpointsImpl implements ClaimEndpoint {
	
	private static final Logger logger = LoggerFactory.getLogger(ClaimEndpointImpl.class);
	
	@Autowired
	private CirrasClaimService cirrasClaimService;
	
	@Override
	public Response getClaim(String claimNumber) {
		logger.debug("<getClaim");
		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.CREATE_CALCULATION)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {

			ClaimCalculationRsrc result = (ClaimCalculationRsrc) cirrasClaimService.getClaim(
					claimNumber, 
					getFactoryContext(), 
					getWebAdeAuthentication());

			response = Response.ok(result).tag(result.getUnquotedETag()).build();


		} catch (NotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">getClaim " + response);
		return response;
	}	


}
