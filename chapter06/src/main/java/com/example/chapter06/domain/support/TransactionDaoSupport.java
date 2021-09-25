package com.example.chapter06.domain.support;

import java.util.List;

import javax.sql.DataSource;

import com.example.chapter06.domain.Transaction;
import com.example.chapter06.domain.TransactionDao;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author Michael Minella
 */
public class TransactionDaoSupport extends JdbcTemplate implements TransactionDao {

    public TransactionDaoSupport(DataSource dataSource) {
        super(dataSource);
    }

    /**
     * 모든 거래 레코드를 조회하고 반환한다.
     * ItemProcessor가 이 모든 거래정보를 주어진 계좌에 적용해 현재 잔액을 결정한다. (TransactionApplierProcessor)
     * @param accountNumber
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Transaction> getTransactionsByAccountNumber(String accountNumber) {
        return query(
                "select t.id, t.timestamp, t.amount " +
                        "from transaction t inner join account_summary a on " +
                        "a.id = t.account_summary_id " +
                        "where a.account_number = ?",
                new Object[] { accountNumber },
                (rs, rowNum) -> {
                    Transaction trans = new Transaction();
                    trans.setAmount(rs.getDouble("amount"));
                    trans.setTimestamp(rs.getDate("timestamp"));
                    return trans;
                }
        );
    }
}
