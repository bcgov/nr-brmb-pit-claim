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
import ca.bc.gov.mal.cirras.claims.data.entities.ClaimCalculationGrainQuantityDto;
import ca.bc.gov.mal.cirras.claims.data.entities.ClaimCalculationGrapesDto;
import ca.bc.gov.mal.cirras.claims.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dto.PagedDtos;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class ClaimCalculationDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;

	private Integer quantityClaimNumber = 99778865;
	
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
		List<ClaimCalculationDto> ccDtos = dao.getCalculationsByClaimNumber(quantityClaimNumber, null);

		if(ccDtos != null && ccDtos.size() > 0) {

			ClaimCalculationGrainQuantityDao daoGrain = persistenceSpringConfig.claimCalculationGrainQuantityDao();
			
			for (ClaimCalculationDto dto : ccDtos) {
				//delete claim calculation first
				dao.delete(dto.getClaimCalculationGuid());
				
				if ( dto.getClaimCalculationGrainQuantityGuid() != null ) {
					//delete claim calculation grain quantity
					daoGrain.delete(dto.getClaimCalculationGrainQuantityGuid());
				}
			}
		}
	}
	
	@Test
	public void testGetLatestVersionOfCalculation() throws Exception {
		
		Integer claimNumber = 33137;
		ClaimCalculationDao dao = persistenceSpringConfig.claimCalculationDao();
		ClaimCalculationDto dto = dao.getLatestVersionOfCalculation(claimNumber);
		  		    
		Assert.assertEquals((long)33137, (long)dto.getClaimNumber());
		Assert.assertEquals(dto.getCalculateIivInd(), "Y");
		 		
	}
	
	@Test 
	public void testInsertUpdateDeleteClaimCalculation() throws Exception {
		
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
		newDto.setHasChequeReqInd(true);
		newDto.setClaimCalculationGrainQuantityGuid(null);
		newDto.setIsPedigreeInd(false);
		
		String userId = "JUNIT_TEST";

		ClaimCalculationDao dao = persistenceSpringConfig.claimCalculationDao();
		//INSERT
		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getClaimCalculationGuid()); 
		
		//FETCH
		ClaimCalculationDto fetchedDto = dao.fetch(newDto.getClaimCalculationGuid());
		
		Assert.assertEquals("PrimaryPerilCode", newDto.getPrimaryPerilCode(), fetchedDto.getPrimaryPerilCode());
		Assert.assertEquals("SecondaryPerilCode", newDto.getSecondaryPerilCode(), fetchedDto.getSecondaryPerilCode());
		Assert.assertEquals("ClaimStatusCode", newDto.getClaimStatusCode(), fetchedDto.getClaimStatusCode());
		Assert.assertEquals("CommodityCoverageCode", newDto.getCommodityCoverageCode(), fetchedDto.getCommodityCoverageCode());
		Assert.assertEquals("CalculationStatusCode", newDto.getCalculationStatusCode(), fetchedDto.getCalculationStatusCode());
		Assert.assertEquals("InsurancePlanId", newDto.getInsurancePlanId(), fetchedDto.getInsurancePlanId());
		Assert.assertEquals("CropCommodityId", newDto.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("CropYear", newDto.getCropYear(), fetchedDto.getCropYear());
		Assert.assertEquals("InsuredByMeasType", newDto.getInsuredByMeasurementType(), fetchedDto.getInsuredByMeasurementType());
		Assert.assertEquals("PolicyNumber", newDto.getPolicyNumber(), fetchedDto.getPolicyNumber());
		Assert.assertEquals("ContractId", newDto.getContractId(), fetchedDto.getContractId());
		Assert.assertEquals("ClaimNumber", newDto.getClaimNumber(), fetchedDto.getClaimNumber());
		Assert.assertEquals("CalculationVersion", newDto.getCalculationVersion(), fetchedDto.getCalculationVersion());
		Assert.assertEquals("GrowerNumber", newDto.getGrowerNumber(), fetchedDto.getGrowerNumber());
		Assert.assertEquals("GrowerName", newDto.getGrowerName(), fetchedDto.getGrowerName());
		Assert.assertEquals("GrowerAddressLine1", newDto.getGrowerAddressLine1(), fetchedDto.getGrowerAddressLine1());
		Assert.assertEquals("GrowerAddressLine2", newDto.getGrowerAddressLine2(), fetchedDto.getGrowerAddressLine2());
		Assert.assertEquals("GrowerPostalCode", newDto.getGrowerPostalCode(), fetchedDto.getGrowerPostalCode());
		Assert.assertEquals("GrowerCity", newDto.getGrowerCity(), fetchedDto.getGrowerCity());
		Assert.assertEquals("GrowerProvince", newDto.getGrowerProvince(), fetchedDto.getGrowerProvince());
		Assert.assertEquals("TotalClaimAmount", newDto.getTotalClaimAmount(), fetchedDto.getTotalClaimAmount());
		Assert.assertEquals("CalculationComment", newDto.getCalculationComment(), fetchedDto.getCalculationComment());
		Assert.assertEquals("SubmittedByUserid", newDto.getSubmittedByUserid(), fetchedDto.getSubmittedByUserid());
		Assert.assertEquals("SubmittedByName", newDto.getSubmittedByName(), fetchedDto.getSubmittedByName());
		Assert.assertEquals("RecommendedByUserid", newDto.getRecommendedByUserid(), fetchedDto.getRecommendedByUserid());
		Assert.assertEquals("RecommendedByName", newDto.getRecommendedByName(), fetchedDto.getRecommendedByName());
		Assert.assertEquals("ApprovedByUserid", newDto.getApprovedByUserid(), fetchedDto.getApprovedByUserid());
		Assert.assertEquals("ApprovedByName", newDto.getApprovedByName(), fetchedDto.getApprovedByName());
		Assert.assertEquals("HasChequeReqInd", newDto.getHasChequeReqInd(), fetchedDto.getHasChequeReqInd());
		Assert.assertEquals("ClaimCalculationGrainQuantityGuid", newDto.getClaimCalculationGrainQuantityGuid(), fetchedDto.getClaimCalculationGrainQuantityGuid());
		Assert.assertEquals("IsPedigreeInd", newDto.getIsPedigreeInd(), fetchedDto.getIsPedigreeInd());

		//UPDATE
		transactionDate = new Date();
		fetchedDto.setPrimaryPerilCode("FLOOD");
		fetchedDto.setSecondaryPerilCode("WIND");
		fetchedDto.setClaimStatusCode("IN PROGRESS");
		fetchedDto.setCalculationStatusCode("SUBMITTED");
		fetchedDto.setGrowerNumber(22222);
		fetchedDto.setGrowerName("Name 2");
		fetchedDto.setGrowerAddressLine1("Line 1 2");
		fetchedDto.setGrowerAddressLine2("Line 2 2");
		fetchedDto.setGrowerPostalCode("V2V2V2");
		fetchedDto.setGrowerCity("Vancouver");
		fetchedDto.setGrowerProvince("AB");
		fetchedDto.setTotalClaimAmount(15001.0);
		fetchedDto.setCalculationComment("Test Comment 2");
		fetchedDto.setSubmittedByUserid("user1 2");
		fetchedDto.setSubmittedByName("user 1 2");
		fetchedDto.setSubmittedByDate(transactionDate);
		fetchedDto.setRecommendedByUserid("user2 2");
		fetchedDto.setRecommendedByName("user 2 2");
		fetchedDto.setRecommendedByDate(transactionDate);
		fetchedDto.setApprovedByUserid("user3 2");
		fetchedDto.setApprovedByName("user 3 2");
		fetchedDto.setApprovedByDate(transactionDate);
		fetchedDto.setHasChequeReqInd(false);

		
		dao.update(fetchedDto.getClaimCalculationGuid(), fetchedDto, userId);
		
		//FETCH
		ClaimCalculationDto updatedDto = dao.fetch(fetchedDto.getClaimCalculationGuid());

		Assert.assertEquals("PrimaryPerilCode", fetchedDto.getPrimaryPerilCode(), updatedDto.getPrimaryPerilCode());
		Assert.assertEquals("SecondaryPerilCode", fetchedDto.getSecondaryPerilCode(), updatedDto.getSecondaryPerilCode());
		Assert.assertEquals("ClaimStatusCode", fetchedDto.getClaimStatusCode(), updatedDto.getClaimStatusCode());
		Assert.assertEquals("GrowerNumber", fetchedDto.getGrowerNumber(), updatedDto.getGrowerNumber());
		Assert.assertEquals("GrowerName", fetchedDto.getGrowerName(), updatedDto.getGrowerName());
		Assert.assertEquals("GrowerAddressLine1", fetchedDto.getGrowerAddressLine1(), updatedDto.getGrowerAddressLine1());
		Assert.assertEquals("GrowerAddressLine2", fetchedDto.getGrowerAddressLine2(), updatedDto.getGrowerAddressLine2());
		Assert.assertEquals("GrowerPostalCode", fetchedDto.getGrowerPostalCode(), updatedDto.getGrowerPostalCode());
		Assert.assertEquals("GrowerCity", fetchedDto.getGrowerCity(), updatedDto.getGrowerCity());
		Assert.assertEquals("GrowerProvince", fetchedDto.getGrowerProvince(), updatedDto.getGrowerProvince());
		Assert.assertEquals("TotalClaimAmount", fetchedDto.getTotalClaimAmount(), updatedDto.getTotalClaimAmount());
		Assert.assertEquals("CalculationComment", fetchedDto.getCalculationComment(), updatedDto.getCalculationComment());
		Assert.assertEquals("SubmittedByUserid", fetchedDto.getSubmittedByUserid(), updatedDto.getSubmittedByUserid());
		Assert.assertEquals("SubmittedByName", fetchedDto.getSubmittedByName(), updatedDto.getSubmittedByName());
		Assert.assertEquals("RecommendedByUserid", fetchedDto.getRecommendedByUserid(), updatedDto.getRecommendedByUserid());
		Assert.assertEquals("RecommendedByName", fetchedDto.getRecommendedByName(), updatedDto.getRecommendedByName());
		Assert.assertEquals("ApprovedByUserid", fetchedDto.getApprovedByUserid(), updatedDto.getApprovedByUserid());
		Assert.assertEquals("ApprovedByName", fetchedDto.getApprovedByName(), updatedDto.getApprovedByName());
		Assert.assertEquals("HasChequeReqInd", fetchedDto.getHasChequeReqInd(), updatedDto.getHasChequeReqInd());
		Assert.assertEquals("ClaimCalculationGrainQuantityGuid", fetchedDto.getClaimCalculationGrainQuantityGuid(), updatedDto.getClaimCalculationGrainQuantityGuid());
		Assert.assertEquals("IsPedigreeInd", fetchedDto.getIsPedigreeInd(), updatedDto.getIsPedigreeInd());

		//DELETE
		dao.delete(updatedDto.getClaimCalculationGuid());

	}
	
	//@Test 
	public void testInsertNewVersion() throws Exception {

		String claimCalculationGuid = "B584B129EE9D19FEE053E60A0A0ACC2B";
		ClaimCalculationDao dao = persistenceSpringConfig.claimCalculationDao();
		ClaimCalculationDto dto = dao.fetch(claimCalculationGuid);

		
		ClaimCalculationDto newDto = new ClaimCalculationDto();
		
		Integer version = new Integer(dto.getCalculationVersion());
		
		for (int i = 1; i <= 2; i = i + 1) {
			
			Integer newVersion = new Integer(version + i);
			
			newDto.setPrimaryPerilCode(dto.getPrimaryPerilCode());
			newDto.setSecondaryPerilCode(dto.getSecondaryPerilCode());
			newDto.setClaimStatusCode(dto.getClaimStatusCode());
			newDto.setCommodityCoverageCode(dto.getCommodityCoverageCode());
			newDto.setCalculationStatusCode(dto.getCalculationStatusCode());
			newDto.setInsurancePlanId(dto.getInsurancePlanId());
			newDto.setCropCommodityId(dto.getCropCommodityId());
			newDto.setCropYear(dto.getCropYear());
			newDto.setPolicyNumber(dto.getPolicyNumber());
			newDto.setContractId(dto.getContractId());
			newDto.setClaimNumber(dto.getClaimNumber());
			newDto.setCalculationVersion(newVersion);
			newDto.setGrowerNumber(dto.getGrowerNumber());
			newDto.setGrowerName(dto.getGrowerName());
			newDto.setGrowerAddressLine1(dto.getGrowerAddressLine1());
			newDto.setGrowerAddressLine2(dto.getGrowerAddressLine2());
			newDto.setGrowerPostalCode(dto.getGrowerPostalCode());
			newDto.setGrowerCity(dto.getGrowerCity());
			newDto.setGrowerProvince(dto.getGrowerProvince());
			newDto.setTotalClaimAmount(dto.getTotalClaimAmount());
			newDto.setCalculationComment(dto.getCalculationComment());
			newDto.setSubmittedByUserid(dto.getSubmittedByUserid());
			newDto.setSubmittedByName(dto.getSubmittedByName());
			newDto.setSubmittedByDate(dto.getSubmittedByDate());
			newDto.setRecommendedByUserid(dto.getRecommendedByUserid());
			newDto.setRecommendedByName(dto.getRecommendedByName());
			newDto.setRecommendedByDate(dto.getRecommendedByDate());
			newDto.setApprovedByUserid(dto.getApprovedByUserid());
			newDto.setApprovedByName(dto.getApprovedByName());
			newDto.setApprovedByDate(dto.getApprovedByDate());
			newDto.setCalculateIivInd(dto.getCalculateIivInd());
			newDto.setHasChequeReqInd(dto.getHasChequeReqInd());
			newDto.setCreateClaimCalcUserGuid(dto.getCreateClaimCalcUserGuid());
			newDto.setUpdateClaimCalcUserGuid(dto.getUpdateClaimCalcUserGuid());
			
			String userId = "JUNIT_TEST";
	
			dao.insert(newDto, userId);
		}

	}
	
	
	//@Test
	public void testUpdateClaimCalculation() throws Exception {

		ClaimCalculationDto dto = getDto();
		
		String userId = "JUNIT_TEST";
		
		ClaimCalculationDao dao = persistenceSpringConfig.claimCalculationDao();
		dao.update(dto.getClaimCalculationGuid(), dto, userId);
		Assert.assertNotNull(dto.getClaimCalculationGuid()); 

	}

	
	@Test
	public void testGetByClaimNumber() throws Exception {

		ClaimCalculationDao dao = persistenceSpringConfig.claimCalculationDao();
		Integer claimNumber = new Integer(25749);
		String calculationStatusCode = null;
		List<ClaimCalculationDto> dtos = dao.getCalculationsByClaimNumber(claimNumber, calculationStatusCode);

		Assert.assertNotNull(dtos); 

	}
	
	@Test
	public void testGetByInsurancePlan() throws Exception {

		ClaimCalculationDao dao = persistenceSpringConfig.claimCalculationDao();
		Integer maxRows = new Integer(1000);
		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		Integer insurancePlan = new Integer(2);
		
		PagedDtos<ClaimCalculationDto> dtos = dao.select(null, null, null, null, null, null, insurancePlan, null, null, maxRows, pageNumber, pageRowCount);
		Assert.assertNotNull(dtos);

	}

	
	@Test 
	public void testClaimCalculationWithGrainQuantity() throws Exception {

		String userId = "JUNIT_TEST";
		
		ClaimCalculationGrainQuantityDto grainQtyDto = new ClaimCalculationGrainQuantityDto();

		grainQtyDto.setAdvancedClaim(12.34);
		grainQtyDto.setMaxClaimPayable(56.78);
		grainQtyDto.setProductionGuaranteeAmount(11.22);
		grainQtyDto.setQuantityLossClaim(33.44);
		grainQtyDto.setReseedClaim(55.66);
		grainQtyDto.setTotalCoverageValue(77.88);
		grainQtyDto.setTotalYieldLossValue(99.88);
		
		ClaimCalculationGrainQuantityDao grainQtyDao = persistenceSpringConfig.claimCalculationGrainQuantityDao();

		//INSERT Grain Quantity
		grainQtyDao.insert(grainQtyDto, userId);
		Assert.assertNotNull(grainQtyDto.getClaimCalculationGrainQuantityGuid());
		
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
		newDto.setClaimNumber(quantityClaimNumber);
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
		newDto.setHasChequeReqInd(true);
		newDto.setClaimCalculationGrainQuantityGuid(grainQtyDto.getClaimCalculationGrainQuantityGuid());
		newDto.setIsPedigreeInd(false);

		ClaimCalculationDao dao = persistenceSpringConfig.claimCalculationDao();
		//INSERT
		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getClaimCalculationGuid()); 
		
		//FETCH
		ClaimCalculationDto fetchedDto = dao.fetch(newDto.getClaimCalculationGuid());
		
		Assert.assertEquals("PrimaryPerilCode", newDto.getPrimaryPerilCode(), fetchedDto.getPrimaryPerilCode());
		Assert.assertEquals("SecondaryPerilCode", newDto.getSecondaryPerilCode(), fetchedDto.getSecondaryPerilCode());
		Assert.assertEquals("ClaimStatusCode", newDto.getClaimStatusCode(), fetchedDto.getClaimStatusCode());
		Assert.assertEquals("CommodityCoverageCode", newDto.getCommodityCoverageCode(), fetchedDto.getCommodityCoverageCode());
		Assert.assertEquals("CalculationStatusCode", newDto.getCalculationStatusCode(), fetchedDto.getCalculationStatusCode());
		Assert.assertEquals("InsurancePlanId", newDto.getInsurancePlanId(), fetchedDto.getInsurancePlanId());
		Assert.assertEquals("CropCommodityId", newDto.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("CropYear", newDto.getCropYear(), fetchedDto.getCropYear());
		Assert.assertEquals("InsuredByMeasType", newDto.getInsuredByMeasurementType(), fetchedDto.getInsuredByMeasurementType());
		Assert.assertEquals("PolicyNumber", newDto.getPolicyNumber(), fetchedDto.getPolicyNumber());
		Assert.assertEquals("ContractId", newDto.getContractId(), fetchedDto.getContractId());
		Assert.assertEquals("ClaimNumber", newDto.getClaimNumber(), fetchedDto.getClaimNumber());
		Assert.assertEquals("CalculationVersion", newDto.getCalculationVersion(), fetchedDto.getCalculationVersion());
		Assert.assertEquals("GrowerNumber", newDto.getGrowerNumber(), fetchedDto.getGrowerNumber());
		Assert.assertEquals("GrowerName", newDto.getGrowerName(), fetchedDto.getGrowerName());
		Assert.assertEquals("GrowerAddressLine1", newDto.getGrowerAddressLine1(), fetchedDto.getGrowerAddressLine1());
		Assert.assertEquals("GrowerAddressLine2", newDto.getGrowerAddressLine2(), fetchedDto.getGrowerAddressLine2());
		Assert.assertEquals("GrowerPostalCode", newDto.getGrowerPostalCode(), fetchedDto.getGrowerPostalCode());
		Assert.assertEquals("GrowerCity", newDto.getGrowerCity(), fetchedDto.getGrowerCity());
		Assert.assertEquals("GrowerProvince", newDto.getGrowerProvince(), fetchedDto.getGrowerProvince());
		Assert.assertEquals("TotalClaimAmount", newDto.getTotalClaimAmount(), fetchedDto.getTotalClaimAmount());
		Assert.assertEquals("CalculationComment", newDto.getCalculationComment(), fetchedDto.getCalculationComment());
		Assert.assertEquals("SubmittedByUserid", newDto.getSubmittedByUserid(), fetchedDto.getSubmittedByUserid());
		Assert.assertEquals("SubmittedByName", newDto.getSubmittedByName(), fetchedDto.getSubmittedByName());
		Assert.assertEquals("RecommendedByUserid", newDto.getRecommendedByUserid(), fetchedDto.getRecommendedByUserid());
		Assert.assertEquals("RecommendedByName", newDto.getRecommendedByName(), fetchedDto.getRecommendedByName());
		Assert.assertEquals("ApprovedByUserid", newDto.getApprovedByUserid(), fetchedDto.getApprovedByUserid());
		Assert.assertEquals("ApprovedByName", newDto.getApprovedByName(), fetchedDto.getApprovedByName());
		Assert.assertEquals("HasChequeReqInd", newDto.getHasChequeReqInd(), fetchedDto.getHasChequeReqInd());
		Assert.assertEquals("ClaimCalculationGrainQuantityGuid", newDto.getClaimCalculationGrainQuantityGuid(), fetchedDto.getClaimCalculationGrainQuantityGuid());
		Assert.assertEquals("IsPedigreeInd", newDto.getIsPedigreeInd(), fetchedDto.getIsPedigreeInd());

		//UPDATE
		transactionDate = new Date();
		fetchedDto.setPrimaryPerilCode("FLOOD");
		fetchedDto.setSecondaryPerilCode("WIND");
		fetchedDto.setClaimStatusCode("IN PROGRESS");
		fetchedDto.setCalculationStatusCode("SUBMITTED");
		fetchedDto.setGrowerNumber(22222);
		fetchedDto.setGrowerName("Name 2");
		fetchedDto.setGrowerAddressLine1("Line 1 2");
		fetchedDto.setGrowerAddressLine2("Line 2 2");
		fetchedDto.setGrowerPostalCode("V2V2V2");
		fetchedDto.setGrowerCity("Vancouver");
		fetchedDto.setGrowerProvince("AB");
		fetchedDto.setTotalClaimAmount(15001.0);
		fetchedDto.setCalculationComment("Test Comment 2");
		fetchedDto.setSubmittedByUserid("user1 2");
		fetchedDto.setSubmittedByName("user 1 2");
		fetchedDto.setSubmittedByDate(transactionDate);
		fetchedDto.setRecommendedByUserid("user2 2");
		fetchedDto.setRecommendedByName("user 2 2");
		fetchedDto.setRecommendedByDate(transactionDate);
		fetchedDto.setApprovedByUserid("user3 2");
		fetchedDto.setApprovedByName("user 3 2");
		fetchedDto.setApprovedByDate(transactionDate);
		fetchedDto.setHasChequeReqInd(false);
		fetchedDto.setClaimCalculationGrainQuantityGuid(null);
		
		dao.update(fetchedDto.getClaimCalculationGuid(), fetchedDto, userId);
		
		//FETCH
		ClaimCalculationDto updatedDto = dao.fetch(fetchedDto.getClaimCalculationGuid());

		Assert.assertEquals("PrimaryPerilCode", fetchedDto.getPrimaryPerilCode(), updatedDto.getPrimaryPerilCode());
		Assert.assertEquals("SecondaryPerilCode", fetchedDto.getSecondaryPerilCode(), updatedDto.getSecondaryPerilCode());
		Assert.assertEquals("ClaimStatusCode", fetchedDto.getClaimStatusCode(), updatedDto.getClaimStatusCode());
		Assert.assertEquals("GrowerNumber", fetchedDto.getGrowerNumber(), updatedDto.getGrowerNumber());
		Assert.assertEquals("GrowerName", fetchedDto.getGrowerName(), updatedDto.getGrowerName());
		Assert.assertEquals("GrowerAddressLine1", fetchedDto.getGrowerAddressLine1(), updatedDto.getGrowerAddressLine1());
		Assert.assertEquals("GrowerAddressLine2", fetchedDto.getGrowerAddressLine2(), updatedDto.getGrowerAddressLine2());
		Assert.assertEquals("GrowerPostalCode", fetchedDto.getGrowerPostalCode(), updatedDto.getGrowerPostalCode());
		Assert.assertEquals("GrowerCity", fetchedDto.getGrowerCity(), updatedDto.getGrowerCity());
		Assert.assertEquals("GrowerProvince", fetchedDto.getGrowerProvince(), updatedDto.getGrowerProvince());
		Assert.assertEquals("TotalClaimAmount", fetchedDto.getTotalClaimAmount(), updatedDto.getTotalClaimAmount());
		Assert.assertEquals("CalculationComment", fetchedDto.getCalculationComment(), updatedDto.getCalculationComment());
		Assert.assertEquals("SubmittedByUserid", fetchedDto.getSubmittedByUserid(), updatedDto.getSubmittedByUserid());
		Assert.assertEquals("SubmittedByName", fetchedDto.getSubmittedByName(), updatedDto.getSubmittedByName());
		Assert.assertEquals("RecommendedByUserid", fetchedDto.getRecommendedByUserid(), updatedDto.getRecommendedByUserid());
		Assert.assertEquals("RecommendedByName", fetchedDto.getRecommendedByName(), updatedDto.getRecommendedByName());
		Assert.assertEquals("ApprovedByUserid", fetchedDto.getApprovedByUserid(), updatedDto.getApprovedByUserid());
		Assert.assertEquals("ApprovedByName", fetchedDto.getApprovedByName(), updatedDto.getApprovedByName());
		Assert.assertEquals("HasChequeReqInd", fetchedDto.getHasChequeReqInd(), updatedDto.getHasChequeReqInd());
		Assert.assertEquals("ClaimCalculationGrainQuantityGuid", null, updatedDto.getClaimCalculationGrainQuantityGuid());
		Assert.assertEquals("IsPedigreeInd", fetchedDto.getIsPedigreeInd(), updatedDto.getIsPedigreeInd());

		updatedDto.setClaimCalculationGrainQuantityGuid(grainQtyDto.getClaimCalculationGrainQuantityGuid());
		
		dao.update(updatedDto.getClaimCalculationGuid(), updatedDto, userId);
		
		//FETCH
		updatedDto = dao.fetch(updatedDto.getClaimCalculationGuid());
		Assert.assertEquals("ClaimCalculationGrainQuantityGuid", grainQtyDto.getClaimCalculationGrainQuantityGuid(), updatedDto.getClaimCalculationGrainQuantityGuid());
		
		//DELETE
		dao.delete(updatedDto.getClaimCalculationGuid());
		grainQtyDao.delete(grainQtyDto.getClaimCalculationGrainQuantityGuid());
	}

	@Test 
	public void testClaimCalculationWithPedigree() throws Exception {

		String userId = "JUNIT_TEST";
				
		ClaimCalculationDto newDto = new ClaimCalculationDto();

		Date transactionDate = new Date();
		newDto.setPrimaryPerilCode("DROUGHT");
		newDto.setSecondaryPerilCode("FIRE");
		newDto.setClaimStatusCode("OPEN");
		newDto.setCommodityCoverageCode("CQNT");
		newDto.setCalculationStatusCode("DRAFT");
		newDto.setInsurancePlanId(1);
		newDto.setCropCommodityId(1);   // Fresh Grapes
		newDto.setCropYear(2020);
		newDto.setInsuredByMeasurementType("ACRES");
		newDto.setPolicyNumber("100100-20");
		newDto.setContractId(1000);
		newDto.setClaimNumber(quantityClaimNumber);
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
		newDto.setHasChequeReqInd(true);
		newDto.setClaimCalculationGrainQuantityGuid(null);
		newDto.setIsPedigreeInd(false);

		ClaimCalculationDao dao = persistenceSpringConfig.claimCalculationDao();
		//INSERT
		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getClaimCalculationGuid()); 
		
		//FETCH
		ClaimCalculationDto fetchedDto = dao.fetch(newDto.getClaimCalculationGuid());
		
		Assert.assertEquals("InsurancePlanId", newDto.getInsurancePlanId(), fetchedDto.getInsurancePlanId());
		Assert.assertEquals("CropCommodityId", newDto.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("IsPedigreeInd", newDto.getIsPedigreeInd(), fetchedDto.getIsPedigreeInd());

		//DELETE (because crop commodity id cannot be updated)
		dao.delete(newDto.getClaimCalculationGuid());

		newDto = new ClaimCalculationDto();
		
		newDto.setPrimaryPerilCode("DROUGHT");
		newDto.setSecondaryPerilCode("FIRE");
		newDto.setClaimStatusCode("OPEN");
		newDto.setCommodityCoverageCode("CQNT");
		newDto.setCalculationStatusCode("DRAFT");
		newDto.setInsurancePlanId(4);
		newDto.setCropCommodityId(17);   // Barley - Pedigreed
		newDto.setCropYear(2020);
		newDto.setInsuredByMeasurementType("ACRES");
		newDto.setPolicyNumber("100100-20");
		newDto.setContractId(1000);
		newDto.setClaimNumber(quantityClaimNumber);
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
		newDto.setHasChequeReqInd(true);
		newDto.setClaimCalculationGrainQuantityGuid(null);
		newDto.setIsPedigreeInd(true);

		//INSERT
		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getClaimCalculationGuid()); 
		
		//FETCH
		fetchedDto = dao.fetch(newDto.getClaimCalculationGuid());
		
		Assert.assertEquals("InsurancePlanId", newDto.getInsurancePlanId(), fetchedDto.getInsurancePlanId());
		Assert.assertEquals("CropCommodityId", newDto.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("IsPedigreeInd", newDto.getIsPedigreeInd(), fetchedDto.getIsPedigreeInd());

		//DELETE (because crop commodity id cannot be updated)
		dao.delete(newDto.getClaimCalculationGuid());

		newDto = new ClaimCalculationDto();
		
		newDto.setPrimaryPerilCode("DROUGHT");
		newDto.setSecondaryPerilCode("FIRE");
		newDto.setClaimStatusCode("OPEN");
		newDto.setCommodityCoverageCode("CQNT");
		newDto.setCalculationStatusCode("DRAFT");
		newDto.setInsurancePlanId(4);
		newDto.setCropCommodityId(16);   // Barley (not Pedigreed)
		newDto.setCropYear(2020);
		newDto.setInsuredByMeasurementType("ACRES");
		newDto.setPolicyNumber("100100-20");
		newDto.setContractId(1000);
		newDto.setClaimNumber(quantityClaimNumber);
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
		newDto.setHasChequeReqInd(true);
		newDto.setClaimCalculationGrainQuantityGuid(null);
		newDto.setIsPedigreeInd(false);

		//INSERT
		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getClaimCalculationGuid()); 
		
		//FETCH
		fetchedDto = dao.fetch(newDto.getClaimCalculationGuid());
		
		Assert.assertEquals("InsurancePlanId", newDto.getInsurancePlanId(), fetchedDto.getInsurancePlanId());
		Assert.assertEquals("CropCommodityId", newDto.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("IsPedigreeInd", newDto.getIsPedigreeInd(), fetchedDto.getIsPedigreeInd());

		//DELETE (because crop commodity id cannot be updated)
		dao.delete(newDto.getClaimCalculationGuid());		
	}
	

	@Test 
	public void testGetByClaimNumberAndVersion() throws Exception {

		String userId = "JUNIT_TEST";
				
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
		newDto.setClaimNumber(quantityClaimNumber);
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
		newDto.setHasChequeReqInd(true);
		newDto.setClaimCalculationGrainQuantityGuid(null);
		newDto.setIsPedigreeInd(false);

		ClaimCalculationDao dao = persistenceSpringConfig.claimCalculationDao();
		//INSERT
		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getClaimCalculationGuid()); 
		
		//Get by Claim Number and Version
		ClaimCalculationDto fetchedDto = dao.getByClaimNumberAndVersion(quantityClaimNumber, 1);
		
		Assert.assertEquals("PrimaryPerilCode", newDto.getPrimaryPerilCode(), fetchedDto.getPrimaryPerilCode());
		Assert.assertEquals("SecondaryPerilCode", newDto.getSecondaryPerilCode(), fetchedDto.getSecondaryPerilCode());
		Assert.assertEquals("ClaimStatusCode", newDto.getClaimStatusCode(), fetchedDto.getClaimStatusCode());
		Assert.assertEquals("CommodityCoverageCode", newDto.getCommodityCoverageCode(), fetchedDto.getCommodityCoverageCode());
		Assert.assertEquals("CalculationStatusCode", newDto.getCalculationStatusCode(), fetchedDto.getCalculationStatusCode());
		Assert.assertEquals("InsurancePlanId", newDto.getInsurancePlanId(), fetchedDto.getInsurancePlanId());
		Assert.assertEquals("CropCommodityId", newDto.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("CropYear", newDto.getCropYear(), fetchedDto.getCropYear());
		Assert.assertEquals("InsuredByMeasType", newDto.getInsuredByMeasurementType(), fetchedDto.getInsuredByMeasurementType());
		Assert.assertEquals("PolicyNumber", newDto.getPolicyNumber(), fetchedDto.getPolicyNumber());
		Assert.assertEquals("ContractId", newDto.getContractId(), fetchedDto.getContractId());
		Assert.assertEquals("ClaimNumber", newDto.getClaimNumber(), fetchedDto.getClaimNumber());
		Assert.assertEquals("CalculationVersion", newDto.getCalculationVersion(), fetchedDto.getCalculationVersion());
		Assert.assertEquals("GrowerNumber", newDto.getGrowerNumber(), fetchedDto.getGrowerNumber());
		Assert.assertEquals("GrowerName", newDto.getGrowerName(), fetchedDto.getGrowerName());
		Assert.assertEquals("GrowerAddressLine1", newDto.getGrowerAddressLine1(), fetchedDto.getGrowerAddressLine1());
		Assert.assertEquals("GrowerAddressLine2", newDto.getGrowerAddressLine2(), fetchedDto.getGrowerAddressLine2());
		Assert.assertEquals("GrowerPostalCode", newDto.getGrowerPostalCode(), fetchedDto.getGrowerPostalCode());
		Assert.assertEquals("GrowerCity", newDto.getGrowerCity(), fetchedDto.getGrowerCity());
		Assert.assertEquals("GrowerProvince", newDto.getGrowerProvince(), fetchedDto.getGrowerProvince());
		Assert.assertEquals("TotalClaimAmount", newDto.getTotalClaimAmount(), fetchedDto.getTotalClaimAmount());
		Assert.assertEquals("CalculationComment", newDto.getCalculationComment(), fetchedDto.getCalculationComment());
		Assert.assertEquals("SubmittedByUserid", newDto.getSubmittedByUserid(), fetchedDto.getSubmittedByUserid());
		Assert.assertEquals("SubmittedByName", newDto.getSubmittedByName(), fetchedDto.getSubmittedByName());
		Assert.assertEquals("RecommendedByUserid", newDto.getRecommendedByUserid(), fetchedDto.getRecommendedByUserid());
		Assert.assertEquals("RecommendedByName", newDto.getRecommendedByName(), fetchedDto.getRecommendedByName());
		Assert.assertEquals("ApprovedByUserid", newDto.getApprovedByUserid(), fetchedDto.getApprovedByUserid());
		Assert.assertEquals("ApprovedByName", newDto.getApprovedByName(), fetchedDto.getApprovedByName());
		Assert.assertEquals("HasChequeReqInd", newDto.getHasChequeReqInd(), fetchedDto.getHasChequeReqInd());
		Assert.assertEquals("ClaimCalculationGrainQuantityGuid", newDto.getClaimCalculationGrainQuantityGuid(), fetchedDto.getClaimCalculationGrainQuantityGuid());
		Assert.assertEquals("IsPedigreeInd", newDto.getIsPedigreeInd(), fetchedDto.getIsPedigreeInd());

		//Get by Claim Number and Version
		fetchedDto = dao.getByClaimNumberAndVersion(quantityClaimNumber, 2);   // Non-existent version

		Assert.assertNull(fetchedDto);

		// Create version 2
		ClaimCalculationDto newDto2 = new ClaimCalculationDto();

		newDto2.setPrimaryPerilCode("DROUGHT");
		newDto2.setSecondaryPerilCode("FIRE");
		newDto2.setClaimStatusCode("OPEN");
		newDto2.setCommodityCoverageCode("CQNT");
		newDto2.setCalculationStatusCode("DRAFT");
		newDto2.setInsurancePlanId(1);
		newDto2.setCropCommodityId(1);
		newDto2.setCropYear(2020);
		newDto2.setInsuredByMeasurementType("ACRES");
		newDto2.setPolicyNumber("100100-20");
		newDto2.setContractId(1000);
		newDto2.setClaimNumber(quantityClaimNumber);
		newDto2.setCalculationVersion(2);
		newDto2.setGrowerNumber(11111);
		newDto2.setGrowerName("Name 1");
		newDto2.setGrowerAddressLine1("Line 1");
		newDto2.setGrowerAddressLine2("Line 2");
		newDto2.setGrowerPostalCode("V1V1V1");
		newDto2.setGrowerCity("Victoria");
		newDto2.setGrowerProvince("BC");
		newDto2.setTotalClaimAmount(15000.0);
		newDto2.setCalculationComment("Test Comment");
		newDto2.setSubmittedByUserid("user1");
		newDto2.setSubmittedByName("user 1");
		newDto2.setSubmittedByDate(transactionDate);
		newDto2.setRecommendedByUserid("user2");
		newDto2.setRecommendedByName("user 2");
		newDto2.setRecommendedByDate(transactionDate);
		newDto2.setApprovedByUserid("user3");
		newDto2.setApprovedByName("user 3");
		newDto2.setApprovedByDate(transactionDate);
		newDto2.setCalculateIivInd("Y");
		newDto2.setHasChequeReqInd(true);
		newDto2.setClaimCalculationGrainQuantityGuid(null);
		newDto2.setIsPedigreeInd(false);

		//INSERT
		dao.insert(newDto2, userId);
		Assert.assertNotNull(newDto2.getClaimCalculationGuid()); 

		//Get by Claim Number and Version
		fetchedDto = dao.getByClaimNumberAndVersion(quantityClaimNumber, 2);
		
		Assert.assertEquals("PrimaryPerilCode", newDto2.getPrimaryPerilCode(), fetchedDto.getPrimaryPerilCode());
		Assert.assertEquals("SecondaryPerilCode", newDto2.getSecondaryPerilCode(), fetchedDto.getSecondaryPerilCode());
		Assert.assertEquals("ClaimStatusCode", newDto2.getClaimStatusCode(), fetchedDto.getClaimStatusCode());
		Assert.assertEquals("CommodityCoverageCode", newDto2.getCommodityCoverageCode(), fetchedDto.getCommodityCoverageCode());
		Assert.assertEquals("CalculationStatusCode", newDto2.getCalculationStatusCode(), fetchedDto.getCalculationStatusCode());
		Assert.assertEquals("InsurancePlanId", newDto2.getInsurancePlanId(), fetchedDto.getInsurancePlanId());
		Assert.assertEquals("CropCommodityId", newDto2.getCropCommodityId(), fetchedDto.getCropCommodityId());
		Assert.assertEquals("CropYear", newDto2.getCropYear(), fetchedDto.getCropYear());
		Assert.assertEquals("InsuredByMeasType", newDto2.getInsuredByMeasurementType(), fetchedDto.getInsuredByMeasurementType());
		Assert.assertEquals("PolicyNumber", newDto2.getPolicyNumber(), fetchedDto.getPolicyNumber());
		Assert.assertEquals("ContractId", newDto2.getContractId(), fetchedDto.getContractId());
		Assert.assertEquals("ClaimNumber", newDto2.getClaimNumber(), fetchedDto.getClaimNumber());
		Assert.assertEquals("CalculationVersion", newDto2.getCalculationVersion(), fetchedDto.getCalculationVersion());
		Assert.assertEquals("GrowerNumber", newDto2.getGrowerNumber(), fetchedDto.getGrowerNumber());
		Assert.assertEquals("GrowerName", newDto2.getGrowerName(), fetchedDto.getGrowerName());
		Assert.assertEquals("GrowerAddressLine1", newDto2.getGrowerAddressLine1(), fetchedDto.getGrowerAddressLine1());
		Assert.assertEquals("GrowerAddressLine2", newDto2.getGrowerAddressLine2(), fetchedDto.getGrowerAddressLine2());
		Assert.assertEquals("GrowerPostalCode", newDto2.getGrowerPostalCode(), fetchedDto.getGrowerPostalCode());
		Assert.assertEquals("GrowerCity", newDto2.getGrowerCity(), fetchedDto.getGrowerCity());
		Assert.assertEquals("GrowerProvince", newDto2.getGrowerProvince(), fetchedDto.getGrowerProvince());
		Assert.assertEquals("TotalClaimAmount", newDto2.getTotalClaimAmount(), fetchedDto.getTotalClaimAmount());
		Assert.assertEquals("CalculationComment", newDto2.getCalculationComment(), fetchedDto.getCalculationComment());
		Assert.assertEquals("SubmittedByUserid", newDto2.getSubmittedByUserid(), fetchedDto.getSubmittedByUserid());
		Assert.assertEquals("SubmittedByName", newDto2.getSubmittedByName(), fetchedDto.getSubmittedByName());
		Assert.assertEquals("RecommendedByUserid", newDto2.getRecommendedByUserid(), fetchedDto.getRecommendedByUserid());
		Assert.assertEquals("RecommendedByName", newDto2.getRecommendedByName(), fetchedDto.getRecommendedByName());
		Assert.assertEquals("ApprovedByUserid", newDto2.getApprovedByUserid(), fetchedDto.getApprovedByUserid());
		Assert.assertEquals("ApprovedByName", newDto2.getApprovedByName(), fetchedDto.getApprovedByName());
		Assert.assertEquals("HasChequeReqInd", newDto2.getHasChequeReqInd(), fetchedDto.getHasChequeReqInd());
		Assert.assertEquals("ClaimCalculationGrainQuantityGuid", newDto2.getClaimCalculationGrainQuantityGuid(), fetchedDto.getClaimCalculationGrainQuantityGuid());
		Assert.assertEquals("IsPedigreeInd", newDto2.getIsPedigreeInd(), fetchedDto.getIsPedigreeInd());
		
		//DELETE
		dao.delete(newDto.getClaimCalculationGuid());
		dao.delete(newDto2.getClaimCalculationGuid());
	}
	
	private ClaimCalculationDto getDto() throws Exception {
		String claimCalculationGuid = "D2CF0984F30C0CC4E053E60A0A0A8DBD";
		ClaimCalculationDao dao = persistenceSpringConfig.claimCalculationDao();
		ClaimCalculationDto dto = dao.fetch(claimCalculationGuid);
		
		return dto;
		
	}
	
	 
}
