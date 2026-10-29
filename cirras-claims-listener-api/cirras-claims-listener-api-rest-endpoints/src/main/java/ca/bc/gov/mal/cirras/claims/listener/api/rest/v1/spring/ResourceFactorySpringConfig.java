package ca.bc.gov.mal.cirras.claims.listener.api.rest.v1.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.bc.gov.mal.cirras.claims.listener.api.rest.v1.resource.factory.SyncClaimsPoliciesRsrcFactory;
import ca.bc.gov.mal.cirras.claims.listener.api.rest.v1.resource.factory.SyncClaimsUnderwritingRsrcFactory;

@Configuration
public class ResourceFactorySpringConfig {

	private static final Logger logger = LoggerFactory.getLogger(ResourceFactorySpringConfig.class);
	
	public ResourceFactorySpringConfig() {
		logger.info("<ResourceFactorySpringConfig");
		
		logger.info(">ResourceFactorySpringConfig");
	}
	
	@Bean
	public SyncClaimsPoliciesRsrcFactory syncClaimsPoliciesRsrcFactory() {
		SyncClaimsPoliciesRsrcFactory result = new SyncClaimsPoliciesRsrcFactory();
		return result;
	}	
	
	@Bean
	public SyncClaimsUnderwritingRsrcFactory syncClaimsUnderwritingRsrcFactory() {
		SyncClaimsUnderwritingRsrcFactory result = new SyncClaimsUnderwritingRsrcFactory();
		return result;
	}

}
