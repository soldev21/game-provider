package com.megafair.service;

import com.megafair.auth.Roles;
import com.megafair.cache.AuthCachePrefix;
import com.megafair.cache.GameCacheRepository;
import com.megafair.cache.RedisKeyHasher;
import com.megafair.cache.StringCacheRepository;
import com.megafair.cache.model.GameCache;
import com.megafair.model.SessionTokenRequest;
import com.megafair.model.SessionTokenResponse;
import com.megafair.model.Response;
import com.megafair.properties.CacheProperties;
import com.megafair.security.jwt.GenerateToken;
import com.megafair.security.jwt.PrincipalWrapper;
import io.quarkus.security.UnauthorizedException;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.Objects;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class AuthService {

    private final StringCacheRepository authCacheRepository;
    private final GameCacheRepository gameCacheRepository;
    private final CacheProperties cacheProperties;
    private final JWTParser jwtParser;

    @Inject
    PrincipalWrapper principal;

    /**
     * @param platformId identifies the platform in our system which is being defined while registering the platform in our system.
     * @param secretKey  is for getting new token for accessing all resources.
     *                   <p>
     *                   While requesting new token we should validate platformId and secretKey pair first. Considering these pair doesn't change frequently
     *                   we can store them in a redis cache for high availability and low latency.
     *                   If validation passes successfully then we check if there is an actual token for this platform. We also store this token in the cache and set eviction policy.
     *                   While accessing any resource with token, if token doesn't pass through validation successfully then user should get a new token.
     */
    public Uni<String> getToken(UUID platformId, String secretKey) {
        return authCacheRepository.findById(platformId.toString(), AuthCachePrefix.SECRET.name())
            .flatMap(item -> {
                if (item == null || !item.equals(secretKey)) {
                    return Uni.createFrom().failure(new UnauthorizedException("Unauthorized"));
                } else {
                    return Uni.createFrom().item(platformId.toString());
                }
            })
            .flatMap(this::generateAndCacheToken);
    }

    /**
     * @param gameInitPayload This method is for getting the sessionToken for specific game.
     *                        should be validated first.
     */
    public Uni<SessionTokenResponse> getSessionToken(SessionTokenRequest gameInitPayload) {
        return gameCacheRepository.findById(RedisKeyHasher.hashCompoundKey(principal.getPlatformId(), gameInitPayload.getCasinoId().toString(), gameInitPayload.getGameId().toString()))
            .map(gameCache -> {
                if (Objects.nonNull(gameCache)) {
                    return gameInitResponse(gameInitPayload, gameCache);
                } else {
                    return new SessionTokenResponse(Response.ERROR, null);
                }
            })
            .flatMap(sessionTokenResponse -> {
                if (sessionTokenResponse.response().equals(Response.SUCCESS)) {
                    return authCacheRepository.addToSet(AuthCachePrefix.SESSION_TOKENS.name(), sessionTokenResponse.data().sessionToken())
                        .map(i -> sessionTokenResponse);
                }
                return Uni.createFrom().item(sessionTokenResponse);
            });
    }

    /**
     * @param sessionToken This method is for invalidating the game session. This method supposed to be used when user needs to be banned by platform.
     */
    @SneakyThrows
    public Uni<Boolean> invalidateSessionToken(String sessionToken) {
        JsonWebToken jwt = jwtParser.parse(sessionToken);
        if (!jwt.getGroups().contains(Roles.GAME)
            || !jwt.getClaim(Claims.orig).equals(principal.getPlatformId())
        ) {
            return Uni.createFrom().item(false);
        }
        return authCacheRepository.removeFromSet(AuthCachePrefix.SESSION_TOKENS.name(), sessionToken)
            .map(integer -> integer != 0);
    }

    /**
     * @param token This method is for checking if this token is actual or not.
     */
    public Uni<Boolean> checkToken(String id, String token) {
        return authCacheRepository.findById(id, AuthCachePrefix.TOKEN.name())
            .map(t -> t != null && t.equals(token));
    }

    private Uni<String> generateAndCacheToken(String id) {
        String prefix = AuthCachePrefix.TOKEN.name();
        return authCacheRepository.executeIfAbsent(id, prefix, GenerateToken::generatePlatformToken)
            .flatMap(token -> authCacheRepository.expire(id, prefix, cacheProperties.tokenTtl())
                .map(b -> token));
    }

    private SessionTokenResponse gameInitResponse(SessionTokenRequest sessionTokenRequest, GameCache gameCache) {
        return gameInitResponse(sessionTokenRequest, gameCache.getUrl());
    }

    private SessionTokenResponse gameInitResponse(SessionTokenRequest sessionTokenRequest, String url) {
        return new SessionTokenResponse(Response.SUCCESS, new SessionTokenResponse.Payload(GenerateToken.generateSessionToken(principal.getPlatformId(), sessionTokenRequest), url));
    }
}
