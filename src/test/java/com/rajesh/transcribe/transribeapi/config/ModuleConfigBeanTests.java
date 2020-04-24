package com.rajesh.transcribe.transribeapi.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.boot.test.context.FilteredClassLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ModuleConfigBeanTests {
    
    @Autowired
    ApplicationContextRunner contextRunner;
    
    @Autowired
    Environment environment;
    
    @Test
    public void contextLoads() {
        assertThat(environment).isNotNull();
    }
    
    @Test
    void serviceIsIgnoredIfLibraryIsNotPresent() {
        this.contextRunner.withClassLoader(new FilteredClassLoader(SolrAutoConfiguration.class))
                .run((context) -> assertThat(context).doesNotHaveBean("UserService"));
    }
}
