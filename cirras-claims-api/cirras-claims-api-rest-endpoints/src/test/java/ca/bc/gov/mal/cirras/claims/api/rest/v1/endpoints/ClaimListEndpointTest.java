package ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.claims.api.rest.client.v1.CirrasClaimService;
import ca.bc.gov.mal.cirras.claims.api.rest.client.v1.CirrasClaimServiceException;
import ca.bc.gov.mal.cirras.claims.api.rest.client.v1.ValidationException;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.EndpointsRsrc;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.InsuranceClaimRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.ClaimCalculationListRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.ClaimCalculationRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.ClaimListRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.ClaimRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class ClaimListEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(ClaimListEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.SEARCH_CLAIMS, 
		Scopes.SEARCH_CALCULATIONS,
		Scopes.GET_CALCULATION,
		Scopes.CREATE_CALCULATION
	};
	

	@Test
	public void testSearchClaims() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testSearchClaims");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasClaimService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();
		String claimNumber = null;//"25739";//25749 (claims with calculations); 28082 (claim without calculation)
		String policyNumber = null;//"400110-12";
		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(20);
		ClaimListRsrc searchResults = service.getClaimList(topLevelEndpoints, claimNumber, policyNumber, null, null, null, pageNumber, pageRowCount);

		Assert.assertNotNull(searchResults);
		Assert.assertTrue(searchResults.getCollection().size() > 0);
		
		logger.debug(">testSearchClaims");
	}

	@Test
	public void testSearchClaimsAllFilters() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testSearchClaimsAllFilters");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasClaimService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();
		String claimNumber = "28168";
		String policyNumber = null;//"712393-21";
		String calculationStatus = "DRAFT";
		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(20);
		ClaimListRsrc searchResults = service.getClaimList(topLevelEndpoints, claimNumber, policyNumber, calculationStatus, null, null, pageNumber, pageRowCount);

		Assert.assertNotNull(searchResults);
		
		logger.debug(">testSearchClaimsAllFilters");
	}	
	@Test
	public void testSearchClaimsByPolicy() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testSearchClaimsByPolicy");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasClaimService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		ClaimListRsrc searchResults = service.getClaimList(topLevelEndpoints, null, "701141-20", null, null, null, pageNumber, pageRowCount);
		
		Assert.assertNotNull(searchResults);
		//Assert.assertTrue(searchResults.getTotalRowCount() == 2);
		
		logger.debug(">testSearchClaimsByPolicy");
	}
	
	@Test
	public void testSearchClaimsByClaimNumber() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testSearchClaimsByClaimNumber");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasClaimService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		String claimNumber = "28213"; //27633 = not supported; 28168 = has calculations; 28213 = new
		ClaimListRsrc searchResults = service.getClaimList(topLevelEndpoints, claimNumber, null, null, null, null, pageNumber, pageRowCount);
		
		Assert.assertNotNull(searchResults);
		//Assert.assertTrue(searchResults.getTotalRowCount() == 2);
		
		logger.debug(">testSearchClaimsByClaimNumber");
	}	
	
	@Test
	public void testSearchClaimsByCalculationStatus() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testSearchClaimsByCalculationStatus");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasClaimService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		ClaimListRsrc searchResults = service.getClaimList(topLevelEndpoints, null, null, "DRAFT", null, null, pageNumber, pageRowCount);
		
		Assert.assertNotNull(searchResults);
		//Assert.assertTrue(searchResults.getTotalRowCount() == 2);
		
		logger.debug(">testSearchClaimsByCalculationStatus");
	}	
	
	private int previousIntValue = 0;
	private Double previousDblValue = 0.0;
	private String previousStringValue = "";
	private Date previousDateValue;
	private boolean orderMatches = true;
	private boolean testSuccessful = true;

	@Test
	public void testSearchClaimsOrderByVersion() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testSearchClaimsOrderByVersion");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasClaimService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		//Needs to be tested with a claim with multiple calculations
		String claimNumber = "28168";
		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		String sortBy = "calculationVersion";
		String sortDirection = "ASC";
		ClaimListRsrc searchResults = service.getClaimList(topLevelEndpoints, claimNumber, null, null, sortBy, sortDirection, pageNumber, pageRowCount);

		orderMatches = true;
		previousStringValue = "";
		testSuccessful = true;
		
		
		searchResults.getCollection().forEach((temp) -> {
			//First iteration of loop: previousValue = 0			
			if(previousStringValue != "" && temp.getCalculationVersion() != null){
				
				int compare = previousStringValue.compareToIgnoreCase(temp.getCalculationVersion());
				
				//compare < 0 if sA is smaller than sB
				//compare = 0 if sA is the same as sB
				//compare > 0 if sA is greater than sB 

				orderMatches = (compare <= 0); 
			}
			
			if(!orderMatches) {
	            System.out.println("Order is wrong: " + temp.getCalculationVersion() + " < " + previousStringValue);
	            testSuccessful = false;
			}
			
			previousStringValue = temp.getCalculationVersion();
        });	  
		
		Assert.assertTrue(testSuccessful);
		
		logger.debug(">testSearchClaimsOrderByVersion");
	}	


	@Test
	public void testSearchClaimsOrderByCalculationStatusCode() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testSearchClaimsOrderByCalculationStatusCode");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasClaimService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		//Needs to be tested with a claim with multiple calculations
		String claimNumber = "28168";
		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		String sortBy = "calculationStatusCode";
		String sortDirection = "ASC";
		ClaimListRsrc searchResults = service.getClaimList(topLevelEndpoints, claimNumber, null, null, sortBy, sortDirection, pageNumber, pageRowCount);

		orderMatches = true;
		previousStringValue = "";
		testSuccessful = true;
		
		
		searchResults.getCollection().forEach((temp) -> {
			//First iteration of loop: previousValue = 0			
			if(previousStringValue != "" && temp.getCalculationStatusCode() != null){
				
				int compare = previousStringValue.compareToIgnoreCase(temp.getCalculationStatusCode());
				
				//compare < 0 if sA is smaller than sB
				//compare = 0 if sA is the same as sB
				//compare > 0 if sA is greater than sB 

				orderMatches = (compare <= 0); 
			}
			
			if(!orderMatches) {
	            System.out.println("Order is wrong: " + temp.getCalculationStatusCode() + " < " + previousStringValue);
	            testSuccessful = false;
			}
			
			previousStringValue = temp.getCalculationStatusCode();
        });	  
		
		Assert.assertTrue(testSuccessful);
		
		logger.debug(">testSearchClaimsOrderByCalculationStatusCode");
	}	

	@Test
	public void testSearchClaimsOrderByClaimNumber() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testSearchClaimsOrderByClaimNumber");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasClaimService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		String sortBy = "claimNumber";
		String sortDirection = "ASC";
		ClaimListRsrc searchResults = service.getClaimList(topLevelEndpoints, null, null, null, sortBy, sortDirection, pageNumber, pageRowCount);

		orderMatches = true;
		previousIntValue = 0;
		testSuccessful = true;
		
		searchResults.getCollection().forEach((temp) -> {
			//First iteration of loop: previousValue = 0			
			if(previousIntValue > 0){
				orderMatches = (previousIntValue <= temp.getClaimNumber()); 
			}
			
			if(!orderMatches) {
	            System.out.println("Order is wrong: " + temp.getClaimNumber() + " < " + previousIntValue);
	            testSuccessful = false;
			}
			
			previousIntValue = temp.getClaimNumber();
        });	  
		
		Assert.assertTrue(testSuccessful);
		
		logger.debug(">testSearchClaimsOrderByClaimNumber");
	}

	@Test
	public void testSearchClaimsOrderByPolicyNumber() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testSearchClaimsOrderByPolicyNumber");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasClaimService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		String sortBy = "policyNumber";
		String sortDirection = "ASC";
		ClaimListRsrc searchResults = service.getClaimList(topLevelEndpoints, null, null, null, sortBy, sortDirection, pageNumber, pageRowCount);

		orderMatches = true;
		previousStringValue = "";
		testSuccessful = true;
		
		
		searchResults.getCollection().forEach((temp) -> {
			//First iteration of loop: previousValue = 0			
			if(previousStringValue != "" && temp.getPolicyNumber() != null){
				
				int compare = previousStringValue.compareToIgnoreCase(temp.getPolicyNumber());
				
				//compare < 0 if sA is smaller than sB
				//compare = 0 if sA is the same as sB
				//compare > 0 if sA is greater than sB 

				orderMatches = (compare <= 0); 
			}
			
			if(!orderMatches) {
	            System.out.println("Order is wrong: " + temp.getPolicyNumber() + " < " + previousStringValue);
	            testSuccessful = false;
			}
			
			previousStringValue = temp.getPolicyNumber();
        });	   
		
		Assert.assertTrue(testSuccessful);
		
		logger.debug(">testSearchClaimsOrderByPolicyNumber");
	}
	
	@Test
	public void testSearchClaimsOrderByInsurancePlanName() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testSearchClaimsOrderByInsurancePlanName");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasClaimService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		String sortBy = "insurancePlanName";
		String sortDirection = "ASC";
		ClaimListRsrc searchResults = service.getClaimList(topLevelEndpoints, null, null, null, sortBy, sortDirection, pageNumber, pageRowCount);

		orderMatches = true;
		previousStringValue = "";
		testSuccessful = true;
		
		
		searchResults.getCollection().forEach((temp) -> {
			//First iteration of loop: previousValue = 0			
			if(previousStringValue != "" && temp.getInsurancePlanName() != null){
				
				int compare = previousStringValue.compareToIgnoreCase(temp.getInsurancePlanName());
				
				//compare < 0 if sA is smaller than sB
				//compare = 0 if sA is the same as sB
				//compare > 0 if sA is greater than sB 

				orderMatches = (compare <= 0); 
			}
			
			if(!orderMatches) {
	            System.out.println("Order is wrong: " + temp.getInsurancePlanName() + " < " + previousStringValue);
	            testSuccessful = false;
			}
			
			previousStringValue = temp.getInsurancePlanName();
        });	   
		
		Assert.assertTrue(testSuccessful);
		
		logger.debug(">testSearchClaimsOrderByInsurancePlanName");
	}

	@Test
	public void testSearchClaimsOrderByCommodityName() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testSearchClaimsOrderByCommodityName");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasClaimService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		String sortBy = "commodityName";
		String sortDirection = "ASC";
		ClaimListRsrc searchResults = service.getClaimList(topLevelEndpoints, null, null, null, sortBy, sortDirection, pageNumber, pageRowCount);

		orderMatches = true;
		previousStringValue = "";
		testSuccessful = true;
		
		
		searchResults.getCollection().forEach((temp) -> {
			//First iteration of loop: previousValue = 0			
			if(previousStringValue != "" && temp.getCommodityName() != null){
				
				int compare = previousStringValue.compareToIgnoreCase(temp.getCommodityName());
				
				//compare < 0 if sA is smaller than sB
				//compare = 0 if sA is the same as sB
				//compare > 0 if sA is greater than sB 

				orderMatches = (compare <= 0); 
			}
			
			if(!orderMatches) {
	            System.out.println("Order is wrong: " + temp.getCommodityName() + " < " + previousStringValue);
	            testSuccessful = false;
			}
			
			previousStringValue = temp.getCommodityName();
        });	   
		
		Assert.assertTrue(testSuccessful);
		
		logger.debug(">testSearchClaimsOrderByCommodityName");
	}

	@Test
	public void testSearchClaimsOrderByCoverageName() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testSearchClaimsOrderByCoverageName");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasClaimService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		String sortBy = "coverageName";
		String sortDirection = "ASC";
		ClaimListRsrc searchResults = service.getClaimList(topLevelEndpoints, null, null, null, sortBy, sortDirection, pageNumber, pageRowCount);

		orderMatches = true;
		previousStringValue = "";
		testSuccessful = true;
		
		
		searchResults.getCollection().forEach((temp) -> {
			//First iteration of loop: previousValue = 0			
			if(previousStringValue != "" && temp.getCoverageName() != null){
				
				int compare = previousStringValue.compareToIgnoreCase(temp.getCoverageName());
				
				//compare < 0 if sA is smaller than sB
				//compare = 0 if sA is the same as sB
				//compare > 0 if sA is greater than sB 

				orderMatches = (compare <= 0); 
			}
			
			if(!orderMatches) {
	            System.out.println("Order is wrong: " + temp.getCoverageName() + " < " + previousStringValue);
	            testSuccessful = false;
			}
			
			previousStringValue = temp.getCoverageName();
        });	   
		
		Assert.assertTrue(testSuccessful);
		
		logger.debug(">testSearchClaimsOrderByCoverageName");
	}

	@Test
	public void testSearchClaimsOrderByGrowerName() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testSearchClaimsOrderByGrowerName");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasClaimService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		String sortBy = "growerName";
		String sortDirection = "ASC";
		ClaimListRsrc searchResults = service.getClaimList(topLevelEndpoints, null, null, null, sortBy, sortDirection, pageNumber, pageRowCount);

		orderMatches = true;
		previousStringValue = "";
		testSuccessful = true;
		
		
		searchResults.getCollection().forEach((temp) -> {
			//First iteration of loop: previousValue = 0			
			if(previousStringValue != "" && temp.getGrowerName() != null){
				
				int compare = previousStringValue.compareToIgnoreCase(temp.getGrowerName());
				
				//compare < 0 if sA is smaller than sB
				//compare = 0 if sA is the same as sB
				//compare > 0 if sA is greater than sB 

				orderMatches = (compare <= 0); 
			}
			
			if(!orderMatches) {
	            System.out.println("Order is wrong: " + temp.getGrowerName() + " < " + previousStringValue);
	            testSuccessful = false;
			}
			
			previousStringValue = temp.getGrowerName();
        });	   
		
		Assert.assertTrue(testSuccessful);
		
		logger.debug(">testSearchClaimsOrderByGrowerName");
	}
	
	@Test
	public void testSearchClaimsOrderByClaimStatus() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testSearchClaimsOrderByClaimStatus");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasClaimService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		String sortBy = "claimStatusCode";
		String sortDirection = "ASC";
		ClaimListRsrc searchResults = service.getClaimList(topLevelEndpoints, null, null, null, sortBy, sortDirection, pageNumber, pageRowCount);

		orderMatches = true;
		previousStringValue = "";
		testSuccessful = true;
		
		
		searchResults.getCollection().forEach((temp) -> {
			//First iteration of loop: previousValue = 0			
			if(previousStringValue != "" && temp.getClaimStatusCode() != null){
				
				int compare = previousStringValue.compareToIgnoreCase(temp.getClaimStatusCode());
				
				//compare < 0 if sA is smaller than sB
				//compare = 0 if sA is the same as sB
				//compare > 0 if sA is greater than sB 

				orderMatches = (compare <= 0); 
			}
			
			if(!orderMatches) {
	            System.out.println("Order is wrong: " + temp.getClaimStatusCode() + " < " + previousStringValue);
	            testSuccessful = false;
			}
			
			previousStringValue = temp.getClaimStatusCode();
        });	   
		
		Assert.assertTrue(testSuccessful);
		
		logger.debug(">testSearchClaimsOrderByClaimStatus");
	}
}
