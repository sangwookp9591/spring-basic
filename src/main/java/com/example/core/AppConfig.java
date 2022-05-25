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
    @Bean //FactoryMethodName
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(
                memberRepository(),
                discountPolicy());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
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
