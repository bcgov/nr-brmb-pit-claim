package ca.bc.gov.mal.cirras.claims.controllers;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.common.wfone.rest.resource.HeaderConstants;
import ca.bc.gov.nrs.common.wfone.rest.resource.MessageListRsrc;
import ca.bc.gov.nrs.wfone.common.model.Message;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
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
import ca.bc.gov.mal.cirras.claims.controllers.parameters.PagingQueryParameters;
import ca.bc.gov.mal.cirras.claims.controllers.parameters.validation.ParameterValidator;
import ca.bc.gov.mal.cirras.claims.data.resources.ClaimCalculationListRsrc;
import ca.bc.gov.mal.cirras.claims.data.resources.ClaimCalculationRsrc;
import ca.bc.gov.mal.cirras.claims.services.CirrasClaimService;

@Path("/calculations")
public class ClaimCalculationListEndpoint extends BaseEndpointsImpl {
	
	private static final Logger logger = LoggerFactory.getLogger(ClaimCalculationListEndpoint.class);
	
	@Autowired
	private CirrasClaimService cirrasClaimService;

	@Autowired
	private ParameterValidator parameterValidator;
	
	
	public void setCirrasClaimService(CirrasClaimService cirrasClaimService) {
		this.cirrasClaimService = cirrasClaimService;
	}

	public void setParameterValidator(ParameterValidator parameterValidator) {
		this.parameterValidator = parameterValidator;
	}
	

	@Operation(operationId = "Get list of calculations", summary = "Get list of calculations", security = @SecurityRequirement(name = "Webade-OAUTH2", scopes = {Scopes.SEARCH_CALCULATIONS}), extensions = {@Extension(properties = {@ExtensionProperty(name = "auth-type", value = "#{wso2.x-auth-type.none}"), @ExtensionProperty(name = "throttling-tier", value = "Unlimited") })})
	@Parameters({
		@Parameter(name = HeaderConstants.REQUEST_ID_HEADER, description = HeaderConstants.REQUEST_ID_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.VERSION_HEADER, description = HeaderConstants.VERSION_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = Integer.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.CACHE_CONTROL_HEADER, description = HeaderConstants.CACHE_CONTROL_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.PRAGMA_HEADER, description = HeaderConstants.PRAGMA_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.AUTHORIZATION_HEADER, description = HeaderConstants.AUTHORIZATION_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER) 
	})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ClaimCalculationListRsrc.class)), headers = @Header(name = HeaderConstants.ETAG_HEADER, schema = @Schema(implementation = String.class), description = HeaderConstants.ETAG_DESCRIPTION)),
		@ApiResponse(responseCode = "404", description = "Not Found"),
		@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = MessageListRsrc.class))) 
	})
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getClaimCalculationList(
		@Parameter(description = "Filter the results by the claim number") @QueryParam("claimNumber") String claimNumber,
		@Parameter(description = "Filter the results by the policy number") @QueryParam("policyNumber") String policyNumber,
		@Parameter(description = "Filter the results by the year") @QueryParam("cropYear") String cropYear,
		@Parameter(description = "Filter the results by the calculation status code") @QueryParam("calculationStatusCode") String calculationStatusCode,
		@Parameter(description = "Filter the results by the created by user") @QueryParam("createClaimCalcUserGuid") String createClaimCalcUserGuid,
		@Parameter(description = "Filter the results by the last updated by user") @QueryParam("updateClaimCalcUserGuid") String updateClaimCalcUserGuid,
		@Parameter(description = "Filter the results by the insurance plan") @QueryParam("insurancePlanId") String insurancePlanId,
		@Parameter(description = "Sort by column") @QueryParam("sortColumn") String sortColumn,
		@Parameter(description = "Sort direction") @QueryParam("sortDirection") String sortDirection,
		@Parameter(description = "The page number of the results to be returned") @QueryParam("pageNumber") String pageNumber,		
		@Parameter(description = "The number of results per page.") @QueryParam("pageRowCount") String pageRowCount
	) {
		
		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.SEARCH_CALCULATIONS)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {
			
			PagingQueryParameters parameters = new PagingQueryParameters();

			parameters.setPageNumber(pageNumber);
			parameters.setPageRowCount(pageRowCount);
			
			List<Message> validation = new ArrayList<>();
			validation.addAll(this.parameterValidator.validatePagingQueryParameters(parameters));
			
			MessageListRsrc validationMessages = new MessageListRsrc(validation);
			if (validationMessages.hasMessages()) {
				response = Response.status(Status.BAD_REQUEST).entity(validationMessages).build();
			} else {
				
				ClaimCalculationListRsrc results = (ClaimCalculationListRsrc) cirrasClaimService.getClaimCalculationList(
						toInteger(claimNumber), 
						toString(policyNumber),
						toInteger(cropYear),
						toString(calculationStatusCode),
						toString(createClaimCalcUserGuid),
						toString(updateClaimCalcUserGuid),
						toInteger(insurancePlanId),
						toString(sortColumn),
						toString(sortDirection),
						toInteger(pageNumber), 
						toInteger(pageRowCount), 
						getFactoryContext(), 
						getWebAdeAuthentication());

				GenericEntity<ClaimCalculationListRsrc> entity = new GenericEntity<ClaimCalculationListRsrc>(results) {
					/* do nothing */
				};

				response = Response.ok(entity).tag(results.getUnquotedETag()).build();
			}
			
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		return response;
	}

	@Operation(operationId = "Add a new calculation", security = @SecurityRequirement(name = "Webade-OAUTH2", scopes = {Scopes.CREATE_CALCULATION}), summary = "Add a new calculation", extensions = {@Extension(properties = {@ExtensionProperty(name = "auth-type", value = "#{wso2.x-auth-type.none}"), @ExtensionProperty(name = "throttling-tier", value = "Unlimited") })})
	@Parameters({
		@Parameter(name = HeaderConstants.REQUEST_ID_HEADER, description = HeaderConstants.REQUEST_ID_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.VERSION_HEADER, description = HeaderConstants.VERSION_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = Integer.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.CACHE_CONTROL_HEADER, description = HeaderConstants.CACHE_CONTROL_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.PRAGMA_HEADER, description = HeaderConstants.PRAGMA_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.AUTHORIZATION_HEADER, description = HeaderConstants.AUTHORIZATION_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER) 
	})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = ClaimCalculationRsrc.class)), headers = {
			@Header(name = HeaderConstants.ETAG_HEADER, schema = @Schema(implementation = String.class), description = HeaderConstants.ETAG_DESCRIPTION),
			@Header(name = HeaderConstants.LOCATION_HEADER, schema = @Schema(implementation = String.class), description = HeaderConstants.LOCATION_DESCRIPTION) }),
		@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
		@ApiResponse(responseCode = "403", description = "Forbidden"),
		@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = MessageListRsrc.class))) 
	})
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response createClaimCalculation(
			@Parameter(name = "calculation", description = "The claim calculation resource containing the new values.", required = true) ClaimCalculationRsrc claim
	) {
		logger.debug("<createClaimCalculation");
		Response response = null;
		
		logRequest();
		
		//if(!hasAuthority(Scopes.CREATE_CALCULATION)) {
		//	return Response.status(Status.FORBIDDEN).build();
		//}

		try {

			ClaimCalculationRsrc result = (ClaimCalculationRsrc) cirrasClaimService.createClaimCalculation(
					claim, 
					getFactoryContext(), 
					getWebAdeAuthentication());

			URI createdUri = URI.create(result.getSelfLink());

			response = Response.created(createdUri).entity(result).tag(result.getUnquotedETag()).build();

		} catch(ValidationFailureException e) {
			response = Response.status(Status.BAD_REQUEST).entity(new MessageListRsrc(e.getValidationErrors())).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">createClaimCalculation " + response);
		return response;
	}
}
