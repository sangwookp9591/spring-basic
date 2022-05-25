package com.example.core.order;

import com.example.core.discount.DiscountPolicy;
import com.example.core.discount.FixDiscountPolicy;
import com.example.core.discount.RateDiscountPolicy;
import com.example.core.member.Member;
import com.example.core.member.MemberRepository;
import com.example.core.member.MemoryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component //스프링 첫번째 라이프사이클에서 스프링빈 등록
public class OrderServiceImpl implements OrderService{
    private final MemberRepository memberRepository; //private final -> 얘는 값이 무조건 있어야함.

    private final DiscountPolicy discountPolicy; //private final -> 얘는 값이 무조건 있어야함.

    //Spring의 두번째 라이프 사이클에서 autowired가 등록된 연관관계 주입
//    //수정자 주입 -< filied에 final이 없어야함.
    /** 선택, 변경 가능성이 있는 의존관계에 사용
     자바빈 프로퍼티 규약의 수정자 메서드 방식을 사용하는 방법
     **/
//    @Autowired
//    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
//        this.discountPolicy = discountPolicy;
//    }
//
//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

        @Autowired// 생성자 주입.
    /**생성자주입은 생성자 호출시점에 딱 1번만 호출되는것이 보장이됨. '불변, 필수'의존관계에 사용(항상은아지만 주로!)
     * 생성자 주입은 class가 Bean에 등록될때 어쩔수없이 생성자를 불러야하기 때문에 의존관계 주입도 같이 일어난다.
    *중요! -> 생성자가 딱 1개만 있으면 @Autowired를 생략해도 자동 주입 된다.
    *tmi -좋은 개발 습관은 한계점이랑 제약이 있어야함! 즉 여기서는 생략되어 있어도 상관없다.
     *
     *
     * */
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy
            discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }


    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);
        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    //test용
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }

}

