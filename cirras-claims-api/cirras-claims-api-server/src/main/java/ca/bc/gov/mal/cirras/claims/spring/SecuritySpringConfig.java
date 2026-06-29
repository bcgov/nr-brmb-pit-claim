package ca.bc.gov.mal.cirras.claims.spring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.TokenService;

@Configuration
@EnableWebSecurity(debug = false)
@Import({
	TokenServiceSpringConfig.class
})
public class SecuritySpringConfig  {

	private static final Logger logger = LoggerFactory.getLogger(SecuritySpringConfig.class);

	// Beans provided by TokenServiceSpringConfig
	// This allows Spring to use the proxied service
	@Autowired 
	@Qualifier("tokenService")
	TokenService tokenService;

	@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
	private String issuerUri;

	public SecuritySpringConfig() {
		super();
		logger.info("<SecuritySpringConfig");
		
		logger.info(">SecuritySpringConfig");
	}

	@Bean
	AuthenticationEntryPoint authenticationEntryPoint() {
		BasicAuthenticationEntryPoint result;
		
		result = new BasicAuthenticationEntryPoint();
		result.setRealmName("cirras-claims-api");
		
		return result;
	}

	@Bean
	  public WebSecurityCustomizer webSecurityCustomizer() {
		return (web)-> web.ignoring().requestMatchers(
	        new AntPathRequestMatcher("/openapi.*", HttpMethod.OPTIONS.name()),
	        new AntPathRequestMatcher("/openapi.*", HttpMethod.GET.name()),
	        new AntPathRequestMatcher("/checkHealth", HttpMethod.OPTIONS.name()),
	        new AntPathRequestMatcher("/checkHealth", HttpMethod.GET.name())
	        );		
	  }

	@Bean
	public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation(issuerUri);
    }

	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter(AppSecurityProperties properties) {
		JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

		converter.setJwtGrantedAuthoritiesConverter(jwt -> {
			Collection<GrantedAuthority> authorities = new ArrayList<>();

			List<String> roles = jwt.getClaimAsStringList("roles");
			if (roles != null) {
				roles.forEach(role -> {
					List<String> scopes = properties.getScopesForRole(role);
					if (scopes != null) {
						scopes.forEach(scope -> authorities.add(new SimpleGrantedAuthority("CIRRAS_CLAIMS." + scope)));
					}
				});
			}

			return authorities;
		});

		return converter;
	}
	
	  @Bean
	  public SecurityFilterChain filterChain(HttpSecurity http, AppSecurityProperties properties) throws Exception {
		http.csrf(csrf -> csrf.disable())
		  .cors(cors -> cors.disable())
	      .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter(properties))) )
	      .authorizeHttpRequests(authorize -> authorize
	              .requestMatchers(HttpMethod.OPTIONS, "/openapi.*", "/checkHealth").permitAll()
	              .requestMatchers(HttpMethod.GET, "/openapi.*", "/checkHealth").permitAll()
	              .requestMatchers("/**").hasAuthority("CIRRAS_CLAIMS.GET_TOP_LEVEL")
	              .anyRequest().denyAll()
	      ).exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint()) );		
		return http.build();
	  }	

}
