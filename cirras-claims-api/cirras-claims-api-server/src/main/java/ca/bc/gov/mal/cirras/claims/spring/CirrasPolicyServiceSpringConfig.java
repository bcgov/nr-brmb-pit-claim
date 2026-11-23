package ca.bc.gov.mal.cirras.claims.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.target.HotSwappableTargetSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.bc.gov.mal.cirras.policies.api.rest.client.v1.CirrasPolicyService;
import ca.bc.gov.mal.cirras.policies.api.rest.client.v1.impl.CirrasPolicyServiceImpl;


@Configuration
public class CirrasPolicyServiceSpringConfig  {

	private static final Logger logger = LoggerFactory.getLogger(CirrasPolicyServiceSpringConfig.class);
	
	public CirrasPolicyServiceSpringConfig() {
		logger.info("<CirrasPolicyServiceSpringConfig");
		
		logger.info(">CirrasPolicyServiceSpringConfig");
	}

	@Value("${CIRRAS_CLAIMS_REST_CLIENT_ID}")
	private String keycloakClientId;

	@Value("${CIRRAS_CLAIMS_REST_SECRET}")
	private String keycloakClientSecret;

	@Value("${KEYCLOAK_TOKEN_ENDPOINT}")
	private String keycloakTokenEndpoint;
	
	@Value("${CIRRAS_POLICIES_API_URL}")
	private String cirrasPolicyServiceTopLevelRestURL;
	
	// I don't know if this should be a preference or not
	private String scopes = "CIRRAS_CLAIMS.*";
	
	
	@Bean
	public CirrasPolicyService cirrasPolicyServiceImpl() {
		CirrasPolicyServiceImpl serviceImpl = new CirrasPolicyServiceImpl(keycloakClientId, keycloakClientSecret, keycloakTokenEndpoint, scopes);
		
		serviceImpl.setTopLevelRestURL(cirrasPolicyServiceTopLevelRestURL);

		return serviceImpl;
	}
	

	
	@Bean
	public HotSwappableTargetSource swappableCirrasPolicyService() {
		HotSwappableTargetSource result = new HotSwappableTargetSource(cirrasPolicyServiceImpl());
		
		return result;
	}

	
	@Bean
	public ProxyFactoryBean cirrasPolicyService() {
		ProxyFactoryBean result = new ProxyFactoryBean();
		
		result.setTargetSource(swappableCirrasPolicyService());
		
		return result;
	}
}
