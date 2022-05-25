package com.example.core.singleton;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.junit.jupiter.api.Assertions.*;

class StatefulServiceTest {

    // 객체 인스턴스를 딱하나만 생성해서 공유하는 SingleTon방식은 주의해야할것이있다.
    // 여러 client가 하나의 객체 인스턴스를 공유하기 때문에 싱글톤 객체는 상태를 유지하게 설계하면 안된다.
    // 무상태로 설계해야함!
    // - 특정 클라이언트에 의존적인 필드가 있으면 안된다.
    // - 특정 클라이언트가 값을 변경할 수 있는 필드가 있으면 안된다.
    // - 가급적 읽기만 가능해야한다.
    // - 필드 대신에 자바에서 공유되지 않는, 지역변수, 파라미터, ThradeLocal등을 사용해야한다.
    // 스프링 빈(singleton bean)에 공유값을 설정하면 큰 장애가 발생할 수 있다.
    @Test
    @DisplayName("")
    void statefulServiceSingleton(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean("statefulService", StatefulService.class);
        StatefulService statefulService2 = ac.getBean("statefulService", StatefulService.class);

        //ThreadA: A사용자 10000원 주문
        int priceA = statefulService1.order("userA", 10000);
        //ThreadB: B사용자가 끼어들어와서 20000원 주문
        int priceB = statefulService2.order("userB", 20000);

        //ThreadA : 사용자A가 주문 금액 조회
//        int price1 = statefulService1.getPrice();
//        int price2 = statefulService1.getPrice();

        //20000원 나올듯
        Assertions.assertThat(priceA).isNotEqualTo(priceB);
        //기대한것은 10000원이지만 20000원이 나옴.
        // instance를 같은 애로 사용하고, 공유하는 값을 가지고 있기때문에

        // StatefulService 의 price는 공유되는 필드인데, 특정 클라이언트가 값을 변경한다.
        // 공유 필드는 조심해야한다 -> 무상태로 설계해야한다.
    }

    static class TestConfig{
        @Bean
        public StatefulService statefulService(){
            return new StatefulService();

        }
    }

}