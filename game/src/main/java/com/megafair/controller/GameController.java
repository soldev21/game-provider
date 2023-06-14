package com.megafair.controller;

import com.megafair.model.AccountOperationRequest;
import com.megafair.service.GameService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestHeader;
import org.jboss.resteasy.reactive.RestQuery;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/game")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@RequiredArgsConstructor
@Tag(name = "Auth services")
public class GameController {

    private final GameService gameService;

    @PUT
    @Path("/requestForAmount")
    @RolesAllowed("game")
    public Response requestForAmount(
        @Valid
        AccountOperationRequest accountOperationRequest,

        @RestQuery("sessionToken")
        String sessionToken
    ) {
        return Response.ok(gameService.requestForAmount(accountOperationRequest, sessionToken))
            .build();
    }

    @PUT
    @Path("/depositOnAccount")
    @RolesAllowed("game")
    public Response depositOnAccount(
        @Valid
        AccountOperationRequest accountOperationRequest,

        @RestHeader("sessionToken")
        String sessionToken
    ) {
        return Response.ok(gameService.depositOnAccount(accountOperationRequest, sessionToken))
            .build();
    }

    @PUT
    @Path("/checkSessionToken")
    @PermitAll
    public Response checkSessionToken(
        @RestHeader("platformToken") String platformToken,
        @RestHeader("sessionToken") String sessionToken
    ) {
        gameService.checkSessionToken(sessionToken);
        return Response.ok().build();
    }
}
