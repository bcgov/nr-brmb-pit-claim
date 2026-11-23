package ca.bc.gov.mal.cirras.claims.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import ca.bc.gov.mal.cirras.claims.security.ClaimsPrincipal;
import ca.bc.gov.mal.pit.common.rest.endpoints.BaseEndpointsImpl;

public abstract class ClaimsEndpointsImpl extends BaseEndpointsImpl {

	protected ClaimsPrincipal getClaimsPrincipal() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof JwtAuthenticationToken jwtAuthentication) {
			return ClaimsPrincipal.fromJwt(jwtAuthentication.getToken());
		}
		if (authentication != null && authentication.getPrincipal() instanceof ClaimsPrincipal claimsPrincipal) {
			return claimsPrincipal;
		}
		return null;
	}

	protected boolean hasClaimAuthority(String authority) {
		ClaimsPrincipal principal = getClaimsPrincipal();
		return principal != null && principal.hasAuthority(authority);
	}
}
