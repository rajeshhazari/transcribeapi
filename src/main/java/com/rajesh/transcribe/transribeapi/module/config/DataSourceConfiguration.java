package com.rajesh.transcribe.transribeapi.module.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {
    
    @Bean
    @Profile("default")
    public DataSource embeddedDataSource(){
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:/h2-db/schema_h2.sql")
                .addScript("classpath:/h2-db/data-temp.sql")
                .continueOnError(true)
                .build();
    }
    
   /*
    @Profile("qa")
    @Bean
    public DataSource Data() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("org.h2.Driver");
        ds.setUrl("jdbc:h2:tcp://localhost/~/spitter");
        ds.setUsername("sa");
        ds.setPassword("");
        ds.setInitialSize(5);
        ds.setMaxActive(10);
        return ds;
    }
    @Profile("production")
    @Bean
    public DataSource dataSource() {
        JndiObjectFactoryBean jndiObjectFactoryBean
                = new JndiObjectFactoryBean();
        jndiObjectFactoryBean.setJndiName("jdbc/SpittrDS");
        jndiObjectFactoryBean.setResourceRef(true);
        jndiObjectFactoryBean.setProxyInterface(javax.sql.DataSource.class);
        return (DataSource) jndiObjectFactoryBean.getObject();
    }
    
    */
}
