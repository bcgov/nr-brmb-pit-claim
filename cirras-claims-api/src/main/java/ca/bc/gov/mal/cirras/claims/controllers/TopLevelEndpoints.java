package ca.bc.gov.mal.cirras.claims.controllers;

import java.net.URI;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.claims.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.claims.data.resources.EndpointsRsrc;
import ca.bc.gov.mal.cirras.claims.data.assemblers.ClaimRsrcFactory;
import ca.bc.gov.mal.cirras.claims.data.assemblers.CirrasDataSyncRsrcFactory;
import ca.bc.gov.mal.cirras.claims.data.assemblers.ClaimCalculationRsrcFactory;
import ca.bc.gov.mal.cirras.claims.data.resources.types.ResourceTypes;
import ca.bc.gov.nrs.common.wfone.rest.resource.HeaderConstants;
import ca.bc.gov.nrs.common.wfone.rest.resource.MessageListRsrc;
import ca.bc.gov.nrs.common.wfone.rest.resource.RelLink;
import ca.bc.gov.nrs.wfone.common.api.rest.code.resource.factory.CodeTableResourceFactory;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
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

@Path("/")
public class TopLevelEndpoints extends BaseEndpointsImpl {
	/** Logger. */
	private static final Logger logger = LoggerFactory.getLogger(TopLevelEndpoints.class);
	
	@Operation(operationId = "Get Top Level Resources.", summary = "Get Top Level Resources", extensions = {@Extension(properties = {@ExtensionProperty(name = "auth-type", value = "#{wso2.x-auth-type.none}"), @ExtensionProperty(name = "throttling-tier", value = "Unlimited") })})
	@Parameters({
		@Parameter(name = HeaderConstants.REQUEST_ID_HEADER, description = HeaderConstants.REQUEST_ID_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.VERSION_HEADER, description = HeaderConstants.VERSION_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = Integer.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.CACHE_CONTROL_HEADER, description = HeaderConstants.CACHE_CONTROL_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.PRAGMA_HEADER, description = HeaderConstants.PRAGMA_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.AUTHORIZATION_HEADER, description = HeaderConstants.AUTHORIZATION_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER) 
	})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = EndpointsRsrc.class)), headers = @Header(name = HeaderConstants.ETAG_HEADER, schema = @Schema(implementation = String.class), description = HeaderConstants.ETAG_DESCRIPTION)),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = MessageListRsrc.class))) })
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getTopLevel() {
		Response response = null;

		URI baseUri = this.getBaseUri();
		
		EndpointsRsrc result = new EndpointsRsrc();

		result.setReleaseVersion(this.getApplicationProperties().getProperty("application.version"));

		try {
			
			String selfURI = CodeTableResourceFactory.getCodeTablesSelfUri(null, baseUri);
			result.getLinks().add(new RelLink(ResourceTypes.CODE_TABLE_LIST, selfURI, HttpMethod.GET));
			
			if (hasAuthority(Scopes.SEARCH_CLAIMS)) {
				selfURI = ClaimRsrcFactory.getClaimListSelfUri(null, null, null, null, null, null, null, baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.CLAIM_LIST, selfURI, HttpMethod.GET));
			}
			
			if (hasAuthority(Scopes.SEARCH_CALCULATIONS)) {
				selfURI = ClaimCalculationRsrcFactory.getClaimCalculationListSelfUri(null, null, null, null, null, null, null, null, null, null, baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.CLAIM_CALCULATION_LIST, selfURI, HttpMethod.GET));
			}

			if (hasAuthority(Scopes.CREATE_CALCULATION)) {
				selfURI = ClaimCalculationRsrcFactory.getClaimCalculationListSelfUri(null, null, null, null, null, null, null, null, null, null, baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.CREATE_CLAIM_CALCULATION, selfURI, HttpMethod.POST));
			}
			
			//DATA SYNC ENDPOINTS
			if (hasAuthority(Scopes.GET_SYNC_CLAIM)) {
				selfURI = CirrasDataSyncRsrcFactory.getSyncClaimSelfUri(null, baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.SYNC_CLAIM, selfURI, HttpMethod.GET));
			}
			if (hasAuthority(Scopes.CREATE_SYNC_CLAIM)) {
				selfURI = CirrasDataSyncRsrcFactory.getSyncClaimSelfUri(null, baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.CREATE_SYNC_CLAIM, selfURI, HttpMethod.POST));
			}
				
			if (hasAuthority(Scopes.UPDATE_SYNC_CLAIM)) {
				//grower and policy updates
				selfURI = CirrasDataSyncRsrcFactory.getSyncClaimRelatedDataSelfUri(null, baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.UPDATE_SYNC_CLAIM_RELATED_DATA, selfURI, HttpMethod.PUT));
				
				//COMMODITY/VARIETY SYNC
				selfURI = CirrasDataSyncRsrcFactory.getSyncCommodityVarietySelfUri(baseUri);
				//create/update
				result.getLinks().add(new RelLink(ResourceTypes.SYNCHRONIZE_COMMODITY_VARIETY, selfURI, HttpMethod.POST));
				//delete
				result.getLinks().add(new RelLink(ResourceTypes.DELETE_SYNC_COMMODITY_VARIETY, selfURI, HttpMethod.DELETE));
				
				//CODE SYNC
				selfURI = CirrasDataSyncRsrcFactory.getSyncCodeSelfUri(baseUri);
				//create/update
				result.getLinks().add(new RelLink(ResourceTypes.SYNCHRONIZE_CODE, selfURI, HttpMethod.POST));
				//delete
				result.getLinks().add(new RelLink(ResourceTypes.DELETE_SYNC_CODE, selfURI, HttpMethod.DELETE));
				
				//COVERAGE PERIL SYNC
				selfURI = CirrasDataSyncRsrcFactory.getSyncCoveragePerilSelfUri(baseUri);
				//create/update
				result.getLinks().add(new RelLink(ResourceTypes.SYNCHRONIZE_COVERAGE_PERIL, selfURI, HttpMethod.POST));
				//delete
				result.getLinks().add(new RelLink(ResourceTypes.DELETE_SYNC_COVERAGE_PERIL, selfURI, HttpMethod.DELETE));
				
			}
				
			result.setETag(getEtag(result));

			GenericEntity<EndpointsRsrc> entity = new GenericEntity<EndpointsRsrc>(result) {
				/* do nothing */
			};

			response = Response.ok(entity).tag(result.getUnquotedETag()).build();
			
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getTopLevel=" + response);
		}
		
		return response;
	}
}
