package ca.bc.gov.mal.cirras.claims.persistence.v1.dao;

import java.util.Date;
import java.util.List;

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
import ca.bc.gov.nrs.wfone.common.persistence.dto.PagedDtos;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class ClaimCalculationDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;

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
	
	private ClaimCalculationDto getDto() throws Exception {
		String claimCalculationGuid = "D2CF0984F30C0CC4E053E60A0A0A8DBD";
		ClaimCalculationDao dao = persistenceSpringConfig.claimCalculationDao();
		ClaimCalculationDto dto = dao.fetch(claimCalculationGuid);
		
		return dto;
		
	}
	
	 
}
