package com.example.chapter05;

import java.util.List;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class ExploringTasklet implements Tasklet {
    private JobExplorer explorer;

    public ExploringTasklet(JobExplorer explorer) {
        this.explorer = explorer;
    }

    public RepeatStatus execute(StepContribution stepContribution,
                                ChunkContext chunkContext) {

        String jobName = chunkContext.getStepContext().getJobName();

        /* 지금까지 실행됬던 모든 JobInstance 를 가져온다 (현재의 JobInstance 까지 함께 조회) */
        List<JobInstance> instances =
                explorer.getJobInstances(jobName,
                        0,
                        Integer.MAX_VALUE);

        System.out.println(
                String.format("There are %d job instances for the job %s",
                        instances.size(), /* 총 잡의 개수 */
                        jobName));

        System.out.println("They have had the following results");
        System.out.println("************************************");

        /* 각 잡의 결과 표시 */
        for (JobInstance instance : instances) {
            List<JobExecution> jobExecutions =
                    this.explorer.getJobExecutions(instance);

            System.out.println(
                    String.format("Instance %d had %d executions",
                            instance.getInstanceId(),
                            jobExecutions.size()));

            for (JobExecution jobExecution : jobExecutions) {
                System.out.println(
                        String.format("\tExecution %d resulted in Exit Status %s",
                                jobExecution.getId(),
                                jobExecution.getExitStatus()));
            }
        }

        return RepeatStatus.FINISHED;
    }
}
