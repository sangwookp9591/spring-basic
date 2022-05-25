package com.example.core.singleton;

import com.example.core.AppConfig;
import com.example.core.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

public class SingleTonTest {

    @Test
    @DisplayName("스프링 없는 순수한 DI 컨테이너")
    void pureContainer() {
        AppConfig appConfig = new AppConfig();

        //1.조회 : 호출할때마다 객체를 생성
        MemberService memberService1 = appConfig.memberService();
        //2.조회 : 호출할때마다 객체를 생성
        MemberService memberService2 = appConfig.memberService();

        //참고 값이 다른 것을 확인
        System.out.println("memberService1 = " + memberService1);
        System.out.println("memberService2 = " + memberService2);
        /*
        * memberService1 = com.example.core.member.MemberServiceImpl@7e5c856f
            memberService2 = com.example.core.member.MemberServiceImpl@74bf1791
        * */
        /*
        * 결과 다른 참조 객체가 생성 -> 이렇게 되면 JVM 메모리에 계속 객체가 생성되어 올라간다.
        * */

        //검증
        assertThat(memberService1).isNotEqualTo(memberService2);

        //결국 총객체가 4개가 생성된 것이다.
        //MemoryMemberRepository 생성 -> memberService 생성  x 2

        // 스프링없는 순수한 Di 컨테이너인 Appconfig는 요청을 할때마다 객체를 새로 생성
        // 고객 트래픽이 초당 100이 나오면 100갸가 생성되고 소멸된다 -> 메모리 낭비가 심하다.
        // 해결방안은 객체가 딴 한번 생성되고 공유하도록 설계 -> 싱글톤 패턴

    }

    @Test
    @DisplayName("싱글톤 패턴을 적용한 객체 사용.")
    void singleTonServiceTest() {
        SingleTonService instance1 = SingleTonService.getInstance();
        SingleTonService instance2 = SingleTonService.getInstance();

        System.out.println("instance1 = " + instance1);
        System.out.println("instance2 = " + instance2);
        /**
         * instance1 = com.example.core.singleton.SingleTonService@35432107
         * instance2 = com.example.core.singleton.SingleTonService@35432107
         * */

        //isSameAs -> == 객체 비교랑 같다. (인스턴스 비교)
        //isEqualsTo java Equals 비교
        assertThat(instance1).isSameAs(instance2);

        // 근데 다 private static final로 해줘야할까?
        // 그렇지 않다!!
        // 스프링 컨테이너를 쓰면 Spring Container가 객체를 싱글톤으로 만들어서 관리해준다.
        // 있는 객체를 재활용하기 때문에 성능이 좋아진다.

        // 싱글톤 패턴을 적용하면 고객의 요청이 올때 마다. 객체를 생성하는 것이 아니라, 이미 만들어진 객체를 공유해서 효율적으로 사용
        // 싱글톤 패턴은 많은 문제점을 가지고 있음.
        // - 구현하는 코드 자체가 많이 들어감
        // - 의존관계상 클라이언트가 구체 클래스에 의존 (구체클래스.getInstance SingleTonService.getInstance를 하기때문!) -> DIP를 위반
        // - 클라이언트가 구체 클래스에 의존해서 OCP 원칙을 위반할 가능성이 높음
        // - 테스트하기 어려움(싱글톤은 딱 지정해서 가지고 옴, 싱글톤은 인스턴스를 미리 받아서 설정 이 끝나버림 유연하게테스트하기어려움)
        // - 내부 속성을 변경하거나 초기화 하기 어렵다
        // - private 생성자 사용 -> 자식 클래스를 만들기 어렵다.
        // - 결론적으로 유연성이 떨어짐
        // - 안티패턴으로 불리기도 한다.

        // 확실한 객체 하나가 있다는게 보장이되지만 여러가지 단점이있음
        // Spring Conatiner는  Singleton의 이러한 단점을 제거해준다!.
        // 즉 spring bean이 SingleTon으로 관리되는 Bean
        // 이전을 보면 스프링컨테이너가 빈객체를 하나씩 미리 등록헤놓고 관리한다(객체 인스턴스를 미리 생성하여 관리) 조회하면 관리되는 것을 조회
        // Spring container는 singletone container역할을 한다. 이렇게 싱글톤 객체를 생성하고 관리하는 기능을 싱글톤 레지스트리라고 한다.
        // 싱글톤 컨테이너는 싱글톤 패턴을 위한 지저분한 코드가 들어가지 않아도된다.
        // DIP, OCP, 테스트, private  생성자로 부터 자유롭게 싱글톤을 할 수 있다.

    }

    @Test
    @DisplayName("스프링 컨테이너와 싱글톤")
    void springContainer() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        //1.조회 : 조회할때마다 같은 객체 반환
        MemberService memberService1 = ac.getBean("memberService", MemberService.class);
        //2.조회 : 조회할때마다 같은 객체 반환
        MemberService memberService2 = ac.getBean("memberService", MemberService.class);

        //참고 값이 같은 것을 확인
        System.out.println("memberService1 = " + memberService1);
        System.out.println("memberService2 = " + memberService2);
        /*
            memberService1 = com.example.core.member.MemberServiceImpl@3f2049b6
            memberService2 = com.example.core.member.MemberServiceImpl@3f2049b6
        * */
        /*
         * Spring container가 빈등록한것을 계속 반환해주는 것이다.
         * 실제 Springton과 관련된 코드는 들어가 있지 않다.
         *
         * */

        //검증
        assertThat(memberService1).isSameAs(memberService2);
        // 스프링의 기본 빈 등록 방식은 싱글톤이지만, 싱글톤 방식만 지원하는 것은 아니다.
        // 요청할때마다 새로운 객체를 생성해서 반환하는 기능도 제공한다 . -> Bean Scope
        // 99% SingleTon Bean;


        // 객체 인스턴스를 딱하나만 생성해서 공유하는 SingleTon방식은 주의해야할것이있다.
        // 여러 client가 하나의 객체 인스턴스를 공유하기 때문에 싱글톤 객체는 상태를 유지하게 설계하면 안된다.
        // 무상태로 설계해야함!
        // - 특정 클라이언트에 의존적인 필드가 있으면 안된다.
        // - 특정 클라이언트가 값을 변경할 수 있는 필드가 있으면 안된다.
        // - 가급적 읽기만 가능해야한다.
        // - 필드 대신에 자바에서 공유되지 않는, 지역변수, 파라미터, ThradeLocal등을 사용해야한다.
        // 스프링 빈(singleton bean)에 공유값을 설정하면 큰 장애가 발생할 수 있다.


    }
}
