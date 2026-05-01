package ca.bc.gov.mal.cirras.claims.controllers;


import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import ca.bc.gov.mal.cirras.claims.data.resources.SyncClaimRsrc;
import ca.bc.gov.nrs.common.wfone.rest.resource.HeaderConstants;
import ca.bc.gov.nrs.common.wfone.rest.resource.MessageListRsrc;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.utils.HttpServletRequestHolder;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.TokenService;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.impl.TokenServiceImpl;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.resource.CheckedToken;
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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;


@Path("/checkTokenUI")
public class CheckTokenController extends BaseEndpointsImpl  {
	
	private static final Logger logger = LoggerFactory.getLogger(CheckTokenController.class);
	
	@Autowired 
	private TokenService tokenService; 
	
	@Operation(operationId = "Check the token.", summary = "Check the token.", extensions = {@Extension(properties = {@ExtensionProperty(name = "auth-type", value = "#{wso2.x-auth-type.none}"), @ExtensionProperty(name = "throttling-tier", value = "Unlimited") })})
	
	@Parameters({
		@Parameter(name = HeaderConstants.REQUEST_ID_HEADER, description = HeaderConstants.REQUEST_ID_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.VERSION_HEADER, description = HeaderConstants.VERSION_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = Integer.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.CACHE_CONTROL_HEADER, description = HeaderConstants.CACHE_CONTROL_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.PRAGMA_HEADER, description = HeaderConstants.PRAGMA_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.AUTHORIZATION_HEADER, description = HeaderConstants.AUTHORIZATION_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER) 
	})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = CheckedToken.class)), headers = @Header(name = HeaderConstants.ETAG_HEADER, schema = @Schema(implementation = String.class), description = HeaderConstants.ETAG_DESCRIPTION)),
		@ApiResponse(responseCode = "404", description = "Not Found"),
		@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = MessageListRsrc.class))) })
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response checkTokenUI(){
        logger.debug("<checkTokenUI in the api");

        Response response = null;
        HttpServletRequest request = HttpServletRequestHolder.getHttpServletRequest();
        
        logger.debug("<checkTokenUI -> request = HttpServletRequestHolder.getHttpServletRequest()");
        //TokenServiceImpl tokenService;

//        tokenService = new TokenServiceImpl(
//        		webadeOauth2ClientId,
//        		webadeOauth2ClientSecret,
//        		webadeOauth2CheckTokenUrl,
//        		webadeOauth2TokenUrl);
//        
        CheckedToken result = null;
        String authorizationHeader = request.getHeader("Authorization");
        logger.debug(" checkTokenUI -> authorizationHeader : " + authorizationHeader);
        
        request.getSession().setAttribute("authToken", authorizationHeader);
        try {
            if (authorizationHeader == null) {
            	logger.debug("checkTokenUI ->  authorizationHeader is null");
                // response.sendError(401);
            	return Response.status(Status.UNAUTHORIZED).build();
            	
            } else {
                result = tokenService.checkToken(authorizationHeader.replace("Bearer ", ""));
                logger.debug("checkTokenUI -> result : " + result);
                response = Response.ok(result).build();
            }
        } catch (Throwable t) {
            // response.sendError(500, "Authentication request was unable to be processed, please try again later.");
        	logger.debug("checkTokenUI ->  error: ");
        	response = getInternalServerErrorResponse(t);
            logger.error(" ### Error while checking for valid authorization token", t);
            
        }

        logger.debug(">checkTokenForUI in the api");
        //return result;
        return response;
    }
	
}
