package com.example.core.web;

import com.example.core.common.MyLogger;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class LogDemoController {

    private final LogDemoService logDemoService;
//    private final ObjectProvider<MyLogger> myLoggerProvider;
    private final MyLogger myLogger;

    @RequestMapping("log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
//        MyLogger mylogger = myLoggerProvider.getObject(); //이부분에서 spring bean에 등록
        myLogger.setRequestURL(requestURI);

        System.out.println("myLogger = " + myLogger.getClass()); //진짜 Mylogger가 아니라 껍대기를 넣어놓는것
        //myLogger = class com.example.core.common.MyLogger$$EnhancerBySpringCGLIB$$f611f7ad
        //기능을 실제 호출하는 시점에 진짜를 찾아서 provider가 동작했던것 처럼 동작

        myLogger.log("controller test");
        logDemoService.logic("testId");
        return "OK";
    }
}


