package com.example.core.singleton;

// 객체 인스턴스를 딱하나만 생성해서 공유하는 SingleTon방식은 주의해야할것이있다.
// 여러 client가 하나의 객체 인스턴스를 공유하기 때문에 싱글톤 객체는 상태를 유지하게 설계하면 안된다.
// 무상태로 설계해야함!
// - 특정 클라이언트에 의존적인 필드가 있으면 안된다.
// - 특정 클라이언트가 값을 변경할 수 있는 필드가 있으면 안된다.
// - 가급적 읽기만 가능해야한다.
// - 필드 대신에 자바에서 공유되지 않는, 지역변수, 파라미터, ThradeLocal등을 사용해야한다.
// 스프링 빈(singleton bean)에 공유값을 설정하면 큰 장애가 발생할 수 있다.

public class StatefulService {
//    private int price; //상태를 유지하는 필드

    public int order(String name, int price) {
        System.out.println("name = " + name + ", price= " + price);
//        this.price = price;//여기가 문제
        return price; //지역변수로 해서 무상태로 넘김
    }



//    public int getPrice(){
//        return price;
//    }


}
