package com.example.core.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//구현체에 명시
@Component// 1 ComponentScan이 읽어서 자동으로 Spring bean 등록 ->기본 Bean 이름은 클래스명은 사용하되 맨앞글자만 소문자를 사용!
//직접 지정은 @Component("memberServiceImpl2")
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Autowired //2. 자동 의존관계 주입 -> MemberRepository 타입에 맞는 bean을 주입해줌.
    //조금은 다르지만 비슷한것은 ac.getBean(MemberRepository.class); 자동으로 이 코드가 들어간다.
    //Spring container에 있는 MemberRepository type과(타입이 같은 빈을 찾아 주입) 같은게 있는지 뒤진다. 현재 등록되어있는게 memberyMemberRepository이기때문에
    //bean에 memoryMemberRepository가 등록됨.
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }

    //테스트 용도
    public MemberRepository getMemberRepository(){
        return memberRepository;
    }
}
