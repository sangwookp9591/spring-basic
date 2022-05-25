package com.example.core.singleton;

import com.example.core.AppConfig;
import com.example.core.member.MemberRepository;
import com.example.core.member.MemberService;
import com.example.core.member.MemberServiceImpl;
import com.example.core.order.OrderServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

public class ConfigurationSingletonTest {

    @Test
    void configurationTest(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        MemberServiceImpl memberService = ac.getBean("memberService", MemberServiceImpl.class);
        OrderServiceImpl orderService = ac.getBean("orderService", OrderServiceImpl.class);
        //impl로 한 이유는 그냥 getMemberRepository할라고 한거
        //원래는 구체 타입으로 꺼내면 좋지 않다 추상 으로꺼내야함.

        //진짜 memberRepsoitory
        MemberRepository memberRepository = ac.getBean("memberRepository",MemberRepository.class);
        

        MemberRepository memberRepository1 = memberService.getMemberRepository();
        MemberRepository memberRepository2 = orderService.getMemberRepository();
        System.out.println("memberService -> memberRepository1 = " + memberRepository1);
        System.out.println("orderService -> memberRepository2 = " + memberRepository2);
        System.out.println("memberRepository = " + memberRepository);

        Assertions.assertThat(memberRepository1).isSameAs(memberRepository2).isSameAs(memberRepository); //같음..

        //3개다 같다.???
        //로직이 3번 실행되었어야 할거같은데 결과는 같은 인스턴스가 실행됨.
        //모두 같은 인스턴스가 공유되어 사용됨.
        //logic은 세번인데 어떻게 된걸까???

    }
    
    @Test
    void configurationDeep(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        AppConfig bean = ac.getBean( AppConfig.class);
        System.out.println("bean.getClass() = " + bean.getClass());
        //결과 -> class com.example.core.AppConfig$$EnhancerBySpringCGLIB$$cd77e4

        //원래 순수한class라면 com.example.core.AppConfig까지만 나와야함.
        //EnhancerBySpringCGLIB -> 얘는 뭔가? 내가만든 클래스가아니다.
        // spring이 빈을 등록하는 과정에서 CGLIB라는 바이트 코드를 조작하는 라이브러리를 통해서
        //AppConfig 클래스를 상속받은 임의의 다른 클래스를 만들고, 그 다른 클래스를 스프링 빈으로 등록한 것이다.

        //AppConfig <- AppConfig@CGLIB
        //AppConfig가 CGLIB 조작라이브러리를 가지고 상속받아 다른 클래스를 만듦
        //이름은 AppConfig지만 instance 객체가 AppConfig@CGLIB 이렇게 등록된다. ->이래서 AppConfig로 호출된거임.
        //임의의 다른 클래스가 싱글톤이 되더록 보장해준다.
        //이미 스프링 컨테이너에 등록되어 있으면 스프링 컨테이너 에서 찾아서 반환 -> 인스턴스를 생성하고 스프링 컨테이너에 등록

        //@Bean이 붙은 메서드 마다 이미 스프링 빈이 존재하면 존재하는 빈을 반환하고, 스프링 빈이 없으면 생성해서 스프링 빈으로 등록하고 반환하는 코드가 동적으로 만들어진다.
        //이 덕분에 싱글톤이 보장되는 것이다.
        //그래서 출력결과가 3번 출력된게아니라 한번만 호출된 것이다.

        //@Configuration이 없어도 @Bean을 사용해서 bean으로는 등록은 가능하지만.
        //@Configuration을 붙이면 바이트코드를 조작하는 CGLIB 기술을 사용해서 싱글톤을 보장하지만 안하면 싱글톤은 보장하지 않는다.
        // 클래스가 pure appConfig로 보임 -> com.example.core.AppConfig
        // 출력이 MemberRepository가 3번호출된다 -> singleton이 깨지고 순수한 자바코드가 도는 것이다.

        //즉 Spring 설정정보는 항상 @Configuration을 사용하자!!






    }
}
