package com.tenco.blog._core.interceptor;

import com.tenco.blog._core.errors.Exception401;
import com.tenco.blog.user.User;
import com.tenco.blog.user.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

// 규칙 - 스펙을 충족시켜야 함 (HandlerInterceptor)
@Component // IoC 대상 - 싱글톤 패턴으로 관리
public class LoginInterceptor implements HandlerInterceptor {

    // 컨트롤러에 들어오기 전에 먼저 동작 방식
    // 리턴에 true가 있으면 --> Controller로 진행
    // 리턴에 false가 있으면 --> Controller로 진입 불가 
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 인증 검사
        HttpSession session = request.getSession();
        UserResponse.SessionDTO sessionUser = (UserResponse.SessionDTO) session.getAttribute("sessionUser");
        if (sessionUser == null){
            throw new Exception401("로그인이 필요한 서비스입니다.");
        }
        return true; // controller로 진행
    } // end of preHandle

//    // 뷰가 렌더링 되기 전에 낚아채는 방식 - 뷰 리졸브가 파일찾을때 막는 방식
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
//    } // end of postHandle
//
//    // 요청 처리가 완료된 후, 즉 뷰가 완전 렌더링이 된 후 호출
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
//    } // end of afterCompletion


} // end of class
