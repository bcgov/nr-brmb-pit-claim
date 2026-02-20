package ca.bc.gov.mal.cirras.claims.controllers.impl;

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
import ca.bc.gov.mal.cirras.claims.controllers.ClaimCalculationListEndpoint;
import ca.bc.gov.mal.cirras.claims.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.claims.controllers.parameters.PagingQueryParameters;
import ca.bc.gov.mal.cirras.claims.controllers.parameters.validation.ParameterValidator;
import ca.bc.gov.mal.cirras.claims.data.resources.ClaimCalculationListRsrc;
import ca.bc.gov.mal.cirras.claims.data.resources.ClaimCalculationRsrc;
import ca.bc.gov.mal.cirras.claims.services.CirrasClaimService;

public class ClaimCalculationListEndpointImpl extends BaseEndpointsImpl implements ClaimCalculationListEndpoint {
	
	private static final Logger logger = LoggerFactory.getLogger(ClaimCalculationListEndpointImpl.class);
	
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
	public Response getClaimCalculationList(
			String claimNumber,
			String policyNumber,
			String cropYear,
			String calculationStatusCode,
			String createClaimCalcUserGuid,
			String updateClaimCalcUserGuid,
			String insurancePlanId,
			String sortColumn,
			String sortDirection,
		    String pageNumber,
			String pageRowCount) {
		
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

	
	@Override
	public Response createClaimCalculation(ClaimCalculationRsrc resource) {
		logger.debug("<createClaimCalculation");
		Response response = null;
		
		logRequest();
		
		//if(!hasAuthority(Scopes.CREATE_CALCULATION)) {
		//	return Response.status(Status.FORBIDDEN).build();
		//}

		try {

			ClaimCalculationRsrc result = (ClaimCalculationRsrc) cirrasClaimService.createClaimCalculation(
					resource, 
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
