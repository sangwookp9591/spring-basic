package com.example.core.order;

import com.example.core.annotation.MainDiscountPolicy;
import com.example.core.discount.DiscountPolicy;
import com.example.core.discount.FixDiscountPolicy;
import com.example.core.discount.RateDiscountPolicy;
import com.example.core.member.Member;
import com.example.core.member.MemberRepository;
import com.example.core.member.MemoryMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component //스프링 첫번째 라이프사이클에서 스프링빈 등록
//@RequiredArgsConstructor
/**
 * final이 붙으면 필수값이기때문에
 * final이 붙은 필드를 찾아 하위의 생정자를 만들어준다.
 *     public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy
 *             discountPolicy) {
 *         this.memberRepository = memberRepository;
 *         this.discountPolicy = discountPolicy;
 *     }
 *
 *     */
public class OrderServiceImpl implements OrderService{

    private final MemberRepository memberRepository; //private final -> 얘는 값이 무조건 있어야함.

    private final DiscountPolicy discountPolicy; //private final -> 얘는 값이 무조건 있어야함.

    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository,/*@Qualifier("mainDiscountPolicy")*/ @MainDiscountPolicy DiscountPolicy
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

//Spring의 두번째 라이프 사이클에서 autowired가 등록된 연관관계 주입

//      1.  @Autowired// 생성자 주입.
/**생성자주입은 생성자 호출시점에 딱 1번만 호출되는것이 보장이됨. '불변, 필수'의존관계에 사용(항상은아지만 주로!)
 * 생성자 주입은 class가 Bean에 등록될때 어쩔수없이 생성자를 불러야하기 때문에 의존관계 주입도 같이 일어난다.
 *중요! -> 생성자가 딱 1개만 있으면 @Autowired를 생략해도 자동 주입 된다.
 *tmi -좋은 개발 습관은 한계점이랑 제약이 있어야함! 즉 여기서는 생략되어 있어도 상관없다.
 *
 *
 * */
//    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy
//            discountPolicy) {
//        this.memberRepository = memberRepository;
//        this.discountPolicy = discountPolicy;
//    }


//2. 필드주입
//필드에다가 바로 의존관계 매핑
/**특징 코드가 간결해서 많은 개발자들을 유혹하지만 외부에서 변경이 불가능해서 테스트하기 힘들다는 치명적인 단점 -> memory를 jdbc못바꿈 ...
 *
 * 순수한 자바코드로 돌린다고 했을때
 * @Test
 * void fileInjectionTest() {
 *     OrderServiceImpl orderServiceImpl =new OrderServiceImpl();이렇게 생성할경우
 *     orderServiceImpl.createOrder()를 하면 memberRepository와 discountRepository에서 nullpointException 발생
 * }
 * 해결할려고하면 memberRepository와 discountRepository의 setter를 만들어서 해결해야함.
 *DI 프레임워크가 없으면 아무것도 할수 없다.
 * * 사용하지말자!
 * 사용해도되는 곳
 * - application의 실제 코드와 관계 없는 테스트 코드(@SpringBootTest) ->
 * - 스프링 설정을 목적으로하는 @Configuration같은 곳에서만 특별한 용도로 사용
 **/
//    @Autowired
//    private MemberRepository memberRepository;
//    @Autowired
//    private DiscountPolicy discountPolicy;

//3. 수정자 주입 -< filied에 final이 없어야함.
/** 선택, 변경 가능성이 있는 의존관계에 사용
 자바빈 프로퍼티 규약의 수정자 메서드 방식을 사용하는 방법
 필듸 값을 변경할때는 set~ 가져올때는 get~
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

//4. 일반메서드 주입
/**
 * 일반 메서드를 통해서 주입 받을수있다.
 * 특징
 *  - 한번에 여러 필드를 주입받을 수 있다.
 *  - 일반적으로 잘사용하지 않는다
 *  .참고 : 당연한 이야기이지만 의존관계 자동주입은 스프링 컨테이너가 관리하는 스프링 빈이여야 동작한다.
 *  스프링 빈이아닌 Member같은 클래스에서 @Autowired 코드를 적용해도 아무 기능도 동작하지 않는다.
 * **/
//private MemberRepository memberRepository;
//private DiscountPolicy discountPolicy;
//public void init(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
//    this.memberRepository = memberRepository;
//    this.discountPolicy = discountPolicy;
//}