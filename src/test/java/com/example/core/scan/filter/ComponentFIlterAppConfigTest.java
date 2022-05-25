package com.example.core.scan.filter;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.context.annotation.ComponentScan.*;

public class ComponentFIlterAppConfigTest {

    @Test
    void fiterScan() {

        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ComponentAppFilterAppConfig.class);

        BeanA beanA = ac.getBean("beanA", BeanA.class);
//        BeanB beanB = ac.getBean("beanB", BeanB.class);
        assertThat(beanA).isNotNull();
        assertThrows(
                NoSuchBeanDefinitionException.class,
                ()-> ac.getBean("beanB", BeanB.class));
    }

    @Configuration
    @ComponentScan(
            includeFilters = @Filter(type = FilterType.ANNOTATION, classes = MyIncludeComponent.class),
            excludeFilters = @Filter(type = FilterType.ANNOTATION, classes = MyExcludeComponent.class)
    )

    /***FiterType 옵션 5가지 (크게 사용을잘안함 @Component면 충분함 -> Spring 이 기본적으로 제공하는 거에 최대한 맞춰서 사용)
     * ANNOTATION: 기본값 annotation을 인식해서 동작한다. -> 생략가능.
     *  ex) `org.example.SomeAnntation`
     * ASSIGNABLE_TYPE :  지정한 타입과 자식 타입을 인식해서 동작한다. -> class 직접 지정
     *  ex) `org.example.SomeClass`
     * ASPECTJ : AspectJ 패턴 사용
     *  ex) `org.eaxmpale..*Service+`
     * REGEX :정규 표현식
     *  ex) `org\.example\.Default.*`
     * CUSTOM: TypeFilter 라는 인터페이스를 구현해서 처리
     *  ex) `org.example.MyTypeFilter`
     * */
    static class ComponentAppFilterAppConfig {

    }
}
