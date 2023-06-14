package com.megafair.auth;

import io.vertx.ext.web.Router;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.ws.rs.core.MediaType;

@ApplicationScoped
public class AccessForbiddenRoute {

    public void init(@Observes Router router) {
        router.route()
            .order(Integer.MIN_VALUE)
            .produces(MediaType.TEXT_HTML)
            .failureHandler(context -> {
                if (context.statusCode() == 403) {
                    context.response()
                        .setStatusCode(context.statusCode())
                        .sendFile("403.html");
                } else {
                    context.next();
                }
            });
    }
}