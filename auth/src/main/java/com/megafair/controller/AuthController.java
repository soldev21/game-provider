package com.megafair.controller;

import com.megafair.model.SessionTokenRequest;
import com.megafair.service.AuthService;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestQuery;

import java.util.UUID;

import static com.megafair.security.Roles.PLATFORM;
import static com.megafair.util.MonoUtil.wrapResponseOk;


@Path("/auth")
@Tag(name = "Authentication Module Endpoints")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GET
    @Path("/token")
    @Produces(MediaType.TEXT_PLAIN)
    @PermitAll
    public Uni<Response> getToken(@RestQuery("id") @NotNull UUID id, @RestQuery("secret") @NotNull String secret) {
        return wrapResponseOk(authService.getToken(id, secret));
    }

    @POST
    @Path("/sessionToken")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(PLATFORM)
    public Uni<Response> getSessionToken(SessionTokenRequest sessionTokenRequest) {
        return wrapResponseOk(authService.getSessionToken(sessionTokenRequest));
    }

    @PUT
    @Path("/invalidateSession")
    @RolesAllowed(PLATFORM)
    public Uni<Response> invalidate(@RestQuery("sessionToken") String sessionToken) {
        return wrapResponseOk(authService.invalidateSessionToken(sessionToken));
    }
}
