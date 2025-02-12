package com.example.demobatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

    @Bean
    public Job sampleJob(JobRepository jobRepository, Step acquireStep, Step geocodeStep, Step persistStep, Step finishStep) {
        return new JobBuilder("sampleJob", jobRepository)
                .start(acquireStep)
                .next(geocodeStep)
                .next(persistStep)
                .next(finishStep)
                .build();
    }

    @Bean
    public Step acquireStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("acquireStep", jobRepository)
                .tasklet(acquireTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Step geocodeStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("geocodeStep", jobRepository)
                .tasklet(geocodeTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Step persistStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("persistStep", jobRepository)
                .tasklet(persistTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Step finishStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("finishStep", jobRepository)
                .tasklet(finishingTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Tasklet acquireTasklet() {
        return (contribution, chunkContext) -> {
            System.out.println("ACQUIRE step executed at: " + new java.util.Date());
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet geocodeTasklet() {
        return (contribution, chunkContext) -> {
            System.out.println("GEOCODE step executed at: " + new java.util.Date());
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet persistTasklet() {
        return (contribution, chunkContext) -> {
            System.out.println("PERSIST step executed at: " + new java.util.Date());
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet finishingTasklet() {
        return (contribution, chunkContext) -> {
            System.out.println("Finishing step executed at: " + new java.util.Date());
            System.out.println("Job completed successfully!");
            return RepeatStatus.FINISHED;
        };
    }
} 