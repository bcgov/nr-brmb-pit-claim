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
import ca.bc.gov.mal.cirras.claims.data.resources.SyncCoveragePerilRsrc;
import ca.bc.gov.mal.cirras.claims.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;
import ca.bc.gov.mal.cirras.policies.model.v1.PoliciesEventTypes;


public class SyncCoveragePerilEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(SyncCoveragePerilEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.CREATE_SYNC_CLAIM,
		Scopes.UPDATE_SYNC_CLAIM,
		Scopes.DELETE_SYNC_CLAIM
	};
	
	private CirrasClaimService service;
	private EndpointsRsrc topLevelEndpoints;
	private Integer coveragePerilId = 12345678;
	private Integer cropCommodityId = 1;
	private String perilCode = "SNOW";
	private String coverageCode = "CQNT";
	
	@Before
	public void prepareTests() throws CirrasClaimServiceException, Oauth2ClientException, NotFoundDaoException, DaoException{
		service = getService(SCOPES);
		topLevelEndpoints = service.getTopLevelEndpoints();

		deleteCoveragePeril();

	}

	@After 
	public void cleanUp() throws CirrasClaimServiceException, NotFoundDaoException, DaoException {
		deleteCoveragePeril();
	}

	
	private void deleteCoveragePeril() throws NotFoundDaoException, DaoException, CirrasClaimServiceException{

		//Delete variety
		delete(coveragePerilId);
		
		//Delete Commodity
		delete(coveragePerilId);

	}
	
	private void delete(Integer coveragePerilId) throws NotFoundDaoException, DaoException, CirrasClaimServiceException{

		service.deleteSyncCoveragePeril(topLevelEndpoints, coveragePerilId.toString());
		
	}
	
	@Test
	public void testCreateUpdateDeleteCoveragePeril() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testCreateUpdateDeleteCoveragePeril");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		Date transactionDate = new Date();
		Date createTransactionDate = addSeconds(transactionDate, -1);

		//CREATE CODE
		SyncCoveragePerilRsrc resource = new SyncCoveragePerilRsrc();

		resource.setCoveragePerilId(coveragePerilId);
		resource.setCropCommodityId(cropCommodityId);
		resource.setPerilCode(perilCode);
		resource.setCommodityCoverageCode(coverageCode);
		resource.setIsActive(true);
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesEventTypes.CoveragePerilCreated);

		service.synchronizeCoveragePeril(resource);
		
		//INACTIVATE (expiry date set to now())
		resource.setIsActive(false);
		resource.setDataSyncTransDate(addSeconds(transactionDate, +1));
		resource.setTransactionType(PoliciesEventTypes.CoveragePerilUpdated);
		
		service.synchronizeCoveragePeril(resource);

		//ACTIVATE (expiry date set to max date and effective date to now())
		resource.setIsActive(true);
		resource.setDataSyncTransDate(addSeconds(transactionDate, +2));
		resource.setTransactionType(PoliciesEventTypes.CoveragePerilUpdated);
		
		service.synchronizeCoveragePeril(resource);
		
		//No update
		resource.setDataSyncTransDate(addSeconds(transactionDate, +3));
		service.synchronizeCoveragePeril(resource);
		

		//DELETE CODE (SET INACTIVE)
		resource.setDataSyncTransDate(addSeconds(transactionDate, +4));
		resource.setTransactionType(PoliciesEventTypes.CoveragePerilDeleted);

		service.synchronizeCoveragePeril(resource);

		//CLEAN UP: DELETE CODE
		delete(coveragePerilId);
		
		logger.debug(">testCreateUpdateDeleteCoveragePeril");
	}

	
	@Test
	public void testUpdateCoveragePerilWithoutRecordNoUpdate() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testUpdateCoveragePerilWithoutRecordNoUpdate");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		Date transactionDate = new Date();
		Date createTransactionDate = addSeconds(transactionDate, -1);

		//CREATE RECORD
		SyncCoveragePerilRsrc resource = new SyncCoveragePerilRsrc();

		resource.setCoveragePerilId(coveragePerilId);
		resource.setCropCommodityId(cropCommodityId);
		resource.setPerilCode(perilCode);
		resource.setCommodityCoverageCode(coverageCode);
		resource.setIsActive(false);
		resource.setDataSyncTransDate(createTransactionDate);

		
		//TRY TO DELETE A RECORD THAT DOESN'T EXIST (NO ERROR EXECTED)
		resource.setTransactionType(PoliciesEventTypes.CoveragePerilDeleted);
		service.synchronizeCoveragePeril(resource);

		//SHOULD RESULT IN AN INSERT
		resource.setTransactionType(PoliciesEventTypes.CoveragePerilUpdated);

		//Expect insert (should be detected)
		service.synchronizeCoveragePeril(resource);

		//UPDATE RECORD (No Update)
		resource.setIsActive(true);
		resource.setDataSyncTransDate(addSeconds(transactionDate, -1));
		resource.setTransactionType(PoliciesEventTypes.CoveragePerilUpdated);

		//NO UPDATE EXPECTED BECAUSE TRANSACTION DATE IS EARLIER THAN STORED ONE
		service.synchronizeCoveragePeril(resource);
		
		//UPDATE CODE AND ACTIVATE --> USE CREATED TYPE
		resource.setDataSyncTransDate(addSeconds(transactionDate, 1));
		resource.setIsActive(true);
		resource.setTransactionType(PoliciesEventTypes.CoveragePerilCreated);

		//UPDATE EXPECTED BECAUSE RECORD EXISTS IT WILL UPDATE IT
		service.synchronizeCoveragePeril(resource);		
		
		//CLEAN UP: DELETE RECORD
		delete(coveragePerilId);
		
		logger.debug(">testUpdateCoveragePerilWithoutRecordNoUpdate");
	}
	
	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}
	

}
