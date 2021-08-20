package com.example.springbatch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableBatchProcessing /* 배치 잡에 필요한 인프라스트럭처를 제공 */
@RequiredArgsConstructor
public class SpringBatchApplication {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step step() {
//        return this.stepBuilderFactory.get("step1")
//                .tasklet(new Tasklet() {
//                    @Override
//                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
//                        System.out.println("Hello, world!");
//                        return RepeatStatus.FINISHED;
//                    }
//                }).build();
        /* get 메서드를 호출하면서 스텝 이름을 전달하면 StepBuilder 가 반환되며 이 빌더를 사용하여 스텝을 정의 */
        return this.stepBuilderFactory.get("step1")
                .tasklet((stepContribution, chunkContext) -> { /* Tasklet 구현체로써 람다 코드 전달 */
                    System.out.println("Hello, world!");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    /**
     * 같은 job 을 2번 호출하면(동일한 파라미터) = JobInstanceAlreadyCompleteException 발생
     * @return
     */
    @Bean
    public Job job() {
        /* get 메서드를 호출하면서 잡 이름을 전달하면 JobBuilder 을 얻을 수 있다. */
        return this.jobBuilderFactory.get("job")
                .start(step())
                .build(); /* 실제 잡 생성 */
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchApplication.class, args);
    }

}
