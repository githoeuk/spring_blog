package com.tenco.blog.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller // IOC 대상
@RequiredArgsConstructor // DI 처리
public class UserController {

    private final UserService userService;

    // 프로필 수정 요청
    @PostMapping("/user/update")
    public String updateProc(UserRequest.UpdateDTO updateDTO, HttpSession session) {
        // 인증 검사 처리 - LoginInterceptor 에서 처리
        // 유효성 검사
        updateDTO.validate();
        // 수정 기능
        User sessionUser = (User) session.getAttribute("sessionUser");
        User userEntity = userService.updateById(sessionUser.getId(), updateDTO);
        // 세션 동기화 처리
        session.setAttribute("sessionUser", userEntity);

        return "redirect:/";
    } // end of updateProc

    // 프로필 화면 요청
    @GetMapping("/user/update-form")
    public String updateFormPage(HttpSession session, Model model) {
        // session -> 로그인 정보가 담겨있음
        User sessionUser = (User) session.getAttribute("sessionUser");
        // 인증 검사
        User userEntity = userService.findById(sessionUser.getId());
        // 모아서 전달
        model.addAttribute("user", userEntity);

        return "/user/update-form";
    } // end of updateFormPage

    // 로그인 화면 요청
    // 주소 설계 : http://localhost:8080/login-form
    @GetMapping("/login-form")
    public String loginFormPage() {
        // 인증 검사 x, 유효성 검사 x
        // 페이지에 user/ 내포하지 않기 때문에 인터셉터가 작동하지 않는다.
        return "user/login-form";
    }

    // 로그인 기능 요청
    @PostMapping("/login")
    public String loginProc(UserRequest.LoginDTO loginDTO,HttpSession session) {
        // 인증 검사 x , 유효성 검사 o

        // 유효성 검사
        loginDTO.validate();
        // 로그인 기능 요청
        User userEntity = userService.login(loginDTO);
        //
        session.setAttribute("sessionUser",userEntity);

        return "redirect:/";
    } // end of loginProc

    // 로그아웃 기능 요청
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 세션 메모리에 내 정보를 삭제시켜버림 -- 무효화 처리 ==  로그아웃
        session.invalidate();
        return "redirect:/";
    }

    // 회원 가입 화면 요청
    // 주소 설계 : http://localhost:8080/join-form
    @GetMapping("join-form")
    public String joinFormPage() {
        return "user/join-form";
    } // end of joinFormPage

    // 회원 가입 기능 요청
    // 주소 설계 : http://localhost:8080/join
    @PostMapping("/join")
    public String joinProc(UserRequest.JoinDTO joinDTO) {

        // 인증검사 x - 유효성 검사 o
        // 유효성 검사 하기 --> 문제 발생 시 -> 예외 처리됨
        joinDTO.validate();

        // 회원가입 기능 요청
        userService.join(joinDTO);

        // 로그인 화면으로 리다이렉션 처리 예정
        return "redirect:/login-form";
    } // end of joinProc

} // end of class
