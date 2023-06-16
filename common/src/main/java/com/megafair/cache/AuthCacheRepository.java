package com.megafair.cache;

import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.hash.ReactiveHashCommands;
import io.quarkus.redis.datasource.keys.ReactiveKeyCommands;
import io.quarkus.redis.datasource.set.ReactiveSetCommands;
import io.quarkus.redis.datasource.value.ReactiveValueCommands;
import io.smallrye.mutiny.Uni;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Function;

@NoArgsConstructor
public abstract class AuthCacheRepository<V> {

    private static final String DELIMITER = "_";
    private ReactiveKeyCommands<String> keyCommands;
    private ReactiveValueCommands<String, V> valueCommands;
    private ReactiveSetCommands<String, V> setCommands;
    private ReactiveHashCommands<String, String, V> hashCommands;

    public AuthCacheRepository(ReactiveRedisDataSource ds, ReactiveRedisDataSource reactive, Class<V> vClass) {
        keyCommands = reactive.key();
        valueCommands = ds.value(vClass);
        setCommands = ds.set(vClass);
        hashCommands = ds.hash(String.class, String.class, vClass);
    }

    public Uni<V> findById(String id) {
        return valueCommands.get(id);
    }

    public Uni<V> findById(String id, String prefix) {
        return findById(id(id, prefix));
    }

    public Uni<Integer> addToSet(String setName, V val) {
        return setCommands.sadd(setName, val);
    }

    public Uni<Boolean> addToMap(String mapName, String key, V val) {
        return hashCommands.hset(mapName, key, val);
    }

    public Uni<Integer> removeFromSet(String setName, V val) {
        return setCommands.srem(setName, val);
    }

    public Uni<Boolean> existInTheSet(String setName, V val) {
        return setCommands.sismember(setName, val);
    }

    public Uni<V> executeIfAbsent(String id, Function<String, V> function) {
        return valueCommands.get(id)
            .flatMap(value -> {
                if (Objects.isNull(value)) {
                    V v = function.apply(id);
                    return valueCommands.setnx(id, v)
                        .flatMap(b -> (b) ? Uni.createFrom().item(v) : findById(id));
                } else {
                    return Uni.createFrom().item(value);
                }
            });
    }

    public Uni<V> executeIfAbsent(String id, String prefix, Function<String, V> function) {
        return executeIfAbsent(id(id, prefix), (o) -> function.apply(id));
    }

    public Uni<Boolean> expire(String id, String prefix, Duration duration) {
        return expire(id(id, prefix), duration);
    }

    public Uni<Boolean> expire(String id, Duration duration) {
        return duration.isZero() ? Uni.createFrom().item(false) : keyCommands.expire(id, duration);
    }

    private String id(String id, String prefix) {
        return prefix + DELIMITER + id;
    }
}
