package com.megafair.service;

import com.megafair.cache.AuthCachePrefix;
import com.megafair.cache.GameCacheRepository;
import com.megafair.cache.RedisKeyHasher;
import com.megafair.cache.StringCacheRepository;
import com.megafair.cache.model.GameCache;
import com.megafair.model.Response;
import com.megafair.model.SessionTokenRequest;
import com.megafair.model.SessionTokenResponse;
import com.megafair.properties.CacheProperties;
import com.megafair.security.Roles;
import com.megafair.security.jwt.GenerateToken;
import com.megafair.security.jwt.PrincipalWrapper;
import io.quarkus.security.UnauthorizedException;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.Objects;
import java.util.UUID;

import static com.megafair.util.MonoUtil.invalidSessionTokenException;

@ApplicationScoped
@RequiredArgsConstructor
public class AuthService {

    final StringCacheRepository authCacheRepository;
    final GameCacheRepository gameCacheRepository;
    final CacheProperties cacheProperties;
    final JWTParser jwtParser;
    final PrincipalWrapper principal;

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
        return authCacheRepository.findById(platformId.toString(), AuthCachePrefix.SECRET.getName())
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
        return principal.getId().flatMap(platformId -> gameCacheRepository.findById(RedisKeyHasher.hashCompoundKey(platformId, gameInitPayload.getCasinoId().toString(), gameInitPayload.getGameId().toString()))
            .map(gameCache -> {
                if (Objects.nonNull(gameCache)) {
                    return gameInitResponse(gameInitPayload, gameCache, platformId);
                } else {
                    return new SessionTokenResponse(Response.ERROR, null);
                }
            })
            .flatMap(sessionTokenResponse -> {
                if (sessionTokenResponse.response().equals(Response.SUCCESS)) {
                    return authCacheRepository.addToSet(AuthCachePrefix.SESSION_TOKENS.getName(), sessionTokenResponse.data().sessionToken())
                        .map(i -> sessionTokenResponse);
                }
                return Uni.createFrom().item(sessionTokenResponse);
            }));
    }

    /**
     * @param sessionToken This method is for invalidating the game session. This method supposed to be used when user needs to be banned by platform.
     */
    public Uni<Void> invalidateSessionToken(String sessionToken) {
        JsonWebToken jwt = null;
        try {
            jwt = jwtParser.parse(sessionToken);
        } catch (ParseException e) {
            return invalidSessionTokenException();
        }
        JsonWebToken finalJwt = jwt;
        return principal.getId().flatMap(platformId -> {
            if (!finalJwt.getGroups().contains(Roles.GAME)
                || !finalJwt.getClaim(Claims.orig).equals(platformId)
            ) {
                return invalidSessionTokenException();
            }
            return authCacheRepository.removeFromSet(AuthCachePrefix.SESSION_TOKENS.getName(), sessionToken)
                .flatMap(integer -> {
                    if (integer != 0) {
                        return Uni.createFrom().voidItem();
                    }else {
                        return invalidSessionTokenException();
                    }
                });
        });
    }

    /**
     * @param token This method is for checking if this token is actual or not.
     */
    public Uni<Boolean> checkToken(String id, String token) {
        return authCacheRepository.findById(id, AuthCachePrefix.TOKEN.getName())
            .map(t -> t != null && t.equals(token));
    }

    private Uni<String> generateAndCacheToken(String id) {
        String prefix = AuthCachePrefix.TOKEN.getName();
        return authCacheRepository.executeIfAbsent(id, prefix, (pid) -> GenerateToken.generatePlatformToken(pid, cacheProperties.tokenTtl()))
            .flatMap(token -> authCacheRepository.expire(id, prefix, cacheProperties.tokenTtl())
                .map(b -> token));
    }

    private SessionTokenResponse gameInitResponse(SessionTokenRequest sessionTokenRequest, GameCache gameCache, String platformId) {
        return gameInitResponse(sessionTokenRequest, gameCache.getUrl(), platformId);
    }

    private SessionTokenResponse gameInitResponse(SessionTokenRequest sessionTokenRequest, String url, String platformId) {
        return new SessionTokenResponse(Response.SUCCESS, new SessionTokenResponse.Payload(GenerateToken.generateSessionToken(platformId, sessionTokenRequest, cacheProperties.sessionTokenTtl()), url));
    }
}
