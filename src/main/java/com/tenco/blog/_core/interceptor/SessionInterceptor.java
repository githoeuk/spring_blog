package com.tenco.blog._core.interceptor;

import com.tenco.blog.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


@Component // IoC 처리
public class SessionInterceptor implements HandlerInterceptor {

    // 컨트롤러 로직이 거의 끝나는 시점
    // - 즉 화면이 그려지기 직건(mustache전 SessionUser 값을 주입할 예정

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
        System.out.println("SessionInterceptor 인터셉터 동작 확인");

        // 1. 화면을 (View) 반환하는 요청인지 먼저 확인한다. /** <-- 모든 URL 요청 시 동작
        // alert창을 띄우는 것도 있기떄문
        // 데이터만(Json) 반환하는 요청일 경우 modelAndView값이 없으므로 건너뛴다.
        if (modelAndView != null){
            // 화면을 반환하는 동작
            // was서버를 통해 request가 생성되기때문에 request를 통해 session을 가져올 수 있다.
            // request.getSession(true);
            // request.getSession(false); 
            // request.getSession(); <--- 기본값은 true이다.
            // 만약 A라는 사용자가 우리 서버에 최초 요청일 경우 스프링이 자동으로 세션을 만드는 동작을하게 된다

            HttpSession session = request.getSession(false);
            // request.getSession(); - false로 설정하는 이유
            // 성능때문에 매번 세션 매모리를 생성하는 일을 방지
            if(session != null){
                User sessionUser = (User) session.getAttribute("sessionUser");
                if (sessionUser != null){
                    modelAndView.addObject("sessionUser",sessionUser);
                }
            }


        } // end of if
    } // end of postHandle


} // end of class
