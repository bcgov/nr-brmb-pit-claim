package ca.bc.gov.mal.cirras.claims.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import ca.bc.gov.mal.cirras.claims.security.KeycloakJwtAuthoritiesConverter;

@Configuration
@EnableWebSecurity(debug = false)
public class SecuritySpringConfig {

	@Bean
	public JwtDecoder jwtDecoder(@Value("${KEYCLOAK_ISSUER_URI}") String issuerUri) {
		return JwtDecoders.fromIssuerLocation(issuerUri);
	}

	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtAuthenticationConverter result = new JwtAuthenticationConverter();
		result.setJwtGrantedAuthoritiesConverter(new KeycloakJwtAuthoritiesConverter());
		return result;
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring().requestMatchers(
				new AntPathRequestMatcher("/openapi.*", HttpMethod.OPTIONS.name()),
				new AntPathRequestMatcher("/openapi.*", HttpMethod.GET.name()),
				new AntPathRequestMatcher("/checkHealth", HttpMethod.OPTIONS.name()),
				new AntPathRequestMatcher("/checkHealth", HttpMethod.GET.name()));
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationConverter jwtAuthenticationConverter)
			throws Exception {
		http.csrf(csrf -> csrf.disable())
				.cors(cors -> cors.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)))
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers(HttpMethod.OPTIONS, "/openapi.*", "/checkHealth", "/checkToken").permitAll()
						.requestMatchers(HttpMethod.GET, "/openapi.*", "/checkHealth").permitAll()
						.requestMatchers(HttpMethod.GET, "/checkToken").authenticated()
						.requestMatchers("/**").hasAnyAuthority("CIRRAS_CLAIMS.GET_TOP_LEVEL", "CIRRAS_CLAIMS.*")
						.anyRequest().denyAll());
		return http.build();
	}
}
