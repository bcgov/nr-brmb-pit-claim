package ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints;

import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.claims.api.rest.client.v1.CirrasClaimService;
import ca.bc.gov.mal.cirras.claims.api.rest.client.v1.CirrasClaimServiceException;
import ca.bc.gov.mal.cirras.claims.api.rest.client.v1.ValidationException;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.EndpointsRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.SyncCodeRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;
import ca.bc.gov.mal.cirras.policies.model.v1.CodeTableTypes;
import ca.bc.gov.mal.cirras.policies.model.v1.PoliciesEventTypes;


public class SyncPerilCodeEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(SyncPerilCodeEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.CREATE_SYNC_CLAIM,
		Scopes.UPDATE_SYNC_CLAIM,
		Scopes.DELETE_SYNC_CLAIM
	};
	
	private CirrasClaimService service;
	private EndpointsRsrc topLevelEndpoints;
	private String testPerilCode = "TEST_CODE"; 
	private String testDescription = "Test Peril"; 
	
	@Before
	public void prepareTests() throws CirrasClaimServiceException, Oauth2ClientException, NotFoundDaoException, DaoException{
		service = getService(SCOPES);
		topLevelEndpoints = service.getTopLevelEndpoints();

		deletePerilCode();

	}

	@After 
	public void cleanUp() throws CirrasClaimServiceException, NotFoundDaoException, DaoException {
		deletePerilCode();
	}

	
	private void deletePerilCode() throws NotFoundDaoException, DaoException, CirrasClaimServiceException{

		//Delete variety
		delete(testPerilCode);
		
		//Delete Commodity
		delete(testPerilCode);

	}
	
	private void delete(String perilCode) throws NotFoundDaoException, DaoException, CirrasClaimServiceException{

		service.deleteSyncCode(topLevelEndpoints, CodeTableTypes.PerilTypes, perilCode);
		
	}
	
	@Test
	public void testCreateUpdateDeletePerilCode() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testCreateUpdatePerilCode");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		Date transactionDate = new Date();
		Date createTransactionDate = addSeconds(transactionDate, -1);

		//CREATE CODE
		SyncCodeRsrc resource = new SyncCodeRsrc();
		
		resource.setCodeTableType(CodeTableTypes.PerilTypes);
		resource.setUniqueKeyString(testPerilCode);
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
		delete(testPerilCode);
		
		logger.debug(">testCreateUpdatePerilCode");
	}

	
	@Test
	public void testUpdatePerilCodeWithoutRecordNoUpdate() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testUpdatePerilCodeWithoutRecordNoUpdate");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		Date transactionDate = new Date();
		Date createTransactionDate = addSeconds(transactionDate, -1);

		//CREATE COMMODITY
		SyncCodeRsrc resource = new SyncCodeRsrc();

		resource.setCodeTableType(CodeTableTypes.PerilTypes);
		resource.setUniqueKeyString(testPerilCode);
		resource.setDescription(testDescription);
		resource.setIsActive(false);
		resource.setDataSyncTransDate(createTransactionDate);

		
		//TRY TO DELETE A Code THAT DOESN'T EXIST (NO ERROR EXECTED)
		resource.setTransactionType(PoliciesEventTypes.CodeDeleted);
		service.synchronizeCode(resource);

		//SHOULD RESULT IN AN INSERT
		resource.setTransactionType(PoliciesEventTypes.CodeUpdated);

		//Expect insert (should be detected)
		service.synchronizeCode(resource);

		//UPDATE COMMODITY
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
		
		//CLEAN UP: DELETE COMMODITY
		delete(testPerilCode);
		
		logger.debug(">testUpdatePerilCodeWithoutRecordNoUpdate");
	}
	
	@Test
	public void testCreateActivateInactivatePerilCode() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testInactivatePerilCode");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		Date transactionDate = new Date();
		Date createTransactionDate = addSeconds(transactionDate, -1);

		//CREATE CODE
		SyncCodeRsrc resource = new SyncCodeRsrc();
		
		resource.setCodeTableType(CodeTableTypes.PerilTypes);
		resource.setUniqueKeyString(testPerilCode);
		resource.setDescription(testDescription);
		resource.setIsActive(true);
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesEventTypes.CodeCreated);

		service.synchronizeCode(resource);

		//INACTIVATE (expiry date set to now())
		resource.setIsActive(false);
		resource.setDataSyncTransDate(addSeconds(transactionDate, +1));
		resource.setTransactionType(PoliciesEventTypes.CodeUpdated);
		
		service.synchronizeCode(resource);

		//ACTIVATE (set effective date set to now() and expiry date to maxdate)
		resource.setIsActive(true);
		resource.setDataSyncTransDate(addSeconds(transactionDate, +2));
		resource.setTransactionType(PoliciesEventTypes.CodeUpdated);
		
		service.synchronizeCode(resource);

		//CLEAN UP: DELETE CODE
		delete(testPerilCode);
		
		logger.debug(">testInactivatePerilCode");
	}
	
	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}
	

}
