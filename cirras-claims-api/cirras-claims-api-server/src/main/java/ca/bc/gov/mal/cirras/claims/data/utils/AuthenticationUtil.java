package ca.bc.gov.mal.cirras.claims.data.utils;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class AuthenticationUtil {

    public static boolean hasAuthority(String... authorityName) {
        Authentication authentication = getAuthentication();

        boolean result = false;

        List<String> authorityNames = Arrays.asList(authorityName);

        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {

            String authority = grantedAuthority.getAuthority();

            if (authorityNames.contains(authority)) {
                result = true;
                break;
            }
        }

        return result;
    }

    public static boolean isTokenExpired() {

        // extract exp claim
        Authentication authentication = getAuthentication();
        Instant expInstant = toJwt(authentication).getClaimAsInstant("exp");

        // compare with the current instant
        return Instant.now().isAfter(expInstant);
    }

    private static Jwt toJwt(Authentication authentication) {
        return (Jwt) authentication.getPrincipal();
    }

    public static String getUserGuid(Authentication authentication) {
        String result = toJwt(authentication).getClaimAsString("oid");
        return result.replace("-", "").toUpperCase();
    }

    public static String getFamilyName() {
        return getFamilyName(getAuthentication());
    }

    public static String getFamilyName(Authentication authentication) {
        return toJwt(authentication).getClaimAsString("family_name");
    }

    public static String getGivenName() {
        return getGivenName(getAuthentication());
    }

    public static String getGivenName(Authentication authentication) {
        return toJwt(authentication).getClaimAsString("given_name");
    }

    public static String getUserId(Authentication authentication) {
        return toJwt(authentication).getClaimAsString("upn");
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static Long getExp() {
        return toJwt(getAuthentication()).getClaimAsInstant("exp").getEpochSecond();
    }

    public static String[] getScope() {
        return getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toArray(String[]::new);
    }
}
