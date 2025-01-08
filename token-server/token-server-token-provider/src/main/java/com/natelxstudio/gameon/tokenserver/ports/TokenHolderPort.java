package com.natelxstudio.gameon.tokenserver.ports;

import com.natelxstudio.gameon.tokenserver.model.SessionToken;
import java.util.List;

public interface TokenHolderPort {
    void createTokenEntry(SessionToken sessionToken, List<String> characterNames);
    List<String> getCharacterListBySessionToken(SessionToken sessionToken);
}
