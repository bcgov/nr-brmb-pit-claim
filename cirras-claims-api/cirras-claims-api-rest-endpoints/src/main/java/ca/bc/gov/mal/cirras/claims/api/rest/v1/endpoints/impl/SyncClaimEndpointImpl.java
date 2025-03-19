package ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.impl;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.common.wfone.rest.resource.HeaderConstants;
import ca.bc.gov.nrs.common.wfone.rest.resource.MessageListRsrc;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.ConflictException;
import ca.bc.gov.nrs.wfone.common.service.api.ForbiddenException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ValidationFailureException;
import ca.bc.gov.nrs.wfone.common.utils.HttpServletRequestHolder;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.SyncClaimEndpoint;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.SyncClaimRsrc;
import ca.bc.gov.mal.cirras.claims.service.api.v1.CirrasDataSyncService;

public class SyncClaimEndpointImpl extends BaseEndpointsImpl implements SyncClaimEndpoint {
	
	private static final Logger logger = LoggerFactory.getLogger(SyncClaimEndpointImpl.class);
	
	@Autowired
	private CirrasDataSyncService cirrasDataSyncService; 
	
	
	@Override
	public Response getSyncClaim(String colId) {
		
		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.GET_SYNC_CLAIM)) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		try {
			SyncClaimRsrc result = (SyncClaimRsrc) cirrasDataSyncService.getSyncClaim(
					toInteger(colId),
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
	public Response createSyncClaim(SyncClaimRsrc resource) {
		logger.debug("<createSyncClaim");
		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.CREATE_SYNC_CLAIM)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {
			SyncClaimRsrc result = (SyncClaimRsrc) cirrasDataSyncService.createSyncClaim(
					resource, 
					getFactoryContext(), 
					getWebAdeAuthentication());

			response = Response.ok(result).tag(result.getUnquotedETag()).build();

		} catch(ValidationFailureException e) {
			response = Response.status(Status.BAD_REQUEST).entity(new MessageListRsrc(e.getValidationErrors())).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">createSyncClaim " + response);
		return response;
	}	
	
	@Override
	public Response updateSyncClaim(SyncClaimRsrc resource) {
		logger.debug("<updateSyncClaim");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.UPDATE_SYNC_CLAIM)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			SyncClaimRsrc currentSyncClaim = (SyncClaimRsrc) cirrasDataSyncService.getSyncClaim(
					resource.getColId(),
					getFactoryContext(), 
					getWebAdeAuthentication());

			EntityTag currentTag = EntityTag.valueOf(currentSyncClaim.getQuotedETag());

			ResponseBuilder responseBuilder = this.evaluatePreconditions(currentTag);

			if (responseBuilder == null) {
				// Preconditions Are Met
				
				SyncClaimRsrc result = (SyncClaimRsrc) cirrasDataSyncService.updateSyncClaim(
						resource, 
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

		logger.debug(">updateSyncClaim " + response.getStatus());
		return response;
	}

	
	@Override
	public Response deleteSyncClaim(String colId) {
		logger.debug("<deleteSyncClaim");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.DELETE_SYNC_CLAIM)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			SyncClaimRsrc currentSyncClaim = (SyncClaimRsrc) cirrasDataSyncService.getSyncClaim(
					toInteger(colId),
					getFactoryContext(), 
					getWebAdeAuthentication());

			EntityTag currentTag = EntityTag.valueOf(currentSyncClaim.getQuotedETag());

			ResponseBuilder responseBuilder = this.evaluatePreconditions(currentTag);

			if (responseBuilder == null) {
				// Preconditions Are Met
				
				String optimisticLock = getIfMatchHeader();

				cirrasDataSyncService.deleteSyncClaim(
						toInteger(colId), 
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

		logger.debug(">deleteSyncClaim " + response);
		return response;
	}

	@Override
	protected ResponseBuilder evaluatePreconditions(EntityTag eTag) {
		logger.info("> evaluatePreconditions: Resource ETag " + eTag.getValue());
		
		HttpServletRequest httpServletRequest = HttpServletRequestHolder.getHttpServletRequest();
		if ( httpServletRequest != null ) {
		
			String requestIfMatch = httpServletRequest.getHeader(HeaderConstants.IF_MATCH_HEADER);
			logger.info("> evaluatePreconditions: Raw If-Match Header: " + requestIfMatch);
	
			if ( requestIfMatch != null && requestIfMatch.length() > 2 && requestIfMatch.startsWith("\"") && requestIfMatch.endsWith("\"") ) {
				// EntityTag cannot correctly parse a weak etag that is also surrounded by double-quotes, so remove them.
				String unquotedEtagValue = requestIfMatch.substring(1, requestIfMatch.length() - 1);
				EntityTag requestETag = EntityTag.valueOf(unquotedEtagValue);
				
				logger.info("> evaluatePreconditions: Request ETag " + requestETag.getValue());
				
				if (requestETag.getValue().equals(eTag.getValue())) {
					// If the request eTag and the resource eTag match, regardless of whether they are weak or strong, we can
					// assume preconditions on the check are met.
					logger.info("> evaluatePreconditions: etags matched.");
					return null;
				}
			}

		} else {
			// Probably can't happen.
			logger.warn("> evaluatePreconditions: could not retrieve httpServletRequest");
		}

		// otherwise, fall back to regular check.
		return super.evaluatePreconditions(eTag);
	}
	
}
