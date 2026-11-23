package ca.bc.gov.mal.cirras.claims.listener.api.rest.v1.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.target.HotSwappableTargetSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.bc.gov.mal.cirras.claims.clients.CirrasClaimService;

@Configuration
public class CirrasClaimServiceSpringConfig  {

	private static final Logger logger = LoggerFactory.getLogger(CirrasClaimServiceSpringConfig.class);
	
	public CirrasClaimServiceSpringConfig() {
		logger.info("<CirrasClaimServiceSpringConfig");
		
		logger.info(">CirrasClaimServiceSpringConfig");
	}

	@Value("${CIRRAS_CLAIMS_REST_CLIENT_ID}")
	private String keycloakClientId;

	@Value("${CIRRAS_CLAIMS_REST_SECRET}")
	private String keycloakClientSecret;

	@Value("${KEYCLOAK_TOKEN_ENDPOINT}")
	private String keycloakTokenEndpoint;
	
	@Value("${CIRRAS_CLAIMS_API_URL}")
	private String cirrasClaimServiceTopLevelRestURL;
	
	// I don't know if this should be a preference or not
	private String scopes = "CIRRAS_CLAIMS.*";
	
	
	@Bean
	public CirrasClaimService cirrasClaimServiceImpl() {
		CirrasClaimService serviceImpl = new CirrasClaimService(keycloakClientId, keycloakClientSecret, keycloakTokenEndpoint, scopes);
		
		serviceImpl.setTopLevelRestURL(cirrasClaimServiceTopLevelRestURL);

		return serviceImpl;
	}

	
	@Bean
	public HotSwappableTargetSource swappableCirrasClaimsService() {
		HotSwappableTargetSource result = new HotSwappableTargetSource(cirrasClaimServiceImpl());
		
		return result;
	}
	
	
	@Bean
	public ProxyFactoryBean cirrasClaimService() {
		ProxyFactoryBean result = new ProxyFactoryBean();
		
		result.setTargetSource(swappableCirrasClaimsService());
		
		return result;
	}
}
