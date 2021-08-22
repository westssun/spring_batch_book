package com.example.springbatch;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

public class ParameterValidator implements JobParametersValidator {

    /**
     * 에러가 발생하지 않고 통과한다면 pass 했다고 볼 수 있음 (return type void)
     * @param jobParameters
     * @throws JobParametersInvalidException
     */
    @Override
    public void validate(JobParameters jobParameters) throws JobParametersInvalidException {
        String fileName = jobParameters.getString("fileName");

        if (!StringUtils.hasText(fileName)) {
            throw new JobParametersInvalidException("fileName parameter is missing");
        } else if (!StringUtils.endsWithIgnoreCase(fileName, "csv")) {
            throw new JobParametersInvalidException("file Name parameter does " +
                                                    "not use the csv file extension");
        }
    }
}
