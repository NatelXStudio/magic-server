package com.natelxstudio.gameon.spring.security.keycloak;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;

@RequiredArgsConstructor
public class KeycloakTokenParser {
    private final Jwt token;

    public String getUserId() {
        return token.getClaim("sub");
    }
}
