package com.rajesh.transcribe.transribeapi.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@ComponentScan(basePackageClasses = { AppInfoController.class,BCryptPasswordEncoder.class,EncryptDecryptController.class })
public class AppRestServiceInitTestConfig {
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
    }
    
    @Autowired
    AppInfoController appInfoController;
    
}
