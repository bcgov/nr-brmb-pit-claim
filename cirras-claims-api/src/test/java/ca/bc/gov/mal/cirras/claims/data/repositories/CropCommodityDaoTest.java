package ca.bc.gov.mal.cirras.claims.data.repositories;

import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.bc.gov.mal.cirras.claims.data.entities.CropCommodityDto;
import ca.bc.gov.mal.cirras.claims.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class CropCommodityDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private Integer cropCommodityId = 99999;


	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		deleteCropCommodity();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		deleteCropCommodity();
	}
	
	private void deleteCropCommodity() throws NotFoundDaoException, DaoException{
		
		CropCommodityDao dao = persistenceSpringConfig.cropCommodityDao();
		CropCommodityDto dto = dao.fetch(cropCommodityId);
		if (dto != null) {
			dao.delete(cropCommodityId);
		}
	}
	
	@Test 
	public void testInsertUpdateDeleteClaimCalculationCropCommodity() throws Exception {

		CropCommodityDao dao = persistenceSpringConfig.cropCommodityDao();
		CropCommodityDto newDto = new CropCommodityDto();
		
		String commodityName = "Test Commodity";
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date date = cal.getTime();
		
		Date effectiveDate = TestUtils.removeTimestamp(TestUtils.addDays(date, -121));
		Date expiryDate = TestUtils.removeTimestamp(TestUtils.addDays(date, -119));
		Date dataSyncTransDate = TestUtils.addSeconds(date, -120);

		String userId = "JUNIT_TEST";

		//INSERT
		newDto.setCropCommodityId(cropCommodityId);
		newDto.setCommodityName(commodityName);
		newDto.setEffectiveDate(effectiveDate);
		newDto.setExpiryDate(expiryDate);
		newDto.setDataSyncTransDate(dataSyncTransDate);

		dao.insert(newDto, userId);
		
		//FETCH
		CropCommodityDto fetchedDto = dao.fetch(cropCommodityId);

		Assert.assertEquals("CropCommodityId", newDto.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("CommodityName", newDto.getCommodityName(), fetchedDto.getCommodityName());
		Assert.assertTrue("EffectiveDate", newDto.getEffectiveDate().compareTo(fetchedDto.getEffectiveDate()) == 0);
		Assert.assertTrue("ExpiryDate", newDto.getExpiryDate().compareTo(fetchedDto.getExpiryDate()) == 0);
		Assert.assertTrue("DataSyncTransDate", newDto.getDataSyncTransDate().compareTo(fetchedDto.getDataSyncTransDate()) == 0);
		
		
		//UPDATE
		commodityName = "Test Commodity 2";
		effectiveDate = TestUtils.removeTimestamp(TestUtils.addDays(date, -61));
		expiryDate = TestUtils.removeTimestamp(TestUtils.addDays(date, -59));
		dataSyncTransDate = TestUtils.addSeconds(date, -60);

		fetchedDto.setCommodityName(commodityName);
		fetchedDto.setEffectiveDate(effectiveDate);
		fetchedDto.setExpiryDate(expiryDate);
		fetchedDto.setDataSyncTransDate(dataSyncTransDate);
		
		dao.update(fetchedDto, userId);
		
		//FETCH
		CropCommodityDto updatedDto = dao.fetch(cropCommodityId);
		
		Assert.assertEquals("CommodityName", fetchedDto.getCommodityName(), updatedDto.getCommodityName());
		Assert.assertTrue("EffectiveDate", fetchedDto.getEffectiveDate().compareTo(updatedDto.getEffectiveDate()) == 0);
		Assert.assertTrue("ExpiryDate", fetchedDto.getExpiryDate().compareTo(updatedDto.getExpiryDate()) == 0);
		Assert.assertTrue("DataSyncTransDate", fetchedDto.getDataSyncTransDate().compareTo(updatedDto.getDataSyncTransDate()) == 0);

		//DELETE
		dao.delete(updatedDto.getCropCommodityId());

	}

	@Test 
	public void testGetLinkedCropCommodityByPedigree() throws Exception {

		CropCommodityDao dao = persistenceSpringConfig.cropCommodityDao();

		CropCommodityDto srcCrpDto = null;
		CropCommodityDto linkedCrpDto = null;


		// TEST 1: Search for linked commodity to FIELD PEA
		Integer srcCrptId = 21;
		Boolean srcIsPedigreeInd = false;
		String srcCommodityName = "Field Pea";

		Integer linkedCrptId = 22;
		Boolean linkedIsPedigreeInd = true;
		String linkedCommodityName = "Field Pea - Pedigreed";

		srcCrpDto = dao.fetch(srcCrptId);
		Assert.assertEquals(srcCrptId, srcCrpDto.getCropCommodityId());
		Assert.assertEquals(srcIsPedigreeInd, srcCrpDto.getIsPedigreeInd());
		Assert.assertEquals(srcCommodityName, srcCrpDto.getCommodityName());
		
		
		linkedCrpDto = dao.getLinkedCommodityByPedigree(srcCrptId);
		
		Assert.assertEquals(linkedCrptId, linkedCrpDto.getCropCommodityId());
		Assert.assertEquals(linkedIsPedigreeInd, linkedCrpDto.getIsPedigreeInd());
		Assert.assertEquals(linkedCommodityName, linkedCrpDto.getCommodityName());


		// TEST 2: Search for linked commodity to FIELD PEA - PEDIGREED
		srcCrptId = 22;
		srcIsPedigreeInd = true;
		srcCommodityName = "Field Pea - Pedigreed";
		
		linkedCrptId = 21;
		linkedIsPedigreeInd = false;
		linkedCommodityName = "Field Pea";

		srcCrpDto = dao.fetch(srcCrptId);
		Assert.assertEquals(srcCrptId, srcCrpDto.getCropCommodityId());
		Assert.assertEquals(srcIsPedigreeInd, srcCrpDto.getIsPedigreeInd());
		Assert.assertEquals(srcCommodityName, srcCrpDto.getCommodityName());
				
		linkedCrpDto = dao.getLinkedCommodityByPedigree(srcCrptId);
		
		Assert.assertEquals(linkedCrptId, linkedCrpDto.getCropCommodityId());
		Assert.assertEquals(linkedIsPedigreeInd, linkedCrpDto.getIsPedigreeInd());
		Assert.assertEquals(linkedCommodityName, linkedCrpDto.getCommodityName());
		

		// TEST 3: Search for linked commodity to APPLE
		srcCrptId = 3;
		srcIsPedigreeInd = false;
		srcCommodityName = "Apple";

		linkedCrptId = null;  // None
		linkedIsPedigreeInd = null;
		linkedCommodityName = null;

		srcCrpDto = dao.fetch(srcCrptId);
		Assert.assertEquals(srcCrptId, srcCrpDto.getCropCommodityId());
		Assert.assertEquals(srcIsPedigreeInd, srcCrpDto.getIsPedigreeInd());
		Assert.assertEquals(srcCommodityName, srcCrpDto.getCommodityName());
		
		linkedCrpDto = dao.getLinkedCommodityByPedigree(srcCrptId);
		
		Assert.assertEquals(null, linkedCrpDto);		
	}
}
