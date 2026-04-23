package com.tenco.blog.repository;

import com.tenco.blog.model.Board;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

// db 작업을 할때는 Repository 어노테이션을 적용한다
@Repository // Ioc + DI
@RequiredArgsConstructor // 자동으로 생성자 의존 주입
public class BoardNativeRepository {

    // EntityManager : JPA 핵심 인터페이스
    // 데이터베이스와 모든 작업을 담당한다.
    private final EntityManager em;

    // DI - 생성자 의존 주입(dependency injection)
 /*   public BoardNativeRepository(EntityManager em) {
        this.em = em;
    }*/

    //트랜잭션 처리
    @Transactional
    // 값 저장 커리
    public void save(String title, String content, String username) {
        Query query = em.createNativeQuery("insert" +
                " into board_tb(title,content,username , created_at) values (? , ? , ? , now())");

        query.setParameter(1, title);
        query.setParameter(2, content);
        query.setParameter(3, username);

        query.executeUpdate();
    }

    // 게시글 목록 조회
    public List<Board> findAll() {
        String sql = """
                SELECT * FROM board_tb order by id desc
                """;

        // while(rs.next) {Board board = new Board();
        // board.settitle(rs.getString("title")))}

        Query query = em.createNativeQuery(sql,Board.class);

        // while(rs.next){
        // rs....
        // } - 이제 필요없음
        return query.getResultList(); // 다중행으로 받아준다.
    }


}
