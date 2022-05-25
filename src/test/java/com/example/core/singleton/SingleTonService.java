package com.example.core.singleton;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/*
* 정리
* 1. static 영역에 객체 instance를 미리 하나 올려둔다.
* 2. 필요하면 오직 getInstance를 통해서만 조회가능.
* 3. 딱 1개의 객체 인스턴스만 존재해야 하므로,
*  생성자를 private으로 막아서 혹시라도 외부에서 new 키워드로 객체 인스턴스가 생성되는 것을 막는다.
* */
public class SingleTonService {
    //싱글 톤 패턴 -> 클래스의 인스턴스가 딱 1개만 생성되는 것을 보장하는 디자인 패턴
    //하는 방법 -> 똑같은 객체 인스턴스를 2개이상 생성하지 못하게 막아야함 ,
    // -> private 생성자를 사용해서 외부에서 임의로 new 키워드를 사용하지 못하도록 막아야 한다.

    private static final SingleTonService instance = new SingleTonService();
    //자기자신을 내부의 private으로 가지고 있는 statice으로 가지고 있다.
    //이렇게하면 class 레벨에 존재하기 때문에 딱하나만 존재


    //JVM이 뜰때 SingleTonService가 static으로 자기 자신을 생성하고 있내?
    // 내부적으로 실행을 하여 자기자신을 생성해서 instance의 참조를 넣어놓는다.
    //statice area에 데이터 저장하여 프로그램이 시작할때 생성해서 종료할때까지 들고 있는다.
    //조회할때그냥 getInstance를 사용하면 된다.
    public static SingleTonService getInstance() {
        return instance;
    }

    //그리고 private 생성자를 통해 외부로 부터의 생성을 막는다.
    //외부에서는 getInstance를 통해서만 접근
    private SingleTonService() {
    }

    public void logic() {
        System.out.println("싱글톤 객체 로직 호출");
    }

}
