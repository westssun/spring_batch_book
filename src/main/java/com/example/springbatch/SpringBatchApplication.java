package com.example.springbatch;

import com.example.springbatch.listener.JobLoggerAnnotationListener;
import com.example.springbatch.listener.JobLoggerListener;
import com.example.springbatch.parameter.DailyJobTimestamper;
import com.example.springbatch.validator.ParameterValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.listener.JobListenerFactoryBean;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

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
//        return this.stepBuilderFactory.get("step1")
//                .tasklet((stepContribution, chunkContext) -> { /* Tasklet 구현체로써 람다 코드 전달 */
//                    System.out.println("Hello, world!");
//                    return RepeatStatus.FINISHED;
//                }).build();
        return this.stepBuilderFactory.get("step1")
                                .tasklet(helloWorldTasklet())
                                .build();
    }

    @Bean
    public Tasklet helloWorldTasklet() {
        return (stepContribution, chunkContext) -> {
            String name = (String) chunkContext.getStepContext() /* 타입 캐스팅 */
                    .getJobParameters() /* 잡 파라미터를 가져오면 Map<String, Object> 가 반환된다. */
                    .get("name");

            System.out.println(String.format("Hello, %s!", name));
            return RepeatStatus.FINISHED;
        };
    }

//    @Bean
//    @StepScope /* 잡의 실행범위에 들어갈 때까지 빈 생성을 지연 */
//    public Tasklet taskletHelloWorldTasklet(@Value("#{jobParameters['name']}") String name) {
//        return (stepContribution, chunkContext) -> {
//            System.out.println(String.format("Hello, %s!", name));
//            return RepeatStatus.FINISHED;
//        };
//    }

    /**
     * 같은 job 을 2번 호출하면(동일한 파라미터) = JobInstanceAlreadyCompleteException 발생
     * @return
     */
    @Bean
    public Job job() {
        /* get 메서드를 호출하면서 잡 이름을 전달하면 JobBuilder 을 얻을 수 있다. */
        return this.jobBuilderFactory.get("job")
                .start(step())
                .validator(validator()) /* 검증 추가 */
                .incrementer(new DailyJobTimestamper()) /* 파라미터 자동 증가 */
                .listener(new JobLoggerListener()) /* 리스너 추가 */
                .listener(JobListenerFactoryBean.getListener(new JobLoggerAnnotationListener())) /* 리스너 추가2 */
                .build(); /* 실제 잡 생성 */
    }

    /**
     * 필수 파라미터 누락을 확인하는 유효성 검증기 (기본적으로 제공되는 유효성체크)
     * 유효성 검증기 구성에 사용하는 JobBuilder 의 메서드는 하나의 JobParameterValidator 인스턴스만 저장한다.
     * 이를 해결하기 위해 아래 CompositeJobParametersValidator 을 사용한다.
     * @return
     */
//    @Bean
//    public JobParametersValidator validator() {
//        DefaultJobParametersValidator validator = new DefaultJobParametersValidator();
//
//        validator.setRequiredKeys(new String[] {"fileName"}); /* 필수 파라미터 */
//        validator.setOptionalKeys(new String[] {"name"}); /* 필수 아닌 파라미터 */
//
//        return validator;
//    }

    @Bean
    public CompositeJobParametersValidator validator() {
        CompositeJobParametersValidator validator
                    = new CompositeJobParametersValidator();

        DefaultJobParametersValidator defaultJobParametersValidator
                    = new DefaultJobParametersValidator(
                            new String[] {"fileName"},
                            new String[] {"name", "currentDate"}
        );

        defaultJobParametersValidator.afterPropertiesSet();

        validator.setValidators(
                Arrays.asList(new ParameterValidator(), defaultJobParametersValidator)
        );

        return validator;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchApplication.class, args);
    }

}
