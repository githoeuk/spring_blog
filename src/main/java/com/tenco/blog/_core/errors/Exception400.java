package com.tenco.blog._core.errors;

// 400 -> bad Request
public class Exception400 extends RuntimeException {

    // 예외 메세지를 외부에서 받아서 내 부모클래스 RuntimeException에게
    // 생성자로 전달

    public Exception400(String msg){
        super(msg);  // 즉 부모 클래스 메세지로 내가 직접 작성한 부분을 설정
    }
    // throw new Exception400("잘못된 요청"); ->  사용 예시
}
