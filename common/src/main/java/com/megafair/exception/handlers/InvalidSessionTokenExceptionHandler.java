package com.megafair.exception.handlers;

import com.megafair.exception.InvalidSessionTokenException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InvalidSessionTokenExceptionHandler extends AbstractExceptionMapper<InvalidSessionTokenException> {

    @Override
    public Response toResponse(InvalidSessionTokenException exception) {
        logger.error(exception.getMessage());
        return Response.status(Response.Status.BAD_REQUEST)
            .entity(exception.getMessage())
            .build();
    }
}
