package ca.bc.gov.mal.cirras.claims.data.repositories;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimCalculationDao;
import ca.bc.gov.mal.cirras.claims.data.entities.ClaimCalculationDto;
import ca.bc.gov.mal.cirras.claims.data.entities.ClaimDto;
import ca.bc.gov.mal.cirras.claims.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.TooManyRecordsException;
import ca.bc.gov.nrs.wfone.common.persistence.dto.PagedDtos;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class ClaimDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;

	private Integer colId = 1038112;
	private Integer ippId = 1205576;
	
	@Test
	public void testFetchClaim() throws Exception {
		
		ClaimDao dao = persistenceSpringConfig.claimDao();
		ClaimDto dto = dao.fetch(colId);
		
		
		Assert.assertNotNull(dto); 
		Assert.assertEquals(colId, dto.getColId());
		 		
	}

	@Test
	public void testSelectByProductId() throws Exception {
		
		ClaimDao dao = persistenceSpringConfig.claimDao();
		ClaimDto dto = dao.selectByProductId(ippId);
		
		Assert.assertNotNull(dto); 
		Assert.assertEquals(colId, dto.getColId());
		Assert.assertEquals(ippId, dto.getIppId());
		
		// Search for non-existent product id
		dto = dao.selectByProductId(999999999);
		
		Assert.assertNull(dto); 
	}
	
	@Test
	public void testSelectQuantityClaimsByPolicyId() throws Exception {
		
		Integer iplId = 1070071; //711519-24
		
		ClaimDao dao = persistenceSpringConfig.claimDao();
		List<ClaimDto> dtos = dao.selectQuantityClaimsByPolicyId(iplId);
		
		Assert.assertNotNull(dtos);
		
		Integer countCalculation = 0;
		
		for (ClaimDto dto : dtos) {
			Assert.assertNotNull(dto.getClaimStatusCode()); 
			Assert.assertNotNull(dto.getCommodityCoverageCode());
			Assert.assertNotNull(dto.getCropCommodityId());
			Assert.assertNotNull(dto.getClaimNumber());
			if(dto.getClaimNumber().equals(37187)) {
				//Claim 37187 and a calculation has to exist
				Assert.assertNotNull(dto.getClaimCalculationGuid());
				Assert.assertNotNull(dto.getCalculationStatusCode());
				countCalculation = countCalculation +1;
			} else {
				//Only works if all other claims don't have a calculation yet
				Assert.assertNull(dto.getClaimCalculationGuid());
				Assert.assertNull(dto.getCalculationStatusCode());
			}
		}
		
		Assert.assertEquals(1, countCalculation.intValue());
		
	}
	
	
	@Test
	public void testSelectByClaimNumber() throws Exception {
		
		Integer claimNumber = 28282;
		
		ClaimDao dao = persistenceSpringConfig.claimDao();
		ClaimDto dto = dao.selectByClaimNumber(claimNumber);
		
		
		Assert.assertNotNull(dto); 
		Assert.assertEquals(claimNumber, dto.getClaimNumber());
		 		
	}
	
	@Test
	public void testSelectListByClaimNumber() throws DaoException, TooManyRecordsException {
		
		ClaimDao dao = persistenceSpringConfig.claimDao();
		Integer maxRows = new Integer(1000);
		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		Integer claimNumber = new Integer(27633);
		
		PagedDtos<ClaimDto> dtos = dao.select(claimNumber, null, null, null, null, maxRows, pageNumber, pageRowCount);
		Assert.assertNotNull(dtos);

	}
	
	@Test
	public void testSelectListByPolicyNumber() throws DaoException, TooManyRecordsException {
		
		ClaimDao dao = persistenceSpringConfig.claimDao();
		Integer maxRows = new Integer(1000);
		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		String policyNumber = "701141-20";
		
		PagedDtos<ClaimDto> dtos = dao.select(null, policyNumber, null, null, null, maxRows, pageNumber, pageRowCount);
		Assert.assertNotNull(dtos);

	}
	
	@Test
	public void testSelectListByCalculationStatus() throws DaoException, TooManyRecordsException {
		
		ClaimDao dao = persistenceSpringConfig.claimDao();
		Integer maxRows = new Integer(1000);
		Integer pageNumber = new Integer(0);
		Integer pageRowCount = new Integer(100);
		String calculationStatus = "DRAFT";
		
		PagedDtos<ClaimDto> dtos = dao.select(null, null, calculationStatus, null, null, maxRows, pageNumber, pageRowCount);
		Assert.assertNotNull(dtos);

	}
	
	
	
	@Test 
	public void testInsertDeleteClaim() throws Exception {

		ClaimDao dao = persistenceSpringConfig.claimDao();
		
		Integer newColId = 7;

		ClaimDto dto = insertClaim(dao, newColId);

		Assert.assertEquals(Boolean.TRUE, dto.getHasChequeReqInd());
		
		//Test fails if delete fails, no assert necessary
		dao.delete(newColId);

	}

	private ClaimDto insertClaim(ClaimDao dao, Integer newColId) throws DaoException, NotFoundDaoException {
		//delete if it already exists
		ClaimDto dto = dao.fetch(newColId);
		if (dto != null) {
			dao.delete(newColId);
		}

		ClaimDto newDto = new ClaimDto();
		
		newDto.setColId(newColId);
		newDto.setClaimNumber(999999);
		newDto.setClaimStatusCode("OPEN");
		newDto.setIppId(1);
		newDto.setCommodityCoverageCode("CQNT");
		newDto.setCropCommodityId(2);
		newDto.setIplId(3);
		newDto.setPolicyNumber("123123-12");
		newDto.setInsurancePlanId(6);
		newDto.setCropYear(2021);
		newDto.setContractId(4);
		newDto.setIgId(5);
		newDto.setGrowerName("TEST Name");
		newDto.setClaimDataSyncTransDate(new Date());
		newDto.setPolicyDataSyncTransDate(new Date());
		newDto.setGrowerDataSyncTransDate(new Date());
		newDto.setSubmittedByDate(new Date());
		newDto.setSubmittedByName("Submitter");
		newDto.setSubmittedByUserid("Sub");
		newDto.setRecommendedByDate(new Date());
		newDto.setRecommendedByName("Recommender");
		newDto.setRecommendedByUserid("Rec");
		newDto.setApprovedByDate(new Date());
		newDto.setApprovedByName("Approver");
		newDto.setApprovedByUserid("Apr");
		newDto.setHasChequeReqInd(true);
		
		String userId = "JUNIT_TEST";

		dao.insert(newDto, userId);
		
		return dao.fetch(newColId);
		
	}
	
	
	
	@Test
	public void testUpdateClaim() throws Exception {

		ClaimDao dao = persistenceSpringConfig.claimDao();
		ClaimDto dto = dao.fetch(colId);
		
		ClaimDto dtoUpdate = new ClaimDto();
		
		Assert.assertNotNull(dto); 
		
		dtoUpdate.setColId(dto.getColId());
		dtoUpdate.setClaimNumber(dto.getClaimNumber());
		dtoUpdate.setClaimStatusCode("IN PROGRESS");
		dtoUpdate.setIppId(1);
		dtoUpdate.setCommodityCoverageCode("CPLANT");
		dtoUpdate.setCropCommodityId(5);
		dtoUpdate.setIplId(2);
		dtoUpdate.setPolicyNumber("123123-12");
		dtoUpdate.setInsurancePlanId(3);
		dtoUpdate.setCropYear(2020);
		dtoUpdate.setContractId(4);
		dtoUpdate.setIgId(5);
		dtoUpdate.setGrowerName("TEST");
		dtoUpdate.setClaimDataSyncTransDate(dto.getClaimDataSyncTransDate());
		dtoUpdate.setPolicyDataSyncTransDate(dto.getPolicyDataSyncTransDate());
		dtoUpdate.setGrowerDataSyncTransDate(dto.getGrowerDataSyncTransDate());
		dtoUpdate.setSubmittedByDate(new Date());
		dtoUpdate.setSubmittedByName("Submitter");
		dtoUpdate.setSubmittedByUserid("Sub");
		dtoUpdate.setRecommendedByDate(new Date());
		dtoUpdate.setRecommendedByName("Recommender");
		dtoUpdate.setRecommendedByUserid("Rec");
		dtoUpdate.setApprovedByDate(new Date());
		dtoUpdate.setApprovedByName("Approver");
		dtoUpdate.setApprovedByUserid("Apr");
		dtoUpdate.setHasChequeReqInd(true);
		
		String userId = "JUNIT_TEST";
		
		dao.update(dtoUpdate, userId);

		dtoUpdate = dao.fetch(colId);

		Assert.assertEquals(Boolean.TRUE, dtoUpdate.getHasChequeReqInd());
		
		//Reset the record to what it was before
		//Values have to be set manually because only dirty/changed fields are updated
		dtoUpdate.setClaimStatusCode(dto.getClaimStatusCode());
		dtoUpdate.setIppId(dto.getIppId());
		dtoUpdate.setCommodityCoverageCode(dto.getCommodityCoverageCode());
		dtoUpdate.setCropCommodityId(dto.getCropCommodityId());
		dtoUpdate.setIplId(dto.getIplId());
		dtoUpdate.setPolicyNumber(dto.getPolicyNumber());
		dtoUpdate.setInsurancePlanId(dto.getInsurancePlanId());
		dtoUpdate.setCropYear(dto.getCropYear());
		dtoUpdate.setContractId(dto.getContractId());
		dtoUpdate.setIgId(dto.getIgId());
		dtoUpdate.setGrowerName(dto.getGrowerName());
		dtoUpdate.setSubmittedByDate(dto.getSubmittedByDate());
		dtoUpdate.setSubmittedByName(dto.getSubmittedByName());
		dtoUpdate.setSubmittedByUserid(dto.getSubmittedByUserid());
		dtoUpdate.setRecommendedByDate(dto.getRecommendedByDate());
		dtoUpdate.setRecommendedByName(dto.getRecommendedByName());
		dtoUpdate.setRecommendedByUserid(dto.getRecommendedByUserid());
		dtoUpdate.setApprovedByDate(dto.getApprovedByDate());
		dtoUpdate.setApprovedByName(dto.getApprovedByName());
		dtoUpdate.setApprovedByUserid(dto.getApprovedByUserid());
		dtoUpdate.setHasChequeReqInd(dto.getHasChequeReqInd());
		
		dao.update(dtoUpdate, userId);

	}
	
	
	@Test
	public void testUpdateClaimData() throws Exception {
		ClaimDao dao = persistenceSpringConfig.claimDao();
		ClaimDto dto = dao.fetch(colId);
		
		ClaimDto dtoUpdate = new ClaimDto();
		
		Assert.assertNotNull(dto); 
		
		dtoUpdate.setColId(dto.getColId());
		dtoUpdate.setClaimStatusCode("OPEN");
		dtoUpdate.setHasChequeReqInd(true);
		
		String userId = "JUNIT_UPDATE_CD_TEST_1";
		Date claimTransactionDate = addSeconds(dto.getClaimDataSyncTransDate(), 1);
		dtoUpdate.setClaimDataSyncTransDate(claimTransactionDate);
		dao.updateClaimData(dtoUpdate, userId);

		ClaimDto fetchedDto = dao.fetch(colId);

		Assert.assertEquals(dtoUpdate.getHasChequeReqInd(), fetchedDto.getHasChequeReqInd());
		
		dtoUpdate.setColId(dto.getColId());
		dtoUpdate.setClaimStatusCode("OPEN");
		
		//Expect NO update becaus the transaction date is before the latest update
		userId = "JUNIT_UPDATE_CD_TEST_2";
		claimTransactionDate = addSeconds(dto.getClaimDataSyncTransDate(), 1);
		dtoUpdate.setClaimDataSyncTransDate(claimTransactionDate);
		dao.updateClaimData(dtoUpdate, userId);

		//Expect NO update becaus the transaction date is before the latest update
		dto.setClaimStatusCode("IN PROGRESS");
		userId = "JUNIT_UPDATE_CD_TEST_3";
		claimTransactionDate = addSeconds(dto.getClaimDataSyncTransDate(), -1);
		dto.setClaimDataSyncTransDate(claimTransactionDate);
		dao.updateClaimData(dto, userId);

		//Reset the record to what it was before
		//Values have to be set manually because only dirty/changed fields are updated
		dto.setClaimStatusCode("IN PROGRESS");
		dto.setHasChequeReqInd(false);
		userId = "JUNIT_UPDATE_CD_TEST_4";
		claimTransactionDate = addSeconds(dto.getClaimDataSyncTransDate(), 2);
		dto.setClaimDataSyncTransDate(claimTransactionDate);
		dao.updateClaimData(dto, userId);

	}
	
	@Test
	public void testUpdatePolicyData() throws Exception {
		
		ClaimDao dao = persistenceSpringConfig.claimDao();
		
		Integer newColId = 7;

		ClaimDto dto = insertClaim(dao, newColId);
		
		Assert.assertNotNull(dto); 


		Integer originalIgId = dto.getIgId();
		Integer newIgId = 11111;
		Date policyTransactionDate = addSeconds(dto.getPolicyDataSyncTransDate(), 1);
		
		dto.setPolicyDataSyncTransDate(policyTransactionDate);
		dto.setIgId(newIgId);
		
		//Expect update
		String userId = "JUNIT_UPDATE_P_TEST_1";
		dao.updatePolicyData(dto, userId);

		dto = dao.fetch(newColId);
		Assert.assertEquals("PolicyDataSyncTransDate 1", policyTransactionDate, dto.getPolicyDataSyncTransDate());
		Assert.assertEquals("GrowerId 1", newIgId, dto.getIgId());
		
		//Expect NO update because ig id didn't change
		userId = "JUNIT_UPDATE_P_TEST_2";
		Date policyTransactionDate2 = addSeconds(policyTransactionDate, 1);
		dto.setPolicyDataSyncTransDate(policyTransactionDate2);
		dao.updatePolicyData(dto, userId);

		dto = dao.fetch(newColId);
		Assert.assertEquals("PolicyDataSyncTransDate 2", policyTransactionDate, dto.getPolicyDataSyncTransDate());
		Assert.assertEquals("GrowerId 2", newIgId, dto.getIgId());
		
		
		//Expect NO update becaus the transaction date is before the latest update
		userId = "JUNIT_UPDATE_P_TEST_3";
		Date policyTransactionDate3 = addSeconds(policyTransactionDate, -1);
		dto.setPolicyDataSyncTransDate(policyTransactionDate3);
		dto.setIgId(originalIgId);
		dao.updatePolicyData(dto, userId);
		
		dto = dao.fetch(newColId);
		Assert.assertEquals("PolicyDataSyncTransDate 3", policyTransactionDate, dto.getPolicyDataSyncTransDate());
		Assert.assertEquals("GrowerId 3", newIgId, dto.getIgId());

		//Update expected
		userId = "JUNIT_UPDATE_P_TEST_4";
		Date policyTransactionDate4 = addSeconds(policyTransactionDate, 1);
		dto.setPolicyDataSyncTransDate(policyTransactionDate4);
		dto.setIgId(originalIgId);
		dao.updatePolicyData(dto, userId);
		
		dto = dao.fetch(newColId);
		Assert.assertEquals("PolicyDataSyncTransDate 4", policyTransactionDate4, dto.getPolicyDataSyncTransDate());
		Assert.assertEquals("GrowerId 4", originalIgId, dto.getIgId());
		
		dao.delete(newColId);
	}
	
	@Test
	public void testUpdateGrowerData() throws Exception {
		//get the existing record to set the transaction date
		ClaimDao dao = persistenceSpringConfig.claimDao();
		
		Integer newColId = 7;

		ClaimDto dto = insertClaim(dao, newColId);
		
		Assert.assertNotNull(dto); 		
		
		String originalGrowerName = dto.getGrowerName();
		String newGrowerName = "New Grower";
		Date growerTransactionDate = addSeconds(dto.getGrowerDataSyncTransDate(), 1);
		
		dto.setGrowerDataSyncTransDate(growerTransactionDate);
		dto.setGrowerName(newGrowerName);

		//Expect update
		String userId = "JUNIT_UPDATE_G_TEST_1";
		dao.updateGrowerData(dto, userId);
		
		dto = dao.fetch(newColId);
		Assert.assertEquals("GrowerDataSyncTransDate 1", growerTransactionDate, dto.getGrowerDataSyncTransDate());
		Assert.assertEquals("GrowerName 1", newGrowerName, dto.getGrowerName());
		
		//Expect NO update because grower name didn't change
		userId = "JUNIT_UPDATE_G_TEST_2";
		Date growerTransactionDate2 = addSeconds(dto.getGrowerDataSyncTransDate(), 1);
		dto.setGrowerDataSyncTransDate(growerTransactionDate2);
		dao.updateGrowerData(dto, userId);
		
		dto = dao.fetch(newColId);
		Assert.assertEquals("GrowerDataSyncTransDate 2", growerTransactionDate, dto.getGrowerDataSyncTransDate());
		Assert.assertEquals("GrowerName 2", newGrowerName, dto.getGrowerName());

		//Expect NO update becaus the transaction date is before the latest update
		userId = "JUNIT_UPDATE_G_TEST_3";
		Date growerTransactionDate3 = addSeconds(dto.getGrowerDataSyncTransDate(), -1);
		dto.setGrowerDataSyncTransDate(growerTransactionDate3);
		dto.setGrowerName(originalGrowerName);
		dao.updateGrowerData(dto, userId);
		
		dto = dao.fetch(newColId);
		Assert.assertEquals("GrowerDataSyncTransDate 4", growerTransactionDate, dto.getGrowerDataSyncTransDate());
		Assert.assertEquals("GrowerName 4", newGrowerName, dto.getGrowerName());

		//Expect update
		userId = "JUNIT_UPDATE_G_TEST_4";
		Date growerTransactionDate4 = addSeconds(dto.getGrowerDataSyncTransDate(), 2);
		dto.setGrowerDataSyncTransDate(growerTransactionDate4);
		dto.setGrowerName(originalGrowerName);
		dao.updateGrowerData(dto, userId);
		
		dto = dao.fetch(newColId);
		Assert.assertEquals("GrowerDataSyncTransDate 4", growerTransactionDate4, dto.getGrowerDataSyncTransDate());
		Assert.assertEquals("GrowerName 4", originalGrowerName, dto.getGrowerName());
		
		
		dao.delete(newColId);
		
	}
		
	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}
	
}
