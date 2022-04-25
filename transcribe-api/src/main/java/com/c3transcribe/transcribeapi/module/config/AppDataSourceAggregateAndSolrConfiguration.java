package com.c3transcribe.transcribeapi.module.config;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.LBHttpSolrClient;
import org.apache.solr.client.solrj.impl.SolrClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.solr.SolrHealthContributorAutoConfiguration;
import org.springframework.boot.actuate.jdbc.DataSourceHealthIndicator;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.sql.Clob;
import java.sql.SQLException;

import static org.apache.calcite.linq4j.tree.Primitive.asList;

@Configuration
@EnableAutoConfiguration(exclude = {
        SolrAutoConfiguration.class,
        SolrHealthContributorAutoConfiguration.class})
public class AppDataSourceAggregateAndSolrConfiguration  {
        //extends AbstractJdbcConfiguration {
    
    @Value("${solr.solrUrl}")
    private String solrUrl ;
    @Value("${solr.zkhosts}")
    private String zkHost ;
    @Autowired
    private DataSource dataSource;
    
    @Bean
    @Profile("default-local")
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
    
    @Bean
    public DataSourceHealthIndicator dataSourceHealthIndicator() {
        return new DataSourceHealthIndicator(dataSource);
    }
    
    /*@Override
     // more info @ https://github.com/spring-projects/spring-data-examples/blob/master/jdbc/basics/src/main/java/example/springdata/jdbc/basics/aggregate/AggregateConfiguration.java
    public JdbcCustomConversions jdbcCustomConversions() {
        
        return new JdbcCustomConversions(asList(new Converter<Clob, String>() {
            
            @Embedded.Nullable
            @Override
            public String convert(Clob clob) {
                
                try {
                    
                    return Math.toIntExact(clob.length()) == 0 //
                            ? "" //
                            : clob.getSubString(1, Math.toIntExact(clob.length()));
                    
                } catch (SQLException e) {
                    throw new IllegalStateException("Failed to convert CLOB to String.", e);
                }
            }
        }));
    }*/
}
