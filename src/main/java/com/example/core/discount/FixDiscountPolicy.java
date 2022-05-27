package com.example.core.discount;

import com.example.core.member.Grade;
import com.example.core.member.Member;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * 조회 대상 빈이 2개 이상일 때 해결 방법
 *
 * @Autowired 필드명, @Qualifier , @Primary
 *
 * 1. @Autowired 필드명 매칭 -> Autowired는 처음에 type매칭 시도 -> bean 2개 발견 -> 필드이름이나 파라미터 이름으로 Bean이름을 추가 매핑
 *  - 1. 타입 매칭
 *  - 2. 타입 매칭 결고가가 2개 이상일때는, 필드 명 , 파리머터 명으로 빈 이름 매칭
 *
 * 2. @Qualifier -> @Qualifier 끼리 매칭 -> 빈 이름 매칭
 *  - 1. 추가 구분자를 붙여주는 방법 -> 주입시 추가적인 방법을 제공하는 것이지 빈 이름을 변경하는 것은 아님.
 *  -  @Qualifier로 주입할때 Qualifier("mainDiscountPolicy")를 못 찾으면 어떻게 될까?
 *     그러면 mainDiscountPolicy라는 이름의 스프링빈을 추가로 찾는다.
 *  - 하지만 경험상 Qualifier는 Qualifier를 찾는 용도로만 사용하는게 명확하고 좋다.
 *
 *  - 1. @Qualifier끼리 매칭
 *    2. 찾는게없으면 Bean 이름 매칭
 *    3. NoSuchBeanBefinitonException 발생
 *
 *
 * 3. @Primary 사용 (자주 사용)
 *  - 타입이 여러개가 있을때 primary가 붙은 것을 우선 순위로 사용.
 *  - 좋은이유 :  @Qualifier처럼 지저분하지 않음.
 *  - 사용 시기 : main db ,sub db가 있을 때 main은 primary로 사용 sub는 잘안쓰니깐 @Qulifier사용.
 *
 *  - 우선순위
 * @Primary는 기본값 처럼 동작, @Qulifier는 매우 상세하게동작, 이런 경우 어떤 것이 우선권을 가져갈까?
 * 스프링은 자동보다는 수동이, 넓은 범위의 선택권 보다는 좁은 범위의 선택권이 우선순위가 넓다.
 * 즉 @Qulifier가 우선순위가 높다.
 *
 * */
@Component
@Qualifier("fixDiscountPolicy")
public class FixDiscountPolicy implements DiscountPolicy{

    private int discountFixAmount = 1000; //1000원 할인
    @Override
    public int discount(Member member, int price) {
        if (member.getGrade() == Grade.VIP) {
            return discountFixAmount;
        } else {
            return 0;
        } }
}
