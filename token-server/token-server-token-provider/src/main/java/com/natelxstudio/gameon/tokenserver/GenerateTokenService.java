package com.natelxstudio.gameon.tokenserver;

import com.natelxstudio.gameon.tokenserver.model.SessionToken;
import com.natelxstudio.gameon.tokenserver.ports.TokenGeneratorPort;
import com.natelxstudio.gameon.tokenserver.ports.TokenHolderPort;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class GenerateTokenService {
    private final TokenGeneratorPort tokenGeneratorPort;
    private final TokenHolderPort tokenHolderPort;

    SessionToken generateToken(@NonNull List<String> characterNames) {
        SessionToken sessionToken = tokenGeneratorPort.generateSessionToken();
        tokenHolderPort.createTokenEntry(sessionToken, characterNames);
        return sessionToken;
    }
}
