package ca.bc.gov.mal.cirras.claims.security;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.oauth2.jwt.Jwt;

public class KeycloakClaimsTest {

	@Test
	public void testScopesAndKeycloakRolesAreAuthorities() {
		Jwt jwt = Jwt.withTokenValue("token")
				.header("alg", "none")
				.claim("scope", "CIRRAS_CLAIMS.GET_TOP_LEVEL openid")
				.claim("realm_access", java.util.Map.of("roles", java.util.List.of("CIRRAS_CLAIMS.SEARCH_CLAIMS")))
				.claim("resource_access", java.util.Map.of("claims", java.util.Map.of("roles",
						java.util.List.of("CIRRAS_CLAIMS.GET_CODE_TABLES"))))
				.build();

		Assert.assertEquals(Set.of(
				"CIRRAS_CLAIMS.GET_TOP_LEVEL",
				"openid",
				"CIRRAS_CLAIMS.SEARCH_CLAIMS",
				"CIRRAS_CLAIMS.GET_CODE_TABLES"), KeycloakClaims.getAuthorities(jwt));
	}

	@Test
	public void testIdentityUsesConfiguredKeycloakClaims() {
		Jwt jwt = Jwt.withTokenValue("token")
				.header("alg", "none")
				.claim("sub", "user-guid")
				.claim("preferred_username", "user-id")
				.claim("user_type_code", "SCL")
				.claim("given_name", "Given")
				.claim("family_name", "Family")
				.claim("azp", "pit-claim-ui")
				.build();

		ClaimsPrincipal principal = ClaimsPrincipal.fromJwt(jwt);
		Assert.assertEquals("user-guid", principal.getUserGuid());
		Assert.assertEquals("user-id", principal.getUserId());
		Assert.assertEquals("SCL", principal.getUserTypeCode());
		Assert.assertEquals("Given", principal.getGivenName());
		Assert.assertEquals("Family", principal.getFamilyName());
		Assert.assertEquals("pit-claim-ui", principal.getClientId());
	}

	@Test
	public void testServiceAccountWildcardGrantsOnlyItsResource() {
		ClaimsPrincipal principal = ClaimsPrincipal.serviceAccount("cirras-claims-api");

		Assert.assertTrue(principal.hasAuthority("CIRRAS_CLAIMS.UPDATE_SYNC_CLAIM"));
		Assert.assertFalse(principal.hasAuthority("CIRRAS_UNDERWRITING.GET_POLICY"));
	}
}
