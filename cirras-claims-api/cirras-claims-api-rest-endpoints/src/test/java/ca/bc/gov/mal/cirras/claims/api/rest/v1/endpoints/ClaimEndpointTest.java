package ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.claims.api.rest.client.v1.CirrasClaimService;
import ca.bc.gov.mal.cirras.claims.api.rest.client.v1.CirrasClaimServiceException;
import ca.bc.gov.mal.cirras.claims.api.rest.client.v1.ValidationException;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.EndpointsRsrc;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationGrainUnseeded;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationGrainQuantityDetailDto;
import ca.bc.gov.mal.cirras.claims.service.api.v1.util.ClaimsServiceEnums;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculation;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationGrainQuantity;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationGrainQuantityDetail;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationGrainSpotLoss;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.ClaimCalculationListRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.ClaimCalculationRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.ClaimListRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.ClaimRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class ClaimEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(ClaimEndpointTest.class);


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

	private CirrasClaimService service;
	private EndpointsRsrc topLevelEndpoints;

	private String currentClaimNumber;
	
	@Before
	public void prepareTests() throws CirrasClaimServiceException, Oauth2ClientException{
		service = getService(SCOPES);
		topLevelEndpoints = service.getTopLevelEndpoints();
	}
	
	@After 
	public void cleanUp() throws CirrasClaimServiceException, NotFoundDaoException, DaoException {
		deleteClaimCalculation(currentClaimNumber);
	}

	
	@Test
	public void testGetClaim() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testGetClaim");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
        String claimNumber = "28082";//"28084";//25749 (claims with calculations); 28082 (claim without calculation) 28245 = Berries
		String policyNumber = null;//"401837-06";
		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		ClaimListRsrc searchResults = service.getClaimList(topLevelEndpoints, claimNumber, policyNumber, null, null, null, pageNumber, pageRowCount);
		
		
		if(searchResults.getCollection().size() > 0) {
		
			ClaimRsrc claimRsrc = searchResults.getCollection().get(0);
			//Only works if there is no calculation yet
			ClaimCalculationRsrc claimCalculationRsrc = service.getClaim(claimRsrc);
	
			Assert.assertNotNull(claimCalculationRsrc);
		}
//
//		logger.debug(">testGetClaim");
	}
	
	@Test
	public void testGetInsertUpdateDeleteGrainUnseededClaimRounding() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testGetInsertUpdateDeleteGrainUnseededClaimRounding()");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		/*
		 	AdHoc test to test this business rules:
		 	Unseeded Loss Claim (Line M): 
		 		if the max number of eligible acres are the same as eligible unseeded acres 
		 		then the Unseeded Loss Claim needs to be the same as the calculated coverage
			Same formula with line numbers: If L = F then M = H else M = L * G. 
		*/
		
        String claimNumber = "37166";//Has to be a claim with a saved unseeded calculation
		Integer pageNumber = 0;
		Integer pageRowCount = 100;
		
		//Values for Claim number 37166
		Double insuredAcres = 11964.0;
		Integer deductibleLevel = 20;
		Double insurableValue = 60.0;

		ClaimCalculationListRsrc searchResults = service.getClaimCalculations(topLevelEndpoints, claimNumber, null, null, null, null, null, null, "claimNumber", "ASC", pageNumber, pageRowCount);

		
		if(searchResults.getCollection().size() > 0) {
			
			ClaimCalculationRsrc claimCalcRsrc = searchResults.getCollection().get(0);
			//Only works if there is no calculation yet
			ClaimCalculationRsrc claimCalculationRsrc = service.getClaimCalculation(claimCalcRsrc, false);
	
			Assert.assertNotNull(claimCalculationRsrc);
			Assert.assertNotNull(claimCalculationRsrc.getClaimCalculationGrainUnseeded());
			
			ClaimCalculationGrainUnseeded grainUnseeeded = claimCalculationRsrc.getClaimCalculationGrainUnseeded();
			
			Assert.assertEquals(insuredAcres, grainUnseeeded.getInsuredAcres());
			Assert.assertEquals(insurableValue, grainUnseeeded.getInsurableValue());
			Assert.assertEquals(deductibleLevel, grainUnseeeded.getDeductibleLevel());
			
			Assert.assertNotNull(claimCalculationRsrc.getClaimCalculationGuid());
			Assert.assertNotNull(grainUnseeeded.getClaimCalculationGrainUnseededGuid());
			Assert.assertNotNull(grainUnseeeded.getClaimCalculationGuid());
			
			
			ClaimCalculationRsrc updatedCalculation = service.updateClaimCalculation(claimCalculationRsrc, null);			

		}

		logger.debug(">testGetInsertUpdateDeleteGrainUnseededClaimRounding()");
	}
	
	@Test
	public void testGetInsertUpdateDeleteGrainUnseededClaim() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testGetInsertUpdateDeleteGrainUnseededClaim()");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
        String claimNumber = "37164";//????? (claims with calculations); 37164 (claim without calculation)
        currentClaimNumber = claimNumber;
		String policyNumber = null;//"145110-24";
		Integer pageNumber = 0;
		Integer pageRowCount = 100;
		ClaimListRsrc searchResults = service.getClaimList(topLevelEndpoints, claimNumber, policyNumber, null, null, null, pageNumber, pageRowCount);
		
		//Values for Claim number 37164
		Double insuredAcres = 987.0;
		Integer deductibleLevel = 10;
		Double insurableValue = 75.0;

		
		if(searchResults.getCollection().size() > 0) {
		
			ClaimRsrc claimRsrc = searchResults.getCollection().get(0);
			//Only works if there is no calculation yet
			ClaimCalculationRsrc claimCalculationRsrc = service.getClaim(claimRsrc);
	
			Assert.assertNotNull(claimCalculationRsrc);
			Assert.assertNotNull(claimCalculationRsrc.getClaimCalculationGrainUnseeded());
			Assert.assertEquals("OPEN", claimCalculationRsrc.getClaimStatusCode());
			
			ClaimCalculationGrainUnseeded grainUnseeeded = claimCalculationRsrc.getClaimCalculationGrainUnseeded();
			
			Assert.assertEquals(insuredAcres, grainUnseeeded.getInsuredAcres());
			Assert.assertEquals(insurableValue, grainUnseeeded.getInsurableValue());
			Assert.assertEquals(deductibleLevel, grainUnseeeded.getDeductibleLevel());
			
			Assert.assertNull(claimCalculationRsrc.getClaimCalculationGuid());
			Assert.assertNull(grainUnseeeded.getClaimCalculationGrainUnseededGuid());
			Assert.assertNull(grainUnseeeded.getClaimCalculationGuid());
			
			//Create new calculation
			//User Entered
			grainUnseeeded.setLessAdjustmentAcres(18.5);
			grainUnseeeded.setUnseededAcres(986.0);
			grainUnseeeded.setLessAssessmentAcres(15.5);

			//Calculated
			Double adjustedAcres = insuredAcres -  grainUnseeeded.getLessAdjustmentAcres();
			Double deductibleAcres = adjustedAcres * (deductibleLevel/100.0);
			Double maxEligibleAcres = adjustedAcres - deductibleAcres;
			Double coverageValue = (double)Math.round(maxEligibleAcres * insurableValue);
			Double eligibleUnseededAcres = grainUnseeeded.getUnseededAcres() - grainUnseeeded.getLessAssessmentAcres() - deductibleAcres;

			Double totalClaimAmount = eligibleUnseededAcres * insurableValue;

			//Create new calculation
			ClaimCalculationRsrc createdCalculation = service.createClaimCalculation(claimCalculationRsrc);

			Assert.assertNotNull(createdCalculation);
			Assert.assertNotNull(createdCalculation.getClaimCalculationGrainUnseeded());
			
			Assert.assertNotNull(createdCalculation);
			Assert.assertNotNull(createdCalculation.getClaimCalculationGrainUnseeded());
			//Assert.assertEquals("OPEN", createdCalculation.getClaimStatusCode());

			Assert.assertNotNull(createdCalculation.getClaimCalculationGuid());
			Assert.assertNotNull(createdCalculation.getClaimCalculationGrainUnseeded().getClaimCalculationGrainUnseededGuid());
			Assert.assertNotNull(createdCalculation.getClaimCalculationGrainUnseeded().getClaimCalculationGuid());

			Assert.assertEquals(insuredAcres, createdCalculation.getClaimCalculationGrainUnseeded().getInsuredAcres());
			Assert.assertEquals(insurableValue, createdCalculation.getClaimCalculationGrainUnseeded().getInsurableValue());
			Assert.assertEquals(deductibleLevel, createdCalculation.getClaimCalculationGrainUnseeded().getDeductibleLevel());

			Assert.assertEquals(grainUnseeeded.getLessAdjustmentAcres(), createdCalculation.getClaimCalculationGrainUnseeded().getLessAdjustmentAcres());
			Assert.assertEquals(adjustedAcres, createdCalculation.getClaimCalculationGrainUnseeded().getAdjustedAcres());
			Assert.assertEquals(deductibleAcres, createdCalculation.getClaimCalculationGrainUnseeded().getDeductibleAcres(), 0.00005);
			Assert.assertEquals(maxEligibleAcres, createdCalculation.getClaimCalculationGrainUnseeded().getMaxEligibleAcres(), 0.00005);
			Assert.assertEquals(coverageValue, createdCalculation.getClaimCalculationGrainUnseeded().getCoverageValue());
			Assert.assertEquals(grainUnseeeded.getUnseededAcres(), createdCalculation.getClaimCalculationGrainUnseeded().getUnseededAcres());
			Assert.assertEquals(grainUnseeeded.getLessAssessmentAcres(), createdCalculation.getClaimCalculationGrainUnseeded().getLessAssessmentAcres());
			Assert.assertEquals(eligibleUnseededAcres, createdCalculation.getClaimCalculationGrainUnseeded().getEligibleUnseededAcres());
			Assert.assertEquals(totalClaimAmount, createdCalculation.getTotalClaimAmount());

			//update calculation
			
			grainUnseeeded = createdCalculation.getClaimCalculationGrainUnseeded();
			
			//User Entered
			grainUnseeeded.setLessAdjustmentAcres(null);
			grainUnseeeded.setUnseededAcres(787.0);
			grainUnseeeded.setLessAssessmentAcres(null);

			//Calculated
			adjustedAcres = insuredAcres - notNull(grainUnseeeded.getLessAdjustmentAcres(), 0.0);
			deductibleAcres = adjustedAcres * (deductibleLevel/100.0);
			maxEligibleAcres = adjustedAcres - deductibleAcres;
			coverageValue = (double)Math.round(maxEligibleAcres * insurableValue);
			eligibleUnseededAcres = notNull(grainUnseeeded.getUnseededAcres(), 0.0) - notNull(grainUnseeeded.getLessAssessmentAcres(), 0.0) - deductibleAcres;

			totalClaimAmount = eligibleUnseededAcres * insurableValue;

			ClaimCalculationRsrc updatedCalculation = service.updateClaimCalculation(createdCalculation, null);

			Assert.assertEquals(insuredAcres, updatedCalculation.getClaimCalculationGrainUnseeded().getInsuredAcres());
			Assert.assertEquals(insurableValue, updatedCalculation.getClaimCalculationGrainUnseeded().getInsurableValue());
			Assert.assertEquals(deductibleLevel, updatedCalculation.getClaimCalculationGrainUnseeded().getDeductibleLevel());

			Assert.assertEquals(grainUnseeeded.getLessAdjustmentAcres(), updatedCalculation.getClaimCalculationGrainUnseeded().getLessAdjustmentAcres());
			Assert.assertEquals(adjustedAcres, updatedCalculation.getClaimCalculationGrainUnseeded().getAdjustedAcres());
			Assert.assertEquals(deductibleAcres, updatedCalculation.getClaimCalculationGrainUnseeded().getDeductibleAcres(), 0.00005);
			Assert.assertEquals(maxEligibleAcres, updatedCalculation.getClaimCalculationGrainUnseeded().getMaxEligibleAcres(), 0.00005);
			Assert.assertEquals(coverageValue, updatedCalculation.getClaimCalculationGrainUnseeded().getCoverageValue());
			Assert.assertEquals(grainUnseeeded.getUnseededAcres(), updatedCalculation.getClaimCalculationGrainUnseeded().getUnseededAcres());
			Assert.assertEquals(grainUnseeeded.getLessAssessmentAcres(), updatedCalculation.getClaimCalculationGrainUnseeded().getLessAssessmentAcres());
			Assert.assertEquals(eligibleUnseededAcres, updatedCalculation.getClaimCalculationGrainUnseeded().getEligibleUnseededAcres());
			Assert.assertEquals(totalClaimAmount, updatedCalculation.getTotalClaimAmount());

			//Update with all user input = null => expect eligible unseeded acres and total claim amount = 0 even thought it would be < 0
			updatedCalculation.getClaimCalculationGrainUnseeded().setUnseededAcres(null);
			eligibleUnseededAcres = 0.0;
			totalClaimAmount = 0.0;
			
			updatedCalculation = service.updateClaimCalculation(updatedCalculation, null);

			Assert.assertEquals(insuredAcres, updatedCalculation.getClaimCalculationGrainUnseeded().getInsuredAcres());
			Assert.assertEquals(insurableValue, updatedCalculation.getClaimCalculationGrainUnseeded().getInsurableValue());
			Assert.assertEquals(deductibleLevel, updatedCalculation.getClaimCalculationGrainUnseeded().getDeductibleLevel());

			Assert.assertEquals(grainUnseeeded.getLessAdjustmentAcres(), updatedCalculation.getClaimCalculationGrainUnseeded().getLessAdjustmentAcres());
			Assert.assertEquals(adjustedAcres, updatedCalculation.getClaimCalculationGrainUnseeded().getAdjustedAcres());
			Assert.assertEquals(deductibleAcres, updatedCalculation.getClaimCalculationGrainUnseeded().getDeductibleAcres(), 0.00005);
			Assert.assertEquals(maxEligibleAcres, updatedCalculation.getClaimCalculationGrainUnseeded().getMaxEligibleAcres(), 0.00005);
			Assert.assertEquals(coverageValue, updatedCalculation.getClaimCalculationGrainUnseeded().getCoverageValue());
			Assert.assertEquals(null, updatedCalculation.getClaimCalculationGrainUnseeded().getUnseededAcres());
			Assert.assertEquals(grainUnseeeded.getLessAssessmentAcres(), updatedCalculation.getClaimCalculationGrainUnseeded().getLessAssessmentAcres());
			Assert.assertEquals(eligibleUnseededAcres, updatedCalculation.getClaimCalculationGrainUnseeded().getEligibleUnseededAcres());
			Assert.assertEquals(totalClaimAmount, updatedCalculation.getTotalClaimAmount());
			
			//Total Loss (rounding of coverage value)
			//User Entered
			grainUnseeeded = updatedCalculation.getClaimCalculationGrainUnseeded();

			grainUnseeeded.setLessAdjustmentAcres(null);
			grainUnseeeded.setUnseededAcres(insuredAcres);
			grainUnseeeded.setLessAssessmentAcres(null);

			//Calculated
			adjustedAcres = insuredAcres - notNull(grainUnseeeded.getLessAdjustmentAcres(), 0.0);
			deductibleAcres = adjustedAcres * (deductibleLevel/100.0);
			maxEligibleAcres = adjustedAcres - deductibleAcres;
			coverageValue = (double)Math.round(maxEligibleAcres * insurableValue);
			eligibleUnseededAcres = notNull(grainUnseeeded.getUnseededAcres(), 0.0) - notNull(grainUnseeeded.getLessAssessmentAcres(), 0.0) - deductibleAcres;

			totalClaimAmount = eligibleUnseededAcres * insurableValue;

			updatedCalculation = service.updateClaimCalculation(updatedCalculation, null);
			
			//Get Total Claim Amount needs to be different from calculated Claim Amount from above.
			//This is necessary to properly test the total loss business rules that claim amount equals rounded coverage value
			Assert.assertNotEquals(coverageValue, totalClaimAmount);

			Assert.assertEquals(insuredAcres, updatedCalculation.getClaimCalculationGrainUnseeded().getInsuredAcres());
			Assert.assertEquals(insurableValue, updatedCalculation.getClaimCalculationGrainUnseeded().getInsurableValue());
			Assert.assertEquals(deductibleLevel, updatedCalculation.getClaimCalculationGrainUnseeded().getDeductibleLevel());

			Assert.assertEquals(grainUnseeeded.getLessAdjustmentAcres(), updatedCalculation.getClaimCalculationGrainUnseeded().getLessAdjustmentAcres());
			Assert.assertEquals(adjustedAcres, updatedCalculation.getClaimCalculationGrainUnseeded().getAdjustedAcres());
			Assert.assertEquals(deductibleAcres, updatedCalculation.getClaimCalculationGrainUnseeded().getDeductibleAcres(), 0.00005);
			Assert.assertEquals(maxEligibleAcres, updatedCalculation.getClaimCalculationGrainUnseeded().getMaxEligibleAcres(), 0.00005);
			Assert.assertEquals(coverageValue, updatedCalculation.getClaimCalculationGrainUnseeded().getCoverageValue());
			Assert.assertEquals(grainUnseeeded.getUnseededAcres(), updatedCalculation.getClaimCalculationGrainUnseeded().getUnseededAcres());
			Assert.assertEquals(grainUnseeeded.getLessAssessmentAcres(), updatedCalculation.getClaimCalculationGrainUnseeded().getLessAssessmentAcres());
			Assert.assertEquals(eligibleUnseededAcres, updatedCalculation.getClaimCalculationGrainUnseeded().getEligibleUnseededAcres());
			Assert.assertEquals(coverageValue, updatedCalculation.getTotalClaimAmount());
			
			
			//Delete calculation
			deleteClaimCalculation(claimNumber);
		}

		logger.debug(">testGetInsertUpdateDeleteGrainUnseededClaim()");
	}	
	
	@Test
	public void testGetInsertUpdateDeleteGrainSpotLossClaim() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testGetInsertUpdateDeleteGrainSpotLossClaim()");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
        String claimNumber = "37167";       // Set to Open Grain Spot Loss Claim without a calculation.
        currentClaimNumber = claimNumber;
		String policyNumber = null;
		Integer pageNumber = 0;
		Integer pageRowCount = 100;
		ClaimListRsrc searchResults = service.getClaimList(topLevelEndpoints, claimNumber, policyNumber, null, null, null, pageNumber, pageRowCount);
		
		//Values for Claim number 37167
		Integer deductibleLevel = 5; //Always 5% (hard coded)
		Double insuredAcres = 2465.0;
		Double coverageAmtPerAcre = 300.0;
		Double coverageValue = 739500.0;

		Assert.assertEquals(1, searchResults.getCollection().size());
		
		ClaimRsrc claimRsrc = searchResults.getCollection().get(0);
		//Only works if there is no calculation yet
		ClaimCalculationRsrc claimCalculationRsrc = service.getClaim(claimRsrc);

		Assert.assertNotNull(claimCalculationRsrc);
		Assert.assertNotNull(claimCalculationRsrc.getClaimCalculationGrainSpotLoss());
		Assert.assertEquals("OPEN", claimCalculationRsrc.getClaimStatusCode());
		
		ClaimCalculationGrainSpotLoss grainSpotLoss = claimCalculationRsrc.getClaimCalculationGrainSpotLoss();
		
		Assert.assertEquals(insuredAcres, grainSpotLoss.getInsuredAcres());
		Assert.assertEquals(coverageAmtPerAcre, grainSpotLoss.getCoverageAmtPerAcre());
		Assert.assertEquals(coverageValue, grainSpotLoss.getCoverageValue());
		Assert.assertEquals(deductibleLevel, grainSpotLoss.getDeductible());
		
		Assert.assertNull(grainSpotLoss.getAdjustedAcres());
		Assert.assertNull(grainSpotLoss.getEligibleYieldReduction());
		Assert.assertNull(grainSpotLoss.getPercentYieldReduction());
		Assert.assertNull(grainSpotLoss.getSpotLossReductionValue());
		
		Assert.assertNull(claimCalculationRsrc.getClaimCalculationGuid());
		Assert.assertNull(grainSpotLoss.getClaimCalculationGrainSpotLossGuid());
		Assert.assertNull(grainSpotLoss.getClaimCalculationGuid());
		

		//Create new calculation
		//User Entered
		grainSpotLoss.setAdjustedAcres(12.3456);
		grainSpotLoss.setPercentYieldReduction(10.55);

		//Calculated
		Double eligibleYieldReduction = 1.3025; // Adjusted Acres x Percent Yield Reduction.
		Double spotLossReductionValue = 390.7382; // Coverage Amt Per Acre x Eligible Yield Reduction.
		Double totalClaimAmount = 205.55; // Adjusted Acres x (Percent Yield Reduction - Deductible) x Coverage Amount Per Acre
		
		
		//Create new calculation
		ClaimCalculationRsrc createdCalculation = service.createClaimCalculation(claimCalculationRsrc);

		Assert.assertNotNull(createdCalculation);
		Assert.assertNotNull(createdCalculation.getClaimCalculationGrainSpotLoss());

		Assert.assertNotNull(createdCalculation.getClaimCalculationGuid());
		Assert.assertNotNull(createdCalculation.getClaimCalculationGrainSpotLoss().getClaimCalculationGrainSpotLossGuid());
		Assert.assertNotNull(createdCalculation.getClaimCalculationGrainSpotLoss().getClaimCalculationGuid());

		
		Assert.assertEquals(grainSpotLoss.getInsuredAcres(), createdCalculation.getClaimCalculationGrainSpotLoss().getInsuredAcres());
		Assert.assertEquals(grainSpotLoss.getCoverageAmtPerAcre(), createdCalculation.getClaimCalculationGrainSpotLoss().getCoverageAmtPerAcre());
		Assert.assertEquals(grainSpotLoss.getCoverageValue(), createdCalculation.getClaimCalculationGrainSpotLoss().getCoverageValue());
		Assert.assertEquals(grainSpotLoss.getDeductible(), createdCalculation.getClaimCalculationGrainSpotLoss().getDeductible());
		
		Assert.assertEquals(grainSpotLoss.getAdjustedAcres(), createdCalculation.getClaimCalculationGrainSpotLoss().getAdjustedAcres());
		Assert.assertEquals(grainSpotLoss.getPercentYieldReduction(), createdCalculation.getClaimCalculationGrainSpotLoss().getPercentYieldReduction());
		Assert.assertEquals(eligibleYieldReduction, createdCalculation.getClaimCalculationGrainSpotLoss().getEligibleYieldReduction());
		Assert.assertEquals(spotLossReductionValue, createdCalculation.getClaimCalculationGrainSpotLoss().getSpotLossReductionValue());
		
		Assert.assertEquals(totalClaimAmount, createdCalculation.getTotalClaimAmount());
		

		//update calculation
		grainSpotLoss = createdCalculation.getClaimCalculationGrainSpotLoss();

		//User Entered
		grainSpotLoss.setAdjustedAcres(98.7654);
		grainSpotLoss.setPercentYieldReduction(6.78);

		//Calculated
		eligibleYieldReduction = 6.6963; // Adjusted Acres x Percent Yield Reduction.
		spotLossReductionValue = 2008.8882; // Coverage Amt Per Acre x Eligible Yield Reduction.
		totalClaimAmount = 527.41; // Adjusted Acres x (Percent Yield Reduction - Deductible) x Coverage Amount Per Acre
		

		ClaimCalculationRsrc updatedCalculation = service.updateClaimCalculation(createdCalculation, null);

		Assert.assertEquals(grainSpotLoss.getInsuredAcres(), updatedCalculation.getClaimCalculationGrainSpotLoss().getInsuredAcres());
		Assert.assertEquals(grainSpotLoss.getCoverageAmtPerAcre(), updatedCalculation.getClaimCalculationGrainSpotLoss().getCoverageAmtPerAcre());
		Assert.assertEquals(grainSpotLoss.getCoverageValue(), updatedCalculation.getClaimCalculationGrainSpotLoss().getCoverageValue());
		Assert.assertEquals(grainSpotLoss.getDeductible(), updatedCalculation.getClaimCalculationGrainSpotLoss().getDeductible());
		
		Assert.assertEquals(grainSpotLoss.getAdjustedAcres(), updatedCalculation.getClaimCalculationGrainSpotLoss().getAdjustedAcres());
		Assert.assertEquals(grainSpotLoss.getPercentYieldReduction(), updatedCalculation.getClaimCalculationGrainSpotLoss().getPercentYieldReduction());
		Assert.assertEquals(eligibleYieldReduction, updatedCalculation.getClaimCalculationGrainSpotLoss().getEligibleYieldReduction());
		Assert.assertEquals(spotLossReductionValue, updatedCalculation.getClaimCalculationGrainSpotLoss().getSpotLossReductionValue());
		
		Assert.assertEquals(totalClaimAmount, updatedCalculation.getTotalClaimAmount());
			

		//Update with all user input = null => expect calculated values to be 0.
		grainSpotLoss = updatedCalculation.getClaimCalculationGrainSpotLoss();

		//User Entered
		grainSpotLoss.setAdjustedAcres(null);
		grainSpotLoss.setPercentYieldReduction(null);

		//Calculated
		eligibleYieldReduction = 0.0; // Adjusted Acres x Percent Yield Reduction.
		spotLossReductionValue = 0.0; // Coverage Amt Per Acre x Eligible Yield Reduction.
		totalClaimAmount = 0.0; // Adjusted Acres x (Percent Yield Reduction - Deductible) x Coverage Amount Per Acre

		updatedCalculation = service.updateClaimCalculation(updatedCalculation, null);

		Assert.assertEquals(grainSpotLoss.getInsuredAcres(), updatedCalculation.getClaimCalculationGrainSpotLoss().getInsuredAcres());
		Assert.assertEquals(grainSpotLoss.getCoverageAmtPerAcre(), updatedCalculation.getClaimCalculationGrainSpotLoss().getCoverageAmtPerAcre());
		Assert.assertEquals(grainSpotLoss.getCoverageValue(), updatedCalculation.getClaimCalculationGrainSpotLoss().getCoverageValue());
		Assert.assertEquals(grainSpotLoss.getDeductible(), updatedCalculation.getClaimCalculationGrainSpotLoss().getDeductible());
		
		Assert.assertEquals(grainSpotLoss.getAdjustedAcres(), updatedCalculation.getClaimCalculationGrainSpotLoss().getAdjustedAcres());
		Assert.assertEquals(grainSpotLoss.getPercentYieldReduction(), updatedCalculation.getClaimCalculationGrainSpotLoss().getPercentYieldReduction());
		Assert.assertEquals(eligibleYieldReduction, updatedCalculation.getClaimCalculationGrainSpotLoss().getEligibleYieldReduction());
		Assert.assertEquals(spotLossReductionValue, updatedCalculation.getClaimCalculationGrainSpotLoss().getSpotLossReductionValue());
		
		Assert.assertEquals(totalClaimAmount, updatedCalculation.getTotalClaimAmount());
		
		//Delete calculation
		deleteClaimCalculation(claimNumber);

		logger.debug(">testGetInsertUpdateDeleteGrainSpotLossClaim()");
	}
	
	@Test
	public void testGetInsertUpdateDeleteGrainQuantityClaim() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testGetInsertUpdateDeleteGrainQuantityClaim()");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		// Quantity Claim with linked product
        String claimNumber1 = "37195";       // Set to Open Grain Quantity Claim without a calculation.
        currentClaimNumber = claimNumber1;
		String policyNumber = null;
        ClaimListRsrc searchResults = service.getClaimList(topLevelEndpoints, claimNumber1, policyNumber, null, null, null, pageNumber, pageRowCount);
		
		//Values for Claim number 37195
		// From CIRRAS
		Integer cropCommodityId = 22;
		String commodityName = "FIELD PEA - PEDIGREED";
		Boolean isPedigreeInd = true;
		Double coverageDollars = 48513.0;
		Integer deductibleLevel = 30;
		Double selectedInsurableValue = 246.8571;
		Double acres = 250.0;
		Double probableYield = 1.1230;
		Double productionGuarantee = 197.000;

		// From CUWS
		Double yieldToCount = 120.0;

		// Linked Product/Claim
		Integer linkedClaimNumber = 37196;
		Integer linkedProductId = 1251643;
		
        searchResults = service.getClaimList(topLevelEndpoints, claimNumber1, policyNumber, null, null, null, pageNumber, pageRowCount);
		Assert.assertEquals(1, searchResults.getCollection().size());
		
		ClaimRsrc claimRsrc = searchResults.getCollection().get(0);
		//Only works if there is no calculation yet
		ClaimCalculationRsrc claimCalculationRsrc = service.getClaim(claimRsrc);

		Assert.assertNotNull(claimCalculationRsrc);
		Assert.assertNotNull(claimCalculationRsrc.getClaimCalculationGrainQuantity());
		Assert.assertNotNull(claimCalculationRsrc.getClaimCalculationGrainQuantityDetail());
		Assert.assertEquals("OPEN", claimCalculationRsrc.getClaimStatusCode());
		Assert.assertNotNull(claimCalculationRsrc.getCalculationComment());
		Assert.assertEquals(null, claimCalculationRsrc.getClaimCalculationGrainQuantityGuid());
		Assert.assertEquals(cropCommodityId, claimCalculationRsrc.getCropCommodityId());
		Assert.assertEquals(commodityName, claimCalculationRsrc.getCommodityName());
		Assert.assertEquals(isPedigreeInd, claimCalculationRsrc.getIsPedigreeInd());
		Assert.assertEquals(null, claimCalculationRsrc.getLinkedClaimCalculationGuid());
		Assert.assertEquals(linkedClaimNumber, claimCalculationRsrc.getLinkedClaimNumber());
		Assert.assertEquals(linkedProductId, claimCalculationRsrc.getLinkedProductId());
		Assert.assertEquals(null, claimCalculationRsrc.getLatestLinkedClaimCalculationGuid());
		Assert.assertEquals(null, claimCalculationRsrc.getLatestLinkedCalculationVersion());

		ClaimCalculationGrainQuantity grainQty = claimCalculationRsrc.getClaimCalculationGrainQuantity();
		ClaimCalculationGrainQuantityDetail grainQtyDetail = claimCalculationRsrc.getClaimCalculationGrainQuantityDetail();

		// Grain Quantity
		Assert.assertEquals(null, grainQty.getAdvancedClaim());
		Assert.assertEquals(null, grainQty.getClaimCalculationGrainQuantityGuid());
		Assert.assertEquals(null, grainQty.getMaxClaimPayable());
		Assert.assertEquals(null, grainQty.getProductionGuaranteeAmount());
		Assert.assertEquals(null, grainQty.getQuantityLossClaim());
		Assert.assertEquals(null, grainQty.getReseedClaim());
		Assert.assertEquals(null, grainQty.getTotalCoverageValue());
		Assert.assertEquals(null, grainQty.getTotalYieldLossValue());

		// Grain Quantity Detail
		// GUIDs
		Assert.assertEquals(null, grainQtyDetail.getClaimCalculationGrainQuantityDetailGuid());
		Assert.assertEquals(null, grainQtyDetail.getClaimCalculationGuid());
				
		// Values From CIRRAS
		Assert.assertEquals(coverageDollars, grainQtyDetail.getCoverageValue());
		Assert.assertEquals(deductibleLevel, grainQtyDetail.getDeductible());
		Assert.assertEquals(selectedInsurableValue, grainQtyDetail.getInsurableValue());
		Assert.assertEquals(acres, grainQtyDetail.getInsuredAcres());
		Assert.assertEquals(probableYield, grainQtyDetail.getProbableYield());
		Assert.assertEquals(productionGuarantee, grainQtyDetail.getProductionGuaranteeWeight());

		// Values from CUWS
		Assert.assertEquals(yieldToCount, grainQtyDetail.getTotalYieldToCount());
		
		// User Entered
		Assert.assertEquals(null, grainQtyDetail.getAssessedYield());
		Assert.assertEquals(null, grainQtyDetail.getDamagedAcres());
		Assert.assertEquals(null, grainQtyDetail.getEarlyEstDeemedYieldValue());
		Assert.assertEquals(null, grainQtyDetail.getInspEarlyEstYield());
		Assert.assertEquals(null, grainQtyDetail.getSeededAcres());
		
		// Calculated
		Assert.assertEquals(null, grainQtyDetail.getCalcEarlyEstYield());
		Assert.assertEquals(null, grainQtyDetail.getFiftyPercentProductionGuarantee());
		Assert.assertEquals(null, grainQtyDetail.getYieldValue());
		Assert.assertEquals(null, grainQtyDetail.getYieldValueWithEarlyEstDeemedYield());


		//Create new calculation
		//User Entered
		grainQtyDetail.setAssessedYield(5.0);
		grainQtyDetail.setDamagedAcres(50.0);
		grainQtyDetail.setSeededAcres(100.0);
		grainQtyDetail.setInspEarlyEstYield(45.0);
		grainQty.setReseedClaim(1000.0);
		grainQty.setAdvancedClaim(250.0);
		claimCalculationRsrc.setTotalClaimAmount(3000.0);
		
		ClaimCalculationGrainQuantity expectedGrainQuantity = new ClaimCalculationGrainQuantity();
		ClaimCalculationGrainQuantityDetail expectedGrainQuantityDetail = new ClaimCalculationGrainQuantityDetail();
		
		createExpectedGrainQuantityCalculation(claimCalculationRsrc, expectedGrainQuantity, expectedGrainQuantityDetail, null);
		
		//Create new calculation
		ClaimCalculationRsrc createdCalculation = service.createClaimCalculation(claimCalculationRsrc);

		Assert.assertNotNull(createdCalculation);
		Assert.assertNotNull(createdCalculation.getClaimCalculationGrainQuantity());
		Assert.assertNotNull(createdCalculation.getClaimCalculationGrainQuantityDetail());

		Assert.assertNotNull(createdCalculation.getClaimCalculationGuid());
		Assert.assertNotNull(createdCalculation.getClaimCalculationGrainQuantity().getClaimCalculationGrainQuantityGuid());
		Assert.assertNotNull(createdCalculation.getClaimCalculationGrainQuantityDetail().getClaimCalculationGrainQuantityDetailGuid());

		Assert.assertEquals(linkedClaimNumber, createdCalculation.getLinkedClaimNumber());
		Assert.assertEquals(linkedProductId, createdCalculation.getLinkedProductId());
		Assert.assertEquals(null, createdCalculation.getLinkedClaimCalculationGuid());
		Assert.assertEquals(null, createdCalculation.getLatestLinkedClaimCalculationGuid());
		Assert.assertEquals(null, createdCalculation.getLatestLinkedCalculationVersion());
		
		
		Assert.assertEquals(claimCalculationRsrc.getTotalClaimAmount(), createdCalculation.getTotalClaimAmount(), 0.00005);
		
		assertGrainQuantity(expectedGrainQuantity, createdCalculation.getClaimCalculationGrainQuantity());
		assertGrainQuantityDetail(expectedGrainQuantityDetail, createdCalculation.getClaimCalculationGrainQuantityDetail());

		//update calculation
		grainQty = createdCalculation.getClaimCalculationGrainQuantity();
		grainQtyDetail = createdCalculation.getClaimCalculationGrainQuantityDetail();


		//Update calculation
		//User Entered - Remove all optional values
		grainQtyDetail.setAssessedYield(null);
		grainQtyDetail.setDamagedAcres(null);
		grainQtyDetail.setSeededAcres(null);
		grainQtyDetail.setInspEarlyEstYield(null);
		grainQty.setReseedClaim(null);
		grainQty.setAdvancedClaim(null);
		createdCalculation.setTotalClaimAmount(4000.0);

		expectedGrainQuantity = new ClaimCalculationGrainQuantity();
		expectedGrainQuantityDetail = new ClaimCalculationGrainQuantityDetail();
		
		createExpectedGrainQuantityCalculation(createdCalculation, expectedGrainQuantity, expectedGrainQuantityDetail, null);

		ClaimCalculationRsrc updatedCalculation = service.updateClaimCalculation(createdCalculation, null);

		Assert.assertNotNull(updatedCalculation);
		Assert.assertNotNull(updatedCalculation.getClaimCalculationGrainQuantity());
		Assert.assertNotNull(updatedCalculation.getClaimCalculationGrainQuantityDetail());
		
		Assert.assertEquals(createdCalculation.getTotalClaimAmount(), updatedCalculation.getTotalClaimAmount(), 0.00005);
		
		Assert.assertNotNull(updatedCalculation.getClaimCalculationGuid());
		Assert.assertNotNull(updatedCalculation.getClaimCalculationGrainQuantity().getClaimCalculationGrainQuantityGuid());
		Assert.assertNotNull(updatedCalculation.getClaimCalculationGrainQuantityDetail().getClaimCalculationGrainQuantityDetailGuid());
		
		assertGrainQuantity(expectedGrainQuantity, updatedCalculation.getClaimCalculationGrainQuantity());
		assertGrainQuantityDetail(expectedGrainQuantityDetail, updatedCalculation.getClaimCalculationGrainQuantityDetail());
		
		
		//Update calculation
		grainQty = updatedCalculation.getClaimCalculationGrainQuantity();
		grainQtyDetail = updatedCalculation.getClaimCalculationGrainQuantityDetail();
		
		//User Entered
		grainQtyDetail.setAssessedYield(2.5);
		grainQtyDetail.setDamagedAcres(25.5);
		grainQtyDetail.setSeededAcres(95.5);
		grainQtyDetail.setInspEarlyEstYield(30.0);
		grainQty.setReseedClaim(755.0);
		grainQty.setAdvancedClaim(200.0);
		updatedCalculation.setTotalClaimAmount(4000.0);

		expectedGrainQuantity = new ClaimCalculationGrainQuantity();
		expectedGrainQuantityDetail = new ClaimCalculationGrainQuantityDetail();
		
		createExpectedGrainQuantityCalculation(updatedCalculation, expectedGrainQuantity, expectedGrainQuantityDetail, null);

		updatedCalculation = service.updateClaimCalculation(updatedCalculation, null);

		Assert.assertNotNull(updatedCalculation);
		Assert.assertNotNull(updatedCalculation.getClaimCalculationGrainQuantity());
		Assert.assertNotNull(updatedCalculation.getClaimCalculationGrainQuantityDetail());

		assertGrainQuantity(expectedGrainQuantity, updatedCalculation.getClaimCalculationGrainQuantity());
		assertGrainQuantityDetail(expectedGrainQuantityDetail, updatedCalculation.getClaimCalculationGrainQuantityDetail());
		
		//Test to submit: Only possible if there are two calculations
		//Expect error
		try {
			updatedCalculation = service.updateClaimCalculation(updatedCalculation, ClaimsServiceEnums.UpdateTypes.SUBMIT.toString());
			Assert.fail("updateClaimCalculation should have thrown an exception because the calculation can't be submitted because there are two quantity products for this commodity on this policy. However, no calculation with the same version exists.");
		} catch ( CirrasClaimServiceException e) {
			// Expected.
			Assert.assertNotNull(e.getMessage());
			Assert.assertTrue(e.getMessage().contains("The calculation can't be submitted because there are two quantity products for this commodity on this policy. However, no calculation with the same version exists."));
		}
		
		//Add second calculation
		// Quantity Claim linked to 37195
		String claimNumber2 = "37196";       // Set to Open Grain Quantity Claim without a calculation.
		policyNumber = null;
        searchResults = service.getClaimList(topLevelEndpoints, claimNumber2, policyNumber, null, null, null, pageNumber, pageRowCount);
		
		//Values for Claim number 37195
		// From CIRRAS
		cropCommodityId = 21;
		commodityName = "FIELD PEA";
		isPedigreeInd = false;
		coverageDollars = 35868.0;
		deductibleLevel = 20;
		selectedInsurableValue = 180.0;
		acres = 237.0;
		probableYield = 1.051;
		productionGuarantee = 199.000;

		// From CUWS
		yieldToCount = 180.0;
		
		Assert.assertEquals(1, searchResults.getCollection().size());
		
		ClaimRsrc claimRsrc2 = searchResults.getCollection().get(0);
		//Only works if there is no calculation yet
		ClaimCalculationRsrc claimCalculationRsrc2 = service.getClaim(claimRsrc2);

		Assert.assertNotNull(claimCalculationRsrc2);
		Assert.assertNotNull(claimCalculationRsrc2.getClaimCalculationGrainQuantity());
		Assert.assertEquals(updatedCalculation.getClaimCalculationGrainQuantityGuid(), claimCalculationRsrc2.getClaimCalculationGrainQuantityGuid());
		Assert.assertNotNull(claimCalculationRsrc2.getClaimCalculationGrainQuantityDetail());
		Assert.assertEquals("OPEN", claimCalculationRsrc2.getClaimStatusCode());
		Assert.assertNotNull(claimCalculationRsrc2.getCalculationComment());
		Assert.assertEquals(cropCommodityId, claimCalculationRsrc2.getCropCommodityId());
		Assert.assertEquals(commodityName, claimCalculationRsrc2.getCommodityName());
		Assert.assertEquals(isPedigreeInd, claimCalculationRsrc2.getIsPedigreeInd());
		Assert.assertEquals(updatedCalculation.getClaimCalculationGuid(), claimCalculationRsrc2.getLinkedClaimCalculationGuid());
		Assert.assertEquals(updatedCalculation.getClaimNumber(), claimCalculationRsrc2.getLinkedClaimNumber());
		Assert.assertEquals(updatedCalculation.getClaimCalculationGuid(), claimCalculationRsrc2.getLatestLinkedClaimCalculationGuid());
		Assert.assertEquals(updatedCalculation.getCalculationVersion(), claimCalculationRsrc2.getLatestLinkedCalculationVersion());


		ClaimCalculationGrainQuantityDetail grainQtyDetail2 = claimCalculationRsrc2.getClaimCalculationGrainQuantityDetail();
		
		//User Entered
		grainQtyDetail2.setAssessedYield(20.0);
		grainQtyDetail2.setDamagedAcres(10.0);
		grainQtyDetail2.setSeededAcres(100.0);
		grainQtyDetail2.setInspEarlyEstYield(null);
		claimCalculationRsrc2.setTotalClaimAmount(5000.0);

		
		expectedGrainQuantity = new ClaimCalculationGrainQuantity();
		expectedGrainQuantityDetail = new ClaimCalculationGrainQuantityDetail();

		createExpectedGrainQuantityCalculation(claimCalculationRsrc2, expectedGrainQuantity, expectedGrainQuantityDetail, updatedCalculation.getClaimCalculationGrainQuantityDetail());
		
		//Create new calculation
		ClaimCalculationRsrc createdCalculation2 = service.createClaimCalculation(claimCalculationRsrc2);

		Assert.assertEquals(updatedCalculation.getClaimCalculationGuid(), createdCalculation2.getLinkedClaimCalculationGuid());
		Assert.assertEquals(updatedCalculation.getClaimCalculationGuid(), createdCalculation2.getLatestLinkedClaimCalculationGuid());
		Assert.assertEquals(updatedCalculation.getCalculationVersion(), createdCalculation2.getLatestLinkedCalculationVersion());
		Assert.assertEquals(updatedCalculation.getClaimCalculationGrainQuantityGuid(), createdCalculation2.getClaimCalculationGrainQuantityGuid());
		
		assertGrainQuantity(expectedGrainQuantity, createdCalculation2.getClaimCalculationGrainQuantity());
		assertGrainQuantityDetail(expectedGrainQuantityDetail, createdCalculation2.getClaimCalculationGrainQuantityDetail());
		

		//Try to save calculation if the claim amount is greater the coverage amount
		//Expect error
		//ClaimCalculationRsrc updatedCalculation2 = new ClaimCalculationRsrc();
		createdCalculation2.setTotalClaimAmount(createdCalculation2.getClaimCalculationGrainQuantityDetail().getCoverageValue() + 1.0);
		try {
			service.updateClaimCalculation(createdCalculation2, null);
			Assert.fail("updateClaimCalculation should have thrown an exception because Amount on line Z (claim amount pushed to CIRRAS) can't exceed the total coverage (line F) of the claim.");
		} catch ( CirrasClaimServiceException e) {
			// Expected.
			Assert.assertNotNull(e.getMessage());
			Assert.assertTrue(e.getMessage().contains("The calculation can't be saved because the Total Claim Amount is bigger than the Coverage Value."));
		}
		
		//Try to submit calculation if the claim amount sum of both calculations is greater than the calculated quantity loss
		//Expect error
		createdCalculation2.setTotalClaimAmount(5000.0);
		try {
			service.updateClaimCalculation(createdCalculation2, ClaimsServiceEnums.UpdateTypes.SUBMIT.toString());
			Assert.fail("updateClaimCalculation should have thrown an exception because Amount on line Z (claim amount pushed to CIRRAS) can't exceed the total coverage (line F) of the claim.");
		} catch ( CirrasClaimServiceException e) {
			// Expected.
			Assert.assertNotNull(e.getMessage());
			Assert.assertTrue(e.getMessage().contains("The calculation can't be submitted because the sum of the Total Claim Amount has to be equal to the calculated Quantity Loss Claim."));
		}
		
		//Try to delete calculations with doDeleteLinkedCalculations parameter set to false
		try {
			service.deleteClaimCalculation(createdCalculation2, false);
			Assert.fail("deleteClaimCalculation should have thrown an exception because parameter doDeleteLinkedCalculations is set to false");
		} catch ( CirrasClaimServiceException e) {
			// Expected.
		}

		// Check linked fields
		ClaimCalculationListRsrc claimCalculations = service.getClaimCalculations(topLevelEndpoints, claimNumber1, null, null, null, null, null, null, "claimNumber", "ASC", pageNumber, pageRowCount);
		ClaimCalculationRsrc fetchedCalc1 = service.getClaimCalculation(claimCalculations.getCollection().get(0), false);

		claimCalculations = service.getClaimCalculations(topLevelEndpoints, claimNumber2, null, null, null, null, null, null, "claimNumber", "ASC", pageNumber, pageRowCount);
		ClaimCalculationRsrc fetchedCalc2 = service.getClaimCalculation(claimCalculations.getCollection().get(0), false);

		Assert.assertEquals(updatedCalculation.getClaimCalculationGuid(), fetchedCalc1.getClaimCalculationGuid());
		Assert.assertEquals(createdCalculation2.getClaimCalculationGuid(), fetchedCalc2.getClaimCalculationGuid());

		Assert.assertEquals(fetchedCalc2.getClaimCalculationGuid(), fetchedCalc1.getLinkedClaimCalculationGuid());
		Assert.assertEquals(fetchedCalc2.getClaimCalculationGuid(), fetchedCalc1.getLatestLinkedClaimCalculationGuid());
		Assert.assertEquals(fetchedCalc2.getCalculationVersion(), fetchedCalc1.getLatestLinkedCalculationVersion());

		Assert.assertEquals(fetchedCalc1.getClaimCalculationGuid(), fetchedCalc2.getLinkedClaimCalculationGuid());
		Assert.assertEquals(fetchedCalc1.getClaimCalculationGuid(), fetchedCalc2.getLatestLinkedClaimCalculationGuid());
		Assert.assertEquals(fetchedCalc1.getCalculationVersion(), fetchedCalc2.getLatestLinkedCalculationVersion());

		Assert.assertEquals(fetchedCalc1.getClaimCalculationGrainQuantityGuid(), fetchedCalc2.getClaimCalculationGrainQuantityGuid());
		
		//Delete calculations
		service.deleteClaimCalculation(createdCalculation2, true);

		//Check if both calculations are deleted
		claimCalculations = service.getClaimCalculations(topLevelEndpoints, claimNumber1, null, null, null, null, null, null, "claimNumber", "ASC", pageNumber, pageRowCount);
		Assert.assertEquals(0, claimCalculations.getCollection().size());

		claimCalculations = service.getClaimCalculations(topLevelEndpoints, claimNumber2, null, null, null, null, null, null, "claimNumber", "ASC", pageNumber, pageRowCount);
		Assert.assertEquals(0, claimCalculations.getCollection().size());

		logger.debug(">testGetInsertUpdateDeleteGrainQuantityClaim()");
	}


	private void assertGrainQuantity(ClaimCalculationGrainQuantity expected,
			ClaimCalculationGrainQuantity actual) {
		
		Assert.assertNotNull(actual.getClaimCalculationGrainQuantityGuid());
		Assert.assertEquals(expected.getTotalCoverageValue(), actual.getTotalCoverageValue(), 0.00005);
		Assert.assertEquals(expected.getProductionGuaranteeAmount(), actual.getProductionGuaranteeAmount(), 0.005);
		Assert.assertEquals(expected.getTotalYieldLossValue(), actual.getTotalYieldLossValue(), 0.00005);
		Assert.assertEquals(expected.getMaxClaimPayable(), actual.getMaxClaimPayable(), 0.00005);
		Assert.assertEquals(expected.getQuantityLossClaim(), actual.getQuantityLossClaim(), 0.05);

		//User Entered Fields
		if(expected.getReseedClaim() == null) {
			Assert.assertNull(actual.getReseedClaim());
		} else {
			Assert.assertEquals(expected.getReseedClaim(), actual.getReseedClaim(), 0.00005);
		}
		if(expected.getAdvancedClaim() == null) {
			Assert.assertNull(actual.getAdvancedClaim());
		} else {
			Assert.assertEquals(expected.getAdvancedClaim(), actual.getAdvancedClaim());
		}
	}

	private void assertGrainQuantityDetail(ClaimCalculationGrainQuantityDetail expected,
			ClaimCalculationGrainQuantityDetail actual) {

		Assert.assertNotNull(actual.getClaimCalculationGrainQuantityDetailGuid());
		Assert.assertNotNull(actual.getClaimCalculationGuid());
		Assert.assertEquals(expected.getInsuredAcres(), actual.getInsuredAcres());
		Assert.assertEquals(expected.getProbableYield(), actual.getProbableYield());
		Assert.assertEquals(expected.getDeductible(), actual.getDeductible());
		Assert.assertEquals(expected.getProductionGuaranteeWeight(), actual.getProductionGuaranteeWeight());
		Assert.assertEquals(expected.getInsurableValue(), actual.getInsurableValue());
		Assert.assertEquals(expected.getCoverageValue(), actual.getCoverageValue());
		Assert.assertEquals(expected.getTotalYieldToCount(), actual.getTotalYieldToCount());
		Assert.assertEquals(expected.getEarlyEstDeemedYieldValue(), actual.getEarlyEstDeemedYieldValue(), 0.00005);
		Assert.assertEquals(expected.getFiftyPercentProductionGuarantee(), actual.getFiftyPercentProductionGuarantee(), 0.00005);
		Assert.assertEquals(expected.getCalcEarlyEstYield(), actual.getCalcEarlyEstYield(), 0.00005);
		Assert.assertEquals(expected.getYieldValue(), actual.getYieldValue(), 0.00005);
		Assert.assertEquals(expected.getYieldValueWithEarlyEstDeemedYield(), actual.getYieldValueWithEarlyEstDeemedYield(), 0.00005);
		
		//User Entered Fields
		if(expected.getAssessedYield() == null) {
			Assert.assertNull(actual.getAssessedYield());
		} else {
			Assert.assertEquals(expected.getAssessedYield(), actual.getAssessedYield());
		}
		if(expected.getDamagedAcres() == null) {
			Assert.assertNull(actual.getDamagedAcres());
		} else {
			Assert.assertEquals(expected.getDamagedAcres(), actual.getDamagedAcres());
		}
		if(expected.getSeededAcres() == null) {
			Assert.assertNull(actual.getSeededAcres());
		} else {
			Assert.assertEquals(expected.getSeededAcres(), actual.getSeededAcres());
		}
		if(expected.getInspEarlyEstYield() == null) {
			Assert.assertNull(actual.getInspEarlyEstYield());
		} else {
			Assert.assertEquals(expected.getInspEarlyEstYield(), actual.getInspEarlyEstYield());
		}
	}

	private void createExpectedGrainQuantityCalculation(
			ClaimCalculationRsrc claimCalculationRsrc,
			ClaimCalculationGrainQuantity expGrainQuantity,
			ClaimCalculationGrainQuantityDetail expGrainQuantityDetail,
			ClaimCalculationGrainQuantityDetail linkedGrainQtyDetail) {
		
		ClaimCalculationGrainQuantity grainQty = claimCalculationRsrc.getClaimCalculationGrainQuantity();
		ClaimCalculationGrainQuantityDetail grainQtyDetail = claimCalculationRsrc.getClaimCalculationGrainQuantityDetail();
		
		//Calculated
		//G SUM of coverage value
		Double linkedCoverageValue = linkedGrainQtyDetail != null ? linkedGrainQtyDetail.getCoverageValue() : 0.0;
		Double totalCoverageValue = grainQtyDetail.getCoverageValue() + linkedCoverageValue;
		//K Sum of pedigreed and non pedigreed of ( D - I ) x E
		Double linkedProductionGuaranteeAmount = 0.0;
		if( linkedGrainQtyDetail != null ) {
			linkedProductionGuaranteeAmount = calculateProductionGuarantee(
					linkedGrainQtyDetail.getProductionGuaranteeWeight(),
					linkedGrainQtyDetail.getAssessedYield(),
					linkedGrainQtyDetail.getInsurableValue());
		}
		Double productionGuaranteeAmount = calculateProductionGuarantee(
				grainQtyDetail.getProductionGuaranteeWeight(),
				grainQtyDetail.getAssessedYield(),
				grainQtyDetail.getInsurableValue())
				+ linkedProductionGuaranteeAmount;
		//O 50% of Production Guarantee (Tonnes) D
		Double fiftyPercentProductionGuarantee = grainQtyDetail.getProductionGuaranteeWeight() * 0.5;
		//P - O x ( M / N)
		Double calcEarlyEstYield = 0.0;
		if(grainQtyDetail.getSeededAcres() != null && grainQtyDetail.getSeededAcres() > 0) {
			calcEarlyEstYield = fiftyPercentProductionGuarantee * (notNull(grainQtyDetail.getDamagedAcres(), 0.0) / grainQtyDetail.getSeededAcres());
		}
		if(calcEarlyEstYield > 0) {
			calcEarlyEstYield = (double) Math.round(calcEarlyEstYield * 1000d) / 1000d;
		}
		
		//L - ( Q or P ) x E
		Double earlyEstablishment = grainQtyDetail.getInspEarlyEstYield() == null ? calcEarlyEstYield : grainQtyDetail.getInspEarlyEstYield();
		Double earlyEstDeemedYieldValue = earlyEstablishment * grainQtyDetail.getInsurableValue();
		//R - Total Yield Harvested and Appraised (H) * Insurable Value per Tonnes (E)
		Double yieldValue = notNull(grainQtyDetail.getTotalYieldToCount(), 0.0) * grainQtyDetail.getInsurableValue();
		//S - R + L
		Double yieldValueWithEarlyEstDeemedYield = notNull(yieldValue, 0.0) + notNull(earlyEstDeemedYieldValue, 0.0);
		//T - K - Sum of S
		Double linkedYieldValueWithEarlyEstDeemedYield = linkedGrainQtyDetail != null ? linkedGrainQtyDetail.getYieldValueWithEarlyEstDeemedYield() : 0.0;
		Double totalYieldLossValue = Math.max(0, productionGuaranteeAmount - notNull(yieldValueWithEarlyEstDeemedYield, 0.0) - linkedYieldValueWithEarlyEstDeemedYield);
		//V - G-U
		Double maxClaimPayable = Math.max(0, notNull(totalCoverageValue, 0.0) - notNull(grainQty.getReseedClaim(), 0.0));
		
		//Y - Lesser of Maximum Claim Payable (V) or Total Quantity Loss (W) - Less Advanced Claim(s) ( X )
		Double quantityLossClaim = Math.max(0, Math.min(maxClaimPayable, totalYieldLossValue) - notNull(grainQty.getAdvancedClaim(), 0.0));
		if(quantityLossClaim > 0) {
			//Round to two decimals
			quantityLossClaim = (double) Math.round(quantityLossClaim * 100d) / 100d;
		}
		
		expGrainQuantity.setClaimCalculationGrainQuantityGuid(grainQty.getClaimCalculationGrainQuantityGuid());
		expGrainQuantity.setTotalCoverageValue(totalCoverageValue); //Calculated
		expGrainQuantity.setProductionGuaranteeAmount(productionGuaranteeAmount); //Calculated
		expGrainQuantity.setTotalYieldLossValue(totalYieldLossValue); //Calculated
		expGrainQuantity.setReseedClaim(grainQty.getReseedClaim());
		expGrainQuantity.setMaxClaimPayable(maxClaimPayable); //Calculated
		expGrainQuantity.setAdvancedClaim(grainQty.getAdvancedClaim());
		expGrainQuantity.setQuantityLossClaim(quantityLossClaim); //Calculated

		expGrainQuantityDetail.setClaimCalculationGrainQuantityDetailGuid(grainQtyDetail.getClaimCalculationGrainQuantityDetailGuid());
		expGrainQuantityDetail.setClaimCalculationGuid(grainQtyDetail.getClaimCalculationGuid());
		expGrainQuantityDetail.setInsuredAcres(grainQtyDetail.getInsuredAcres());
		expGrainQuantityDetail.setProbableYield(grainQtyDetail.getProbableYield());
		expGrainQuantityDetail.setDeductible(grainQtyDetail.getDeductible());
		expGrainQuantityDetail.setProductionGuaranteeWeight(grainQtyDetail.getProductionGuaranteeWeight());
		expGrainQuantityDetail.setInsurableValue(grainQtyDetail.getInsurableValue());
		expGrainQuantityDetail.setCoverageValue(grainQtyDetail.getCoverageValue());
		expGrainQuantityDetail.setTotalYieldToCount(grainQtyDetail.getTotalYieldToCount());
		expGrainQuantityDetail.setAssessedYield(grainQtyDetail.getAssessedYield());
		expGrainQuantityDetail.setEarlyEstDeemedYieldValue(earlyEstDeemedYieldValue); //Calculated
		expGrainQuantityDetail.setDamagedAcres(grainQtyDetail.getDamagedAcres());
		expGrainQuantityDetail.setSeededAcres(grainQtyDetail.getSeededAcres());
		expGrainQuantityDetail.setFiftyPercentProductionGuarantee(fiftyPercentProductionGuarantee); //Calculated
		expGrainQuantityDetail.setCalcEarlyEstYield(calcEarlyEstYield); //Calculated
		expGrainQuantityDetail.setInspEarlyEstYield(grainQtyDetail.getInspEarlyEstYield());
		expGrainQuantityDetail.setYieldValue(yieldValue); //Calculated
		expGrainQuantityDetail.setYieldValueWithEarlyEstDeemedYield(yieldValueWithEarlyEstDeemedYield); //Calculated
		
	}
	
	private Double calculateProductionGuarantee(Double productionGuaranteeWeight, Double assessedYield, Double insurableValue) {
		//( D - I ) x E
		Double calcProdGuaranteeWeight = notNull(productionGuaranteeWeight, 0.0) - notNull(assessedYield, 0.0);
		return Math.max(0, calcProdGuaranteeWeight) * notNull(insurableValue, 0.0);
	}
	
	private Double notNull(Double value, Double defaultValue) {
		return (value == null) ? defaultValue : value;
	}
	
	@Test
	public void testGetInsertUpdateDeleteGrapesClaim() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testGetInsertUpdateDeleteGrapesClaim()");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
        String claimNumber = "28082";//25749 (claims with calculations); 28082 (claim without calculation)
		String policyNumber = null;//"401837-06";
		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		ClaimListRsrc searchResults = service.getClaimList(topLevelEndpoints, claimNumber, policyNumber, null, null, null, pageNumber, pageRowCount);
		
		
		if(searchResults.getCollection().size() > 0) {
		
			ClaimRsrc claimRsrc = searchResults.getCollection().get(0);
			//Only works if there is no calculation yet
			ClaimCalculationRsrc claimCalculationRsrc = service.getClaim(claimRsrc);
	
			Assert.assertNotNull(claimCalculationRsrc);
			
			claimCalculationRsrc.getClaimCalculationGrapes().setCoverageAmountAssessed(99000.5);
			claimCalculationRsrc.getClaimCalculationGrapes().setCoverageAssessedReason("test");
			
			//Create new calculation
			ClaimCalculationRsrc createdCalculation = service.createClaimCalculation(claimCalculationRsrc);

			Assert.assertNotNull(createdCalculation);
			Assert.assertNotNull(createdCalculation.getClaimCalculationGrapes());

			Assert.assertEquals("InsurableValueSelected", claimCalculationRsrc.getClaimCalculationGrapes().getInsurableValueSelected(), createdCalculation.getClaimCalculationGrapes().getInsurableValueSelected());
			Assert.assertEquals("InsurableValueHundredPercent", claimCalculationRsrc.getClaimCalculationGrapes().getInsurableValueHundredPercent(), createdCalculation.getClaimCalculationGrapes().getInsurableValueHundredPercent());
			Assert.assertEquals("CoverageAmount", claimCalculationRsrc.getClaimCalculationGrapes().getCoverageAmount(), createdCalculation.getClaimCalculationGrapes().getCoverageAmount());
			Assert.assertEquals("CoverageAmountAssessed", claimCalculationRsrc.getClaimCalculationGrapes().getCoverageAmountAssessed(), createdCalculation.getClaimCalculationGrapes().getCoverageAmountAssessed());
			Assert.assertEquals("CoverageAssessedReason", claimCalculationRsrc.getClaimCalculationGrapes().getCoverageAssessedReason(), createdCalculation.getClaimCalculationGrapes().getCoverageAssessedReason());

			//update calculation
			createdCalculation.getClaimCalculationGrapes().setInsurableValueSelected(1.5);
			createdCalculation.getClaimCalculationGrapes().setInsurableValueHundredPercent(2.8230);
			createdCalculation.getClaimCalculationGrapes().setCoverageAmount((double)100001);
			createdCalculation.getClaimCalculationGrapes().setCoverageAmountAssessed(99001.5);
			createdCalculation.getClaimCalculationGrapes().setCoverageAssessedReason("test 2");
			
			ClaimCalculationRsrc updatedCalculation = service.updateClaimCalculation(createdCalculation, null);

			Assert.assertEquals("InsurableValueSelected", createdCalculation.getClaimCalculationGrapes().getInsurableValueSelected(), updatedCalculation.getClaimCalculationGrapes().getInsurableValueSelected());
			Assert.assertEquals("InsurableValueHundredPercent", createdCalculation.getClaimCalculationGrapes().getInsurableValueHundredPercent(), updatedCalculation.getClaimCalculationGrapes().getInsurableValueHundredPercent());
			Assert.assertEquals("CoverageAmount", createdCalculation.getClaimCalculationGrapes().getCoverageAmount(), updatedCalculation.getClaimCalculationGrapes().getCoverageAmount());
			Assert.assertEquals("CoverageAmountAssessed", createdCalculation.getClaimCalculationGrapes().getCoverageAmountAssessed(), updatedCalculation.getClaimCalculationGrapes().getCoverageAmountAssessed());
			Assert.assertEquals("CoverageAssessedReason", createdCalculation.getClaimCalculationGrapes().getCoverageAssessedReason(), updatedCalculation.getClaimCalculationGrapes().getCoverageAssessedReason());

			//Delete calculation
			service.deleteClaimCalculation(updatedCalculation, true);
		}

		logger.debug(">testGetInsertUpdateDeleteGrapesClaim()");
	}
	
	private void deleteClaimCalculation(String claimNumber) throws CirrasClaimServiceException {
		
		if(claimNumber != null) {
			//Check if the claimNumber with calculation version already exists and delete it if it does
			ClaimCalculationListRsrc searchResults = service.getClaimCalculations(topLevelEndpoints, claimNumber, null, null, null, null, null, null, null, null, pageNumber, pageRowCount);
			if(searchResults.getTotalRowCount() > 0) {
				ClaimCalculationRsrc tempRsrc = new ClaimCalculationRsrc();
				for (int i = 0; i < searchResults.getTotalRowCount(); i++) {
					tempRsrc = searchResults.getCollection().get(i);
					
					if(tempRsrc.getCalculationVersion() == 1) {
						ClaimCalculationRsrc calculationToDel = service.getClaimCalculation(tempRsrc, false);
						//Delete claim
						service.deleteClaimCalculation(calculationToDel, true);
						break;
					}
				}
			}
		}
	}
	
	@Test
	public void testGetInsertUpdateDeleteBerriesClaim() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testGetInsertUpdateDeleteBerriesClaim");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
        String claimNumber = "28088";// Needs to be manually set to a real, valid BERRIES Quantity claim in CIRRAS db.
		String policyNumber = null;
		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);

		//delete calculation if it exists
		deleteClaimCalculation(claimNumber.toString());
		
		ClaimListRsrc searchResults = service.getClaimList(topLevelEndpoints, claimNumber, policyNumber, null, null, null, pageNumber, pageRowCount);
		
		
		if(searchResults.getCollection().size() > 0) {
		
			ClaimRsrc claimRsrc = searchResults.getCollection().get(0);
			
			//Only works if there is no calculation yet
			ClaimCalculationRsrc claimCalculationRsrc = service.getClaim(claimRsrc);
	
			Assert.assertNotNull(claimCalculationRsrc);
			
			//Set values
			claimCalculationRsrc.setPrimaryPerilCode("DROUGHT");
			

			claimCalculationRsrc.getClaimCalculationBerries().setConfirmedAcres(claimCalculationRsrc.getClaimCalculationBerries().getDeclaredAcres());
			claimCalculationRsrc.getClaimCalculationBerries().setHarvestedYield((double)10000);
			claimCalculationRsrc.getClaimCalculationBerries().setAppraisedYield((double)1000);
			claimCalculationRsrc.getClaimCalculationBerries().setAbandonedYield((double)500);
			claimCalculationRsrc.getClaimCalculationBerries().setTotalYieldFromAdjuster((double)20000);
			claimCalculationRsrc.getClaimCalculationBerries().setYieldAssessment((double)400);
			
			//Create new calculation
			ClaimCalculationRsrc createdCalculation = service.createClaimCalculation(claimCalculationRsrc);
			
			Assert.assertNotNull(createdCalculation);
			Assert.assertNotNull(createdCalculation.getClaimCalculationBerries());

			Assert.assertEquals("TotalProbableYield", claimCalculationRsrc.getClaimCalculationBerries().getTotalProbableYield(), createdCalculation.getClaimCalculationBerries().getTotalProbableYield());
			Assert.assertEquals("DeductibleLevel", claimCalculationRsrc.getClaimCalculationBerries().getDeductibleLevel(), createdCalculation.getClaimCalculationBerries().getDeductibleLevel());
			Assert.assertEquals("ProductionGuarantee", claimCalculationRsrc.getClaimCalculationBerries().getProductionGuarantee(), createdCalculation.getClaimCalculationBerries().getProductionGuarantee());
			Assert.assertEquals("DeclaredAcres", claimCalculationRsrc.getClaimCalculationBerries().getDeclaredAcres(), createdCalculation.getClaimCalculationBerries().getDeclaredAcres());
			Assert.assertEquals("InsurableValueSelected", claimCalculationRsrc.getClaimCalculationBerries().getInsurableValueSelected(), createdCalculation.getClaimCalculationBerries().getInsurableValueSelected());
			Assert.assertEquals("InsurableValueHundredPercent", claimCalculationRsrc.getClaimCalculationBerries().getInsurableValueHundredPercent(), createdCalculation.getClaimCalculationBerries().getInsurableValueHundredPercent());
			Assert.assertEquals("ConfirmedAcres", claimCalculationRsrc.getClaimCalculationBerries().getConfirmedAcres(), createdCalculation.getClaimCalculationBerries().getConfirmedAcres());
			Assert.assertEquals("HarvestedYield", claimCalculationRsrc.getClaimCalculationBerries().getHarvestedYield(), createdCalculation.getClaimCalculationBerries().getHarvestedYield());
			Assert.assertEquals("AppraisedYield", claimCalculationRsrc.getClaimCalculationBerries().getAppraisedYield(), createdCalculation.getClaimCalculationBerries().getAppraisedYield());
			Assert.assertEquals("AbandonedYield", claimCalculationRsrc.getClaimCalculationBerries().getAbandonedYield(), createdCalculation.getClaimCalculationBerries().getAbandonedYield());
			Assert.assertEquals("TotalYieldFromAdjuster", claimCalculationRsrc.getClaimCalculationBerries().getTotalYieldFromAdjuster(), createdCalculation.getClaimCalculationBerries().getTotalYieldFromAdjuster());
			Assert.assertEquals("YieldAssessment", claimCalculationRsrc.getClaimCalculationBerries().getYieldAssessment(), createdCalculation.getClaimCalculationBerries().getYieldAssessment());

			
			//update calculation
			createdCalculation.setPrimaryPerilCode("FIRE");
			createdCalculation.getClaimCalculationBerries().setTotalProbableYield(createdCalculation.getClaimCalculationBerries().getTotalProbableYield() +1);
			createdCalculation.getClaimCalculationBerries().setDeductibleLevel(createdCalculation.getClaimCalculationBerries().getDeductibleLevel() +1);
			createdCalculation.getClaimCalculationBerries().setProductionGuarantee(createdCalculation.getClaimCalculationBerries().getProductionGuarantee() +1);
			createdCalculation.getClaimCalculationBerries().setDeclaredAcres(createdCalculation.getClaimCalculationBerries().getDeclaredAcres());
			createdCalculation.getClaimCalculationBerries().setInsurableValueHundredPercent(1.51);
			createdCalculation.getClaimCalculationBerries().setInsurableValueSelected(1.21);
			createdCalculation.getClaimCalculationBerries().setConfirmedAcres(claimCalculationRsrc.getClaimCalculationBerries().getDeclaredAcres() +1);
			createdCalculation.getClaimCalculationBerries().setHarvestedYield((double)10001);
			createdCalculation.getClaimCalculationBerries().setAppraisedYield((double)1001);
			createdCalculation.getClaimCalculationBerries().setAbandonedYield((double)501);
			createdCalculation.getClaimCalculationBerries().setTotalYieldFromAdjuster((double)20001);
			createdCalculation.getClaimCalculationBerries().setYieldAssessment((double)500);

			ClaimCalculationRsrc updatedCalculation = service.updateClaimCalculation(createdCalculation, null);
			
			Assert.assertEquals("TotalProbableYield", createdCalculation.getClaimCalculationBerries().getTotalProbableYield(), updatedCalculation.getClaimCalculationBerries().getTotalProbableYield());
			Assert.assertEquals("DeductibleLevel", createdCalculation.getClaimCalculationBerries().getDeductibleLevel(), updatedCalculation.getClaimCalculationBerries().getDeductibleLevel());
			Assert.assertEquals("ProductionGuarantee", createdCalculation.getClaimCalculationBerries().getProductionGuarantee(), updatedCalculation.getClaimCalculationBerries().getProductionGuarantee());
			Assert.assertEquals("DeclaredAcres", createdCalculation.getClaimCalculationBerries().getDeclaredAcres(), updatedCalculation.getClaimCalculationBerries().getDeclaredAcres());
			Assert.assertEquals("InsurableValueSelected", createdCalculation.getClaimCalculationBerries().getInsurableValueSelected(), updatedCalculation.getClaimCalculationBerries().getInsurableValueSelected());
			Assert.assertEquals("InsurableValueHundredPercent", createdCalculation.getClaimCalculationBerries().getInsurableValueHundredPercent(), updatedCalculation.getClaimCalculationBerries().getInsurableValueHundredPercent());
			Assert.assertEquals("ConfirmedAcres", createdCalculation.getClaimCalculationBerries().getConfirmedAcres(), updatedCalculation.getClaimCalculationBerries().getConfirmedAcres());
			Assert.assertEquals("HarvestedYield", createdCalculation.getClaimCalculationBerries().getHarvestedYield(), updatedCalculation.getClaimCalculationBerries().getHarvestedYield());
			Assert.assertEquals("AppraisedYield", createdCalculation.getClaimCalculationBerries().getAppraisedYield(), updatedCalculation.getClaimCalculationBerries().getAppraisedYield());
			Assert.assertEquals("AbandonedYield", createdCalculation.getClaimCalculationBerries().getAbandonedYield(), updatedCalculation.getClaimCalculationBerries().getAbandonedYield());
			Assert.assertEquals("TotalYieldFromAdjuster", createdCalculation.getClaimCalculationBerries().getTotalYieldFromAdjuster(), updatedCalculation.getClaimCalculationBerries().getTotalYieldFromAdjuster());
			Assert.assertEquals("YieldAssessment", createdCalculation.getClaimCalculationBerries().getYieldAssessment(), updatedCalculation.getClaimCalculationBerries().getYieldAssessment());

			//Delete calculation
			service.deleteClaimCalculation(updatedCalculation, true);
		}

		logger.debug(">testGetInsertUpdateDeleteBerriesClaim");
	}
	
	@Test
	public void testGetInsertUpdateDeletePlantUnitsClaim() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testGetInsertUpdateDeletePlantUnitsClaim");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
        String claimNumber = "28201";//28201 (claim without calculation)
		String policyNumber = null;
		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);

		//delete calculation if it exists
		deleteClaimCalculation(claimNumber.toString());
		
		ClaimListRsrc searchResults = service.getClaimList(topLevelEndpoints, claimNumber, policyNumber, null, null, null, pageNumber, pageRowCount);
		
		
		if(searchResults.getCollection().size() > 0) {
		
			ClaimRsrc claimRsrc = searchResults.getCollection().get(0);
			
			//Only works if there is no calculation yet
			ClaimCalculationRsrc claimCalculationRsrc = service.getClaim(claimRsrc);
	
			Assert.assertNotNull(claimCalculationRsrc);
			
			//Set values
			claimCalculationRsrc.setPrimaryPerilCode("DROUGHT");

			claimCalculationRsrc.getClaimCalculationPlantUnits().setLessAdjustmentReason("Less Adjustment");
			claimCalculationRsrc.getClaimCalculationPlantUnits().setLessAdjustmentUnits(500);
			claimCalculationRsrc.getClaimCalculationPlantUnits().setDamagedUnits(7000);
			claimCalculationRsrc.getClaimCalculationPlantUnits().setLessAssessmentReason("Less Assessment");
			claimCalculationRsrc.getClaimCalculationPlantUnits().setLessAssessmentUnits(1000);
			
			String userId = "JUNIT_TEST";
			
			//Create new calculation
			ClaimCalculationRsrc createdCalculation = service.createClaimCalculation(claimCalculationRsrc);

			
			//update calculation
			//Updated from policy
			createdCalculation.getClaimCalculationPlantUnits().setInsurableValue(claimCalculationRsrc.getClaimCalculationPlantUnits().getInsurableValue() + 0.5);
			createdCalculation.getClaimCalculationPlantUnits().setInsuredUnits(claimCalculationRsrc.getClaimCalculationPlantUnits().getInsuredUnits() - 1000);
			createdCalculation.getClaimCalculationPlantUnits().setDeductibleLevel(10);
			//Manually entered
			createdCalculation.getClaimCalculationPlantUnits().setLessAdjustmentReason("Less Adjustment 2");
			createdCalculation.getClaimCalculationPlantUnits().setLessAdjustmentUnits(499);
			createdCalculation.getClaimCalculationPlantUnits().setDamagedUnits(6999);
			createdCalculation.getClaimCalculationPlantUnits().setLessAssessmentReason("Less Assessment 2");
			createdCalculation.getClaimCalculationPlantUnits().setLessAssessmentUnits(999);
			
			ClaimCalculationRsrc updatedCalculation = service.updateClaimCalculation(createdCalculation, null);

			//Delete calculation
			service.deleteClaimCalculation(updatedCalculation, true);
		}

		logger.debug(">testGetInsertUpdateDeletePlantUnitsClaim");
	}	
	
	@Test
	public void testGetInsertUpdateDeletePlantAcresClaim() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testGetInsertUpdateDeletePlantAcresClaim");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
        String claimNumber = "28080";// Needs to be manually set to a real, valid Grapes Quantity claim in CIRRAS db.
		String policyNumber = null;
		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);

		//delete calculation if it exists
		deleteClaimCalculation(claimNumber.toString());
		
		ClaimListRsrc searchResults = service.getClaimList(topLevelEndpoints, claimNumber, policyNumber, null, null, null, pageNumber, pageRowCount);
		
		
		if(searchResults.getCollection().size() > 0) {
		
			ClaimRsrc claimRsrc = searchResults.getCollection().get(0);
			
			//Only works if there is no calculation yet
			ClaimCalculationRsrc claimCalculationRsrc = service.getClaim(claimRsrc);
	
			Assert.assertNotNull(claimCalculationRsrc);
			
			//Set values
			claimCalculationRsrc.setPrimaryPerilCode("DROUGHT");

			claimCalculationRsrc.getClaimCalculationPlantAcres().setConfirmedAcres(1.1);
			claimCalculationRsrc.getClaimCalculationPlantAcres().setDamagedAcres(0.6);
			claimCalculationRsrc.getClaimCalculationPlantAcres().setLessAssessmentAmount((double)500);
			claimCalculationRsrc.getClaimCalculationPlantAcres().setLessAssessmentReason("Less Assessment");
			
			String userId = "JUNIT_TEST";
			
			//Create new calculation
			ClaimCalculationRsrc createdCalculation = service.createClaimCalculation(claimCalculationRsrc);

			
			//update calculation
			createdCalculation.setPrimaryPerilCode("FIRE");
			//Updated from policy
			createdCalculation.getClaimCalculationPlantAcres().setInsurableValue((double)3500);
			createdCalculation.getClaimCalculationPlantAcres().setDeclaredAcres((double)8);
			createdCalculation.getClaimCalculationPlantAcres().setDeductibleLevel(5);
			//Manually entered
			createdCalculation.getClaimCalculationPlantAcres().setConfirmedAcres(8.9);
			createdCalculation.getClaimCalculationPlantAcres().setDamagedAcres(1.79);
			createdCalculation.getClaimCalculationPlantAcres().setLessAssessmentAmount((double)5000);
			createdCalculation.getClaimCalculationPlantAcres().setLessAssessmentReason("Less Assessment 2");
			
			ClaimCalculationRsrc updatedCalculation = service.updateClaimCalculation(createdCalculation, null);

			//Delete calculation
			service.deleteClaimCalculation(updatedCalculation, true);
		}

		logger.debug(">testGetInsertUpdateDeletePlantAcresClaim");
	}	
	
}
