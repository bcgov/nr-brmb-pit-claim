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
import ca.bc.gov.mal.cirras.policies.model.v1.PoliciesEventTypes;
import ca.bc.gov.mal.cirras.claims.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class SyncClaimRelatedDataEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(SyncClaimRelatedDataEndpointTest.class);


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
	private Integer testIplId = 4;
	private Integer originalIgId = 7;
	private Integer updatedIgId = 8;
	private String originalGrowerName = "Test Grower";
	private String updatedGrowerName = "Updated Grower";
	
	
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
	public void testUpdatePolicyData() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testUpdatePolicyData");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		Date transactionDate = new Date();
		
		//Create new claim record
		createClaim(transactionDate);
		
		Date policyTransactionDate = addSeconds(transactionDate, 1);
		
		SyncClaimRsrc syncClaim = new SyncClaimRsrc();
		
		syncClaim.setTransactionType(PoliciesEventTypes.PolicyUpdated);
		syncClaim.setPolicyDataSyncTransDate(policyTransactionDate);
		syncClaim.setIgId(updatedIgId);
		syncClaim.setGrowerName(updatedGrowerName);
		syncClaim.setIplId(testIplId);
		
		//Expect update
		service.updateSyncClaimRelatedData(syncClaim);

		syncClaim = service.getSyncClaim(topLevelEndpoints, testColId.toString());
		Assert.assertNotNull(syncClaim);

		Assert.assertEquals("GrowerId 1", updatedIgId, syncClaim.getIgId());
		Assert.assertEquals("Grower Name 1", updatedGrowerName, syncClaim.getGrowerName());

		
		//Expect NO update because ig id didn't change
		Date policyTransactionDate2 = addSeconds(policyTransactionDate, 1);
		syncClaim = new SyncClaimRsrc();
		syncClaim.setTransactionType(PoliciesEventTypes.PolicyUpdated);
		syncClaim.setPolicyDataSyncTransDate(policyTransactionDate2);
		syncClaim.setIgId(updatedIgId);
		syncClaim.setGrowerName(updatedGrowerName);
		syncClaim.setIplId(testIplId);

		service.updateSyncClaimRelatedData(syncClaim);

		syncClaim = service.getSyncClaim(topLevelEndpoints, testColId.toString());
		Assert.assertNotNull(syncClaim);
		Assert.assertEquals("GrowerId 2", updatedIgId, syncClaim.getIgId());
		Assert.assertEquals("Grower Name 2", updatedGrowerName, syncClaim.getGrowerName());
		
		//Expect NO update becaus the transaction date is before the latest update
		Date policyTransactionDate3 = addSeconds(policyTransactionDate, -1);
		syncClaim = new SyncClaimRsrc();
		syncClaim.setTransactionType(PoliciesEventTypes.PolicyUpdated);
		syncClaim.setPolicyDataSyncTransDate(policyTransactionDate3);
		syncClaim.setIgId(originalIgId);
		syncClaim.setGrowerName(originalGrowerName);
		syncClaim.setIplId(testIplId);

		service.updateSyncClaimRelatedData(syncClaim);

		syncClaim = service.getSyncClaim(topLevelEndpoints, testColId.toString());
		
		Assert.assertEquals("GrowerId 3", updatedIgId, syncClaim.getIgId());
		Assert.assertEquals("Grower Name 3", updatedGrowerName, syncClaim.getGrowerName());

		//Update expected
		Date policyTransactionDate4 = addSeconds(policyTransactionDate, 1);
		syncClaim = new SyncClaimRsrc();
		syncClaim.setTransactionType(PoliciesEventTypes.PolicyUpdated);
		syncClaim.setPolicyDataSyncTransDate(policyTransactionDate4);
		syncClaim.setIgId(originalIgId);
		syncClaim.setGrowerName(originalGrowerName);
		syncClaim.setIplId(testIplId);

		service.updateSyncClaimRelatedData(syncClaim);

		syncClaim = service.getSyncClaim(topLevelEndpoints, testColId.toString());

		Assert.assertEquals("GrowerId 4", originalIgId, syncClaim.getIgId());
		Assert.assertEquals("Grower Name 4", originalGrowerName, syncClaim.getGrowerName());

		service.deleteSyncClaim(syncClaim);

		
		logger.debug(">testUpdatePolicyData");
	}
	
	@Test
	public void testUpdateGrowerData() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException {
		logger.debug("<testUpdateGrowerData");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		Date transactionDate = new Date();
		
		//Create new claim record
		createClaim(transactionDate);
		
		Date growerTransactionDate = addSeconds(transactionDate, 1);
		
		SyncClaimRsrc syncClaim = new SyncClaimRsrc();
		
		syncClaim.setTransactionType(PoliciesEventTypes.GrowerUpdated);
		syncClaim.setGrowerDataSyncTransDate(growerTransactionDate);
		syncClaim.setIgId(originalIgId);
		syncClaim.setGrowerName(updatedGrowerName);
		
		//Expect update
		service.updateSyncClaimRelatedData(syncClaim);

		syncClaim = service.getSyncClaim(topLevelEndpoints, testColId.toString());
		Assert.assertNotNull(syncClaim);

		Assert.assertEquals("Grower Name 1", updatedGrowerName, syncClaim.getGrowerName());
		
		//Expect NO update because Growername didn't change
		Date growerTransactionDate2 = addSeconds(growerTransactionDate, 1);
		syncClaim = new SyncClaimRsrc();
		syncClaim.setTransactionType(PoliciesEventTypes.GrowerUpdated);
		syncClaim.setGrowerDataSyncTransDate(growerTransactionDate2);
		syncClaim.setIgId(originalIgId);
		syncClaim.setGrowerName(updatedGrowerName);

		service.updateSyncClaimRelatedData(syncClaim);

		syncClaim = service.getSyncClaim(topLevelEndpoints, testColId.toString());

		Assert.assertEquals("Grower Name 2", updatedGrowerName, syncClaim.getGrowerName());
				
		//Expect NO update becaus the transaction date is before the latest update
		Date growerTransactionDate3 = addSeconds(growerTransactionDate, -1);
		syncClaim = new SyncClaimRsrc();
		syncClaim.setTransactionType(PoliciesEventTypes.GrowerUpdated);
		syncClaim.setGrowerDataSyncTransDate(growerTransactionDate3);
		syncClaim.setIgId(originalIgId);
		syncClaim.setGrowerName(originalGrowerName);

		service.updateSyncClaimRelatedData(syncClaim);

		syncClaim = service.getSyncClaim(topLevelEndpoints, testColId.toString());
		
		Assert.assertEquals("Grower Name 3", updatedGrowerName, syncClaim.getGrowerName());

		//Update expected
		Date growerTransactionDate4 = addSeconds(growerTransactionDate, 1);
		syncClaim = new SyncClaimRsrc();
		syncClaim.setTransactionType(PoliciesEventTypes.GrowerUpdated);
		syncClaim.setGrowerDataSyncTransDate(growerTransactionDate4);
		syncClaim.setIgId(originalIgId);
		syncClaim.setGrowerName(originalGrowerName);

		service.updateSyncClaimRelatedData(syncClaim);

		syncClaim = service.getSyncClaim(topLevelEndpoints, testColId.toString());

		Assert.assertEquals("Grower Name 4", originalGrowerName, syncClaim.getGrowerName());
		
		service.deleteSyncClaim(syncClaim);

		
		logger.debug(">testUpdateGrowerData");
	}
	
	private SyncClaimRsrc createClaim(Date createTransactionDate)
			throws CirrasClaimServiceException, ValidationException {
		SyncClaimRsrc newRsrc = new SyncClaimRsrc();
		newRsrc.setColId(testColId);
		newRsrc.setClaimNumber(123456);
		newRsrc.setClaimStatusCode("OPEN");
		newRsrc.setIppId(2);
		newRsrc.setCommodityCoverageCode("CQNT");
		newRsrc.setCropCommodityId(3);
		newRsrc.setIplId(testIplId);
		newRsrc.setPolicyNumber("987654-21");
		newRsrc.setInsurancePlanId(5);
		newRsrc.setCropYear(2021);
		newRsrc.setContractId(6);
		newRsrc.setIgId(originalIgId);
		newRsrc.setGrowerName(originalGrowerName);
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
		newRsrc.setHasChequeReqInd(false);

		SyncClaimRsrc syncClaimRsrc = service.createSyncClaim(newRsrc);
		
		Assert.assertNotNull(syncClaimRsrc);

		return syncClaimRsrc;
	}
	
	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}
	
}
