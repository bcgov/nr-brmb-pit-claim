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

import ca.bc.gov.mal.cirras.claims.data.entities.InsurancePlanDto;
import ca.bc.gov.mal.cirras.claims.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class InsurancePlanDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private Integer insurancePlanId = 1111;


	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		deleteInsurancePlan();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		deleteInsurancePlan();
	}
	
	private void deleteInsurancePlan() throws NotFoundDaoException, DaoException{
		
		InsurancePlanDao dao = persistenceSpringConfig.insurancePlanDao();
		InsurancePlanDto dto = dao.fetch(insurancePlanId);
		if (dto != null) {
			dao.delete(insurancePlanId);
		}
	}
	
	@Test 
	public void testInsertUpdateDeleteInsurancePlan() throws Exception {

		InsurancePlanDao dao = persistenceSpringConfig.insurancePlanDao();
		InsurancePlanDto newDto = new InsurancePlanDto();
		
		String insurancePlanOriginalName = "TESTPLAN";
		String insurancePlanName = "Testplan";
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date date = cal.getTime();
		
		Date effectiveDate = TestUtils.removeTimestamp(TestUtils.addDays(date, -121));
		Date expiryDate = TestUtils.removeTimestamp(TestUtils.addDays(date, -119));
		Date dataSyncTransDate = TestUtils.addSeconds(date, -120);

		String userId = "JUNIT_TEST";

		//INSERT
		newDto.setInsurancePlanId(insurancePlanId);
		newDto.setInsurancePlanOriginalName(insurancePlanOriginalName);
		newDto.setInsurancePlanName(insurancePlanName);
		newDto.setEffectiveDate(effectiveDate);
		newDto.setExpiryDate(expiryDate);
		newDto.setDataSyncTransDate(dataSyncTransDate);

		dao.insert(newDto, userId);
		
		//FETCH
		InsurancePlanDto fetchedDto = dao.fetch(insurancePlanId);

		Assert.assertEquals("InsurancePlanId", newDto.getInsurancePlanId(), fetchedDto.getInsurancePlanId());
		Assert.assertEquals("InsurancePlanOriginalName", newDto.getInsurancePlanOriginalName(), fetchedDto.getInsurancePlanOriginalName());
		Assert.assertEquals("InsurancePlanName", newDto.getInsurancePlanName(), fetchedDto.getInsurancePlanName());
		Assert.assertTrue("EffectiveDate", newDto.getEffectiveDate().compareTo(fetchedDto.getEffectiveDate()) == 0);
		Assert.assertTrue("ExpiryDate", newDto.getExpiryDate().compareTo(fetchedDto.getExpiryDate()) == 0);
		Assert.assertTrue("DataSyncTransDate", newDto.getDataSyncTransDate().compareTo(fetchedDto.getDataSyncTransDate()) == 0);
		
		
		//UPDATE
		insurancePlanOriginalName = "TESTPLAN 2";
		insurancePlanName = "Testplan 2";
		effectiveDate = TestUtils.removeTimestamp(TestUtils.addDays(date, -61));
		expiryDate = TestUtils.removeTimestamp(TestUtils.addDays(date, -59));
		dataSyncTransDate = TestUtils.addSeconds(date, -60);

		fetchedDto.setInsurancePlanOriginalName(insurancePlanOriginalName);
		fetchedDto.setInsurancePlanName(insurancePlanName);
		fetchedDto.setEffectiveDate(effectiveDate);
		fetchedDto.setExpiryDate(expiryDate);
		fetchedDto.setDataSyncTransDate(dataSyncTransDate);
		
		dao.update(fetchedDto, userId);
		
		//FETCH
		InsurancePlanDto updatedDto = dao.fetch(insurancePlanId);
		
		Assert.assertEquals("InsurancePlanOriginalName", fetchedDto.getInsurancePlanOriginalName(), updatedDto.getInsurancePlanOriginalName());
		Assert.assertEquals("InsurancePlanName", fetchedDto.getInsurancePlanName(), updatedDto.getInsurancePlanName());
		Assert.assertTrue("EffectiveDate", fetchedDto.getEffectiveDate().compareTo(updatedDto.getEffectiveDate()) == 0);
		Assert.assertTrue("ExpiryDate", fetchedDto.getExpiryDate().compareTo(updatedDto.getExpiryDate()) == 0);
		Assert.assertTrue("DataSyncTransDate", fetchedDto.getDataSyncTransDate().compareTo(updatedDto.getDataSyncTransDate()) == 0);

		
		//Expect NO update becaus the transaction date is before the latest update
		userId = "JUNIT_TEST_NO_UPDATE";
		Date newDataSyncTransDate = TestUtils.addSeconds(updatedDto.getDataSyncTransDate(), -10);
		updatedDto.setDataSyncTransDate(newDataSyncTransDate);

		dao.update(updatedDto, userId);
		
		//FETCH
		InsurancePlanDto notUpdatedDto = dao.fetch(insurancePlanId);

		//DataSyncTransDate is still the same (no update happened)
		Assert.assertTrue("DataSyncTransDate", notUpdatedDto.getDataSyncTransDate().compareTo(dataSyncTransDate) == 0);
		
		//DELETE
		dao.delete(notUpdatedDto.getInsurancePlanId());

	}

}
