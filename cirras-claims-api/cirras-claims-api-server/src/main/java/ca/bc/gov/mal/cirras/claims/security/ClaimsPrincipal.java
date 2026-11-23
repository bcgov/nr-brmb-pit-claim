package ca.bc.gov.mal.cirras.claims.security;

import java.util.Set;

import org.springframework.security.oauth2.jwt.Jwt;

public final class ClaimsPrincipal {

	private final String userGuid;
	private final String userId;
	private final String userTypeCode;
	private final String givenName;
	private final String familyName;
	private final String clientId;
	private final Set<String> authorities;

	private ClaimsPrincipal(
			String userGuid,
			String userId,
			String userTypeCode,
			String givenName,
			String familyName,
			String clientId,
			Set<String> authorities) {
		this.userGuid = userGuid;
		this.userId = userId;
		this.userTypeCode = userTypeCode;
		this.givenName = givenName;
		this.familyName = familyName;
		this.clientId = clientId;
		this.authorities = Set.copyOf(authorities);
	}

	public static ClaimsPrincipal fromJwt(Jwt jwt) {
		String clientId = jwt.getClaimAsString("azp");
		if (clientId == null) {
			clientId = jwt.getClaimAsString("client_id");
		}

		return new ClaimsPrincipal(
				jwt.getClaimAsString("sub"),
				jwt.getClaimAsString("preferred_username"),
				jwt.getClaimAsString("user_type_code"),
				jwt.getClaimAsString("given_name"),
				jwt.getClaimAsString("family_name"),
				clientId,
				KeycloakClaims.getAuthorities(jwt));
	}

	public static ClaimsPrincipal serviceAccount(String clientId) {
		return new ClaimsPrincipal(
				clientId,
				clientId,
				"SCL",
				null,
				"System",
				clientId,
				Set.of("CIRRAS_CLAIMS.*"));
	}

	public boolean hasAuthority(String authority) {
		if (authorities.contains(authority)) {
			return true;
		}
		for (String grantedAuthority : authorities) {
			if (grantedAuthority.endsWith(".*")
					&& authority.startsWith(grantedAuthority.substring(0, grantedAuthority.length() - 1))) {
				return true;
			}
		}
		return false;
	}

	public String getUserGuid() {
		return userGuid;
	}

	public String getUserId() {
		return userId;
	}

	public String getUserTypeCode() {
		return userTypeCode;
	}

	public String getGivenName() {
		return givenName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public String getClientId() {
		return clientId;
	}
}
