package com.example.chapter06.batch;

import java.util.List;

import com.example.chapter06.domain.AccountSummary;
import com.example.chapter06.domain.Transaction;
import com.example.chapter06.domain.TransactionDao;
import org.springframework.batch.item.ItemProcessor;

/**
 * @author Michael Minella
 */
public class TransactionApplierProcessor implements
        ItemProcessor<AccountSummary, AccountSummary> {

    private TransactionDao transactionDao;

    public TransactionApplierProcessor(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }

    /**
     * ItemProcessor가 이 모든 거래정보를 주어진 계좌에 적용해 현재 잔액을 결정한다.
     * @param summary
     * @return
     * @throws Exception
     */
    public AccountSummary process(AccountSummary summary) throws Exception {
        List<Transaction> transactions = transactionDao
                .getTransactionsByAccountNumber(summary.getAccountNumber());

        for (Transaction transaction : transactions) {
            summary.setCurrentBalance(summary.getCurrentBalance()
                    + transaction.getAmount());
        }
        return summary;
    }
}