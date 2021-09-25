package com.example.chapter06;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;

import com.example.chapter06.batch.TransactionApplierProcessor;
import com.example.chapter06.batch.TransactionReader;
import com.example.chapter06.domain.AccountSummary;
import com.example.chapter06.domain.Transaction;
import com.example.chapter06.domain.TransactionDao;
import com.example.chapter06.domain.support.TransactionDaoSupport;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.PassThroughFieldSetMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

/**
 * 339599,2018-01-13 18:53:29,884.16
 * 991085,2018-01-13 18:53:29,-15.05
 * 297579,2018-01-13 18:53:29,-344.38
 *
 * @author Michael Minella
 */
@EnableBatchProcessing
@SpringBootApplication
public class TransactionProcessingJob {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    @StepScope
    public TransactionReader transactionReader() {
        return new TransactionReader(fileItemReader(null));
    }

    @Bean
    @StepScope
    public FlatFileItemReader<FieldSet> fileItemReader(
            @Value("#{jobParameters['transactionFile']}") Resource inputFile) {
        return new FlatFileItemReaderBuilder<FieldSet>()
                .name("fileItemReader")
                .resource(inputFile)
                .lineTokenizer(new DelimitedLineTokenizer())
                .fieldSetMapper(new PassThroughFieldSetMapper())
                .build();
    }

    /**
     * 값을 데이터베이스에 저장
     * @param dataSource
     * @return
     */
    @Bean
    public JdbcBatchItemWriter<Transaction> transactionWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Transaction>()
                .itemSqlParameterSourceProvider(
                        new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO TRANSACTION " +
                        "(ACCOUNT_SUMMARY_ID, TIMESTAMP, AMOUNT) " +
                        "VALUES ((SELECT ID FROM ACCOUNT_SUMMARY " +
                        "	WHERE ACCOUNT_NUMBER = :accountNumber), " +
                        ":timestamp, :amount)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Step importTransactionFileStep() {
        return this.stepBuilderFactory.get("importTransactionFileStep")
                // .startLimit(2) /* 잡의 재시작 횟수 제한 */
                // .allowStartIfComplete(true) /* 완료된 스텝 재실행 (스텝이 완료되었더라도 다시 실행할 수 있다고 알린다) */
                .<Transaction, Transaction>chunk(100)
                .reader(transactionReader())
                .writer(transactionWriter(null))
                .allowStartIfComplete(true) /* 잡이 재시작될시, 스텝이 다시 실행될 수 있도록 재시작 허용 */
                .listener(transactionReader()) /* 스텝 빌드하기 전 실행할 리스너 등록 */
                .build();
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<AccountSummary> accountSummaryReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<AccountSummary>()
                .name("accountSummaryReader")
                .dataSource(dataSource)
                .sql("SELECT ACCOUNT_NUMBER, CURRENT_BALANCE " +
                        "FROM ACCOUNT_SUMMARY A " +
                        "WHERE A.ID IN (" +
                        "	SELECT DISTINCT T.ACCOUNT_SUMMARY_ID " +
                        "	FROM TRANSACTION T) " +
                        "ORDER BY A.ACCOUNT_NUMBER")
                .rowMapper((resultSet, rowNumber) -> {
                    AccountSummary summary = new AccountSummary();

                    summary.setAccountNumber(resultSet.getString("account_number"));
                    summary.setCurrentBalance(resultSet.getDouble("current_balance"));

                    return summary;
                }).build();
    }

    @Bean
    public TransactionDao transactionDao(DataSource dataSource) {
        return new TransactionDaoSupport(dataSource);
    }

    @Bean
    public TransactionApplierProcessor transactionApplierProcessor() {
        return new TransactionApplierProcessor(transactionDao(null));
    }

    @Bean
    public JdbcBatchItemWriter<AccountSummary> accountSummaryWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<AccountSummary>()
                .dataSource(dataSource)
                .itemSqlParameterSourceProvider(
                        new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("UPDATE ACCOUNT_SUMMARY " +
                        "SET CURRENT_BALANCE = :currentBalance " +
                        "WHERE ACCOUNT_NUMBER = :accountNumber")
                .build();
    }

    @Bean
    public Step applyTransactionsStep() {
        return this.stepBuilderFactory.get("applyTransactionsStep")
                .<AccountSummary, AccountSummary>chunk(100)
                .reader(accountSummaryReader(null))
                .processor(transactionApplierProcessor())
                .writer(accountSummaryWriter(null))
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<AccountSummary> accountSummaryFileWriter(
            @Value("#{jobParameters['summaryFile']}") Resource summaryFile) {

        DelimitedLineAggregator<AccountSummary> lineAggregator =
                new DelimitedLineAggregator<>();
        BeanWrapperFieldExtractor<AccountSummary> fieldExtractor =
                new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[] {"accountNumber", "currentBalance"});
        fieldExtractor.afterPropertiesSet();
        lineAggregator.setFieldExtractor(fieldExtractor);

        return new FlatFileItemWriterBuilder<AccountSummary>()
                .name("accountSummaryFileWriter")
                .resource(summaryFile)
                .lineAggregator(lineAggregator)
                .build();
    }

    @Bean
    public Step generateAccountSummaryStep() {
        return this.stepBuilderFactory.get("generateAccountSummaryStep")
                .<AccountSummary, AccountSummary>chunk(100)
                .reader(accountSummaryReader(null))
                .writer(accountSummaryFileWriter(null))
                .build();
    }

    /**
     * 잡 실행
     * @return
     */
    @Bean
    public Job transactionJob() {
        return this.jobBuilderFactory.get("transactionJob")
                .preventRestart() /* 잡은 기본적으로 재시작이 가능하도록 구성되어있다. 잡의 재시작 방지 */
                .start(importTransactionFileStep())
                .next(applyTransactionsStep())
                .next(generateAccountSummaryStep())
                .build();
//		return this.jobBuilderFactory.get("transactionJob")
//				.start(importTransactionFileStep())
        // EXITSTATUS 가 STOPPED 라면 잡을 중지하며, 재시작시 해당 스텝부터 다시 시작한다.
//				.on("STOPPED").stopAndRestart(importTransactionFileStep())
        // EXITSTATUS 가 STOPPED 가 아니라면 applyTransactionSTep 으로 넘어간다
//				.from(importTransactionFileStep()).on("*").to(applyTransactionsStep())
        // applyTransactionSteㅔ 이 끝나면 generateAccountSummaryStep 으로 넘어간다.
//				.from(applyTransactionsStep()).next(generateAccountSummaryStep())
//				.end()
//				.build();
    }

    public static void main(String[] args) {
        List<String> realArgs = new ArrayList<>(Arrays.asList(args));

        realArgs.add("transactionFile=input/transactionFile.csv");
        realArgs.add("summaryFile=file:///Users/mminella/tmp/summaryFile3.csv");

        SpringApplication.run(TransactionProcessingJob.class, realArgs.toArray(new String[realArgs.size()]));
    }
}
