package com.tenco.blog.board;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;


// 테이블 생성

@Data // get,set , toString ..
// @Entity - JPA가 이 클래스를 데이터베이스 테이블과 매핑하는 객체로 인식하게 설정
// 즉, 이 어노테이션이 있어야 JPA가 관리 함
@Entity // 자동으로 테이블 생성
@Table(name = "board_tb") // 테이블 이름 생성
@NoArgsConstructor // 기본생성자(필수) - 생성 -> BoardRequest를 사용하기 위함
@AllArgsConstructor // 전체 맴버 변수를 넣을 수 있는 생성자.
@Builder // 빌더 패턴
public class Board {

    // pk명시 - 어노테이션 이용
    @Id
    // IDENTITY 전략 : 데이터베이스 기본 auto_increment 기능 사용
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String title;
    private String content;

    // 어노테이션 추가
    // @CreationTimestamp : 하이버네이트가 제공하는 어노테이션
    // 특정 하나의 엔티티가 저장이 될 때 현재 시간을 자동으로 저장해 주는 기능
    // 기존 now() 명시 -> 어노테이션 추가함으로 사용할 필요 없음
    // pc 시간 --> db (자동 날짜 주입)
    @CreationTimestamp
    private Timestamp createdAt;

}
