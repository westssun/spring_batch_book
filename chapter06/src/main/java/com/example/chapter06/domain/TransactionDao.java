package com.example.chapter06.domain;

import java.util.List;

public interface TransactionDao {
    /** 제공된 계좌번호와 연관도니 거래 목록을 반환 */
    List<Transaction> getTransactionsByAccountNumber(String accountNumber);
}
