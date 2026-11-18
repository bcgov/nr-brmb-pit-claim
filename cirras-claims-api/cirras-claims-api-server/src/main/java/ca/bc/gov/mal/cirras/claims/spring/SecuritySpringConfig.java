package ca.bc.gov.mal.cirras.claims.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity(debug = false)
public class SecuritySpringConfig  {

	private static final Logger logger = LoggerFactory.getLogger(SecuritySpringConfig.class);
	
	@Value("${KEYCLOAK_ISSUER_URI:https://dev.loginproxy.gov.bc.ca/auth/realms/standard}")
	private String issuerUri;
	
	public SecuritySpringConfig() {
		super();
		logger.info("<SecuritySpringConfig");
		
		logger.info(">SecuritySpringConfig");
	}
	
	@Bean
	public JwtDecoder jwtDecoder() {
		return JwtDecoders.fromIssuerLocation(issuerUri);
	}

	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
		converter.setJwtGrantedAuthoritiesConverter(jwt -> extractAuthorities(jwt));
		return converter;
	}

	private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
		List<GrantedAuthority> authorities = new ArrayList<>();

		Object scope = jwt.getClaim("scope");
		if (scope instanceof String) {
			for (String value : ((String) scope).split("\\s+")) {
				if (!value.isBlank()) authorities.add(new SimpleGrantedAuthority(value));
			}
		}

		Map<String, Object> realmAccess = jwt.getClaim("realm_access");
		if (realmAccess != null) {
			addRoles(authorities, realmAccess.get("roles"));
		}

		Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
		if (resourceAccess != null) {
			for (Object resource : resourceAccess.values()) {
				if (resource instanceof Map) {
					addRoles(authorities, ((Map<?, ?>) resource).get("roles"));
				}
			}
		}
		return authorities;
	}

	private void addRoles(List<GrantedAuthority> authorities, Object roles) {
		if (roles instanceof List) {
			for (Object role : (List<?>) roles) {
				authorities.add(new SimpleGrantedAuthority(role.toString()));
			}
		}
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
	        new AntPathRequestMatcher("/checkHealth", HttpMethod.GET.name()),
    		new AntPathRequestMatcher("/checkToken", HttpMethod.OPTIONS.name()),
	        new AntPathRequestMatcher("/checkToken", HttpMethod.GET.name())
	        );		
	  }
	
	  @Bean
	  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
		  .cors(cors -> cors.disable())
	      .oauth2ResourceServer(ooauth2 -> ooauth2.jwt(jwt -> jwt
	              .decoder(jwtDecoder())
	              .jwtAuthenticationConverter(jwtAuthenticationConverter())))
	      .httpBasic(Customizer.withDefaults())
	      .authorizeHttpRequests(authorize -> authorize
	              .requestMatchers(HttpMethod.OPTIONS, "/openapi.*", "/checkHealth").permitAll()
	              .requestMatchers(HttpMethod.GET, "/openapi.*", "/checkHealth").permitAll()
	              .requestMatchers(HttpMethod.GET, "/checkToken").permitAll()
	              .requestMatchers("/**").hasAuthority("CIRRAS_CLAIMS.GET_TOP_LEVEL")
	              .anyRequest().denyAll()
	      ).exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint()) );		
		return http.build();
	  }	

}
