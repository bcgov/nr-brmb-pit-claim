package ca.bc.gov.mal.cirras.claims.data.repositories;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimCalculationDao;
import ca.bc.gov.mal.cirras.claims.data.entities.ClaimCalculationDto;
import ca.bc.gov.mal.cirras.claims.data.entities.ClaimCalculationBerriesDto;
import ca.bc.gov.mal.cirras.claims.spring.PersistenceSpringConfig;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class ClaimCalculationBerriesDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;

	@Test 
	public void testInsertUpdateDeleteClaimCalculationBerries() throws Exception {

		//Get any existing calculation and add a plant unit record
		ClaimCalculationDto dto = getDto();
		
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
		Assert.assertNotNull(newDto.getClaimCalculationGuid()); 
		
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

		
		Assert.assertEquals("ClaimCalculationGuid", updatedDto.getClaimCalculationGuid(), updatedDto.getClaimCalculationGuid());
		Assert.assertEquals("TotalProbableYield", updatedDto.getTotalProbableYield(), updatedDto.getTotalProbableYield());
		Assert.assertEquals("DeductibleLevel", updatedDto.getDeductibleLevel(), updatedDto.getDeductibleLevel());
		Assert.assertEquals("ProductionGuarantee", updatedDto.getProductionGuarantee(), updatedDto.getProductionGuarantee());
		Assert.assertEquals("DeclaredAcres", updatedDto.getDeclaredAcres(), updatedDto.getDeclaredAcres());
		Assert.assertEquals("ConfirmedAcres", updatedDto.getConfirmedAcres(), updatedDto.getConfirmedAcres());
		Assert.assertEquals("AdjustmentFactor", updatedDto.getAdjustmentFactor(), updatedDto.getAdjustmentFactor());
		Assert.assertEquals("AdjustedProductionGuarantee", updatedDto.getAdjustedProductionGuarantee(), updatedDto.getAdjustedProductionGuarantee());
		Assert.assertEquals("InsurableValueSelected", updatedDto.getInsurableValueSelected(), updatedDto.getInsurableValueSelected());
		Assert.assertEquals("InsurableValueHundredPercent", updatedDto.getInsurableValueHundredPercent(), updatedDto.getInsurableValueHundredPercent());
		Assert.assertEquals("CoverageAmountAdjusted", updatedDto.getCoverageAmountAdjusted(), updatedDto.getCoverageAmountAdjusted());
		Assert.assertEquals("MaxCoverageAmount", updatedDto.getMaxCoverageAmount(), updatedDto.getMaxCoverageAmount());
		Assert.assertEquals("HarvestedYield", updatedDto.getHarvestedYield(), updatedDto.getHarvestedYield());
		Assert.assertEquals("AppraisedYield", updatedDto.getAppraisedYield(), updatedDto.getAppraisedYield());
		Assert.assertEquals("AbandonedYield", updatedDto.getAbandonedYield(), updatedDto.getAbandonedYield());
		Assert.assertEquals("TotalYieldFromDop", updatedDto.getTotalYieldFromDop(), updatedDto.getTotalYieldFromDop());
		Assert.assertEquals("TotalYieldFromAdjuster", updatedDto.getTotalYieldFromAdjuster(), updatedDto.getTotalYieldFromAdjuster());
		Assert.assertEquals("YieldAssessment", updatedDto.getYieldAssessment(), updatedDto.getYieldAssessment());
		Assert.assertEquals("TotalYieldForCalculation", updatedDto.getTotalYieldForCalculation(), updatedDto.getTotalYieldForCalculation());
		Assert.assertEquals("YieldLossEligible", updatedDto.getYieldLossEligible(), updatedDto.getYieldLossEligible());

		//DELETE
		dao.delete(updatedDto.getClaimCalculationBerriesGuid());

	}
	
	
	private ClaimCalculationDto getDto() throws Exception {
		String claimCalculationGuid = "0282C14368490524E0632FB3228E11C3";
		ClaimCalculationDao dao = persistenceSpringConfig.claimCalculationDao();
		ClaimCalculationDto dto = dao.fetch(claimCalculationGuid);
		
		return dto;
		
	}

	
	 
}
