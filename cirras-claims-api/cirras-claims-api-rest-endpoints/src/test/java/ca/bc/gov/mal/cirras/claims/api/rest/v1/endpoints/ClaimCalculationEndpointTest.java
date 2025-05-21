package ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Iterator;

import org.junit.Before;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.claims.api.rest.client.v1.CirrasClaimService;
import ca.bc.gov.mal.cirras.claims.api.rest.client.v1.CirrasClaimServiceException;
import ca.bc.gov.mal.cirras.claims.api.rest.client.v1.ValidationException;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.EndpointsRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.ClaimCalculationListRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.ClaimCalculationRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.ClaimListRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.ClaimRsrc;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationGrainUnseeded;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationVariety;
import ca.bc.gov.mal.cirras.claims.service.api.v1.util.ClaimsServiceEnums;
import ca.bc.gov.mal.cirras.claims.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class ClaimCalculationEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(ClaimCalculationEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.SEARCH_CLAIMS,
		Scopes.GET_CALCULATION,
		Scopes.CREATE_CALCULATION,
		Scopes.UPDATE_CALCULATION,
		Scopes.DELETE_CLAIM,
		Scopes.SEARCH_CALCULATIONS,
		Scopes.REFRESH_DATA,
		Scopes.PRINT_CALCULATION
	};
	
	private Integer pageNumber = new Integer(0);
	private Integer pageRowCount = new Integer(100);
	private Integer claimNumber = new Integer(28082);

	// Used by out of sync and refresh tests.
	private Integer outOfSyncClaimNumber = null;
	private String outOfSyncClaimCalculationGuid = null;

	// Used by replace tests.
	private Integer replaceClaimNumber = null;
	private String replaceClaimCalculationGuid1 = null;
	private String replaceClaimCalculationGuid2 = null;
	private String replaceClaimCalculationGuid3 = null;
	
	
	private CirrasClaimService service;
	private EndpointsRsrc topLevelEndpoints;

	
	@Before
	public void prepareTests() throws CirrasClaimServiceException, Oauth2ClientException{
		service = getService(SCOPES);
		topLevelEndpoints = service.getTopLevelEndpoints();
		//deleteClaimCalculation();
		deleteClaimCalculation(outOfSyncClaimNumber, outOfSyncClaimCalculationGuid);
		deleteClaimCalculation(replaceClaimNumber, replaceClaimCalculationGuid1);
		deleteClaimCalculation(replaceClaimNumber, replaceClaimCalculationGuid2);
		deleteClaimCalculation(replaceClaimNumber, replaceClaimCalculationGuid3);
	}
	
	@After 
	public void cleanUp() throws CirrasClaimServiceException {
		deleteClaimCalculation();
		deleteClaimCalculation(outOfSyncClaimNumber, outOfSyncClaimCalculationGuid);
		deleteClaimCalculation(replaceClaimNumber, replaceClaimCalculationGuid1);
		deleteClaimCalculation(replaceClaimNumber, replaceClaimCalculationGuid2);
		deleteClaimCalculation(replaceClaimNumber, replaceClaimCalculationGuid3);
	}
	
	@Test
	public void testGetClaimCalculation() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testGetClaimCalculation");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		Integer claimNumber = 28201;//28168;

		ClaimCalculationListRsrc searchResults = service.getClaimCalculations(topLevelEndpoints, claimNumber.toString(), null, null, null, null, null, null, null, null, pageNumber, pageRowCount);
		Assert.assertEquals(1, searchResults.getTotalRowCount());
		ClaimCalculationRsrc claimRsrc = searchResults.getCollection().get(0);
		Assert.assertEquals(claimNumber.intValue(), claimRsrc.getClaimNumber().intValue()); //28168

		String calcStatusBefore = claimRsrc.getCalculationStatusCode();
		String claimStatusBefore = claimRsrc.getClaimStatusCode();
		String submittedByUserId = claimRsrc.getSubmittedByUserid();
		String submittedByName = claimRsrc.getSubmittedByName();
		Date submittedByDate = claimRsrc.getSubmittedByDate();
		String recommendedByUserId = claimRsrc.getRecommendedByUserid();
		String recommendedByName = claimRsrc.getRecommendedByName();
		Date recommendedByDate = claimRsrc.getRecommendedByDate();
		String approvedByUserId = claimRsrc.getApprovedByUserid();
		String approvedByName = claimRsrc.getApprovedByName();
		Date approvedByDate = claimRsrc.getApprovedByDate();
		String currentClaimStatusBefore = claimRsrc.getCurrentClaimStatusCode();

		claimRsrc = service.getClaimCalculation(claimRsrc, false);
		Assert.assertNotNull(claimRsrc);
		Assert.assertEquals(claimNumber.intValue(), claimRsrc.getClaimNumber().intValue());
		
		//Assert.assertEquals(claimRsrc.getCalculateIivInd(), "Y");

		//Assert.assertEquals(6, claimRsrc.getVarieties().size());
		logger.debug("Calculation Status Before: " + calcStatusBefore + " - After: " + claimRsrc.getCalculationStatusCode());
		logger.debug("Claim Status Before: " + claimStatusBefore + " - After: " + claimRsrc.getClaimStatusCode());
		logger.debug("Current Claim Status Before: " + currentClaimStatusBefore + " - After: " + claimRsrc.getCurrentClaimStatusCode());
		logger.debug("SubmittedByUserid Before: " + submittedByUserId + " - After: " + claimRsrc.getSubmittedByUserid());
		logger.debug("SubmittedByName Before: " + submittedByName + " - After: " + claimRsrc.getSubmittedByName());
		logger.debug("SubmittedByDate Before: " + submittedByDate + " - After: " + claimRsrc.getSubmittedByDate());
		logger.debug("RecommendedByUserid Before: " + recommendedByUserId + " - After: " + claimRsrc.getRecommendedByUserid());
		logger.debug("RecommendedByName Before: " + recommendedByName + " - After: " + claimRsrc.getRecommendedByName());
		logger.debug("RecommendedByDate Before: " + recommendedByDate + " - After: " + claimRsrc.getRecommendedByDate());
		logger.debug("ApprovedByUserid Before: " + approvedByUserId + " - After: " + claimRsrc.getApprovedByUserid());
		logger.debug("ApprovedByName Before: " + approvedByName + " - After: " + claimRsrc.getApprovedByName());
		logger.debug("ApprovedByDate Before: " + approvedByDate + " - After: " + claimRsrc.getApprovedByDate());
		//233
		//1010419

//		//Refresh
//		ClaimCalculationRsrc claimCalc = service.getClaimCalculation(claimRsrc, true);
		
		logger.debug(">testGetClaimCalculation");
	}
	
	@Test
	public void testReplaceClaimCalculation() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testReplaceClaimCalculation");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		// To run this test, set the claimNumber to an existing claim with an existing calculation.
		// Set expectedStatus and expectedHasChequeReqInd accordingly. There are two combinations allowed for REPLACE:
		// 1. Claim Status = OPEN, hasChequeReqInd = false.
		// 2. Claim Status = IN PROGRESS, hasChequeReqInd = true.
		// This can't be automated, because the claim statuses have to be set manually in CIRRAS.
		
		String claimNumber = "31131";
		String expectedCalcClaimStatus = ClaimsServiceEnums.ClaimStatusCodes.Approved.getClaimStatusCode();
		String expectedCurrentClaimStatus = ClaimsServiceEnums.ClaimStatusCodes.InProgress.getClaimStatusCode();
		Boolean expectedCalcHasChequeReqInd = false;
		Boolean expectedCurrentHasChequeReqInd = true;
		String replaceType = ClaimsServiceEnums.UpdateTypes.REPLACE_COPY.toString();
		Integer version = new Integer(0);
		
		ClaimCalculationListRsrc searchResults = service.getClaimCalculations(topLevelEndpoints, claimNumber, null, null, null, null, null, null, null, null, pageNumber, pageRowCount);
		
		ClaimCalculationRsrc calculationToUpdate = new ClaimCalculationRsrc();
		
		//Find a calculation with varieties
		if(searchResults.getTotalRowCount() > 0) {
			ClaimCalculationRsrc tempRsrc = new ClaimCalculationRsrc();
			ClaimCalculationRsrc highestVersionCalculation = new ClaimCalculationRsrc();
			
			for (int i = 0; i < searchResults.getTotalRowCount(); i++) {

				tempRsrc = searchResults.getCollection().get(i);
				 if ( tempRsrc.getCalculationVersion() > version) {
					 version = tempRsrc.getCalculationVersion();
					 highestVersionCalculation = tempRsrc;
				 }
			}			

			//getClaimCalculations doesn't return the varieties so we have to get the single calculation
			calculationToUpdate = service.getClaimCalculation(highestVersionCalculation, false);

			Assert.assertEquals(expectedCalcClaimStatus, calculationToUpdate.getClaimStatusCode());
			Assert.assertEquals(expectedCurrentClaimStatus, calculationToUpdate.getCurrentClaimStatusCode());

			Assert.assertEquals(expectedCalcHasChequeReqInd, calculationToUpdate.getHasChequeReqInd());
			Assert.assertEquals(expectedCurrentHasChequeReqInd, calculationToUpdate.getCurrentHasChequeReqInd());
			
			//Update calculation
			calculationToUpdate.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.ARCHIVED.toString());

			ClaimCalculationRsrc newCalculation = service.updateClaimCalculation(calculationToUpdate, replaceType);
			
			Assert.assertEquals("Calculation Status", newCalculation.getCalculationStatusCode(), ClaimsServiceEnums.CalculationStatusCodes.DRAFT.toString());
			
			//service.createClaimCalculation(newCalculation);

		} else {
			Assert.fail("No calculation found.");
		}
		
		
		logger.debug(">testReplaceClaimCalculation");
	}
	
	@Test
	public void testReplaceClaimCalculationInvalidStatus() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testReplaceClaimCalculationInvalidStatus");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		// To run this test, set the claimNumber to an existing claim with an existing calculation.
		// Set expectedStatus and expectedHasChequeReqInd accordingly. The following are two combinations that should fail for REPLACE due to invalid claim status and cheque req ind:
		// 1. Claim Status = IN PROGRESS, hasChequeReqInd = false.
		// 2. Claim Status = APPROVED, hasChequeReqInd = true.
		// This can't be automated, because the claim statuses have to be set manually in CIRRAS.
		
		String claimNumber = "31131";
		String expectedClaimStatus = ClaimsServiceEnums.ClaimStatusCodes.Approved.getClaimStatusCode();
		Boolean expectedHasChequeReqInd = true;
		Integer version = new Integer(0);
		
		ClaimCalculationListRsrc searchResults = service.getClaimCalculations(topLevelEndpoints, claimNumber, null, null, null, null, null, null, null, null, pageNumber, pageRowCount);
		
		ClaimCalculationRsrc calculationToUpdate = new ClaimCalculationRsrc();
		
		//Find a calculation with varieties
		if(searchResults.getTotalRowCount() > 0) {
			ClaimCalculationRsrc tempRsrc = new ClaimCalculationRsrc();
			ClaimCalculationRsrc highestVersionCalculation = new ClaimCalculationRsrc();
			
			for (int i = 0; i < searchResults.getTotalRowCount(); i++) {

				tempRsrc = searchResults.getCollection().get(i);
				 if ( tempRsrc.getCalculationVersion() > version) {
					 version = tempRsrc.getCalculationVersion();
					 highestVersionCalculation = tempRsrc;
				 }
			}			

			//getClaimCalculations doesn't return the varieties so we have to get the single calculation
			calculationToUpdate = service.getClaimCalculation(highestVersionCalculation, false);

			Assert.assertEquals(expectedClaimStatus, calculationToUpdate.getClaimStatusCode());
			Assert.assertEquals(expectedClaimStatus, calculationToUpdate.getCurrentClaimStatusCode());

			Assert.assertEquals(expectedHasChequeReqInd, calculationToUpdate.getHasChequeReqInd());
			Assert.assertEquals(expectedHasChequeReqInd, calculationToUpdate.getCurrentHasChequeReqInd());
			
			//Update calculation
			calculationToUpdate.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.ARCHIVED.toString());

			try {
				service.updateClaimCalculation(calculationToUpdate, ClaimsServiceEnums.UpdateTypes.REPLACE_NEW.toString());
				Assert.fail("REPLACE_NEW operation should have thrown an exception");
			} catch ( CirrasClaimServiceException e) {
				// Expected.
			}
						
		} else {
			Assert.fail("No calculation found.");
		}
		
		
		logger.debug(">testReplaceClaimCalculationInvalidStatus");
	}
	
	
	@Test
	public void testUpdateClaimCalculation() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testUpdateClaimCalculation");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		ClaimCalculationListRsrc searchResults = service.getClaimCalculations(topLevelEndpoints, null, null, null, null, null, null, null, null, null, pageNumber, pageRowCount);
		
		ClaimCalculationRsrc calculationToUpdate = new ClaimCalculationRsrc();
		boolean calculationFound = false;
		
		//Find a calculation with varieties
		if(searchResults.getTotalRowCount() > 0) {
			ClaimCalculationRsrc tempRsrc = new ClaimCalculationRsrc();

			for (int i = 0; i < searchResults.getTotalRowCount(); i++) {

				tempRsrc = searchResults.getCollection().get(i);
				
				//getClaimCalculations doesn't return the varieties so we have to get the single calculation
				calculationToUpdate = service.getClaimCalculation(tempRsrc, false);
				
				if(calculationToUpdate.getVarieties().size() > 0) {
					calculationFound = true;
					break;
				}
			}
			
			if(calculationFound) {
				//Clean up: check if there is a calculation with the claimNumber used for the new one and delete it
				
				//Update calculation
				calculationToUpdate.setGrowerAddressLine1("Test Street");
				ClaimCalculationVariety variety = calculationToUpdate.getVarieties().get(0);
				variety.setYieldAssessed((double) 10);
				variety.setYieldAssessedReason("Unit test test comment");
				ClaimCalculationRsrc updatedCalculation = service.updateClaimCalculation(calculationToUpdate, null);// ClaimsServiceEnums.UpdateTypes.REPLACE_COPY.toString());

				Assert.assertEquals("UpdateClaimCalcUserGuid is not correct", "CEB90F1F826B146CE053E60A0A0A72D1", updatedCalculation.getUpdateClaimCalcUserGuid());
				
			} else {
				Assert.fail("No calculation with varieties found.");
			}

		} else {
			Assert.fail("No calculation found.");
		}
		
		
		logger.debug(">testUpdateClaimCalculation");
	}
	
	@Test
	public void testSubmitClaimCalculation() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testSubmitClaimCalculation");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		String claimNumber = "28168";
		
		ClaimCalculationListRsrc searchResults = service.getClaimCalculations(topLevelEndpoints, claimNumber, null, null, "DRAFT", null, null, null, null, null, pageNumber, pageRowCount);
		Assert.assertNotNull("getClaimCalculations() returned null", searchResults);
		
		ClaimCalculationRsrc tempRsrc = searchResults.getCollection().get(0);
		ClaimCalculationRsrc calculationToUpdate = service.getClaimCalculation(tempRsrc, false);

		//Assert.assertEquals("Claim Status not Open", ClaimStatusCodes.OPEN, calculationToUpdate.getClaimStatusCode());

		//Submit calculation
		calculationToUpdate.setCalculationStatusCode("SUBMITTED");
		ClaimCalculationRsrc updatedCalculation = service.updateClaimCalculation(calculationToUpdate, ClaimsServiceEnums.UpdateTypes.SUBMIT.toString());

		//Assert.assertEquals("UpdateClaimCalcUserGuid is not correct", "CEB90F1F826B146CE053E60A0A0A72D1", updatedCalculation.getUpdateClaimCalcUserGuid());
		
		logger.debug(">testSubmitClaimCalculation");
	}
	
	
	@Test
	public void testInsertDeleteClaimCalculation() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testInsertDeleteClaimCalculation");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//Making sure no calculation with the same claim number and version exists
		deleteClaimCalculation();

		ClaimCalculationListRsrc searchResults = service.getClaimCalculations(topLevelEndpoints, null, null, null, null, null, null, null, null, null, pageNumber, pageRowCount);
		
		ClaimCalculationRsrc calculationToCopy = new ClaimCalculationRsrc();
		boolean calculationFound = false;
		
		//Find a calculation with varieties
		if(searchResults.getTotalRowCount() > 0) {
			ClaimCalculationRsrc tempRsrc = new ClaimCalculationRsrc();

			for (int i = 0; i < searchResults.getTotalRowCount(); i++) {

				tempRsrc = searchResults.getCollection().get(i);
				
				//getClaimCalculations doesn't return the varieties so we have to get the single calculation
				calculationToCopy = service.getClaimCalculation(tempRsrc, false);
				
				if(calculationToCopy.getVarieties().size() > 0) {
					calculationFound = true;
					break;
				}
			}
			
			if(calculationFound) {
				//Clean up: check if there is a calculation with the claimNumber used for the new one and delete it
				
				//Create new claim calculation
				ClaimCalculationRsrc newCalculationRsrc = getClaimCalculationRsrc(calculationToCopy);
				
				//Add varieties
				newCalculationRsrc.setVarieties(copyVarieties(calculationToCopy.getVarieties()));
				
				//Create new calculation
				ClaimCalculationRsrc createdCalculation = service.createClaimCalculation(newCalculationRsrc);
				
				//getClaimCalculations doesn't return the varieties so we have to get the single calculation
				//calculationToCopy = service.getClaimCalculation(tempRsrc);

				Assert.assertEquals("CreateClaimCalcUserGuid is not correct", "CEB90F1F826B146CE053E60A0A0A72D1", createdCalculation.getCreateClaimCalcUserGuid());
				Assert.assertEquals("UpdateClaimCalcUserGuid is not correct", "CEB90F1F826B146CE053E60A0A0A72D1", createdCalculation.getUpdateClaimCalcUserGuid());
				
				//Delete claim
				//service.deleteClaimCalculation(newCalculationRsrc);
				
			} else {
				Assert.fail("No calculation with varieties found.");
			}

		} else {
			Assert.fail("No calculation found.");
		}
		
		
		logger.debug(">testInsertDeleteClaimCalculation");
	}	
	
	private void deleteClaimCalculation() throws CirrasClaimServiceException {
		//Check if the claimNumber with calculation version already exists and delete it if it does
		ClaimCalculationListRsrc searchResults = service.getClaimCalculations(topLevelEndpoints, claimNumber.toString(), null, null, null, null, null, null, null, null, pageNumber, pageRowCount);
		if(searchResults.getTotalRowCount() > 0) {
			ClaimCalculationRsrc tempRsrc = new ClaimCalculationRsrc();
			for (int i = 0; i < searchResults.getTotalRowCount(); i++) {
				tempRsrc = searchResults.getCollection().get(i);
				
				if(tempRsrc.getCalculationVersion() == 1) {
					ClaimCalculationRsrc calculationToDel = service.getClaimCalculation(tempRsrc, false);
					//Delete claim
					service.deleteClaimCalculation(calculationToDel);
					break;
				}
			}
		}
	}

	private void deleteClaimCalculation(Integer claimNumber, String claimCalculationGuid) throws CirrasClaimServiceException {
		if ( claimNumber != null && claimCalculationGuid != null ) {
			//Check if the claimNumber with calculation version already exists and delete it if it does
			ClaimCalculationListRsrc searchResults = service.getClaimCalculations(topLevelEndpoints, claimNumber.toString(), null, null, null, null, null, null, null, null, pageNumber, pageRowCount);
			if(searchResults.getTotalRowCount() > 0) {
				for (int i = 0; i < searchResults.getTotalRowCount(); i++) {
					
					if ( searchResults.getCollection().get(i).getClaimCalculationGuid().equals(claimCalculationGuid) ) {
						ClaimCalculationRsrc calculationToDel = service.getClaimCalculation(searchResults.getCollection().get(i), false);
						service.deleteClaimCalculation(calculationToDel);
					}
				}
			}
		}
	}
	
	
	private ClaimCalculationRsrc getClaimCalculationRsrc(ClaimCalculationRsrc calculationToCopy) {
		ClaimCalculationRsrc newCalculation = new ClaimCalculationRsrc();

		newCalculation.setClaimNumber(claimNumber);

		newCalculation.setPrimaryPerilCode(calculationToCopy.getPrimaryPerilCode());
		newCalculation.setSecondaryPerilCode(calculationToCopy.getSecondaryPerilCode());
		newCalculation.setClaimStatusCode(calculationToCopy.getClaimStatusCode());
		newCalculation.setCommodityCoverageCode(calculationToCopy.getCommodityCoverageCode());
		newCalculation.setCalculationStatusCode(calculationToCopy.getCalculationStatusCode());
		newCalculation.setInsurancePlanId(calculationToCopy.getInsurancePlanId());
		newCalculation.setInsurancePlanName(calculationToCopy.getInsurancePlanName());
		newCalculation.setCropCommodityId(calculationToCopy.getCropCommodityId());
		newCalculation.setCropYear(calculationToCopy.getCropYear());
		newCalculation.setPolicyNumber(calculationToCopy.getPolicyNumber());
		newCalculation.setContractId(calculationToCopy.getContractId());
		newCalculation.setCalculationVersion(calculationToCopy.getCalculationVersion());
		newCalculation.setGrowerNumber(calculationToCopy.getGrowerNumber());
		newCalculation.setGrowerName(calculationToCopy.getGrowerName());
		newCalculation.setGrowerAddressLine1(calculationToCopy.getGrowerAddressLine1());
		newCalculation.setGrowerAddressLine2(calculationToCopy.getGrowerAddressLine2());
		newCalculation.setGrowerPostalCode(calculationToCopy.getGrowerPostalCode());
		newCalculation.setGrowerCity(calculationToCopy.getGrowerCity());
		newCalculation.setGrowerProvince(calculationToCopy.getGrowerProvince());
		newCalculation.setTotalClaimAmount(calculationToCopy.getTotalClaimAmount());
		newCalculation.setCalculationComment(calculationToCopy.getCalculationComment());
		newCalculation.setSubmittedByUserid(calculationToCopy.getSubmittedByUserid());
		newCalculation.setSubmittedByName(calculationToCopy.getSubmittedByName());
		newCalculation.setSubmittedByDate(calculationToCopy.getSubmittedByDate());
		newCalculation.setRecommendedByUserid(calculationToCopy.getRecommendedByUserid());
		newCalculation.setRecommendedByName(calculationToCopy.getRecommendedByName());
		newCalculation.setRecommendedByDate(calculationToCopy.getRecommendedByDate());
		newCalculation.setApprovedByUserid(calculationToCopy.getApprovedByUserid());
		newCalculation.setApprovedByName(calculationToCopy.getApprovedByName());
		newCalculation.setApprovedByDate(calculationToCopy.getApprovedByDate());
		newCalculation.setCalculateIivInd(calculationToCopy.getCalculateIivInd());
		newCalculation.setHasChequeReqInd(calculationToCopy.getHasChequeReqInd());
		
		return newCalculation;
	}
	
	private List<ClaimCalculationVariety> copyVarieties(List<ClaimCalculationVariety> oldVarieties) {
	List<ClaimCalculationVariety> varieties = new ArrayList<ClaimCalculationVariety>();
	
	for(ClaimCalculationVariety ov: oldVarieties) {
		
		ClaimCalculationVariety nv = new ClaimCalculationVariety();

		//nv.setClaimCalculationGuid(ov.getClaimCalculationGuid()); -> This is set in the createClaimCalculation method
		nv.setCropVarietyId(ov.getCropVarietyId());
		nv.setAveragePrice(ov.getAveragePrice());
		nv.setAveragePriceOverride(ov.getAveragePriceOverride());
		nv.setAveragePriceFinal(ov.getAveragePriceFinal());
		nv.setInsurableValue(ov.getInsurableValue());
		nv.setYieldAssessedReason(ov.getYieldAssessedReason());
		nv.setYieldAssessed(ov.getYieldAssessed());
		nv.setYieldTotal(ov.getYieldTotal());
		nv.setYieldActual(ov.getYieldActual());
		nv.setVarietyProductionValue(ov.getVarietyProductionValue());
		
		varieties.add(nv);
	}
	
	return varieties;
}

	//Use this to delete the claim calc created by testClaimCalculationOutOfSyncFlags() if it fails without completing.
	@Test
	public void testCleanupOutOfSyncTest() throws CirrasClaimServiceException {

		ClaimCalculationListRsrc searchResults = service.getClaimCalculations(topLevelEndpoints, "28082", null, null, null, null, null, null, null, null, pageNumber, pageRowCount);
		ClaimCalculationRsrc claimCalc = searchResults.getCollection().get(0);
		claimCalc = service.getClaimCalculation(claimCalc, false);
		service.deleteClaimCalculation(claimCalc);		
	}
	
	@Test
	public void testClaimCalculationOutOfSyncFlags() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testClaimCalculationOutOfSyncFlags");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		//1. Create a new Claim Calculation, verify that Out of Sync flags are all false.
		String testClaimNumber = "28082";  // Needs to be manually set to a real, valid WINE GRAPES claim in CIRRAS db.

		Assert.assertFalse("testClaimNumber must be set before this test can be run", testClaimNumber.equals("TODO"));
		
		ClaimListRsrc claimList = service.getClaimList(topLevelEndpoints, testClaimNumber, null, null, null, null, pageNumber, pageRowCount);
		Assert.assertNotNull("getClaimList() returned null", claimList);
		Assert.assertTrue("getClaimList() returned empty list or more than one result", claimList.getCollection().size() == 1);

		ClaimRsrc claim = claimList.getCollection().get(0);

		ClaimCalculationRsrc claimCalc = service.getClaim(claim);

		claimCalc = service.createClaimCalculation(claimCalc);

		Assert.assertNotNull("Saved Claim has no varieties", claimCalc.getVarieties());
		Assert.assertTrue("Saved Claim has 0 varieties", claimCalc.getVarieties().size() > 0);

		assertOutOfSyncFlagsFalse(claimCalc);
		
		//2. Update ClaimCalculation, setting each field to check the corresponding Out of Sync flag.
		//a. GrowerAddrLine1
		String oldGrowerAddrLine1 = claimCalc.getGrowerAddressLine1();
		claimCalc.setGrowerAddressLine1("ABCDEFG");
		claimCalc = service.updateClaimCalculation(claimCalc, null);
		assertOutOfSyncFlagsFalseExceptOne(claimCalc, "GrowerAddrLine1");
		claimCalc.setGrowerAddressLine1(oldGrowerAddrLine1);
		
		//b. GrowerAddrLine2
		String oldGrowerAddressLine2 = claimCalc.getGrowerAddressLine2();
		claimCalc.setGrowerAddressLine2("ABCDEFG");
		claimCalc = service.updateClaimCalculation(claimCalc, null);
		assertOutOfSyncFlagsFalseExceptOne(claimCalc, "GrowerAddrLine2");
		claimCalc.setGrowerAddressLine2(oldGrowerAddressLine2);

		
		//c. GrowerCity
		String oldGrowerCity = claimCalc.getGrowerCity();
		claimCalc.setGrowerCity("ABCDEFG");
		claimCalc = service.updateClaimCalculation(claimCalc, null);
		assertOutOfSyncFlagsFalseExceptOne(claimCalc, "GrowerCity");
		claimCalc.setGrowerCity(oldGrowerCity);

		//c. GrowerProvince
		String oldGrowerProvince = claimCalc.getGrowerProvince();
		claimCalc.setGrowerProvince("QB");
		claimCalc = service.updateClaimCalculation(claimCalc, null);
		assertOutOfSyncFlagsFalseExceptOne(claimCalc, "GrowerProvince");
		claimCalc.setGrowerProvince(oldGrowerProvince);

		
		//d. GrowerName
		String oldGrowerName = claimCalc.getGrowerName();
		claimCalc.setGrowerName("ABCDEFG");
		claimCalc = service.updateClaimCalculation(claimCalc, null);
		assertOutOfSyncFlagsFalseExceptOne(claimCalc, "GrowerName");
		claimCalc.setGrowerName(oldGrowerName);

		
		//e. GrowerNumber
		Integer oldGrowerNumber = claimCalc.getGrowerNumber();
		claimCalc.setGrowerNumber(98765);
		claimCalc = service.updateClaimCalculation(claimCalc, null);
		assertOutOfSyncFlagsFalseExceptOne(claimCalc, "GrowerNumber");
		claimCalc.setGrowerNumber(oldGrowerNumber);

				
		//f. GrowerPostalCode
		String oldGrowerPostalCode = claimCalc.getGrowerPostalCode();
		claimCalc.setGrowerPostalCode("ABCDEFG");
		claimCalc = service.updateClaimCalculation(claimCalc, null);
		assertOutOfSyncFlagsFalseExceptOne(claimCalc, "GrowerPostalCode");
		claimCalc.setGrowerPostalCode(oldGrowerPostalCode);

		
		//h. AvgPrice
		ClaimCalculationVariety claimCalcVariety = claimCalc.getVarieties().get(0);
		Integer varietyId = claimCalcVariety.getCropVarietyId();
		
		Double oldAveragePrice = claimCalcVariety.getAveragePrice();
		claimCalcVariety.setAveragePrice(123.45);
		claimCalc = service.updateClaimCalculation(claimCalc, null);
		claimCalcVariety = getClaimCalcVarietyById(claimCalc, varietyId);
		assertOutOfSyncFlagsFalseExceptOne(claimCalc, "AvgPrice", varietyId);
		claimCalcVariety.setAveragePrice(oldAveragePrice);
		claimCalc = service.updateClaimCalculation(claimCalc, null);
		claimCalcVariety = getClaimCalcVarietyById(claimCalc, varietyId);
		assertOutOfSyncFlagsFalse(claimCalc);
		
		
		//i. VarietyAdded
		Iterator<ClaimCalculationVariety> it = claimCalc.getVarieties().iterator();
		while (it.hasNext()) {
			ClaimCalculationVariety v = it.next();
			if (v.getCropVarietyId().equals(varietyId)) {
				it.remove();
			}
		}

		claimCalc = service.updateClaimCalculation(claimCalc, null);
		assertOutOfSyncFlagsFalseExceptOne(claimCalc, "VarietyAdded");

		ClaimCalculationVariety nv = new ClaimCalculationVariety();

		copyClaimCalcVariety(claimCalcVariety, nv);
		

		claimCalc.getVarieties().add(nv);

		claimCalc = service.updateClaimCalculation(claimCalc, null);
		assertOutOfSyncFlagsFalse(claimCalc);

		
		//j. VarietyRemoved
		varietyId = 1010393;
		Assert.assertNull("Variety " + varietyId + " already exists. Change test for VarietyRemoved to use a different variety", getClaimCalcVarietyById(claimCalc, varietyId));
		
		nv = new ClaimCalculationVariety();

		copyClaimCalcVariety(claimCalcVariety, nv);
		nv.setCropVarietyId(varietyId);

		claimCalc.getVarieties().add(nv);
		claimCalc = service.updateClaimCalculation(claimCalc, null);
		assertOutOfSyncFlagsFalseExceptOne(claimCalc, "VarietyRemoved", varietyId);

		it = claimCalc.getVarieties().iterator();
		while (it.hasNext()) {
			ClaimCalculationVariety v = it.next();
			if (v.getCropVarietyId().equals(varietyId)) {
				it.remove();
			}
		}

		claimCalc = service.updateClaimCalculation(claimCalc, null);				
		assertOutOfSyncFlagsFalse(claimCalc);

		//k. Out of sync flags not set for certain statuses.
		claimCalc.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.APPROVED.toString());
		claimCalc = service.updateClaimCalculation(claimCalc, null);
		assertOutOfSyncFlagsNull(claimCalc);

		claimCalc.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.ARCHIVED.toString());
		claimCalc = service.updateClaimCalculation(claimCalc, null);
		assertOutOfSyncFlagsNull(claimCalc);
		
		//3. Delete the Claim Calculation.
		service.deleteClaimCalculation(claimCalc);
		
		logger.debug(">testClaimCalculationOutOfSyncFlags");
	}
	
	@Test
	public void testGrapesClaimCalculationOutOfSyncFlags() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testGrapesClaimCalculationOutOfSyncFlags");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		//1. Create a new Claim Calculation, verify that Out of Sync flags are all false.
		String testClaimNumber = "28082";  // Needs to be manually set to a real, valid Grapes Quantity claim in CIRRAS db.

		Assert.assertFalse("testClaimNumber must be set before this test can be run", testClaimNumber.equals("TODO"));
		
		ClaimListRsrc claimList = service.getClaimList(topLevelEndpoints, testClaimNumber, null, null, null, null, pageNumber, pageRowCount);
		Assert.assertNotNull("getClaimList() returned null", claimList);
		Assert.assertTrue("getClaimList() returned empty list or more than one result", claimList.getCollection().size() == 1);

		ClaimRsrc claim = claimList.getCollection().get(0);

		ClaimCalculationRsrc claimCalc = service.getClaim(claim);

		claimCalc = service.createClaimCalculation(claimCalc);

		assertOutOfSyncFlagsFalse(claimCalc);
		
		//2. Update ClaimCalculation, setting each field to check the corresponding Out of Sync flag.
		//4 Grapes specific fields
		if(claimCalc.getClaimCalculationGrapes() != null) {
		
			//InsurableValueSelected
			Double oldInsurableValueSelected = claimCalc.getClaimCalculationGrapes().getInsurableValueSelected();
			claimCalc.getClaimCalculationGrapes().setInsurableValueSelected(123.4567);
			claimCalc = service.updateClaimCalculation(claimCalc, null);
			assertOutOfSyncFlagsFalseExceptOne(claimCalc, "InsurableValueSelected");
			claimCalc.getClaimCalculationGrapes().setInsurableValueSelected(oldInsurableValueSelected);


			//InsurableValueHundredPct
			Double oldInsurableValueHundredPct = claimCalc.getClaimCalculationGrapes().getInsurableValueHundredPercent();
			claimCalc.getClaimCalculationGrapes().setInsurableValueHundredPercent(123.4567);
			claimCalc = service.updateClaimCalculation(claimCalc, null);
			assertOutOfSyncFlagsFalseExceptOne(claimCalc, "InsurableValueHundredPct");
			claimCalc.getClaimCalculationGrapes().setInsurableValueHundredPercent(oldInsurableValueHundredPct);

			
			//CoverageAmount
			Double oldCoverageAmount = claimCalc.getClaimCalculationGrapes().getCoverageAmount();
			claimCalc.getClaimCalculationGrapes().setCoverageAmount(123.45);
			claimCalc = service.updateClaimCalculation(claimCalc, null);
			assertOutOfSyncFlagsFalseExceptOne(claimCalc, "CoverageAmount");
			claimCalc.getClaimCalculationGrapes().setCoverageAmount(oldCoverageAmount);

		}
		
		claimCalc = service.updateClaimCalculation(claimCalc, null);				
		assertOutOfSyncFlagsFalse(claimCalc);

		//k. Out of sync flags not set for certain statuses.
		claimCalc.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.APPROVED.toString());
		claimCalc = service.updateClaimCalculation(claimCalc, null);
		assertOutOfSyncFlagsNull(claimCalc);

		claimCalc.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.ARCHIVED.toString());
		claimCalc = service.updateClaimCalculation(claimCalc, null);
		assertOutOfSyncFlagsNull(claimCalc);
		
		//3. Delete the Claim Calculation.
		service.deleteClaimCalculation(claimCalc);
		
		logger.debug(">testGrapesClaimCalculationOutOfSyncFlags");
	}

	@Test
	public void testBerriesClaimCalculationOutOfSyncFlags() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testBerriesClaimCalculationOutOfSyncFlags");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		//1. Create a new Claim Calculation, verify that Out of Sync flags are all false.
		String testClaimNumber = "28214";  // Needs to be manually set to a real, valid BERRIES Quantity claim in CIRRAS db.

		Assert.assertFalse("testClaimNumber must be set before this test can be run", testClaimNumber.equals("TODO"));
		
		ClaimListRsrc claimList = service.getClaimList(topLevelEndpoints, testClaimNumber, null, null, null, null, pageNumber, pageRowCount);
		Assert.assertNotNull("getClaimList() returned null", claimList);
		Assert.assertTrue("getClaimList() returned empty list or more than one result", claimList.getCollection().size() == 1);

		ClaimRsrc claim = claimList.getCollection().get(0);

		ClaimCalculationRsrc claimCalc = service.getClaim(claim);

		claimCalc = service.createClaimCalculation(claimCalc);

		assertOutOfSyncFlagsFalse(claimCalc);
		
		//2. Update ClaimCalculation, setting each field to check the corresponding Out of Sync flag.
		//4 Berries specific fields
		if(claimCalc.getClaimCalculationBerries() != null) {
			//Total Probable Yield
			Double oldTotalProbableYield = claimCalc.getClaimCalculationBerries().getTotalProbableYield();
			claimCalc.getClaimCalculationBerries().setTotalProbableYield(oldTotalProbableYield - 100);
			claimCalc = service.updateClaimCalculation(claimCalc, null);
			assertOutOfSyncFlagsFalseExceptOne(claimCalc, "TotalProbableYield");
			claimCalc.getClaimCalculationBerries().setTotalProbableYield(oldTotalProbableYield);
			
			//Deductible Level
			Integer oldDeductibleLevel = claimCalc.getClaimCalculationBerries().getDeductibleLevel();
			claimCalc.getClaimCalculationBerries().setDeductibleLevel(oldDeductibleLevel + 10);
			claimCalc = service.updateClaimCalculation(claimCalc, null);
			assertOutOfSyncFlagsFalseExceptOne(claimCalc, "DeductibleLevel");
			claimCalc.getClaimCalculationBerries().setDeductibleLevel(oldDeductibleLevel);

			//Production Guarantee
			Double oldProductionGuarantee = claimCalc.getClaimCalculationBerries().getProductionGuarantee();
			claimCalc.getClaimCalculationBerries().setProductionGuarantee(oldProductionGuarantee + 10.5);
			claimCalc = service.updateClaimCalculation(claimCalc, null);
			assertOutOfSyncFlagsFalseExceptOne(claimCalc, "ProductionGuarantee");
			claimCalc.getClaimCalculationBerries().setProductionGuarantee(oldProductionGuarantee);

			//Declared Acres
			Double oldDeclaredAcres = claimCalc.getClaimCalculationBerries().getDeclaredAcres();
			claimCalc.getClaimCalculationBerries().setDeclaredAcres(oldDeclaredAcres + 0.5);
			claimCalc = service.updateClaimCalculation(claimCalc, null);
			assertOutOfSyncFlagsFalseExceptOne(claimCalc, "DeclaredAcres");
			claimCalc.getClaimCalculationBerries().setDeclaredAcres(oldDeclaredAcres);
		
			//g. InsurableValueSelected
			Double oldInsurableValueSelected = claimCalc.getClaimCalculationBerries().getInsurableValueSelected();
			claimCalc.getClaimCalculationBerries().setInsurableValueSelected(123.4567);
			claimCalc = service.updateClaimCalculation(claimCalc, null);
			assertOutOfSyncFlagsFalseExceptOne(claimCalc, "InsurableValueSelected");
			claimCalc.getClaimCalculationBerries().setInsurableValueSelected(oldInsurableValueSelected);


			//h. InsurableValueHundredPct
			Double oldInsurableValueHundredPct = claimCalc.getClaimCalculationBerries().getInsurableValueHundredPercent();
			claimCalc.getClaimCalculationBerries().setInsurableValueHundredPercent(123.4567);
			claimCalc = service.updateClaimCalculation(claimCalc, null);
			assertOutOfSyncFlagsFalseExceptOne(claimCalc, "InsurableValueHundredPct");
			claimCalc.getClaimCalculationBerries().setInsurableValueHundredPercent(oldInsurableValueHundredPct);

		}
		
		claimCalc = service.updateClaimCalculation(claimCalc, null);				
		assertOutOfSyncFlagsFalse(claimCalc);

		//k. Out of sync flags not set for certain statuses.
		claimCalc.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.APPROVED.toString());
		claimCalc = service.updateClaimCalculation(claimCalc, null);
		assertOutOfSyncFlagsNull(claimCalc);

		claimCalc.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.ARCHIVED.toString());
		claimCalc = service.updateClaimCalculation(claimCalc, null);
		assertOutOfSyncFlagsNull(claimCalc);
		
		//3. Delete the Claim Calculation.
		service.deleteClaimCalculation(claimCalc);
		
		logger.debug(">testBerriesClaimCalculationOutOfSyncFlags");
	}
	
	@Test
	public void testBerriesClaimCalculationReplace() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testBerriesClaimCalculationReplace");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//Choose a claim number of a claim without a calculation
		String claimNumber = "28214";
		
		ClaimListRsrc claimList = service.getClaimList(topLevelEndpoints, claimNumber, null, null, null, null, pageNumber, pageRowCount);
		Assert.assertNotNull("getClaimList() returned null", claimList);
		Assert.assertTrue("getClaimList() returned empty list or more than one result", claimList.getCollection().size() == 1);

		ClaimRsrc claim = claimList.getCollection().get(0);

		ClaimCalculationRsrc calculationToUpdate = service.getClaim(claim);

		calculationToUpdate = service.createClaimCalculation(calculationToUpdate);
		
		//Original values
		Double originalDeclaredAcres = calculationToUpdate.getClaimCalculationBerries().getDeclaredAcres();
		Integer originalDeductibleLevel = calculationToUpdate.getClaimCalculationBerries().getDeductibleLevel();
		Double originalTotalProbableYield = calculationToUpdate.getClaimCalculationBerries().getTotalProbableYield();
		Double originalProductionGuarantee = calculationToUpdate.getClaimCalculationBerries().getProductionGuarantee();
		Double originalInsurableValueSelected = calculationToUpdate.getClaimCalculationBerries().getInsurableValueSelected();
		Double originalInsurableValueHundredPercent = calculationToUpdate.getClaimCalculationBerries().getInsurableValueHundredPercent();
		Double originalMaxCoverageAmount = calculationToUpdate.getClaimCalculationBerries().getMaxCoverageAmount();
		
		//Update values for pulled in data to test if replacing NEW and COPY works correctly
		Double updatedDeclaredAcres = originalDeclaredAcres + 2;
		Integer updatedDeductibleLevel = originalDeductibleLevel - 5;
		Double updatedTotalProbableYield = originalTotalProbableYield +1;
		Double updatedProductionGuarantee = originalProductionGuarantee +1;
		Double updatedInsurableValueSelected = originalInsurableValueSelected + 1;
		Double updatedInsurableValueHundredPercent = originalInsurableValueHundredPercent +1;
		Double updatedMaxCoverageAmount = originalMaxCoverageAmount +1;
		
		calculationToUpdate.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.APPROVED.toString());
		calculationToUpdate.getClaimCalculationBerries().setDeclaredAcres(updatedDeclaredAcres);
		calculationToUpdate.getClaimCalculationBerries().setDeductibleLevel(updatedDeductibleLevel);
		calculationToUpdate.getClaimCalculationBerries().setTotalProbableYield(updatedTotalProbableYield);
		calculationToUpdate.getClaimCalculationBerries().setProductionGuarantee(updatedProductionGuarantee);
		calculationToUpdate.getClaimCalculationBerries().setInsurableValueSelected(updatedInsurableValueSelected);
		calculationToUpdate.getClaimCalculationBerries().setInsurableValueHundredPercent(updatedInsurableValueHundredPercent);
		calculationToUpdate.getClaimCalculationBerries().setMaxCoverageAmount(updatedMaxCoverageAmount);
		
		//Saving updated values
		calculationToUpdate = service.updateClaimCalculation(calculationToUpdate, null);

		//NEW
		//Update calculation
		calculationToUpdate.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.ARCHIVED.toString());
		ClaimCalculationRsrc newCalculation = service.updateClaimCalculation(calculationToUpdate, ClaimsServiceEnums.UpdateTypes.REPLACE_NEW.toString());

		//Check if newCalculation contains the original values from the replaced one
		Assert.assertEquals("New Calculation Status", newCalculation.getCalculationStatusCode(), ClaimsServiceEnums.CalculationStatusCodes.DRAFT.toString());
		Assert.assertEquals("New Declared Acres not consistent", originalDeclaredAcres, newCalculation.getClaimCalculationBerries().getDeclaredAcres());
		Assert.assertEquals("New Deductible Level not correct", originalDeductibleLevel, newCalculation.getClaimCalculationBerries().getDeductibleLevel());
		Assert.assertEquals("New Total Probably Yield not correct", originalTotalProbableYield, newCalculation.getClaimCalculationBerries().getTotalProbableYield());
		Assert.assertEquals("New Production Guarantee not correct", originalProductionGuarantee, newCalculation.getClaimCalculationBerries().getProductionGuarantee());
		Assert.assertEquals("New Insurable Value Selected Value not correct", originalInsurableValueSelected, newCalculation.getClaimCalculationBerries().getInsurableValueSelected());
		Assert.assertEquals("New Insurable Value 100% not correct", originalInsurableValueHundredPercent, newCalculation.getClaimCalculationBerries().getInsurableValueHundredPercent());
		Assert.assertEquals("New Coverage Amount not correct", originalMaxCoverageAmount, newCalculation.getClaimCalculationBerries().getMaxCoverageAmount());
		
		//Delete new calculation
		service.deleteClaimCalculation(newCalculation);
		
		//Reset calculation to be replaced to approved
		//Need to load the original calculation again to prevent precondition error (http 412) because of etag differences
		calculationToUpdate = service.getClaimCalculation(calculationToUpdate, false);
		calculationToUpdate.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.APPROVED.toString());
		calculationToUpdate = service.updateClaimCalculation(calculationToUpdate, null);
		
		//COPY
		calculationToUpdate.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.ARCHIVED.toString());
		newCalculation = service.updateClaimCalculation(calculationToUpdate, ClaimsServiceEnums.UpdateTypes.REPLACE_COPY.toString());

		//Check if newCalculation contains the updated values from the replaced one
		Assert.assertEquals("Copy Calculation Status", newCalculation.getCalculationStatusCode(), ClaimsServiceEnums.CalculationStatusCodes.DRAFT.toString());
		Assert.assertEquals("Copy Declared Acres not consistent", updatedDeclaredAcres, newCalculation.getClaimCalculationBerries().getDeclaredAcres());
		Assert.assertEquals("Copy Deductible Level not correct", updatedDeductibleLevel, newCalculation.getClaimCalculationBerries().getDeductibleLevel());
		Assert.assertEquals("New Total Probably Yield not correct", updatedTotalProbableYield, newCalculation.getClaimCalculationBerries().getTotalProbableYield());
		Assert.assertEquals("New Production Guarantee not correct", updatedProductionGuarantee, newCalculation.getClaimCalculationBerries().getProductionGuarantee());
		Assert.assertEquals("New Insurable Value Selected Value not correct", updatedInsurableValueSelected, newCalculation.getClaimCalculationBerries().getInsurableValueSelected());
		Assert.assertEquals("New Insurable Value 100% not correct", updatedInsurableValueHundredPercent, newCalculation.getClaimCalculationBerries().getInsurableValueHundredPercent());
		Assert.assertEquals("New Max Coverage Amount not correct", updatedMaxCoverageAmount, newCalculation.getClaimCalculationBerries().getMaxCoverageAmount());

		
		//Delete - Clean up
		service.deleteClaimCalculation(newCalculation);
		//Need to load the original calculation again to prevent precondition error (http 412) because of etag differences
		calculationToUpdate = service.getClaimCalculation(calculationToUpdate, false);
		service.deleteClaimCalculation(calculationToUpdate);

		logger.debug(">testBerriesClaimCalculationReplace");
	}	

	@Test
	public void testPlantUnitsClaimCalculationOutOfSyncFlags() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testPlantUnitsClaimCalculationOutOfSyncFlags");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		//1. Create a new Claim Calculation, verify that Out of Sync flags are all false.
		String testClaimNumber = "27774";  // Needs to be manually set to a real, valid WINE GRAPES claim in CIRRAS db.

		Assert.assertFalse("testClaimNumber must be set before this test can be run", testClaimNumber.equals("TODO"));
		
		ClaimListRsrc claimList = service.getClaimList(topLevelEndpoints, testClaimNumber, null, null, null, null, pageNumber, pageRowCount);
		Assert.assertNotNull("getClaimList() returned null", claimList);
		Assert.assertTrue("getClaimList() returned empty list or more than one result", claimList.getCollection().size() == 1);

		ClaimRsrc claim = claimList.getCollection().get(0);

		ClaimCalculationRsrc claimCalc = service.getClaim(claim);

		claimCalc = service.createClaimCalculation(claimCalc);

		assertOutOfSyncFlagsFalse(claimCalc);
		
		//2. Update ClaimCalculation, setting each field to check the corresponding Out of Sync flag.
		if(claimCalc.getClaimCalculationPlantUnits() != null) {
			//Insured Plants
			Double oldInsuredUnits = claimCalc.getClaimCalculationPlantUnits().getInsuredUnits();
			claimCalc.getClaimCalculationPlantUnits().setInsuredUnits(oldInsuredUnits - 100);
			claimCalc = service.updateClaimCalculation(claimCalc, null);
			assertOutOfSyncFlagsFalseExceptOne(claimCalc, "PlantUnitsInsuredUnits");
			claimCalc.getClaimCalculationPlantUnits().setInsuredUnits(oldInsuredUnits);
			
			//Deductible Level
			Integer oldDeductibleLevel = claimCalc.getClaimCalculationPlantUnits().getDeductibleLevel();
			claimCalc.getClaimCalculationPlantUnits().setDeductibleLevel(oldDeductibleLevel + 10);
			claimCalc = service.updateClaimCalculation(claimCalc, null);
			assertOutOfSyncFlagsFalseExceptOne(claimCalc, "PlantUnitsDeductibleLevel");
			claimCalc.getClaimCalculationPlantUnits().setDeductibleLevel(oldDeductibleLevel);

			//Insurable Value
			Double oldInsurableValue = claimCalc.getClaimCalculationPlantUnits().getInsurableValue();
			claimCalc.getClaimCalculationPlantUnits().setInsurableValue(oldInsurableValue - 1);
			claimCalc = service.updateClaimCalculation(claimCalc, null);
			assertOutOfSyncFlagsFalseExceptOne(claimCalc, "PlantUnitsInsurableValue");
			claimCalc.getClaimCalculationPlantUnits().setInsurableValue(oldInsurableValue);
		
		}

		claimCalc = service.updateClaimCalculation(claimCalc, null);				
		assertOutOfSyncFlagsFalse(claimCalc);

		//k. Out of sync flags not set for certain statuses.
		claimCalc.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.APPROVED.toString());
		claimCalc = service.updateClaimCalculation(claimCalc, null);
		assertOutOfSyncFlagsNull(claimCalc);

		claimCalc.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.ARCHIVED.toString());
		claimCalc = service.updateClaimCalculation(claimCalc, null);
		assertOutOfSyncFlagsNull(claimCalc);
		
		//3. Delete the Claim Calculation.
		service.deleteClaimCalculation(claimCalc);
		
		logger.debug(">testPlantUnitsClaimCalculationOutOfSyncFlags");
	}
	
	@Test
	public void testPlantUnitsClaimCalculationRefresh() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testPlantUnitsClaimCalculationRefresh");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		//1. Create a new Claim Calculation, verify that Out of Sync flags are all false.
		String testClaimNumber = "27774";  // Needs to be manually set to a real, valid WINE GRAPES claim in CIRRAS db.

		Assert.assertFalse("testClaimNumber must be set before this test can be run", testClaimNumber.equals("TODO"));
		
		ClaimListRsrc claimList = service.getClaimList(topLevelEndpoints, testClaimNumber, null, null, null, null, pageNumber, pageRowCount);
		Assert.assertNotNull("getClaimList() returned null", claimList);
		Assert.assertTrue("getClaimList() returned empty list or more than one result", claimList.getCollection().size() == 1);

		ClaimRsrc claim = claimList.getCollection().get(0);

		ClaimCalculationRsrc claimCalc = service.getClaim(claim);

		claimCalc = service.createClaimCalculation(claimCalc);
		
		assertOutOfSyncFlagsPlantUnits(claimCalc, false);
		
		//2. Update ClaimCalculation, setting each field to be out of sync with claim.

		// ClaimCalculation fields
		if(claimCalc.getClaimCalculationPlantUnits() != null) {
			//Insured Plants
			Double oldInsuredUnits = claimCalc.getClaimCalculationPlantUnits().getInsuredUnits();
			claimCalc.getClaimCalculationPlantUnits().setInsuredUnits(oldInsuredUnits - 100);
			
			//Deductible Level
			Integer oldDeductibleLevel = claimCalc.getClaimCalculationPlantUnits().getDeductibleLevel();
			claimCalc.getClaimCalculationPlantUnits().setDeductibleLevel(oldDeductibleLevel + 10);

			//Insurable Value
			Double oldInsurableValue = claimCalc.getClaimCalculationPlantUnits().getInsurableValue();
			claimCalc.getClaimCalculationPlantUnits().setInsurableValue(oldInsurableValue - 1);
		
		}
		
		claimCalc = service.updateClaimCalculation(claimCalc, null);

		assertOutOfSyncFlagsPlantUnits(claimCalc, true);

		claimCalc = service.getClaimCalculation(claimCalc, true);
		
		assertOutOfSyncFlagsPlantUnits(claimCalc, false);
		
		claimCalc = service.updateClaimCalculation(claimCalc, null);

		//4. Delete the Claim Calculation.
		service.deleteClaimCalculation(claimCalc);
		
		logger.debug(">testPlantUnitsClaimCalculationRefresh");
	}
	
	@Test
	public void testPlantUnitsClaimCalculationReplace() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testPlantUnitsClaimCalculationReplace");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//Choose a claim number of a claim without a calculation
		String claimNumber = "27774";
		
		ClaimListRsrc claimList = service.getClaimList(topLevelEndpoints, claimNumber, null, null, null, null, pageNumber, pageRowCount);
		Assert.assertNotNull("getClaimList() returned null", claimList);
		Assert.assertTrue("getClaimList() returned empty list or more than one result", claimList.getCollection().size() == 1);

		ClaimRsrc claim = claimList.getCollection().get(0);

		ClaimCalculationRsrc calculationToUpdate = service.getClaim(claim);

		calculationToUpdate = service.createClaimCalculation(calculationToUpdate);
		
		//Original values
		Double originalInsuredUnits = calculationToUpdate.getClaimCalculationPlantUnits().getInsuredUnits();
		Integer originalDeductibleLevel = calculationToUpdate.getClaimCalculationPlantUnits().getDeductibleLevel();
		Double originalInsurableValue = calculationToUpdate.getClaimCalculationPlantUnits().getInsurableValue();
		
		//Update values for pulled in data to test if replacing NEW and COPY works correctly
		Double updatedInsuredUnits = originalInsuredUnits - 100;
		Integer updatedDeductibleLevel = originalDeductibleLevel + 10;
		Double updatedInsurableValue = originalInsurableValue - 1;
		
		calculationToUpdate.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.APPROVED.toString());
		calculationToUpdate.getClaimCalculationPlantUnits().setInsuredUnits(updatedInsuredUnits);
		calculationToUpdate.getClaimCalculationPlantUnits().setDeductibleLevel(updatedDeductibleLevel);
		calculationToUpdate.getClaimCalculationPlantUnits().setInsurableValue(updatedInsurableValue);
		
		//Saving updated values
		calculationToUpdate = service.updateClaimCalculation(calculationToUpdate, null);

		//NEW
		//Update calculation
		calculationToUpdate.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.ARCHIVED.toString());
		ClaimCalculationRsrc newCalculation = service.updateClaimCalculation(calculationToUpdate, ClaimsServiceEnums.UpdateTypes.REPLACE_NEW.toString());

		//Check if newCalculation contains the original values from the replaced one
		Assert.assertEquals("New Calculation Status", newCalculation.getCalculationStatusCode(), ClaimsServiceEnums.CalculationStatusCodes.DRAFT.toString());
		Assert.assertEquals("New Insured Units not consistent", originalInsuredUnits, newCalculation.getClaimCalculationPlantUnits().getInsuredUnits());
		Assert.assertEquals("New Deductible Level not correct", originalDeductibleLevel, newCalculation.getClaimCalculationPlantUnits().getDeductibleLevel());
		Assert.assertEquals("New Insurable Value not correct", originalInsurableValue, newCalculation.getClaimCalculationPlantUnits().getInsurableValue());
		
		//Delete new calculation
		service.deleteClaimCalculation(newCalculation);
		
		//Reset calculation to be replaced to approved
		//Need to load the original calculation again to prevent precondition error (http 412) because of etag differences
		calculationToUpdate = service.getClaimCalculation(calculationToUpdate, false);
		calculationToUpdate.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.APPROVED.toString());
		calculationToUpdate = service.updateClaimCalculation(calculationToUpdate, null);
		
		//COPY
		calculationToUpdate.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.ARCHIVED.toString());
		newCalculation = service.updateClaimCalculation(calculationToUpdate, ClaimsServiceEnums.UpdateTypes.REPLACE_COPY.toString());

		//Check if newCalculation contains the updated values from the replaced one
		Assert.assertEquals("Copy Calculation Status", newCalculation.getCalculationStatusCode(), ClaimsServiceEnums.CalculationStatusCodes.DRAFT.toString());
		Assert.assertEquals("Copy Insured Units not consistent", updatedInsuredUnits, newCalculation.getClaimCalculationPlantUnits().getInsuredUnits());
		Assert.assertEquals("Copy Deductible Level not correct", updatedDeductibleLevel, newCalculation.getClaimCalculationPlantUnits().getDeductibleLevel());
		Assert.assertEquals("Copy Insurable Value not correct", updatedInsurableValue, newCalculation.getClaimCalculationPlantUnits().getInsurableValue());
		
		//Delete - Clean up
		service.deleteClaimCalculation(newCalculation);
		//Need to load the original calculation again to prevent precondition error (http 412) because of etag differences
		calculationToUpdate = service.getClaimCalculation(calculationToUpdate, false);
		service.deleteClaimCalculation(calculationToUpdate);

		logger.debug(">testPlantUnitsClaimCalculationReplace");
	}	
	
	
	@Test
	public void testPlantUnitsTotalCalculations() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testPlantUnitsTotalCalculations");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		//Has to be an existing Claim Calculation
		String testClaimNumber = "28201";  // Needs to be manually set to an existing berries quantity loss calculation
		//Make sure it's a clean calculation with all data pulled in from CIRRAS without added yield or confirmed acres

		Assert.assertFalse("testClaimNumber must be set before this test can be run", testClaimNumber.equals("TODO"));

		ClaimCalculationListRsrc searchResults = service.getClaimCalculations(topLevelEndpoints, testClaimNumber, null, null, null, null, null, null, null, null, pageNumber, pageRowCount);
		Assert.assertNotNull("getClaimList() returned null", searchResults);
		Assert.assertTrue("getClaimList() returned empty list or more than one result", searchResults.getCollection().size() == 1);
		ClaimCalculationRsrc claimRsrc = searchResults.getCollection().get(0);

		claimRsrc = service.getClaimCalculation(claimRsrc, false);

		//Make sure damaged plants are equal to insured plants
		claimRsrc.getClaimCalculationPlantUnits().setDamagedUnits((int)Math.round(claimRsrc.getClaimCalculationPlantUnits().getInsuredUnits()));
		if(claimRsrc.getClaimCalculationPlantUnits().getLessAdjustmentUnits() == null) {
			claimRsrc.getClaimCalculationPlantUnits().setLessAssessmentUnits(null);
		} else {
			claimRsrc.getClaimCalculationPlantUnits().setLessAssessmentUnits((int)Math.round(claimRsrc.getClaimCalculationPlantUnits().getLessAdjustmentUnits()));
		}
			

		ClaimCalculationRsrc updatedClaimRsrc = service.updateClaimCalculation(claimRsrc, null);
		
		//In a total loss, the coverage has to match the claim amount
		Assert.assertEquals("Coverage and Claim amount don't match", updatedClaimRsrc.getClaimCalculationPlantUnits().getCoverageAmount(), updatedClaimRsrc.getTotalClaimAmount());
		Assert.assertTrue("Claim amount not rounded", isInteger(updatedClaimRsrc.getTotalClaimAmount()));

		logger.debug(">testPlantUnitsTotalCalculations");
	}	
	
	
	@Test
	public void testPlantAcresClaimCalculationOutOfSyncFlags() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testPlantAcresClaimCalculationOutOfSyncFlags");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		//1. Create a new Claim Calculation, verify that Out of Sync flags are all false.
		String testClaimNumber = "28502";  // Needs to be manually set to a real, valid WINE GRAPES claim in CIRRAS db.

		Assert.assertFalse("testClaimNumber must be set before this test can be run", testClaimNumber.equals("TODO"));
		
		ClaimListRsrc claimList = service.getClaimList(topLevelEndpoints, testClaimNumber, null, null, null, null, pageNumber, pageRowCount);
		Assert.assertNotNull("getClaimList() returned null", claimList);
		Assert.assertTrue("getClaimList() returned empty list or more than one result", claimList.getCollection().size() == 1);

		ClaimRsrc claim = claimList.getCollection().get(0);

		ClaimCalculationRsrc claimCalc = service.getClaim(claim);

		claimCalc = service.createClaimCalculation(claimCalc);

		assertOutOfSyncFlagsFalse(claimCalc);
		
		//2. Update ClaimCalculation, setting each field to check the corresponding Out of Sync flag.
		//4 Berries specific fields
		if(claimCalc.getClaimCalculationPlantAcres() != null) {
			//Insured Plants
			Double oldDeclaredAcres = claimCalc.getClaimCalculationPlantAcres().getDeclaredAcres();
			claimCalc.getClaimCalculationPlantAcres().setDeclaredAcres(oldDeclaredAcres +2);
			claimCalc = service.updateClaimCalculation(claimCalc, null);
			assertOutOfSyncFlagsFalseExceptOne(claimCalc, "PlantAcresDeclaredAcres");
			claimCalc.getClaimCalculationPlantAcres().setDeclaredAcres(oldDeclaredAcres);
			
			//Deductible Level
			Integer oldDeductibleLevel = claimCalc.getClaimCalculationPlantAcres().getDeductibleLevel();
			claimCalc.getClaimCalculationPlantAcres().setDeductibleLevel(oldDeductibleLevel - 10);
			claimCalc = service.updateClaimCalculation(claimCalc, null);
			assertOutOfSyncFlagsFalseExceptOne(claimCalc, "PlantAcresDeductibleLevel");
			claimCalc.getClaimCalculationPlantAcres().setDeductibleLevel(oldDeductibleLevel);

			//Insurable Value
			Double oldInsurableValue = claimCalc.getClaimCalculationPlantAcres().getInsurableValue();
			claimCalc.getClaimCalculationPlantAcres().setInsurableValue(oldInsurableValue - 100);
			claimCalc = service.updateClaimCalculation(claimCalc, null);
			assertOutOfSyncFlagsFalseExceptOne(claimCalc, "PlantAcresInsurableValue");
			claimCalc.getClaimCalculationPlantAcres().setInsurableValue(oldInsurableValue);
		
		}

		claimCalc = service.updateClaimCalculation(claimCalc, null);				
		assertOutOfSyncFlagsFalse(claimCalc);

		//k. Out of sync flags not set for certain statuses.
		claimCalc.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.APPROVED.toString());
		claimCalc = service.updateClaimCalculation(claimCalc, null);
		assertOutOfSyncFlagsNull(claimCalc);

		claimCalc.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.ARCHIVED.toString());
		claimCalc = service.updateClaimCalculation(claimCalc, null);
		assertOutOfSyncFlagsNull(claimCalc);
		
		//3. Delete the Claim Calculation.
		service.deleteClaimCalculation(claimCalc);
		
		logger.debug(">testPlantAcresClaimCalculationOutOfSyncFlags");
	}	

	@Test
	public void testPlantAcresClaimCalculationRefresh() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testPlantAcresClaimCalculationRefresh");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		//1. Create a new Claim Calculation, verify that Out of Sync flags are all false.
		String testClaimNumber = "28502";  // Needs to be manually set to a real, valid WINE GRAPES claim in CIRRAS db.

		Assert.assertFalse("testClaimNumber must be set before this test can be run", testClaimNumber.equals("TODO"));
		
		ClaimListRsrc claimList = service.getClaimList(topLevelEndpoints, testClaimNumber, null, null, null, null, pageNumber, pageRowCount);
		Assert.assertNotNull("getClaimList() returned null", claimList);
		Assert.assertTrue("getClaimList() returned empty list or more than one result", claimList.getCollection().size() == 1);

		ClaimRsrc claim = claimList.getCollection().get(0);

		ClaimCalculationRsrc claimCalc = service.getClaim(claim);

		claimCalc = service.createClaimCalculation(claimCalc);
		
		assertOutOfSyncFlagsPlantAcres(claimCalc, false);
		
		//2. Update ClaimCalculation, setting each field to be out of sync with claim.

		// ClaimCalculation fields
		if(claimCalc.getClaimCalculationPlantAcres() != null) {
			//Insured Plants
			Double oldDeclaredAcres = claimCalc.getClaimCalculationPlantAcres().getDeclaredAcres();
			claimCalc.getClaimCalculationPlantAcres().setDeclaredAcres(oldDeclaredAcres +2);
			
			//Deductible Level
			Integer oldDeductibleLevel = claimCalc.getClaimCalculationPlantAcres().getDeductibleLevel();
			claimCalc.getClaimCalculationPlantAcres().setDeductibleLevel(oldDeductibleLevel - 10);

			//Insurable Value
			Double oldInsurableValue = claimCalc.getClaimCalculationPlantAcres().getInsurableValue();
			claimCalc.getClaimCalculationPlantAcres().setInsurableValue(oldInsurableValue - 100);
			
		}
		
		claimCalc = service.updateClaimCalculation(claimCalc, null);

		assertOutOfSyncFlagsPlantAcres(claimCalc, true);

		claimCalc = service.getClaimCalculation(claimCalc, true);
		
		assertOutOfSyncFlagsPlantAcres(claimCalc, false);

		claimCalc = service.updateClaimCalculation(claimCalc, null);

		//4. Delete the Claim Calculation.
		service.deleteClaimCalculation(claimCalc);
		
		logger.debug(">testPlantAcresClaimCalculationRefresh");
	}
	
	@Test
	public void testPlantAcresClaimCalculationReplace() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testPlantAcresClaimCalculationReplace");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//Choose a claim number of a claim without a calculation
		String claimNumber = "28502";
		
		ClaimListRsrc claimList = service.getClaimList(topLevelEndpoints, claimNumber, null, null, null, null, pageNumber, pageRowCount);
		Assert.assertNotNull("getClaimList() returned null", claimList);
		Assert.assertTrue("getClaimList() returned empty list or more than one result", claimList.getCollection().size() == 1);

		ClaimRsrc claim = claimList.getCollection().get(0);

		ClaimCalculationRsrc calculationToUpdate = service.getClaim(claim);

		calculationToUpdate = service.createClaimCalculation(calculationToUpdate);
		
		//Original values
		Double originalDeclaredAcres = calculationToUpdate.getClaimCalculationPlantAcres().getDeclaredAcres();
		Integer originalDeductibleLevel = calculationToUpdate.getClaimCalculationPlantAcres().getDeductibleLevel();
		Double originalInsurableValue = calculationToUpdate.getClaimCalculationPlantAcres().getInsurableValue();
		
		//Update values for pulled in data to test if replacing NEW and COPY works correctly
		Double updatedDeclaredAcres = originalDeclaredAcres + 2;
		Integer updatedDeductibleLevel = originalDeductibleLevel - 5;
		Double updatedInsurableValue = originalInsurableValue - 100;
		
		calculationToUpdate.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.APPROVED.toString());
		calculationToUpdate.getClaimCalculationPlantAcres().setDeclaredAcres(updatedDeclaredAcres);
		calculationToUpdate.getClaimCalculationPlantAcres().setDeductibleLevel(updatedDeductibleLevel);
		calculationToUpdate.getClaimCalculationPlantAcres().setInsurableValue(updatedInsurableValue);
		
		//Saving updated values
		calculationToUpdate = service.updateClaimCalculation(calculationToUpdate, null);

		//NEW
		//Update calculation
		calculationToUpdate.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.ARCHIVED.toString());
		ClaimCalculationRsrc newCalculation = service.updateClaimCalculation(calculationToUpdate, ClaimsServiceEnums.UpdateTypes.REPLACE_NEW.toString());

		//Check if newCalculation contains the original values from the replaced one
		Assert.assertEquals("New Calculation Status", newCalculation.getCalculationStatusCode(), ClaimsServiceEnums.CalculationStatusCodes.DRAFT.toString());
		Assert.assertEquals("New Declared Acres not consistent", originalDeclaredAcres, newCalculation.getClaimCalculationPlantAcres().getDeclaredAcres());
		Assert.assertEquals("New Deductible Level not correct", originalDeductibleLevel, newCalculation.getClaimCalculationPlantAcres().getDeductibleLevel());
		Assert.assertEquals("New Insurable Value not correct", originalInsurableValue, newCalculation.getClaimCalculationPlantAcres().getInsurableValue());
		
		//Delete new calculation
		service.deleteClaimCalculation(newCalculation);
		
		//Reset calculation to be replaced to approved
		//Need to load the original calculation again to prevent precondition error (http 412) because of etag differences
		calculationToUpdate = service.getClaimCalculation(calculationToUpdate, false);
		calculationToUpdate.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.APPROVED.toString());
		calculationToUpdate = service.updateClaimCalculation(calculationToUpdate, null);
		
		//COPY
		calculationToUpdate.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.ARCHIVED.toString());
		newCalculation = service.updateClaimCalculation(calculationToUpdate, ClaimsServiceEnums.UpdateTypes.REPLACE_COPY.toString());

		//Check if newCalculation contains the updated values from the replaced one
		Assert.assertEquals("Copy Calculation Status", newCalculation.getCalculationStatusCode(), ClaimsServiceEnums.CalculationStatusCodes.DRAFT.toString());
		Assert.assertEquals("Copy Declared Acres not consistent", updatedDeclaredAcres, newCalculation.getClaimCalculationPlantAcres().getDeclaredAcres());
		Assert.assertEquals("Copy Deductible Level not correct", updatedDeductibleLevel, newCalculation.getClaimCalculationPlantAcres().getDeductibleLevel());
		Assert.assertEquals("Copy Insurable Value not correct", updatedInsurableValue, newCalculation.getClaimCalculationPlantAcres().getInsurableValue());
		
		//Delete - Clean up
		service.deleteClaimCalculation(newCalculation);
		//Need to load the original calculation again to prevent precondition error (http 412) because of etag differences
		calculationToUpdate = service.getClaimCalculation(calculationToUpdate, false);
		service.deleteClaimCalculation(calculationToUpdate);

		logger.debug(">testPlantAcresClaimCalculationReplace");
	}	
	
	@Test
	public void testGetCoverageCode() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testGetCoverageCode");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		assertEquals("AWP", ClaimsServiceEnums.CommodityCoverageCodes.Awp.getCode());
		assertEquals("AWPE", ClaimsServiceEnums.CommodityCoverageCodes.AwpExcreta.getCode());
		assertEquals("CAL", ClaimsServiceEnums.CommodityCoverageCodes.AcreageLoss.getCode());
		assertEquals("CHAIL", ClaimsServiceEnums.CommodityCoverageCodes.HailRain.getCode());
		assertEquals("CLHR", ClaimsServiceEnums.CommodityCoverageCodes.LotHailRain.getCode());
		assertEquals("COTH", ClaimsServiceEnums.CommodityCoverageCodes.Other.getCode());
		assertEquals("CPLANT", ClaimsServiceEnums.CommodityCoverageCodes.Plant.getCode());
		assertEquals("CPTF", ClaimsServiceEnums.CommodityCoverageCodes.PlantTreeFruit.getCode());
		assertEquals("CQF", ClaimsServiceEnums.CommodityCoverageCodes.QuantityForage.getCode());
		assertEquals("CQG", ClaimsServiceEnums.CommodityCoverageCodes.QuantityGrain.getCode());
		assertEquals("CQNT", ClaimsServiceEnums.CommodityCoverageCodes.Quantity.getCode());
		assertEquals("CQSC", ClaimsServiceEnums.CommodityCoverageCodes.QuantitySilageCorn.getCode());
		assertEquals("CQTF", ClaimsServiceEnums.CommodityCoverageCodes.QuantityTreeFruit.getCode());
		assertEquals("CQTFN", ClaimsServiceEnums.CommodityCoverageCodes.QuantityTreeFruitNetProduction.getCode());
		assertEquals("CSL", ClaimsServiceEnums.CommodityCoverageCodes.GrainSpotLoss.getCode());
		assertEquals("CUNS", ClaimsServiceEnums.CommodityCoverageCodes.CropUnseeded.getCode());
		assertEquals("FS", ClaimsServiceEnums.CommodityCoverageCodes.ForageSupply.getCode());
		assertEquals("GB", ClaimsServiceEnums.CommodityCoverageCodes.GrainBasket.getCode());
		assertEquals("SBU", ClaimsServiceEnums.CommodityCoverageCodes.SpecialBu.getCode());
		assertEquals("STC", ClaimsServiceEnums.CommodityCoverageCodes.SpecialTc.getCode());
		assertEquals("YP", ClaimsServiceEnums.CommodityCoverageCodes.YoungPlant.getCode());
	
		logger.debug(">testGetCoverageCode");
}
	
	@Test
	public void testGetClaimCalculationRefresh() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testGetClaimCalculationRefresh");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		//1. Create a new Claim Calculation, verify that Out of Sync flags are all false.
		String testClaimNumber = "28082";  // Needs to be manually set to a real, valid WINE GRAPES claim in CIRRAS db.

		Assert.assertFalse("testClaimNumber must be set before this test can be run", testClaimNumber.equals("TODO"));
		
		ClaimListRsrc claimList = service.getClaimList(topLevelEndpoints, testClaimNumber, null, null, null, null, pageNumber, pageRowCount);
		Assert.assertNotNull("getClaimList() returned null", claimList);
		Assert.assertTrue("getClaimList() returned empty list or more than one result", claimList.getCollection().size() == 1);

		ClaimRsrc claim = claimList.getCollection().get(0);

		ClaimCalculationRsrc claimCalc = service.getClaim(claim);

		claimCalc = service.createClaimCalculation(claimCalc);
		
		Assert.assertNotNull("Saved Claim has no varieties", claimCalc.getVarieties());
		Assert.assertTrue("Saved Claim has < 2 varieties", claimCalc.getVarieties().size() >= 2);

		assertOutOfSyncFlagsFalse(claimCalc);
		
		//2. Update ClaimCalculation, setting each field to be out of sync with claim.

		// ClaimCalculation fields
		claimCalc.setGrowerAddressLine1("ABCDEFG");
		claimCalc.setGrowerAddressLine2("ABCDEFG");
		claimCalc.setGrowerCity("ABCDEFG");
		claimCalc.setGrowerProvince("QB");
		claimCalc.setGrowerName("ABCDEFG");
		claimCalc.setGrowerNumber(98765);
		claimCalc.setGrowerPostalCode("ABCDEFG");

		ClaimCalculationVariety claimCalcVariety = claimCalc.getVarieties().get(0);
		claimCalcVariety.setAveragePrice(123.45);

		claimCalc.getVarieties().remove(1);
		
		Integer newVarietyId = 1010393;
		Assert.assertNull("Variety " + newVarietyId + " already exists. Change this test to use a different variety", getClaimCalcVarietyById(claimCalc, newVarietyId));

		ClaimCalculationVariety nv = new ClaimCalculationVariety();
		copyClaimCalcVariety(claimCalcVariety, nv);
		nv.setCropVarietyId(newVarietyId);
		claimCalc.getVarieties().add(nv);
		
		claimCalc = service.updateClaimCalculation(claimCalc, null);

		Assert.assertEquals("IsOutOfSync", true, claimCalc.getIsOutOfSync());

		claimCalc = service.getClaimCalculation(claimCalc, true);
		
		assertOutOfSyncFlagsFalse(claimCalc);

		//3. Refresh not allowed for certain statuses.
		claimCalc.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.APPROVED.toString());
		claimCalc = service.updateClaimCalculation(claimCalc, null);

		try {
			claimCalc = service.getClaimCalculation(claimCalc, true);
			Assert.fail("Refresh should have failed for status of APPROVED");
		
		} catch (CirrasClaimServiceException e) {
			// Expected
		}

		claimCalc.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.ARCHIVED.toString());
		claimCalc = service.updateClaimCalculation(claimCalc, null);

		try {
			claimCalc = service.getClaimCalculation(claimCalc, true);
			Assert.fail("Refresh should have failed for status of ARCHIVED");
		
		} catch (CirrasClaimServiceException e) {
			// Expected
		}
		
		//4. Delete the Claim Calculation.
		service.deleteClaimCalculation(claimCalc);
		
		logger.debug(">testGetClaimCalculationRefresh");
	}
	
	@Test
	public void testGrapesClaimCalculationRefresh() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testGrapesClaimCalculationRefresh");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		//1. Create a new Claim Calculation, verify that Out of Sync flags are all false.
		String testClaimNumber = "28082";  // Needs to be manually set to a real, valid Grapes claim in CIRRAS db.

		Assert.assertFalse("testClaimNumber must be set before this test can be run", testClaimNumber.equals("TODO"));
		
		ClaimListRsrc claimList = service.getClaimList(topLevelEndpoints, testClaimNumber, null, null, null, null, pageNumber, pageRowCount);
		Assert.assertNotNull("getClaimList() returned null", claimList);
		Assert.assertTrue("getClaimList() returned empty list or more than one result", claimList.getCollection().size() == 1);

		ClaimRsrc claim = claimList.getCollection().get(0);

		ClaimCalculationRsrc claimCalc = service.getClaim(claim);

		claimCalc = service.createClaimCalculation(claimCalc);
		
		assertOutOfSyncFlagsGrapes(claimCalc, false);
		
		//2. Update ClaimCalculation, setting each field to be out of sync with claim.

		if(claimCalc.getClaimCalculationGrapes() != null) {
			//Insurable Value Selected
			Double oldInsurableValueSelected = claimCalc.getClaimCalculationGrapes().getInsurableValueSelected();
			claimCalc.getClaimCalculationGrapes().setInsurableValueSelected(oldInsurableValueSelected + 0.5);

			//Insurable Value 100%
			Double oldInsurableValueHundredPercent = claimCalc.getClaimCalculationGrapes().getInsurableValueHundredPercent();
			claimCalc.getClaimCalculationGrapes().setInsurableValueHundredPercent(oldInsurableValueHundredPercent + 0.5);

			//Coverage Amount
			Double oldDeclaredAcres = claimCalc.getClaimCalculationGrapes().getCoverageAmount();
			claimCalc.getClaimCalculationGrapes().setCoverageAmount(oldDeclaredAcres + 1);
		}
		
		claimCalc = service.updateClaimCalculation(claimCalc, null);

		assertOutOfSyncFlagsGrapes(claimCalc, true);

		claimCalc = service.getClaimCalculation(claimCalc, true);
		
		assertOutOfSyncFlagsGrapes(claimCalc, false);

		claimCalc = service.updateClaimCalculation(claimCalc, null);		
		
		//4. Delete the Claim Calculation.
		service.deleteClaimCalculation(claimCalc);
		
		logger.debug(">testGrapesClaimCalculationRefresh");
	}
	
	@Test
	public void testGrapesClaimCalculationReplace() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testGrapesClaimCalculationReplace");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//Choose a claim number of a claim without a calculation
		String claimNumber = "30306";// Needs to be manually set to a real, valid Grapes Quantity claim in CIRRAS db.
		
		ClaimListRsrc claimList = service.getClaimList(topLevelEndpoints, claimNumber, null, null, null, null, pageNumber, pageRowCount);
		Assert.assertNotNull("getClaimList() returned null", claimList);
		Assert.assertTrue("getClaimList() returned empty list or more than one result", claimList.getCollection().size() == 1);

		ClaimRsrc claim = claimList.getCollection().get(0);

		ClaimCalculationRsrc calculationToUpdate = service.getClaim(claim);

		calculationToUpdate = service.createClaimCalculation(calculationToUpdate);
		
		//Original values
		Double originalCoverageAmount = calculationToUpdate.getClaimCalculationGrapes().getCoverageAmount();
		Double originalInsurableValueSelected = calculationToUpdate.getClaimCalculationGrapes().getInsurableValueSelected();
		Double originalInsurableValueHundredPercent = calculationToUpdate.getClaimCalculationGrapes().getInsurableValueHundredPercent();
		
		//Update values for pulled in data to test if replacing NEW and COPY works correctly
		Double updatedCoverageAmount = originalCoverageAmount +1;
		Double updatedInsurableValueSelected = originalInsurableValueSelected + 1;
		Double updatedInsurableValueHundredPercent = originalInsurableValueHundredPercent +1;
		
		calculationToUpdate.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.APPROVED.toString());
		calculationToUpdate.getClaimCalculationGrapes().setCoverageAmount(updatedCoverageAmount);
		calculationToUpdate.getClaimCalculationGrapes().setInsurableValueSelected(updatedInsurableValueSelected);
		calculationToUpdate.getClaimCalculationGrapes().setInsurableValueHundredPercent(updatedInsurableValueHundredPercent);
		
		Integer cropVarietyId = null;
		Double averagePrice = null;
		Double averagePriceOverride = 1.55;
		Double averagePriceFinal = null;
		Double averagePriceNew = 1.77;
		
		//Update one variety to test replace function
		if (calculationToUpdate.getVarieties() != null) {
			
			//Get first variety
			ClaimCalculationVariety variety = calculationToUpdate.getVarieties().get(0);
			
			if (variety != null) {
				//Store original values
				cropVarietyId = variety.getCropVarietyId();
				averagePrice = variety.getAveragePrice();
				averagePriceFinal = variety.getAveragePriceFinal();
				
				//Update average price and override
				variety.setAveragePrice(averagePriceNew);
				variety.setAveragePriceOverride(averagePriceOverride);
			}
		}
		
		//Saving updated values
		calculationToUpdate = service.updateClaimCalculation(calculationToUpdate, null);

		//NEW
		//Update calculation
		calculationToUpdate.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.ARCHIVED.toString());
		ClaimCalculationRsrc newCalculation = service.updateClaimCalculation(calculationToUpdate, ClaimsServiceEnums.UpdateTypes.REPLACE_NEW.toString());
		
		//Check if newCalculation contains the original values from the replaced one
		Assert.assertEquals("New Calculation Status", newCalculation.getCalculationStatusCode(), ClaimsServiceEnums.CalculationStatusCodes.DRAFT.toString());
		Assert.assertEquals("New Coverage Amount not correct", originalCoverageAmount, newCalculation.getClaimCalculationGrapes().getCoverageAmount());
		Assert.assertEquals("New Insurable Value Selected Value not correct", originalInsurableValueSelected, newCalculation.getClaimCalculationGrapes().getInsurableValueSelected());
		Assert.assertEquals("New Insurable Value 100% not correct", originalInsurableValueHundredPercent, newCalculation.getClaimCalculationGrapes().getInsurableValueHundredPercent());
		
		checkVarieties(cropVarietyId, averagePrice, null, averagePriceFinal, newCalculation, ClaimsServiceEnums.UpdateTypes.REPLACE_NEW.toString());
		
		//Delete new calculation
		service.deleteClaimCalculation(newCalculation);
		
		//Reset calculation to be replaced to approved
		//Need to load the original calculation again to prevent precondition error (http 412) because of etag differences
		calculationToUpdate = service.getClaimCalculation(calculationToUpdate, false);
		calculationToUpdate.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.APPROVED.toString());
		calculationToUpdate = service.updateClaimCalculation(calculationToUpdate, null);
		
		//COPY
		calculationToUpdate.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.ARCHIVED.toString());
		newCalculation = service.updateClaimCalculation(calculationToUpdate, ClaimsServiceEnums.UpdateTypes.REPLACE_COPY.toString());

		//Check if newCalculation contains the updated values from the replaced one
		Assert.assertEquals("Copy Calculation Status", newCalculation.getCalculationStatusCode(), ClaimsServiceEnums.CalculationStatusCodes.DRAFT.toString());
		Assert.assertEquals("New Coverage Amount not correct", updatedCoverageAmount, newCalculation.getClaimCalculationGrapes().getCoverageAmount());
		Assert.assertEquals("New Insurable Value Selected Value not correct", updatedInsurableValueSelected, newCalculation.getClaimCalculationGrapes().getInsurableValueSelected());
		Assert.assertEquals("New Insurable Value 100% not correct", updatedInsurableValueHundredPercent, newCalculation.getClaimCalculationGrapes().getInsurableValueHundredPercent());
		
		checkVarieties(cropVarietyId, averagePriceNew, averagePriceOverride, null, newCalculation, ClaimsServiceEnums.UpdateTypes.REPLACE_COPY.toString());

		//Delete - Clean up
		service.deleteClaimCalculation(newCalculation);
		//Need to load the original calculation again to prevent precondition error (http 412) because of etag differences
		calculationToUpdate = service.getClaimCalculation(calculationToUpdate, false);
		service.deleteClaimCalculation(calculationToUpdate);

		logger.debug(">testGrapesClaimCalculationReplace");
	}

	private void checkVarieties(Integer cropVarietyId, Double averagePrice, Double averagePriceOverride, Double averagePriceFinal,
			ClaimCalculationRsrc newCalculation, String updateType) {
		if (newCalculation.getVarieties() != null) {
			
			for (ClaimCalculationVariety v : newCalculation.getVarieties()) {
				
				if(v.getAveragePriceOverride() != null) {
					//Final price needs to match the override price
					Assert.assertEquals("Average Price Final not equal average price override", v.getAveragePriceOverride(), v.getAveragePriceFinal());
				} else if (v.getAveragePrice() != null) {
					//Final price needs to match the average price from CIRRAS
					Assert.assertEquals("Average Price Final not equal average price", v.getAveragePrice(), v.getAveragePriceFinal());
				} else {
					//Non of the others are set
					Assert.assertNull("Average Price Final not null", v.getAveragePriceFinal());
				}
					
				if(v.getCropVarietyId().equals(cropVarietyId)) {
					if(updateType.equals(ClaimsServiceEnums.UpdateTypes.REPLACE_NEW.toString())) {
						Assert.assertEquals("Average Price", averagePrice, v.getAveragePrice());
						Assert.assertNull("Average Price Override", v.getAveragePriceOverride());
						Assert.assertEquals("Average Price Final", averagePriceFinal, v.getAveragePriceFinal());
					} else if(updateType.equals(ClaimsServiceEnums.UpdateTypes.REPLACE_COPY.toString())) {
						Assert.assertEquals("Average Price 2", averagePrice, v.getAveragePrice());
						Assert.assertEquals("Average Price Override 2", averagePriceOverride, v.getAveragePriceOverride());
					}
				}
			}
		}
	}
	
	@Test
	public void testVarietiesAvgPriceRefresh() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testVarietiesAvgPriceRefresh");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//The average price should only be overridden if it's not editable (average price = null)
		
		String testClaimNumber = "30595";  // Needs to be a calculation with 2 varieties with IIV from CIRRAS and at least one without.
		
		Assert.assertFalse("testClaimNumber must be set before this test can be run", testClaimNumber.equals("TODO"));
		
		ClaimCalculationListRsrc searchResults = service.getClaimCalculations(topLevelEndpoints, testClaimNumber, null, null, "DRAFT", null, null, null, null, null, pageNumber, pageRowCount);
		Assert.assertEquals(1, searchResults.getTotalRowCount());
		ClaimCalculationRsrc claimRsrc = searchResults.getCollection().get(0);

		claimRsrc = service.getClaimCalculation(claimRsrc, false);
		Assert.assertNotNull(claimRsrc);
		
		Integer cropVarietyId = null;
		Double averagePrice = null;
		Double averagePriceOverride = null;
		Double averagePriceFinal = null;
		
		if (claimRsrc.getVarieties() != null) {
			
			//Get a variety that has an avg price set and is editable
			ClaimCalculationVariety variety = claimRsrc.getVarieties().stream()
					.filter(x -> x.getAveragePrice() == null && x.getAveragePriceOverride() != null)
					.findAny().get();

			if (variety != null) {
				cropVarietyId = variety.getCropVarietyId();
				averagePrice = variety.getAveragePrice();
				averagePriceOverride = variety.getAveragePriceOverride();
				averagePriceFinal = variety.getAveragePriceFinal();
			}
		}
		
		//Refresh
		claimRsrc = service.getClaimCalculation(claimRsrc, true);
		Boolean foundVariety = false;

		if (claimRsrc.getVarieties() != null) {
			
			for (ClaimCalculationVariety v : claimRsrc.getVarieties()) {
				
				if(v.getAveragePriceOverride() != null) {
					//Final price needs to match the override price
					Assert.assertEquals("Average Price Final not equal average price override", v.getAveragePriceOverride(), v.getAveragePriceFinal());
				} else if (v.getAveragePrice() != null) {
					//Final price needs to match the average price from CIRRAS
					Assert.assertEquals("Average Price Final not equal average price", v.getAveragePrice(), v.getAveragePriceFinal());
				} else {
					//Non of the others are set
					Assert.assertNull("Average Price Final not null", v.getAveragePriceFinal());
				}
				
			
				if(v.getCropVarietyId().equals(cropVarietyId)) {
					Assert.assertEquals("Average Price", averagePrice, v.getAveragePrice());
					Assert.assertEquals("Average Price Override", averagePriceOverride, v.getAveragePriceOverride());
					Assert.assertEquals("Average Price Final", averagePriceFinal, v.getAveragePriceFinal());
					logger.debug("Average Price Expected: " + averagePrice + "Average Price Actual: " + v.getAveragePrice());
					foundVariety = true;
				}
			}
		}

		Assert.assertEquals("Variety Found", true, foundVariety);

		logger.debug("Variety Found: " + foundVariety);
		

		logger.debug(">testVarietiesAvgPriceRefresh");
		
		/*
		 	//Return all varieties for calculation
			select v.average_price, v.average_price_override, v.average_price_final, v.average_price_editable_ind, v.crop_variety_id, v.claim_calculation_guid, v.claim_calculation_variety_guid
			from claim_calculation_variety v
			join claim_calculation c on c.claim_calculation_guid = v.claim_calculation_guid
			where c.claim_number = 30595;

			//Update one of the varieties with a IIV from CIRRAS to get it out of sync
			UPDATE claim_calculation_variety SET
			average_price_final = average_price_final - 0.05,
			average_price = average_price - 0.05
			where claim_calculation_variety_guid = 'F89AADD38F210609E053690A0A0A30FE';

		 */
	}
	
	@Test
	public void testVarietiesAvgPriceOverride() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testVarietiesAvgPriceOverride");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//The average price should only be overridden if it's not editable (average price = null)
		
		String testClaimNumber = "30595";  // Needs to be a calculation with at least one varieties with IIV from CIRRAS and at least one without.
		
		Assert.assertFalse("testClaimNumber must be set before this test can be run", testClaimNumber.equals("TODO"));
		
		ClaimCalculationListRsrc searchResults = service.getClaimCalculations(topLevelEndpoints, testClaimNumber, null, null, "DRAFT", null, null, null, null, null, pageNumber, pageRowCount);
		Assert.assertEquals(1, searchResults.getTotalRowCount());
		ClaimCalculationRsrc claimRsrc = searchResults.getCollection().get(0);

		claimRsrc = service.getClaimCalculation(claimRsrc, false);
		Assert.assertNotNull(claimRsrc);
		
		if (claimRsrc.getVarieties() != null) {
			
			//Get a variety that has no avg price set
			ClaimCalculationVariety variety = claimRsrc.getVarieties().stream()
					.filter(x -> x.getAveragePrice() == null)
					.findAny().get();
			
			if (variety != null) {
				variety.setAveragePriceOverride(1.123);
			}

			//Get a variety that has avg price set
			ClaimCalculationVariety variety2 = claimRsrc.getVarieties().stream()
					.filter(x -> x.getAveragePrice() != null)
					.findAny().get();
			
			if (variety2 != null) {
				variety2.setAveragePriceOverride(2.123);
			}
		}
		
		service.updateClaimCalculation(claimRsrc, null);

		//Refresh
		claimRsrc = service.getClaimCalculation(claimRsrc, true);

		if (claimRsrc.getVarieties() != null) {
			
			for (ClaimCalculationVariety v : claimRsrc.getVarieties()) {
				
				if(v.getAveragePriceOverride() != null) {
					//Final price needs to match the override price
					Assert.assertEquals("Average Price Final not equal average price override", v.getAveragePriceOverride(), v.getAveragePriceFinal());
				} else if (v.getAveragePrice() != null) {
					//Final price needs to match the average price from CIRRAS
					Assert.assertEquals("Average Price Final not equal average price", v.getAveragePrice(), v.getAveragePriceFinal());
				} else {
					//Non of the others are set
					Assert.assertNull("Average Price Final not null", v.getAveragePriceFinal());
				}
				
			}
		}

		logger.debug(">testVarietiesAvgPriceOverride");
		
		/*
		 	//Return all varieties for calculation
			select v.average_price, v.average_price_override, v.average_price_final, v.average_price_editable_ind, v.crop_variety_id, v.claim_calculation_guid, v.claim_calculation_variety_guid
			from claim_calculation_variety v
			join claim_calculation c on c.claim_calculation_guid = v.claim_calculation_guid
			where c.claim_number = 30595;

			//Update one of the varieties with a IIV from CIRRAS to get it out of sync
			UPDATE claim_calculation_variety SET
			average_price_final = average_price_final - 0.05,
			average_price = average_price - 0.05
			where claim_calculation_variety_guid = 'F89AADD38F210609E053690A0A0A30FE';

		 */
	}	
	
	@Test
	public void testBerriesClaimCalculationRefresh() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testBerriesClaimCalculationRefresh");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		//1. Create a new Claim Calculation, verify that Out of Sync flags are all false.
		String testClaimNumber = "28214";  // Needs to be manually set to a real, valid BERRIES claim in CIRRAS db.

		Assert.assertFalse("testClaimNumber must be set before this test can be run", testClaimNumber.equals("TODO"));
		
		ClaimListRsrc claimList = service.getClaimList(topLevelEndpoints, testClaimNumber, null, null, null, null, pageNumber, pageRowCount);
		Assert.assertNotNull("getClaimList() returned null", claimList);
		Assert.assertTrue("getClaimList() returned empty list or more than one result", claimList.getCollection().size() == 1);

		ClaimRsrc claim = claimList.getCollection().get(0);

		ClaimCalculationRsrc claimCalc = service.getClaim(claim);

		claimCalc = service.createClaimCalculation(claimCalc);
		
		assertOutOfSyncFlagsBerries(claimCalc, false);
		
		//2. Update ClaimCalculation, setting each field to be out of sync with claim.

		if(claimCalc.getClaimCalculationBerries() != null) {
			//Total Probable Yield
			Double oldTotalProbableYield = claimCalc.getClaimCalculationBerries().getTotalProbableYield();
			claimCalc.getClaimCalculationBerries().setTotalProbableYield(oldTotalProbableYield - 100);
			
			//Deductible Level
			Integer oldDeductibleLevel = claimCalc.getClaimCalculationBerries().getDeductibleLevel();
			claimCalc.getClaimCalculationBerries().setDeductibleLevel(oldDeductibleLevel + 10);

			//Production Guarantee
			Double oldProductionGuarantee = claimCalc.getClaimCalculationBerries().getProductionGuarantee();
			claimCalc.getClaimCalculationBerries().setProductionGuarantee(oldProductionGuarantee + 10.5);

			//Declared Acres
			Double oldDeclaredAcres = claimCalc.getClaimCalculationBerries().getDeclaredAcres();
			claimCalc.getClaimCalculationBerries().setDeclaredAcres(oldDeclaredAcres + 0.5);

			//Insurable Value Selected
			Double oldInsurableValueSelected = claimCalc.getClaimCalculationBerries().getInsurableValueSelected();
			claimCalc.getClaimCalculationBerries().setInsurableValueSelected(oldInsurableValueSelected + 0.5);

			//Insurable Value 100%
			Double oldInsurableValueHundredPercent = claimCalc.getClaimCalculationBerries().getInsurableValueHundredPercent();
			claimCalc.getClaimCalculationBerries().setInsurableValueHundredPercent(oldInsurableValueHundredPercent + 0.5);

			//Coverage Amount
			Double oldCoverageAmount = claimCalc.getClaimCalculationBerries().getMaxCoverageAmount();
			claimCalc.getClaimCalculationBerries().setMaxCoverageAmount(oldCoverageAmount + 1000);

		}
		
		claimCalc = service.updateClaimCalculation(claimCalc, null);

		assertOutOfSyncFlagsBerries(claimCalc, true);

		claimCalc = service.getClaimCalculation(claimCalc, true);
		
		assertOutOfSyncFlagsBerries(claimCalc, false);

		claimCalc = service.updateClaimCalculation(claimCalc, null);		
		
		//4. Delete the Claim Calculation.
		service.deleteClaimCalculation(claimCalc);
		
		logger.debug(">testBerriesClaimCalculationRefresh");
	}
	
	@Test
	public void testBerriesClaimCalculationUpdate() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testBerriesClaimCalculationUpdate");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		//Has to be an existing Claim Calculation
		String testClaimNumber = "28092";  // Needs to be manually set to a real, valid BERRIES claim in CIRRAS db.

		Assert.assertFalse("testClaimNumber must be set before this test can be run", testClaimNumber.equals("TODO"));

		ClaimCalculationListRsrc searchResults = service.getClaimCalculations(topLevelEndpoints, testClaimNumber, null, null, null, null, null, null, null, null, pageNumber, pageRowCount);
		Assert.assertNotNull("getClaimList() returned null", searchResults);
		Assert.assertTrue("getClaimList() returned empty list or more than one result", searchResults.getCollection().size() == 1);
		ClaimCalculationRsrc claimRsrc = searchResults.getCollection().get(0);

		claimRsrc = service.getClaimCalculation(claimRsrc, true);

		claimRsrc.setCalculationComment(claimRsrc.getCalculationComment() + ".");
		
		claimRsrc = service.updateClaimCalculation(claimRsrc, null);
		
		logger.debug(">testBerriesClaimCalculationUpdate");
	}
	
	@Test
	public void testBerriesTotalCalculations() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testBerriesTotalCalculations");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		//Has to be an existing Claim Calculation
		String testClaimNumber = "28090";  // Needs to be manually set to an existing berries quantity loss calculation
		//Make sure it's a clean calculation with all data pulled in from CIRRAS without added yield or confirmed acres

		Assert.assertFalse("testClaimNumber must be set before this test can be run", testClaimNumber.equals("TODO"));

		ClaimCalculationListRsrc searchResults = service.getClaimCalculations(topLevelEndpoints, testClaimNumber, null, null, null, null, null, null, null, null, pageNumber, pageRowCount);
		Assert.assertNotNull("getClaimList() returned null", searchResults);
		Assert.assertTrue("getClaimList() returned empty list or more than one result", searchResults.getCollection().size() == 1);
		ClaimCalculationRsrc claimRsrc = searchResults.getCollection().get(0);

		claimRsrc = service.getClaimCalculation(claimRsrc, false);
		
		claimRsrc.getClaimCalculationBerries().setHarvestedYield(null);
		claimRsrc.getClaimCalculationBerries().setTotalYieldFromAdjuster(null);
		claimRsrc.getClaimCalculationBerries().setYieldAssessment(null);
		claimRsrc.getClaimCalculationBerries().setConfirmedAcres(null);

		ClaimCalculationRsrc updatedClaimRsrc0 = service.updateClaimCalculation(claimRsrc, null);
		
		//In a total loss, the coverage has to match the claim amount
		Assert.assertEquals("Coverage and Claim amount don't match", updatedClaimRsrc0.getClaimCalculationBerries().getCoverageAmountAdjusted(), updatedClaimRsrc0.getTotalClaimAmount());
		//If confirmed acres are null the calculated coverage amount has to match the coverage amount in CIRRAS
		Assert.assertEquals("Coverage and Claim amount don't match", updatedClaimRsrc0.getClaimCalculationBerries().getCoverageAmountAdjusted(), updatedClaimRsrc0.getClaimCalculationBerries().getMaxCoverageAmount());


		//Update confirmed acres
		//Total loss (no yield data added) with a calculated claim amount lower than the coverage
		//updatedClaimRsrc0.getClaimCalculationBerries().setConfirmedAcres(32.1);
		updatedClaimRsrc0.getClaimCalculationBerries().setConfirmedAcres(35.0);
		
		ClaimCalculationRsrc updatedClaimRsrc1 = service.updateClaimCalculation(updatedClaimRsrc0, null);
		
		//Adjustment factor should be 1
		Assert.assertTrue("Adjustment Factor should be no bigger 1", updatedClaimRsrc1.getClaimCalculationBerries().getAdjustmentFactor() <= 1.0 );
		
		//In a total loss, the coverage has to match the claim amount
		Assert.assertEquals("Coverage and Claim amount don't match", updatedClaimRsrc1.getClaimCalculationBerries().getCoverageAmountAdjusted(), updatedClaimRsrc1.getTotalClaimAmount());
		Assert.assertTrue("Adjusted Production Guarantee not rounded", isInteger(updatedClaimRsrc1.getClaimCalculationBerries().getAdjustedProductionGuarantee()));

		//Add a yield with a decimal to test if the calculation are rounded to an integer
		updatedClaimRsrc1.getClaimCalculationBerries().setHarvestedYield(100.4);
		updatedClaimRsrc1.getClaimCalculationBerries().setTotalYieldFromAdjuster(25.1);
		updatedClaimRsrc1.getClaimCalculationBerries().setYieldAssessment(1.1);
		
		ClaimCalculationRsrc updatedClaimRsrc2 = service.updateClaimCalculation(updatedClaimRsrc1, null);
		
		Assert.assertTrue("Total Yield From Declaration of Production (DoP) not rounded", isInteger(updatedClaimRsrc2.getClaimCalculationBerries().getTotalYieldFromDop()));
		Assert.assertTrue("Total Yield for Claim Calculation (lbs) not rounded", isInteger(updatedClaimRsrc2.getClaimCalculationBerries().getTotalYieldForCalculation()));
		Assert.assertTrue("Yield Loss Eligible for Claim (lbs) not rounded", isInteger(updatedClaimRsrc2.getClaimCalculationBerries().getYieldLossEligible()));
		
		// Make sure the Yield Loss Eligible for Claim and Quantity Loss Claim are not negative
		updatedClaimRsrc2.getClaimCalculationBerries().setHarvestedYield( 444000.0);
		ClaimCalculationRsrc updatedClaimRsrc3 = service.updateClaimCalculation(updatedClaimRsrc2, null);
		
		Assert.assertTrue("Yield Loss Eligible is not negative", updatedClaimRsrc3.getClaimCalculationBerries().getYieldLossEligible() >= 0);
		Assert.assertTrue("Total Claim Amount is not negative ", updatedClaimRsrc3.getTotalClaimAmount() >= 0) ;
		
		updatedClaimRsrc3.getClaimCalculationBerries().setHarvestedYield(null);
		updatedClaimRsrc3.getClaimCalculationBerries().setTotalYieldFromAdjuster(null);
		updatedClaimRsrc3.getClaimCalculationBerries().setYieldAssessment(null);
		updatedClaimRsrc3.getClaimCalculationBerries().setConfirmedAcres(null);
		
		claimRsrc = service.updateClaimCalculation(updatedClaimRsrc3, null);
		
		logger.debug(">testBerriesTotalCalculations");
	}	

	@Test
	public void testGrainUnseededClaimCalculationOutOfSyncFlags() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testGrainUnseededClaimCalculationOutOfSyncFlags");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		//1. Create a new Claim Calculation, verify that Out of Sync flags are all false.
		String testClaimNumber = "37159";  // Needs to be manually set to a real, valid GRAIN UNSEEDED claim in CIRRAS db with no existing calculations.
		
		Assert.assertFalse("testClaimNumber must be set before this test can be run", testClaimNumber.equals("TODO"));

		outOfSyncClaimNumber = Integer.valueOf(testClaimNumber);
		
		ClaimListRsrc claimList = service.getClaimList(topLevelEndpoints, testClaimNumber, null, null, null, null, pageNumber, pageRowCount);
		Assert.assertNotNull("getClaimList() returned null", claimList);
		Assert.assertTrue("getClaimList() returned empty list or more than one result", claimList.getCollection().size() == 1);

		ClaimRsrc claim = claimList.getCollection().get(0);

		ClaimCalculationRsrc claimCalc = service.getClaim(claim);

		claimCalc = service.createClaimCalculation(claimCalc);

		outOfSyncClaimCalculationGuid = claimCalc.getClaimCalculationGuid();
		
		assertOutOfSyncFlagsFalse(claimCalc);
		
		//2. Update ClaimCalculation, setting each field to check the corresponding Out of Sync flag.
		//Insured Acres
		Double oldInsuredAcres = claimCalc.getClaimCalculationGrainUnseeded().getInsuredAcres();
		claimCalc.getClaimCalculationGrainUnseeded().setInsuredAcres(oldInsuredAcres - 1);
		claimCalc = service.updateClaimCalculation(claimCalc, null);
		assertOutOfSyncFlagsFalseExceptOne(claimCalc, "GrainUnseededInsuredAcres");
		claimCalc.getClaimCalculationGrainUnseeded().setInsuredAcres(oldInsuredAcres);
		
		//Deductible Level
		Integer oldDeductibleLevel = claimCalc.getClaimCalculationGrainUnseeded().getDeductibleLevel();
		claimCalc.getClaimCalculationGrainUnseeded().setDeductibleLevel(oldDeductibleLevel + 10);
		claimCalc = service.updateClaimCalculation(claimCalc, null);
		assertOutOfSyncFlagsFalseExceptOne(claimCalc, "GrainUnseededDeductibleLevel");
		claimCalc.getClaimCalculationGrainUnseeded().setDeductibleLevel(oldDeductibleLevel);

		//Insurable Value
		Double oldInsurableValue = claimCalc.getClaimCalculationGrainUnseeded().getInsurableValue();
		claimCalc.getClaimCalculationGrainUnseeded().setInsurableValue(oldInsurableValue - 1);
		claimCalc = service.updateClaimCalculation(claimCalc, null);
		assertOutOfSyncFlagsFalseExceptOne(claimCalc, "GrainUnseededInsurableValue");
		claimCalc.getClaimCalculationGrainUnseeded().setInsurableValue(oldInsurableValue);

		claimCalc = service.updateClaimCalculation(claimCalc, null);				
		assertOutOfSyncFlagsFalse(claimCalc);

		//3. Out of sync flags not set for certain statuses.
		claimCalc.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.APPROVED.toString());
		claimCalc = service.updateClaimCalculation(claimCalc, null);
		assertOutOfSyncFlagsNull(claimCalc);

		claimCalc.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.ARCHIVED.toString());
		claimCalc = service.updateClaimCalculation(claimCalc, null);
		assertOutOfSyncFlagsNull(claimCalc);
		
		//3. Delete the Claim Calculation.
		service.deleteClaimCalculation(claimCalc);
		
		logger.debug(">testGrainUnseededClaimCalculationOutOfSyncFlags");
	}

	
	@Test
	public void testGrainUnseededClaimCalculationRefresh() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testGrainUnseededClaimCalculationRefresh");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		
		//1. Create a new Claim Calculation, verify that Out of Sync flags are all false.
		String testClaimNumber = "37159";  // Needs to be manually set to a real, valid GRAIN UNSEEDED claim in CIRRAS db with no existing calculations.
		
		Assert.assertFalse("testClaimNumber must be set before this test can be run", testClaimNumber.equals("TODO"));

		outOfSyncClaimNumber = Integer.valueOf(testClaimNumber);
		
		ClaimListRsrc claimList = service.getClaimList(topLevelEndpoints, testClaimNumber, null, null, null, null, pageNumber, pageRowCount);
		Assert.assertNotNull("getClaimList() returned null", claimList);
		Assert.assertTrue("getClaimList() returned empty list or more than one result", claimList.getCollection().size() == 1);

		ClaimRsrc claim = claimList.getCollection().get(0);

		ClaimCalculationRsrc claimCalc = service.getClaim(claim);

		claimCalc = service.createClaimCalculation(claimCalc);

		outOfSyncClaimCalculationGuid = claimCalc.getClaimCalculationGuid();
		
		assertOutOfSyncFlagsGrainUnseeded(claimCalc, false);
		
		//2. Update ClaimCalculation, setting each field to be out of sync with claim.
		ClaimCalculationGrainUnseeded unseeded = claimCalc.getClaimCalculationGrainUnseeded();

		//Insured Acres
		Double oldInsuredAcres = unseeded.getInsuredAcres();
		unseeded.setInsuredAcres(oldInsuredAcres - 1);
		
		//Deductible Level
		Integer oldDeductibleLevel = unseeded.getDeductibleLevel();
		unseeded.setDeductibleLevel(oldDeductibleLevel + 10);

		//Insurable Value
		Double oldInsurableValue = unseeded.getInsurableValue();
		unseeded.setInsurableValue(oldInsurableValue - 1);

		claimCalc = service.updateClaimCalculation(claimCalc, null);

		assertOutOfSyncFlagsGrainUnseeded(claimCalc, true);

		claimCalc = service.getClaimCalculation(claimCalc, true);
		
		assertOutOfSyncFlagsGrainUnseeded(claimCalc, false);
		
		claimCalc = service.updateClaimCalculation(claimCalc, null);

		//3. Delete the Claim Calculation.
		service.deleteClaimCalculation(claimCalc);
				
		logger.debug(">testGrainUnseededClaimCalculationRefresh");
	}

	@Test
	public void testGrainUnseededClaimCalculationReplace() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testGrainUnseededClaimCalculationReplace");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		

		String testClaimNumber = "37159";  // Needs to be manually set to a real, valid GRAIN UNSEEDED claim in CIRRAS db with no existing calculations.
		
		Assert.assertFalse("testClaimNumber must be set before this test can be run", testClaimNumber.equals("TODO"));

		replaceClaimNumber = Integer.valueOf(testClaimNumber);
		
		ClaimListRsrc claimList = service.getClaimList(topLevelEndpoints, testClaimNumber, null, null, null, null, pageNumber, pageRowCount);
		Assert.assertNotNull("getClaimList() returned null", claimList);
		Assert.assertTrue("getClaimList() returned empty list or more than one result", claimList.getCollection().size() == 1);

		ClaimRsrc claim = claimList.getCollection().get(0);

		ClaimCalculationRsrc calculationToUpdate = service.getClaim(claim);

		calculationToUpdate = service.createClaimCalculation(calculationToUpdate);

		replaceClaimCalculationGuid1 = calculationToUpdate.getClaimCalculationGuid();
		
		//Original values
		Double originalInsuredAcres = calculationToUpdate.getClaimCalculationGrainUnseeded().getInsuredAcres();
		Integer originalDeductibleLevel = calculationToUpdate.getClaimCalculationGrainUnseeded().getDeductibleLevel();
		Double originalInsurableValue = calculationToUpdate.getClaimCalculationGrainUnseeded().getInsurableValue();
		
		//Update values for pulled in data to test if replacing NEW and COPY works correctly
		Double updatedInsuredAcres = originalInsuredAcres - 1;
		Integer updatedDeductibleLevel = originalDeductibleLevel + 10;
		Double updatedInsurableValue = originalInsurableValue - 1;
		
		calculationToUpdate.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.APPROVED.toString());
		calculationToUpdate.getClaimCalculationGrainUnseeded().setInsuredAcres(updatedInsuredAcres);
		calculationToUpdate.getClaimCalculationGrainUnseeded().setDeductibleLevel(updatedDeductibleLevel);
		calculationToUpdate.getClaimCalculationGrainUnseeded().setInsurableValue(updatedInsurableValue);
		
		//Saving updated values
		calculationToUpdate = service.updateClaimCalculation(calculationToUpdate, null);

		//NEW
		//Update calculation
		calculationToUpdate.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.ARCHIVED.toString());
		ClaimCalculationRsrc newCalculation = service.updateClaimCalculation(calculationToUpdate, ClaimsServiceEnums.UpdateTypes.REPLACE_NEW.toString());

		replaceClaimCalculationGuid2 = newCalculation.getClaimCalculationGuid();
		
		
		//Check if newCalculation contains the original values from the replaced one
		Assert.assertEquals("New Calculation Status", newCalculation.getCalculationStatusCode(), ClaimsServiceEnums.CalculationStatusCodes.DRAFT.toString());
		Assert.assertEquals("New Insured Acres not consistent", originalInsuredAcres, newCalculation.getClaimCalculationGrainUnseeded().getInsuredAcres());
		Assert.assertEquals("New Deductible Level not correct", originalDeductibleLevel, newCalculation.getClaimCalculationGrainUnseeded().getDeductibleLevel());
		Assert.assertEquals("New Insurable Value not correct", originalInsurableValue, newCalculation.getClaimCalculationGrainUnseeded().getInsurableValue());
		
		//Delete new calculation
		service.deleteClaimCalculation(newCalculation);
		
		//Reset calculation to be replaced to approved
		//Need to load the original calculation again to prevent precondition error (http 412) because of etag differences
		calculationToUpdate = service.getClaimCalculation(calculationToUpdate, false);
		calculationToUpdate.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.APPROVED.toString());
		calculationToUpdate = service.updateClaimCalculation(calculationToUpdate, null);
		
		//COPY
		calculationToUpdate.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.ARCHIVED.toString());
		newCalculation = service.updateClaimCalculation(calculationToUpdate, ClaimsServiceEnums.UpdateTypes.REPLACE_COPY.toString());

		replaceClaimCalculationGuid3 = newCalculation.getClaimCalculationGuid();
		
		
		//Check if newCalculation contains the updated values from the replaced one
		Assert.assertEquals("Copy Calculation Status", newCalculation.getCalculationStatusCode(), ClaimsServiceEnums.CalculationStatusCodes.DRAFT.toString());
		Assert.assertEquals("Copy Insured Acres not consistent", updatedInsuredAcres, newCalculation.getClaimCalculationGrainUnseeded().getInsuredAcres());
		Assert.assertEquals("Copy Deductible Level not correct", updatedDeductibleLevel, newCalculation.getClaimCalculationGrainUnseeded().getDeductibleLevel());
		Assert.assertEquals("Copy Insurable Value not correct", updatedInsurableValue, newCalculation.getClaimCalculationGrainUnseeded().getInsurableValue());
		
		//Delete - Clean up
		service.deleteClaimCalculation(newCalculation);
		
		//Need to load the original calculation again to prevent precondition error (http 412) because of etag differences
		calculationToUpdate = service.getClaimCalculation(calculationToUpdate, false);
		service.deleteClaimCalculation(calculationToUpdate);

		logger.debug(">testGrainUnseededClaimCalculationReplace");
	}
	
	
	private boolean isInteger(double number) {
	    return number % 1 == 0;// if the modulus(remainder of the division) of the argument(number) with 1 is 0 then return true otherwise false.
	}
	
	private ClaimCalculationVariety getClaimCalcVarietyById(ClaimCalculationRsrc c, Integer varietyId) {

		ClaimCalculationVariety retVal = null;
		
		if (c.getVarieties() != null) {
			for (ClaimCalculationVariety v : c.getVarieties()) {
				if (v.getCropVarietyId().equals(varietyId)) {
					retVal = v;
					break;
				}
			}
		}
		
		return retVal;
	}

	private void copyClaimCalcVariety(ClaimCalculationVariety src, ClaimCalculationVariety dest) {
		dest.setCropVarietyId(src.getCropVarietyId());
		dest.setVarietyName(src.getVarietyName());
		dest.setAveragePrice(src.getAveragePrice());
		dest.setAveragePriceOverride(src.getAveragePriceOverride());
		dest.setAveragePriceFinal(src.getAveragePriceFinal());
		dest.setInsurableValue(src.getInsurableValue());
		dest.setYieldAssessedReason(src.getYieldAssessedReason());
		dest.setYieldAssessed(src.getYieldAssessed());
		dest.setYieldTotal(src.getYieldTotal());
		dest.setYieldActual(src.getYieldActual());
		dest.setVarietyProductionValue(src.getVarietyProductionValue());
	}
	
	private void assertOutOfSyncFlagsFalse(ClaimCalculationRsrc c) {

		Assert.assertEquals("IsOutOfSync", false, c.getIsOutOfSync());
		Assert.assertEquals("IsOutOfSyncGrowerAddrLine1", false, c.getIsOutOfSyncGrowerAddrLine1());
		Assert.assertEquals("IsOutOfSyncGrowerAddrLine2", false, c.getIsOutOfSyncGrowerAddrLine2());
		Assert.assertEquals("IsOutOfSyncGrowerCity", false, c.getIsOutOfSyncGrowerCity());
		Assert.assertEquals("IsOutOfSyncGrowerProvince", false, c.getIsOutOfSyncGrowerProvince());
		Assert.assertEquals("IsOutOfSyncGrowerName", false, c.getIsOutOfSyncGrowerName());
		Assert.assertEquals("IsOutOfSyncGrowerNumber", false, c.getIsOutOfSyncGrowerNumber());
		Assert.assertEquals("IsOutOfSyncGrowerPostalCode", false, c.getIsOutOfSyncGrowerPostalCode());

		if (c.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAPES.toString())
				&& c.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.Quantity.getCode())) {
			Assert.assertEquals("IsOutOfSyncVarietyAdded", false, c.getIsOutOfSyncVarietyAdded());
		}

		for (ClaimCalculationVariety v : c.getVarieties()) {
			Assert.assertEquals(v.getVarietyName() + " IsOutOfSyncAvgPrice", false, v.getIsOutOfSyncAvgPrice());
			Assert.assertEquals(v.getVarietyName() + " IsOutOfSyncVarietyRemoved", false, v.getIsOutOfSyncVarietyRemoved());
		}

		if ( c.getClaimCalculationBerries() != null ) {
			assertOutOfSyncFlagsBerries(c, false);
		}

		if ( c.getClaimCalculationGrapes() != null ) {
			assertOutOfSyncFlagsGrapes(c, false);
		}

		if ( c.getClaimCalculationPlantAcres() != null ) { 
			assertOutOfSyncFlagsPlantAcres(c, false);
		}
		
		if ( c.getClaimCalculationPlantUnits() != null ) {
			assertOutOfSyncFlagsPlantUnits(c, false);
		}
		
		if ( c.getClaimCalculationGrainUnseeded() != null ) { 
			assertOutOfSyncFlagsGrainUnseeded(c, false);
		}
	}

	private void assertOutOfSyncFlagsNull(ClaimCalculationRsrc c) {

		Assert.assertNull("IsOutOfSync", c.getIsOutOfSync());
		Assert.assertNull("IsOutOfSyncGrowerAddrLine1", c.getIsOutOfSyncGrowerAddrLine1());
		Assert.assertNull("IsOutOfSyncGrowerAddrLine2", c.getIsOutOfSyncGrowerAddrLine2());
		Assert.assertNull("IsOutOfSyncGrowerCity", c.getIsOutOfSyncGrowerCity());
		Assert.assertNull("IsOutOfSyncGrowerProvince", c.getIsOutOfSyncGrowerProvince());
		Assert.assertNull("IsOutOfSyncGrowerName", c.getIsOutOfSyncGrowerName());
		Assert.assertNull("IsOutOfSyncGrowerNumber", c.getIsOutOfSyncGrowerNumber());
		Assert.assertNull("IsOutOfSyncGrowerPostalCode", c.getIsOutOfSyncGrowerPostalCode());
		Assert.assertNull("IsOutOfSyncVarietyAdded", c.getIsOutOfSyncVarietyAdded());

		//grapes
		if(c.getClaimCalculationGrapes() != null) {
			Assert.assertNull("IsOutOfSyncInsurableValueSelected", c.getClaimCalculationGrapes().getIsOutOfSyncInsurableValueSelected());
			Assert.assertNull("IsOutOfSyncInsurableValueHundredPct", c.getClaimCalculationGrapes().getIsOutOfSyncInsurableValueHundredPct());
			Assert.assertNull("IsOutOfSyncCoverageAmount", c.getClaimCalculationGrapes().getIsOutOfSyncCoverageAmount());
		}

		//berries
		if(c.getClaimCalculationBerries() != null) {
			Assert.assertNull("IsOutOfSyncTotalProbableYield", c.getClaimCalculationBerries().getIsOutOfSyncTotalProbableYield());
			Assert.assertNull("IsOutOfSyncProductionGuarantee", c.getClaimCalculationBerries().getIsOutOfSyncProductionGuarantee());
			Assert.assertNull("IsOutOfSyncDeductibleLevel", c.getClaimCalculationBerries().getIsOutOfSyncDeductibleLevel());
			Assert.assertNull("IsOutOfSyncDeclaredAcres", c.getClaimCalculationBerries().getIsOutOfSyncDeclaredAcres());
			Assert.assertNull("IsOutOfSyncInsurableValueSelected", c.getClaimCalculationBerries().getIsOutOfSyncInsurableValueSelected());
			Assert.assertNull("IsOutOfSyncInsurableValueHundredPct", c.getClaimCalculationBerries().getIsOutOfSyncInsurableValueHundredPct());
		}
		
		//plant units
		if(c.getClaimCalculationPlantUnits() != null) {
			Assert.assertNull("IsOutOfSyncInsuredUnits", c.getClaimCalculationPlantUnits().getIsOutOfSyncInsuredUnits());
			Assert.assertNull("IsOutOfSyncDeductibleLevel", c.getClaimCalculationPlantUnits().getIsOutOfSyncDeductibleLevel());
			Assert.assertNull("IsOutOfSyncInsurableValue", c.getClaimCalculationPlantUnits().getIsOutOfSyncInsurableValue());
		}

		//plant acres
		if(c.getClaimCalculationPlantAcres() != null) {
			Assert.assertNull("IsOutOfSyncDeclaredAcres", c.getClaimCalculationPlantAcres().getIsOutOfSyncDeclaredAcres());
			Assert.assertNull("IsOutOfSyncDeductibleLevel", c.getClaimCalculationPlantAcres().getIsOutOfSyncDeductibleLevel());
			Assert.assertNull("IsOutOfSyncInsurableValue", c.getClaimCalculationPlantAcres().getIsOutOfSyncInsurableValue());
		}

		//grain unseeded
		if(c.getClaimCalculationGrainUnseeded() != null) {
			Assert.assertNull("IsOutOfSyncInsuredAcres", c.getClaimCalculationGrainUnseeded().getIsOutOfSyncInsuredAcres());
			Assert.assertNull("IsOutOfSyncDeductibleLevel", c.getClaimCalculationGrainUnseeded().getIsOutOfSyncDeductibleLevel());
			Assert.assertNull("IsOutOfSyncInsurableValue", c.getClaimCalculationGrainUnseeded().getIsOutOfSyncInsurableValue());		
		}
		
		for (ClaimCalculationVariety v : c.getVarieties()) {
			Assert.assertNull(v.getVarietyName() + " IsOutOfSyncAvgPrice", v.getIsOutOfSyncAvgPrice());
			Assert.assertNull(v.getVarietyName() + " IsOutOfSyncVarietyRemoved", v.getIsOutOfSyncVarietyRemoved());
		}		

	}
	
	private void assertOutOfSyncFlagsGrapes(ClaimCalculationRsrc c, boolean flagValue) {

		Assert.assertEquals("IsOutOfSync", flagValue, c.getIsOutOfSync());
		Assert.assertEquals("IsOutOfSyncInsurableValueSelected", flagValue, c.getClaimCalculationGrapes().getIsOutOfSyncInsurableValueSelected());
		Assert.assertEquals("IsOutOfSyncInsurableValueHundredPct", flagValue, c.getClaimCalculationGrapes().getIsOutOfSyncInsurableValueHundredPct());
		Assert.assertEquals("IsOutOfSyncCoverageAmount", flagValue, c.getClaimCalculationGrapes().getIsOutOfSyncCoverageAmount());

	}	

	
	private void assertOutOfSyncFlagsBerries(ClaimCalculationRsrc c, boolean flagValue) {

		Assert.assertEquals("IsOutOfSync", flagValue, c.getIsOutOfSync());
		Assert.assertEquals("IsOutOfSyncTotalProbableYield", flagValue, c.getClaimCalculationBerries().getIsOutOfSyncTotalProbableYield());
		Assert.assertEquals("IsOutOfSyncProductionGuarantee", flagValue, c.getClaimCalculationBerries().getIsOutOfSyncProductionGuarantee());
		Assert.assertEquals("IsOutOfSyncDeductibleLevel", flagValue, c.getClaimCalculationBerries().getIsOutOfSyncDeductibleLevel());
		Assert.assertEquals("IsOutOfSyncDeclaredAcres", flagValue, c.getClaimCalculationBerries().getIsOutOfSyncDeclaredAcres());
		Assert.assertEquals("IsOutOfSyncInsurableValueSelected", flagValue, c.getClaimCalculationBerries().getIsOutOfSyncInsurableValueSelected());
		Assert.assertEquals("IsOutOfSyncInsurableValueHundredPct", flagValue, c.getClaimCalculationBerries().getIsOutOfSyncInsurableValueHundredPct());

	}	

	private void assertOutOfSyncFlagsPlantUnits(ClaimCalculationRsrc c, boolean flagValue) {

		Assert.assertEquals("IsOutOfSync", flagValue, c.getIsOutOfSync());
		Assert.assertEquals("IsOutOfSyncInsuredUnits", flagValue, c.getClaimCalculationPlantUnits().getIsOutOfSyncInsuredUnits());
		Assert.assertEquals("IsOutOfSyncDeductibleLevel", flagValue, c.getClaimCalculationPlantUnits().getIsOutOfSyncDeductibleLevel());
		Assert.assertEquals("IsOutOfSyncInsurableValue", flagValue, c.getClaimCalculationPlantUnits().getIsOutOfSyncInsurableValue());

	}
	
	private void assertOutOfSyncFlagsPlantAcres(ClaimCalculationRsrc c, boolean flagValue) {

		Assert.assertEquals("IsOutOfSync", flagValue, c.getIsOutOfSync());
		Assert.assertEquals("IsOutOfSyncDeclaredAcres", flagValue, c.getClaimCalculationPlantAcres().getIsOutOfSyncDeclaredAcres());
		Assert.assertEquals("IsOutOfSyncDeductibleLevel", flagValue, c.getClaimCalculationPlantAcres().getIsOutOfSyncDeductibleLevel());
		Assert.assertEquals("IsOutOfSyncInsurableValue", flagValue, c.getClaimCalculationPlantAcres().getIsOutOfSyncInsurableValue());

	}		

	private void assertOutOfSyncFlagsGrainUnseeded(ClaimCalculationRsrc c, boolean flagValue) {

		Assert.assertEquals("IsOutOfSync", flagValue, c.getIsOutOfSync());
		Assert.assertEquals("IsOutOfSyncInsuredAcres", flagValue, c.getClaimCalculationGrainUnseeded().getIsOutOfSyncInsuredAcres());
		Assert.assertEquals("IsOutOfSyncDeductibleLevel", flagValue, c.getClaimCalculationGrainUnseeded().getIsOutOfSyncDeductibleLevel());
		Assert.assertEquals("IsOutOfSyncInsurableValue", flagValue, c.getClaimCalculationGrainUnseeded().getIsOutOfSyncInsurableValue());
		
	}
	
	private void assertOutOfSyncFlagsFalseExceptOne(ClaimCalculationRsrc c, String f) {
		assertOutOfSyncFlagsFalseExceptOne(c, f, null);		
	}
	
	private void assertOutOfSyncFlagsFalseExceptOne(ClaimCalculationRsrc c, String f, Integer varietyId) {

		Assert.assertEquals("IsOutOfSync", true, c.getIsOutOfSync());
		Assert.assertEquals("IsOutOfSyncGrowerAddrLine1", f.equals("GrowerAddrLine1"), c.getIsOutOfSyncGrowerAddrLine1());
		Assert.assertEquals("IsOutOfSyncGrowerAddrLine2", f.equals("GrowerAddrLine2"), c.getIsOutOfSyncGrowerAddrLine2());
		Assert.assertEquals("IsOutOfSyncGrowerCity", f.equals("GrowerCity"), c.getIsOutOfSyncGrowerCity());
		Assert.assertEquals("IsOutOfSyncGrowerProvince", f.equals("GrowerProvince"), c.getIsOutOfSyncGrowerProvince());
		Assert.assertEquals("IsOutOfSyncGrowerName", f.equals("GrowerName"), c.getIsOutOfSyncGrowerName());
		Assert.assertEquals("IsOutOfSyncGrowerNumber", f.equals("GrowerNumber"), c.getIsOutOfSyncGrowerNumber());
		Assert.assertEquals("IsOutOfSyncGrowerPostalCode", f.equals("GrowerPostalCode"), c.getIsOutOfSyncGrowerPostalCode());

		if (c.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAPES.toString())
				&& c.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.Quantity.getCode())) {
			Assert.assertEquals("IsOutOfSyncVarietyAdded", f.equals("VarietyAdded"), c.getIsOutOfSyncVarietyAdded());
		}		

		//grapes
		if(c.getClaimCalculationGrapes() != null) {
			Assert.assertEquals("IsOutOfSyncInsurableValueSelected", f.equals("InsurableValueSelected"), c.getClaimCalculationGrapes().getIsOutOfSyncInsurableValueSelected());
			Assert.assertEquals("IsOutOfSyncInsurableValueHundredPct", f.equals("InsurableValueHundredPct"), c.getClaimCalculationGrapes().getIsOutOfSyncInsurableValueHundredPct());
			Assert.assertEquals("IsOutOfSyncCoverageAmount", f.equals("CoverageAmount"), c.getClaimCalculationGrapes().getIsOutOfSyncCoverageAmount());
		}

		//berries
		if(c.getClaimCalculationBerries() != null) {
			Assert.assertEquals("IsOutOfSyncTotalProbableYield", f.equals("TotalProbableYield"), c.getClaimCalculationBerries().getIsOutOfSyncTotalProbableYield());
			Assert.assertEquals("IsOutOfSyncProductionGuarantee", f.equals("ProductionGuarantee"), c.getClaimCalculationBerries().getIsOutOfSyncProductionGuarantee());
			Assert.assertEquals("IsOutOfSyncDeductibleLevel", f.equals("DeductibleLevel"), c.getClaimCalculationBerries().getIsOutOfSyncDeductibleLevel());
			Assert.assertEquals("IsOutOfSyncDeclaredAcres", f.equals("DeclaredAcres"), c.getClaimCalculationBerries().getIsOutOfSyncDeclaredAcres());
			Assert.assertEquals("IsOutOfSyncInsurableValueSelected", f.equals("InsurableValueSelected"), c.getClaimCalculationBerries().getIsOutOfSyncInsurableValueSelected());
			Assert.assertEquals("IsOutOfSyncInsurableValueHundredPct", f.equals("InsurableValueHundredPct"), c.getClaimCalculationBerries().getIsOutOfSyncInsurableValueHundredPct());
		}
		
		//plant units
		if(c.getClaimCalculationPlantUnits() != null) {
			Assert.assertEquals("IsOutOfSyncInsuredUnits", f.equals("PlantUnitsInsuredUnits"), c.getClaimCalculationPlantUnits().getIsOutOfSyncInsuredUnits());
			Assert.assertEquals("IsOutOfSyncDeductibleLevel", f.equals("PlantUnitsDeductibleLevel"), c.getClaimCalculationPlantUnits().getIsOutOfSyncDeductibleLevel());
			Assert.assertEquals("IsOutOfSyncInsurableValue", f.equals("PlantUnitsInsurableValue"), c.getClaimCalculationPlantUnits().getIsOutOfSyncInsurableValue());
		}

		//plant acres
		if(c.getClaimCalculationPlantAcres() != null) {
			Assert.assertEquals("IsOutOfSyncDeclaredAcres", f.equals("PlantAcresDeclaredAcres"), c.getClaimCalculationPlantAcres().getIsOutOfSyncDeclaredAcres());
			Assert.assertEquals("IsOutOfSyncDeductibleLevel", f.equals("PlantAcresDeductibleLevel"), c.getClaimCalculationPlantAcres().getIsOutOfSyncDeductibleLevel());
			Assert.assertEquals("IsOutOfSyncInsurableValue", f.equals("PlantAcresInsurableValue"), c.getClaimCalculationPlantAcres().getIsOutOfSyncInsurableValue());
		}

		//grain unseeded
		if(c.getClaimCalculationGrainUnseeded() != null) {
			Assert.assertEquals("IsOutOfSyncInsuredAcres", f.equals("GrainUnseededInsuredAcres"), c.getClaimCalculationGrainUnseeded().getIsOutOfSyncInsuredAcres());
			Assert.assertEquals("IsOutOfSyncDeductibleLevel", f.equals("GrainUnseededDeductibleLevel"), c.getClaimCalculationGrainUnseeded().getIsOutOfSyncDeductibleLevel());
			Assert.assertEquals("IsOutOfSyncInsurableValue", f.equals("GrainUnseededInsurableValue"), c.getClaimCalculationGrainUnseeded().getIsOutOfSyncInsurableValue());
		}
		
		for (ClaimCalculationVariety v : c.getVarieties()) {
			Assert.assertEquals(v.getVarietyName() + " IsOutOfSyncAvgPrice", (f.equals("AvgPrice") && v.getCropVarietyId().equals(varietyId)), v.getIsOutOfSyncAvgPrice());
			Assert.assertEquals(v.getVarietyName() + " IsOutOfSyncVarietyRemoved", (f.equals("VarietyRemoved") && v.getCropVarietyId().equals(varietyId)), v.getIsOutOfSyncVarietyRemoved());
		}		

	}

}
