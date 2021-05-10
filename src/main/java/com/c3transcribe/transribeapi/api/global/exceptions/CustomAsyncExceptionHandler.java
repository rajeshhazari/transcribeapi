package com.c3transcribe.transribeapi.api.global.exceptions;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAsyncExceptionHandler.class);
    
    @Override
    public void handleUncaughtException(
            Throwable throwable, Method method, Object... obj) {
    
        LOGGER.debug("Exception message - " + throwable.getMessage());
        LOGGER.debug("Method name - " + method.getName());
        for (Object param : obj) {
            LOGGER.debug("Parameter value - " + param);
        }
    }
}
