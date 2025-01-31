package com.natelxstudio.gameon.spring.security.keycloak;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.tngtech.keycloakmock.api.KeycloakMock;
import com.tngtech.keycloakmock.api.ServerConfig;
import com.tngtech.keycloakmock.api.TokenConfig;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KeycloakTokenParserTest {
    private static final String KEYCLOAK_REALM_NAME = "test";
    private static final int KEYCLOAK_PORT = 9091;
    private static final String KEYCLOAK_ISSUER_URL = String.format("http://localhost:%d/auth/realms/%s", KEYCLOAK_PORT, KEYCLOAK_REALM_NAME);

    private static final KeycloakMock KEYCLOAK_MOCK_SERVER = new KeycloakMock(ServerConfig.aServerConfig()
        .withPort(KEYCLOAK_PORT)
        .withDefaultRealm(KEYCLOAK_REALM_NAME)
        .build());

    @BeforeAll
    public void beforeAll() {
        KEYCLOAK_MOCK_SERVER.start();
    }

    @AfterAll
    public void afterAll() {
        KEYCLOAK_MOCK_SERVER.stop();
    }

    @Test
    void whenKeycloakTokenIsParsed_thenReturnCorrectUserId() {
        String userId = UUID.randomUUID().toString();

        String testToken = KEYCLOAK_MOCK_SERVER.getAccessToken(TokenConfig.aTokenConfig()
            .withRealm(KEYCLOAK_REALM_NAME)
            .withIssuedAt(Instant.now())
            .withExpiration(Instant.now().plusSeconds(333))
            .withSubject(userId)
            .build());
        Jwt jwt = NimbusJwtDecoder.withIssuerLocation(KEYCLOAK_ISSUER_URL).build().decode(testToken);

        KeycloakTokenParser keycloakTokenParser = new KeycloakTokenParser(jwt);

        assertThat(keycloakTokenParser.getUserId()).isEqualTo(userId);
    }
}