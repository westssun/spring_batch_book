package com.example.chapter04.jobs;

import java.util.Random;

import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * @author Michael Minella
 */
public class RandomChunkSizePolicy implements CompletionPolicy {

    private int chunksize;
    private int totalProcessed;
    private Random random = new Random();

    /**
     * 청크 완려여부의 상태를 기반으로 결정 로직을 수행한다.
     * @param context
     * @param result
     * @return
     */
    @Override
    public boolean isComplete(RepeatContext context,
                              RepeatStatus result) {

        if(RepeatStatus.FINISHED == result) {
            return true;
        }
        else {
            return isComplete(context);
        }
    }

    /**
     * 내부 상태 이용해 청크 완료 여부를 판단한다.
     * @param context
     * @return
     */
    @Override
    public boolean isComplete(RepeatContext context) {
        return this.totalProcessed >= chunksize;
    }

    /**
     * 청크의 시작 시에 내부 카운터를 0으로 초기화한다.
     * @param parent
     * @return
     */
    @Override
    public RepeatContext start(RepeatContext parent) {
        this.chunksize = random.nextInt(20);
        this.totalProcessed = 0;

        System.out.println("The chunk size has been set to " +
                this.chunksize);

        return parent;
    }

    /**
     * 각 아아템이 처리되면 update 메서드가 한번씩 호출되면서 내부 상태를 갱신한다.
     * @param context
     */
    @Override
    public void update(RepeatContext context) {
        this.totalProcessed++;
    }
}
