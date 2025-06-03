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

		// TEST 1: Quantity Claim without linked Product
        String claimNumber = "37185";       // Set to Open Grain Quantity Claim without a calculation.
        currentClaimNumber = claimNumber;
		String policyNumber = null;
		Integer pageNumber = 0;
		Integer pageRowCount = 100;
		ClaimListRsrc searchResults = service.getClaimList(topLevelEndpoints, claimNumber, policyNumber, null, null, null, pageNumber, pageRowCount);
		
		//Values for Claim number 37185
		// From CIRRAS
		Integer cropCommodityId = 24;
		String commodityName = "OAT";
		Boolean isPedigreeInd = false;
		Double coverageDollars = 45908.0;
		Integer deductibleLevel = 20;
		Double selectedInsurableValue = 154.6875;
		Double acres = 270.0;
		Double probableYield = 1.3740;
		Double productionGuarantee = 297.000;
		

		// From CUWS
		Double assessedYield = 56088.0;
		Double yieldToCount = 24762.0;
		String calculationComment = "Verified Yield Summary - OAT:\nTest Oat Verified Yield Summary Comment 4 5 6"
				+ "\n\nTest Oat Verified Yield Summary Comment 1 2 3"
				+ "\n\nVerified Yield Amendment Rationale - OAT:\nTest Oat Appraisal Rationale G H I"
				+ "\n\nVerified Yield Amendment Rationale - OAT:\nTest Oat Assessment Rationale A B C";

		// Linked Product/Claim
		Integer linkedClaimNumber = null;
		Integer linkedProductId = null;
		
		Assert.assertEquals(1, searchResults.getCollection().size());
		
		ClaimRsrc claimRsrc = searchResults.getCollection().get(0);
		//Only works if there is no calculation yet
		ClaimCalculationRsrc claimCalculationRsrc = service.getClaim(claimRsrc);

		Assert.assertNotNull(claimCalculationRsrc);
		Assert.assertNotNull(claimCalculationRsrc.getClaimCalculationGrainQuantity());
		Assert.assertNotNull(claimCalculationRsrc.getClaimCalculationGrainQuantityDetail());
		Assert.assertEquals("OPEN", claimCalculationRsrc.getClaimStatusCode());
		Assert.assertEquals(calculationComment, claimCalculationRsrc.getCalculationComment());
		Assert.assertEquals(null, claimCalculationRsrc.getClaimCalculationGrainQuantityGuid());
		Assert.assertEquals(cropCommodityId, claimCalculationRsrc.getCropCommodityId());
		Assert.assertEquals(commodityName, claimCalculationRsrc.getCommodityName());
		Assert.assertEquals(isPedigreeInd, claimCalculationRsrc.getIsPedigreeInd());
		Assert.assertEquals(null, claimCalculationRsrc.getLinkedClaimCalculationGuid());
		Assert.assertEquals(linkedClaimNumber, claimCalculationRsrc.getLinkedClaimNumber());
		Assert.assertEquals(linkedProductId, claimCalculationRsrc.getLinkedProductId());
		
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
		Assert.assertEquals(assessedYield, grainQtyDetail.getAssessedYield());
		Assert.assertEquals(yieldToCount, grainQtyDetail.getTotalYieldToCount());
		
		// User Entered
		Assert.assertEquals(null, grainQtyDetail.getDamagedAcres());
		Assert.assertEquals(null, grainQtyDetail.getEarlyEstDeemedYieldValue());
		Assert.assertEquals(null, grainQtyDetail.getInspEarlyEstYield());
		Assert.assertEquals(null, grainQtyDetail.getSeededAcres());
		
		// Calculated
		Assert.assertEquals(null, grainQtyDetail.getCalcEarlyEstYield());
		Assert.assertEquals(null, grainQtyDetail.getFiftyPercentProductionGuarantee());
		Assert.assertEquals(null, grainQtyDetail.getYieldValue());
		Assert.assertEquals(null, grainQtyDetail.getYieldValueWithEarlyEstDeemedYield());
		
		// TEST 2: Quantity Claim with linked product
        claimNumber = "37195";       // Set to Open Grain Quantity Claim without a calculation.
        currentClaimNumber = claimNumber;
		searchResults = service.getClaimList(topLevelEndpoints, claimNumber, policyNumber, null, null, null, pageNumber, pageRowCount);
		
		//Values for Claim number 37195
		// From CIRRAS
		cropCommodityId = 22;
		commodityName = "FIELD PEA - PEDIGREED";
		isPedigreeInd = true;
		coverageDollars = 48513.0;
		deductibleLevel = 30;
		selectedInsurableValue = 246.8571;
		acres = 250.0;
		probableYield = 1.1230;
		productionGuarantee = 197.000;
		

		// From CUWS
		assessedYield = 431235.0;
		yieldToCount = 0.0;
		calculationComment = "Verified Yield Summary - FIELD PEA:\nTest Field Pea Verified Yield Summary Comment 2 2 2"
				+ "\n\nVerified Yield Summary - FIELD PEA - Pedigreed:\nTest Field Pea Pedigreed Verified Yield Summary Comment 1 1 1"
				+ "\n\nVerified Yield Amendment Rationale - FIELD PEA:\nTest Field Pea Appraisal Rationale A A A"
				+ "\n\nVerified Yield Amendment Rationale - FIELD PEA - Pedigreed:\nTest Field Pea Pedigreed Rationale B B B";

		// Linked Product/Claim
		linkedClaimNumber = 37196;
		linkedProductId = 1251643;
		
		Assert.assertEquals(1, searchResults.getCollection().size());
		
		claimRsrc = searchResults.getCollection().get(0);
		//Only works if there is no calculation yet
		claimCalculationRsrc = service.getClaim(claimRsrc);

		Assert.assertNotNull(claimCalculationRsrc);
		Assert.assertNotNull(claimCalculationRsrc.getClaimCalculationGrainQuantity());
		Assert.assertNotNull(claimCalculationRsrc.getClaimCalculationGrainQuantityDetail());
		Assert.assertEquals("OPEN", claimCalculationRsrc.getClaimStatusCode());
		Assert.assertEquals(calculationComment, claimCalculationRsrc.getCalculationComment());
		Assert.assertEquals(null, claimCalculationRsrc.getClaimCalculationGrainQuantityGuid());
		Assert.assertEquals(cropCommodityId, claimCalculationRsrc.getCropCommodityId());
		Assert.assertEquals(commodityName, claimCalculationRsrc.getCommodityName());
		Assert.assertEquals(isPedigreeInd, claimCalculationRsrc.getIsPedigreeInd());
		Assert.assertEquals(null, claimCalculationRsrc.getLinkedClaimCalculationGuid());
		Assert.assertEquals(linkedClaimNumber, claimCalculationRsrc.getLinkedClaimNumber());
		Assert.assertEquals(linkedProductId, claimCalculationRsrc.getLinkedProductId());
		
		grainQty = claimCalculationRsrc.getClaimCalculationGrainQuantity();
		grainQtyDetail = claimCalculationRsrc.getClaimCalculationGrainQuantityDetail();

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
		Assert.assertEquals(assessedYield, grainQtyDetail.getAssessedYield());
		Assert.assertEquals(yieldToCount, grainQtyDetail.getTotalYieldToCount());
		
		// User Entered
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
/* TODO: Implemneted in future ticket.
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
*/
		logger.debug(">testGetInsertUpdateDeleteGrainQuantityClaim()");
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
			service.deleteClaimCalculation(updatedCalculation);
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
						service.deleteClaimCalculation(calculationToDel);
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
			service.deleteClaimCalculation(updatedCalculation);
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
			service.deleteClaimCalculation(updatedCalculation);
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
			service.deleteClaimCalculation(updatedCalculation);
		}

		logger.debug(">testGetInsertUpdateDeletePlantAcresClaim");
	}	
	
}
