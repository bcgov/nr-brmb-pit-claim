package ca.bc.gov.mal.cirras.claims.data.repositories;

import java.util.List;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimCalculationVarietyDao;
import ca.bc.gov.mal.cirras.claims.data.entities.ClaimCalculationVarietyDto;
import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimCalculationDao;
import ca.bc.gov.mal.cirras.claims.data.entities.ClaimCalculationDto;
import ca.bc.gov.mal.cirras.claims.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class ClaimCalculationVarietyDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private String claimCalculationGuid;
	private String claimCalculationVarietyGuid1;
	private String claimCalculationVarietyGuid2;
	private String userId = "JUNIT_TEST";

	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		delete();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		delete();
	}
	
	private void delete() throws DaoException {
		//delete claim calculation variety
		if(claimCalculationVarietyGuid1 != null) {
			ClaimCalculationVarietyDao dao = persistenceSpringConfig.claimCalculationVarietyDao();
			ClaimCalculationVarietyDto dto = dao.fetch(claimCalculationVarietyGuid1);
			if (dto != null) {
				dao.delete(claimCalculationVarietyGuid1);
			}
			
			//Cleanup others
			if(claimCalculationGuid != null) {
				dao.deleteForClaim(claimCalculationGuid);
			}

		}
		
		//delete claim calculation
		if(claimCalculationGuid != null) {
			ClaimCalculationDao dao = persistenceSpringConfig.claimCalculationDao();
			ClaimCalculationDto dto = dao.fetch(claimCalculationGuid);
			if (dto != null) {
				dao.delete(claimCalculationGuid);
			}
			
			
		}
	}
	
	private void cleanUpInsertUpdateDelete(String claimCalculationGuid) throws Exception {

		ClaimCalculationVarietyDao daoVariety = persistenceSpringConfig.claimCalculationVarietyDao();
		daoVariety.deleteForClaim(claimCalculationGuid);

		ClaimCalculationDao daoCalculation = persistenceSpringConfig.claimCalculationDao();
		daoCalculation.delete(claimCalculationGuid);

	}
	
	@Test
	public void testInsertUpdateDeleteVarieties() throws Exception{

		Integer cropVarietyId = 1010336;
		Double averagePrice = 1.123;
		Double averagePriceOverride = 2.321;
		Double averagePriceFinal = 2.333;
		Double insurableValue = 1.024;
		String yieldAssessedReason = "Test Reason";
		Double yieldAssessed = 123.4;
		Double yieldTotal = 234.5;
		Double yieldActual = 255.1;
		Double varietyProductionValue = 280.1;
		
		//Insert claim calculation
		createClaimCalculation();
		
		//Add variety
		claimCalculationVarietyGuid1 = addVariety(cropVarietyId,
					averagePrice,
					averagePriceOverride,
					averagePriceFinal,
					insurableValue,
					yieldAssessedReason,
					yieldAssessed,
					yieldTotal,
					yieldActual,
					varietyProductionValue);
		
		//Get varieties and update them
		ClaimCalculationVarietyDao dao = persistenceSpringConfig.claimCalculationVarietyDao();

		
		ClaimCalculationVarietyDto fetchedDto = dao.fetch(claimCalculationVarietyGuid1);
		
		Assert.assertEquals("ClaimCalculationVarietyGuid", claimCalculationVarietyGuid1, fetchedDto.getClaimCalculationVarietyGuid());
		Assert.assertEquals("ClaimCalculationGuid", claimCalculationGuid, fetchedDto.getClaimCalculationGuid());
		Assert.assertEquals("CropVarietyId", cropVarietyId, fetchedDto.getCropVarietyId());
		Assert.assertEquals("RevisionCount", (Integer)1, fetchedDto.getRevisionCount());
		Assert.assertEquals("AveragePrice", averagePrice, fetchedDto.getAveragePrice());
		Assert.assertEquals("AveragePriceOverride", averagePriceOverride, fetchedDto.getAveragePriceOverride());
		Assert.assertEquals("AveragePriceFinal", averagePriceFinal, fetchedDto.getAveragePriceFinal());
		Assert.assertEquals("InsurableValue", insurableValue, fetchedDto.getInsurableValue());
		Assert.assertEquals("YieldAssessedReason", yieldAssessedReason, fetchedDto.getYieldAssessedReason());
		Assert.assertEquals("YieldAssessed", yieldAssessed, fetchedDto.getYieldAssessed());
		Assert.assertEquals("YieldTotal", yieldTotal, fetchedDto.getYieldTotal());
		Assert.assertEquals("YieldActual", yieldActual, fetchedDto.getYieldActual());
		Assert.assertEquals("VarietyProductionValue", varietyProductionValue, fetchedDto.getVarietyProductionValue());

		//UPDATE
		fetchedDto.setAveragePrice(averagePrice + 1);
		fetchedDto.setAveragePriceOverride(averagePriceOverride + 1);
		fetchedDto.setAveragePriceFinal(averagePriceFinal + 1);
		fetchedDto.setInsurableValue(insurableValue + 1);
		fetchedDto.setYieldAssessedReason(yieldAssessedReason + " updated");
		fetchedDto.setYieldAssessed(yieldAssessed + 1);
		fetchedDto.setYieldTotal(yieldTotal + 1);
		fetchedDto.setYieldActual(yieldActual + 1);
		fetchedDto.setVarietyProductionValue(varietyProductionValue + 1);

		dao.update(fetchedDto, userId);
		
		ClaimCalculationVarietyDto updatedDto = dao.fetch(claimCalculationVarietyGuid1);
		
		Assert.assertEquals("RevisionCount", (Integer)2, updatedDto.getRevisionCount());
		Assert.assertEquals("AveragePrice", fetchedDto.getAveragePrice(), updatedDto.getAveragePrice());
		Assert.assertEquals("AveragePriceOverride", fetchedDto.getAveragePriceOverride(), updatedDto.getAveragePriceOverride());
		Assert.assertEquals("AveragePriceFinal", fetchedDto.getAveragePriceFinal(), updatedDto.getAveragePriceFinal());
		Assert.assertEquals("InsurableValue", fetchedDto.getInsurableValue(), updatedDto.getInsurableValue());
		Assert.assertEquals("YieldAssessedReason", fetchedDto.getYieldAssessedReason(), updatedDto.getYieldAssessedReason());
		Assert.assertEquals("YieldAssessed", fetchedDto.getYieldAssessed(), updatedDto.getYieldAssessed());
		Assert.assertEquals("YieldTotal", fetchedDto.getYieldTotal(), updatedDto.getYieldTotal());
		Assert.assertEquals("YieldActual", fetchedDto.getYieldActual(), updatedDto.getYieldActual());
		Assert.assertEquals("VarietyProductionValue", fetchedDto.getVarietyProductionValue(), updatedDto.getVarietyProductionValue());
		
		
		//Add second variety
		claimCalculationVarietyGuid2 = addVariety(1010337,
				averagePrice,
				averagePriceOverride,
				averagePriceFinal,
				insurableValue,
				yieldAssessedReason,
				yieldAssessed,
				yieldTotal,
				yieldActual,
				varietyProductionValue);
		
		List<ClaimCalculationVarietyDto> dtos = dao.select(claimCalculationGuid);
		Assert.assertEquals("Total varieties wrong: ", 2, dtos.size());
        for (ClaimCalculationVarietyDto dto : dtos) {
    		Assert.assertEquals("ClaimCalculationGuid", claimCalculationGuid, dto.getClaimCalculationGuid());
        }

		
		//Delete varieties and calculation
		delete();
		
		dtos = dao.select(claimCalculationGuid);
		Assert.assertNotNull("varieties object null", dtos);
		Assert.assertEquals("Varieties not deleted", 0, dtos.size());
		
	}
	
	private String addVariety(Integer cropVarietyId,
			Double averagePrice,
			Double averagePriceOverride,
			Double averagePriceFinal,
			Double insurableValue,
			String yieldAssessedReason,
			Double yieldAssessed,
			Double yieldTotal,
			Double yieldActual,
			Double varietyProductionValue
						) throws Exception {
		
		ClaimCalculationVarietyDao dao = persistenceSpringConfig.claimCalculationVarietyDao();
		
		ClaimCalculationVarietyDto newDto = new ClaimCalculationVarietyDto();
		
		newDto.setClaimCalculationGuid(claimCalculationGuid);
		newDto.setCropVarietyId(cropVarietyId);
		newDto.setAveragePrice(averagePrice);
		newDto.setAveragePriceOverride(averagePriceOverride);
		newDto.setAveragePriceFinal(averagePriceFinal);
		newDto.setInsurableValue(insurableValue);
		newDto.setYieldAssessedReason(yieldAssessedReason);
		newDto.setYieldAssessed(yieldAssessed);
		newDto.setYieldTotal(yieldTotal);
		newDto.setYieldActual(yieldActual);
		newDto.setVarietyProductionValue(varietyProductionValue);
		
		dao.insert(newDto, userId);
		
		return newDto.getClaimCalculationVarietyGuid();

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
		newDto.setClaimNumber(99999);
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
