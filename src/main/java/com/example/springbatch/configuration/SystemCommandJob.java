package com.example.springbatch.configuration;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Michael Minella
 */
@EnableBatchProcessing
@SpringBootApplication
public class SystemCommandJob {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

//	@Bean
//	public Job job() {
//		return this.jobBuilderFactory.get("systemCommandJob")
//				.start(systemCommandStep())
//				.build();
//	}
//
//	@Bean
//	public Step systemCommandStep() {
//		return this.stepBuilderFactory.get("systemCommandStep")
//				.tasklet(systemCommandTasklet())
//				.build();
//	}
//
//	@Bean
//	public SystemCommandTasklet systemCommandTasklet() {
//		SystemCommandTasklet systemCommandTasklet = new SystemCommandTasklet();
//
//		systemCommandTasklet.setCommand("rm -rf /tmp.txt");
//		systemCommandTasklet.setTimeout(5000);
//		systemCommandTasklet.setInterruptOnCancel(true);
//
//		return systemCommandTasklet;
//	}
//
//	public static void main(String[] args) {
//		SpringApplication.run(SystemCommandJob.class, args);
//	}
}
