package ca.bc.gov.mal.cirras.claims.listener.underwriting.event.listener.v1.spring;

import javax.sql.DataSource;

import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.target.HotSwappableTargetSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import ca.bc.gov.mal.cirras.claims.listener.failover.persistence.v1.spring.FailoverPersistenceSpringConfig;
import ca.bc.gov.mal.cirras.claims.listener.failover.service.api.v1.async.FailOverService;
import ca.bc.gov.mal.cirras.claims.listener.failover.service.api.v1.async.FailOverServiceImpl;

@Configuration
@Import({ FailoverPersistenceSpringConfig.class })
public class UnderwritingFailoverServiceSpringConfig {

	@Autowired private FailoverPersistenceSpringConfig failoverPersistenceSpringConfig;
	
	@Bean
	public FailOverService underwritingFailOverServiceImpl() {
		FailOverServiceImpl result;
		
		result = new FailOverServiceImpl();
		result.setSyncOwnershipDao(failoverPersistenceSpringConfig.syncOwnershipDao());
		
		return result;
	}

	// The client is made hot swappable to support JUNIT tests.
	@Bean
	public HotSwappableTargetSource swappableUnderwritingFailOverService() {
		HotSwappableTargetSource result;

		result = new HotSwappableTargetSource(underwritingFailOverServiceImpl());
		return result;
	}

	// The swappable token client is proxied to support JUNIT tests.
	@Bean
	public ProxyFactoryBean underwritingFailOverService() {
		ProxyFactoryBean result;

		result =  new ProxyFactoryBean();
		result.setTargetSource(swappableUnderwritingFailOverService());
		return result;
	}

}
