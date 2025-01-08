package com.natelxstudio.gameon.tokenserver;

import com.natelxstudio.gameon.tokenserver.model.SessionToken;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TokenProviderFacade {
    private final GenerateTokenService generateTokenService;
    private final TokenValidationService tokenValidationService;

    public SessionToken generateToken(List<String> characterNames) {
        return generateTokenService.generateToken(characterNames);
    }

    public void validateToken(SessionToken sessionToken, String characterName) {
        this.tokenValidationService.validateToken(sessionToken, characterName);
    }
}
