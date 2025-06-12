package ca.bc.gov.mal.cirras.claims.persistence.v1.dao;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationGrainQuantityDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class ClaimCalculationGrainQuantityDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private String userId = "JUNIT_TEST";
	private String claimCalculationGrainQuantityGuid;
	private Integer claimNumber1 = 99778865;
	private Integer claimNumber2 = 99778866;
	
	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		delete();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		delete();
	}
	
	private void delete() throws DaoException {
		deleteCalculation(claimNumber1);
		deleteCalculation(claimNumber2);
		
		if ( claimCalculationGrainQuantityGuid != null ) {
			ClaimCalculationGrainQuantityDao dao = persistenceSpringConfig.claimCalculationGrainQuantityDao();
			ClaimCalculationGrainQuantityDto dto = dao.fetch(claimCalculationGrainQuantityGuid);
			if(dto != null) {
				dao.delete(claimCalculationGrainQuantityGuid);
			}
		}
	}
	
	private void deleteCalculation(Integer claimNumber) throws DaoException {

		ClaimCalculationDao dao = persistenceSpringConfig.claimCalculationDao();
		List<ClaimCalculationDto> ccDtos = dao.getCalculationsByClaimNumber(claimNumber, null);

		if(ccDtos != null && ccDtos.size() > 0) {

			ClaimCalculationGrainQuantityDao daoGrain = persistenceSpringConfig.claimCalculationGrainQuantityDao();
			
			for (ClaimCalculationDto dto : ccDtos) {
				//delete claim calculation first
				dao.delete(dto.getClaimCalculationGuid());
				
			}
		}
	}

	@Test 
	public void testInsertUpdateDeleteClaimCalculationGrainQuantity() throws Exception {
		
		ClaimCalculationGrainQuantityDto newDto = new ClaimCalculationGrainQuantityDto();

		newDto.setAdvancedClaim(12.34);
		newDto.setMaxClaimPayable(56.78);
		newDto.setProductionGuaranteeAmount(11.22);
		newDto.setQuantityLossClaim(33.44);
		newDto.setReseedClaim(55.66);
		newDto.setTotalCoverageValue(77.88);
		newDto.setTotalYieldLossValue(99.88);
		
		
		ClaimCalculationGrainQuantityDao dao = persistenceSpringConfig.claimCalculationGrainQuantityDao();

		//INSERT
		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getClaimCalculationGrainQuantityGuid());
		claimCalculationGrainQuantityGuid = newDto.getClaimCalculationGrainQuantityGuid();

		//Add claim calculation
		String claimCalculationGuid1 = createClaimCalculation(claimNumber1);
		
		//FETCH
		ClaimCalculationGrainQuantityDto fetchedDto = dao.fetch(newDto.getClaimCalculationGrainQuantityGuid());
		
		assertGrainQuantity(newDto, fetchedDto, true);

		//UPDATE
		fetchedDto.setAdvancedClaim(23.45);
		fetchedDto.setMaxClaimPayable(67.89);
		fetchedDto.setProductionGuaranteeAmount(22.33);
		fetchedDto.setQuantityLossClaim(44.55);
		fetchedDto.setReseedClaim(66.77);
		fetchedDto.setTotalCoverageValue(88.99);
		fetchedDto.setTotalYieldLossValue(88.77);
		
		dao.update(fetchedDto, userId);
		
		//FETCH
		ClaimCalculationGrainQuantityDto updatedDto = dao.fetch(fetchedDto.getClaimCalculationGrainQuantityGuid());

		assertGrainQuantity(fetchedDto, updatedDto, false);
		
		//SELECT
		ClaimCalculationGrainQuantityDto dto = dao.select(claimCalculationGuid1);
		Assert.assertNotNull(dto);
		assertGrainQuantity(dto, updatedDto, false);
		
		//SELECT ALL
		List<ClaimCalculationGrainQuantityDto> dtos = dao.selectAll();
		Assert.assertNotNull(dtos);
		
		//Generate second claim with shared data
		String claimCalculationGuid2 = createClaimCalculation(claimNumber2);
		
		//Get all calculations that are associated with the grain quantity record
		ClaimCalculationDao ccDao = persistenceSpringConfig.claimCalculationDao();
		List<ClaimCalculationDto> calculationsDto = ccDao.getCalculationsByGrainQuantityGuid(claimCalculationGrainQuantityGuid);
		Assert.assertNotNull(calculationsDto);
		Assert.assertEquals(2, calculationsDto.size());

		// Delete claim calc
		ccDao.delete(claimCalculationGuid1);
		ccDao.delete(claimCalculationGuid2);
		
		//DELETE
		dao.delete(updatedDto.getClaimCalculationGrainQuantityGuid());

		//FETCH
		dto = dao.fetch(updatedDto.getClaimCalculationGrainQuantityGuid());
		Assert.assertNull(dto);
	}

	
	private void assertGrainQuantity(ClaimCalculationGrainQuantityDto expectedDto, 
									 ClaimCalculationGrainQuantityDto actualDto,
									 Boolean insertCheck) {
		if(insertCheck) {
			Assert.assertNotNull("ClaimCalculationGrainQuantityGuid", actualDto.getClaimCalculationGrainQuantityGuid());
		} else {
			Assert.assertEquals("ClaimCalculationGrainQuantityGuid", expectedDto.getClaimCalculationGrainQuantityGuid(), actualDto.getClaimCalculationGrainQuantityGuid());
		}
		
		Assert.assertEquals("AdvancedClaim", expectedDto.getAdvancedClaim(), actualDto.getAdvancedClaim());
		Assert.assertEquals("MaxClaimPayable", expectedDto.getMaxClaimPayable(), actualDto.getMaxClaimPayable());
		Assert.assertEquals("ProductionGuaranteeAmount", expectedDto.getProductionGuaranteeAmount(), actualDto.getProductionGuaranteeAmount());
		Assert.assertEquals("QuantityLossClaim", expectedDto.getQuantityLossClaim(), actualDto.getQuantityLossClaim());
		Assert.assertEquals("ReseedClaim", expectedDto.getReseedClaim(), actualDto.getReseedClaim());
		Assert.assertEquals("TotalCoverageValue", expectedDto.getTotalCoverageValue(), actualDto.getTotalCoverageValue());
		Assert.assertEquals("TotalYieldLossValue", expectedDto.getTotalYieldLossValue(), actualDto.getTotalYieldLossValue());
	}
	
	private String createClaimCalculation(Integer claimNumber) throws Exception {
		ClaimCalculationDto newDto = new ClaimCalculationDto();

		Date transactionDate = new Date();
		newDto.setPrimaryPerilCode("DROUGHT");
		newDto.setSecondaryPerilCode("FIRE");
		newDto.setClaimStatusCode("OPEN");
		newDto.setCommodityCoverageCode("CQNT");
		newDto.setCalculationStatusCode("DRAFT");
		newDto.setInsurancePlanId(1);
		newDto.setCropCommodityId(1);
		newDto.setCropYear(2020);
		newDto.setInsuredByMeasurementType("ACRES");
		newDto.setPolicyNumber("100100-20");
		newDto.setContractId(1000);
		newDto.setClaimNumber(claimNumber);
		newDto.setCalculationVersion(1);
		newDto.setGrowerNumber(11111);
		newDto.setGrowerName("Name 1");
		newDto.setGrowerAddressLine1("Line 1");
		newDto.setGrowerAddressLine2("Line 2");
		newDto.setGrowerPostalCode("V1V1V1");
		newDto.setGrowerCity("Victoria");
		newDto.setGrowerProvince("BC");
		newDto.setTotalClaimAmount(15000.0);
		newDto.setCalculationComment("Test Comment");
		newDto.setSubmittedByUserid("user1");
		newDto.setSubmittedByName("user 1");
		newDto.setSubmittedByDate(transactionDate);
		newDto.setRecommendedByUserid("user2");
		newDto.setRecommendedByName("user 2");
		newDto.setRecommendedByDate(transactionDate);
		newDto.setApprovedByUserid("user3");
		newDto.setApprovedByName("user 3");
		newDto.setApprovedByDate(transactionDate);
		newDto.setCalculateIivInd("Y");
		newDto.setHasChequeReqInd(false);
		newDto.setClaimCalculationGrainQuantityGuid(claimCalculationGrainQuantityGuid);
		

		ClaimCalculationDao dao = persistenceSpringConfig.claimCalculationDao();
		//INSERT
		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getClaimCalculationGuid()); 
		
		//set claim Calculation GUID
		return newDto.getClaimCalculationGuid();
	}	

}
