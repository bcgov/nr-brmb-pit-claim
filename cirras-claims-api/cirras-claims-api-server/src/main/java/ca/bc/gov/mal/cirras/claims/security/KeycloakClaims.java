package ca.bc.gov.mal.cirras.claims.security;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.security.oauth2.jwt.Jwt;

public final class KeycloakClaims {

	private KeycloakClaims() {
	}

	public static Set<String> getAuthorities(Jwt jwt) {
		Set<String> result = new LinkedHashSet<>();

		addScopes(jwt.getClaim("scope"), result);
		addScopes(jwt.getClaim("scp"), result);
		addScopes(jwt.getClaim("authorities"), result);
		addRoles(jwt.getClaim("realm_access"), result);

		Object resourceAccess = jwt.getClaim("resource_access");
		if (resourceAccess instanceof Map<?, ?> resources) {
			for (Object resource : resources.values()) {
				addRoles(resource, result);
			}
		}

		return result;
	}

	private static void addScopes(Object claim, Set<String> authorities) {
		if (claim instanceof String scope) {
			for (String value : scope.split("\\s+")) {
				if (!value.isBlank()) {
					authorities.add(value);
				}
			}
		} else if (claim instanceof Collection<?> values) {
			for (Object value : values) {
				if (value instanceof String authority && !authority.isBlank()) {
					authorities.add(authority);
				}
			}
		}
	}

	private static void addRoles(Object claim, Set<String> authorities) {
		if (claim instanceof Map<?, ?> access) {
			addScopes(access.get("roles"), authorities);
		}
	}
}
