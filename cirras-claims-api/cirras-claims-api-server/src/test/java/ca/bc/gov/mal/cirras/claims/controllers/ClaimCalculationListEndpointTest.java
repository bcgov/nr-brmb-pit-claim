package ca.bc.gov.mal.cirras.claims.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.claims.clients.CirrasClaimService;
import ca.bc.gov.mal.cirras.claims.clients.CirrasClaimServiceException;
import ca.bc.gov.mal.cirras.claims.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.claims.data.resources.EndpointsRsrc;
import ca.bc.gov.mal.cirras.claims.data.entities.ClaimCalculationDto;
import ca.bc.gov.mal.cirras.claims.data.entities.PerilCodeDto;
import ca.bc.gov.mal.cirras.claims.data.resources.ClaimCalculationListRsrc;
import ca.bc.gov.mal.cirras.claims.data.resources.ClaimCalculationRsrc;
import ca.bc.gov.mal.cirras.claims.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class ClaimCalculationListEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(ClaimCalculationListEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.SEARCH_CLAIMS, 
		Scopes.SEARCH_CALCULATIONS,
		Scopes.GET_CALCULATION
	};
	

	//@Test
	public void testSearchClaimCalculationsByClaim() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testSearchClaimCalculationsByClaim");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasClaimService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		ClaimCalculationListRsrc searchResults = service.getClaimCalculations(topLevelEndpoints, "25737", null, null, null, null, null, null, null, null, pageNumber, pageRowCount);

		Assert.assertNotNull(searchResults);
		Assert.assertTrue(searchResults.getTotalRowCount() == 1);

		logger.debug(">testSearchClaimCalculationsByClaim");
	}
	
	//@Test
	public void testSearchClaimCalculationsByPolicy() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testSearchClaimCalculationsByPolicy");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasClaimService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		ClaimCalculationListRsrc searchResults = service.getClaimCalculations(topLevelEndpoints, null, "500280-20", null, null, null, null, null, null, null, pageNumber, pageRowCount);

		Assert.assertNotNull(searchResults);
		Assert.assertTrue(searchResults.getTotalRowCount() == 2);
		
		logger.debug(">testSearchClaimCalculationsByPolicy");
	}
	
	//@Test
	public void testSearchClaimCalculationsAllParameters() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testSearchClaimCalculationsAllParameters");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasClaimService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		ClaimCalculationListRsrc searchResults = service.getClaimCalculations(topLevelEndpoints, 
																			  "25742", 
																			  "500280-20", 
																			  "2020", 
																			  "DRAFT", 
																			  "CC122E6FC92B1EC3E053E60A0A0A25C0", 
																			  "CC122E72A7381EC7E053E60A0A0A2754", 
																			  null,
																			  null, 
																			  null, 
																			  pageNumber, 
																			  pageRowCount);

		Assert.assertNotNull(searchResults);
		Assert.assertTrue(searchResults.getTotalRowCount() == 1);
		
		logger.debug(">testSearchClaimCalculationsAllParameters");
	}
	
	
	//@Test
	public void testSearchClaimCalculationsByCreateUser() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testSearchClaimCalculationsByCreateUser");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasClaimService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		ClaimCalculationListRsrc searchResults = service.getClaimCalculations(topLevelEndpoints, null, null, null, null, "CC122E6FC92B1EC3E053E60A0A0A25C0", null, null, null, null, pageNumber, pageRowCount);

		Assert.assertNotNull(searchResults);
		Assert.assertTrue(searchResults.getTotalRowCount() == 1);
		
		logger.debug(">testSearchClaimCalculationsByCreateUser");
	}	

	//@Test
	public void testSearchClaimCalculationsByUpdateUser() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testSearchClaimCalculationsByUpdateUser");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasClaimService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		ClaimCalculationListRsrc searchResults = service.getClaimCalculations(topLevelEndpoints, null, null, null, null, null, "CC122E6FC92B1EC3E053E60A0A0A25C0", null, null, null, pageNumber, pageRowCount);

		Assert.assertNotNull(searchResults);
		Assert.assertTrue(searchResults.getTotalRowCount() == 2);
		
		logger.debug(">testSearchClaimCalculationsByUpdateUser");
	}
	
	@Test
	public void testSearchClaimCalculationsByInsurancePlan() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testSearchClaimCalculationsByInsurancePlan");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasClaimService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		ClaimCalculationListRsrc searchResults = service.getClaimCalculations(topLevelEndpoints, null, null, null, null, null, null, "3", null, null, pageNumber, pageRowCount);

		Assert.assertNotNull(searchResults);
		//Assert.assertTrue(searchResults.getTotalRowCount() == 2);
		
		logger.debug(">testSearchClaimCalculationsByInsurancePlan");
	}
	
	private int previousIntValue = 0;
	private Double previousDblValue = 0.0;
	private String previousStringValue = "";
	private Date previousDateValue;
	private boolean orderMatches = true;
	private boolean testSuccessful = true;
	
	@Test
	public void testSearchClaimCalculationsOrderByCalculationStatusCode() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testSearchClaimCalculationsOrderByCalculationStatusCode");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasClaimService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		ClaimCalculationListRsrc searchResults = service.getClaimCalculations(topLevelEndpoints, null, null, null, null, null, null, null, "calculationStatusCode", "ASC", pageNumber, pageRowCount);

		orderMatches = true;
		previousStringValue = "";
		testSuccessful = true;
		
		
		searchResults.getCollection().forEach((temp) -> {
			//First iteration of loop: previousValue = 0			
			if(previousStringValue != ""){
				
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
		
		logger.debug(">testSearchClaimCalculationsOrderByCalculationStatusCode");
	}
	
	@Test
	public void testSearchClaimCalculationsOrderByClaimNumber() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testSearchClaimCalculationsOrderByClaimNumber");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasClaimService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		ClaimCalculationListRsrc searchResults = service.getClaimCalculations(topLevelEndpoints, null, null, null, null, null, null, null, "claimNumber", "ASC", pageNumber, pageRowCount);

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
		
		logger.debug(">testSearchClaimCalculationsOrderByClaimNumber");
	}	

	@Test
	public void testSearchClaimCalculationsOrderByCalculationVersion() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testSearchClaimCalculationsOrderByCalculationVersion");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasClaimService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		ClaimCalculationListRsrc searchResults = service.getClaimCalculations(topLevelEndpoints, null, null, null, null, null, null, null, "calculationVersion", "ASC", pageNumber, pageRowCount);

		orderMatches = true;
		previousIntValue = 0;
		testSuccessful = true;
		
		searchResults.getCollection().forEach((temp) -> {
			//First iteration of loop: previousValue = 0			
			if(previousIntValue > 0){
				orderMatches = (previousIntValue <= temp.getCalculationVersion()); 
			}
			
			if(!orderMatches) {
	            System.out.println("Order is wrong: " + temp.getCalculationVersion() + " < " + previousIntValue);
	            testSuccessful = false;
			}
			
			previousIntValue = temp.getCalculationVersion();
        });	  
		
		Assert.assertTrue(testSuccessful);
		
		logger.debug(">testSearchClaimCalculationsOrderByCalculationVersion");
	}	

	@Test
	public void testSearchClaimCalculationsOrderByClaimStatusCode() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testSearchClaimCalculationsOrderByClaimStatusCode");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasClaimService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		ClaimCalculationListRsrc searchResults = service.getClaimCalculations(topLevelEndpoints, null, null, null, null, null, null, null, "claimStatusCode", "ASC", pageNumber, pageRowCount);

		orderMatches = true;
		previousStringValue = "";
		testSuccessful = true;
		
		
		searchResults.getCollection().forEach((temp) -> {
			//First iteration of loop: previousValue = 0			
			if(previousStringValue != ""){
				
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
		
		logger.debug(">testSearchClaimCalculationsOrderByClaimStatusCode");
	}
	
	@Test
	public void testSearchClaimCalculationsOrderByPolicyNumber() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testSearchClaimCalculationsOrderByPolicyNumber");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasClaimService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		ClaimCalculationListRsrc searchResults = service.getClaimCalculations(topLevelEndpoints, null, null, null, null, null, null, null, "policyNumber", "ASC", pageNumber, pageRowCount);

		orderMatches = true;
		previousStringValue = "";
		testSuccessful = true;
		
		
		searchResults.getCollection().forEach((temp) -> {
			//First iteration of loop: previousValue = 0			
			if(previousStringValue != ""){
				
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
		
		logger.debug(">testSearchClaimCalculationsOrderByPolicyNumber");
	}	
	
	@Test
	public void testSearchClaimCalculationsOrderByGrowerName() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testSearchClaimCalculationsOrderByGrowerName");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasClaimService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		ClaimCalculationListRsrc searchResults = service.getClaimCalculations(topLevelEndpoints, null, null, null, null, null, null, null, "growerName", "ASC", pageNumber, pageRowCount);

		orderMatches = true;
		previousStringValue = "";
		testSuccessful = true;
		
		
		searchResults.getCollection().forEach((temp) -> {
			//First iteration of loop: previousValue = 0			
			if(previousStringValue != ""){
				
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
		
		logger.debug(">testSearchClaimCalculationsOrderByGrowerName");
	}
	
	@Test
	public void testSearchClaimCalculationsOrderByInsurancePlanName() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testSearchClaimCalculationsOrderByInsurancePlanName");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasClaimService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		ClaimCalculationListRsrc searchResults = service.getClaimCalculations(topLevelEndpoints, null, null, null, null, null, null, null, "insurancePlanName", "ASC", pageNumber, pageRowCount);

		orderMatches = true;
		previousStringValue = "";
		testSuccessful = true;
		
		
		searchResults.getCollection().forEach((temp) -> {
			//First iteration of loop: previousValue = 0			
			if(previousStringValue != ""){
				
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
		
		logger.debug(">testSearchClaimCalculationsOrderByInsurancePlanName");
	}	
	
	@Test
	public void testSearchClaimCalculationsOrderByCoverageName() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testSearchClaimCalculationsOrderByCoverageName");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasClaimService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		ClaimCalculationListRsrc searchResults = service.getClaimCalculations(topLevelEndpoints, null, null, null, null, null, null, null, "coverageName", "ASC", pageNumber, pageRowCount);

		orderMatches = true;
		previousStringValue = "";
		testSuccessful = true;
		
		
		searchResults.getCollection().forEach((temp) -> {
			//First iteration of loop: previousValue = 0			
			if(previousStringValue != ""){
				
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
		
		logger.debug(">testSearchClaimCalculationsOrderByCoverageName");
	}	
	
	@Test
	public void testSearchClaimCalculationsOrderByCommodityName() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testSearchClaimCalculationsOrderByCommodityName");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasClaimService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		ClaimCalculationListRsrc searchResults = service.getClaimCalculations(topLevelEndpoints, null, null, null, null, null, null, null, "commodityName", "ASC", pageNumber, pageRowCount);

		orderMatches = true;
		previousStringValue = "";
		testSuccessful = true;
		
		
		searchResults.getCollection().forEach((temp) -> {
			//First iteration of loop: previousValue = 0			
			if(previousStringValue != ""){
				
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
		
		logger.debug(">testSearchClaimCalculationsOrderByCommodityName");
	}	

	@Test
	public void testSearchClaimCalculationsOrderByTotalClaimAmount() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testSearchClaimCalculationsOrderByTotalClaimAmount");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasClaimService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		ClaimCalculationListRsrc searchResults = service.getClaimCalculations(topLevelEndpoints, null, null, null, null, null, null, null, "totalClaimAmount", "ASC", pageNumber, pageRowCount);

		orderMatches = true;
		previousDblValue = 0.0;
		testSuccessful = true;
		
		searchResults.getCollection().forEach((temp) -> {
			
			if(temp.getTotalClaimAmount() != null) {
				
				//First iteration of loop: previousValue = 0			
				if(previousDblValue > 0){
					orderMatches = (previousDblValue <= temp.getTotalClaimAmount()); 
				}
				
				if(!orderMatches) {
		            System.out.println("Order is wrong: " + temp.getTotalClaimAmount() + " < " + previousDblValue);
		            testSuccessful = false;
				}
				
				previousDblValue = temp.getTotalClaimAmount();
			}
        });	  
		
		Assert.assertTrue(testSuccessful);
		
		logger.debug(">testSearchClaimCalculationsOrderByTotalClaimAmount");
	}

	@Test
	public void testSearchClaimCalculationsOrderByCreateClaimCalcUserName() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testSearchClaimCalculationsOrderByCreateClaimCalcUserName");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasClaimService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		ClaimCalculationListRsrc searchResults = service.getClaimCalculations(topLevelEndpoints, null, null, null, null, null, null, null, "createClaimCalcUserName", "ASC", pageNumber, pageRowCount);

		orderMatches = true;
		previousStringValue = "";
		testSuccessful = true;
		
		
		searchResults.getCollection().forEach((temp) -> {
			
            System.out.println("CreateClaimCalcUserName: " + temp.getCreateClaimCalcUserName());
			
			//First iteration of loop: previousValue = 0			
			if(previousStringValue != ""){
				
				int compare = previousStringValue.compareToIgnoreCase(temp.getCreateClaimCalcUserName());
				
				//compare < 0 if sA is smaller than sB
				//compare = 0 if sA is the same as sB
				//compare > 0 if sA is greater than sB 

				orderMatches = (compare <= 0); 
			}
			
			if(!orderMatches) {
	            System.out.println("Order is wrong: " + temp.getCreateClaimCalcUserName() + " < " + previousStringValue);
	            testSuccessful = false;
			}
			
			previousStringValue = temp.getCreateClaimCalcUserName();
        });	  
		
		Assert.assertTrue(testSuccessful);
		
		logger.debug(">testSearchClaimCalculationsOrderByCreateClaimCalcUserName");
	}
	
	
	@Test
	public void testSearchClaimCalculationsOrderByCreateUser() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testSearchClaimCalculationsOrderByCreateUser");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasClaimService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		ClaimCalculationListRsrc searchResults = service.getClaimCalculations(topLevelEndpoints, null, null, null, null, null, null, null, "createUser", "ASC", pageNumber, pageRowCount);

		orderMatches = true;
		previousStringValue = "";
		testSuccessful = true;
		
		
		searchResults.getCollection().forEach((temp) -> {
			
            System.out.println("CreateUser: " + temp.getCreateUser());
			
			//First iteration of loop: previousValue = 0			
			if(previousStringValue != ""){
				
				int compare = previousStringValue.compareToIgnoreCase(temp.getCreateUser());
				
				//compare < 0 if sA is smaller than sB
				//compare = 0 if sA is the same as sB
				//compare > 0 if sA is greater than sB 

				orderMatches = (compare <= 0); 
			}
			
			if(!orderMatches) {
	            System.out.println("Order is wrong: " + temp.getCreateUser() + " < " + previousStringValue);
	            testSuccessful = false;
			}
			
			previousStringValue = temp.getCreateUser();
        });	  
		
		Assert.assertTrue(testSuccessful);
		
		logger.debug(">testSearchClaimCalculationsOrderByCreateUser");
	}

	@Test
	public void testSearchClaimCalculationsOrderByUpdateClaimCalcUserName() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testSearchClaimCalculationsOrderByUpdateClaimCalcUserName");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasClaimService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		ClaimCalculationListRsrc searchResults = service.getClaimCalculations(topLevelEndpoints, null, null, null, null, null, null, null, "updateClaimCalcUserName", "ASC", pageNumber, pageRowCount);

		orderMatches = true;
		previousStringValue = "";
		testSuccessful = true;
		
		
		searchResults.getCollection().forEach((temp) -> {
			
			//First iteration of loop: previousValue = 0			
			if(previousStringValue != ""){
				
				int compare = previousStringValue.compareToIgnoreCase(temp.getUpdateClaimCalcUserName());
				
				//compare < 0 if sA is smaller than sB
				//compare = 0 if sA is the same as sB
				//compare > 0 if sA is greater than sB 

				orderMatches = (compare <= 0); 
			}
			
			if(!orderMatches) {
	            System.out.println("Order is wrong: " + temp.getUpdateClaimCalcUserName() + " < " + previousStringValue);
	            testSuccessful = false;
			}
			
			previousStringValue = temp.getUpdateClaimCalcUserName();
        });	  
		
		Assert.assertTrue(testSuccessful);
		
		logger.debug(">testSearchClaimCalculationsOrderByUpdateClaimCalcUserName");
	}
	
	@Test
	public void testSearchClaimCalculationsOrderByUpdateUser() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testSearchClaimCalculationsOrderByUpdateUser");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasClaimService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		ClaimCalculationListRsrc searchResults = service.getClaimCalculations(topLevelEndpoints, null, null, null, null, null, null, null, "updateUser", "ASC", pageNumber, pageRowCount);

		orderMatches = true;
		previousStringValue = "";
		testSuccessful = true;
		
		
		searchResults.getCollection().forEach((temp) -> {
			
			//First iteration of loop: previousValue = 0			
			if(previousStringValue != ""){
				
				int compare = previousStringValue.compareToIgnoreCase(temp.getUpdateUser());
				
				//compare < 0 if sA is smaller than sB
				//compare = 0 if sA is the same as sB
				//compare > 0 if sA is greater than sB 

				orderMatches = (compare <= 0); 
			}
			
			if(!orderMatches) {
	            System.out.println("Order is wrong: " + temp.getUpdateUser() + " < " + previousStringValue);
	            testSuccessful = false;
			}
			
			previousStringValue = temp.getUpdateUser();
        });	  
		
		Assert.assertTrue(testSuccessful);
		
		logger.debug(">testSearchClaimCalculationsOrderByUpdateUser");
	}

	
	@Test
	public void testSearchClaimCalculationsOrderByCreateDate() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testSearchClaimCalculationsOrderByCreateDate");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasClaimService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		ClaimCalculationListRsrc searchResults = service.getClaimCalculations(topLevelEndpoints, null, null, null, null, null, null, null, "createDate", "ASC", pageNumber, pageRowCount);

		orderMatches = true;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			previousDateValue = sdf.parse("1980-01-01");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		testSuccessful = true;
		
		searchResults.getCollection().forEach((temp) -> {
			
			Date currentValue = temp.getCreateDate();
			
			int compare = previousDateValue.compareTo(currentValue);
			
			//compare < 0 if sA is smaller than sB
			//compare = 0 if sA is the same as sB
			//compare > 0 if sA is greater than sB 

			orderMatches = (compare <= 0); 

            System.out.println(currentValue + " - " + previousDateValue);

			
			if(!orderMatches) {
	            System.out.println("Order is wrong: " + currentValue + " < " + previousDateValue);
	            testSuccessful = false;
			}
			
			previousDateValue = currentValue;
        });	  
		
		Assert.assertTrue(testSuccessful);
		
		logger.debug(">testSearchClaimCalculationsOrderByCreateDate");
	}

	@Test
	public void testSearchClaimCalculationsOrderByUpdateDate() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testSearchClaimCalculationsOrderByUpdateDate");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasClaimService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		ClaimCalculationListRsrc searchResults = service.getClaimCalculations(topLevelEndpoints, null, null, null, null, null, null, null, "updateDate", "ASC", pageNumber, pageRowCount);

		orderMatches = true;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			previousDateValue = sdf.parse("1980-01-01");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		testSuccessful = true;
		
		searchResults.getCollection().forEach((temp) -> {
			
			Date currentValue = temp.getUpdateDate();
			
			int compare = previousDateValue.compareTo(currentValue);
			
			//compare < 0 if sA is smaller than sB
			//compare = 0 if sA is the same as sB
			//compare > 0 if sA is greater than sB 

			orderMatches = (compare <= 0); 

            System.out.println(currentValue + " - " + previousDateValue);

			
			if(!orderMatches) {
	            System.out.println("Order is wrong: " + currentValue + " < " + previousDateValue);
	            testSuccessful = false;
			}
			
			previousDateValue = currentValue;
        });	  
		
		Assert.assertTrue(testSuccessful);
		
		logger.debug(">testSearchClaimCalculationsOrderByUpdateDate");
	}

}
