//package com.megafair.security;
//
//import com.megafair.service.AuthService;
//import io.quarkus.security.identity.SecurityIdentity;
//import io.quarkus.vertx.http.runtime.security.HttpSecurityPolicy;
//import io.smallrye.mutiny.Uni;
//import io.vertx.ext.web.RoutingContext;
//import jakarta.annotation.Priority;
//import jakarta.enterprise.context.ApplicationScoped;
//import jakarta.enterprise.context.Dependent;
//import jakarta.enterprise.inject.Alternative;
//import jakarta.inject.Singleton;
//import lombok.RequiredArgsConstructor;
//import org.eclipse.microprofile.jwt.Claims;
//import org.eclipse.microprofile.jwt.JsonWebToken;
//
//import java.security.Principal;
//
//import static io.quarkus.vertx.http.runtime.security.HttpSecurityPolicy.CheckResult.DENY;
//import static io.quarkus.vertx.http.runtime.security.HttpSecurityPolicy.CheckResult.PERMIT;
//
//@Dependent
//@RequiredArgsConstructor
//public class JwtAuthorizationSecurityPolicy implements HttpSecurityPolicy {
//
//    private final AuthService authService;
//
//    @Override
//    public Uni<CheckResult> checkPermission(
//        RoutingContext request,
//        Uni<SecurityIdentity> identity,
//        AuthorizationRequestContext requestContext) {
//        System.out.println("identity");
//        return identity.flatMap(e -> checkPermission(e, request));
//    }
//
//
//    private Uni<CheckResult> checkPermission(SecurityIdentity securityIdentity, RoutingContext routingContext) {
//        System.out.println("check permission");
//        Principal principal = securityIdentity.getPrincipal();
//        if (principal instanceof JsonWebToken jwt) {
//            return authService.checkToken(jwt.getClaim(Claims.azp), jwt.getRawToken())
//                .map(b -> b ? PERMIT : DENY);
//        }
//        return Uni.createFrom().item(PERMIT);
//    }
//}
