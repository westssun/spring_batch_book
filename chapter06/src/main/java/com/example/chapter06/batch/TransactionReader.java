package com.example.chapter06.batch;

import com.example.chapter06.domain.Transaction;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.file.transform.FieldSet;

/**
 * TransactionReader : 위임리더
 * 스프링 배치는 ItemReader, ItemProcessor, ItemWriter 가 ItemStream 의 구현체인지
 * 자동으로 확인하고 적절한 시점에 콜백이 수행되도록 자동으로 등록한다.
 * TransactionReader 는 ItemStream 을 구현하지만, 스프링 배치에 명시적으로 등록되있지 않은 상태이므로
 * 프레임워크는 해당 리더가 ItemStream 을 구현했는지 확인되지 않는다.
 * 이때 선택할 수 있는 옵션은 2가지이다.
 * 1) 잡에서 해당 위임 리더를 ItemStream 으로 명시적으로 등록한다.
 * 2) TransactionItemReader 에서 ItemStream 을 구현하고 적절한 라이프 사이클에 따라 메서드를 호출한다.
 */
public class TransactionReader implements ItemStreamReader<Transaction> {

    private ItemStreamReader<FieldSet> fieldSetReader;
    private int recordCount = 0;
    private int expectedRecordCount = 0;

    /**
     * afterStep 이 아닌 beforeStep 을 사용하도록 변경하여 StepExecution 을 가져온다.
     */
    private StepExecution stepExecution;

    public TransactionReader(ItemStreamReader<FieldSet> fieldSetReader) {
        this.fieldSetReader = fieldSetReader;
    }

    /**
     * 해당 리더에게 주입된 위임 리더에게 실제 읽기 작업을 위임한다.
     * @return
     * @throws Exception
     */
    public Transaction read() throws Exception {
        return process(fieldSetReader.read());
    }

    private Transaction process(FieldSet fieldSet) {
        /* 스프링 배치는 예외가 발생하면 기본적으로 스텝 및 잡이 실패한 것으로 간주한다 */
        /* 예외를 던짐으로써 실패 상태로 잡이 중지된다. (FAILED 상태) */
        /* 스텝이 FAILED 로 식별되면 스프링 배치는 해당 스텝으 처음부터 다시 시작하지는 않는다.
        스프링 배치는 예외가 발생할때 어떤 청크를 처리하고 잇던 중이었는지 기억을 하고, 잡을 재시작하면 스프링배치는
        중단됐던 부분을 가져온다. */
        if(this.recordCount == 25) {
            throw new ParseException("This isn't what I hoped to happen");
        }

        Transaction result = null;

        if(fieldSet != null) {
            if(fieldSet.getFieldCount() > 1) {
                result = new Transaction();
                result.setAccountNumber(fieldSet.readString(0));
                result.setTimestamp(
                        fieldSet.readDate(1,
                                "yyyy-MM-DD HH:mm:ss"));
                result.setAmount(fieldSet.readDouble(2));

                recordCount++;
            } else {
                expectedRecordCount = fieldSet.readInt(0);

                /* stepExecution : setTerminateOnly (스텝이 완료된 후 스프링 배치가 종료되도록 지시하는 플래그 설정 ) */
                if(expectedRecordCount != this.recordCount) {
                    this.stepExecution.setTerminateOnly();
                }
            }
        }

        return result;
    }

    public void setFieldSetReader(ItemStreamReader<FieldSet> fieldSetReader) {
        this.fieldSetReader = fieldSetReader;
    }

//	@AfterStep
//	public ExitStatus afterStep(StepExecution execution) {
//		if(recordCount == expectedRecordCount) {
//			return execution.getExitStatus();
//		} else {
//			return ExitStatus.STOPPED;
//		}
//	}

    /* 스텝 빌드 전 stepExecution set */
    @BeforeStep
    public void beforeStep(StepExecution execution) {
        this.stepExecution = execution;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        this.fieldSetReader.open(executionContext);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        this.fieldSetReader.update(executionContext);
    }

    @Override
    public void close() throws ItemStreamException {
        this.fieldSetReader.close();
    }
}

