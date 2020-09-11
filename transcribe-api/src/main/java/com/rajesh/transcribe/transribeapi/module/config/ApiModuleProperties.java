package com.rajesh.transcribe.transribeapi.module.config;

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
}
