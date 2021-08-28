package com.example.springbatch.configuration;

import com.example.springbatch.service.CustomService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author Michael Minella
 */
@EnableBatchProcessing
@SpringBootApplication
@RequiredArgsConstructor
public class MethodInvokingTaskletConfiguration {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

//	@Bean
//	public Job methodInvokingJob() {
//		return this.jobBuilderFactory.get("methodInvokingJob")
//				.start(methodInvokingStep())
//				.build();
//	}
//
//	@Bean
//	public Step methodInvokingStep() {
//		return this.stepBuilderFactory.get("methodInvokingStep")
//				.tasklet(methodInvokingTasklet(null))
//				.build();
//	}
//
//	@StepScope
//	@Bean
//	public MethodInvokingTaskletAdapter methodInvokingTasklet(
//			@Value("#{jobParameters['message']}") String message) {
//
//		MethodInvokingTaskletAdapter methodInvokingTaskletAdapter =
//				new MethodInvokingTaskletAdapter();
//
//		methodInvokingTaskletAdapter.setTargetObject(service());
//		methodInvokingTaskletAdapter.setTargetMethod("serviceMethod");
//		methodInvokingTaskletAdapter.setArguments(
//				new String[] {message}); /* 파라미터 */
//
//		return methodInvokingTaskletAdapter;
//	}

    @Bean
    public CustomService service() {
        return new CustomService();
    }

//	public static void main(String[] args) {
//		SpringApplication.run(MethodInvokingTaskletConfiguration.class, args);
//	}
}
