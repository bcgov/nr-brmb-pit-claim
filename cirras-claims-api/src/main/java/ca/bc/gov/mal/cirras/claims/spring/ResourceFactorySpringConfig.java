package ca.bc.gov.mal.cirras.claims.spring;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ca.bc.gov.mal.cirras.claims.data.assemblers.ClaimCalculationRsrcFactory;
import ca.bc.gov.mal.cirras.claims.data.assemblers.ClaimRsrcFactory;
import ca.bc.gov.mal.cirras.claims.data.assemblers.CirrasDataSyncRsrcFactory;


@Configuration
public class ResourceFactorySpringConfig {

	private static final Logger logger = LoggerFactory.getLogger(ResourceFactorySpringConfig.class);
	
	public ResourceFactorySpringConfig() {
		logger.info("<ResourceFactorySpringConfig");
		
		logger.info(">ResourceFactorySpringConfig");
	}

	
	@Bean
	public ClaimRsrcFactory claimRsrcFactory() {
		ClaimRsrcFactory result = new ClaimRsrcFactory();
		return result;
	}

	@Bean
	public ClaimCalculationRsrcFactory claimCalculationRsrcFactory() {
		ClaimCalculationRsrcFactory result = new ClaimCalculationRsrcFactory();
		return result;
	}
	
	@Bean
	public CirrasDataSyncRsrcFactory cirrasDataSyncRsrcFactory() {
		CirrasDataSyncRsrcFactory result = new CirrasDataSyncRsrcFactory();
		return result;
	}
}
