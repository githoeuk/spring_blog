package com.tenco.blog.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user_tb") // 자동으로 테이블 생성


public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincrement 기능
    private Integer id;
    @Column(unique = true) // 사용자명 중복 방지를 위한 유니크 제약 조건 설정
    private String username;
    private String password;
    private String email;
    @CreationTimestamp // 엔티티가 영속화 될 때 자동으로 현재 시간을 주입 - pc -> db
    private Timestamp createdAt;

    @Builder
    public User(Integer id, String username, String password, String email, Timestamp createdAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.createdAt = createdAt;
    }
}
