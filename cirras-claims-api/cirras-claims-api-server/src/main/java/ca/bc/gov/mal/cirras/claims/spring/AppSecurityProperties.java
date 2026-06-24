package ca.bc.gov.mal.cirras.claims.spring;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AppSecurityProperties {

    private final Map<String, List<String>> roleScopeMappings;

    public AppSecurityProperties(
            @Value("${app.security.role-scope-mappings}") String jsonMappings,
            ObjectMapper objectMapper) throws Exception {
        this.roleScopeMappings = objectMapper.readValue(jsonMappings, new TypeReference<>() {
        });
    }

    public List<String> getScopesForRole(String role) {
        String cleanRole = role.replace("ROLE_", "");
        return roleScopeMappings.getOrDefault(cleanRole, List.of());
    }
}
