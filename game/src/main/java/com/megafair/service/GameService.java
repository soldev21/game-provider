package com.megafair.service;

import com.megafair.auth.Roles;
import com.megafair.cache.AuthCachePrefix;
import com.megafair.cache.StringCacheRepository;
import com.megafair.model.AccountOperationRequest;
import com.megafair.model.AccountOperationResponse;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.jwt.JsonWebToken;

import static com.megafair.util.MonoUtil.invalidSessionTokenException;
import static com.megafair.util.MonoUtil.unsupportedOperationException;

@ApplicationScoped
@RequiredArgsConstructor
public class GameService {

    private final JWTParser jwtParser;
    private final StringCacheRepository stringCacheRepository;

    public Uni<Boolean> checkSessionToken(String sessionToken) {
        JsonWebToken jwt;
        try {
            jwt = jwtParser.parse(sessionToken);
        } catch (ParseException e) {
            return invalidSessionTokenException();
        }
        if (!jwt.getGroups().contains(Roles.GAME)) {
            return invalidSessionTokenException();
        }
        return stringCacheRepository.existInTheSet(AuthCachePrefix.SESSION_TOKENS.getName(), jwt.getRawToken());
    }

    public Uni<AccountOperationResponse> requestForAmount(AccountOperationRequest accountOperationRequest, String token) {
        checkSessionToken(token);
        return unsupportedOperationException();
    }

    public Uni<AccountOperationResponse> depositOnAccount(AccountOperationRequest accountOperationRequest, String token) {
        checkSessionToken(token);
        return unsupportedOperationException();
    }
}
