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

import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimCalculationDao;
import ca.bc.gov.mal.cirras.claims.data.entities.ClaimCalculationDto;
import ca.bc.gov.mal.cirras.claims.data.entities.ClaimCalculationBerriesDto;
import ca.bc.gov.mal.cirras.claims.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class ClaimCalculationBerriesDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private Integer claimNumber = 99778865;
	private String claimCalculationBerriesGuid = null;

	private Integer contractId = 253216515;
	private Integer cropCommodityId = 10;
	private Integer calculationVersion = 1;
	private Integer cropYear = 2020;
	private String calculationStatusCode = "DRAFT";
	
	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		delete();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		delete();
	}
	
	private void delete() throws DaoException {
		
		if (claimCalculationBerriesGuid != null) {
			ClaimCalculationBerriesDao dao = persistenceSpringConfig.claimCalculationBerriesDao();
			ClaimCalculationBerriesDto dto = dao.fetch(claimCalculationBerriesGuid);
			if(dto != null) {
				dao.delete(claimCalculationBerriesGuid);
			}
		}

		ClaimCalculationDao dao = persistenceSpringConfig.claimCalculationDao();
		List<ClaimCalculationDto> ccDtos = dao.getCalculationsByClaimNumber(claimNumber, null);

		if(ccDtos != null && ccDtos.size() > 0) {
			for (ClaimCalculationDto dto : ccDtos) {
				//delete claim calculation
				dao.delete(dto.getClaimCalculationGuid());
			}
		}
	}

	@Test 
	public void testInsertUpdateDeleteClaimCalculationBerries() throws Exception {

		//Get any existing calculation and add a plant unit record
		ClaimCalculationDto dto = createClaimCalculation();
		
		ClaimCalculationBerriesDto newDto = new ClaimCalculationBerriesDto();

		newDto.setClaimCalculationGuid(dto.getClaimCalculationGuid());
		newDto.setTotalProbableYield(100000.0);
		newDto.setDeductibleLevel(50);
		newDto.setProductionGuarantee(50000.0);
		newDto.setDeclaredAcres(14.7);
		newDto.setConfirmedAcres(13.3);
		newDto.setAdjustmentFactor(1.0);
		newDto.setAdjustedProductionGuarantee(40000.0);
		newDto.setInsurableValueSelected(0.5);
		newDto.setInsurableValueHundredPercent(0.8);
		newDto.setMaxCoverageAmount((double)35000);
		newDto.setCoverageAmountAdjusted((double)30000);
		newDto.setHarvestedYield((double)10000);
		newDto.setAppraisedYield((double)5000);
		newDto.setAbandonedYield((double)3000);
		newDto.setTotalYieldFromDop((double)38000);
		newDto.setTotalYieldFromAdjuster((double)40000);
		newDto.setYieldAssessment((double)4000);
		newDto.setTotalYieldForCalculation((double)44000);
		newDto.setYieldLossEligible((double)20000);


		String userId = "JUNIT_TEST";

		ClaimCalculationBerriesDao dao = persistenceSpringConfig.claimCalculationBerriesDao();
		//INSERT
		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getClaimCalculationBerriesGuid()); 

		claimCalculationBerriesGuid = newDto.getClaimCalculationBerriesGuid();
				
		//FETCH
		ClaimCalculationBerriesDto fetchedDto = dao.fetch(newDto.getClaimCalculationBerriesGuid());
		
		Assert.assertEquals("ClaimCalculationGuid", newDto.getClaimCalculationGuid(), fetchedDto.getClaimCalculationGuid());
		Assert.assertEquals("TotalProbableYield", newDto.getTotalProbableYield(), fetchedDto.getTotalProbableYield());
		Assert.assertEquals("DeductibleLevel", newDto.getDeductibleLevel(), fetchedDto.getDeductibleLevel());
		Assert.assertEquals("ProductionGuarantee", newDto.getProductionGuarantee(), fetchedDto.getProductionGuarantee());
		Assert.assertEquals("DeclaredAcres", newDto.getDeclaredAcres(), fetchedDto.getDeclaredAcres());
		Assert.assertEquals("ConfirmedAcres", newDto.getConfirmedAcres(), fetchedDto.getConfirmedAcres());
		Assert.assertEquals("AdjustmentFactor", newDto.getAdjustmentFactor(), fetchedDto.getAdjustmentFactor());
		Assert.assertEquals("AdjustedProductionGuarantee", newDto.getAdjustedProductionGuarantee(), fetchedDto.getAdjustedProductionGuarantee());
		Assert.assertEquals("InsurableValueSelected", newDto.getInsurableValueSelected(), fetchedDto.getInsurableValueSelected());
		Assert.assertEquals("InsurableValueHundredPercent", newDto.getInsurableValueHundredPercent(), fetchedDto.getInsurableValueHundredPercent());
		Assert.assertEquals("CoverageAmountAdjusted", newDto.getCoverageAmountAdjusted(), fetchedDto.getCoverageAmountAdjusted());
		Assert.assertEquals("MaxCoverageAmount", newDto.getMaxCoverageAmount(), fetchedDto.getMaxCoverageAmount());
		Assert.assertEquals("HarvestedYield", newDto.getHarvestedYield(), fetchedDto.getHarvestedYield());
		Assert.assertEquals("AppraisedYield", newDto.getAppraisedYield(), fetchedDto.getAppraisedYield());
		Assert.assertEquals("AbandonedYield", newDto.getAbandonedYield(), fetchedDto.getAbandonedYield());
		Assert.assertEquals("TotalYieldFromDop", newDto.getTotalYieldFromDop(), fetchedDto.getTotalYieldFromDop());
		Assert.assertEquals("TotalYieldFromAdjuster", newDto.getTotalYieldFromAdjuster(), fetchedDto.getTotalYieldFromAdjuster());
		Assert.assertEquals("YieldAssessment", newDto.getYieldAssessment(), fetchedDto.getYieldAssessment());
		Assert.assertEquals("TotalYieldForCalculation", newDto.getTotalYieldForCalculation(), fetchedDto.getTotalYieldForCalculation());
		Assert.assertEquals("YieldLossEligible", newDto.getYieldLossEligible(), fetchedDto.getYieldLossEligible());
		Assert.assertEquals("CropCommodityId", cropCommodityId, fetchedDto.getCropCommodityId());
		Assert.assertEquals("CropYear", cropYear, fetchedDto.getCropYear());
		Assert.assertEquals("ContractId", contractId, fetchedDto.getContractId());
		Assert.assertEquals("CalculationVersion", calculationVersion, fetchedDto.getCalculationVersion());
		Assert.assertEquals("CalculationStatusCode", calculationStatusCode, fetchedDto.getCalculationStatusCode());

		//UPDATE
		fetchedDto.setClaimCalculationGuid(dto.getClaimCalculationGuid());
		fetchedDto.setTotalProbableYield(100001.0);
		fetchedDto.setDeductibleLevel(51);
		fetchedDto.setProductionGuarantee(50001.0);
		fetchedDto.setDeclaredAcres(15.1);
		fetchedDto.setConfirmedAcres(14.1);
		fetchedDto.setAdjustmentFactor(1.1);
		fetchedDto.setAdjustedProductionGuarantee(40001.0);
		fetchedDto.setInsurableValueSelected(1.5);
		fetchedDto.setInsurableValueHundredPercent(1.8);
		fetchedDto.setCoverageAmountAdjusted((double)30001);
		fetchedDto.setMaxCoverageAmount((double)35001);
		fetchedDto.setHarvestedYield((double)10001);
		fetchedDto.setAppraisedYield((double)5001);
		fetchedDto.setAbandonedYield((double)3001);
		fetchedDto.setTotalYieldFromDop((double)38001);
		fetchedDto.setTotalYieldFromAdjuster((double)40001);
		fetchedDto.setYieldAssessment((double)4001);
		fetchedDto.setTotalYieldForCalculation((double)44001);
		fetchedDto.setYieldLossEligible((double)20001);		
		
		dao.update(fetchedDto, userId);
		
		//FETCH
		ClaimCalculationBerriesDto updatedDto = dao.fetch(fetchedDto.getClaimCalculationBerriesGuid());
		
		Assert.assertEquals("ClaimCalculationBerriesGuid", fetchedDto.getClaimCalculationBerriesGuid(), updatedDto.getClaimCalculationBerriesGuid());
		Assert.assertEquals("ClaimCalculationGuid", fetchedDto.getClaimCalculationGuid(), updatedDto.getClaimCalculationGuid());
		Assert.assertEquals("TotalProbableYield", fetchedDto.getTotalProbableYield(), updatedDto.getTotalProbableYield());
		Assert.assertEquals("DeductibleLevel", fetchedDto.getDeductibleLevel(), updatedDto.getDeductibleLevel());
		Assert.assertEquals("ProductionGuarantee", fetchedDto.getProductionGuarantee(), updatedDto.getProductionGuarantee());
		Assert.assertEquals("DeclaredAcres", fetchedDto.getDeclaredAcres(), updatedDto.getDeclaredAcres());
		Assert.assertEquals("ConfirmedAcres", fetchedDto.getConfirmedAcres(), updatedDto.getConfirmedAcres());
		Assert.assertEquals("AdjustmentFactor", fetchedDto.getAdjustmentFactor(), updatedDto.getAdjustmentFactor());
		Assert.assertEquals("AdjustedProductionGuarantee", fetchedDto.getAdjustedProductionGuarantee(), updatedDto.getAdjustedProductionGuarantee());
		Assert.assertEquals("InsurableValueSelected", fetchedDto.getInsurableValueSelected(), updatedDto.getInsurableValueSelected());
		Assert.assertEquals("InsurableValueHundredPercent", fetchedDto.getInsurableValueHundredPercent(), updatedDto.getInsurableValueHundredPercent());
		Assert.assertEquals("CoverageAmountAdjusted", fetchedDto.getCoverageAmountAdjusted(), updatedDto.getCoverageAmountAdjusted());
		Assert.assertEquals("MaxCoverageAmount", fetchedDto.getMaxCoverageAmount(), updatedDto.getMaxCoverageAmount());
		Assert.assertEquals("HarvestedYield", fetchedDto.getHarvestedYield(), updatedDto.getHarvestedYield());
		Assert.assertEquals("AppraisedYield", fetchedDto.getAppraisedYield(), updatedDto.getAppraisedYield());
		Assert.assertEquals("AbandonedYield", fetchedDto.getAbandonedYield(), updatedDto.getAbandonedYield());
		Assert.assertEquals("TotalYieldFromDop", fetchedDto.getTotalYieldFromDop(), updatedDto.getTotalYieldFromDop());
		Assert.assertEquals("TotalYieldFromAdjuster", fetchedDto.getTotalYieldFromAdjuster(), updatedDto.getTotalYieldFromAdjuster());
		Assert.assertEquals("YieldAssessment", fetchedDto.getYieldAssessment(), updatedDto.getYieldAssessment());
		Assert.assertEquals("TotalYieldForCalculation", fetchedDto.getTotalYieldForCalculation(), updatedDto.getTotalYieldForCalculation());
		Assert.assertEquals("YieldLossEligible", fetchedDto.getYieldLossEligible(), updatedDto.getYieldLossEligible());
		Assert.assertEquals("CropCommodityId", cropCommodityId, updatedDto.getCropCommodityId());
		Assert.assertEquals("CropYear", cropYear, updatedDto.getCropYear());
		Assert.assertEquals("ContractId", contractId, updatedDto.getContractId());
		Assert.assertEquals("CalculationVersion", calculationVersion, updatedDto.getCalculationVersion());
		Assert.assertEquals("CalculationStatusCode", calculationStatusCode, updatedDto.getCalculationStatusCode());

		//DELETE
		dao.delete(updatedDto.getClaimCalculationBerriesGuid());
		
		ClaimCalculationBerriesDto deletedDto = dao.fetch(claimCalculationBerriesGuid);
		Assert.assertNull(deletedDto);

	}
	
	private ClaimCalculationDto createClaimCalculation() throws DaoException {
		ClaimCalculationDto newDto = new ClaimCalculationDto();

		Date transactionDate = new Date();
		newDto.setPrimaryPerilCode("DROUGHT");
		newDto.setSecondaryPerilCode("FIRE");
		newDto.setClaimStatusCode("OPEN");
		newDto.setCommodityCoverageCode("CQNT");
		newDto.setCalculationStatusCode(calculationStatusCode );
		newDto.setInsurancePlanId(1);
		newDto.setCropCommodityId(cropCommodityId);
		newDto.setCropYear(cropYear);
		newDto.setInsuredByMeasurementType("ACRES");
		newDto.setPolicyNumber("100100-20");
		newDto.setContractId(contractId);
		newDto.setClaimNumber(claimNumber);
		newDto.setCalculationVersion(calculationVersion);
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
		newDto.setHasChequeReqInd(true);
		newDto.setClaimCalculationGrainQuantityGuid(null);
		newDto.setIsPedigreeInd(false);
		
		String userId = "JUNIT_TEST";

		ClaimCalculationDao dao = persistenceSpringConfig.claimCalculationDao();
		//INSERT
		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getClaimCalculationGuid()); 
		
		return newDto;
		
	}
	
	private ClaimCalculationDto getDto() throws Exception {
		String claimCalculationGuid = "0282C14368490524E0632FB3228E11C3";
		ClaimCalculationDao dao = persistenceSpringConfig.claimCalculationDao();
		ClaimCalculationDto dto = dao.fetch(claimCalculationGuid);
		
		return dto;
		
	}

	
	 
}
