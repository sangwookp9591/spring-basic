package com.example.core.common;

import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value="request", proxyMode = ScopedProxyMode.TARGET_CLASS)
/**proxyMode = ScopedProxyMode.TARGET_CLASS 가짜 프록시를 만듦
 * 적용 대상이 인터페이스가 아니면 TARGET_CLASS
 * 적용 대상이 인터페이스면 INTERFACES
 * - 가짜 프록시 클래스를 만들고 http request와 상관없이 가짜 프록시 클래스를 다른빈에미리 주입함.
 *
 * CGLIB라는 라이브러리러ㅗ 내 클래스를 상속받은 가짜 프록시 객체를 만들어서 주입한다
 * 스프링 컨테이너에 myLogger라는 이름으로 찐짜 대신에 가짜 프록시 객체를 등록한다.
 * 가짜 프록시 객체는 실제 요청이 오면 그때 내부에서 '진짜 빈을 요청하는 위임 로직이 들어있다' 즉 가짜 프록시 객체는 내부에 진짜 myLogger를 찾는 방법을 알고있음
 * logic()을 호출하면 사실 가자 프록시 객체의 메서드를 호출한것이다.
 * 가짜 프록시 객체는 request 스코프의 진짜 mylogger.logic()을 호출
 * 가짜 프록시 객체는 '원본 클래스를 상속 받아서 만들어졋기때문이' 이 객체를 사용하는 클라이언트 입장에서는 사실 원본인지 아닌지 모르게 동일하게 사용(다형성)
 *
 * 동작 정리
 * CGLIB라는 라이브러로 네 클래스를 상속 받은 가짜 프록시 객체 만들어서 주입
 * 가짜 프록시 객체는 실제 요청이 오면 그때 내부에서 실제 빈을 요청하는 위임 로직가지고있음.
 * 가짜 프록시 객체는 실제 request scope와 관계 없음. 가짜이고 내부에 단순한 위임 로직만 있고, 싱글톤 처럼 동작한다.
 *
 * 특징 정리.
 * 프록시 객체 덕분에 클라이언트느느 마치 싱글톤 빈을 사용하듯이 편리하게 request scope 사용 -> private final MyLogger mylogger
 *
 * 중요! .Provider든 proxy든 핵심 아이디어는 진짜 객체를 조회가 꼭 필요한 시점까지 지연처리!!! 필요할때 꺼내씀
 * 단지 annotation 설정 변경만으로 원본 객체를 프록시 객체로 대체할 수 있다. 이것이 바로 다형성과 DI container가 가진가장 큰 강점!!!!
 * - 웹 스코프가 아니여도 프록시는 사용할 수 있음.
 * - client 코드를 고칠 필요가 없다.
 *
 * 주의점
 * 싱글톤같지만 다르게 동작함. 주의해서 사용! -> 예제에서는 요청마다 각각따로 생성되는 거임. request scope
 * 특별한 scope는 꼭 필요한 곳에만 최소화해서 사용하자 무분별하게 사용하면 유지보수가 어렵다
 *
 *
 *
 *


 */

public class MyLogger {

    private String uuid;
    private String requestURL;

    /**requestURL은 client가 요청을 해야 알수있다.
    *즉 빈이 생성되는 시점에는 알 수 없으므로 외부에서 settter로 입력받는다.*/
    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public void log(String message) {
        System.out.println("["+uuid+"]"+"["+requestURL+"] "+ message);
    }

    @PostConstruct
    public void init() {
        uuid = UUID.randomUUID().toString();
        System.out.println("["+uuid+"] request scope bean create : "+this);
        //이 빈은 http요청 당 하나씩 생성되므로, uuid를 저장해두면 다른 http 요청과 구분할 수 있다.
    }

    @PreDestroy //고객요청이 끝날때 호출(반아 소멸되는 시점)
    public void close() {
        System.out.println("["+uuid+"] request scope bean close : "+this);
    }
}
