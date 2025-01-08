package ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.impl;

import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.common.wfone.rest.resource.MessageListRsrc;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.ConflictException;
import ca.bc.gov.nrs.wfone.common.service.api.ForbiddenException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ValidationFailureException;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.ClaimCalculationEndpoint;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.ClaimCalculationRsrc;
import ca.bc.gov.mal.cirras.claims.service.api.v1.CirrasClaimService;

public class ClaimCalculationEndpointImpl extends BaseEndpointsImpl implements ClaimCalculationEndpoint {
	
	private static final Logger logger = LoggerFactory.getLogger(ClaimCalculationEndpointImpl.class);
	
	@Autowired
	private CirrasClaimService cirrasClaimService;
	
	
	@Override
	public Response getClaimCalculation(final String claimCalculationGuid, String doRefreshManualClaimData) {
		
		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.GET_CALCULATION)) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		try {
			ClaimCalculationRsrc result = (ClaimCalculationRsrc) cirrasClaimService.getClaimCalculation(
					claimCalculationGuid,
					toBoolean(doRefreshManualClaimData),
					getFactoryContext(), 
					getWebAdeAuthentication());
			response = Response.ok(result).tag(result.getUnquotedETag()).build();

		} catch (NotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
			
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		return response;
	}

	
	@Override
	public Response updateClaimCalculation(String claimCalculationGuid, String updateType, ClaimCalculationRsrc inputResouce) {
		logger.debug("<updateClaimCalculation");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.UPDATE_CALCULATION)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			ClaimCalculationRsrc currentClaimCalculation = (ClaimCalculationRsrc) cirrasClaimService.getClaimCalculation(
					claimCalculationGuid, 
					false,
					getFactoryContext(), 
					getWebAdeAuthentication());

			EntityTag currentTag = EntityTag.valueOf(currentClaimCalculation.getQuotedETag());

			ResponseBuilder responseBuilder = this.evaluatePreconditions(currentTag);

			if (responseBuilder == null) {
				// Preconditions Are Met
				
				String optimisticLock = getIfMatchHeader();

				ClaimCalculationRsrc result = (ClaimCalculationRsrc) cirrasClaimService.updateClaimCalculation(
						claimCalculationGuid,
						updateType,
						optimisticLock, 
						inputResouce, 
						getFactoryContext(), 
						getWebAdeAuthentication());

				response = Response.ok(result).tag(result.getUnquotedETag()).build();
				
			} else {
				// Preconditions Are NOT Met

				response = responseBuilder.tag(currentTag).build();
			}
		} catch (ForbiddenException e) {
			response = Response.status(Status.FORBIDDEN).build();
		} catch(ValidationFailureException e) {
			response = Response.status(Status.BAD_REQUEST).entity(new MessageListRsrc(e.getValidationErrors())).build();
		} catch (ConflictException e) {
			response = Response.status(Status.CONFLICT).entity(e.getMessage()).build();
		} catch (NotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">updateClaimCalculation " + response.getStatus());
		return response;
	}

	
	@Override
	public Response deleteClaimCalculation(String claimCalculationGuid) {
		logger.debug("<deleteClaimCalculation");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.DELETE_CLAIM)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			ClaimCalculationRsrc current = (ClaimCalculationRsrc) this.cirrasClaimService.getClaimCalculation(
					claimCalculationGuid, 
					false,
					getFactoryContext(), 
					getWebAdeAuthentication());

			EntityTag currentTag = EntityTag.valueOf(current.getQuotedETag());

			ResponseBuilder responseBuilder = this.evaluatePreconditions(currentTag);

			if (responseBuilder == null) {
				// Preconditions Are Met
				
				String optimisticLock = getIfMatchHeader();

				cirrasClaimService.deleteClaimCalculation(
						claimCalculationGuid, 
						optimisticLock, 
						getWebAdeAuthentication());

				response = Response.status(204).build();
			} else {
				// Preconditions Are NOT Met

				response = responseBuilder.tag(currentTag).build();
			}
		} catch (ForbiddenException e) {
			response = Response.status(Status.FORBIDDEN).build();
		} catch (ConflictException e) {
			response = Response.status(Status.CONFLICT).entity(e.getMessage()).build();
		} catch (NotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">deleteClaimCalculation " + response);
		return response;
	}
}
