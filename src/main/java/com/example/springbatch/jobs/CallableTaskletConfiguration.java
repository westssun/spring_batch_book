package com.example.springbatch.jobs;

import java.util.concurrent.Callable;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author Michael Minella
 */
@EnableBatchProcessing
@SpringBootApplication
@RequiredArgsConstructor
public class CallableTaskletConfiguration {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

//	@Bean
//	public Job callableJob() {
//		return this.jobBuilderFactory.get("callableJob")
//				.start(callableStep())
//				.build();
//	}
//
//	@Bean
//	public Step callableStep() {
//		return this.stepBuilderFactory.get("callableStep")
//				.tasklet(tasklet())
//				.build();
//	}

    @Bean
    public Callable<RepeatStatus> callableObject() {
        return () -> {
            System.out.println("This was executed in another thread");

            /* RepeatStatus 가 반환되기 전에는 완료된 것으로 간주하지 않으므로,
               해당 스텝이 완료될 떄까지 플로우 내의 다른 스텝은 실행되지 않는다.
             */
            return RepeatStatus.FINISHED;
        };
    }

//	@Bean
//	public CallableTaskletAdapter tasklet() {
//		CallableTaskletAdapter callableTaskletAdapter =
//				new CallableTaskletAdapter();
//
//		callableTaskletAdapter.setCallable(callableObject());
//
//		return callableTaskletAdapter;
//	}
//
//	public static void main(String[] args) {
//		SpringApplication.run(CallableTaskletConfiguration.class, args);
//	}
}