package com.natelxstudio.gameon.tokenserver;

import com.natelxstudio.gameon.tokenserver.exceptions.InvalidTokenException;
import com.natelxstudio.gameon.tokenserver.model.SessionToken;
import com.natelxstudio.gameon.tokenserver.ports.TokenHolderPort;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class TokenValidationService {
    private final TokenHolderPort tokenHolderPort;

    void validateToken(SessionToken sessionToken, String characterName) {
        List<String> characterNames = tokenHolderPort.getCharacterListBySessionToken(sessionToken);
        if (!characterNames.contains(characterName)) {
            throw new InvalidTokenException("Token entry doesn't contain character name");
        }
    }
}
