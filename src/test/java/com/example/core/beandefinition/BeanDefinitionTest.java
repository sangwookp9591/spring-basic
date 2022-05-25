package com.example.core.beandefinition;

import com.example.core.AppConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BeanDefinitionTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
    
    @Test
    @DisplayName("빈 설정 메타정보 확인")
    void findApplicationBean() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);

            if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
                System.out.println("beanDefinitionName = " + beanDefinitionName +", beanDefinition = "+beanDefinition);
            }
            //scope=; 이렇게 할당이 안되있는것은 singleton임
        }
    }
}


//spring container는 Bean Definition에만 의존한다. -> AppConfig.class, 던AppConfig.xml이든 상관안한다.
//추상화에만 의존하도록 설계
//BeanDefinition자체가 interface이기때문에 SpringContainer는 추상화에만 의존한다.

//ApplicationConfigApplicationContext는 AnnotaionDefinitionReader를 통해서
// AppConfig.class를 읽고 BeanDefinition을 생성한다.

//BeanDefinition을 직접 생성해서 스프링 컨테이너에 등록할 수 도 있다. But 실무에서는 실제 정의해서 사용하지 않는다.

//스프링이 다양한 형태의 설정 정보를 BeanDefinition으로 추상화 해서 사용한다.


