package com.example.core;

import com.example.core.member.MemberRepository;
import com.example.core.member.MemoryMemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/***
 * 스프링 빈이 늘어나면 일일이 등록하기 귀찮고 설정정보도 커지며 누락되는 현상이 발생 -> 반복문제 발생!
 * 스프링은 설정 정보가 없어도 자동으로 스프링 빈을 등록하는 컴포넌트 스캔이라는 기능을 제공
 * 또 , 의존관계도 자동으로 주입하는 '@Autowired'라는 기능을 제공
 *
 *
 * */
@Configuration
//설정 정보이기 때문에
@ComponentScan(
        basePackages = "com.example.core", //이 패키지 하위로 찾음
//        basePackageClasses = AutoAppConfig.class, //여기서 부터 찾음
//        아무것도 지정안하면 package com.example.core; 포함해서 하위부터 찾음
        /**권장하는 방법
         * - 패키지 위치를 지정하지않고, 설정 정보 클래스의 위치를 프로젝트 최상단에 두는 것 , 스프링부트도 이방법을 기본으로 제공*/
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
) //fiter는 등록시 뺄걸 지정 -> 현재는 test를 위해 AppConfig는 빼야함. 충돌방지를 위해
/**
 * 스프링빈을 긁어서 자동으로 끌어올려야하는데 사용
 * @Component 가 붙은 class를 찾아서 자동으로 Spring bean 으로 등록
 * 기존과는 다르게 @Bean으로 등록한것이 아무것도 없다.
 * */
public class AutoAppConfig {

    //AppConfig에는 의존관계를 주입해주기 위한 method들이 있었다 근데 여기는 없다?
    //그래서 해줘야 하는 것이 @Copmonent를 붙여준 구현체(Spring bean등록된)에서 자동 의존관계 주입을 해준다.


    //자동등록 vs 수동등록

    //수동등록 승! 수동등록이 우선권을 가진다
    // Log
    // Overriding bean definition for bean 'memoryMemberRepository' with a different definition: replacing [Generic bean: class [com.example.core.member.MemoryMemberRepository];
    // 하지만 이런 상황이 발생되면 여러 설정이 꼬여서 잡기 어려운 버그가 만들어진다.
    // 그래서 최근 스프링부트에서는 수동 빈 등록과 자동 빈 등록이 충돌나면 오류가 발생하도록 기본값을 바꾸었다.
    @Bean(name ="memoryMemberRepository")//수동등록
    MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }
}
