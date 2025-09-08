package ca.bc.gov.nrs.wfrm.spring;

import ca.bc.gov.nrs.wfrm.util.ChainedAuthorizationRestTemplate;
import ca.bc.gov.webade.rest.client.v1.WebADEService;
import ca.bc.gov.webade.rest.client.v1.impl.WebADEServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.target.HotSwappableTargetSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

@Configuration
public class WebadeUserServiceSpringConfig {

	private static final Logger logger = LoggerFactory.getLogger(WebadeUserServiceSpringConfig.class);

	public WebadeUserServiceSpringConfig() {
		logger.debug("<WebadeUserServiceSpringConfig");
		
		logger.debug(">WebadeUserServiceSpringConfig");
	}

	@Value("${webade-rest.url}")
	private String topLevelRestURL;


	@Bean
	@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
	public ChainedAuthorizationRestTemplate currentUserWebadeRestTemplate() {

		return new ChainedAuthorizationRestTemplate();
	}

	@Bean
	public WebADEService webadeUserServiceImpl() {
		WebADEServiceImpl result;

		logger.debug("create webadeUserServiceImpl");
		if(!this.topLevelRestURL.endsWith("/")) {
			this.topLevelRestURL = this.topLevelRestURL.concat("/");
		}

		result = new WebADEServiceImpl();
		result.setTopLevelRestURL(topLevelRestURL);
		result.setRestTemplate(currentUserWebadeRestTemplate());
		
		return result;
	}

	// The client is made hot swappable to support JUNIT tests.
	@Bean
	public HotSwappableTargetSource swappableWebadeUserService() {
		HotSwappableTargetSource result;
		logger.debug("create swapWebadeUserServiceImpl");

		result = new HotSwappableTargetSource(webadeUserServiceImpl());
		return result;
	}

	// The swappable token client is proxied to support JUNIT tests.
	@Bean
	public ProxyFactoryBean webadeUserService() {
		ProxyFactoryBean result;
		logger.debug("create webadeUserService");

		result =  new ProxyFactoryBean();
		result.setTargetSource(swappableWebadeUserService());
		return result;
	}
}
