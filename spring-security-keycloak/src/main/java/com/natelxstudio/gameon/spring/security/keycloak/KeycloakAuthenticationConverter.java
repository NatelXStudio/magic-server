package com.natelxstudio.gameon.spring.security.keycloak;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

public class KeycloakAuthenticationConverter extends JwtAuthenticationConverter {
    private static final String RESOURCE_ACCESS_CLAIM = "resource_access";
    private static final String ROLES_KEY = "roles";

    public KeycloakAuthenticationConverter(String resourceName) {
        setJwtGrantedAuthoritiesConverter(buildJwtKeycloakTokenConverter(resourceName));
    }

    static Converter<Jwt, Collection<GrantedAuthority>> buildJwtKeycloakTokenConverter(String resourceName) {
        return jwt -> {
            Map<String, Map<String, Object>> resourceAccessClaim = jwt.getClaim(RESOURCE_ACCESS_CLAIM);
            Set<String> claimRoles = Optional.ofNullable(resourceAccessClaim)
                .map(resourceAccessKeyMap -> Optional.ofNullable(resourceAccessKeyMap.get(resourceName))
                    .map(resourceClient -> Optional.ofNullable(resourceClient.get(ROLES_KEY))
                        .map(KeycloakAuthenticationConverter::mapTokenRolesToList)
                        .orElseGet(Set::of))
                    .orElseGet(Set::of))
                .orElseGet(Set::of);

            return claimRoles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        };
    }

    private static Set<String> mapTokenRolesToList(Object roles) {
        if (roles instanceof Collection<?> resourceRoles) {
            return resourceRoles.stream().map(Object::toString).collect(Collectors.toSet());
        }
        throw new IllegalArgumentException("Roles object from resource access clain in token must be instance of collection");
    }
}
