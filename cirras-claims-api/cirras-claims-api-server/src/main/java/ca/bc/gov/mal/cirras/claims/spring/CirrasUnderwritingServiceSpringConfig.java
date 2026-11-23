package ca.bc.gov.mal.cirras.claims.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.target.HotSwappableTargetSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.bc.gov.mal.cirras.underwriting.clients.CirrasUnderwritingService;


@Configuration
public class CirrasUnderwritingServiceSpringConfig  {

	private static final Logger logger = LoggerFactory.getLogger(CirrasUnderwritingServiceSpringConfig.class);
	
	public CirrasUnderwritingServiceSpringConfig() {
		logger.info("<CirrasUnderwritingServiceSpringConfig");
		
		logger.info(">CirrasUnderwritingServiceSpringConfig");
	}

	@Value("${CIRRAS_CLAIMS_REST_CLIENT_ID}")
	private String keycloakClientId;

	@Value("${CIRRAS_CLAIMS_REST_SECRET}")
	private String keycloakClientSecret;

	@Value("${KEYCLOAK_TOKEN_ENDPOINT}")
	private String keycloakTokenEndpoint;
	
	@Value("${CIRRAS_UNDERWRITING_API_URL}")
	private String cirrasUnderwritingServiceTopLevelRestURL;
	
	// I don't know if this should be a preference or not
	private String scopes = "CIRRAS_UNDERWRITING.*";
	
	
	@Bean
	public CirrasUnderwritingService cirrasUnderwritingServiceImpl() {
		CirrasUnderwritingService serviceImpl = new CirrasUnderwritingService(keycloakClientId, keycloakClientSecret, keycloakTokenEndpoint, scopes);
		
		serviceImpl.setTopLevelRestURL(cirrasUnderwritingServiceTopLevelRestURL);

		return serviceImpl;
	}
	

	
	@Bean
	public HotSwappableTargetSource swappableCirrasUnderwritingService() {
		HotSwappableTargetSource result = new HotSwappableTargetSource(cirrasUnderwritingServiceImpl());
		
		return result;
	}

	
	@Bean
	public ProxyFactoryBean cirrasUnderwritingService() {
		ProxyFactoryBean result = new ProxyFactoryBean();
		
		result.setTargetSource(swappableCirrasUnderwritingService());
		
		return result;
	}
}
