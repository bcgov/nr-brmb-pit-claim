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
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationGrainSpotLossDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class ClaimCalculationGrainSpotLossDaoTest {
	
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

			ClaimCalculationGrainSpotLossDao daoGrain = persistenceSpringConfig.claimCalculationGrainSpotLossDao();
			
			for (ClaimCalculationDto dto : ccDtos) {
				//delete claim calculation grain spot loss
				daoGrain.deleteForClaim(dto.getClaimCalculationGuid());
				//delete claim calculation
				dao.delete(dto.getClaimCalculationGuid());
			}
		}
	}

	@Test 
	public void testInsertUpdateDeleteClaimCalculationGrainSpotLoss() throws Exception {

		//Add claim calculation
		createClaimCalculation();
		
		ClaimCalculationGrainSpotLossDto newDto = new ClaimCalculationGrainSpotLossDto();

		newDto.setClaimCalculationGuid(claimCalculationGuid);
		newDto.setInsuredAcres(100.0);
		newDto.setCoverageAmtPerAcre(400.0);
		newDto.setCoverageValue(40000.0);
		newDto.setAdjustedAcres(48.0);
		newDto.setPercentYieldReduction(10.0);
		newDto.setEligibleYieldReduction(4.0);
		newDto.setSpotLossReductionValue(2080.0);
		newDto.setDeductible(5);
		
		ClaimCalculationGrainSpotLossDao dao = persistenceSpringConfig.claimCalculationGrainSpotLossDao();
		//INSERT
		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getClaimCalculationGuid()); 
		
		//FETCH
		ClaimCalculationGrainSpotLossDto fetchedDto = dao.fetch(newDto.getClaimCalculationGrainSpotLossGuid());
		
		assertGrainSpotLoss(newDto, fetchedDto, true);

		//UPDATE
		fetchedDto.setInsuredAcres(100.8);
		fetchedDto.setCoverageAmtPerAcre(400.8);
		fetchedDto.setCoverageValue(40000.8);
		fetchedDto.setAdjustedAcres(48.8);
		fetchedDto.setPercentYieldReduction(10.8);
		fetchedDto.setEligibleYieldReduction(4.8);
		fetchedDto.setSpotLossReductionValue(2080.8);
		fetchedDto.setDeductible(8);
		
		dao.update(fetchedDto, userId);
		
		//FETCH
		ClaimCalculationGrainSpotLossDto updatedDto = dao.fetch(fetchedDto.getClaimCalculationGrainSpotLossGuid());

		assertGrainSpotLoss(fetchedDto, updatedDto, false);
		
		//SELECT
		ClaimCalculationGrainSpotLossDto dto = dao.select(claimCalculationGuid);
		Assert.assertNotNull(dto);
		assertGrainSpotLoss(dto, updatedDto, false);
		
		//SELECT ALL
		List<ClaimCalculationGrainSpotLossDto> dtos = dao.selectAll();
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());
		assertGrainSpotLoss(dtos.get(0), updatedDto, false);
		
		//DELETE
		dao.delete(updatedDto.getClaimCalculationGrainSpotLossGuid());

	}

	@Test 
	public void testDeleteForClaimClaimCalculationGrainSpotLoss() throws Exception {

		//Add claim calculation
		createClaimCalculation();
		
		ClaimCalculationGrainSpotLossDto newDto = new ClaimCalculationGrainSpotLossDto();

		newDto.setClaimCalculationGuid(claimCalculationGuid);
		newDto.setInsuredAcres(100.0);
		newDto.setCoverageAmtPerAcre(400.0);
		newDto.setCoverageValue(40000.0);
		newDto.setAdjustedAcres(48.0);
		newDto.setPercentYieldReduction(10.0);
		newDto.setEligibleYieldReduction(4.0);
		newDto.setSpotLossReductionValue(2080.0);
		newDto.setDeductible(5);
		
		ClaimCalculationGrainSpotLossDao dao = persistenceSpringConfig.claimCalculationGrainSpotLossDao();
		//INSERT
		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getClaimCalculationGuid()); 

		//SELECT
		ClaimCalculationGrainSpotLossDto dto = dao.select(claimCalculationGuid);
		Assert.assertNotNull(dto); //Not Null
		
		//DELETE FOR CLAIM
		dao.deleteForClaim(claimCalculationGuid);
		
		//SELECT
		dto = dao.select(claimCalculationGuid);
		Assert.assertNull(dto); //Null
	}
	
	private void assertGrainSpotLoss(ClaimCalculationGrainSpotLossDto expectedDto, 
									 ClaimCalculationGrainSpotLossDto actualDto,
									 Boolean insertCheck) {
		if(insertCheck) {
			Assert.assertNotNull("ClaimCalculationGrainSpotLossGuid", actualDto.getClaimCalculationGrainSpotLossGuid());
		} else {
			Assert.assertEquals("ClaimCalculationGrainSpotLossGuid", expectedDto.getClaimCalculationGrainSpotLossGuid(), actualDto.getClaimCalculationGrainSpotLossGuid());
		}
		
		Assert.assertEquals("ClaimCalculationGuid", expectedDto.getClaimCalculationGuid(), actualDto.getClaimCalculationGuid());
		Assert.assertEquals("InsuredAcres", expectedDto.getInsuredAcres(), actualDto.getInsuredAcres());
		Assert.assertEquals("CoverageAmtPerAcre", expectedDto.getCoverageAmtPerAcre(), actualDto.getCoverageAmtPerAcre());
		Assert.assertEquals("CoverageValue", expectedDto.getCoverageValue(), actualDto.getCoverageValue());
		Assert.assertEquals("AdjustedAcres", expectedDto.getAdjustedAcres(), actualDto.getAdjustedAcres());
		Assert.assertEquals("PercentYieldReduction", expectedDto.getPercentYieldReduction(), actualDto.getPercentYieldReduction());
		Assert.assertEquals("EligibleYieldReduction", expectedDto.getEligibleYieldReduction(), actualDto.getEligibleYieldReduction());
		Assert.assertEquals("SpotLossReductionValue", expectedDto.getSpotLossReductionValue(), actualDto.getSpotLossReductionValue());
		Assert.assertEquals("Deductible", expectedDto.getDeductible(), actualDto.getDeductible());
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
