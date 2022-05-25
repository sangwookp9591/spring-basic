package com.example.core.singleton;

import com.example.core.AppConfig;
import com.example.core.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

    }
}
