package com.megafair.exception.handlers;

import com.megafair.exception.ValidationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ValidationExceptionHandler extends AbstractExceptionMapper<ValidationException> {

    @Override
    public Response toResponse(ValidationException exception) {
        logger.error(exception.getMessage());
        return Response.status(Response.Status.BAD_REQUEST)
            .entity(exception.getMessage())
            .build();
    }
}
