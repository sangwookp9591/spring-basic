package com.example.core.lifecycle;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanLifeCycleTest {

    @Test
    public void lifeCycleTest() {
        ConfigurableApplicationContext cac = new AnnotationConfigApplicationContext(
            LifeCycleConfig.class);

        //ApplicationContext는 기본적으로 close를 제공하지 않는다.

        NetworkClient client = cac.getBean(NetworkClient.class);
        cac.close();

    }

    @Configuration
    static class LifeCycleConfig {

        /**
         * 스프링 빈의 간단한 라이프사이클
         * 객체생성 -> 의존관계 주입
         *
         * 스프링은 의존관계 주입이 완료되면 스프링 빈에게 콜백 메서드를 통해서 초기화 시점을 알려주는 다양한 기능을 제공
         * - 스프링은 스프링 컨테이너가 종료되기 직전에 소멸 콜백을 한다.
         *
         * - 스프링 빈의 이벤트 라이프사이클
         * 스프링 컨테이너 생성 -> 스프링 빈 생성 -> 의존관계 주입 -> 초기화 콜백 ->  실제 application이 사용 -> 소멸전 콜백 -> 스프링 종료
         * 초기화 콜백 :  빈이 생성되고, 빈의 의존관계 주입이 완료된 후 호출
         * 소멸전 콜백 : 빈이 소멸되기 직전에 호출
         *
         * 참고 :  객체의 생성과 초기화를 분리하자!
         * 생성자는 필수 파라미터를 받고 메모리를 할당해서 객체를 생성하는 책임을 가진다.
         * 반면에 초기화는 이렇게 생성된 값을 활용해서 외부 커넨션을 연결하는등 무거운 동작 수행
         * 따라서 생성자 안에서 무거운 초기화 작업을 함께 하는 것보다 객체를 생성하는 부분과 초기화 하는 부분을 명확하게 나누는 것이
         * 유지보수 관점에서 좋다.
         * 최초에 어떤 행위가 쥐어졌을때 초기화할수있는 장점 (동작을 지연시킬수있는 장점이있다.)
         *
         * 물론 초기화 작업이 내부 값들만 약간 변경하는 정도로 단순한 경우에는 생정자에서 한번에 다 처리하는게 더 나을 수 있다.
         * 복잡도에 따라 다르게 설정하다!. 생성자와 초기화 단계를 분리하던 붙이던.
         *
         *
         * */
//        @Bean(initMethod = "init", destroyMethod = "close")
        @Bean
        public NetworkClient networkClient() {
            NetworkClient networkClient = new NetworkClient();//생성한다음에  ->여기서 다 Null로나옴.
            networkClient.setUrl("http://hello-spring.dev");//설정
            return networkClient;
        }
    }

}
