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
