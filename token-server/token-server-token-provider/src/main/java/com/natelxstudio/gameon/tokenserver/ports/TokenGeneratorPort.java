package com.natelxstudio.gameon.tokenserver.ports;

import com.natelxstudio.gameon.tokenserver.model.SessionToken;

public interface TokenGeneratorPort {
    SessionToken generateSessionToken();
}
