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

/**
 * 생성자 주입을 권장 하는 이유
 * '불변'
 * - 대부분의 의존관계 주입은 한번 일어나면 애플리케이션 종료 시점까지 의존관계를 변경할 일이 없다.
 * 오히려 대부분의 의존관게는 애플리케이션 종료 전까지 변하면 안된다.(불변해야한다)
 * - 수정자 주입을 사용하면 , setXxx 메서드를 public으로 열어두어야 한다.
 * 누군가 실수로 변경할 수도 있고, 변경하면 안되는 메서드를 열어두는 것은 좋은 설계 방법이 아니다.
 * 생성자 주입은 객체를 생성할때 딱 1번만 호출되므로 이후에 호출되는 일이 없다. 따라서 불변하게 설계할 수 있다.
 *
 * '누락'
 * 프레임워크 없이 순수한 자바 코드를 단위 테스트하는 경우 -편의에 따라 가짜객체를 주입해서 사용할때도 있다.
 * 만약 수정자로 했을경우 임의의
 * OrderServiceImpl orderService = new OrderServiceImpl();
 * 이라고 하고
 * orderService.createOrder를하면 런타임에서 에러가 발생한다.
 * 하지만 생성자 주입일경우
 * new OrderServiceImpl();에서 바로 에러를 캐치 할수 있다.
 * new OrderServiceImpl(new 가째객체, new 가짜객체); 이런식으로 mock mvc를 사용할 수도 있다.
 *
 * 그리고 생성자는 final 키워드를 사용할 수있다. 한번 생성할게 정해지면 안바뀐다. 초기값이나 생성자에서만 값을 넣을 수 있다.
 * 그리고 final을 넣으면 초기화 단계에 생성자에서 값이 안들어올 경우 컴파일 시점에 오류로 알려준다.
 *
 * tmi - 컴파일 오류는 세상에서 가장 빠르고, 좋은 오류다
 * '참고' : 수정자 주입을 포함한 나머지 주입 방식은 모두 생성자 이후에 호출되므로, 필드에 final 키워드를 사용할 수 없다.
 * 오직 생성자 주입 방식만 final 키워드를 사용 할 수 있다.
 *
 * '정리'
 * 생성자 주입 방식을 선택하는 이유는 여러가지가 있지만, 프레임워크에 의존하지 않고, 순수한 자바 언어의 특징을 잘 살리는 방법
 * 기본으로 생성자 주입을 사용하고, 필수 값이 아닌 경우에는 수정자 주입 방식을 옵션으로 부여하면 된다. 생성자 주입과 수정자 주입을 동시에 사용할 수 있다.
 * 항상 생성자 주입을 선택해라! 그리고 가끔 옵션이 필요하면 수정자 주입을 선택해라. 필드 주입은 사용하지 않는게 좋다.
 *
 * */