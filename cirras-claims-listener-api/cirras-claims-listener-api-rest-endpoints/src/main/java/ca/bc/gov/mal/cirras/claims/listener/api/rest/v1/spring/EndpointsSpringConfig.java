package ca.bc.gov.mal.cirras.claims.listener.api.rest.v1.spring;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import ca.bc.gov.mal.cirras.claims.listener.api.rest.v1.parameters.validation.ParameterValidator;
import ca.bc.gov.mal.cirras.claims.listener.policies.event.listener.v1.spring.MessageListenerSpringConfig;
import ca.bc.gov.mal.cirras.claims.listener.service.api.v1.spring.ServiceApiSpringConfig;
import ca.bc.gov.mal.cirras.claims.listener.underwriting.event.listener.v1.spring.UnderwritingMessageListenerSpringConfig;
import ca.bc.gov.nrs.wfone.common.checkhealth.CheckHealthValidator;
import ca.bc.gov.nrs.wfone.common.checkhealth.CompositeValidator;
import ca.bc.gov.nrs.wfone.common.utils.ApplicationContextProvider;

@Configuration
@Import({
	PropertiesSpringConfig.class,
	ServiceApiSpringConfig.class,
	ResourceFactorySpringConfig.class,
	SecuritySpringConfig.class,
	MessageListenerSpringConfig.class,
	UnderwritingMessageListenerSpringConfig.class,
	CirrasPolicyServiceSpringConfig.class,
	CirrasClaimServiceSpringConfig.class
})
public class EndpointsSpringConfig {

	private static final Logger logger = LoggerFactory.getLogger(EndpointsSpringConfig.class);
	
	public EndpointsSpringConfig() {
		logger.info("<EndpointsSpringConfig");
		
		logger.info(">EndpointsSpringConfig");
	}
	
	@Bean
	public ApplicationContextProvider applicationContextProvider() {
		ApplicationContextProvider result;
		
		result = new ApplicationContextProvider();
		
		return result;
	}

	@Primary
	@Bean
	public DataSource failoverDataSource() {
		DataSource result;
		
		final JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
	    dsLookup.setResourceRef(true);
	    result = dsLookup.getDataSource("java:comp/env/jdbc/failover");
	    
	    return result;
	}
	
	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource result;
		
		result = new ResourceBundleMessageSource();
		result.setBasename("messages");
		
		return result;
	}

	@Bean
	public ParameterValidator parameterValidator() {
		ParameterValidator result;
		
		result = new ParameterValidator();
		result.setMessageSource(messageSource());
		
		return result;
	}

	@Bean(initMethod="init")
	public CompositeValidator checkHealthValidator() {
		CompositeValidator result;
		
		result = new CompositeValidator();
		// TODO MH 2022/01/19: Not sure this is correct
		result.setComponentIdentifier("CIRRAS_CLAIM_LISTENER_API");
		result.setComponentName("CIRRAS Claim Listener API");
		result.setValidators(healthCheckValidators());
		
		return result;
	}

	@Bean()
	public List<CheckHealthValidator> healthCheckValidators() {
		List<CheckHealthValidator> result = new ArrayList<>();
		
		return result;
	}
	
}
