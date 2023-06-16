package com.megafair.cache;

import io.quarkus.redis.datasource.ReactiveRedisDataSource;

public class StringCacheRepository extends AuthCacheRepository<String> {

    public StringCacheRepository(ReactiveRedisDataSource ds, ReactiveRedisDataSource reactive) {
        super(ds, reactive, String.class);
    }
}
