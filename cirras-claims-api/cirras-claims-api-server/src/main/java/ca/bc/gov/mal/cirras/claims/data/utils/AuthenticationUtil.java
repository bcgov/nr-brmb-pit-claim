package ca.bc.gov.mal.cirras.claims.data.utils;

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

    private static Jwt toJwt(Authentication authentication) {
        return (Jwt) authentication.getPrincipal();
    }

    public static String getUserGuid(Authentication authentication) {
        String result = toJwt(authentication).getClaimAsString("oid");
        return result.replace("-", "").toUpperCase();
    }

    public static String getFamilyName(Authentication authentication) {
        return toJwt(authentication).getClaimAsString("family_name");
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
}
