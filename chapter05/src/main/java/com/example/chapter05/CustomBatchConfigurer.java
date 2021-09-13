package com.example.chapter05;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.support.DatabaseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.sql.DataSource;

public class CustomBatchConfigurer extends DefaultBatchConfigurer {
    @Autowired
    @Qualifier("repositoryDataSource")
    private DataSource dataSource;

    /**
     * 메서드 재정의
     * DefaultBatchConfigurer.createJobRepository()
     * DefaultBatchConfigurer 가 실제로 JobRepository 를 생성할때 사용하는 메서드
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
}
