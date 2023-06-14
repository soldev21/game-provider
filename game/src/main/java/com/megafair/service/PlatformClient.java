package com.megafair.service;

import com.megafair.model.AccountOperationResponse;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestQuery;

import java.math.BigDecimal;

import static com.megafair.service.PlatformClient.CACHE_NAME;

@RegisterRestClient(configKey = CACHE_NAME)
public interface PlatformClient {
    String CACHE_NAME = "platform-client";

    @PUT
    @Path("/requestForAmount")
    Uni<AccountOperationResponse> requestForAmount(@RestQuery("amount") BigDecimal amount, @RestQuery("sessionToken") String sessionToken);

    @PUT
    @Path("/depositOnAccount")
    Uni<AccountOperationResponse> depositOnAccount(@RestQuery("amount") BigDecimal amount, @RestQuery("sessionToken") String sessionToken);
}
