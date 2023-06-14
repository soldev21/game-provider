package com.megafair.cache;

import com.megafair.cache.model.GameCache;
import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GameCacheRepository extends AuthCacheRepository<GameCache> {

    public GameCacheRepository(ReactiveRedisDataSource ds, ReactiveRedisDataSource reactive) {
        super(ds, reactive, GameCache.class);
    }
}
