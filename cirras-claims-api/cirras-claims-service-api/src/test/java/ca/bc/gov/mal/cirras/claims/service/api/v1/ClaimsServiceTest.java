package ca.bc.gov.mal.cirras.claims.service.api.v1;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.claims.service.api.v1.util.CirrasServiceHelper;

//public class ClaimCalculationEndpointTest extends EndpointsTest {
//	private static final Logger logger = LoggerFactory.getLogger(ClaimCalculationEndpointTest.class);

public class ClaimsServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(ClaimsServiceTest.class);
	

	@Test
	public void testCapitalization() throws Exception {
		logger.debug("<testCapitalization");
		
		CirrasServiceHelper helper = new CirrasServiceHelper();

		String capWord = helper.capitalizeEachWord("TREE FRUIT");
		
		capWord = helper.capitalizeEachWord("BEAN (WHOLE SPECIAL)");
		
		capWord = helper.capitalizeEachWord("FRUIT TREE - YEAR 1");

//		capWord = helper.capitalizeEachWord("4187 RR");
		
//		capWord = helper.capitalizeEachWord("45CM36");

		logger.debug(">testCapitalization");
		 		
	}
	
	
	
	 
}
