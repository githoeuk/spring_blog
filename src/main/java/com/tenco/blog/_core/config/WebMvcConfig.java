package com.tenco.blog._core.config;

import com.tenco.blog._core.interceptor.LoginInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // IoC - 하나 이상의 IoC 처리를 하고 싶을 때 사용한다.
// 해당 클래스에 다른 어노테이션이 있는지 (EX : bean)을 찾는다.

// 역할
// 자바 코드로 스프링 부트 설정파일을 다룰 수 있다.
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired // DI 처리
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 여기에다가 LoginInterceptor 등록 예정
        System.out.println("인터셉터 동작 중 ");
        registry.addInterceptor(loginInterceptor)
                // loginInterceptor가 동작할 URL 패턴을 명시 해주어야 한다.
                .addPathPatterns("/board/**", "/user/**")
                // 인터셉터에서 제외할 URL 패턴을 지정할 수 있다.
                // /board/{id:\d+} -> /board/7 처럼 정수값이 들어오면 제외 시킨다.
                .excludePathPatterns("/board/{id:\\d+}");
    } // end of addInterceptors
} // end of class
