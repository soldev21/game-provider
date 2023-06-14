package com.megafair.cache;

import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ext.Provider;

public class StringCacheRepository extends AuthCacheRepository<String> {

    public StringCacheRepository(ReactiveRedisDataSource ds, ReactiveRedisDataSource reactive) {
        super(ds, reactive, String.class);
    }
}
