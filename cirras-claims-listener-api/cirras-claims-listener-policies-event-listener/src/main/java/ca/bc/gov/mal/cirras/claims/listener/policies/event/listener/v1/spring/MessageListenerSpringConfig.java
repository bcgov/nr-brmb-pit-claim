package ca.bc.gov.mal.cirras.claims.listener.policies.event.listener.v1.spring;

import java.time.Duration;
import java.util.Properties;

import jakarta.mail.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ca.bc.gov.mal.cirras.claims.listener.failover.service.api.v1.async.FailOverService;
import ca.bc.gov.mal.cirras.claims.listener.policies.event.listener.v1.CirrasClaimsPoliciesEventListenerImpl;
import ca.bc.gov.mal.cirras.claims.listener.policies.event.listener.v1.EventListener;
import ca.bc.gov.mal.cirras.claims.listener.policies.event.listener.v1.FailoverEventListener;
import ca.bc.gov.mal.cirras.claims.listener.policies.event.listener.v1.NatsAuthHandler;
import ca.bc.gov.mal.cirras.claims.listener.policies.event.listener.v1.NatsConnectionListener;
import ca.bc.gov.mal.cirras.claims.listener.policies.event.listener.v1.NatsErrorListener;
import ca.bc.gov.mal.cirras.claims.listener.service.api.v1.spring.ServiceApiSpringConfig;
import io.nats.client.Options;

@Configuration
@Import({ 
	ServiceApiSpringConfig.class,
	FailoverServiceSpringConfig.class
})
public class MessageListenerSpringConfig {

	static final Logger logger = LoggerFactory.getLogger(MessageListenerSpringConfig.class);

	@Value("${message-queue.url}")
	private String natsURL;
	
	@Value("${message-queue.claims.sync.rest.nkey.seed}")
	private String nkeySeed;
	
	@Autowired
	private ServiceApiSpringConfig serviceApiSpringConfig;

	@Autowired
	private FailOverService failOverService;

	public MessageListenerSpringConfig() {
		logger.debug("<MessageListenerSpringConfig");

		logger.debug(">MessageListenerSpringConfig");
	}

	@Bean 
	Options natsConnectionOptions() {

		logger.info("NATS Server URL: " + natsURL);

		NatsAuthHandler authHandler = new NatsAuthHandler(nkeySeed);
		NatsConnectionListener connListener = new NatsConnectionListener();
		NatsErrorListener errListener = new NatsErrorListener();

		Options.Builder builder = new Options.Builder()
                .server(natsURL)
                .connectionTimeout(Duration.ofSeconds(10))
                .pingInterval(Duration.ofSeconds(30))
                .reconnectWait(Duration.ofSeconds(10))
                .authHandler(authHandler)
                .connectionListener(connListener)
                .errorListener(errListener)
                .maxReconnects(3);
		
        Options options = builder.build();

        return options;

	}
	
	
	@Value("${email.error.send.frequency}")
	private String emailFrequency;
	
	public long emailFrequency() {
		long result = 10*1000*60;
		
		if(emailFrequency!=null) {
			
			result = Long.valueOf(emailFrequency).longValue() * 60 * 1000;
		}
		
		return result;
	}

	@Value("${default.application.environment}")
	private String environment;
	
	private static final String ENVIRONMENT_PLACE_HOLDER = "%environment%";

	@Value("${email.claims.listener.synch.error.subject}")
	private String emailSubjectTemplate;
	
	private String emailSubject() {
		String result;
		
		result = emailSubjectTemplate.replace(ENVIRONMENT_PLACE_HOLDER, environment);
		
		return result;
	}

	@Value("${email.admin.address}")
	private String rawAddresses;

	@Value("${email.host.name}")
	private String emailHostName;

	@Value("${email.port}")
	private String emailPort;

	@Value("${email.from.address}")
	private String emailFromAddress;

	private Session emailSession() {
		logger.debug("<emailSession");
		Session result;
		
		Properties mailProperties = new Properties();
		mailProperties.setProperty("mail.smtp.host", emailHostName);
		mailProperties.setProperty("mail.smtp.port", emailPort);
		mailProperties.setProperty("mail.from.address", emailFromAddress);
		
		result = Session.getDefaultInstance(mailProperties);
		
		logger.debug(">emailSession");
		return result;
	}
	
	@Bean
	public EventListener policiesEventListener() {
		CirrasClaimsPoliciesEventListenerImpl result;
		
		result = new CirrasClaimsPoliciesEventListenerImpl();
		
		result.setMessageQueueOptions(natsConnectionOptions());
		result.setMessageQueueConsumer("claims_sync_rest_consumer");
		result.setMessageQueueStream("policies-event-channel");
		result.setInitialRedeliveryDelaySeconds(5);
		result.setReceiveTimeoutMilis(1000);
		result.setCirrasClaimsPoliciesListenerService(serviceApiSpringConfig.cirrasClaimsPoliciesListenerService());
		result.setEmailFrequency(emailFrequency());
		result.setEmailSession(emailSession());
		result.setEmailSubject(emailSubject());
		result.setRawAddresses(rawAddresses);
		result.setEmailFrom(emailFromAddress);
		
		return result;
	}
	
	@Bean
	public FailoverEventListener failoverEventListener() {
		FailoverEventListener result;
		
		result = new FailoverEventListener(
				failOverService, 
				policiesEventListener());
		
		return result;
	}

}
