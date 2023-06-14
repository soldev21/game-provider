package com.megafair.security.jwt;

import io.smallrye.jwt.auth.principal.DefaultJWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import io.smallrye.jwt.build.Jwt;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.Arrays;
import java.util.HashSet;

import static com.megafair.auth.Roles.*;


public class GenerateToken {

    public static String generateToken(Object claim, String... groups) {
        return Jwt
            .groups(new HashSet<>(Arrays.asList(groups)))
            .upn("someupn")
            .claim("data", claim)
            .sign();
    }

    public static JsonWebToken parseToken(String token) throws ParseException {
        return new DefaultJWTParser()
            .parse(token);
    }

    public static String generatePlatformToken(Object claim) {
        return generateToken(claim, PLATFORM);
    }

    public static String generateSessionToken(String orig, Object claim) {
        return Jwt
            .groups(new HashSet<>(Arrays.asList(GAME)))
            .upn("someupn")
            .claim("data", claim)
            .claim(Claims.orig.name(), orig)
            .sign();
    }
}