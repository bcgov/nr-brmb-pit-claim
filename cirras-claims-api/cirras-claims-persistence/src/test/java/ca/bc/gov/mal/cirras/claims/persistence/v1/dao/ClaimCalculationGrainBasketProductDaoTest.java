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
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationGrainBasketProductDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class ClaimCalculationGrainBasketProductDaoTest {
	
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

			ClaimCalculationGrainBasketProductDao daoGrain = persistenceSpringConfig.claimCalculationGrainBasketProductDao();
			
			for (ClaimCalculationDto dto : ccDtos) {
				//delete claim calculation grain unseeded
				daoGrain.deleteForClaim(dto.getClaimCalculationGuid());
				//delete claim calculation
				dao.delete(dto.getClaimCalculationGuid());
			}
		}
	}

	@Test 
	public void testInsertUpdateDeleteClaimCalculationGrainBasketProduct() throws Exception {

		//Add claim calculation
		createClaimCalculation();
		
		ClaimCalculationGrainBasketProductDto newDto = new ClaimCalculationGrainBasketProductDto();
		
		Double hundredPercentInsurableValue = 10.0;
		Double insurableValue = 11.0;
		Double productionGuarantee = 12.0;
		Double coverageValue = 13.0;
		Double totalYieldToCount = 14.0;
		Double assessedYield = 15.0;
		Double quantityClaimAmount = 16.0;
		Double yieldValue = 17.0;

		Integer cropCommodityId = 27;
		String cropCommodityName = "Wheat - Pedigreed";
		Boolean isPedigreeInd = true;

		newDto.setClaimCalculationGuid(claimCalculationGuid);
		newDto.setHundredPercentInsurableValue(hundredPercentInsurableValue);
		newDto.setInsurableValue(insurableValue);
		newDto.setProductionGuarantee(productionGuarantee);
		newDto.setCoverageValue(coverageValue);
		newDto.setTotalYieldToCount(totalYieldToCount);
		newDto.setAssessedYield(assessedYield);
		newDto.setQuantityClaimAmount(quantityClaimAmount);
		newDto.setYieldValue(yieldValue);
		newDto.setCropCommodityId(cropCommodityId);
		newDto.setCropCommodityName(cropCommodityName);
		newDto.setIsPedigreeInd(isPedigreeInd);

		ClaimCalculationGrainBasketProductDao dao = persistenceSpringConfig.claimCalculationGrainBasketProductDao();
		//INSERT
		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getClaimCalculationGuid()); 
		
		//FETCH
		ClaimCalculationGrainBasketProductDto fetchedDto = dao.fetch(newDto.getClaimCalcGrainBasketProductGuid());
		
		assertGrainBasketProduct(newDto, fetchedDto, true);

		//UPDATE
		fetchedDto.setHundredPercentInsurableValue(hundredPercentInsurableValue + 1);
		fetchedDto.setInsurableValue(insurableValue + 1);
		fetchedDto.setProductionGuarantee(productionGuarantee + 1);
		fetchedDto.setCoverageValue(coverageValue + 1);
		fetchedDto.setTotalYieldToCount(totalYieldToCount + 1);
		fetchedDto.setAssessedYield(assessedYield + 1);
		fetchedDto.setQuantityClaimAmount(quantityClaimAmount + 1);
		fetchedDto.setYieldValue(yieldValue + 1);
		//Commodity is not updated
		//fetchedDto.setCropCommodityId(cropCommodityId);
		//fetchedDto.setCropCommodityName(cropCommodityName);
		//fetchedDto.setIsPedigreeInd(isPedigreeInd);		
		
		dao.update(fetchedDto, userId);
		
		//FETCH
		ClaimCalculationGrainBasketProductDto updatedDto = dao.fetch(fetchedDto.getClaimCalcGrainBasketProductGuid());

		assertGrainBasketProduct(fetchedDto, updatedDto, false);
		
		//SELECT
		ClaimCalculationGrainBasketProductDto dto = dao.select(claimCalculationGuid);
		Assert.assertNotNull(dto);
		assertGrainBasketProduct(dto, updatedDto, false);
		
		//SELECT ALL
		List<ClaimCalculationGrainBasketProductDto> dtos = dao.selectAll();
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());
		
		//DELETE
		dao.delete(updatedDto.getClaimCalcGrainBasketProductGuid());

	}

	@Test 
	public void testDeleteForClaimClaimCalculationGrainBasketProduct() throws Exception {

		//Add claim calculation
		createClaimCalculation();
		
		ClaimCalculationGrainBasketProductDto newDto = new ClaimCalculationGrainBasketProductDto();

		Double hundredPercentInsurableValue = 10.0;
		Double insurableValue = 11.0;
		Double productionGuarantee = 12.0;
		Double coverageValue = 13.0;
		Double totalYieldToCount = 14.0;
		Double assessedYield = 15.0;
		Double quantityClaimAmount = 16.0;
		Double yieldValue = 17.0;

		Integer cropCommodityId = 27;
		String cropCommodityName = "Wheat - Pedigreed";
		Boolean isPedigreeInd = true;

		newDto.setClaimCalculationGuid(claimCalculationGuid);
		newDto.setHundredPercentInsurableValue(hundredPercentInsurableValue);
		newDto.setInsurableValue(insurableValue);
		newDto.setProductionGuarantee(productionGuarantee);
		newDto.setCoverageValue(coverageValue);
		newDto.setTotalYieldToCount(totalYieldToCount);
		newDto.setAssessedYield(assessedYield);
		newDto.setQuantityClaimAmount(quantityClaimAmount);
		newDto.setYieldValue(yieldValue);
		newDto.setCropCommodityId(cropCommodityId);
		newDto.setCropCommodityName(cropCommodityName);
		newDto.setIsPedigreeInd(isPedigreeInd);

		ClaimCalculationGrainBasketProductDao dao = persistenceSpringConfig.claimCalculationGrainBasketProductDao();
		//INSERT
		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getClaimCalculationGuid()); 

		//SELECT
		ClaimCalculationGrainBasketProductDto dto = dao.select(claimCalculationGuid);
		Assert.assertNotNull(dto); //Not Null
		
		//DELETE FOR CLAIM
		dao.deleteForClaim(claimCalculationGuid);
		
		//SELECT
		dto = dao.select(claimCalculationGuid);
		Assert.assertNull(dto); //Null
	}
	
	private void assertGrainBasketProduct(ClaimCalculationGrainBasketProductDto expectedDto, 
									 ClaimCalculationGrainBasketProductDto actualDto,
									 Boolean insertCheck) {
		if(insertCheck) {
			Assert.assertNotNull("ClaimCalcGrainBasketProductGuid", actualDto.getClaimCalcGrainBasketProductGuid());
		} else {
			Assert.assertEquals("ClaimCalcGrainBasketProductGuid", expectedDto.getClaimCalcGrainBasketProductGuid(), actualDto.getClaimCalcGrainBasketProductGuid());
		}
		Assert.assertEquals("ClaimCalculationGuid", expectedDto.getClaimCalculationGuid(), actualDto.getClaimCalculationGuid());
		Assert.assertEquals("CropCommodityId", expectedDto.getCropCommodityId(), actualDto.getCropCommodityId());
		Assert.assertEquals("HundredPercentInsurableValue", expectedDto.getHundredPercentInsurableValue(), actualDto.getHundredPercentInsurableValue());
		Assert.assertEquals("InsurableValue", expectedDto.getInsurableValue(), actualDto.getInsurableValue());
		Assert.assertEquals("ProductionGuarantee", expectedDto.getProductionGuarantee(), actualDto.getProductionGuarantee());
		Assert.assertEquals("CoverageValue", expectedDto.getCoverageValue(), actualDto.getCoverageValue());
		Assert.assertEquals("TotalYieldToCount", expectedDto.getTotalYieldToCount(), actualDto.getTotalYieldToCount());
		Assert.assertEquals("AssessedYield", expectedDto.getAssessedYield(), actualDto.getAssessedYield());
		Assert.assertEquals("QuantityClaimAmount", expectedDto.getQuantityClaimAmount(), actualDto.getQuantityClaimAmount());
		Assert.assertEquals("YieldValue", expectedDto.getYieldValue(), actualDto.getYieldValue());
		Assert.assertEquals("CropCommodityName", expectedDto.getCropCommodityName(), actualDto.getCropCommodityName());
		Assert.assertEquals("IsPedigreeInd", expectedDto.getIsPedigreeInd(), actualDto.getIsPedigreeInd());

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
