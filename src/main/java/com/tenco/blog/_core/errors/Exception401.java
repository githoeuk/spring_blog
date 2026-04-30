package com.tenco.blog._core.errors;

public class Exception401 extends RuntimeException{

    public Exception401(String msg){
        super(msg);  // 즉 부모 클래스 메세지로 내가 직접 작성한 부분을 설정
    }
    // throw new Exception400("잘못된 요청"); ->  사용 예시
}
