package ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.SyncClaimRsrc;
import ca.bc.gov.mal.cirras.claims.service.api.v1.util.ClaimsServiceEnums;
import ca.bc.gov.mal.cirras.claims.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class SyncClaimEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(SyncClaimEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.SEARCH_CLAIMS,
		Scopes.GET_CALCULATION,
		Scopes.CREATE_CALCULATION,
		Scopes.UPDATE_CALCULATION,
		Scopes.DELETE_CLAIM,
		Scopes.SEARCH_CALCULATIONS,
		Scopes.REFRESH_DATA,
		Scopes.PRINT_CALCULATION,
		Scopes.GET_SYNC_CLAIM,
		Scopes.CREATE_SYNC_CLAIM,
		Scopes.UPDATE_SYNC_CLAIM,
		Scopes.DELETE_SYNC_CLAIM
	};
	
	private CirrasClaimService service;
	private EndpointsRsrc topLevelEndpoints;
	private Integer testColId = 1;
	
	@Before
	public void prepareTests() throws CirrasClaimServiceException, Oauth2ClientException{
		service = getService(SCOPES);
		topLevelEndpoints = service.getTopLevelEndpoints();

		deleteClaim();

	}

	@After 
	public void cleanUp() throws CirrasClaimServiceException {
		deleteClaim();
	}
	
	private void deleteClaim() throws CirrasClaimServiceException {
		
		SyncClaimRsrc syncClaimRsrc = service.getSyncClaim(topLevelEndpoints, testColId.toString());
		if (syncClaimRsrc != null) {
			service.deleteSyncClaim(syncClaimRsrc);
		}
	}

	@Test
	public void testGetSyncClaim() throws CirrasClaimServiceException, Oauth2ClientException {
		logger.debug("<testGetSyncClaim");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
        String colId = "1038282";
		SyncClaimRsrc syncClaimRsrc = service.getSyncClaim(topLevelEndpoints, colId);
		Assert.assertNotNull(syncClaimRsrc);
		

		logger.debug(">testGetSyncClaim");
	}
	
//	@Test
//	public void deleteSyncClaim() throws CirrasClaimServiceException {
//		deleteClaim();
//	}
	
	@Test
	public void testCreateUpdateDeleteNoCalculationSyncClaim() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testCreateUpdateDeleteNoCalculationSyncClaim");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		Date transactionDate = new Date();
		Date createTransactionDate = addSeconds(transactionDate, -1);
		
		SyncClaimRsrc newRsrc = new SyncClaimRsrc();
		newRsrc.setColId(testColId);
		newRsrc.setClaimNumber(123456);
		newRsrc.setClaimStatusCode("OPEN");
		newRsrc.setIppId(2);
		newRsrc.setCommodityCoverageCode("CQNT");
		newRsrc.setCropCommodityId(3);
		newRsrc.setIplId(4);
		newRsrc.setPolicyNumber("987654-21");
		newRsrc.setInsurancePlanId(5);
		newRsrc.setCropYear(2021);
		newRsrc.setContractId(6);
		newRsrc.setIgId(7);
		newRsrc.setGrowerName("Test Grower");
		newRsrc.setClaimDataSyncTransDate(createTransactionDate);
		newRsrc.setPolicyDataSyncTransDate(createTransactionDate);
		newRsrc.setGrowerDataSyncTransDate(createTransactionDate);
		newRsrc.setSubmittedByDate(new Date());
		newRsrc.setSubmittedByName("Submitter");
		newRsrc.setSubmittedByUserid("Sub");
		newRsrc.setRecommendedByDate(new Date());
		newRsrc.setRecommendedByName("Recommender");
		newRsrc.setRecommendedByUserid("Rec");
		newRsrc.setApprovedByDate(new Date());
		newRsrc.setApprovedByName("Approver");
		newRsrc.setApprovedByUserid("Apr");
		newRsrc.setHasChequeReqInd(true);

		
		SyncClaimRsrc syncClaimRsrc = service.createSyncClaim(newRsrc);
		Assert.assertNotNull(syncClaimRsrc);
		Assert.assertEquals("colId not set", syncClaimRsrc.getColId(), testColId);
		
		syncClaimRsrc = service.getSyncClaim(topLevelEndpoints, testColId.toString());
		Assert.assertNotNull(syncClaimRsrc);

		Assert.assertEquals(newRsrc.getHasChequeReqInd(), syncClaimRsrc.getHasChequeReqInd());
		
		
		syncClaimRsrc.setClaimStatusCode("IN PROGRESS");
		syncClaimRsrc.setClaimDataSyncTransDate(addSeconds(createTransactionDate, -1));

		//No update expected because the transaction date is earlier than on create
		SyncClaimRsrc updatedClaimRsrc = service.updateSyncClaim(syncClaimRsrc);
		Assert.assertNotNull(updatedClaimRsrc);
		Assert.assertEquals("Status not updated", "OPEN", updatedClaimRsrc.getClaimStatusCode());

		syncClaimRsrc.setClaimDataSyncTransDate(transactionDate);
		syncClaimRsrc.setSubmittedByDate(null);
		syncClaimRsrc.setSubmittedByName("Submitter2");
		syncClaimRsrc.setSubmittedByUserid("Sub2");
		syncClaimRsrc.setRecommendedByDate(null);
		syncClaimRsrc.setRecommendedByName("Recommender2");
		syncClaimRsrc.setRecommendedByUserid("Rec2");
		syncClaimRsrc.setApprovedByDate(null);
		syncClaimRsrc.setApprovedByName("Approver2");
		syncClaimRsrc.setApprovedByUserid("Apr2");
		syncClaimRsrc.setHasChequeReqInd(false);

		
		updatedClaimRsrc = service.updateSyncClaim(syncClaimRsrc);
		Assert.assertNotNull(updatedClaimRsrc);
		Assert.assertEquals("Status not updated", "IN PROGRESS", updatedClaimRsrc.getClaimStatusCode());
		Assert.assertEquals(syncClaimRsrc.getHasChequeReqInd(), updatedClaimRsrc.getHasChequeReqInd());
		
		logger.debug(">testCreateUpdateDeleteNoCalculationSyncClaim");
	}
	
	@Test
	public void testUpdateDeleteWithCalculationSyncClaim() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testUpdateDeleteWithCalculationSyncClaim");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		Date transactionDate = new Date();
		Integer colId = 1041132;
		Date submittedDate = addSeconds(transactionDate, -100);
		String submittedByName = "Submitter2";
		String submittedByUserid = "Sub2";
		Date recommendedDate = addSeconds(transactionDate, -90);
		String  recommendedByName = "Recommender2";
		String  recommendedByUserid = "Rec2";
		Date approvedDate = addSeconds(transactionDate, -80);
		String  approvedByName = "Approver2";
		String approvedByUserid = "Apr2";
		
		SyncClaimRsrc syncClaimRsrc = service.getSyncClaim(topLevelEndpoints, colId.toString());
		Assert.assertNotNull(syncClaimRsrc);

		syncClaimRsrc.setClaimStatusCode("OPEN");
		syncClaimRsrc.setSubmittedByDate(null);
		syncClaimRsrc.setSubmittedByName(null);
		syncClaimRsrc.setSubmittedByUserid(null);
		syncClaimRsrc.setRecommendedByDate(null);
		syncClaimRsrc.setRecommendedByName(null);
		syncClaimRsrc.setRecommendedByUserid(null);
		syncClaimRsrc.setApprovedByDate(null);
		syncClaimRsrc.setApprovedByName(null);
		syncClaimRsrc.setApprovedByUserid(null);
		syncClaimRsrc.setHasChequeReqInd(false);
		syncClaimRsrc.setClaimDataSyncTransDate(addSeconds(transactionDate, 1));

		//Reset to claim status Open
		SyncClaimRsrc openClaimRsrc = service.updateSyncClaim(syncClaimRsrc);
		Assert.assertNotNull(openClaimRsrc);
		Assert.assertEquals("Status not updated", ClaimsServiceEnums.ClaimStatusCodes.Open.getClaimStatusCode(), openClaimRsrc.getClaimStatusCode());
		Assert.assertEquals(syncClaimRsrc.getHasChequeReqInd(), openClaimRsrc.getHasChequeReqInd());

		
		
		openClaimRsrc.setClaimDataSyncTransDate(transactionDate);
		openClaimRsrc.setClaimStatusCode(ClaimsServiceEnums.ClaimStatusCodes.InProgress.getClaimStatusCode());
		openClaimRsrc.setSubmittedByDate(submittedDate);
		openClaimRsrc.setSubmittedByName(submittedByName);
		openClaimRsrc.setSubmittedByUserid(submittedByUserid);

		//No update expected because the transaction date is earlier than the previous one
		SyncClaimRsrc stillOpenClaimRsrc = service.updateSyncClaim(openClaimRsrc);
		Assert.assertNotNull(stillOpenClaimRsrc);
		Assert.assertEquals("Status not updated", ClaimsServiceEnums.ClaimStatusCodes.Open.getClaimStatusCode(), stillOpenClaimRsrc.getClaimStatusCode());


		//IN PROGRESS
		stillOpenClaimRsrc.setClaimDataSyncTransDate(addSeconds(syncClaimRsrc.getClaimDataSyncTransDate(), 1));
		stillOpenClaimRsrc.setClaimStatusCode(ClaimsServiceEnums.ClaimStatusCodes.InProgress.getClaimStatusCode());
		stillOpenClaimRsrc.setSubmittedByDate(submittedDate);
		stillOpenClaimRsrc.setSubmittedByName(submittedByName);
		stillOpenClaimRsrc.setSubmittedByUserid(submittedByUserid);
		
		//Update expected because the transaction date is the same as the previous one
		SyncClaimRsrc inProgressClaimRsrc = service.updateSyncClaim(stillOpenClaimRsrc);
		Assert.assertNotNull(inProgressClaimRsrc);
		Assert.assertEquals("Status not updated", ClaimsServiceEnums.ClaimStatusCodes.InProgress.getClaimStatusCode(), inProgressClaimRsrc.getClaimStatusCode());
		//Assert.assertEquals("SubmittedByDate not updated", submittedDate, inProgressClaimRsrc.getSubmittedByDate());
		//Assert.assertTrue("SubmittedByDate not updated", equals("SubmittedByDate", submittedDate, inProgressClaimRsrc.getSubmittedByDate()));
		Assert.assertEquals("SubmittedByName not updated", submittedByName, inProgressClaimRsrc.getSubmittedByName());
		Assert.assertEquals("SubmittedByUserid not updated", submittedByUserid, inProgressClaimRsrc.getSubmittedByUserid());
		
		
//		result = result && cirrasServiceHelper.equals("RecommendedByDate", syncClaim.getRecommendedByDate(), claimDto.getRecommendedByDate());
	//	result = result && cirrasServiceHelper.equals("ApprovedByDate", syncClaim.getApprovedByDate(), claimDto.getApprovedByDate());

		
		//RECOMMENDED
		inProgressClaimRsrc.setClaimDataSyncTransDate(addSeconds(inProgressClaimRsrc.getClaimDataSyncTransDate(), 1));
		inProgressClaimRsrc.setClaimStatusCode(ClaimsServiceEnums.ClaimStatusCodes.Pending.getClaimStatusCode());
		inProgressClaimRsrc.setRecommendedByDate(recommendedDate);
		inProgressClaimRsrc.setRecommendedByName(recommendedByName);
		inProgressClaimRsrc.setRecommendedByUserid(recommendedByUserid);
		
		//Update expected because the transaction date is the same as the previous one
		SyncClaimRsrc recommendedClaimRsrc = service.updateSyncClaim(inProgressClaimRsrc);
		Assert.assertNotNull(recommendedClaimRsrc);
		Assert.assertEquals("Status not updated", ClaimsServiceEnums.ClaimStatusCodes.Pending.getClaimStatusCode(), recommendedClaimRsrc.getClaimStatusCode());
		//Assert.assertEquals("RecommendedByDate not updated", recommendedDate, recommendedClaimRsrc.getRecommendedByDate());
		Assert.assertEquals("RecommendedByName not updated", recommendedByName, recommendedClaimRsrc.getRecommendedByName());
		Assert.assertEquals("RecommendedByUserid not updated", recommendedByUserid, recommendedClaimRsrc.getRecommendedByUserid());

		//APPROVED
		recommendedClaimRsrc.setClaimDataSyncTransDate(addSeconds(recommendedClaimRsrc.getClaimDataSyncTransDate(), 1));
		recommendedClaimRsrc.setClaimStatusCode(ClaimsServiceEnums.ClaimStatusCodes.Approved.getClaimStatusCode());
		recommendedClaimRsrc.setApprovedByDate(approvedDate);
		recommendedClaimRsrc.setApprovedByName(approvedByName);
		recommendedClaimRsrc.setApprovedByUserid(approvedByUserid);
		
		//Update expected because the transaction date is the same as the previous one
		SyncClaimRsrc approvedClaimRsrc = service.updateSyncClaim(recommendedClaimRsrc);
		Assert.assertNotNull(approvedClaimRsrc);
		Assert.assertEquals("Status not updated", ClaimsServiceEnums.ClaimStatusCodes.Approved.getClaimStatusCode(), approvedClaimRsrc.getClaimStatusCode());
		//Assert.assertEquals("ApprovedByDate not updated", approvedDate, approvedClaimRsrc.getApprovedByDate());
		Assert.assertEquals("ApprovedByName not updated", approvedByName, approvedClaimRsrc.getApprovedByName());
		Assert.assertEquals("ApprovedByUserid not updated", approvedByUserid, approvedClaimRsrc.getApprovedByUserid());

		//ROLLBACK
		approvedClaimRsrc.setClaimStatusCode(ClaimsServiceEnums.ClaimStatusCodes.Open.getClaimStatusCode());
		approvedClaimRsrc.setSubmittedByDate(null);
		approvedClaimRsrc.setSubmittedByName(null);
		approvedClaimRsrc.setSubmittedByUserid(null);
		approvedClaimRsrc.setRecommendedByDate(null);
		approvedClaimRsrc.setRecommendedByName(null);
		approvedClaimRsrc.setRecommendedByUserid(null);
		approvedClaimRsrc.setApprovedByDate(null);
		approvedClaimRsrc.setApprovedByName(null);
		approvedClaimRsrc.setApprovedByUserid(null);
		approvedClaimRsrc.setClaimDataSyncTransDate(addSeconds(approvedClaimRsrc.getClaimDataSyncTransDate(), 1));

		//Reset to claim status Open
		SyncClaimRsrc rolledBackClaimRsrc = service.updateSyncClaim(approvedClaimRsrc);
		Assert.assertNotNull(rolledBackClaimRsrc);
		Assert.assertEquals("Status not updated", ClaimsServiceEnums.ClaimStatusCodes.Open.getClaimStatusCode(), rolledBackClaimRsrc.getClaimStatusCode());
		Assert.assertEquals("SubmittedByDate not updated", null, rolledBackClaimRsrc.getSubmittedByDate());
		Assert.assertEquals("SubmittedByName not updated", null, rolledBackClaimRsrc.getSubmittedByName());
		Assert.assertEquals("SubmittedByUserid not updated", null, rolledBackClaimRsrc.getSubmittedByUserid());
		Assert.assertEquals("RecommendedByDate not updated", null, rolledBackClaimRsrc.getRecommendedByDate());
		Assert.assertEquals("RecommendedByName not updated", null, rolledBackClaimRsrc.getRecommendedByName());
		Assert.assertEquals("RecommendedByUserid not updated", null, rolledBackClaimRsrc.getRecommendedByUserid());
		Assert.assertEquals("ApprovedByDate not updated", null, rolledBackClaimRsrc.getApprovedByDate());
		Assert.assertEquals("ApprovedByName not updated", null, rolledBackClaimRsrc.getApprovedByName());
		Assert.assertEquals("ApprovedByUserid not updated", null, rolledBackClaimRsrc.getApprovedByUserid());

		// Back to Approved
		rolledBackClaimRsrc.setClaimStatusCode(ClaimsServiceEnums.ClaimStatusCodes.Approved.getClaimStatusCode());
		rolledBackClaimRsrc.setSubmittedByDate(submittedDate);
		rolledBackClaimRsrc.setSubmittedByName(submittedByName);
		rolledBackClaimRsrc.setSubmittedByUserid(submittedByUserid);
		rolledBackClaimRsrc.setRecommendedByDate(recommendedDate);
		rolledBackClaimRsrc.setRecommendedByName(recommendedByName);
		rolledBackClaimRsrc.setRecommendedByUserid(recommendedByUserid);
		rolledBackClaimRsrc.setApprovedByDate(approvedDate);
		rolledBackClaimRsrc.setApprovedByName(approvedByName);
		rolledBackClaimRsrc.setApprovedByUserid(approvedByUserid);
		rolledBackClaimRsrc.setClaimDataSyncTransDate(addSeconds(rolledBackClaimRsrc.getClaimDataSyncTransDate(), 1));

		SyncClaimRsrc approvedAgainClaimRsrc = service.updateSyncClaim(rolledBackClaimRsrc);
		Assert.assertNotNull(approvedAgainClaimRsrc);
		Assert.assertEquals("Status not updated", ClaimsServiceEnums.ClaimStatusCodes.Approved.getClaimStatusCode(), approvedAgainClaimRsrc.getClaimStatusCode());
		Assert.assertEquals("SubmittedByName not updated", rolledBackClaimRsrc.getSubmittedByName(), approvedAgainClaimRsrc.getSubmittedByName());
		Assert.assertEquals("SubmittedByUserid not updated", rolledBackClaimRsrc.getSubmittedByUserid(), approvedAgainClaimRsrc.getSubmittedByUserid());
		Assert.assertEquals("RecommendedByName not updated", rolledBackClaimRsrc.getRecommendedByName(), approvedAgainClaimRsrc.getRecommendedByName());
		Assert.assertEquals("RecommendedByUserid not updated", rolledBackClaimRsrc.getRecommendedByUserid(), approvedAgainClaimRsrc.getRecommendedByUserid());
		Assert.assertEquals("ApprovedByName not updated", rolledBackClaimRsrc.getApprovedByName(), approvedAgainClaimRsrc.getApprovedByName());
		Assert.assertEquals("ApprovedByUserid not updated", rolledBackClaimRsrc.getApprovedByUserid(), approvedAgainClaimRsrc.getApprovedByUserid());
		Assert.assertEquals(Boolean.FALSE, approvedAgainClaimRsrc.getHasChequeReqInd());

		// Amended.
		approvedAgainClaimRsrc.setClaimStatusCode(ClaimsServiceEnums.ClaimStatusCodes.InProgress.getClaimStatusCode());
		approvedAgainClaimRsrc.setRecommendedByDate(null);
		approvedAgainClaimRsrc.setRecommendedByName(null);
		approvedAgainClaimRsrc.setRecommendedByUserid(null);
		approvedAgainClaimRsrc.setApprovedByDate(null);
		approvedAgainClaimRsrc.setApprovedByName(null);
		approvedAgainClaimRsrc.setApprovedByUserid(null);
		approvedAgainClaimRsrc.setHasChequeReqInd(true);
		approvedAgainClaimRsrc.setClaimDataSyncTransDate(addSeconds(approvedAgainClaimRsrc.getClaimDataSyncTransDate(), 1));

		SyncClaimRsrc amendedClaimRsrc = service.updateSyncClaim(approvedAgainClaimRsrc);
		Assert.assertNotNull(amendedClaimRsrc);
		Assert.assertEquals("Status not updated", ClaimsServiceEnums.ClaimStatusCodes.InProgress.getClaimStatusCode(), amendedClaimRsrc.getClaimStatusCode());
		Assert.assertEquals("SubmittedByName not updated", approvedAgainClaimRsrc.getSubmittedByName(), amendedClaimRsrc.getSubmittedByName());
		Assert.assertEquals("SubmittedByUserid not updated", approvedAgainClaimRsrc.getSubmittedByUserid(), amendedClaimRsrc.getSubmittedByUserid());
		Assert.assertEquals("RecommendedByName not updated", approvedAgainClaimRsrc.getRecommendedByName(), amendedClaimRsrc.getRecommendedByName());
		Assert.assertEquals("RecommendedByUserid not updated", approvedAgainClaimRsrc.getRecommendedByUserid(), amendedClaimRsrc.getRecommendedByUserid());
		Assert.assertEquals("ApprovedByName not updated", approvedAgainClaimRsrc.getApprovedByName(), amendedClaimRsrc.getApprovedByName());
		Assert.assertEquals("ApprovedByUserid not updated", approvedAgainClaimRsrc.getApprovedByUserid(), amendedClaimRsrc.getApprovedByUserid());
		Assert.assertEquals(Boolean.TRUE, amendedClaimRsrc.getHasChequeReqInd());

		//Reset back to Open.
		amendedClaimRsrc.setClaimStatusCode(ClaimsServiceEnums.ClaimStatusCodes.Open.getClaimStatusCode());
		amendedClaimRsrc.setSubmittedByDate(null);
		amendedClaimRsrc.setSubmittedByName(null);
		amendedClaimRsrc.setSubmittedByUserid(null);
		amendedClaimRsrc.setRecommendedByDate(null);
		amendedClaimRsrc.setRecommendedByName(null);
		amendedClaimRsrc.setRecommendedByUserid(null);
		amendedClaimRsrc.setApprovedByDate(null);
		amendedClaimRsrc.setApprovedByName(null);
		amendedClaimRsrc.setApprovedByUserid(null);
		amendedClaimRsrc.setHasChequeReqInd(false);
		amendedClaimRsrc.setClaimDataSyncTransDate(addSeconds(amendedClaimRsrc.getClaimDataSyncTransDate(), 1));

		//Reset to claim status Open
		SyncClaimRsrc openAgainClaimRsrc = service.updateSyncClaim(amendedClaimRsrc);
		Assert.assertNotNull(openAgainClaimRsrc);
		Assert.assertEquals("Status not updated", ClaimsServiceEnums.ClaimStatusCodes.Open.getClaimStatusCode(), openAgainClaimRsrc.getClaimStatusCode());
		Assert.assertEquals("SubmittedByDate not updated", null, openAgainClaimRsrc.getSubmittedByDate());
		Assert.assertEquals("SubmittedByName not updated", null, openAgainClaimRsrc.getSubmittedByName());
		Assert.assertEquals("SubmittedByUserid not updated", null, openAgainClaimRsrc.getSubmittedByUserid());
		Assert.assertEquals("RecommendedByDate not updated", null, openAgainClaimRsrc.getRecommendedByDate());
		Assert.assertEquals("RecommendedByName not updated", null, openAgainClaimRsrc.getRecommendedByName());
		Assert.assertEquals("RecommendedByUserid not updated", null, openAgainClaimRsrc.getRecommendedByUserid());
		Assert.assertEquals("ApprovedByDate not updated", null, openAgainClaimRsrc.getApprovedByDate());
		Assert.assertEquals("ApprovedByName not updated", null, openAgainClaimRsrc.getApprovedByName());
		Assert.assertEquals("ApprovedByUserid not updated", null, openAgainClaimRsrc.getApprovedByUserid());
		Assert.assertEquals(Boolean.FALSE, openAgainClaimRsrc.getHasChequeReqInd());
		
		logger.debug(">testUpdateDeleteWithCalculationSyncClaim");
	}
	
	@Test
	public void testTryUpdatingClaimThatDoesntExist() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testTryUpdatingClaimThatDoesntExist");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		Integer colId = 9999999;
				
		SyncClaimRsrc syncClaimRsrc = null;
		syncClaimRsrc = service.getSyncClaim(topLevelEndpoints, colId.toString());
		Assert.assertNull(syncClaimRsrc);
		
		//Update needs a resource therefore test fails if syncClaimRsrc is not null
		
		logger.debug(">testTryUpdatingClaimThatDoesntExist");
	}
	
	//@Test
	public void testDeleteClaimAndCalculations() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testTryUpdatingClaimThatDoesntExist");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		Integer colId = 1038508;
				
		SyncClaimRsrc syncClaimRsrc = service.getSyncClaim(topLevelEndpoints, colId.toString());
		Assert.assertNotNull(syncClaimRsrc);
		
		service.deleteSyncClaim(syncClaimRsrc);

		logger.debug(">testTryUpdatingClaimThatDoesntExist");
	}
	
	
	
	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}
	
	//test to get an understanding how date.compareTo works
	//@Test
	public void testDateComparison() {
		Date transactionDate = new Date();
		Date date1 = addSeconds(transactionDate, -1);
		Date date2 = addSeconds(transactionDate, -1);
		
		Integer dateComparson = date1.compareTo(date2);
		
		logger.debug("dateComparson1: " + dateComparson.toString());
		
		date1 = addSeconds(date1, 1);
		dateComparson = date1.compareTo(date2);
		logger.debug("dateComparson2: " + dateComparson.toString());

		date1 = addSeconds(date1, -70);
		dateComparson = date1.compareTo(date2);
		logger.debug("dateComparson3: " + dateComparson.toString());

	}
	
	//Compares two date values and returns true if both are the same or both are NULL
	public boolean equals(String propertyName, Date v1, Date v2) {
		boolean result = false;
		
		if(v1==null&&v2==null) {
			
			result = true;
		} else if(v1!=null&&v2!=null) {
			
			result = v1.compareTo(v2) == 0;
		} else {
			
			result = false;
		}
		
		if(!result&&logger!=null) {
			logger.info(propertyName+" is dirty. old:"+v2+" new:"+v1);
		}
		
		return result;
	}	
}
