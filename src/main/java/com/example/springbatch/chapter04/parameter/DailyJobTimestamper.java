package com.example.springbatch.chapter04.parameter;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;

import java.util.Date;

/**
 * 잡 실행시마다 타임스탬프를 파라미터로 사용할 경우 직접 구현해야 한다.
 */
public class DailyJobTimestamper implements JobParametersIncrementer {

    @Override
    public JobParameters getNext(JobParameters jobParameters) {
        return new JobParametersBuilder(jobParameters)
                .addDate("currentDate", new Date())
                .toJobParameters();
    }
}
