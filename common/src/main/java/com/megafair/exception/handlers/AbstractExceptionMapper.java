package com.megafair.exception.handlers;

import jakarta.ws.rs.ext.ExceptionMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.ParameterizedType;

public abstract class AbstractExceptionMapper<T extends Throwable> implements ExceptionMapper<T> {

    Logger logger;

    AbstractExceptionMapper() {
        Class<T> parameterType = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        logger = LogManager.getLogger(parameterType);
    }
}
