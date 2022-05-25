package com.example.core.autowired;

import com.example.core.member.Member;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.lang.Nullable;

public class AutowiredTest {

    /**
     * 옵션 처리
     * 주입할 스프링 빈이 없어도 동작해야 할 때가 있다.
     *
     * 그런데 @Autowired만 사용하면 required기본 옵션이 true로 되어 있어서 자동 주입대상이 없으면 오류 발생
     *
     * 자동주입 대상을 옵션으로 처리하는 방법
     *  - @Autowired(required = false) : 자동 주입할 대상이 없으면 수정자 메서드 자체가 호출안됨
     *  - org.sprignframework.lang.@Nullable : 자동 주입할 대상이 없으면 null이 입력
     *  - Optional<> : 자동 주입할 대상이 없으면  Optional.empty 가 입력됨
     *
     * */
    @Test
    void autowiredOption() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(
            TestBean.class); //이렇게해도 compoentScan처럼 spring bean 등록

    }


    static class TestBean{

        //     *  - @Autowired(required = false) : 자동 주입할 대상이 없으면 수정자 메서드 자체가 호출안됨
        @Autowired(required = false)
        public void setNoBean1(Member noBean1) { //member는 spring 에서 관리되고 있지 않음.
            System.out.println("noBean1 = " + noBean1); //메소드 자체가 호출도안됨

        }

        //     *  - org.sprignframework.lang.@Nullable : 자동 주입할 대상이 없으면 null이 입력
        @Autowired
        public void setNoBean2(@Nullable Member noBean2) {
            System.out.println("noBean2 = " + noBean2); //null
        }

        //     *  - Optional<> : 자동 주입할 대상이 없으면  Optional.empty 가 입력됨
        @Autowired
        public void setNoBean3(Optional<Member> noBean3) {
            System.out.println("noBean3 = " + noBean3);//Optinal.empty
        }
    }

}
