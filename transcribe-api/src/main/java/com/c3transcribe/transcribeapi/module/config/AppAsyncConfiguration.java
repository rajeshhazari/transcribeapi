package com.c3transcribe.transcribeapi.module.config;

import com.c3transcribe.transcribeapi.api.global.exceptions.CustomAsyncExceptionHandler;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/*@Configuration
@EnableAsync*/
public class AppAsyncConfiguration {
    
    @Bean
    public Executor appAsyncExecutor() {
        ThreadPoolTaskExecutor  executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setAwaitTerminationSeconds(5);
        executor.setThreadNamePrefix("TranscribeAppAsyncThread-");
        executor.initialize();
        return executor;
    }
    
    @Bean
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new CustomAsyncExceptionHandler();
    }
}
