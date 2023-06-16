package com.megafair.util;

import com.megafair.exception.InvalidSessionTokenException;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.core.Response;

public class MonoUtil {

    public static <T> Uni<Response> wrapResponseOk(Uni<T> uni) {
        return uni.map(data -> Response.ok().entity(data).build());
    }

    public static <T> Uni<T> invalidSessionTokenException() {
        return Uni.createFrom().failure(new InvalidSessionTokenException());
    }

    public static <T> Uni<T> unsupportedOperationException() {
        return Uni.createFrom().failure(new UnsupportedOperationException());
    }


}
