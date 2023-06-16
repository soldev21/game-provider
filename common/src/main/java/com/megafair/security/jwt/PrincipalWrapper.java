package com.megafair.security.jwt;

import io.quarkus.security.identity.CurrentIdentityAssociation;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.jwt.auth.principal.DefaultJWTCallerPrincipal;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.jwt.Claims;

@ApplicationScoped
@RequiredArgsConstructor
public class PrincipalWrapper {

    @Inject
    CurrentIdentityAssociation currentIdentityAssociation;

    public Uni<String> getId() {
        return currentIdentityAssociation.getDeferredIdentity().map(securityIdentity -> {
            return ((DefaultJWTCallerPrincipal) securityIdentity.getAttribute(SecurityIdentity.USER_ATTRIBUTE)).getClaim(Claims.azp);
        });
    }
}
