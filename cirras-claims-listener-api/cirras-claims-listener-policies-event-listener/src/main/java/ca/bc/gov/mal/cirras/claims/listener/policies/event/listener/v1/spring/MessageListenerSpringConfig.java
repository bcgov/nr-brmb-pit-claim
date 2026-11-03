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

	@Value("${MESSAGE_QUEUE_URL}")
	private String natsURL;
	
	@Value("${MESSAGE_QUEUE_CLAIMS_SYNC_REST_NKEY_SEED}")
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
	
	
	@Value("${EMAIL_ERROR_SEND_FREQUENCY}")
	private String emailFrequency;
	
	public long emailFrequency() {
		long result = 10*1000*60;
		
		if(emailFrequency!=null) {
			
			result = Long.valueOf(emailFrequency).longValue() * 60 * 1000;
		}
		
		return result;
	}

	@Value("${APPLICATION_ENVIRONMENT_NAME}")
	private String environment;
	
	private static final String ENVIRONMENT_PLACE_HOLDER = "%environment%";

	@Value("${EMAIL_CLAIMS_LISTENER_SYNCH_ERROR_SUBJECT}")
	private String emailSubjectTemplate;
	
	private String emailSubject() {
		String result;
		
		result = emailSubjectTemplate.replace(ENVIRONMENT_PLACE_HOLDER, environment);
		
		return result;
	}

	@Value("${EMAIL_ADMIN_ADDRESS}")
	private String rawAddresses;

	@Value("${EMAIL_HOST_NAME}")
	private String emailHostName;

	@Value("${EMAIL_PORT}")
	private String emailPort;

	@Value("${EMAIL_FROM_ADDRESS}")
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
