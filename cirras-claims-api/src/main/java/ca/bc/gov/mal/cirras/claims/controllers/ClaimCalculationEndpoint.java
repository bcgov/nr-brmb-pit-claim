package ca.bc.gov.mal.cirras.claims.controllers;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.EntityTag;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.common.wfone.rest.resource.HeaderConstants;
import ca.bc.gov.nrs.common.wfone.rest.resource.MessageListRsrc;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.ConflictException;
import ca.bc.gov.nrs.wfone.common.service.api.ForbiddenException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.service.api.ValidationFailureException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import ca.bc.gov.mal.cirras.claims.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.claims.data.resources.ClaimCalculationRsrc;
import ca.bc.gov.mal.cirras.claims.services.CirrasClaimService;

@Path("/calculations/{claimCalculationGuid}")
public class ClaimCalculationEndpoint extends BaseEndpointsImpl {
	
	private static final Logger logger = LoggerFactory.getLogger(ClaimCalculationEndpoint.class);
	
	@Autowired
	private CirrasClaimService cirrasClaimService;
	
	
	@Operation(operationId = "Get the calculation.", summary = "Get the calculation", security = @SecurityRequirement(name = "Webade-OAUTH2", scopes = {Scopes.GET_CALCULATION}), extensions = {@Extension(properties = {@ExtensionProperty(name = "auth-type", value = "#{wso2.x-auth-type.none}"), @ExtensionProperty(name = "throttling-tier", value = "Unlimited") })})
	@Parameters({
		@Parameter(name = HeaderConstants.REQUEST_ID_HEADER, description = HeaderConstants.REQUEST_ID_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.VERSION_HEADER, description = HeaderConstants.VERSION_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = Integer.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.CACHE_CONTROL_HEADER, description = HeaderConstants.CACHE_CONTROL_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.PRAGMA_HEADER, description = HeaderConstants.PRAGMA_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.AUTHORIZATION_HEADER, description = HeaderConstants.AUTHORIZATION_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER) 
	})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ClaimCalculationRsrc.class)), headers = @Header(name = HeaderConstants.ETAG_HEADER, schema = @Schema(implementation = String.class), description = HeaderConstants.ETAG_DESCRIPTION)),
		@ApiResponse(responseCode = "404", description = "Not Found"),
		@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = MessageListRsrc.class))) })
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getClaimCalculation(
		@Parameter(description = "The GUID of the claim.") @PathParam("claimCalculationGuid") String claimCalculationGuid,
		@Parameter(description = "Refresh Manual Claim Data") @QueryParam("doRefreshManualClaimData") String doRefreshManualClaimData
	) {
		
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

	@Operation(operationId = "Update calculation", summary = "Update claim", security = @SecurityRequirement(name = "Webade-OAUTH2", scopes = {Scopes.UPDATE_CALCULATION}),  extensions = {@Extension(properties = {@ExtensionProperty(name = "auth-type", value = "#{wso2.x-auth-type.none}"), @ExtensionProperty(name = "throttling-tier", value = "Unlimited") })})
	@Parameters({
		@Parameter(name = HeaderConstants.REQUEST_ID_HEADER, description = HeaderConstants.REQUEST_ID_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.VERSION_HEADER, description = HeaderConstants.VERSION_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = Integer.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.CACHE_CONTROL_HEADER, description = HeaderConstants.CACHE_CONTROL_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.PRAGMA_HEADER, description = HeaderConstants.PRAGMA_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.AUTHORIZATION_HEADER, description = HeaderConstants.AUTHORIZATION_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.IF_MATCH_HEADER, description = HeaderConstants.IF_MATCH_DESCRIPTION, required = true, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER) 
	})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ClaimCalculationRsrc.class)), headers = @Header(name = HeaderConstants.ETAG_HEADER, schema = @Schema(implementation = String.class), description = HeaderConstants.ETAG_DESCRIPTION)),
		@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
		@ApiResponse(responseCode = "403", description = "Forbidden"),
		@ApiResponse(responseCode = "404", description = "Not Found"),
		@ApiResponse(responseCode = "409", description = "Conflict", content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
		@ApiResponse(responseCode = "412", description = "Precondition Failed"),
		@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = MessageListRsrc.class))) })
	@PUT
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateClaimCalculation(
			@Parameter(description = "The GUID of the claim resource.") @PathParam("claimCalculationGuid") String claimCalculationGuid,
			@Parameter(description = "Indicates if other actions than updating a calculation needs to be performed i.e. if it's replaced (REPLACE_NEW, REPLACE_COPY).") @QueryParam("updateType") String updateType,
			@Parameter(name = "calculation", description = "The claim calculation resource containing the new values.", required = true) ClaimCalculationRsrc claim
	) {
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
						claim, 
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

	@Operation(operationId = "Delete claim", summary = "Delete claim", security = @SecurityRequirement(name = "Webade-OAUTH2", scopes = {Scopes.DELETE_CLAIM}), extensions = {@Extension(properties = {@ExtensionProperty(name = "auth-type", value = "#{wso2.x-auth-type.none}"), @ExtensionProperty(name = "throttling-tier", value = "Unlimited") })})
	@Parameters({
		@Parameter(name = HeaderConstants.REQUEST_ID_HEADER, description = HeaderConstants.REQUEST_ID_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.VERSION_HEADER, description = HeaderConstants.VERSION_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = Integer.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.CACHE_CONTROL_HEADER, description = HeaderConstants.CACHE_CONTROL_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.PRAGMA_HEADER, description = HeaderConstants.PRAGMA_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.AUTHORIZATION_HEADER, description = HeaderConstants.AUTHORIZATION_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.IF_MATCH_HEADER, description = HeaderConstants.IF_MATCH_DESCRIPTION, required = true, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER)
	})
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "No Content"),
		@ApiResponse(responseCode = "403", description = "Forbidden"),
		@ApiResponse(responseCode = "404", description = "Not Found"),
		@ApiResponse(responseCode = "409", description = "Conflict"),
		@ApiResponse(responseCode = "412", description = "Precondition Failed"),
		@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = MessageListRsrc.class))) })
	@DELETE
	public Response deleteClaimCalculation(
			@Parameter(description = "The GUID of the claim calculation resource.") @PathParam("claimCalculationGuid") String claimCalculationGuid,
			@Parameter(description = "Indicates if linked calculation are deleted as well") @QueryParam("doDeleteLinkedCalculations") String doDeleteLinkedCalculations
	) {
		logger.debug("<deleteClaimCalculation");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.DELETE_CLAIM)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			//Throw an error if flag is not set to true
			if(Boolean.TRUE.equals(toBoolean(doDeleteLinkedCalculations))) {
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
			} else {
				String msg = "doDeleteLinkedCalculations has to be set to true. All linked calculations need to be deleted at the same time";
				logger.info(msg);
				throw new ServiceException(msg);
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
