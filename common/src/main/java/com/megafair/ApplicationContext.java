package com.megafair;

import com.megafair.cache.StringCacheRepository;
import io.quarkus.arc.DefaultBean;
import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;

import static org.modelmapper.convention.MatchingStrategies.STRICT;


@Dependent
public class ApplicationContext {

    @Produces
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(STRICT);
        return modelMapper;
    }

    @Produces
    @DefaultBean
    public StringCacheRepository stringCacheRepository(ReactiveRedisDataSource ds, ReactiveRedisDataSource reactive) {
        return new StringCacheRepository(ds, reactive);
    }

    @Produces
    @DefaultBean
    Logger getLoggerWithCustomName(InjectionPoint injectionPoint) {
        return LogManager.getLogger(injectionPoint.getBean().getBeanClass());
    }
}
