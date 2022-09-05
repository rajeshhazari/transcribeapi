package com.c3transcribe.transcribeapi.module.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@ConfigurationProperties(prefix = "app")
@Validated
@Data
public class ApiModuleProperties {
    @Value("${app.io.bufferSize}")
    private String appMaxUploadLimitInMB;
    @Value("${app.io.uploadDir}")
    private String uploadDir;
    private Duration connectTimeout = Duration.ofMillis(5000);
    @DurationUnit(ChronoUnit.SECONDS)
    private Duration readTimeout = Duration.ofSeconds(30);
    
    //@Value("${app.io.appAuthWhiteListUri}")
    private static final String[] AUTH_WHITELIST = {
            // -- swagger ui
            "/actuator/**",
            "/v2/api-docs/**",           // swagger
            "/webjars/**",            // swagger-ui webjars
            "/swagger-ui.html/**",  // swagger-ui resources
            "/swagger-resources/**",
            "/configuration/**",      // swagger configuration
            "/auth/**",
            "/public/**",
            "h2-console",
            "/csrf/**",
            "/version/**",           // app version
            "/register/**",           // register customer
            // other public endpoints of your API may be appended to this array
    };
}
