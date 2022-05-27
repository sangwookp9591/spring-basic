package com.example.core.scope;

import com.example.core.scope.SingletonTest.SingletonBean;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

public class PrototypeTest {

    @Test
    void prototypeBeanFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(
            PrototypeBean.class);

        System.out.println("find prototypeBean1 "); //프로토타입을 조회하기전에 호출이됨
        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class); //이때만들어짐
        System.out.println("find prototypeBean2 "); //프로토타입을 조회하기전에 호출이됨
        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class); //이때만들어짐
        System.out.println("prototypeBean1 = " + prototypeBean1);
        System.out.println("prototypeBean2 = " + prototypeBean2);
        Assertions.assertThat(prototypeBean1).isNotSameAs(prototypeBean2);
        ac.close();
    }

    @Scope("prototype")
    static class PrototypeBean {

        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init");
        }

        @PreDestroy
        public void destroy() {
            System.out.println("PrototypeBean.destroy");
        }
    }

}
