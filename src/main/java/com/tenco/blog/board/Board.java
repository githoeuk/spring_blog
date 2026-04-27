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


    // 기능 추가

    // 어노테이션 추가
    // @CreationTimestamp : 하이버네이트가 제공하는 어노테이션
    // 특정 하나의 엔티티가 저장이 될 때 현재 시간을 자동으로 저장해 주는 기능
    // 기존 now() 명시 -> 어노테이션 추가함으로 사용할 필요 없음
    // pc 시간 --> db (자동 날짜 주입)
    @CreationTimestamp
    private Timestamp createdAt;

    // createAt -> 포멧하는 메서드 만들어보기
    public String getTIme(){
        // TODO 수정
        return createdAt.toString();
    } // end of getTIme

    // 수정 편의 기능 추가
    public void update(BoardRequest.UpdateDTO updateDTO){
        this.title =  updateDTO.getTitle();
        this.username = updateDTO.getUsername();
        this.content = updateDTO.getContent();

        // 변경 감지 동작 과정 - 더티 체킹
        // 1. 최초 조회 시 영속성 컨텍스트 1차 캐쉬에 데이터를 스냅샷으로 보관
        // 2. 영속화 된 엔티티가(board)의 멤버 변수값이 변경이 된다면
        // - 1차에서 보관했던 값과 2차에서 수정된 필드값을 비교 한다.
        // 3. 변화가 감지되면 트랜잭션 커밋 시점에 변경된 필드값 update쿼리를 자동 생성
        // 4. 물리적인 DB에 반영 됨.
    } // end of update


}
