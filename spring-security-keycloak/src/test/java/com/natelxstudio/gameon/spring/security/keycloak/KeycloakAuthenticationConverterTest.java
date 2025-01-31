package com.natelxstudio.gameon.spring.security.keycloak;

import static com.natelxstudio.gameon.spring.security.keycloak.KeycloakAuthenticationConverter.buildJwtKeycloakTokenConverter;
import static org.assertj.core.api.Assertions.assertThat;

import com.tngtech.keycloakmock.api.KeycloakMock;
import com.tngtech.keycloakmock.api.ServerConfig;
import com.tngtech.keycloakmock.api.TokenConfig;
import java.time.Instant;
import java.util.Collection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KeycloakAuthenticationConverterTest {
    private static final String KEYCLOAK_REALM_NAME = "test";
    private static final int KEYCLOAK_PORT = 9091;
    private static final String KEYCLOAK_ISSUER_URL = String.format("http://localhost:%d/auth/realms/%s", KEYCLOAK_PORT, KEYCLOAK_REALM_NAME);
    private static final String RESOURCE_NAME = "test_resource";

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
    void whenKeycloakAuthenticationConverterIsUsed_thenReturnCorrectListOfGrantedAuthorities() {
        String resourceRole = "test_client_roles";

        String testToken = KEYCLOAK_MOCK_SERVER.getAccessToken(TokenConfig.aTokenConfig()
            .withRealm(KEYCLOAK_REALM_NAME)
            .withIssuedAt(Instant.now())
            .withExpiration(Instant.now().plusSeconds(100))
            .withResourceRole(RESOURCE_NAME, resourceRole)
            .build());
        Jwt jwt = NimbusJwtDecoder.withIssuerLocation(KEYCLOAK_ISSUER_URL).build().decode(testToken);

        Converter<Jwt, Collection<GrantedAuthority>> converter = buildJwtKeycloakTokenConverter(RESOURCE_NAME);
        Collection<GrantedAuthority> grantedAuthorityList = converter.convert(jwt);

        assertThat(grantedAuthorityList)
            .hasSize(1)
            .anySatisfy(it -> assertThat(it.getAuthority()).isEqualTo(resourceRole));
    }
}