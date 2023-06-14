package com.megafair.exception.handlers;

import io.quarkus.security.UnauthorizedException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UnAuthorizedExceptionHandler extends AbstractExceptionMapper<UnauthorizedException> {

    @Override
    public Response toResponse(UnauthorizedException exception) {
        logger.error(exception.getMessage());
        return Response.status(Response.Status.UNAUTHORIZED)
            .entity(exception.getMessage())
            .build();
    }
}
