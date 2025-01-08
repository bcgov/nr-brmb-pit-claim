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
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.SyncCommodityVarietyRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;
import ca.bc.gov.mal.cirras.policies.model.v1.PoliciesEventTypes;


public class SyncCommodityVarietyEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(SyncCommodityVarietyEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.CREATE_SYNC_CLAIM,
		Scopes.UPDATE_SYNC_CLAIM,
		Scopes.DELETE_SYNC_CLAIM
	};
	
	private CirrasClaimService service;
	private EndpointsRsrc topLevelEndpoints;
	private Integer testCommodityId = 99999;
	private Integer testVarietyId = 77777;
	
	@Before
	public void prepareTests() throws CirrasClaimServiceException, Oauth2ClientException, NotFoundDaoException, DaoException{
		service = getService(SCOPES);
		topLevelEndpoints = service.getTopLevelEndpoints();

		deleteCropVarietyCommodity();

	}

	@After 
	public void cleanUp() throws CirrasClaimServiceException, NotFoundDaoException, DaoException {
		deleteCropVarietyCommodity();
	}

	
	private void deleteCropVarietyCommodity() throws NotFoundDaoException, DaoException, CirrasClaimServiceException{

		//Delete variety
		delete(testVarietyId);
		
		//Delete Commodity
		delete(testCommodityId);

	}
	
	private void delete(Integer cropId) throws NotFoundDaoException, DaoException, CirrasClaimServiceException{

		service.deleteSyncCommodityVariety(topLevelEndpoints, cropId.toString());
		
	}
	
	//
	//COMMODITY TESTS
	//
	@Test
	public void testCreateUpdateDeleteCommodity() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testCreateUpdateDeleteCommodity");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		Date transactionDate = new Date();
		Date createTransactionDate = addSeconds(transactionDate, -1);

		//CREATE COMMODITY
		SyncCommodityVarietyRsrc resource = new SyncCommodityVarietyRsrc();

		resource.setCropId(testCommodityId);
		resource.setCropName("TEST COMMODITY");
		resource.setParentCropId(null);
		resource.setIsProductInsurable(true);
		resource.setIsInventoryCrop(true);
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesEventTypes.CommodityVarietyCreated);

		service.synchronizeCommodityVariety(resource);

		//UPDATE COMMODITY
		resource.setCropName("TEST COMMODITY 2");
		resource.setDataSyncTransDate(addSeconds(transactionDate, +1));
		resource.setTransactionType(PoliciesEventTypes.CommodityVarietyUpdated);
		
		service.synchronizeCommodityVariety(resource);

		//DELETE COMMODITY (SET INACTIVE)
		resource.setDataSyncTransDate(addSeconds(transactionDate, +2));
		resource.setTransactionType(PoliciesEventTypes.CommodityVarietyDeleted);

		service.synchronizeCommodityVariety(resource);

		//CLEAN UP: DELETE COMMODITY
		delete(testCommodityId);
		
		logger.debug(">testCreateUpdateDeleteCommodity");
	}
	
	@Test
	public void testCommodityNotInventoriableInsurable() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testCommodityNotInventoriableInsurable");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		Date transactionDate = new Date();
		Date createTransactionDate = addSeconds(transactionDate, -1);

		//CREATE COMMODITY
		SyncCommodityVarietyRsrc resource = new SyncCommodityVarietyRsrc();

		resource.setCropId(testCommodityId);
		resource.setCropName("TEST COMMODITY");
		resource.setIsProductInsurable(false);
		resource.setIsInventoryCrop(false);
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesEventTypes.CommodityVarietyCreated);

		//No insert expected
		service.synchronizeCommodityVariety(resource);

		
		logger.debug(">testCommodityNotInventoriableInsurable");
	}
	
	@Test
	public void testUpdateCommodityWithoutRecordNoUpdate() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testUpdateCommodityWithoutRecordNoUpdate");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		Date transactionDate = new Date();
		Date createTransactionDate = addSeconds(transactionDate, -1);

		//CREATE COMMODITY
		SyncCommodityVarietyRsrc resource = new SyncCommodityVarietyRsrc();

		resource.setCropId(testCommodityId);
		resource.setCropName("TEST COMMODITY");
		resource.setIsProductInsurable(true);
		resource.setIsInventoryCrop(true);
		resource.setDataSyncTransDate(createTransactionDate);
		
		//TRY TO DELETE A COMMODITY THAT DOESN'T EXIST (NO ERROR EXECTED)
		resource.setTransactionType(PoliciesEventTypes.CommodityVarietyDeleted);
		service.synchronizeCommodityVariety(resource);

		//SHOULD RESULT IN AN INSERT
		resource.setTransactionType(PoliciesEventTypes.CommodityVarietyUpdated);

		//Expect insert (should be detected)
		service.synchronizeCommodityVariety(resource);

		//UPDATE COMMODITY
		resource.setCropName("TEST COMMODITY 2");
		resource.setDataSyncTransDate(addSeconds(transactionDate, -1));
		resource.setTransactionType(PoliciesEventTypes.CommodityVarietyUpdated);

		//NO UPDATE EXPECTED BECAUSE TRANSACTION DATE IS EARLIER THAN STORED ONE
		service.synchronizeCommodityVariety(resource);
		
		//UPDATE CODE --> USE CREATED TYPE
		resource.setDataSyncTransDate(addSeconds(transactionDate, 1));
		resource.setTransactionType(PoliciesEventTypes.CommodityVarietyCreated);

		//UPDATE EXPECTED BECAUSE RECORD EXISTS IT WILL UPDATE IT
		service.synchronizeCommodityVariety(resource);		
				
		//CLEAN UP: DELETE COMMODITY
		delete(testCommodityId);
		
		logger.debug(">testUpdateCommodityWithoutRecordNoUpdate");
	}
	

	//
	//VARIETY TESTS
	//
	@Test
	public void testCreateUpdateDeleteVariety() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testCreateUpdateDeleteVariety");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		Date transactionDate = new Date();
		Date createTransactionDate = addSeconds(transactionDate, -1);
		
		createParentCommodity(createTransactionDate);

		//CREATE VARIETY
		SyncCommodityVarietyRsrc resource = new SyncCommodityVarietyRsrc();

		resource.setCropId(testVarietyId);
		resource.setParentCropId(testCommodityId);
		resource.setCropName("TEST VARIETY");
		resource.setIsProductInsurable(false); //Should be ignored for varieties
		resource.setIsInventoryCrop(false); //Should be ignored for varieties
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesEventTypes.CommodityVarietyCreated);

		service.synchronizeCommodityVariety(resource);

		//UPDATE VARIETY
		resource.setCropName("TEST VARIETY 2");
		resource.setDataSyncTransDate(addSeconds(transactionDate, +1));
		resource.setTransactionType(PoliciesEventTypes.CommodityVarietyUpdated);
		
		service.synchronizeCommodityVariety(resource);

		//DELETE VARIETY (SET INACTIVE)
		resource.setDataSyncTransDate(addSeconds(transactionDate, +2));
		resource.setTransactionType(PoliciesEventTypes.CommodityVarietyDeleted);

		service.synchronizeCommodityVariety(resource);

		//CLEAN UP: DELETE VARIETY AND PARENT COMMODITY
		delete(testVarietyId);
		delete(testCommodityId);
		
		logger.debug(">testCreateUpdateDeleteVariety");
	}

	@Test
	public void testUpdateVarietyWithoutRecordNoUpdate() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testUpdateVarietyWithoutRecordNoUpdate");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		Date transactionDate = new Date();
		Date createTransactionDate = addSeconds(transactionDate, -1);
		
		createParentCommodity(createTransactionDate);

		//CREATE VARIETY
		SyncCommodityVarietyRsrc resource = new SyncCommodityVarietyRsrc();

		resource.setCropId(testVarietyId);
		resource.setParentCropId(testCommodityId);
		resource.setCropName("TEST VARIETY");
		resource.setDataSyncTransDate(createTransactionDate);
		
		//TRY TO DELETE A VARIETY THAT DOESN'T EXIST (NO ERROR EXECTED)
		resource.setTransactionType(PoliciesEventTypes.CommodityVarietyDeleted);
		service.synchronizeCommodityVariety(resource);


		//SHOULD RESULT IN AN INSERT BECAUSE IT DOESN'T EXIST YET
		resource.setTransactionType(PoliciesEventTypes.CommodityVarietyUpdated);
		service.synchronizeCommodityVariety(resource);

		//UPDATE VARIETY
		resource.setCropName("TEST VARIETY 2");
		resource.setDataSyncTransDate(addSeconds(transactionDate, -1));
		resource.setTransactionType(PoliciesEventTypes.CommodityVarietyUpdated);

		//NO UPDATE EXPECTED BECAUSE TRANSACTION DATE IS EARLIER THAN STORED ONE
		service.synchronizeCommodityVariety(resource);

		//UPDATE CODE --> USE CREATED TYPE
		resource.setDataSyncTransDate(addSeconds(transactionDate, 1));
		resource.setTransactionType(PoliciesEventTypes.CommodityVarietyCreated);

		//UPDATE EXPECTED BECAUSE RECORD EXISTS IT WILL UPDATE IT
		service.synchronizeCommodityVariety(resource);		
		//CLEAN UP: DELETE VARIETY AND PARENT COMMODITY
		delete(testVarietyId);
		delete(testCommodityId);
		
		logger.debug(">testUpdateVarietyWithoutRecordNoUpdate");
	}
	
	private void createParentCommodity(Date createTransactionDate)
			throws CirrasClaimServiceException, ValidationException {
		//CREATE PARENT COMMODITY
		SyncCommodityVarietyRsrc resource = new SyncCommodityVarietyRsrc();

		resource.setCropId(testCommodityId);
		resource.setCropName("TEST COMMODITY");
		resource.setIsProductInsurable(true);
		resource.setIsInventoryCrop(true);
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesEventTypes.CommodityVarietyCreated);

		service.synchronizeCommodityVariety(resource);
	}
	
	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}
	

}
