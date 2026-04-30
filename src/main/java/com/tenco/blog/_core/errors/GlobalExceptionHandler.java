package com.tenco.blog._core.errors;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// 모든 컨트롤러에서 발생하는 예외를 이 클래스에서 처리하겟다.
// RuntimeException이 발생되면 이 파일로 예외처리가 오게됨

//private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
@Slf4j
@ControllerAdvice // IoC 제어의 역전
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception400.class) // err400발생 시 여기로 잡아옴
    public String ex400(Exception400 e, HttpServletRequest request) {
        log.warn("=== 400 Bad Request 에러 발생 ===");
        log.warn("요청 URL : {}", request.getRequestURL());
        log.warn("에러 메세지 : {} ", e.getMessage());

        request.setAttribute("msg",e.getMessage());
        return "err/400";
    } // end of ex400

    @ExceptionHandler(Exception401.class) // err401발생 시 여기로 잡아옴
    public String ex401(Exception401 e, HttpServletRequest request) {
        log.warn("=== 401Unauthorized 에러 발생 ===");
        log.warn("요청 URL : {}", request.getRequestURL());
        log.warn("에러 메세지 : {} ", e.getMessage());

        request.setAttribute("msg",e.getMessage());
        return "err/401";
    } // end of ex401

    @ExceptionHandler(Exception403.class) // err403발생 시 여기로 잡아옴
    public String ex403(Exception403 e, HttpServletRequest request) {
        log.warn("=== 403 Forbidden 에러 발생 ===");
        log.warn("요청 URL : {}", request.getRequestURL());
        log.warn("에러 메세지 : {} ", e.getMessage());

        request.setAttribute("msg",e.getMessage());
        return "err/403";
    } // end of ex402

    @ExceptionHandler(Exception404.class) // err404발생 시 여기로 잡아옴
    public String ex404(Exception404 e, HttpServletRequest request) {
        log.warn("=== 404 Not Found 에러 발생 ===");
        log.warn("요청 URL : {}", request.getRequestURL());
        log.warn("에러 메세지 : {} ", e.getMessage());

        request.setAttribute("msg",e.getMessage());
        return "err/404";
    } // end of ex403

    @ExceptionHandler(Exception500.class) // err401발생 시 여기로 잡아옴
    public String ex500(Exception500 e, HttpServletRequest request) {
        log.warn("=== 500 Internal Server Error 에러 발생 ===");
        log.warn("요청 URL : {}", request.getRequestURL());
        log.warn("에러 메세지 : {} ", e.getMessage());

        request.setAttribute("msg",e.getMessage());
        return "err/500";
    } // end of ex404

    //기타 모든 RuntimeException처리 - 최후 보류
    @ExceptionHandler(RuntimeException.class) // err401발생 시 여기로 잡아옴
    public String handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        log.warn("=== 예상치 못한 런타임 에러 발생 ===");
        log.warn("요청 URL : {}", request.getRequestURL());
        log.warn("에러 메세지 : {} ", e.getMessage());

        request.setAttribute("msg","시스템 오류가 발생했습니다. 관리자에게 문의해주세요");
        return "err/500";
    } // end of ex500

} // end of class
