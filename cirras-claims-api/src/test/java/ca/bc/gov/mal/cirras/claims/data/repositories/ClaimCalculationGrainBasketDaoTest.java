package ca.bc.gov.mal.cirras.claims.data.repositories;

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

import ca.bc.gov.mal.cirras.claims.data.entities.ClaimCalculationDto;
import ca.bc.gov.mal.cirras.claims.data.entities.ClaimCalculationGrainBasketDto;
import ca.bc.gov.mal.cirras.claims.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class ClaimCalculationGrainBasketDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private String userId = "JUNIT_TEST";
	private String claimCalculationGuid;
	private Integer claimNumber = 99998865;
	
	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		delete();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		delete();
	}
	
	private void delete() throws DaoException {

		ClaimCalculationDao dao = persistenceSpringConfig.claimCalculationDao();
		List<ClaimCalculationDto> ccDtos = dao.getCalculationsByClaimNumber(claimNumber, null);

		if(ccDtos != null && ccDtos.size() > 0) {

			ClaimCalculationGrainBasketDao daoGrain = persistenceSpringConfig.claimCalculationGrainBasketDao();
			
			for (ClaimCalculationDto dto : ccDtos) {
				//delete claim calculation grain unseeded
				daoGrain.deleteForClaim(dto.getClaimCalculationGuid());
				//delete claim calculation
				dao.delete(dto.getClaimCalculationGuid());
			}
		}
	}

	@Test 
	public void testInsertUpdateDeleteClaimCalculationGrainBasket() throws Exception {

		//Add claim calculation
		createClaimCalculation();
		
		ClaimCalculationGrainBasketDto newDto = new ClaimCalculationGrainBasketDto();
		
		Double grainBasketCoverageValue = 15000.0;
		Integer grainBasketDeductible = 30;
		Double grainBasketHarvestedValue = 10.0;
		Double quantityTotalCoverageValue = 11.0;
		Double quantityTotalYieldValue = 12.0;
		Double quantityTotalClaimAmount = 13.0;
		Double quantityTotalYieldLossIndemnity = 14.0;
		Double totalYieldCoverageValue = 15.0;
		Double totalYieldLoss = 16.0;


		newDto.setClaimCalculationGuid(claimCalculationGuid);
		newDto.setGrainBasketCoverageValue(grainBasketCoverageValue);
		newDto.setGrainBasketDeductible(grainBasketDeductible);
		newDto.setGrainBasketHarvestedValue(grainBasketHarvestedValue);
		newDto.setQuantityTotalCoverageValue(quantityTotalCoverageValue);
		newDto.setQuantityTotalYieldValue(quantityTotalYieldValue);
		newDto.setQuantityTotalClaimAmount(quantityTotalClaimAmount);
		newDto.setQuantityTotalYieldLossIndemnity(quantityTotalYieldLossIndemnity);
		newDto.setTotalYieldCoverageValue(totalYieldCoverageValue);
		newDto.setTotalYieldLoss(totalYieldLoss);

		ClaimCalculationGrainBasketDao dao = persistenceSpringConfig.claimCalculationGrainBasketDao();
		//INSERT
		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getClaimCalculationGuid()); 
		
		//FETCH
		ClaimCalculationGrainBasketDto fetchedDto = dao.fetch(newDto.getClaimCalculationGrainBasketGuid());
		
		assertGrainBasket(newDto, fetchedDto, true);

		//UPDATE
		fetchedDto.setGrainBasketCoverageValue(grainBasketCoverageValue + 1);
		fetchedDto.setGrainBasketDeductible(grainBasketDeductible + 1);
		fetchedDto.setGrainBasketHarvestedValue(grainBasketHarvestedValue + 1);
		fetchedDto.setQuantityTotalCoverageValue(quantityTotalCoverageValue + 1);
		fetchedDto.setQuantityTotalYieldValue(quantityTotalYieldValue + 1);
		fetchedDto.setQuantityTotalClaimAmount(quantityTotalClaimAmount + 1);
		fetchedDto.setQuantityTotalYieldLossIndemnity(quantityTotalYieldLossIndemnity + 1);
		fetchedDto.setTotalYieldCoverageValue(totalYieldCoverageValue + 1);
		fetchedDto.setTotalYieldLoss(totalYieldLoss + 1);
		
		dao.update(fetchedDto, userId);
		
		//FETCH
		ClaimCalculationGrainBasketDto updatedDto = dao.fetch(fetchedDto.getClaimCalculationGrainBasketGuid());

		assertGrainBasket(fetchedDto, updatedDto, false);
		
		//SELECT
		ClaimCalculationGrainBasketDto dto = dao.select(claimCalculationGuid);
		Assert.assertNotNull(dto);
		assertGrainBasket(dto, updatedDto, false);
		
		//SELECT ALL
		List<ClaimCalculationGrainBasketDto> dtos = dao.selectAll();
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());
		assertGrainBasket(dtos.get(0), updatedDto, false);
		
		//DELETE
		dao.delete(updatedDto.getClaimCalculationGrainBasketGuid());

	}

	@Test 
	public void testDeleteForClaimClaimCalculationGrainBasket() throws Exception {

		//Add claim calculation
		createClaimCalculation();
		
		ClaimCalculationGrainBasketDto newDto = new ClaimCalculationGrainBasketDto();

		Double grainBasketCoverageValue = 15000.0;
		Integer grainBasketDeductible = 30;
		Double grainBasketHarvestedValue = 10.0;
		Double quantityTotalCoverageValue = 11.0;
		Double quantityTotalYieldValue = 12.0;
		Double quantityTotalClaimAmount = 13.0;
		Double quantityTotalYieldLossIndemnity = 14.0;
		Double totalYieldCoverageValue = 15.0;
		Double totalYieldLoss = 16.0;


		newDto.setClaimCalculationGuid(claimCalculationGuid);
		newDto.setGrainBasketCoverageValue(grainBasketCoverageValue);
		newDto.setGrainBasketDeductible(grainBasketDeductible);
		newDto.setGrainBasketHarvestedValue(grainBasketHarvestedValue);
		newDto.setQuantityTotalCoverageValue(quantityTotalCoverageValue);
		newDto.setQuantityTotalYieldValue(quantityTotalYieldValue);
		newDto.setQuantityTotalClaimAmount(quantityTotalClaimAmount);
		newDto.setQuantityTotalYieldLossIndemnity(quantityTotalYieldLossIndemnity);
		newDto.setTotalYieldCoverageValue(totalYieldCoverageValue);
		newDto.setTotalYieldLoss(totalYieldLoss);

		ClaimCalculationGrainBasketDao dao = persistenceSpringConfig.claimCalculationGrainBasketDao();
		//INSERT
		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getClaimCalculationGuid()); 

		//SELECT
		ClaimCalculationGrainBasketDto dto = dao.select(claimCalculationGuid);
		Assert.assertNotNull(dto); //Not Null
		
		//DELETE FOR CLAIM
		dao.deleteForClaim(claimCalculationGuid);
		
		//SELECT
		dto = dao.select(claimCalculationGuid);
		Assert.assertNull(dto); //Null
	}
	
	private void assertGrainBasket(ClaimCalculationGrainBasketDto expectedDto, 
									 ClaimCalculationGrainBasketDto actualDto,
									 Boolean insertCheck) {
		if(insertCheck) {
			Assert.assertNotNull("ClaimCalculationGrainBasketGuid", actualDto.getClaimCalculationGrainBasketGuid());
		} else {
			Assert.assertEquals("ClaimCalculationGrainBasketGuid", expectedDto.getClaimCalculationGrainBasketGuid(), actualDto.getClaimCalculationGrainBasketGuid());
		}
		Assert.assertEquals("ClaimCalculationGuid", expectedDto.getClaimCalculationGuid(), actualDto.getClaimCalculationGuid());
		Assert.assertEquals("GrainBasketCoverageValue", expectedDto.getGrainBasketCoverageValue(), actualDto.getGrainBasketCoverageValue());
		Assert.assertEquals("GrainBasketDeductible", expectedDto.getGrainBasketDeductible(), actualDto.getGrainBasketDeductible());
		Assert.assertEquals("GrainBasketHarvestedValue", expectedDto.getGrainBasketHarvestedValue(), actualDto.getGrainBasketHarvestedValue());
		Assert.assertEquals("QuantityTotalCoverageValue", expectedDto.getQuantityTotalCoverageValue(), actualDto.getQuantityTotalCoverageValue());
		Assert.assertEquals("QuantityTotalYieldValue", expectedDto.getQuantityTotalYieldValue(), actualDto.getQuantityTotalYieldValue());
		Assert.assertEquals("QuantityTotalClaimAmount", expectedDto.getQuantityTotalClaimAmount(), actualDto.getQuantityTotalClaimAmount());
		Assert.assertEquals("QuantityTotalYieldLossIndemnity", expectedDto.getQuantityTotalYieldLossIndemnity(), actualDto.getQuantityTotalYieldLossIndemnity());
		Assert.assertEquals("TotalYieldCoverageValue", expectedDto.getTotalYieldCoverageValue(), actualDto.getTotalYieldCoverageValue());
		Assert.assertEquals("TotalYieldLoss", expectedDto.getTotalYieldLoss(), actualDto.getTotalYieldLoss());

	}
	
	private void createClaimCalculation() throws Exception {
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
		
		

		ClaimCalculationDao dao = persistenceSpringConfig.claimCalculationDao();
		//INSERT
		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getClaimCalculationGuid()); 
		
		//set claim Calculation GUID
		claimCalculationGuid = newDto.getClaimCalculationGuid();
	}	

}
