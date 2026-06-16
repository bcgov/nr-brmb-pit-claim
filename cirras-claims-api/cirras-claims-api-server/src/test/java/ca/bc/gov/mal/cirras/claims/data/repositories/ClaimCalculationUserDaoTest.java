package ca.bc.gov.mal.cirras.claims.data.repositories;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimCalculationUserDao;
import ca.bc.gov.mal.cirras.claims.data.entities.ClaimCalculationUserDto;
import ca.bc.gov.mal.cirras.claims.spring.PersistenceSpringConfig;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class ClaimCalculationUserDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;

	@Test
	public void testFetchInsertUpdateDelete() throws Exception {

		String guid = null;
		
		ClaimCalculationUserDao dao = persistenceSpringConfig.claimCalculationUserDao();
		
		//1. Insert record.
		ClaimCalculationUserDto dto = new ClaimCalculationUserDto();

		dto.setLoginUserGuid("CBE9F4CBCDC97AA3E053E60A0A0A2435");
		dto.setLoginUserId("JSMITH");
		dto.setLoginUserType("GOV");
		dto.setGivenName("John");
		dto.setFamilyName("Smith");

		dao.insert(dto, "JUNIT_TEST");

		guid = dto.getClaimCalculationUserGuid();

		//2. Fetch the newly inserted record.
		dto = dao.fetch(guid);
		
		Assert.assertNotNull(dto);
		Assert.assertEquals(guid, dto.getClaimCalculationUserGuid());
		Assert.assertEquals("CBE9F4CBCDC97AA3E053E60A0A0A2435", dto.getLoginUserGuid());
		Assert.assertEquals("JSMITH", dto.getLoginUserId());
		Assert.assertEquals("GOV", dto.getLoginUserType());
		Assert.assertEquals("John", dto.getGivenName());
		Assert.assertEquals("Smith", dto.getFamilyName());		
		Assert.assertEquals("JUNIT_TEST", dto.getCreateUser());
		Assert.assertEquals("JUNIT_TEST", dto.getUpdateUser());

		//3. Update record.
		dto.setGivenName("Johnny");
		dto.setFamilyName("Smitherson");

		dao.update(dto, "JUNIT_TEST2");
		
		//4. Fetch updated record.
		dto = dao.fetch(guid);
		
		Assert.assertNotNull(dto);
		Assert.assertEquals(guid, dto.getClaimCalculationUserGuid());
		Assert.assertEquals("CBE9F4CBCDC97AA3E053E60A0A0A2435", dto.getLoginUserGuid());
		Assert.assertEquals("JSMITH", dto.getLoginUserId());
		Assert.assertEquals("GOV", dto.getLoginUserType());
		Assert.assertEquals("Johnny", dto.getGivenName());
		Assert.assertEquals("Smitherson", dto.getFamilyName());		
		Assert.assertEquals("JUNIT_TEST", dto.getCreateUser());
		Assert.assertEquals("JUNIT_TEST2", dto.getUpdateUser());

		//5. Fetch by user guid.
		dto = dao.getByLoginUserGuid("CBE9F4CBCDC97AA3E053E60A0A0A2435");
		
		Assert.assertNotNull(dto);
		Assert.assertEquals(guid, dto.getClaimCalculationUserGuid());
		Assert.assertEquals("CBE9F4CBCDC97AA3E053E60A0A0A2435", dto.getLoginUserGuid());
		Assert.assertEquals("JSMITH", dto.getLoginUserId());
		Assert.assertEquals("GOV", dto.getLoginUserType());
		Assert.assertEquals("Johnny", dto.getGivenName());
		Assert.assertEquals("Smitherson", dto.getFamilyName());		
		Assert.assertEquals("JUNIT_TEST", dto.getCreateUser());
		Assert.assertEquals("JUNIT_TEST2", dto.getUpdateUser());
		
		
		//6. Delete.
		dao.delete(guid);
		
		//7. Fetch non-existent record.
		dto = dao.fetch(guid);
		Assert.assertNull(dto);
	}
}
