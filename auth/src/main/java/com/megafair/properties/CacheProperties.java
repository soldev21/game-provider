package com.megafair.properties;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

import java.time.Duration;

@ConfigMapping(prefix = "gp.cache")
public interface CacheProperties {

    @WithName("tokenTtl")
    Duration tokenTtl();
    @WithName("sessionTokenTtl")
    Duration sessionTokenTtl();
}