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
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationGrainQuantityDetailDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationGrainSpotLossDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class ClaimCalculationGrainQuantityDetailDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private String userId = "JUNIT_TEST";
	private String claimCalculationGuid;
	private Integer claimNumber = 99778865;
	
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

			ClaimCalculationGrainQuantityDetailDao daoGrain = persistenceSpringConfig.claimCalculationGrainQuantityDetailDao();
			
			for (ClaimCalculationDto dto : ccDtos) {
				//delete claim calculation grain spot loss
				daoGrain.deleteForClaim(dto.getClaimCalculationGuid());
				//delete claim calculation
				dao.delete(dto.getClaimCalculationGuid());
			}
		}
	}

	@Test 
	public void testInsertUpdateDeleteClaimCalculationGrainQuantityDetail() throws Exception {

		//Add claim calculation
		createClaimCalculation();
		
		ClaimCalculationGrainQuantityDetailDto newDto = new ClaimCalculationGrainQuantityDetailDto();

		newDto.setClaimCalculationGuid(claimCalculationGuid);
		newDto.setAssessedYield(12.34);
		newDto.setCalcEarlyEstYield(56.78);
		newDto.setCoverageValue(11.22);
		newDto.setDamagedAcres(33.44);
		newDto.setDeductible(10);
		newDto.setEarlyEstDeemedYieldValue(55.66);
		newDto.setFiftyPercentProductionGuarantee(77.88);
		newDto.setInspEarlyEstYield(99.11);
		newDto.setInsurableValue(98.76);
		newDto.setInsuredAcres(54.32);
		newDto.setProbableYield(99.88);
		newDto.setProductionGuaranteeWeight(77.66);
		newDto.setSeededAcres(55.44);
		newDto.setTotalYieldToCount(33.22);
		newDto.setYieldValue(11.99);
		newDto.setYieldValueWithEarlyEstDeemedYield(11.33);
		
		
		ClaimCalculationGrainQuantityDetailDao dao = persistenceSpringConfig.claimCalculationGrainQuantityDetailDao();
		//INSERT
		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getClaimCalculationGuid()); 
		
		//FETCH
		ClaimCalculationGrainQuantityDetailDto fetchedDto = dao.fetch(newDto.getClaimCalculationGrainQuantityDetailGuid());
		
		assertGrainQuantityDetail(newDto, fetchedDto, true);

		//UPDATE
		fetchedDto.setAssessedYield(23.45);
		fetchedDto.setCalcEarlyEstYield(67.89);
		fetchedDto.setCoverageValue(22.33);
		fetchedDto.setDamagedAcres(44.55);
		fetchedDto.setDeductible(20);
		fetchedDto.setEarlyEstDeemedYieldValue(66.77);
		fetchedDto.setFiftyPercentProductionGuarantee(88.99);
		fetchedDto.setInspEarlyEstYield(11.22);
		fetchedDto.setInsurableValue(87.65);
		fetchedDto.setInsuredAcres(43.21);
		fetchedDto.setProbableYield(88.77);
		fetchedDto.setProductionGuaranteeWeight(66.55);
		fetchedDto.setSeededAcres(44.33);
		fetchedDto.setTotalYieldToCount(22.11);
		fetchedDto.setYieldValue(99.88);
		fetchedDto.setYieldValueWithEarlyEstDeemedYield(99.11);
		
		dao.update(fetchedDto, userId);
		
		//FETCH
		ClaimCalculationGrainQuantityDetailDto updatedDto = dao.fetch(fetchedDto.getClaimCalculationGrainQuantityDetailGuid());

		assertGrainQuantityDetail(fetchedDto, updatedDto, false);
		
		//SELECT
		ClaimCalculationGrainQuantityDetailDto dto = dao.select(claimCalculationGuid);
		Assert.assertNotNull(dto);
		assertGrainQuantityDetail(dto, updatedDto, false);
		
		//SELECT ALL
		List<ClaimCalculationGrainQuantityDetailDto> dtos = dao.selectAll();
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());
		assertGrainQuantityDetail(dtos.get(0), updatedDto, false);
		
		//DELETE
		dao.delete(updatedDto.getClaimCalculationGrainQuantityDetailGuid());

		//FETCH
		dto = dao.fetch(updatedDto.getClaimCalculationGrainQuantityDetailGuid());
		Assert.assertNull(dto);
	
	}

	@Test 
	public void testDeleteForClaimClaimCalculationGrainQuantityDetail() throws Exception {

		//Add claim calculation
		createClaimCalculation();
		
		ClaimCalculationGrainQuantityDetailDto newDto = new ClaimCalculationGrainQuantityDetailDto();

		newDto.setClaimCalculationGuid(claimCalculationGuid);
		newDto.setAssessedYield(12.34);
		newDto.setCalcEarlyEstYield(56.78);
		newDto.setCoverageValue(11.22);
		newDto.setDamagedAcres(33.44);
		newDto.setDeductible(10);
		newDto.setEarlyEstDeemedYieldValue(55.66);
		newDto.setFiftyPercentProductionGuarantee(77.88);
		newDto.setInspEarlyEstYield(99.11);
		newDto.setInsurableValue(98.76);
		newDto.setInsuredAcres(54.32);
		newDto.setProbableYield(99.88);
		newDto.setProductionGuaranteeWeight(77.66);
		newDto.setSeededAcres(55.44);
		newDto.setTotalYieldToCount(33.22);
		newDto.setYieldValue(11.99);
		newDto.setYieldValueWithEarlyEstDeemedYield(11.33);
		
		ClaimCalculationGrainQuantityDetailDao dao = persistenceSpringConfig.claimCalculationGrainQuantityDetailDao();

		//INSERT
		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getClaimCalculationGuid()); 

		//SELECT
		ClaimCalculationGrainQuantityDetailDto dto = dao.select(claimCalculationGuid);
		Assert.assertNotNull(dto); //Not Null
		
		//DELETE FOR CLAIM
		dao.deleteForClaim(claimCalculationGuid);
		
		//SELECT
		dto = dao.select(claimCalculationGuid);
		Assert.assertNull(dto); //Null
	}
	
	private void assertGrainQuantityDetail(ClaimCalculationGrainQuantityDetailDto expectedDto, 
									 ClaimCalculationGrainQuantityDetailDto actualDto,
									 Boolean insertCheck) {
		if(insertCheck) {
			Assert.assertNotNull("ClaimCalculationGrainQuantityDetailGuid", actualDto.getClaimCalculationGrainQuantityDetailGuid());
		} else {
			Assert.assertEquals("ClaimCalculationGrainQuantityDetailGuid", expectedDto.getClaimCalculationGrainQuantityDetailGuid(), actualDto.getClaimCalculationGrainQuantityDetailGuid());
		}
		
		Assert.assertEquals("ClaimCalculationGuid", expectedDto.getClaimCalculationGuid(), actualDto.getClaimCalculationGuid());
		Assert.assertEquals("AssessedYield", expectedDto.getAssessedYield(), actualDto.getAssessedYield());
		Assert.assertEquals("CalcEarlyEstYield", expectedDto.getCalcEarlyEstYield(), actualDto.getCalcEarlyEstYield());
		Assert.assertEquals("CoverageValue", expectedDto.getCoverageValue(), actualDto.getCoverageValue());
		Assert.assertEquals("DamagedAcres", expectedDto.getDamagedAcres(), actualDto.getDamagedAcres());
		Assert.assertEquals("Deductible", expectedDto.getDeductible(), actualDto.getDeductible());
		Assert.assertEquals("EarlyEstDeemedYieldValue", expectedDto.getEarlyEstDeemedYieldValue(), actualDto.getEarlyEstDeemedYieldValue());
		Assert.assertEquals("FiftyPercentProductionGuarantee", expectedDto.getFiftyPercentProductionGuarantee(), actualDto.getFiftyPercentProductionGuarantee());
		Assert.assertEquals("InspEarlyEstYield", expectedDto.getInspEarlyEstYield(), actualDto.getInspEarlyEstYield());
		Assert.assertEquals("InsurableValue", expectedDto.getInsurableValue(), actualDto.getInsurableValue());
		Assert.assertEquals("InsuredAcres", expectedDto.getInsuredAcres(), actualDto.getInsuredAcres());
		Assert.assertEquals("ProbableYield", expectedDto.getProbableYield(), actualDto.getProbableYield());
		Assert.assertEquals("ProductionGuaranteeWeight", expectedDto.getProductionGuaranteeWeight(), actualDto.getProductionGuaranteeWeight());
		Assert.assertEquals("SeededAcres", expectedDto.getSeededAcres(), actualDto.getSeededAcres());
		Assert.assertEquals("TotalYieldToCount", expectedDto.getTotalYieldToCount(), actualDto.getTotalYieldToCount());
		Assert.assertEquals("YieldValue", expectedDto.getYieldValue(), actualDto.getYieldValue());
		Assert.assertEquals("YieldValueWithEarlyEstDeemedYield", expectedDto.getYieldValueWithEarlyEstDeemedYield(), actualDto.getYieldValueWithEarlyEstDeemedYield());
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
