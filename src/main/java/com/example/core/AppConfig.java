package com.example.core;

import com.example.core.discount.DiscountPolicy;
import com.example.core.discount.FixDiscountPolicy;
import com.example.core.discount.RateDiscountPolicy;
import com.example.core.member.MemberRepository;
import com.example.core.member.MemberService;
import com.example.core.member.MemberServiceImpl;
import com.example.core.member.MemoryMemberRepository;
import com.example.core.order.OrderService;
import com.example.core.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//Factory Method를 통해서 개발하는 방법;
@Configuration
public class AppConfig { //FactoryBeanName

    //@Bean memberService -> memberRepository() -> new MemoryMemberRepository()
    //@Bean orderService -> memberRepository() ->new MemoryMemberRepository();
    //이렇게 2번 호출됨. 싱글톤이 깨지는거아님? -> Spring Conatiner가 어떻게 이문제를 해결할까?


    //호출 가정

    //메소드 호출 순서는 보장하지 않지만 예를들어
    //memberService() -> call AppConfig.memberService
    //new MemberServiceImpl(memberRepository()); -> call AppConfig.memberRepository
    //두번째 빈등록시  memberRepository()-> call AppConfig.memberRepository
    //orderService() -> call AppConfig.orderService"
    //call AppConfig.memberRepository

    //호출 결과
    /**
     * memberService -> memberRepository1 = com.example.core.member.MemoryMemberRepository@ec2bf82
     * orderService -> memberRepository2 = com.example.core.member.MemoryMemberRepository@ec2bf82
     * memberRepository = com.example.core.member.MemoryMemberRepository@ec2bf82
     *
     * 3번호출되지 않고 한번만 호출됨. Spring이 어떠한 방법을 써서라도 singleton을 보장해준다.
     * */

    @Bean //FactoryMethodName
    public MemberService memberService() {
        //1
        System.out.println("call AppConfig.memberService");
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        //2
        System.out.println("call AppConfig.memberRepository");
        return new MemoryMemberRepository();
    }

    @Bean
    public OrderService orderService() {
        System.out.println("call AppConfig.orderService");
        return new OrderServiceImpl(
                memberRepository(),
                discountPolicy());
    }



    @Bean
    public DiscountPolicy discountPolicy() {
//        return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }
}

//싱글톤의 탄생 배경
/*
* 웹 어플리케이션은 보통 여러 고객이 동시에 요청을 함
*
* -싱글톤 (객체 인스턴스가 현재나의 java jvm안에 하나만 있어야하는것!)
*
* memberService를 3명이 동시 요청했을때 -> Config는 new MemberService이렇게 반환
* -> 고객에 3번요청하면 객체가 3개생성됨 -> 이게 문제.
* */
