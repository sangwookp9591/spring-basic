package com.example.core.scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Provider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

public class SingletonWithPrototypeTest1 {

    @Test
    void prototypeFind() {

        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(
            PrototypeBean.class);

        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
        prototypeBean1.addCount();
        Assertions.assertThat(prototypeBean1.getCount()).isEqualTo(1);


        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
        prototypeBean2.addCount();
        Assertions.assertThat(prototypeBean2.getCount()).isEqualTo(1);
        ac.close();
    }

    @Test
    void singletonClientUsePrototyppe() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(
            ClientBean.class, PrototypeBean.class);

        ClientBean clientBean1 = ac.getBean(ClientBean.class);
        int count1 = clientBean1.logic();
        ClientBean clientBean2 = ac.getBean(ClientBean.class);
        int count2 = clientBean2.logic();

        Assertions.assertThat(count1).isEqualTo(1);
//        Assertions.assertThat(count2).isEqualTo(2);
        Assertions.assertThat(count2).isEqualTo(1);
    }

    @Scope("prototype")
    static class PrototypeBean {
        private int count =0;
        public void addCount() {
            count++;
        }

        public int getCount() {
            return count;
        }

        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init = " + this);
        }


        @PreDestroy
        public void destroy() {
            System.out.println("PrototypeBean.destroy ");
        }
    }

    @Scope("singleton")
    static class ClientBean {
//        private final PrototypeBean prototypeBean; //ClientBean 생성 시점에 주입.

        /***
         * ObjectProvider은 Springcontainer를 통해서  DL(Dependency lookup)을 좀간단하게 도와주는 역할
         * ObjectProvider getObject를 호출하면 내부에서는 스프링 컨테이너를 통해 해당 빈을 찾아서 반환
         * 스프링이 제공하는 기능을 사용하지만, 기능이 단순하므로 단위테스트를 만들거나 mock코드를 만들기는 쉬워짐
         *
         * '특징'
         * ObjectFatory: 기능이 단순, 별도의 라이브러리 필요 없음,스프링에 의존
         * ObjectProvider: ObjectFactory 상속, 옵션, 스트림 처리등 편의 기능이 많고, 별도의 라이브러리가 필요없음, 스프링에 의존.
         * */

        /**
         * Provider jsr-330 -> gradle에 javax.inject:javax.injecT:1 라이브러리를 추가해줘야함
         * Provider.get()을 통해서 항상 새로운 프로토타입 빈이 생성되는 것을 확인할 수 있다.
         * Provider.get을 호출하면 내부에서는 스프링 컨테이너를 통해서 해당 빈을 찾아서 반환(DL)
         * 자바 표준이고, 기능이 단순하므로 단위테스트를 만들거나 mock 코드를 만들기 훨씬 숴워진다.
         * 자바 표준이므로 스프링이 아닌 다른 컨테이너에서 사용할 수 있다.
         * */

        /**
         * '정리'
         *
         * 사용시기 -> 매번 사용할 때 마다 의존관계 주입이 완료된 새로운 객체가 필요하면 사용하면 된다.
         * 실무에서는 싱글톤 빈으로 대부분의 문제를 해결할 ㅅ ㅜ있기 댸문에 프로토타입 빈을 직접적으로 사용하는 일은 매우 드물다.
         * */
        @Autowired
//        private ObjectProvider<PrototypeBean> prototypeBeanProvider;
        private Provider<PrototypeBean> prototypeBeanProvider;
        //ObjectFactory랑 똑같지만 옛날거 구현해서 더편의기능 추가한게 Provider
//        @Autowired
//        public ClientBean(PrototypeBean prototypeBean) {
//            this.prototypeBean = prototypeBean;
//        }

        public int logic() {
//            PrototypeBean prototypeBean = prototypeBeanProvider.getObject();
            PrototypeBean prototypeBean = prototypeBeanProvider.get();
            //getObject 호출하면 이때서야 Spring Container에서 prototypeBean을 찾아서 반환

            prototypeBean.addCount();
            return prototypeBean.getCount();
        }
    }
}
