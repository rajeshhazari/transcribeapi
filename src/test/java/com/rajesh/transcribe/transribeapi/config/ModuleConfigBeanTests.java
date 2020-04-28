package com.rajesh.transcribe.transribeapi.config;

import com.rajesh.transcribe.transribeapi.api.controller.AppRestServiceInitTestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppRestServiceInitTestConfig.class)
@Profile("test")
public class ModuleConfigBeanTests {
    
    
    @Autowired
    Environment environment;
    
    @Test
    public void contextLoads() {
        assertThat(environment).isNotNull();
    }
    
  
}
