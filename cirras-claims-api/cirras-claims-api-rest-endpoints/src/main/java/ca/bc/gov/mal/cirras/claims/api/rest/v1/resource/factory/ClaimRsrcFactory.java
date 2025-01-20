package ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.factory;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import ca.bc.gov.nrs.common.wfone.rest.resource.RelLink;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dto.PagedDtos;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.ClaimEndpoint;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.ClaimCalculationEndpoint;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.ClaimListEndpoint;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.ClaimListRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.ClaimRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.claims.model.v1.Claim;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimList;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimDto;
import ca.bc.gov.mal.cirras.claims.service.api.v1.model.factory.ClaimFactory;
import ca.bc.gov.mal.cirras.claims.service.api.v1.util.ClaimsServiceEnums;

public class ClaimRsrcFactory extends BaseResourceFactory implements ClaimFactory {
	
	//======================================================================================================================
	// Claim List (Claim Search)
	//======================================================================================================================
	@Override
	public ClaimList<? extends Claim> getClaimList(
			PagedDtos<ClaimDto> dtos,
			Integer claimNumber,
			String policyNumber,
			String calculationStatusCode,
			String sortColumn,
			String sortDirection,
			Integer pageRowCount,
			FactoryContext context, 
			WebAdeAuthentication authentication
	) throws FactoryException, DaoException {
		
		URI baseUri = getBaseURI(context);
		
		ClaimListRsrc result = null;
		
		List<ClaimRsrc> resources = new ArrayList<ClaimRsrc>();
		
		for (ClaimDto dto : dtos.getResults()) {
			ClaimRsrc resource = new ClaimRsrc();
			
			// Check if the claim is supported by the calculator
			boolean isClaimSupported = false;
			if (((dto.getPlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAPES.toString())
					&& dto.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.Quantity.getCode()))
					|| dto.getPlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.BERRIES.toString()))) {
				isClaimSupported = true;
			}
			
			populateResource(resource, dto, isClaimSupported);

			//Only add link if claim is supported by the calculator
			if (isClaimSupported) {
				//If there is no calculation for the claim yet a link to create a new calculation needs to be added instead of the edit link
				if (dto.getClaimCalculationGuid() == null) {
					setListLinks(resource, false, baseUri, authentication);
				} else {
					setListLinks(resource, true, baseUri, authentication);
				}
			}

			//setSelfLink(dto.getClaimCalculationGuid(), resource, baseUri);
			resources.add(resource);
		}
		
		int pageNumber = dtos.getPageNumber();
		int totalRowCount = dtos.getTotalRowCount();
		pageRowCount = Integer.valueOf(pageRowCount==null?totalRowCount:pageRowCount.intValue());
		int totalPageCount = (int) Math.ceil(((double)totalRowCount)/((double)pageRowCount.intValue()));
		
		result = new ClaimListRsrc();
		result.setCollection(resources);
		result.setPageNumber(pageNumber);
		result.setPageRowCount(pageRowCount.intValue());
		result.setTotalRowCount(totalRowCount);
		result.setTotalPageCount(totalPageCount);
		
		String eTag = getEtag(result);
		result.setETag(eTag);		
		
		setSelfLink(result, 
				claimNumber, 
				policyNumber, 
				calculationStatusCode,
				sortColumn,
				sortDirection,
				pageNumber, 
				pageRowCount.intValue(), 
				baseUri);
		
		setLinks(result, 
				claimNumber, 
				policyNumber, 
				calculationStatusCode,
				sortColumn,
				sortDirection,
				pageNumber,
				pageRowCount.intValue(), 
				totalRowCount, 
				baseUri,
				authentication);
		
		return result;
	}	
	
	private void populateResource(ClaimRsrc resource, ClaimDto dto, boolean isClaimSupported) {

		resource.setCalculationStatusCode(dto.getCalculationStatusCode());
		resource.setClaimNumber(dto.getClaimNumber());
		resource.setPolicyNumber(dto.getPolicyNumber());
		resource.setInsurancePlanName(dto.getPlanName());
		resource.setCommodityName(dto.getCommodityName());
		resource.setCoverageName(dto.getCropCoverage());
		resource.setClaimStatusCode(dto.getClaimStatusCode());

		//Version: 
		// - Version number if there is a calculation
		// - "New" if there is no calculation and claim is in status open
		// - "NotAllowed" if there is no calculation and claim is NOT in status open
		// - "Not Supported" if the claim is not supported by the calculator
		if (dto.getClaimCalculationGuid() == null) {
			if (isClaimSupported) {
				if(resource.getClaimStatusCode().equalsIgnoreCase(ClaimsServiceEnums.ClaimStatusCodes.Open.getClaimStatusCode())) {
					resource.setCalculationVersion("NEW");
				} else {
					resource.setCalculationVersion("NotAllowed");
				}
			} else {
				resource.setCalculationVersion("Not Supported");
			}
			//Set grower name from claim in CIRRAS if there is no calculation
			resource.setGrowerName(dto.getClaimGrowerName());
		} else {
			resource.setClaimCalculationGuid(dto.getClaimCalculationGuid());
			resource.setCalculationVersion("V" + dto.getCalculationVersion());
			resource.setCalculationStatusCode(dto.getCalculationStatusCode());
			resource.setCalculationStatus(dto.getCalculationStatus());
			//Set cached grower name if there is a calculation
			resource.setGrowerName(dto.getCalculationGrowerName());
		}		

	}

	
	private static void setListLinks(
		ClaimRsrc resource, 
		boolean hasCalculation,
		URI baseUri, 
		WebAdeAuthentication authentication) {
		
		if(hasCalculation) {
			if (authentication.hasAuthority(Scopes.GET_CALCULATION)) {
				String result = UriBuilder
					.fromUri(baseUri)
					.path(ClaimCalculationEndpoint.class)
					.build(resource.getClaimCalculationGuid()).toString();
				resource.getLinks().add(new RelLink(ResourceTypes.CLAIM_CALCULATION, result, "GET"));
			}
		} else {
			if (authentication.hasAuthority(Scopes.CREATE_CALCULATION)) {
				String result = UriBuilder
					.fromUri(baseUri)
					.path(ClaimEndpoint.class)
					.build(resource.getClaimNumber()).toString();
				resource.getLinks().add(new RelLink(ResourceTypes.CLAIM, result, "GET"));
			}
		}
			
	}

	
	public static String getClaimListSelfUri(
		Integer claimNumber, 
		String policyNumber, 
		String calculationStatusCode,
		String sortColumn,
		String sortDirection,
		Integer pageNumber, 
		Integer pageRowCount, 
		URI baseUri) {

		String result = UriBuilder.fromUri(baseUri)
			.path(ClaimListEndpoint.class)
			.queryParam("claimNumber", nvl(claimNumber, ""))
			.queryParam("policyNumber", nvl(policyNumber, ""))
			.queryParam("calculationStatusCode", nvl(calculationStatusCode, ""))
			.queryParam("sortColumn", nvl(sortColumn, ""))
			.queryParam("sortDirection", nvl(sortDirection, ""))
			.queryParam("pageNumber", nvl(toString(pageNumber), ""))
			.queryParam("pageRowCount", nvl(toString(pageRowCount), ""))
			
			.build()
			.toString();

		return result;
	}
	
	private static void setSelfLink(
		ClaimListRsrc resource, 
		Integer claimNumber, 
		String policyNumber,
		String calculationStatusCode,
		String sortColumn,
		String sortDirection,
		int pageNumber, 
		int pageRowCount, 
		URI baseUri) {
		
		String selfUri = getClaimListSelfUri(
				claimNumber,
				policyNumber,
				calculationStatusCode,
				sortColumn,
				sortDirection,
				Integer.valueOf(pageNumber), 
				Integer.valueOf(pageRowCount), 
				baseUri);
		
		resource.getLinks().add(new RelLink(ResourceTypes.SELF, selfUri, "GET"));
	}	

	private static void setLinks(
		ClaimListRsrc resource, 
		Integer claimNumber, 
		String policyNumber,
		String calculationStatusCode,
		String sortColumn,
		String sortDirection,
		int pageNumber, 
		int pageRowCount, 
		int totalRowCount,
		URI baseUri, 
		WebAdeAuthentication authentication) {
		
		if(pageNumber > 1) {
			String previousUri = getClaimListSelfUri(
					claimNumber,
					policyNumber,
					calculationStatusCode,
					sortColumn,
					sortDirection,
					Integer.valueOf(pageNumber-1), 
					Integer.valueOf(pageRowCount), 
					baseUri);
			resource.getLinks().add(new RelLink(ResourceTypes.PREV, previousUri, "GET"));
		}
		
		if((pageNumber * pageRowCount) < totalRowCount) {
			String nextUri = getClaimListSelfUri(
					claimNumber,
					policyNumber,
					calculationStatusCode,
					sortColumn,
					sortDirection,
					Integer.valueOf(pageNumber+1),
					Integer.valueOf(pageRowCount), 
					baseUri);
			resource.getLinks().add(new RelLink(ResourceTypes.NEXT, nextUri, "GET"));
		}
	}

	protected static String nvl(Integer value, String defaultValue) {
		return (value==null)?defaultValue:value.toString();
	}
	
}
