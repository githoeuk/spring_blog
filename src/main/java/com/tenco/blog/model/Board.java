package com.tenco.blog.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.sql.Timestamp;


// 테이블 생성

@Data // get,set , toString ..
// @Entity - JPA가 이 클래스를 데이터베이스 테이블과 매핑하는 객체로 인식하게 설정
// 즉, 이 어노테이션이 있어야 JPA가 관리 함
@Entity // 자동으로 테이블 생성
@Table(name = "board_tb") // 테이블 이름 생성
public class Board {

    // pk명시 - 어노테이션 이용
    @Id
    // IDENTITY 전략 : 데이터베이스 기본 auto_increment 기능 사용
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String title;
    private String content;
    private Timestamp createdAt;

}
