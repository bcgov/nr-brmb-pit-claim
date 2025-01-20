package ca.bc.gov.mal.cirras.claims.persistence.v1.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationGrapesDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.spring.PersistenceSpringConfig;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class ClaimCalculationGrapesDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;

	@Test 
	public void testInsertUpdateDeleteClaimCalculationGrapes() throws Exception {

		//Get any existing calculation and add a plant unit record
		ClaimCalculationDto dto = getDto();
		
		ClaimCalculationGrapesDto newDto = new ClaimCalculationGrapesDto();

		newDto.setClaimCalculationGuid(dto.getClaimCalculationGuid());
		newDto.setInsurableValueSelected(0.5);
		newDto.setInsurableValueHundredPercent(0.8230);
		newDto.setCoverageAmount((double)100000);
		newDto.setCoverageAmountAssessed(99000.5);
		newDto.setCoverageAssessedReason("test");
		newDto.setCoverageAmountAdjusted(88000.5);
		newDto.setTotalProductionValue(77000.5);


		String userId = "JUNIT_TEST";

		ClaimCalculationGrapesDao dao = persistenceSpringConfig.claimCalculationGrapesDao();
		//INSERT
		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getClaimCalculationGuid()); 
		
		//FETCH
		ClaimCalculationGrapesDto fetchedDto = dao.fetch(newDto.getClaimCalculationGrapesGuid());
		
		Assert.assertEquals("ClaimCalculationGuid", newDto.getClaimCalculationGuid(), fetchedDto.getClaimCalculationGuid());
		Assert.assertEquals("InsurableValueSelected", newDto.getInsurableValueSelected(), fetchedDto.getInsurableValueSelected());
		Assert.assertEquals("InsurableValueHundredPercent", newDto.getInsurableValueHundredPercent(), fetchedDto.getInsurableValueHundredPercent());
		Assert.assertEquals("CoverageAmount", newDto.getCoverageAmount(), fetchedDto.getCoverageAmount());
		Assert.assertEquals("CoverageAmountAssessed", newDto.getCoverageAmountAssessed(), fetchedDto.getCoverageAmountAssessed());
		Assert.assertEquals("CoverageAssessedReason", newDto.getCoverageAssessedReason(), fetchedDto.getCoverageAssessedReason());
		Assert.assertEquals("CoverageAmountAdjusted", newDto.getCoverageAmountAdjusted(), fetchedDto.getCoverageAmountAdjusted());
		Assert.assertEquals("TotalProductionValue", newDto.getTotalProductionValue(), fetchedDto.getTotalProductionValue());

		//UPDATE
		fetchedDto.setInsurableValueSelected(1.5);
		fetchedDto.setInsurableValueHundredPercent(2.8230);
		fetchedDto.setCoverageAmount((double)100001);
		fetchedDto.setCoverageAmountAssessed(99001.5);
		fetchedDto.setCoverageAssessedReason("test 2");
		fetchedDto.setCoverageAmountAdjusted(88001.5);
		fetchedDto.setTotalProductionValue(77001.5);
		
		dao.update(fetchedDto, userId);
		
		//FETCH
		ClaimCalculationGrapesDto updatedDto = dao.fetch(fetchedDto.getClaimCalculationGrapesGuid());

		Assert.assertEquals("ClaimCalculationGuid", fetchedDto.getClaimCalculationGuid(), updatedDto.getClaimCalculationGuid());
		Assert.assertEquals("InsurableValueSelected", fetchedDto.getInsurableValueSelected(), updatedDto.getInsurableValueSelected());
		Assert.assertEquals("InsurableValueHundredPercent", fetchedDto.getInsurableValueHundredPercent(), updatedDto.getInsurableValueHundredPercent());
		Assert.assertEquals("CoverageAmount", fetchedDto.getCoverageAmount(), updatedDto.getCoverageAmount());
		Assert.assertEquals("CoverageAmountAssessed", fetchedDto.getCoverageAmountAssessed(), updatedDto.getCoverageAmountAssessed());
		Assert.assertEquals("CoverageAssessedReason", fetchedDto.getCoverageAssessedReason(), updatedDto.getCoverageAssessedReason());
		Assert.assertEquals("CoverageAmountAdjusted", fetchedDto.getCoverageAmountAdjusted(), updatedDto.getCoverageAmountAdjusted());
		Assert.assertEquals("TotalProductionValue", fetchedDto.getTotalProductionValue(), updatedDto.getTotalProductionValue());

		//DELETE
		dao.delete(updatedDto.getClaimCalculationGrapesGuid());

	}
	
	
	private ClaimCalculationDto getDto() throws Exception {
		String claimCalculationGuid = "0282C14368490524E0632FB3228E11C3";
		ClaimCalculationDao dao = persistenceSpringConfig.claimCalculationDao();
		ClaimCalculationDto dto = dao.fetch(claimCalculationGuid);
		
		return dto;
		
	}
}
