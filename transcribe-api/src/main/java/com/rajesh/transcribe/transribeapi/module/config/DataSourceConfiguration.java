package com.rajesh.transcribe.transribeapi.module.config;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.LBHttpSolrClient;
import org.apache.solr.client.solrj.impl.SolrClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.solr.SolrHealthContributorAutoConfiguration;
import org.springframework.boot.actuate.jdbc.DataSourceHealthIndicator;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
@EnableAutoConfiguration(exclude = {
        SolrAutoConfiguration.class,
        SolrHealthContributorAutoConfiguration.class})
public class DataSourceConfiguration {
    
    @Value("${solr.solrUrl}")
    private String solrUrl ;
    @Value("${solr.zkhosts}")
    private String zkHost ;
    @Bean
    @Profile("default-dev")
    public DataSource embeddedDataSource(){
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:/h2-db/schema_h2.sql")
                .addScript("classpath:/h2-db/data-temp.sql")
                .continueOnError(true)
                .build();
    }
    
    
    @Bean
    @Profile("default-dev")
    public SolrClient getCloudSolrClient(){
        return new CloudSolrClient.Builder()
                .withLBHttpSolrClient(getLBHttpSolrClient())
                .withConnectionTimeout(10000)
                .withSocketTimeout(60000)
                .build();
    }
    
    
    @Bean
    public LBHttpSolrClient getLBHttpSolrClient(){
        return new LBHttpSolrClient.Builder()
                .withBaseSolrUrl(solrUrl)
                .withConnectionTimeout(10000)
                .withSocketTimeout(60000)
                .build();
    }
    
   /*
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
