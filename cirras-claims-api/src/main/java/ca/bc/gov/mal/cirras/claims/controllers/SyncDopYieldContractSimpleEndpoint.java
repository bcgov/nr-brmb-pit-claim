package ca.bc.gov.mal.cirras.claims.controllers;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.common.wfone.rest.resource.HeaderConstants;
import ca.bc.gov.nrs.common.wfone.rest.resource.MessageListRsrc;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
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
import ca.bc.gov.mal.cirras.claims.data.resources.SyncDopYieldContractSimpleRsrc;
import ca.bc.gov.mal.cirras.claims.services.CirrasDataSyncService;


@Path("/syncDopYieldContractSimple")
public class SyncDopYieldContractSimpleEndpoint extends BaseEndpointsImpl {
	
	private static final Logger logger = LoggerFactory.getLogger(SyncDopYieldContractSimpleEndpoint.class);
	
	@Autowired
	private CirrasDataSyncService cirrasDataSyncService; 

	@Operation(operationId = "Insert or Update dop", summary = "Insert or Update dop", security = @SecurityRequirement(name = "Webade-OAUTH2", scopes = {Scopes.UPDATE_SYNC_CLAIM}),  extensions = {@Extension(properties = {@ExtensionProperty(name = "auth-type", value = "#{wso2.x-auth-type.none}"), @ExtensionProperty(name = "throttling-tier", value = "Unlimited") })})
	@Parameters({
		@Parameter(name = HeaderConstants.REQUEST_ID_HEADER, description = HeaderConstants.REQUEST_ID_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.VERSION_HEADER, description = HeaderConstants.VERSION_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = Integer.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.CACHE_CONTROL_HEADER, description = HeaderConstants.CACHE_CONTROL_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.PRAGMA_HEADER, description = HeaderConstants.PRAGMA_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.AUTHORIZATION_HEADER, description = HeaderConstants.AUTHORIZATION_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER) 
	})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "No Content"),	
		@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
		@ApiResponse(responseCode = "403", description = "Forbidden"),
		@ApiResponse(responseCode = "404", description = "Not Found"),
		@ApiResponse(responseCode = "409", description = "Conflict", content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
		@ApiResponse(responseCode = "412", description = "Precondition Failed"),
		@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = MessageListRsrc.class))) })
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response synchronizeDopYieldContractSimple(
		@Parameter(name = "syncDopYieldContractSimple", description = "The dop resource containing the values from CUWS.", required = true) SyncDopYieldContractSimpleRsrc resource
	) {
		logger.debug("<synchronizeDopYieldContractSimple");
		Response response = null;
		
		logRequest();
		logResource(resource, "synchronizeDopYieldContractSimple");
		
		if(!hasAuthority(Scopes.UPDATE_SYNC_CLAIM)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {

			cirrasDataSyncService.synchronizeDopYieldContractSimple(
					resource, 
					getFactoryContext(), 
					getWebAdeAuthentication());

			response = Response.status(204).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">synchronizeDopYieldContractSimple " + response);
		return response;
	}

	@Operation(operationId = "Get dop", summary = "Get dop", security = @SecurityRequirement(name = "Webade-OAUTH2", scopes = {Scopes.GET_SYNC_CLAIM}), extensions = {@Extension(properties = {@ExtensionProperty(name = "auth-type", value = "#{wso2.x-auth-type.none}"), @ExtensionProperty(name = "throttling-tier", value = "Unlimited") })})
	@Parameters({
		@Parameter(name = HeaderConstants.REQUEST_ID_HEADER, description = HeaderConstants.REQUEST_ID_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.VERSION_HEADER, description = HeaderConstants.VERSION_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = Integer.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.CACHE_CONTROL_HEADER, description = HeaderConstants.CACHE_CONTROL_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.PRAGMA_HEADER, description = HeaderConstants.PRAGMA_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.AUTHORIZATION_HEADER, description = HeaderConstants.AUTHORIZATION_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER) 
	})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = SyncDopYieldContractSimpleRsrc.class)), headers = @Header(name = HeaderConstants.ETAG_HEADER, schema = @Schema(implementation = String.class), description = HeaderConstants.ETAG_DESCRIPTION)),
		@ApiResponse(responseCode = "404", description = "Not Found"),
		@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = MessageListRsrc.class))) })
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getSyncDopYieldContractSimpleRsrc(
		@Parameter(description = "The guid of the dop yield contract commodity berries in CUWS.") @QueryParam("declaredYieldContractCommodityBerriesGuid") String declaredYieldContractCommodityBerriesGuid
	){
		logger.debug("<getSyncDopYieldContractSimpleRsrc");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.GET_SYNC_CLAIM)) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		try {

			SyncDopYieldContractSimpleRsrc result = (SyncDopYieldContractSimpleRsrc) cirrasDataSyncService.getSyncDopYieldContractSimple(
					declaredYieldContractCommodityBerriesGuid,
					getFactoryContext(), 
					getWebAdeAuthentication());

			response = Response.ok(result).tag(result.getUnquotedETag()).build();

		} catch (NotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
			
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">getSyncDopYieldContractSimpleRsrc");
		return response;
	}
	
	@Operation(operationId = "Delete dop record", summary = "Delete dop record", security = @SecurityRequirement(name = "Webade-OAUTH2", scopes = {Scopes.DELETE_SYNC_CLAIM}), extensions = {@Extension(properties = {@ExtensionProperty(name = "auth-type", value = "#{wso2.x-auth-type.none}"), @ExtensionProperty(name = "throttling-tier", value = "Unlimited") })})
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
	public Response deleteSyncDopYieldContractSimple(
		@Parameter(description = "The guid of the dop yield contract commodity berries in CUWS.") @QueryParam("declaredYieldContractCommodityBerriesGuid") String declaredYieldContractCommodityBerriesGuid
	) {
		logger.debug("<deleteSyncDopYieldContractSimple");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.DELETE_SYNC_CLAIM)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			SyncDopYieldContractSimpleRsrc resource = (SyncDopYieldContractSimpleRsrc) cirrasDataSyncService.getSyncDopYieldContractSimple(
					declaredYieldContractCommodityBerriesGuid,
					getFactoryContext(), 
					getWebAdeAuthentication());
			
			if(resource != null) {
				cirrasDataSyncService.deleteSyncDopYieldContractSimple(declaredYieldContractCommodityBerriesGuid, getFactoryContext(), getWebAdeAuthentication());
			}
			response = Response.status(204).build();
		} catch (NotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">deleteSyncDopYieldContractSimple " + response);
		return response;
	}	

	private void logResource(SyncDopYieldContractSimpleRsrc resource, String prefix) {

		StringBuilder msgBldr = new StringBuilder();
		
		if ( resource == null ) {
			msgBldr.append(prefix)
		           .append(": Received Resource is null. ")
		    ;
			
		} else {
			
			// Note that only non-sensitive data can be logged.
			msgBldr.append(prefix)
			       .append(": Received Resource: ")
			       .append("Type: SyncDopYieldContractSimpleRsrc")
			       .append(", ContractId: ")
			       .append(resource.getContractId())
			       .append(", CropYear: ")
			       .append(resource.getCropYear())
			       .append(", Transaction Type: ")
			       .append(resource.getTransactionType())
			;

			if ( resource.getSyncDopYieldContractCommodityBerries() == null ) {
				msgBldr.append(", Received SubType is null.");
			} else {
				msgBldr.append(", SubType: SyncDopYieldContractCommodityBerries")
				       .append(", DeclaredYieldContractCommodityBerriesGuid: ")
				       .append(resource.getSyncDopYieldContractCommodityBerries().getDeclaredYieldContractCommodityBerriesGuid())
				;
			}
		}
		
		logger.info(msgBldr.toString());
	}
	
}
