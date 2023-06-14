package com.megafair.controller;

import com.megafair.model.SessionTokenRequest;
import com.megafair.security.jwt.PrincipalWrapper;
import com.megafair.service.AuthService;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestQuery;

import java.util.UUID;

import static com.megafair.auth.Roles.PLATFORM;


@Path("/auth")
@Tag(name = "Auth services")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    @Inject
    PrincipalWrapper principalWrapper;

    @GET
    @Path("/token")
    @Produces(MediaType.TEXT_PLAIN)
    @PermitAll
    public Uni<Response> getToken(@RestQuery("id") @NotNull UUID id, @RestQuery("secret") @NotNull String secret) {
        return authService.getToken(id, secret)
            .map(data -> Response.ok().entity(data).build());
    }

    @POST
    @Path("/sessionToken")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(PLATFORM)
    public Uni<Response> getSessionToken(SessionTokenRequest sessionTokenRequest) {
        return authService.getSessionToken(sessionTokenRequest)
            .map(data -> Response.ok().entity(data).build());
    }

    @PUT
    @Path("/invalidateSession")
    @RolesAllowed(PLATFORM)
    public Uni<Response> invalidate(@RestQuery("sessionToken") String sessionToken) {
        return authService.invalidateSessionToken(sessionToken)
            .map(data -> Response.ok().entity(data).build());
    }

    @GET
    @Path("/test")
    @RolesAllowed(PLATFORM)
    public Response test() {
        return Response.ok(principalWrapper.getPlatformId()).build();
    }
}
