package com.example.core.lifecycle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class NetworkClient /** implements InitializingBean, DisposableBean */{
    private String url;

    public NetworkClient() {
        System.out.println("생성자 호출 url = " + url);

    }

    public void setUrl(String url) {
        this.url = url;
    }

    //서비스 시작시호출
    public void connect() {
        System.out.println("connect :  = " + url);
    }

    public void call(String message) {
        System.out.println("call :  = " + url + " message = "+message);
    }

    //서비스 종료시 호출
    public void disconnect() {
        System.out.println("close : " + url);
    }

    /**
     * @PostConstruct ,@PreDestroy annotation 특징
     * 최신 스프링에서 가장 권장하는 방법이다.
     * 애노테이션 하나만 붙이면  되므로 매우 편리하다.
     * 패키지를 잘보면 javax.annotaion.PostConstruct이다. 스프링에 종속적인 기술이 아니라. JSR-250라는 자바 표준이다.
     *
     * 즉! ->Spring이 아닌 다른 컨테이너에서도 동작한다.
     * 컴포넌트 스캔과 잘 어울린다.
     *
     * 유일한단점
     * - 외부라이브러리에는 적용하지 못함. 외부라이브러리를 초기화,종료 해야한다면 @Bean의 기능을 사용.
     * */

    @PostConstruct
    public void init() throws Exception {
        System.out.println("NetworkClient.init");
        connect();
        call("초기화 연결 메시지");
    }

    @PreDestroy
    public void close() throws Exception {
        System.out.println("NetworkClient.close");
        disconnect();

    }



    /**
     * 설정 정보 사용 특징
     * 장점
     *  - method이름을 자유롭게 줄수있다.
     *  - 스프링빈이 스프링 코드에 의존하지 않는다.
     *  - 코드가 아니라 설정 정보를 사용하기 때문에 코드를 고칠 수없는 외부 라이브러리에도 초기화, 종료 메서드를 적용할 수 있다.
     * */
//    public void init() throws Exception {
//        System.out.println("NetworkClient.init");
//        connect();
//        call("초기화 연결 메시지");
//    }
//
//    public void close() throws Exception {
//        System.out.println("NetworkClient.close");
//        disconnect();
//
//    }

    /**
     * 초기화 소멸 인터페이스의 단점
     * - 이 인터페이스는 스프링 전용 인터페이스이다. 해당 코드가 스프링 전용 인터페이스에 의존적으로 설계해야한다.
     * 초기화 소멸 메서드의 이름을 변경할 수 없다.
     * 내가 코드를 고칠 수 없는 외부 라이브러리에 적용할 수 없다.
     * (라이브러리 자체가 컴파일된것을 받아서 수정이 불가능함. -> 얘의 초기화랑 종료 메서드를 호출해야한다? 그럼 코드를 고치는 방법이 없음)
     *
     * 참고! 인터페이스를 사용하는 초기화, 종료 방법은 스프링 초창기에 나온 방법들이고, 지금은 다음의 더 나은 방법들이 있어서 거의 사용되지 않는다.
     * */
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        /**spring의 의존관게가 다끝나고 호출
//         * 초기화 되고 넣을 준비가 되면 호출
//         * */
//
//        System.out.println("NetworkClient.afterPropertiesSet");
//        connect();
//        call("초기화 연결 메시지");
//    }
//
//    @Override
//    public void destroy() throws Exception {
//        System.out.println("NetworkClient.destroy");
//        /**Bean이 종료될때 호출*/
//        disconnect();
//
//    }
}


/**Spring은 다음과 같은 다양한 스코프를 지원한다.
 * -> 스프링 빈이 스프링 컨테이너 시작 ~ 끝가지 유지 그이유는 -> 싱글톤 스코프이기때문이다.
 * 싱글톤 : 기본스코프, 스프링 컨테이너의 시작과 종료까지 유지되는 가장 넓은 범위의 스코프이다.
 *  - 1. 싱글톤 스코프의 빈을 스프링 컨테이너에 요청
 *  - 2. 스프링 컨테이너는 본인이 관리하는스프링 빈을 반환
 *  - 3. 이후에 스프링 컨테이너에 같은 요청이 와도 같은 객체 인스턴스의 스프링 빈을 반환.
 * 프로토타입 : 스프링 컨테이너는 프로토타입 빈의 생성과 의존관계 주입까지만 관여하고 더는 관리하지 않는 매우 짧은 범위의 스코프
 *  - 싱글톤은 빈을 조회하면 스프링 컨테이너가 항상 같은 인스턴스의 스프링 빈을반환 , 프로토타입은 스프링컨테이너에서 조회하면 항상 새로운 인스턴스를 반환
 *  - 1. 프로토타입 스코프의 빈을 스프링 컨테이너에 요청
 *  - 2. 스프링 컨테이너는 이 시점에 프로토타입 빈을 생성하고, 필요한 의존관계를 주입.
 *  핵심! - 스프링 컨테이너는 프로토타입 빈을 생성하고, 의존관계에 주입, 초기화까지만 처리 (관리는 안함) -> 관리의 책임은 client에 있음
 *  그래서 @PreDestory같은 메서드가 호출되지 않는다.
 * 웹관련 스코프
 *  - request : 웹 요청이 들어오고 날갈때 까지 유지되는 스코프.
 *  - session: 웹 세션이 생성되고 종료될 때 까지 유지되는 스코프.
 *  - application : 웹의 서블릿 컨텍스와 같은 범위로 유지되는 스코프
 * */