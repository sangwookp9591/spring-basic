package com.example.core.scan.filter;

import java.lang.annotation.*;

@Target(ElementType.TYPE) //TYPE이라고 해놓으면 클래스에 붙는다.
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyExcludeComponent {
}
