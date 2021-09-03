package com.example.springbatch.jobs;

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
public class AdvancedSystemCommandJob {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    /**
     * workingDirectory : 명령을 실행할 디렉터리다. 명령을 실제로 실행하기 전에 cd ~/spring-batch 를 실행하는 것과 같다.
     * systemProcessExitCodeMapper : 시스템 반환 코드를 스프링 배치 상태 값으로 매핑할 수 있는 SystemExitCodeMapper 구현체를 사용할 수 있다.
       - ConfigurableSystemProcessExitCodeMapper : 일반적인 구성 방법으로 매핑 구성을 할 수 있게 해준다
       - SimpleSystemProcessExitCodeMapper : 반환된 시스템 코드가 0이면 ExitStatus.FINISHED 를 반환하고, 0이 아니면 FAILED 를 반환한다.
     * terminateCheckInterval : 시스템 명령은 기본적으로 비동기 방식으로 실행되므로, 명령 실행 이후에 태스크릿은 해당 명령의 완료 여부를 주기적으로 확인한다.
                                기본적으로 이 값은 1초로 설정되어있지만 원하는 값으로 변경 가능하다.
     * taskExecutor : 시스템 명령을 실행하는 자신만의 고유한 TaskExecutor 를 구성할 수 있다. 시스템 명령이 문제가 발생한다면 잡에 락이 걸릴 수 있으므로
                      동기식 TaskExecutor 를 구성하지 않는게 좋다.
     * envirnmentParams : 명령을 실행하기 전에 설정하는 환경 파라미터 목록이다.
     */
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
//		SystemCommandTasklet tasklet = new SystemCommandTasklet();
//
//		tasklet.setCommand("touch tmp.txt");
//		tasklet.setTimeout(5000);
//		tasklet.setInterruptOnCancel(true);
//
//		// Change this directory to something appropriate for your environment
//		tasklet.setWorkingDirectory("/Users/mminella/spring-batch");
//
//		tasklet.setSystemProcessExitCodeMapper(touchCodeMapper());
//		tasklet.setTerminationCheckInterval(5000);
//		tasklet.setTaskExecutor(new SimpleAsyncTaskExecutor());
//		tasklet.setEnvironmentParams(new String[] {
//				"JAVA_HOME=/java",
//				"BATCH_HOME=/Users/batch"});
//
//		return tasklet;
//	}
//
//	@Bean
//	public SimpleSystemProcessExitCodeMapper touchCodeMapper() {
//		return new SimpleSystemProcessExitCodeMapper();
//	}
//
//	public static void main(String[] args) {
//		SpringApplication.run(AdvancedSystemCommandJob.class, args);
//	}
}
