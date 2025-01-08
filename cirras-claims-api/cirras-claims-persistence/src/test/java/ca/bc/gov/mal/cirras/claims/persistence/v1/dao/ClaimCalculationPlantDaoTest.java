package ca.bc.gov.mal.cirras.claims.persistence.v1.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationPlantAcresDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationPlantUnitsDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dto.PagedDtos;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class ClaimCalculationPlantDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	
	@Test 
	public void testInsertUpdateDeleteClaimCalculationPlantUnits() throws Exception {

		//Get any existing calculation and add a plant unit record
		ClaimCalculationDto dto = getDto();
		
		ClaimCalculationPlantUnitsDto newDto = new ClaimCalculationPlantUnitsDto();

		newDto.setClaimCalculationGuid(dto.getClaimCalculationGuid());
		newDto.setInsuredUnits((double)1000);
		newDto.setLessAdjustmentReason("Less Adjustment");
		newDto.setLessAdjustmentUnits(50);
		newDto.setAdjustedUnits((double)950);
		newDto.setDeductibleLevel(10);
		newDto.setDeductibleUnits((double)95);
		newDto.setTotalCoverageUnits((double)855);
		newDto.setInsurableValue((double)3.5);
		newDto.setCoverageAmount((double)8000);
		newDto.setDamagedUnits(700);
		newDto.setLessAssessmentReason("Less Assessment");
		newDto.setLessAssessmentUnits(10);
		newDto.setTotalDamagedUnits((double)690);
		
		String userId = "JUNIT_TEST";

		ClaimCalculationPlantUnitsDao dao = persistenceSpringConfig.claimCalculationPlantUnitsDao();
		//INSERT
		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getClaimCalculationGuid()); 
		
		//FETCH
		ClaimCalculationPlantUnitsDto fetchedDto = dao.fetch(newDto.getClaimCalcPlantUnitsGuid());
		
		Assert.assertEquals("ClaimCalculationGuid", newDto.getClaimCalculationGuid(), fetchedDto.getClaimCalculationGuid());
		Assert.assertEquals("InsuredUnits", newDto.getInsuredUnits(), fetchedDto.getInsuredUnits());
		Assert.assertEquals("LessAdjustmentReason", newDto.getLessAdjustmentReason(), fetchedDto.getLessAdjustmentReason());
		Assert.assertEquals("LessAdjustmentUnits", newDto.getLessAdjustmentUnits(), fetchedDto.getLessAdjustmentUnits());
		Assert.assertEquals("AdjustedUnits", newDto.getAdjustedUnits(), fetchedDto.getAdjustedUnits());
		Assert.assertEquals("DeductibleLevel", newDto.getDeductibleLevel(), fetchedDto.getDeductibleLevel());
		Assert.assertEquals("DeductibleUnits", newDto.getDeductibleUnits(), fetchedDto.getDeductibleUnits());
		Assert.assertEquals("TotalCoverageUnits", newDto.getTotalCoverageUnits(), fetchedDto.getTotalCoverageUnits());
		Assert.assertEquals("InsurableValue", newDto.getInsurableValue(), fetchedDto.getInsurableValue());
		Assert.assertEquals("CoverageAmount", newDto.getCoverageAmount(), fetchedDto.getCoverageAmount());
		Assert.assertEquals("DamagedUnits", newDto.getDamagedUnits(), fetchedDto.getDamagedUnits());
		Assert.assertEquals("LessAssessmentReason", newDto.getLessAssessmentReason(), fetchedDto.getLessAssessmentReason());
		Assert.assertEquals("LessAssessmentUnits", newDto.getLessAssessmentUnits(), fetchedDto.getLessAssessmentUnits());
		Assert.assertEquals("TotalDamagedUnits", newDto.getTotalDamagedUnits(), fetchedDto.getTotalDamagedUnits());
	
		
		//UPDATE
		fetchedDto.setInsuredUnits((double)1001);
		fetchedDto.setLessAdjustmentReason("Less Adjustment 2");
		fetchedDto.setLessAdjustmentUnits(55);
		fetchedDto.setAdjustedUnits((double)946);
		fetchedDto.setDeductibleLevel(5);
		fetchedDto.setDeductibleUnits((double)88);
		fetchedDto.setTotalCoverageUnits((double)844);
		fetchedDto.setInsurableValue((double)1.4);
		fetchedDto.setCoverageAmount((double)9000);
		fetchedDto.setDamagedUnits(660);
		fetchedDto.setLessAssessmentReason("Less Assessment 2");
		fetchedDto.setLessAssessmentUnits(100);
		fetchedDto.setTotalDamagedUnits((double)560);
		
		dao.update(fetchedDto, userId);
		
		//FETCH
		ClaimCalculationPlantUnitsDto updatedDto = dao.fetch(fetchedDto.getClaimCalcPlantUnitsGuid());
		
		Assert.assertEquals("ClaimCalcPlantUnitsGuid", fetchedDto.getClaimCalcPlantUnitsGuid(), updatedDto.getClaimCalcPlantUnitsGuid());
		Assert.assertEquals("ClaimCalculationGuid", fetchedDto.getClaimCalculationGuid(), updatedDto.getClaimCalculationGuid());
		Assert.assertEquals("InsuredUnits", fetchedDto.getInsuredUnits(), updatedDto.getInsuredUnits());
		Assert.assertEquals("LessAdjustmentReason", fetchedDto.getLessAdjustmentReason(), updatedDto.getLessAdjustmentReason());
		Assert.assertEquals("LessAdjustmentUnits", fetchedDto.getLessAdjustmentUnits(), updatedDto.getLessAdjustmentUnits());
		Assert.assertEquals("AdjustedUnits", fetchedDto.getAdjustedUnits(), updatedDto.getAdjustedUnits());
		Assert.assertEquals("DeductibleLevel", fetchedDto.getDeductibleLevel(), updatedDto.getDeductibleLevel());
		Assert.assertEquals("DeductibleUnits", fetchedDto.getDeductibleUnits(), updatedDto.getDeductibleUnits());
		Assert.assertEquals("TotalCoverageUnits", fetchedDto.getTotalCoverageUnits(), updatedDto.getTotalCoverageUnits());
		Assert.assertEquals("InsurableValue", fetchedDto.getInsurableValue(), updatedDto.getInsurableValue());
		Assert.assertEquals("CoverageAmount", fetchedDto.getCoverageAmount(), updatedDto.getCoverageAmount());
		Assert.assertEquals("DamagedUnits", fetchedDto.getDamagedUnits(), updatedDto.getDamagedUnits());
		Assert.assertEquals("LessAssessmentReason", fetchedDto.getLessAssessmentReason(), updatedDto.getLessAssessmentReason());
		Assert.assertEquals("LessAssessmentUnits", fetchedDto.getLessAssessmentUnits(), updatedDto.getLessAssessmentUnits());
		Assert.assertEquals("TotalDamagedUnits", fetchedDto.getTotalDamagedUnits(), updatedDto.getTotalDamagedUnits());
		
		//DELETE
		dao.delete(updatedDto.getClaimCalcPlantUnitsGuid());

	}

	@Test 
	public void testInsertUpdateDeleteClaimCalculationPlantAcres() throws Exception {

		//Get any existing calculation and add a plant unit record
		ClaimCalculationDto dto = getDto();
		
		ClaimCalculationPlantAcresDto newDto = new ClaimCalculationPlantAcresDto();

		newDto.setClaimCalculationGuid(dto.getClaimCalculationGuid());
		newDto.setDeclaredAcres((double)1000);
		newDto.setConfirmedAcres((double)900);
		newDto.setInsuredAcres((double)800);
		newDto.setDeductibleLevel(10);
		newDto.setDeductibleAcres((double)80);
		newDto.setTotalCoverageAcres((double)720);
		newDto.setDamagedAcres((double)600);
		newDto.setAcresLossCovered((double)580);
		newDto.setInsurableValue((double)3400);
		newDto.setCoverageAmount((double)10000);
		newDto.setLessAssessmentReason("Less Assessment");
		newDto.setLessAssessmentAmount((double)1500);

		String userId = "JUNIT_TEST";

		ClaimCalculationPlantAcresDao dao = persistenceSpringConfig.claimCalculationPlantAcresDao();
		//INSERT
		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getClaimCalculationGuid()); 
		
		//FETCH
		ClaimCalculationPlantAcresDto fetchedDto = dao.fetch(newDto.getClaimCalcPlantAcresGuid());
		
		Assert.assertEquals("ClaimCalculationGuid", newDto.getClaimCalculationGuid(), fetchedDto.getClaimCalculationGuid());
		Assert.assertEquals("DeclaredAcres", newDto.getDeclaredAcres(), fetchedDto.getDeclaredAcres());
		Assert.assertEquals("ConfirmedAcres", newDto.getConfirmedAcres(), fetchedDto.getConfirmedAcres());
		Assert.assertEquals("InsuredAcres", newDto.getInsuredAcres(), fetchedDto.getInsuredAcres());
		Assert.assertEquals("DeductibleLevel", newDto.getDeductibleLevel(), fetchedDto.getDeductibleLevel());
		Assert.assertEquals("DeductibleAcres", newDto.getDeductibleAcres(), fetchedDto.getDeductibleAcres());
		Assert.assertEquals("TotalCoverageAcres", newDto.getTotalCoverageAcres(), fetchedDto.getTotalCoverageAcres());
		Assert.assertEquals("DamagedAcres", newDto.getDamagedAcres(), fetchedDto.getDamagedAcres());
		Assert.assertEquals("AcresLossCovered", newDto.getAcresLossCovered(), fetchedDto.getAcresLossCovered());
		Assert.assertEquals("InsurableValue", newDto.getInsurableValue(), fetchedDto.getInsurableValue());
		Assert.assertEquals("CoverageAmount", newDto.getCoverageAmount(), fetchedDto.getCoverageAmount());
		Assert.assertEquals("LessAssessmentReason", newDto.getLessAssessmentReason(), fetchedDto.getLessAssessmentReason());
		Assert.assertEquals("LessAssessmentAmount", newDto.getLessAssessmentAmount(), fetchedDto.getLessAssessmentAmount());

		
		//UPDATE
		fetchedDto.setDeclaredAcres((double)999);
		fetchedDto.setConfirmedAcres((double)899);
		fetchedDto.setInsuredAcres((double)799);
		fetchedDto.setDeductibleLevel(5);
		fetchedDto.setDeductibleAcres((double)70);
		fetchedDto.setTotalCoverageAcres((double)730);
		fetchedDto.setDamagedAcres((double)650);
		fetchedDto.setAcresLossCovered((double)540);
		fetchedDto.setInsurableValue((double)300);
		fetchedDto.setCoverageAmount((double)90000);
		fetchedDto.setLessAssessmentReason("Less Assessment 2");
		fetchedDto.setLessAssessmentAmount((double)1400);
		
		dao.update(fetchedDto, userId);
		
		//FETCH
		ClaimCalculationPlantAcresDto updatedDto = dao.fetch(fetchedDto.getClaimCalcPlantAcresGuid());

		Assert.assertEquals("ClaimCalcPlantAcresGuid", fetchedDto.getClaimCalcPlantAcresGuid(), updatedDto.getClaimCalcPlantAcresGuid());
		Assert.assertEquals("ClaimCalculationGuid", fetchedDto.getClaimCalculationGuid(), updatedDto.getClaimCalculationGuid());
		Assert.assertEquals("DeclaredAcres", fetchedDto.getDeclaredAcres(), updatedDto.getDeclaredAcres());
		Assert.assertEquals("ConfirmedAcres", fetchedDto.getConfirmedAcres(), updatedDto.getConfirmedAcres());
		Assert.assertEquals("InsuredAcres", fetchedDto.getInsuredAcres(), updatedDto.getInsuredAcres());
		Assert.assertEquals("DeductibleLevel", fetchedDto.getDeductibleLevel(), updatedDto.getDeductibleLevel());
		Assert.assertEquals("DeductibleAcres", fetchedDto.getDeductibleAcres(), updatedDto.getDeductibleAcres());
		Assert.assertEquals("TotalCoverageAcres", fetchedDto.getTotalCoverageAcres(), updatedDto.getTotalCoverageAcres());
		Assert.assertEquals("DamagedAcres", fetchedDto.getDamagedAcres(), updatedDto.getDamagedAcres());
		Assert.assertEquals("AcresLossCovered", fetchedDto.getAcresLossCovered(), updatedDto.getAcresLossCovered());
		Assert.assertEquals("InsurableValue", fetchedDto.getInsurableValue(), updatedDto.getInsurableValue());
		Assert.assertEquals("CoverageAmount", fetchedDto.getCoverageAmount(), updatedDto.getCoverageAmount());
		Assert.assertEquals("LessAssessmentReason", fetchedDto.getLessAssessmentReason(), updatedDto.getLessAssessmentReason());
		Assert.assertEquals("LessAssessmentAmount", fetchedDto.getLessAssessmentAmount(), updatedDto.getLessAssessmentAmount());

		//DELETE
		dao.delete(updatedDto.getClaimCalcPlantAcresGuid());

	}
	
	
	private ClaimCalculationDto getDto() throws Exception {
		String claimCalculationGuid = "0282C14368490524E0632FB3228E11C3";
		ClaimCalculationDao dao = persistenceSpringConfig.claimCalculationDao();
		ClaimCalculationDto dto = dao.fetch(claimCalculationGuid);
		
		return dto;
		
	}

	
	 
}
