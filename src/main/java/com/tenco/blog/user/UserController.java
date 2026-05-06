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

//
//    // 프로필 기능 요청
//    @PostMapping("/user/update")
//    public String updateProc(UserRequest.UpdateDTO updateDTO, HttpSession session){
//        User sessionUser = (User) session.getAttribute("sessionUser");
//
//        try {
//            // 유효성 검사
//            updateDTO.validate();
//            // 영속성 컨텍스트
//
//            // 더티 체킹 전략
//            User userEntity = userRepository.updateById(sessionUser.getId(),updateDTO);
//
//            // 세션 동기화 처리 - 비밀번호가 변경되고 나면 세션이 달라진다
//            // 반드시 세션 동기화가 필요하다.
//            session.setAttribute("sessionUser",userEntity);
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        return "redirect:/";
//    }
//
//    // 프로필 화면 요청
//    @GetMapping("/user/update-form")
//    public String updateFormPage(HttpSession session, Model model) {
//
//        // 인증 검사
//        User sessionUser = (User) session.getAttribute("sessionUser");
//
//        User userEntity = userRepository.findById(sessionUser.getId());
//        userEntity.setPassword("");
//
//        // 가방(model)에 데이터 담아 화면에 값 전달
//        model.addAttribute("user", userEntity);
//
//        return "/user/update-form";
//    } // end of updateFormPage
//
//    // 로그인 화면 요청
//    // 주소 설계 : http://localhost:8080/login-form
//    @GetMapping("/login-form")
//    public String loginFormPage() {
//        return "user/login-form";
//    }
//
//    // 로그인 기능 요청
//    @PostMapping("/login")
//    public String loginProc(UserRequest.loginDTO loginDTO) {
//        // 유효성 검사
//        loginDTO.validate();
//
//        User sessionUser = userRepository
//                .findByUserNameAndPassword(loginDTO.getUsername(), loginDTO.getPassword());
//        if (sessionUser == null) {
//            // 로그인 실패 (유저 이름 or 비밀번호 불일치
//            throw new IllegalArgumentException("사용자명 또는 비밀번호가 잘못되었습니다.");
//        }
//
//        // 로그인 내용을 세션 메모리에 저장함
//        httpSession.setAttribute("sessionUser", sessionUser);
//
//        return "redirect:/";
//    } // end of loginProc
//
//    // 로그아웃 기능 요청
//    @GetMapping("/logout")
//    public String logout() {
//        // 세션 메모리에 내 정보를 삭제시켜버림 -- 무효화 처리 ==  로그아웃
//        httpSession.invalidate();
//
//        return "redirect:/";
//    }
//
//    // 회원 가입 화면 요청
//    // 주소 설계 : http://localhost:8080/join-form
//    @GetMapping("join-form")
//    public String joinFormPage() {
//        return "user/join-form";
//    } // end of joinFormPage
//
//    // 회원 가입 기능 요청
//    // 주소 설계 : http://localhost:8080/join
//
//    // 메세지 컨버터가 구문을 분석해 자동으로 파싱 처리 및 매핑해준다.
//    // 파싱 전략 1 - key-value 구조(@RequestParam 사용)
//    // 파싱 전략 2 - Object DTO 설계
//    @PostMapping("/join")
//    public String joinProc(UserRequest.JoinDTO joinDTO) {
//
//        // 유효성 검사 하기 --> 문제 발생 시 -> 예외 처리됨
//        joinDTO.validate();
//
//        // 회원가입 요청 전 -> 중복 username 검사
//        User userCheckName = userRepository.findByUserName(joinDTO.getUsername());
//        if (userCheckName != null) {
//            throw new IllegalArgumentException("이미 사용중인 유저네임입니다. " + userCheckName.getUsername());
//        }
//
//        userRepository.save(joinDTO.toEntity());
//
//        // 로그인 화면으로 리다이렉션 처리 예정
//        return "redirect:/login-form";
//    } // end of joinProc

} // end of class
