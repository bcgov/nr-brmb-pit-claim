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

import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.CoveragePerilDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class CoveragePerilDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	//Has to be a combination that does not exist in the coverage_peril table
	private Integer coveragePerilId = 12345678;
	private Integer cropCommodityId = 1;
	private String perilCode = "SNOW";
	private String coverageCode = "CQNT";
	


	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		deleteCoveragePeril();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		deleteCoveragePeril();
	}
	
	private void deleteCoveragePeril() throws NotFoundDaoException, DaoException{
		
		CoveragePerilDao dao = persistenceSpringConfig.coveragePerilDao();
		CoveragePerilDto dto = dao.fetch(coveragePerilId);
		if (dto != null) {
			dao.delete(coveragePerilId);
		}
	}
	
	@Test 
	public void testInsertUpdateDeleteCoveragePeril() throws Exception {

		CoveragePerilDao dao = persistenceSpringConfig.coveragePerilDao();
		CoveragePerilDto newDto = new CoveragePerilDto();
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date date = cal.getTime();
		
		Date effectiveDate = TestUtils.removeTimestamp(TestUtils.addDays(date, -121));
		Date expiryDate = TestUtils.removeTimestamp(TestUtils.addDays(date, -119));
		Date dataSyncTransDate = TestUtils.addSeconds(date, -120);

		String userId = "JUNIT_TEST";

		//INSERT
		newDto.setCoveragePerilId(coveragePerilId);
		newDto.setCropCommodityId(cropCommodityId);
		newDto.setPerilCode(perilCode);
		newDto.setCommodityCoverageCode(coverageCode);
		newDto.setEffectiveDate(effectiveDate);
		newDto.setExpiryDate(expiryDate);
		newDto.setDataSyncTransDate(dataSyncTransDate);

		dao.insert(newDto, userId);
		
		//FETCH
		CoveragePerilDto fetchedDto = dao.fetch(coveragePerilId);

		Assert.assertEquals("CropCommodityId", newDto.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("PerilCode", newDto.getPerilCode(), fetchedDto.getPerilCode());
		Assert.assertEquals("CommodityCoverageCode", newDto.getCommodityCoverageCode(), fetchedDto.getCommodityCoverageCode());
		Assert.assertTrue("EffectiveDate", newDto.getEffectiveDate().compareTo(fetchedDto.getEffectiveDate()) == 0);
		Assert.assertTrue("ExpiryDate", newDto.getExpiryDate().compareTo(fetchedDto.getExpiryDate()) == 0);
		Assert.assertTrue("DataSyncTransDate", newDto.getDataSyncTransDate().compareTo(fetchedDto.getDataSyncTransDate()) == 0);
		
		
		//UPDATE
		effectiveDate = TestUtils.removeTimestamp(TestUtils.addDays(date, -61));
		expiryDate = TestUtils.removeTimestamp(TestUtils.addDays(date, -59));
		dataSyncTransDate = TestUtils.addSeconds(date, -60);

		userId = "JUNIT_TEST_UPDATE";
		fetchedDto.setEffectiveDate(effectiveDate);
		fetchedDto.setExpiryDate(expiryDate);
		fetchedDto.setDataSyncTransDate(dataSyncTransDate);
		
		dao.update(fetchedDto, userId);
		
		//FETCH
		CoveragePerilDto updatedDto = dao.fetch(coveragePerilId);
		
		Assert.assertTrue("EffectiveDate", fetchedDto.getEffectiveDate().compareTo(updatedDto.getEffectiveDate()) == 0);
		Assert.assertTrue("ExpiryDate", fetchedDto.getExpiryDate().compareTo(updatedDto.getExpiryDate()) == 0);
		Assert.assertTrue("DataSyncTransDate", fetchedDto.getDataSyncTransDate().compareTo(updatedDto.getDataSyncTransDate()) == 0);

		
		//Expect NO update becaus the transaction date is before the latest update
		userId = "JUNIT_TEST_NO_UPDATE";
		Date newDataSyncTransDate = TestUtils.addSeconds(updatedDto.getDataSyncTransDate(), -10);
		updatedDto.setDataSyncTransDate(newDataSyncTransDate);

		dao.update(updatedDto, userId);
		
		//FETCH
		CoveragePerilDto notUpdatedDto = dao.fetch(coveragePerilId);

		//DataSyncTransDate is still the same (no update happened)
		Assert.assertTrue("DataSyncTransDate", notUpdatedDto.getDataSyncTransDate().compareTo(dataSyncTransDate) == 0);

		
		//DELETE
		dao.delete(coveragePerilId);

	}

}
