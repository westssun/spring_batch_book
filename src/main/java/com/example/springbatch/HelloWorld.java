package com.example.springbatch;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

public class HelloWorld implements Tasklet {
    private static final String HELLO_WORLD = "Hello, %s";

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        /**
         * StepContext 의 getStepExecutionContext 메소드가 존재
         * : 잡의 ExecutionContext 의 현재 상태를 나타내는 Map<String, Object>를 반환한다.
         *   현재 값에 접근할 수 있지만, 반환된 Map 을 변경하더라도 실제 내용이 바뀌지 않는다.
         *   따라서 실제 ExecutionContext 에 반영되지않은 Map의 변경사항은 오류 발생시 사라진다.
         */
        String name = (String) chunkContext.getStepContext() /* StepContext */
                .getJobParameters()
                .get("name");

        /** 잡의 Execution Context 가져오기 */
//        ExecutionContext jobContext = chunkContext.getStepContext() /* 스텝 */
//                                            .getStepExecution()
//                                            .getJobExecution() /* 잡 */
//                                            .getExecutionContext();

        /** 스텝의 Execution Context 가져오기 */
        ExecutionContext jobContext = chunkContext.getStepContext() /* 스텝 */
                                            .getStepExecution()
                                            .getExecutionContext();

        jobContext.put("user.name", name);

        System.out.println(String.format(HELLO_WORLD, name));
        return RepeatStatus.FINISHED;
    }
}
