package ca.bc.gov.mal.cirras.claims.persistence.v1.dao;

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

import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.CropCommodityDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.CropVarietyDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class CropVarietyDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private Integer cropVarietyId = 99999;
	private Integer cropCommodityId1 = 88888;
	private Integer cropCommodityId2 = 77777;


	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		deleteCropVarietyCommodity();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		deleteCropVarietyCommodity();
	}
	
	private void deleteCropVarietyCommodity() throws NotFoundDaoException, DaoException{
		
		CropVarietyDao dao = persistenceSpringConfig.cropVarietyDao();
		CropVarietyDto dto = dao.fetch(cropVarietyId);
		if (dto != null) {
			dao.delete(cropVarietyId);
		}
		
		deleteCropCommodity(cropCommodityId1);
		deleteCropCommodity(cropCommodityId2);

	}
	
	private void deleteCropCommodity(Integer cropCommodityId) throws NotFoundDaoException, DaoException{
		CropCommodityDao daoCommodity = persistenceSpringConfig.cropCommodityDao();
		CropCommodityDto dtoCommodity = daoCommodity.fetch(cropCommodityId);
		if (dtoCommodity != null) {
			daoCommodity.delete(cropCommodityId);
		}
		
	}
	
	@Test 
	public void testInsertUpdateDeleteClaimCalculationCropVariety() throws Exception {

		//Insert a commodity
		insertCommodity(cropCommodityId1);
		insertCommodity(cropCommodityId2);
		
		CropVarietyDto newDto = new CropVarietyDto();
		
		String varietyName = "Test Variety";
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date date = cal.getTime();
		
		Date effectiveDate = TestUtils.removeTimestamp(TestUtils.addDays(date, -121));
		Date expiryDate = TestUtils.removeTimestamp(TestUtils.addDays(date, -119));
		Date dataSyncTransDate = TestUtils.addSeconds(date, -120);

		String userId = "JUNIT_TEST";

		CropVarietyDao dao = persistenceSpringConfig.cropVarietyDao();
		//INSERT
		newDto.setCropVarietyId(cropVarietyId);
		newDto.setCropCommodityId(cropCommodityId1);
		newDto.setVarietyName(varietyName);
		newDto.setEffectiveDate(effectiveDate);
		newDto.setExpiryDate(expiryDate);
		newDto.setDataSyncTransDate(dataSyncTransDate);

		dao.insert(newDto, userId);
		
		//FETCH
		CropVarietyDto fetchedDto = dao.fetch(cropVarietyId);

		Assert.assertEquals("CropVarietyId", newDto.getCropVarietyId(), fetchedDto.getCropVarietyId());
		Assert.assertEquals("CropCommodityId", newDto.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("VarietyName", newDto.getVarietyName(), fetchedDto.getVarietyName());
		Assert.assertTrue("EffectiveDate", newDto.getEffectiveDate().compareTo(fetchedDto.getEffectiveDate()) == 0);
		Assert.assertTrue("ExpiryDate", newDto.getExpiryDate().compareTo(fetchedDto.getExpiryDate()) == 0);
		Assert.assertTrue("DataSyncTransDate", newDto.getDataSyncTransDate().compareTo(fetchedDto.getDataSyncTransDate()) == 0);
		
		
		//UPDATE
		varietyName = "Test Variety 2";
		effectiveDate = TestUtils.removeTimestamp(TestUtils.addDays(date, -61));
		expiryDate = TestUtils.removeTimestamp(TestUtils.addDays(date, -59));
		dataSyncTransDate = TestUtils.addSeconds(date, -60);

		fetchedDto.setCropCommodityId(cropCommodityId2);
		fetchedDto.setVarietyName(varietyName);
		fetchedDto.setEffectiveDate(effectiveDate);
		fetchedDto.setExpiryDate(expiryDate);
		fetchedDto.setDataSyncTransDate(dataSyncTransDate);
		
		dao.update(fetchedDto, userId);
		
		//FETCH
		CropVarietyDto updatedDto = dao.fetch(cropVarietyId);
		
		Assert.assertEquals("CropCommodityId", fetchedDto.getCropCommodityId(), updatedDto.getCropCommodityId());
		Assert.assertEquals("VarietyName", fetchedDto.getVarietyName(), updatedDto.getVarietyName());
		Assert.assertTrue("EffectiveDate", fetchedDto.getEffectiveDate().compareTo(updatedDto.getEffectiveDate()) == 0);
		Assert.assertTrue("ExpiryDate", fetchedDto.getExpiryDate().compareTo(updatedDto.getExpiryDate()) == 0);
		Assert.assertTrue("DataSyncTransDate", fetchedDto.getDataSyncTransDate().compareTo(updatedDto.getDataSyncTransDate()) == 0);

	}
	
	private void insertCommodity(Integer cropCommodityId) throws DaoException {

		CropCommodityDto newDto = new CropCommodityDto();
		
		String userId = "JUNIT_TEST";

		CropCommodityDao dao = persistenceSpringConfig.cropCommodityDao();
		//INSERT
		newDto.setCropCommodityId(cropCommodityId);
		newDto.setCommodityName("Test Commodity");
		newDto.setEffectiveDate(new Date());
		newDto.setExpiryDate(new Date());
		newDto.setDataSyncTransDate(new Date());

		dao.insert(newDto, userId);
	}

}
