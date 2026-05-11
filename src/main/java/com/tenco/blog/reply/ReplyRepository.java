package com.tenco.blog.reply;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//@Repository - 부모 클래스에 정의 되어 있음
public interface ReplyRepository extends JpaRepository <Reply,Integer> {
    // 기본적인 CRUD 및 추가 편의 기능 자동 생성(JpaRepository의 기능)



} // end of class
