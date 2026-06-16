package ca.bc.gov.mal.cirras.claims.controllers;

import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.claims.clients.CirrasClaimService;
import ca.bc.gov.mal.cirras.claims.clients.CirrasClaimServiceException;
import ca.bc.gov.mal.cirras.claims.clients.ValidationException;
import ca.bc.gov.mal.cirras.claims.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.claims.data.resources.EndpointsRsrc;
import ca.bc.gov.mal.cirras.claims.data.resources.SyncCodeRsrc;
import ca.bc.gov.mal.cirras.claims.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;
import ca.bc.gov.mal.cirras.policies.model.v1.CodeTableTypes;
import ca.bc.gov.mal.cirras.policies.model.v1.PoliciesEventTypes;


public class SyncCommodityCoverageCodeEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(SyncCommodityCoverageCodeEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.CREATE_SYNC_CLAIM,
		Scopes.UPDATE_SYNC_CLAIM,
		Scopes.DELETE_SYNC_CLAIM
	};
	
	private CirrasClaimService service;
	private EndpointsRsrc topLevelEndpoints;
	private String commodityCoverageCode = "TEST";
	private String testDescription = "TEST COVERAGE TYPE"; 
	private String codeTableType = CodeTableTypes.CropCoverageType;
	
	@Before
	public void prepareTests() throws CirrasClaimServiceException, Oauth2ClientException, NotFoundDaoException, DaoException{
		service = getService(SCOPES);
		topLevelEndpoints = service.getTopLevelEndpoints();

		delete(commodityCoverageCode);

	}

	@After 
	public void cleanUp() throws CirrasClaimServiceException, NotFoundDaoException, DaoException {
		delete(commodityCoverageCode);
	}
	
	private void delete(String commodityCoverageCode) throws NotFoundDaoException, DaoException, CirrasClaimServiceException{

		service.deleteSyncCode(topLevelEndpoints, codeTableType, commodityCoverageCode);
		
	}
	
	@Test
	public void testCreateUpdateDeleteCommodityCoverageCode() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testCreateUpdateCommodityCoverageCode");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		Date transactionDate = new Date();
		Date createTransactionDate = addSeconds(transactionDate, -1);

		//CREATE CODE
		SyncCodeRsrc resource = new SyncCodeRsrc();
		
		resource.setCodeTableType(codeTableType);
		resource.setUniqueKeyString(commodityCoverageCode);
		resource.setDescription(testDescription);
		resource.setIsActive(true);
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesEventTypes.CodeCreated);

		service.synchronizeCode(resource);

		//UPDATE CODE
		resource.setDescription(testDescription + " 2");
		resource.setDataSyncTransDate(addSeconds(transactionDate, +1));
		resource.setTransactionType(PoliciesEventTypes.CodeUpdated);
		
		service.synchronizeCode(resource);

		//DELETE CODE (SET INACTIVE)
		resource.setDataSyncTransDate(addSeconds(transactionDate, +2));
		resource.setTransactionType(PoliciesEventTypes.CodeDeleted);

		service.synchronizeCode(resource);

		//CLEAN UP: DELETE CODE
		delete(commodityCoverageCode);
		
		logger.debug(">testCreateUpdateCommodityCoverageCode");
	}

	
	@Test
	public void testUpdateCommodityCoverageCodeWithoutRecordNoUpdate() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testUpdateCommodityCoverageCodeWithoutRecordNoUpdate");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		Date transactionDate = new Date();
		Date createTransactionDate = addSeconds(transactionDate, -1);

		//CREATE CODE
		SyncCodeRsrc resource = new SyncCodeRsrc();

		resource.setCodeTableType(codeTableType);
		resource.setUniqueKeyString(commodityCoverageCode);
		resource.setDescription(testDescription);
		resource.setIsActive(false);
		resource.setDataSyncTransDate(createTransactionDate);

		
		//TRY TO DELETE A CODE THAT DOESN'T EXIST (NO ERROR EXECTED)
		resource.setTransactionType(PoliciesEventTypes.CodeDeleted);
		service.synchronizeCode(resource);

		//SHOULD RESULT IN AN INSERT
		resource.setTransactionType(PoliciesEventTypes.CodeUpdated);

		//Expect insert (should be detected)
		service.synchronizeCode(resource);

		//UPDATE CODE
		resource.setDescription(testDescription + " 2");
		resource.setDataSyncTransDate(addSeconds(transactionDate, -1));
		resource.setTransactionType(PoliciesEventTypes.CodeUpdated);

		//NO UPDATE EXPECTED BECAUSE TRANSACTION DATE IS EARLIER THAN STORED ONE
		service.synchronizeCode(resource);

		//UPDATE CODE AND ACTIVATE --> USE CREATED TYPE
		resource.setDataSyncTransDate(addSeconds(transactionDate, 1));
		resource.setIsActive(true);
		resource.setTransactionType(PoliciesEventTypes.CodeCreated);

		//UPDATE EXPECTED BECAUSE RECORD EXISTS IT WILL UPDATE IT
		service.synchronizeCode(resource);

		//CLEAN UP: DELETE CODE
		delete(commodityCoverageCode);
		
		logger.debug(">testUpdateCommodityCoverageCodeWithoutRecordNoUpdate");
	}
	
	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}
	

}
