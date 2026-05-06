package com.tenco.blog.user;

import lombok.Data;
import lombok.NoArgsConstructor;

public class UserRequest {

    @Data
    @NoArgsConstructor
    // 로그인 시 받을 데이터 DTO클래스 설계
    public static class loginDTO{
        private String username;
        private String password;

        // 유효성 검사
        public void validate(){
            if (username == null || username.trim().isEmpty()){
                throw new IllegalArgumentException("유저네임을 입력하세요 ");
            }
            if (password == null || password.trim().isEmpty()){
                throw new IllegalArgumentException("비밀번호를 입력하세요 ");
            }
        }

    } // end of loginDTO


    // 회원가입 시 받을 데이터 DTO클래스 설계
    @Data
    @NoArgsConstructor
    public static class JoinDTO{
        private String username;
        private String password;
        private String email;

        // 매서드 편의 기능 - 내가 가지고 있는 멤버 변수에 값으로 User 엔티티를 생성
        public User toEntity(){
            return User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .build();
        } // end of toEntity

        // 유효성 검사 메서드
        public void validate(){
            if (username == null || username.trim().isEmpty()){
                throw new IllegalArgumentException("사용자명을 입력하세요 ");
            }
            if (password == null ||password.trim().isEmpty()){
                throw new IllegalArgumentException("비밀번호를 입력하세요 ");
            }
            if (email == null ||email.trim().isEmpty()){
                throw new IllegalArgumentException("이메일를 입력하세요 ");
            }
            if (!email.contains("@")){ //@가 없다면 실행
                throw new IllegalArgumentException("올바른 이메일 형싱이 아닙니다 ");
            }
        }  // end of validate

    } // end of JoinDTO

    @Data
    // 프로필 수정 시 받을 DTO
    public static class UpdateDTO{
        private String password;

        public void validate(){

            if (password == null || password.isBlank()){
                throw new IllegalArgumentException("비밀번호는 필수 입니다.");
            }
            if (password.length() < 3){
                throw new IllegalArgumentException("비밀번호는 4자 이상이어야 합니다.");
            }

        }
    } // end of updateDTO


} // end of class
