package com.example.core.autowired;

import static org.assertj.core.api.Assertions.*;

import com.example.core.AutoAppConfig;
import com.example.core.discount.DiscountPolicy;
import com.example.core.member.Grade;
import com.example.core.member.Member;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AllBeanTest {


    @Test
    void findAllBean() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(
            AutoAppConfig.class,DiscountService.class);
        DiscountService discountService = ac.getBean(DiscountService.class);
        Member member = new Member(1L, "userA", Grade.VIP);

        int fixDiscountPrice = discountService.discount(member, 10000,"fixDiscountPolicy");

        assertThat(discountService).isInstanceOf(DiscountService.class);
        assertThat(fixDiscountPrice).isEqualTo(1000);


        int rateDiscountPrice = discountService.discount(member, 20000,"fixDiscountPolicy");
        assertThat(rateDiscountPrice).isEqualTo(1000);
    }


    //전략적 선택
    static class DiscountService {
        /**
         * 동적 빈을 선택할때는 map을 이용하자
         * */
        private final Map<String, DiscountPolicy> policyMap;

        /**
        어떤 빈이 주입될지 각 빈들의 이름은 무엇일지 코드만보고 한번에 쉽게 파악할 수가 없다.
        이코드를 다른개발자들이 보면 어떻게 할까 ? 자동등록을 사용하고 있기때문에 여러 코드를 찾아봐야한다.
        이런경우 수동빈으로 등록하거나 자동으로하면 특정 패키지에 같임 묶어 두는게 좋다.
        핵심은 딱보고 이해가 되어야한다는게 중요함!

         @Configuration
         public class DiscountPolicyConfig {
            @Bean
            public DiscountPolicy rateDiscountPolicy {
                return new RateDiscountPolicy;
            }
            @Bean
            public DiscountPolicy fixDiscountPolicy {
                return new FixDiscountPolicy;
            }
         }
         이정보만 봐도 한눈에 빈의 이름은 물론이고 어떤 빈들이 주입될지 파악가능.
         그래도 빈 자동 등록을 사용하고 싶으면 파악하기 좋게 DiscountPolicy의 구현 빈들만 따로 모아서 특정 패키지에 모아두자


         '정리'
         편리한 자동 기능을 기본으로 사용하자
         직접 등록하는 기술 지원 객체는 수동 등록
         다형성을 적극 활요하는 비즈니스 로직은 수동 등록을 고민해보자.
        */
        private final List<DiscountPolicy> policies;

        @Autowired
        public DiscountService(Map<String, DiscountPolicy> policyMap, List<DiscountPolicy> policies) {
            this.policyMap = policyMap;
            this.policies = policies;
            System.out.println("policyMap = " + policyMap);
            System.out.println("policies = " + policies);
        }

        public int discount(Member member, int price, String discountCode) {
            DiscountPolicy discountPolicy = policyMap.get(discountCode);
            return discountPolicy.discount(member,price);
        }
    }
}
