package ca.bc.gov.mal.cirras.claims.controllers;

import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.claims.clients.CirrasClaimService;
import ca.bc.gov.mal.cirras.claims.clients.CirrasClaimServiceException;
import ca.bc.gov.mal.cirras.claims.clients.ValidationException;
import ca.bc.gov.mal.cirras.claims.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.claims.data.models.SyncDopYieldContractCommodityBerries;
import ca.bc.gov.mal.cirras.claims.data.resources.EndpointsRsrc;
import ca.bc.gov.mal.cirras.claims.data.resources.SyncDopYieldContractSimpleRsrc;
import ca.bc.gov.mal.cirras.claims.data.resources.UnderwritingSyncEventTypes;
import ca.bc.gov.mal.cirras.claims.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;


public class SyncDopYieldContractSimpleEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(SyncDopYieldContractSimpleEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.GET_SYNC_CLAIM,
		Scopes.CREATE_SYNC_CLAIM,
		Scopes.UPDATE_SYNC_CLAIM,
		Scopes.DELETE_SYNC_CLAIM
	};
	
	private CirrasClaimService service;
	private EndpointsRsrc topLevelEndpoints;

	private String declaredYieldContractCommodityBerriesGuid = "79fe078407254b25bd7905a840f4084e";
	
	@Before
	public void prepareTests() throws CirrasClaimServiceException, Oauth2ClientException, NotFoundDaoException, DaoException{
		service = getService(SCOPES);
		topLevelEndpoints = service.getTopLevelEndpoints();

		delete();

	}

	@After 
	public void cleanUp() throws CirrasClaimServiceException, NotFoundDaoException, DaoException {
		delete();
	}

	
	private void delete() throws NotFoundDaoException, DaoException, CirrasClaimServiceException{
		service.deleteSyncDopYieldContractSimple(topLevelEndpoints, declaredYieldContractCommodityBerriesGuid);
	}
	
	@Test
	public void testCreateUpdateDeleteSyncDopYieldContractSimple() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testCreateUpdateDeleteSyncDopYieldContractSimple");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();
		
		Date createTransactionDate = addSeconds(transactionDate, -1);

		//CREATE
		SyncDopYieldContractSimpleRsrc resource = new SyncDopYieldContractSimpleRsrc();

		resource.setContractId(888888888);
		resource.setCropYear(2020);
		
		SyncDopYieldContractCommodityBerries sdyccb = new SyncDopYieldContractCommodityBerries();

		sdyccb.setCropCommodityId(10);
		sdyccb.setCropCommodityName("Blueberry");
		sdyccb.setDeclaredYieldContractCommodityBerriesGuid(declaredYieldContractCommodityBerriesGuid);
		sdyccb.setTotalAbandonmentYield(111.2222);
		sdyccb.setTotalProduction(333.4444);
		sdyccb.setTotalProductionOverride(555.6666);
		sdyccb.setTotalSalesYield(777.8888);
		sdyccb.setTotalSoldShippedYield(999.1111);
		
		resource.setSyncDopYieldContractCommodityBerries(sdyccb);
		
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(UnderwritingSyncEventTypes.DopYieldContractCommodityBerriesCreated);

		service.synchronizeDopYieldContractSimple(resource);

		//FETCH
		SyncDopYieldContractSimpleRsrc fetchedResource = service.getSyncDopYieldContractSimple(topLevelEndpoints, declaredYieldContractCommodityBerriesGuid);

		Assert.assertEquals(resource.getContractId(), fetchedResource.getContractId());
		Assert.assertEquals(resource.getCropYear(), fetchedResource.getCropYear());
		Assert.assertTrue(resource.getDataSyncTransDate().compareTo(fetchedResource.getDataSyncTransDate()) == 0);
		Assert.assertNotNull(fetchedResource.getSyncDopYieldContractCommodityBerries());
		
		SyncDopYieldContractCommodityBerries fetchedSdyccb = fetchedResource.getSyncDopYieldContractCommodityBerries();
		Assert.assertEquals(sdyccb.getCropCommodityId(), fetchedSdyccb.getCropCommodityId());
		Assert.assertEquals(sdyccb.getCropCommodityName(), fetchedSdyccb.getCropCommodityName());
		Assert.assertEquals(sdyccb.getDeclaredYieldContractCommodityBerriesGuid(), fetchedSdyccb.getDeclaredYieldContractCommodityBerriesGuid());
		Assert.assertEquals(sdyccb.getTotalAbandonmentYield(), fetchedSdyccb.getTotalAbandonmentYield());
		Assert.assertEquals(sdyccb.getTotalProduction(), fetchedSdyccb.getTotalProduction());
		Assert.assertEquals(sdyccb.getTotalProductionOverride(), fetchedSdyccb.getTotalProductionOverride());
		Assert.assertEquals(sdyccb.getTotalSalesYield(), fetchedSdyccb.getTotalSalesYield());
		Assert.assertEquals(sdyccb.getTotalSoldShippedYield(), fetchedSdyccb.getTotalSoldShippedYield());
		
		//UPDATE
		fetchedSdyccb.setTotalAbandonmentYield(222.3333);
		fetchedSdyccb.setTotalProduction(444.5555);
		fetchedSdyccb.setTotalProductionOverride(777.8888);
		fetchedSdyccb.setTotalSalesYield(999.1111);
		fetchedSdyccb.setTotalSoldShippedYield(111.2222);
		
		fetchedResource.setDataSyncTransDate(addSeconds(transactionDate, +1));
		fetchedResource.setTransactionType(UnderwritingSyncEventTypes.DopYieldContractCommodityBerriesUpdated);
		
		service.synchronizeDopYieldContractSimple(fetchedResource);

		//FETCH
		SyncDopYieldContractSimpleRsrc updatedResource = service.getSyncDopYieldContractSimple(topLevelEndpoints, declaredYieldContractCommodityBerriesGuid);

		Assert.assertEquals(fetchedResource.getContractId(), updatedResource.getContractId());
		Assert.assertEquals(fetchedResource.getCropYear(), updatedResource.getCropYear());
		Assert.assertTrue(fetchedResource.getDataSyncTransDate().compareTo(updatedResource.getDataSyncTransDate()) == 0);
		Assert.assertNotNull(updatedResource.getSyncDopYieldContractCommodityBerries());
		
		SyncDopYieldContractCommodityBerries updatedSdyccb = updatedResource.getSyncDopYieldContractCommodityBerries();
		Assert.assertEquals(fetchedSdyccb.getCropCommodityId(), updatedSdyccb.getCropCommodityId());
		Assert.assertEquals(fetchedSdyccb.getCropCommodityName(), updatedSdyccb.getCropCommodityName());
		Assert.assertEquals(fetchedSdyccb.getDeclaredYieldContractCommodityBerriesGuid(), updatedSdyccb.getDeclaredYieldContractCommodityBerriesGuid());
		Assert.assertEquals(fetchedSdyccb.getTotalAbandonmentYield(), updatedSdyccb.getTotalAbandonmentYield());
		Assert.assertEquals(fetchedSdyccb.getTotalProduction(), updatedSdyccb.getTotalProduction());
		Assert.assertEquals(fetchedSdyccb.getTotalProductionOverride(), updatedSdyccb.getTotalProductionOverride());
		Assert.assertEquals(fetchedSdyccb.getTotalSalesYield(), updatedSdyccb.getTotalSalesYield());
		Assert.assertEquals(fetchedSdyccb.getTotalSoldShippedYield(), updatedSdyccb.getTotalSoldShippedYield());
		
		
		//DELETE
		updatedResource.setDataSyncTransDate(addSeconds(transactionDate, +2));
		updatedResource.setTransactionType(UnderwritingSyncEventTypes.DopYieldContractCommodityBerriesDeleted);
		
		service.synchronizeDopYieldContractSimple(updatedResource);

		SyncDopYieldContractSimpleRsrc deletedResource = service.getSyncDopYieldContractSimple(topLevelEndpoints, declaredYieldContractCommodityBerriesGuid);
		Assert.assertNull(deletedResource);

		//CREATE second resource
		SyncDopYieldContractSimpleRsrc resource2 = new SyncDopYieldContractSimpleRsrc();

		resource2.setContractId(888888888);
		resource2.setCropYear(2020);
		
		SyncDopYieldContractCommodityBerries sdyccb2 = new SyncDopYieldContractCommodityBerries();

		sdyccb2.setCropCommodityId(10);
		sdyccb2.setCropCommodityName("Blueberry");
		sdyccb2.setDeclaredYieldContractCommodityBerriesGuid(declaredYieldContractCommodityBerriesGuid);
		sdyccb2.setTotalAbandonmentYield(111.2222);
		sdyccb2.setTotalProduction(333.4444);
		sdyccb2.setTotalProductionOverride(555.6666);
		sdyccb2.setTotalSalesYield(777.8888);
		sdyccb2.setTotalSoldShippedYield(999.1111);
		
		resource2.setSyncDopYieldContractCommodityBerries(sdyccb2);
		
		resource2.setDataSyncTransDate(createTransactionDate);
		resource2.setTransactionType(UnderwritingSyncEventTypes.DopYieldContractCommodityBerriesCreated);

		service.synchronizeDopYieldContractSimple(resource2);

		//FETCH
		SyncDopYieldContractSimpleRsrc fetchedResource2 = service.getSyncDopYieldContractSimple(topLevelEndpoints, declaredYieldContractCommodityBerriesGuid);

		Assert.assertNotNull(fetchedResource2.getSyncDopYieldContractCommodityBerries());
		
		SyncDopYieldContractCommodityBerries fetchedSdyccb2 = fetchedResource2.getSyncDopYieldContractCommodityBerries();
		Assert.assertEquals(sdyccb2.getDeclaredYieldContractCommodityBerriesGuid(), fetchedSdyccb2.getDeclaredYieldContractCommodityBerriesGuid());

		//DELETE using the other delete method
		service.deleteSyncDopYieldContractSimple(topLevelEndpoints, declaredYieldContractCommodityBerriesGuid);

		SyncDopYieldContractSimpleRsrc deletedResource2 = service.getSyncDopYieldContractSimple(topLevelEndpoints, declaredYieldContractCommodityBerriesGuid);
		Assert.assertNull(deletedResource2);
		
		logger.debug(">testCreateUpdateDeleteSyncDopYieldContractSimple");
	}
	
	@Test
	public void testUpdateSyncDopYieldContractSimpleWithoutRecordNoUpdate() throws CirrasClaimServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testUpdateSyncDopYieldContractSimpleWithoutRecordNoUpdate");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);

		//CREATE
		SyncDopYieldContractSimpleRsrc resource = new SyncDopYieldContractSimpleRsrc();

		resource.setContractId(888888888);
		resource.setCropYear(2020);
		
		SyncDopYieldContractCommodityBerries sdyccb = new SyncDopYieldContractCommodityBerries();

		sdyccb.setCropCommodityId(10);
		sdyccb.setCropCommodityName("Blueberry");
		sdyccb.setDeclaredYieldContractCommodityBerriesGuid(declaredYieldContractCommodityBerriesGuid);
		sdyccb.setTotalAbandonmentYield(111.2222);
		sdyccb.setTotalProduction(333.4444);
		sdyccb.setTotalProductionOverride(555.6666);
		sdyccb.setTotalSalesYield(777.8888);
		sdyccb.setTotalSoldShippedYield(999.1111);
		
		resource.setSyncDopYieldContractCommodityBerries(sdyccb);
		
		resource.setDataSyncTransDate(createTransactionDate);

		//TRY TO DELETE A RECORD THAT DOESN'T EXIST (NO ERROR EXECTED)
		resource.setTransactionType(UnderwritingSyncEventTypes.DopYieldContractCommodityBerriesDeleted);

		service.synchronizeDopYieldContractSimple(resource);

		//FETCH
		SyncDopYieldContractSimpleRsrc fetchedResource = service.getSyncDopYieldContractSimple(topLevelEndpoints, declaredYieldContractCommodityBerriesGuid);
		Assert.assertNull(fetchedResource);
		
		//SHOULD RESULT IN AN INSERT
		resource.setTransactionType(UnderwritingSyncEventTypes.DopYieldContractCommodityBerriesUpdated);
		
		service.synchronizeDopYieldContractSimple(resource);
				
		//FETCH
		fetchedResource = service.getSyncDopYieldContractSimple(topLevelEndpoints, declaredYieldContractCommodityBerriesGuid);

		Assert.assertEquals(resource.getContractId(), fetchedResource.getContractId());
		Assert.assertEquals(resource.getCropYear(), fetchedResource.getCropYear());
		Assert.assertTrue(resource.getDataSyncTransDate().compareTo(fetchedResource.getDataSyncTransDate()) == 0);
		Assert.assertNotNull(fetchedResource.getSyncDopYieldContractCommodityBerries());
		
		SyncDopYieldContractCommodityBerries fetchedSdyccb = fetchedResource.getSyncDopYieldContractCommodityBerries();
		Assert.assertEquals(sdyccb.getCropCommodityId(), fetchedSdyccb.getCropCommodityId());
		Assert.assertEquals(sdyccb.getCropCommodityName(), fetchedSdyccb.getCropCommodityName());
		Assert.assertEquals(sdyccb.getDeclaredYieldContractCommodityBerriesGuid(), fetchedSdyccb.getDeclaredYieldContractCommodityBerriesGuid());
		Assert.assertEquals(sdyccb.getTotalAbandonmentYield(), fetchedSdyccb.getTotalAbandonmentYield());
		Assert.assertEquals(sdyccb.getTotalProduction(), fetchedSdyccb.getTotalProduction());
		Assert.assertEquals(sdyccb.getTotalProductionOverride(), fetchedSdyccb.getTotalProductionOverride());
		Assert.assertEquals(sdyccb.getTotalSalesYield(), fetchedSdyccb.getTotalSalesYield());
		Assert.assertEquals(sdyccb.getTotalSoldShippedYield(), fetchedSdyccb.getTotalSoldShippedYield());

		//UPDATE
		fetchedSdyccb.setTotalAbandonmentYield(222.3333);
		fetchedSdyccb.setTotalProduction(444.5555);
		fetchedSdyccb.setTotalProductionOverride(777.8888);
		fetchedSdyccb.setTotalSalesYield(999.1111);
		fetchedSdyccb.setTotalSoldShippedYield(111.2222);
		
		fetchedResource.setDataSyncTransDate(addSeconds(transactionDate, -2));
		fetchedResource.setTransactionType(UnderwritingSyncEventTypes.DopYieldContractCommodityBerriesUpdated);
		
		//NO UPDATE EXPECTED BECAUSE TRANSACTION DATE IS EARLIER THAN STORED ONE
		service.synchronizeDopYieldContractSimple(fetchedResource);

		SyncDopYieldContractSimpleRsrc notUpdatedResource = service.getSyncDopYieldContractSimple(topLevelEndpoints, declaredYieldContractCommodityBerriesGuid);

		Assert.assertTrue(resource.getDataSyncTransDate().compareTo(notUpdatedResource.getDataSyncTransDate()) == 0);

		
		//UPDATE CODE --> USE CREATED TYPE
		fetchedResource.setDataSyncTransDate(addSeconds(transactionDate, 1));
		fetchedResource.setTransactionType(UnderwritingSyncEventTypes.DopYieldContractCommodityBerriesCreated);

		//UPDATE EXPECTED BECAUSE RECORD EXISTS IT WILL UPDATE IT
		service.synchronizeDopYieldContractSimple(fetchedResource);
				
		//FETCH
		SyncDopYieldContractSimpleRsrc updatedResource = service.getSyncDopYieldContractSimple(topLevelEndpoints, declaredYieldContractCommodityBerriesGuid);

		Assert.assertEquals(fetchedResource.getContractId(), updatedResource.getContractId());
		Assert.assertEquals(fetchedResource.getCropYear(), updatedResource.getCropYear());
		Assert.assertTrue(fetchedResource.getDataSyncTransDate().compareTo(updatedResource.getDataSyncTransDate()) == 0);
		Assert.assertNotNull(updatedResource.getSyncDopYieldContractCommodityBerries());
		
		SyncDopYieldContractCommodityBerries updatedSdyccb = updatedResource.getSyncDopYieldContractCommodityBerries();
		Assert.assertEquals(fetchedSdyccb.getCropCommodityId(), updatedSdyccb.getCropCommodityId());
		Assert.assertEquals(fetchedSdyccb.getCropCommodityName(), updatedSdyccb.getCropCommodityName());
		Assert.assertEquals(fetchedSdyccb.getDeclaredYieldContractCommodityBerriesGuid(), updatedSdyccb.getDeclaredYieldContractCommodityBerriesGuid());
		Assert.assertEquals(fetchedSdyccb.getTotalAbandonmentYield(), updatedSdyccb.getTotalAbandonmentYield());
		Assert.assertEquals(fetchedSdyccb.getTotalProduction(), updatedSdyccb.getTotalProduction());
		Assert.assertEquals(fetchedSdyccb.getTotalProductionOverride(), updatedSdyccb.getTotalProductionOverride());
		Assert.assertEquals(fetchedSdyccb.getTotalSalesYield(), updatedSdyccb.getTotalSalesYield());
		Assert.assertEquals(fetchedSdyccb.getTotalSoldShippedYield(), updatedSdyccb.getTotalSoldShippedYield());
		
		//DELETE
		updatedResource.setDataSyncTransDate(addSeconds(transactionDate, 2));
		updatedResource.setTransactionType(UnderwritingSyncEventTypes.DopYieldContractCommodityBerriesDeleted);
		
		service.synchronizeDopYieldContractSimple(updatedResource);
				
		logger.debug(">testUpdateSyncDopYieldContractSimpleWithoutRecordNoUpdate");
	}
	
	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}
}
