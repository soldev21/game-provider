package com.megafair.service;

import com.megafair.auth.Roles;
import com.megafair.cache.AuthCachePrefix;
import com.megafair.cache.StringCacheRepository;
import com.megafair.model.AccountOperationRequest;
import com.megafair.model.AccountOperationResponse;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

@ApplicationScoped
@RequiredArgsConstructor
public class GameService {

    private final JWTParser jwtParser;
    private final StringCacheRepository stringCacheRepository;

    @SneakyThrows
    public Uni<Boolean> checkSessionToken(String sessionToken) {
        JsonWebToken jwt = jwtParser.parse(sessionToken);
        if (!jwt.getGroups().contains(Roles.GAME)) {
            return Uni.createFrom().item(false);
        }
        return stringCacheRepository.existInTheSet(AuthCachePrefix.SESSION_TOKENS.name(), jwt.getRawToken());
    }

    public AccountOperationResponse requestForAmount(AccountOperationRequest accountOperationRequest, String token) {
        checkSessionToken(token);
        return new AccountOperationResponse();
    }

    public AccountOperationResponse depositOnAccount(AccountOperationRequest accountOperationRequest, String token) {
        checkSessionToken(token);
        return new AccountOperationResponse();
    }
}
