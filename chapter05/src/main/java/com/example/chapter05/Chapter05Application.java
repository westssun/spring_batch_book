package com.example.chapter05;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/* @EnableBatchProcessing 애노테이션을 적용하면 스프링 배치는
BatchConfigurer 인터페이스를 사용해 프레임워크에서 사용되는 각 인프라스트럭처 컴포넌트의 인스턴스를 얻는다. */
@EnableBatchProcessing /* 배치 잡에 필요한 인프라스트럭처를 제공 */
@SpringBootApplication
public class Chapter05Application {

    public static void main(String[] args) {
        SpringApplication.run(Chapter05Application.class, args);
    }

}
