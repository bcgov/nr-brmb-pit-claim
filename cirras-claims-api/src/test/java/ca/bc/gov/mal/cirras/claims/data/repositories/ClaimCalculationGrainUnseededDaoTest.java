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
import ca.bc.gov.mal.cirras.claims.data.entities.ClaimCalculationGrainUnseededDto;
import ca.bc.gov.mal.cirras.claims.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class ClaimCalculationGrainUnseededDaoTest {
	
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

			ClaimCalculationGrainUnseededDao daoGrain = persistenceSpringConfig.claimCalculationGrainUnseededDao();
			
			for (ClaimCalculationDto dto : ccDtos) {
				//delete claim calculation grain unseeded
				daoGrain.deleteForClaim(dto.getClaimCalculationGuid());
				//delete claim calculation
				dao.delete(dto.getClaimCalculationGuid());
			}
		}
	}

	@Test 
	public void testInsertUpdateDeleteClaimCalculationGrainUnseeded() throws Exception {

		//Add claim calculation
		createClaimCalculation();
		
		ClaimCalculationGrainUnseededDto newDto = new ClaimCalculationGrainUnseededDto();

		newDto.setClaimCalculationGuid(claimCalculationGuid);
		newDto.setInsuredAcres(55.5);
		newDto.setLessAdjustmentAcres(11.1);
		newDto.setAdjustedAcres(44.4);
		newDto.setDeductibleLevel(20);
		newDto.setDeductibleAcres(22.2);
		newDto.setMaxEligibleAcres(33.3);
		newDto.setInsurableValue(50.1);
		newDto.setCoverageValue(34.5);
		newDto.setUnseededAcres(5.5);
		newDto.setLessAssessmentAcres(1.1);
		newDto.setEligibleUnseededAcres(4.4);

		ClaimCalculationGrainUnseededDao dao = persistenceSpringConfig.claimCalculationGrainUnseededDao();
		//INSERT
		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getClaimCalculationGuid()); 
		
		//FETCH
		ClaimCalculationGrainUnseededDto fetchedDto = dao.fetch(newDto.getClaimCalculationGrainUnseededGuid());
		
		assertGrainUnseeded(newDto, fetchedDto, true);

		//UPDATE
		fetchedDto.setInsuredAcres(55.8);
		fetchedDto.setLessAdjustmentAcres(11.8);
		fetchedDto.setAdjustedAcres(44.8);
		fetchedDto.setDeductibleLevel(30);
		fetchedDto.setDeductibleAcres(22.8);
		fetchedDto.setMaxEligibleAcres(33.8);
		fetchedDto.setInsurableValue(50.8);
		fetchedDto.setCoverageValue(34.8);
		fetchedDto.setUnseededAcres(5.8);
		fetchedDto.setLessAssessmentAcres(1.8);
		fetchedDto.setEligibleUnseededAcres(4.8);
		
		dao.update(fetchedDto, userId);
		
		//FETCH
		ClaimCalculationGrainUnseededDto updatedDto = dao.fetch(fetchedDto.getClaimCalculationGrainUnseededGuid());

		assertGrainUnseeded(fetchedDto, updatedDto, false);
		
		//SELECT
		ClaimCalculationGrainUnseededDto dto = dao.select(claimCalculationGuid);
		Assert.assertNotNull(dto);
		assertGrainUnseeded(dto, updatedDto, false);
		
		//SELECT ALL
		List<ClaimCalculationGrainUnseededDto> dtos = dao.selectAll();
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());
		assertGrainUnseeded(dtos.get(0), updatedDto, false);
		
		//DELETE
		dao.delete(updatedDto.getClaimCalculationGrainUnseededGuid());

	}

	@Test 
	public void testDeleteForClaimClaimCalculationGrainUnseeded() throws Exception {

		//Add claim calculation
		createClaimCalculation();
		
		ClaimCalculationGrainUnseededDto newDto = new ClaimCalculationGrainUnseededDto();

		newDto.setClaimCalculationGuid(claimCalculationGuid);
		newDto.setInsuredAcres(55.5);
		newDto.setLessAdjustmentAcres(11.1);
		newDto.setAdjustedAcres(44.4);
		newDto.setDeductibleLevel(20);
		newDto.setDeductibleAcres(22.2);
		newDto.setMaxEligibleAcres(33.3);
		newDto.setInsurableValue(50.1);
		newDto.setCoverageValue(34.5);
		newDto.setUnseededAcres(5.5);
		newDto.setLessAssessmentAcres(1.1);
		newDto.setEligibleUnseededAcres(4.4);

		ClaimCalculationGrainUnseededDao dao = persistenceSpringConfig.claimCalculationGrainUnseededDao();
		//INSERT
		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getClaimCalculationGuid()); 

		//SELECT
		ClaimCalculationGrainUnseededDto dto = dao.select(claimCalculationGuid);
		Assert.assertNotNull(dto); //Not Null
		
		//DELETE FOR CLAIM
		dao.deleteForClaim(claimCalculationGuid);
		
		//SELECT
		dto = dao.select(claimCalculationGuid);
		Assert.assertNull(dto); //Null
	}
	
	private void assertGrainUnseeded(ClaimCalculationGrainUnseededDto expectedDto, 
									 ClaimCalculationGrainUnseededDto actualDto,
									 Boolean insertCheck) {
		if(insertCheck) {
			Assert.assertNotNull("ClaimCalculationGrainUnseededGuid", actualDto.getClaimCalculationGrainUnseededGuid());
		} else {
			Assert.assertEquals("ClaimCalculationGrainUnseededGuid", expectedDto.getClaimCalculationGrainUnseededGuid(), actualDto.getClaimCalculationGrainUnseededGuid());
		}
		Assert.assertEquals("ClaimCalculationGuid", expectedDto.getClaimCalculationGuid(), actualDto.getClaimCalculationGuid());
		Assert.assertEquals("InsuredAcres", expectedDto.getInsuredAcres(), actualDto.getInsuredAcres());
		Assert.assertEquals("LessAdjustmentAcres", expectedDto.getLessAdjustmentAcres(), actualDto.getLessAdjustmentAcres());
		Assert.assertEquals("AdjustedAcres", expectedDto.getAdjustedAcres(), actualDto.getAdjustedAcres());
		Assert.assertEquals("DeductibleLevel", expectedDto.getDeductibleLevel(), actualDto.getDeductibleLevel());
		Assert.assertEquals("DeductibleAcres", expectedDto.getDeductibleAcres(), actualDto.getDeductibleAcres());
		Assert.assertEquals("MaxEligibleAcres", expectedDto.getMaxEligibleAcres(), actualDto.getMaxEligibleAcres());
		Assert.assertEquals("InsurableValue", expectedDto.getInsurableValue(), actualDto.getInsurableValue());
		Assert.assertEquals("CoverageValue", expectedDto.getCoverageValue(), actualDto.getCoverageValue());
		Assert.assertEquals("UnseededAcres", expectedDto.getUnseededAcres(), actualDto.getUnseededAcres());
		Assert.assertEquals("LessAssessmentAcres", expectedDto.getLessAssessmentAcres(), actualDto.getLessAssessmentAcres());
		Assert.assertEquals("EligibleUnseededAcres", expectedDto.getEligibleUnseededAcres(), actualDto.getEligibleUnseededAcres());
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
