package ca.bc.gov.mal.cirras.claims.listener.policies.event.listener.v1.spring;

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
public class FailoverServiceSpringConfig {

	@Autowired private FailoverPersistenceSpringConfig failoverPersistenceSpringConfig;

	
	@Bean
	public FailOverService failOverServiceImpl() {
		FailOverServiceImpl result;
		
		result = new FailOverServiceImpl();
		result.setSyncOwnershipDao(failoverPersistenceSpringConfig.syncOwnershipDao());
		
		return result;
	}

	// The client is made hot swappable to support JUNIT tests.
	@Bean
	public HotSwappableTargetSource swappableFailOverService() {
		HotSwappableTargetSource result;

		result = new HotSwappableTargetSource(failOverServiceImpl());
		return result;
	}

	// The swappable token client is proxied to support JUNIT tests.
	@Bean
	public ProxyFactoryBean failOverService() {
		ProxyFactoryBean result;

		result =  new ProxyFactoryBean();
		result.setTargetSource(swappableFailOverService());
		return result;
	}

}
