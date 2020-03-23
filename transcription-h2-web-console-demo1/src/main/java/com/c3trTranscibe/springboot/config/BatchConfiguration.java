package com.c3trTranscibe.springboot.config;

import com.c3trTranscibe.springboot.services.batch.BatchTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class BatchConfiguration {


    @Bean
    public Job executeBatchJob(JobBuilderFactory jobBuilderFactory, Step step) {
        return jobBuilderFactory.get("manualjob").incrementer(new RunIdIncrementer())
                .flow(step).end().build();
    }

    @Bean
    public Step step(StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("step").tasklet(getBatchTasklet()).build();
    }

    @Bean
    public BatchTasklet getBatchTasklet() {
        return new BatchTasklet();
    }

}