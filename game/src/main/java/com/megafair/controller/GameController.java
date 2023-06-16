package com.megafair.controller;

import com.megafair.model.AccountOperationRequest;
import com.megafair.service.GameService;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestHeader;
import org.jboss.resteasy.reactive.RestQuery;

import static com.megafair.util.MonoUtil.wrapResponseOk;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/game")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@RequiredArgsConstructor
@Tag(name = "Game Module Endpoints")
public class GameController {

    private final GameService gameService;

    @PUT
    @Path("/requestForAmount")
    @RolesAllowed("game")
    public Uni<Response> requestForAmount(
        @Valid
        AccountOperationRequest accountOperationRequest,

        @RestQuery("sessionToken")
        String sessionToken
    ) {
        return wrapResponseOk(gameService.requestForAmount(accountOperationRequest, sessionToken));
    }

    @PUT
    @Path("/depositOnAccount")
    @RolesAllowed("game")
    public Uni<Response> depositOnAccount(
        @Valid
        AccountOperationRequest accountOperationRequest,

        @RestHeader("sessionToken")
        String sessionToken
    ) {
        return wrapResponseOk(gameService.depositOnAccount(accountOperationRequest, sessionToken));
    }

    @PUT
    @Path("/checkSessionToken")
    @PermitAll
    public Uni<Response> checkSessionToken(
        @RestQuery("sessionToken") String sessionToken
    ) {
        return wrapResponseOk(gameService.checkSessionToken(sessionToken));
    }
}
