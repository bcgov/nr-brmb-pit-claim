package ca.bc.gov.mal.cirras.claims.persistence.v1.dao;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.PerilCodeDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.PerilCodeDto;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.CalculationStatusCodeDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.CalculationStatusCodeDto;

import ca.bc.gov.mal.cirras.claims.persistence.v1.spring.PersistenceSpringConfig;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class CodeTableTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;

	
	@Test
	public void testPerilCodeFetchAll() throws Exception {
		
		PerilCodeDao dao = persistenceSpringConfig.perilCodeDao();
		List<PerilCodeDto> perilCodeList = dao.fetchAll();
		perilCodeList.forEach((temp) -> {
            System.out.println(temp.getPerilCode() + ": " + temp.getDescription());
        });
		
		System.out.println("Total: " + perilCodeList.size());
		
		Assert.assertTrue(perilCodeList.size() > 0);
	}	
	
	@Test
	public void testPerilCodeSelectForCoverage() throws Exception {
		
		String commodityCoverageCode = "CQTF"; //Quantity Treefruit
		Integer cropCommodityId = 3; //Apple
		
		PerilCodeDao dao = persistenceSpringConfig.perilCodeDao();
		List<PerilCodeDto> perilCodeList = dao.selectForCoverage(commodityCoverageCode, cropCommodityId);
		perilCodeList.forEach((temp) -> {
            System.out.println(temp.getPerilCode() + ": " + temp.getDescription());
        });
		
		System.out.println("Total: " + perilCodeList.size());
		
		Assert.assertTrue(perilCodeList.size() > 0);
	}

	@Test
	public void testCalculationStatusCodeFetchAll() throws Exception {
		
		CalculationStatusCodeDao dao = persistenceSpringConfig.calculationStatusCodeDao();
		List<CalculationStatusCodeDto> calculationStatusCodeList = dao.fetchAll();
		calculationStatusCodeList.forEach((temp) -> {
            System.out.println(temp.getCalculationStatusCode() + ": " + temp.getDescription());
        });
		
		System.out.println("Total: " + calculationStatusCodeList.size());
		
		Assert.assertTrue(calculationStatusCodeList.size() > 0);
	}

}
