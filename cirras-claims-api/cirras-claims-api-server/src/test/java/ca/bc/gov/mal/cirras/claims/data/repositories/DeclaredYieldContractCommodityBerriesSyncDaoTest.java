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

import ca.bc.gov.mal.cirras.claims.data.entities.DeclaredYieldContractCommodityBerriesSyncDto;
import ca.bc.gov.mal.cirras.claims.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class DeclaredYieldContractCommodityBerriesSyncDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private String declaredYieldContractCommodityBerriesGuid = "79fe078407254b25bd7905a840f4084e";


	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		delete();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		delete();
	}
	
	private void delete() throws NotFoundDaoException, DaoException{
		
		DeclaredYieldContractCommodityBerriesSyncDao dao = persistenceSpringConfig.declaredYieldContractCommodityBerriesSyncDao();
		DeclaredYieldContractCommodityBerriesSyncDto dto = dao.fetch(declaredYieldContractCommodityBerriesGuid);
		if (dto != null) {
			dao.delete(declaredYieldContractCommodityBerriesGuid);
		}
	}
	
	@Test 
	public void testInsertUpdateDeleteDeclaredYieldContractCommodityBerriesSync() throws Exception {

		DeclaredYieldContractCommodityBerriesSyncDao dao = persistenceSpringConfig.declaredYieldContractCommodityBerriesSyncDao();
		DeclaredYieldContractCommodityBerriesSyncDto newDto = new DeclaredYieldContractCommodityBerriesSyncDto();
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date date = cal.getTime();
		
		Date dataSyncTransDate = TestUtils.addSeconds(date, -120);

		String userId = "JUNIT_TEST";

		//INSERT

		newDto.setContractId(888888888);
		newDto.setCropCommodityId(10);
		newDto.setCropCommodityName("Blueberry");
		newDto.setCropYear(2020);
		newDto.setDeclaredYieldContractCommodityBerriesGuid(declaredYieldContractCommodityBerriesGuid);
		newDto.setDeclaredYieldContractGuid("3991fb6d0abe4696a3574b4d2837bbb4");
		newDto.setTotalAbandonmentYield(111.2222);
		newDto.setTotalProduction(333.4444);
		newDto.setTotalProductionOverride(555.6666);
		newDto.setTotalSalesYield(777.8888);
		newDto.setTotalSoldShippedYield(999.1111);		
		newDto.setDataSyncTransDate(dataSyncTransDate);

		dao.insert(newDto, userId);
		
		//FETCH
		DeclaredYieldContractCommodityBerriesSyncDto fetchedDto = dao.fetch(declaredYieldContractCommodityBerriesGuid);

		Assert.assertEquals("ContractId", newDto.getContractId(), fetchedDto.getContractId());
		Assert.assertEquals("CropCommodityId", newDto.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", newDto.getCropCommodityName(), fetchedDto.getCropCommodityName());
		Assert.assertEquals("CropYear", newDto.getCropYear(), fetchedDto.getCropYear());
		Assert.assertEquals("DeclaredYieldContractCommodityBerriesGuid", newDto.getDeclaredYieldContractCommodityBerriesGuid(), fetchedDto.getDeclaredYieldContractCommodityBerriesGuid());
		Assert.assertEquals("DeclaredYieldContractGuid", newDto.getDeclaredYieldContractGuid(), fetchedDto.getDeclaredYieldContractGuid());
		Assert.assertEquals("TotalAbandonmentYield", newDto.getTotalAbandonmentYield(), fetchedDto.getTotalAbandonmentYield());
		Assert.assertEquals("TotalProduction", newDto.getTotalProduction(), fetchedDto.getTotalProduction());
		Assert.assertEquals("TotalProductionOverride", newDto.getTotalProductionOverride(), fetchedDto.getTotalProductionOverride());
		Assert.assertEquals("TotalSalesYield", newDto.getTotalSalesYield(), fetchedDto.getTotalSalesYield());
		Assert.assertEquals("TotalSoldShippedYield", newDto.getTotalSoldShippedYield(), fetchedDto.getTotalSoldShippedYield());
		Assert.assertTrue("DataSyncTransDate", newDto.getDataSyncTransDate().compareTo(fetchedDto.getDataSyncTransDate()) == 0);
		
		
		//UPDATE
		dataSyncTransDate = TestUtils.addSeconds(date, -60);

		fetchedDto.setDeclaredYieldContractGuid("ab58408c1e624feb8bee84b4d8a6edc5");
		fetchedDto.setContractId(888888889);
		fetchedDto.setCropCommodityId(11);
		fetchedDto.setCropCommodityName("Cranberry");
		fetchedDto.setCropYear(2021);
		fetchedDto.setTotalAbandonmentYield(222.3333);
		fetchedDto.setTotalProduction(444.5555);
		fetchedDto.setTotalProductionOverride(777.8888);
		fetchedDto.setTotalSalesYield(999.1111);
		fetchedDto.setTotalSoldShippedYield(111.2222);
		fetchedDto.setDataSyncTransDate(dataSyncTransDate);
		
		dao.update(fetchedDto, userId);
		
		//FETCH
		DeclaredYieldContractCommodityBerriesSyncDto updatedDto = dao.fetch(declaredYieldContractCommodityBerriesGuid);

		Assert.assertEquals("ContractId", fetchedDto.getContractId(), updatedDto.getContractId());
		Assert.assertEquals("CropCommodityId", fetchedDto.getCropCommodityId(), updatedDto.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", fetchedDto.getCropCommodityName(), updatedDto.getCropCommodityName());
		Assert.assertEquals("CropYear", fetchedDto.getCropYear(), updatedDto.getCropYear());
		Assert.assertEquals("DeclaredYieldContractCommodityBerriesGuid", fetchedDto.getDeclaredYieldContractCommodityBerriesGuid(), updatedDto.getDeclaredYieldContractCommodityBerriesGuid());
		Assert.assertEquals("DeclaredYieldContractGuid", fetchedDto.getDeclaredYieldContractGuid(), updatedDto.getDeclaredYieldContractGuid());
		Assert.assertEquals("TotalAbandonmentYield", fetchedDto.getTotalAbandonmentYield(), updatedDto.getTotalAbandonmentYield());
		Assert.assertEquals("TotalProduction", fetchedDto.getTotalProduction(), updatedDto.getTotalProduction());
		Assert.assertEquals("TotalProductionOverride", fetchedDto.getTotalProductionOverride(), updatedDto.getTotalProductionOverride());
		Assert.assertEquals("TotalSalesYield", fetchedDto.getTotalSalesYield(), updatedDto.getTotalSalesYield());
		Assert.assertEquals("TotalSoldShippedYield", fetchedDto.getTotalSoldShippedYield(), updatedDto.getTotalSoldShippedYield());
		Assert.assertTrue("DataSyncTransDate", fetchedDto.getDataSyncTransDate().compareTo(updatedDto.getDataSyncTransDate()) == 0);

		//GetByContractCommodity
		updatedDto = dao.getByContractCommodity(888888889, 2021, 11);

		Assert.assertEquals("ContractId", fetchedDto.getContractId(), updatedDto.getContractId());
		Assert.assertEquals("CropCommodityId", fetchedDto.getCropCommodityId(), updatedDto.getCropCommodityId());
		Assert.assertEquals("CropCommodityName", fetchedDto.getCropCommodityName(), updatedDto.getCropCommodityName());
		Assert.assertEquals("CropYear", fetchedDto.getCropYear(), updatedDto.getCropYear());
		Assert.assertEquals("DeclaredYieldContractCommodityBerriesGuid", fetchedDto.getDeclaredYieldContractCommodityBerriesGuid(), updatedDto.getDeclaredYieldContractCommodityBerriesGuid());
		Assert.assertEquals("DeclaredYieldContractGuid", fetchedDto.getDeclaredYieldContractGuid(), updatedDto.getDeclaredYieldContractGuid());
		Assert.assertEquals("TotalAbandonmentYield", fetchedDto.getTotalAbandonmentYield(), updatedDto.getTotalAbandonmentYield());
		Assert.assertEquals("TotalProduction", fetchedDto.getTotalProduction(), updatedDto.getTotalProduction());
		Assert.assertEquals("TotalProductionOverride", fetchedDto.getTotalProductionOverride(), updatedDto.getTotalProductionOverride());
		Assert.assertEquals("TotalSalesYield", fetchedDto.getTotalSalesYield(), updatedDto.getTotalSalesYield());
		Assert.assertEquals("TotalSoldShippedYield", fetchedDto.getTotalSoldShippedYield(), updatedDto.getTotalSoldShippedYield());
		Assert.assertTrue("DataSyncTransDate", fetchedDto.getDataSyncTransDate().compareTo(updatedDto.getDataSyncTransDate()) == 0);
		
		
		//DELETE
		dao.delete(updatedDto.getDeclaredYieldContractCommodityBerriesGuid());
		DeclaredYieldContractCommodityBerriesSyncDto deletedDto = dao.fetch(declaredYieldContractCommodityBerriesGuid);
		Assert.assertNull(deletedDto);

	}
}
