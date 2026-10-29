package ca.bc.gov.mal.cirras.claims.listener.service.api.v1.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import ca.bc.gov.mal.cirras.claims.clients.CirrasClaimService;
import ca.bc.gov.mal.cirras.claims.listener.service.api.v1.CirrasClaimsPoliciesListenerService;
import ca.bc.gov.mal.cirras.claims.listener.service.api.v1.CirrasClaimsUnderwritingListenerService;
import ca.bc.gov.mal.cirras.claims.listener.service.api.v1.impl.CirrasClaimsPoliciesListenerServiceImpl;
import ca.bc.gov.mal.cirras.claims.listener.service.api.v1.impl.CirrasClaimsUnderwritingListenerServiceImpl;
import ca.bc.gov.mal.cirras.claims.listener.service.api.v1.model.factory.SyncClaimsPoliciesFactory;
import ca.bc.gov.mal.cirras.claims.listener.service.api.v1.model.factory.SyncClaimsUnderwritingFactory;
import ca.bc.gov.mal.cirras.claims.listener.service.api.v1.validation.ModelValidator;
import ca.bc.gov.mal.cirras.policies.api.rest.client.v1.CirrasPolicyService;

@Configuration
public class ServiceApiSpringConfig {

	private static final Logger logger = LoggerFactory.getLogger(ServiceApiSpringConfig.class);
	
	public ServiceApiSpringConfig() {
		logger.debug("<ServiceApiSpringConfig");
		
		logger.debug(">ServiceApiSpringConfig");
	}

	// Beans provided by EndpointsSpringConfig
	@Autowired ResourceBundleMessageSource messageSource;
	
	//@Autowired CirrasUnderwritingService cirrasUnderwritingService;
	@Autowired CirrasPolicyService cirrasPolicyService;
	@Autowired CirrasClaimService cirrasClaimService;
	@Autowired SyncClaimsPoliciesFactory syncClaimsPoliciesFactory;
	@Autowired SyncClaimsUnderwritingFactory syncClaimsUnderwritingFactory;
	
	@Bean
	public ModelValidator modelValidator() {
		ModelValidator result;
		
		result = new ModelValidator();
		result.setMessageSource(messageSource);
		
		return result;
	}

	@Bean()
	public CirrasClaimsPoliciesListenerService cirrasClaimsPoliciesListenerService() {
		CirrasClaimsPoliciesListenerServiceImpl result = new CirrasClaimsPoliciesListenerServiceImpl();
		
		result.setCirrasPolicyService(cirrasPolicyService);
		result.setCirrasClaimService(cirrasClaimService);
		
		result.setSyncClaimFactory(syncClaimsPoliciesFactory);
		
		return result;
	}
	

	@Bean()
	public CirrasClaimsUnderwritingListenerService cirrasClaimsUnderwritingListenerService() {
		CirrasClaimsUnderwritingListenerServiceImpl result = new CirrasClaimsUnderwritingListenerServiceImpl();
		
		result.setCirrasClaimService(cirrasClaimService);
		
		result.setSyncClaimsUnderwritingFactory(syncClaimsUnderwritingFactory);
		
		return result;
	}
}
