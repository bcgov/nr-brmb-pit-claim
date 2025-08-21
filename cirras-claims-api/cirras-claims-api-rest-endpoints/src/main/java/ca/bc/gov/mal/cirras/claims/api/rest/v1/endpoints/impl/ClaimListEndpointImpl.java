package ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.common.wfone.rest.resource.MessageListRsrc;
import ca.bc.gov.nrs.wfone.common.model.Message;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.ValidationFailureException;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.ClaimListEndpoint;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.parameters.PagingQueryParameters;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.parameters.validation.ParameterValidator;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.ClaimCalculationRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.ClaimListRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.ClaimRsrc;
import ca.bc.gov.mal.cirras.claims.service.api.v1.CirrasClaimService;

public class ClaimListEndpointImpl extends BaseEndpointsImpl implements ClaimListEndpoint {
	
	private static final Logger logger = LoggerFactory.getLogger(ClaimListEndpointImpl.class);
	
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
	

	@Override
	public Response getClaimList(
			String claimNumber,
		    String policyNumber,
		    String calculationStatusCode,
			String sortColumn,
			String sortDirection,
			String pageNumber,
			String pageRowCount) {
		
		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.SEARCH_CLAIMS)) {
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
				
				ClaimListRsrc results = (ClaimListRsrc) cirrasClaimService.getClaimList(
						toInteger(claimNumber),  // returns null is String is empty
						toString(policyNumber),
						toString(calculationStatusCode),
						toString(sortColumn),
						toString(sortDirection),
						toInteger(pageNumber), 
						toInteger(pageRowCount), 
						getFactoryContext(), 
						getWebAdeAuthentication());

				GenericEntity<ClaimListRsrc> entity = new GenericEntity<ClaimListRsrc>(results) {
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

}
