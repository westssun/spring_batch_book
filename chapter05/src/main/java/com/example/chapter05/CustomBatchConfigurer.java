package com.example.chapter05;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.support.DatabaseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

public class CustomBatchConfigurer extends DefaultBatchConfigurer {
    @Autowired
    @Qualifier("repositoryDataSource")
    private DataSource dataSource;

    @Autowired
    @Qualifier("batchTransactionManager")
    private PlatformTransactionManager transactionManager;

    /**
     * 메서드 재정의
     * DefaultBatchConfigurer.createJobRepository()
     * DefaultBatchConfigurer 가 실제로 JobRepository 를 생성할때 사용하는 메서드
     *
     * DefaultBatchConfigurer 안의 스프링 컨테이너가 빈 정의로 직접 호출하지 않는다.
     * return factory.getObject();
     * jobLauncher.afterPropertiesSet();
     * 등을 호출하여 처리한다.
     * @return
     * @throws Exception
     */
    @Override
    protected JobRepository createJobRepository() throws Exception {
        JobRepositoryFactoryBean factoryBean = new JobRepositoryFactoryBean();

        /* 설정 커스터마이징 */
        factoryBean.setDatabaseType(DatabaseType.MYSQL.getProductName());
        factoryBean.setTablePrefix("FOO_"); /* 기본값: BATCH_" */
        factoryBean.setIsolationLevelForCreate("ISOLATION_REPEATABLE_READ"); /* 기본값 : ISOLATION_SERIALIZED */
        factoryBean.setDataSource(this.dataSource);
        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }

    /**
     * BatchConfigurer.getRansactionManager() 메서드를 호출하면 배치 처리에 사용할 목적으로
     * 어딘가에 정의해둔 PlatformTransactionManager 가 명시적으로 반환된다.
     * @return
     */
    @Override
    public PlatformTransactionManager getTransactionManager() {
        /**
         * TransactionManager 가 생성되지 않은 경우에는
         * DefaultBatc hConfigurer 가 기본적으로 setDataSource 수정자 내에서
         * DataSourceTransactionManager 를 자동으로 생성하기 때문이다.
         *
         * TransactionManager 는 BatchCOnfigurer에 의해 이런 방식으로 노출될 수 있는 유일한 컴포넌트다.
         */
        return this.transactionManager;
    }
}
