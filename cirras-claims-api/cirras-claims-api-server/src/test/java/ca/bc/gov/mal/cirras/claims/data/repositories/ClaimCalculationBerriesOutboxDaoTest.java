package ca.bc.gov.mal.cirras.claims.data.repositories;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.bc.gov.mal.cirras.claims.data.entities.ClaimCalculationBerriesOutboxDto;
import ca.bc.gov.mal.cirras.claims.spring.PersistenceSpringConfig;
import ca.bc.gov.mal.cirras.claims.data.models.OutboxTransactionTypes;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class ClaimCalculationBerriesOutboxDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private String claimCalculationBerriesGuid = "abc1236843219";
	private Integer claimCalculationBerriesOutboxId1 = null;
	private Integer claimCalculationBerriesOutboxId2 = null;


	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		delete();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		delete();
	}
	
	private void delete() throws NotFoundDaoException, DaoException{

		deleteDyccbo(claimCalculationBerriesOutboxId1);
		deleteDyccbo(claimCalculationBerriesOutboxId2);
				
	}

	private void deleteDyccbo(Integer claimCalculationBerriesOutboxId) throws NotFoundDaoException, DaoException{
		//DELETE Declared Yield Contract Commodity Berries Outbox Records
		ClaimCalculationBerriesOutboxDao ccbDao = persistenceSpringConfig.claimCalculationBerriesOutboxDao();
		
		if (claimCalculationBerriesOutboxId != null) {
			ClaimCalculationBerriesOutboxDto dto = ccbDao.fetch(claimCalculationBerriesOutboxId);
			if (dto != null) {
				ccbDao.delete(claimCalculationBerriesOutboxId);
			}
		}
	}
	
	@Test 
	public void testClaimCalculationBerriesOutbox() throws Exception {

		String userId = "UNITTEST";
		
		ClaimCalculationBerriesOutboxDao dao = persistenceSpringConfig.claimCalculationBerriesOutboxDao();

		// INSERT
		ClaimCalculationBerriesOutboxDto newDto = new ClaimCalculationBerriesOutboxDto();
		newDto.setAuditTransactionTypeCode(OutboxTransactionTypes.Insert);
		newDto.setClaimCalculationBerriesGuid(claimCalculationBerriesGuid);

		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getClaimCalculationBerriesOutboxId());
		claimCalculationBerriesOutboxId1 = newDto.getClaimCalculationBerriesOutboxId();
		
		//FETCH
		ClaimCalculationBerriesOutboxDto fetchedDto = dao.fetch(claimCalculationBerriesOutboxId1);
		Assert.assertEquals("CcbObId", newDto.getClaimCalculationBerriesOutboxId(), fetchedDto.getClaimCalculationBerriesOutboxId());
		Assert.assertEquals("AuditTransactionTypeCode", newDto.getAuditTransactionTypeCode(), fetchedDto.getAuditTransactionTypeCode());
		Assert.assertEquals("ClaimCalculationBerriesGuid", newDto.getClaimCalculationBerriesGuid(), fetchedDto.getClaimCalculationBerriesGuid());
		
		//UPDATE
		fetchedDto.setAuditTransactionTypeCode(OutboxTransactionTypes.Update);
		
		dao.update(fetchedDto, userId);

		//FETCH
		ClaimCalculationBerriesOutboxDto updatedDto = dao.fetch(claimCalculationBerriesOutboxId1);

		Assert.assertEquals("AuditTransactionTypeCode", fetchedDto.getAuditTransactionTypeCode(), updatedDto.getAuditTransactionTypeCode());


		//INSERT second commodity
		ClaimCalculationBerriesOutboxDto newDto2 = new ClaimCalculationBerriesOutboxDto();
		newDto2.setAuditTransactionTypeCode(OutboxTransactionTypes.Insert);
		newDto2.setClaimCalculationBerriesGuid(claimCalculationBerriesGuid);

		dao.insert(newDto2, userId);
		Assert.assertNotNull(newDto2.getClaimCalculationBerriesOutboxId());
		claimCalculationBerriesOutboxId2 = newDto2.getClaimCalculationBerriesOutboxId();

		//SELECT
		List<ClaimCalculationBerriesOutboxDto> dtos = dao.select(1); //Only get 1 record
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size()); //Only expect one
		
		//DELETE 1
		dao.delete(claimCalculationBerriesOutboxId2);

		//FETCH
		ClaimCalculationBerriesOutboxDto deletedDto = dao.fetch(claimCalculationBerriesOutboxId2);
		Assert.assertNull(deletedDto);

		delete();
	}
	 
}
