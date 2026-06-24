package ca.bc.gov.mal.cirras.claims.spring;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;

public class EntraToWebAdeAuthoritiesMapper implements GrantedAuthoritiesMapper {

    private final AppSecurityProperties properties;

    public EntraToWebAdeAuthoritiesMapper(AppSecurityProperties properties) {
        this.properties = properties;
    }

    @Override
    public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

        for (GrantedAuthority authority : authorities) {
            // 1. Preserve the structural Entra App Role (e.g., ROLE_MANAGER)
            mappedAuthorities.add(authority);

            // 2. Look up the static WebAde scopes tied to this role
            String roleName = authority.getAuthority();
            var scopes = properties.getScopesForRole(roleName);

            // 3. Append them as executable authorities
            for (String scope : scopes) {
                mappedAuthorities.add(new SimpleGrantedAuthority("SCOPE_" + scope));
            }
        }
        return mappedAuthorities;
    }
}