package com.tenco.blog.reply;

import com.tenco.blog.board.Board;
import com.tenco.blog.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Entity
@Table(name = "reply_tb")
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Increment
    private Integer id;

    // 제약 조건 ( 500자까지 작성, null 허용 안함)
    @Column(length = 500, nullable = false)
    private String comment;

    @CreationTimestamp // pc -> db로 자동 주입
    private Timestamp createdAt;

    // 한명의 유저 -> 여러개의 댓글 가능
    // Reply -> User 연관관계 설정 (외래키를 자바에서 표현하는 방법)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 하나의 게시글 -> 여러개의 댓글 가능
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;


    @Builder
    public Reply(String comment, User user, Board board) {
        this.comment = comment;
        this.user = user;
        this.board = board;
    }

    // 인가 처리 (권한 처리)

    /**
     * 댓글 소유자 확인 로직 (세션 정보, DB 작성된 user_id )
     *
     * @return
     */
    public boolean isOwner(Integer userId) {
        if (this.user == null || userId == null) {
            return false;
        }
        if (this.user.getId() != userId) {
            return false;
        }
        return true;
    }


} // end of class