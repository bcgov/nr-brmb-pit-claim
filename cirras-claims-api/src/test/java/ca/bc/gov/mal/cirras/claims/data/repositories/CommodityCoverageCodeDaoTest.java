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

import ca.bc.gov.mal.cirras.claims.data.entities.CommodityCoverageCodeDto;
import ca.bc.gov.mal.cirras.claims.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class CommodityCoverageCodeDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private String commodityCoverageCode = "TEST";


	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		deleteCommodityCoverageCode();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		deleteCommodityCoverageCode();
	}
	
	private void deleteCommodityCoverageCode() throws NotFoundDaoException, DaoException{
		
		CommodityCoverageCodeDao dao = persistenceSpringConfig.commodityCoverageCodeDao();
		CommodityCoverageCodeDto dto = dao.fetch(commodityCoverageCode);
		if (dto != null) {
			dao.delete(commodityCoverageCode);
		}
	}
	
	@Test 
	public void testInsertUpdateDeleteCommodityCoverageCode() throws Exception {

		CommodityCoverageCodeDao dao = persistenceSpringConfig.commodityCoverageCodeDao();
		CommodityCoverageCodeDto newDto = new CommodityCoverageCodeDto();
		
		String commodityCoverageDescription = "TEST COVERAGE TYPE";
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date date = cal.getTime();
		
		Date effectiveDate = TestUtils.removeTimestamp(TestUtils.addDays(date, -121));
		Date expiryDate = TestUtils.removeTimestamp(TestUtils.addDays(date, -119));
		Date dataSyncTransDate = TestUtils.addSeconds(date, -120);

		String userId = "JUNIT_TEST";

		//INSERT
		newDto.setCommodityCoverageCode(commodityCoverageCode);
		newDto.setDescription(commodityCoverageDescription);
		newDto.setEffectiveDate(effectiveDate);
		newDto.setExpiryDate(expiryDate);
		newDto.setDataSyncTransDate(dataSyncTransDate);

		dao.insert(newDto, userId);
		
		//FETCH
		CommodityCoverageCodeDto fetchedDto = dao.fetch(commodityCoverageCode);

		Assert.assertEquals("CommodityCoverageCode", newDto.getCommodityCoverageCode(), fetchedDto.getCommodityCoverageCode());
		Assert.assertEquals("Description", newDto.getDescription(), fetchedDto.getDescription());
		Assert.assertTrue("EffectiveDate", newDto.getEffectiveDate().compareTo(fetchedDto.getEffectiveDate()) == 0);
		Assert.assertTrue("ExpiryDate", newDto.getExpiryDate().compareTo(fetchedDto.getExpiryDate()) == 0);
		Assert.assertTrue("DataSyncTransDate", newDto.getDataSyncTransDate().compareTo(fetchedDto.getDataSyncTransDate()) == 0);
		
		
		//UPDATE
		commodityCoverageDescription = "TEST COVERAGE TYPE 2";
		effectiveDate = TestUtils.removeTimestamp(TestUtils.addDays(date, -61));
		expiryDate = TestUtils.removeTimestamp(TestUtils.addDays(date, -59));
		dataSyncTransDate = TestUtils.addSeconds(date, -60);

		fetchedDto.setDescription(commodityCoverageDescription);
		fetchedDto.setEffectiveDate(effectiveDate);
		fetchedDto.setExpiryDate(expiryDate);
		fetchedDto.setDataSyncTransDate(dataSyncTransDate);
		
		dao.update(fetchedDto, userId);
		
		//FETCH
		CommodityCoverageCodeDto updatedDto = dao.fetch(commodityCoverageCode);
		
		Assert.assertEquals("Description", fetchedDto.getDescription(), updatedDto.getDescription());
		Assert.assertTrue("EffectiveDate", fetchedDto.getEffectiveDate().compareTo(updatedDto.getEffectiveDate()) == 0);
		Assert.assertTrue("ExpiryDate", fetchedDto.getExpiryDate().compareTo(updatedDto.getExpiryDate()) == 0);
		Assert.assertTrue("DataSyncTransDate", fetchedDto.getDataSyncTransDate().compareTo(updatedDto.getDataSyncTransDate()) == 0);

		
		//Expect NO update becaus the transaction date is before the latest update
		userId = "JUNIT_TEST_NO_UPDATE";
		Date newDataSyncTransDate = TestUtils.addSeconds(updatedDto.getDataSyncTransDate(), -10);
		updatedDto.setDataSyncTransDate(newDataSyncTransDate);

		dao.update(updatedDto, userId);
		
		//FETCH
		CommodityCoverageCodeDto notUpdatedDto = dao.fetch(commodityCoverageCode);

		//DataSyncTransDate is still the same (no update happened)
		Assert.assertTrue("DataSyncTransDate", notUpdatedDto.getDataSyncTransDate().compareTo(dataSyncTransDate) == 0);

		
		//DELETE
		dao.delete(notUpdatedDto.getCommodityCoverageCode());

	}

}
