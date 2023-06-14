package com.megafair.service;


import io.quarkus.cache.CacheKeyGenerator;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Produces;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;

@ApplicationScoped
public class RestClientFactory {

    @CacheResult(cacheName = PlatformClient.CACHE_NAME, keyGenerator = RestClientCacheKeyGenerator.class, lockTimeout = 1000)
    @Produces
    public PlatformClient getPlatformClient(String url) throws URISyntaxException {
        return RestClientBuilder.newBuilder()
            .baseUri(new URI(url))
            .build(PlatformClient.class);
    }

    @ApplicationScoped
    public static class RestClientCacheKeyGenerator implements CacheKeyGenerator {

        @Override
        public Object generate(Method method, Object... methodParams) {
            return methodParams[0];
        }
    }
}
