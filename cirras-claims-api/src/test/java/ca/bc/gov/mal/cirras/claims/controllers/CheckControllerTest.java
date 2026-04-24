package ca.bc.gov.mal.cirras.claims.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.target.HotSwappableTargetSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ca.bc.gov.mal.cirras.claims.jetty.EmbeddedServer;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.stub.TokenServiceStub;
import ca.bc.gov.nrs.wfone.common.utils.ApplicationContextProvider;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.resource.CheckedToken;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;


public class CheckControllerTest {
//	private static final Logger logger = LoggerFactory.getLogger(CheckControllerTest.class);
//	
//	protected static boolean skipTests = false;
//
//	protected static final int port = 8889;
//	protected static final String contextPath = "/cirras-claims-api/v1";
//	protected static final String topLevelRestURL = "http://localhost:" + port + contextPath + "/";
//
//	protected static ApplicationContext testApplicationContext;
//	protected static ApplicationContext webApplicationContext;
//	
//	protected static final String WebadeOauth2ClientGuid = "9374JD83HD94JSLE893H3N58DJE74999";
//	protected static final String WebadeOauth2ClientId = "TEST_SERVICE_CLIENT";
//	protected static final String WebadeOauth2ClientSecret = "password";
//
//	protected static final String clientAppCode = "CIRRAS_CLAIMS_REST";
//	protected static final String issuer = "http://www.webade-oauth2-stub.com/webade-oauth2";
//
//	protected static TokenServiceStub tokenService;
//	
//	@BeforeClass
//	public static void startServer() throws Exception {
//		logger.debug("<startServer");
//		
//		if(skipTests) {
//			logger.warn("Skipping tests");
//			return;
//		}
//
//		System.setProperty("webade-bootstrap-override-directory-location", "src/test/resources");
//		System.setProperty("user-info-file-location", "src/test/resources/webade-xml-user-info.xml");
//		
//		testApplicationContext = new ClassPathXmlApplicationContext(new String[] { "classpath:/test-spring-config.xml" });
//		
//		Map<String, DataSource> dataSources = new HashMap<String, DataSource>();
//		{
//			DataSource dataSource = testApplicationContext.getBean("cirrasClaimsDataSource", DataSource.class);
//			dataSources.put("jdbc/cirras_claims_rest", dataSource);
//		}
////		{
////			DataSource dataSource = testApplicationContext.getBean("bootstrapDataSource", DataSource.class);
////			dataSources.put("jdbc/webade_bootstrap", dataSource);
////		}
//
//		EmbeddedServer.startIfRequired(port, contextPath, dataSources);
//
//		// Replace the OAUTH2 token client with the stub
//		webApplicationContext = ApplicationContextProvider.getApplicationContext();
//		Assert.assertNotNull(webApplicationContext);
//		
//		{
//			HotSwappableTargetSource swappableTokenService = webApplicationContext.getBean("swappableTokenService", HotSwappableTargetSource.class);
//			Assert.assertNotNull(swappableTokenService);
//	
//			tokenService = new TokenServiceStub(clientAppCode, issuer);
//	
//			swappableTokenService.swap(tokenService);
//		}

//		logger.debug(">startServer");
//	}

//	@AfterClass
//	public static void stopServer() throws Exception {
//		EmbeddedServer.stop();
//		logger.debug("stopServer");
//	}
//	
//	
//	@Test
//	public void testCheckToken() throws Exception {
//		logger.debug("<testSwagger");
//		
//		if(skipTests) {
//			logger.warn("Skipping tests");
//			return;
//		}
//		// somehow call CheckTokenController.token and pass WebadeOauth2ClientGuid, WebadeOauth2ClientGuid and WebadeOauth2ClientSecret
//		
		
		
//		// 1. Get the controller from your context or instantiate it
//	    CheckTokenController controller = new CheckTokenController();
//
//	    // 2. Manually inject the @Value fields since you aren't using a full Spring runner
//	    ReflectionTestUtils.setField(controller, "webadeOauth2ClientId", WebadeOauth2ClientId);
//	    ReflectionTestUtils.setField(controller, "webadeOauth2ClientSecret", WebadeOauth2ClientSecret);
//	    // Point this to your local stub server (e.g., the one started by EmbeddedServer)
//	    ReflectionTestUtils.setField(controller, "webadeOauth2CheckTokenUrl", topLevelRestURL + "checkToken");
//	    ReflectionTestUtils.setField(controller, "webadeOauth2TokenUrl", topLevelRestURL + "getToken");
//
//	    // 3. Prepare Mock Request/Response (provided by spring-test)
//	    MockHttpServletRequest request = new MockHttpServletRequest();
//	    MockHttpServletResponse response = new MockHttpServletResponse();
//
//	    String testToken = "valid-test-token";
//	    request.addHeader("Authorization", "Bearer " + testToken);
//
//	    // 4. Execute
//	//    CheckedToken result = controller.token(request, response);
//
//	    // 5. Assert
//	    Assert.assertNotNull("Result should not be null", result);
//	    // Add more assertions based on what your TokenServiceStub returns
//	    
	    
//	}
}
