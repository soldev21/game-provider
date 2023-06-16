package com.megafair.security;

import com.megafair.service.AuthService;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.SecurityIdentityAugmentor;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.security.Principal;

import static com.megafair.security.Roles.PLATFORM;

@ApplicationScoped
@RequiredArgsConstructor
public class AuthRolesAugmentor implements SecurityIdentityAugmentor {

    private final AuthService authService;

    @Override
    public Uni<SecurityIdentity> augment(SecurityIdentity identity, AuthenticationRequestContext context) {
        return build(identity);
    }

    private Uni<SecurityIdentity> build(SecurityIdentity identity) {
        if (identity.getRoles().contains(PLATFORM)) {
            Principal principal = identity.getPrincipal();
            if (principal instanceof JsonWebToken jwt) {
                return authService.checkToken(jwt.getClaim(Claims.azp), jwt.getRawToken())
                    .flatMap(b -> {
                        if (b) {
                            return Uni.createFrom().item(identity);
                        } else {
                            return Uni.createFrom().failure(new UnauthorizedException());
                        }
                    });
            }
        }
        return Uni.createFrom().item(identity);
    }

    @Override
    public int priority() {
        return 1;
    }
}