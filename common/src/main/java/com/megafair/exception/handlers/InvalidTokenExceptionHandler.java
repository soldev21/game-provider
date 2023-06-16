package com.megafair.exception.handlers;

import com.megafair.exception.InvalidTokenException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InvalidTokenExceptionHandler extends AbstractExceptionMapper<InvalidTokenException> {

    @Override
    public Response toResponse(InvalidTokenException exception) {
        logger.error(exception.getMessage());
        return Response.status(Response.Status.UNAUTHORIZED)
            .entity(exception.getMessage())
            .build();
    }
}
