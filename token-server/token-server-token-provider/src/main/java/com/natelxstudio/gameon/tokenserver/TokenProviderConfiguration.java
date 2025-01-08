package com.natelxstudio.gameon.tokenserver;

import com.natelxstudio.gameon.tokenserver.ports.TokenGeneratorPort;
import com.natelxstudio.gameon.tokenserver.ports.TokenHolderPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class TokenProviderConfiguration {
    @Bean
    TokenProviderFacade tokenProviderFacade(
        TokenGeneratorPort tokenGeneratorPort,
        TokenHolderPort tokenHolderPort
    ) {
        return new TokenProviderFacade(
            new GenerateTokenService(tokenGeneratorPort, tokenHolderPort),
            new TokenValidationService(tokenHolderPort));
    }
}
