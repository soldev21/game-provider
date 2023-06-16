package com.megafair.security.jwt;

import io.smallrye.jwt.auth.principal.DefaultJWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import io.smallrye.jwt.build.Jwt;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;

import static com.megafair.security.Roles.GAME;
import static com.megafair.security.Roles.PLATFORM;


public class GenerateToken {

    public static JsonWebToken parseToken(String token) throws ParseException {
        return new DefaultJWTParser()
            .parse(token);
    }

    public static String generatePlatformToken(Object platformId, Duration duration) {
        return Jwt
            .groups(new HashSet<>(Arrays.asList(PLATFORM)))
            .upn(PLATFORM)
            .expiresIn(duration)
            .claim(Claims.azp, platformId)
            .sign();
    }

    public static String generateSessionToken(String platformId, Object sessionTokenRequest, Duration duration) {
        return Jwt
            .groups(new HashSet<>(Arrays.asList(GAME)))
            .upn(GAME)
            .expiresIn(duration)
            .claim(Claims.azp, sessionTokenRequest)
            .claim(Claims.orig.name(), platformId)
            .sign();
    }
}