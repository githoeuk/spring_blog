package com.tenco.blog.reply;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

//@Repository - 부모 클래스에 정의 되어 있음
public interface ReplyRepository extends JpaRepository<Reply, Integer> {

    // 자동으로 만들어주는 기능들 - 인수는 PK값의 데이터형을 참조해서 만들어준다.
    // 1. 등록 및 수정 save(Board entity)
    // 2. 단건 조회 : findById(Integer id)
    // 3. 전체 조회 : findAll()
    // 4. 삭제 : deleteById(Integer id) - reply
    // 5. 데이터 개수 : count()
    // 6. 존재 여부 확인 : existsByID(Integer id)


    // 기본적인 CRUD 및 추가 편의 기능 자동 생성(JpaRepository의 기능)

    // 게시글 ID로 댓글 목록 조회 ( 한번에 댓글 작성자 정보 포함 - JOIN FETCH 사용)
//    select
//    r.*,b.*,u.*
//    from reply_tb r
//    join board_tb b on r.board_id = b.id
//    join user_tb u on r.user_id = u.id
//    where r.board_id = 1
//    order by r.created_at asc;
//    JPQL 문법으로 변화 예정

    @Query("""
            SELECT r FROM Reply r
                        JOIN FETCH r.user JOIN FETCH r.board 
                                    WHERE r.board.id = :boardId 
                                                ORDER BY r.createdAt ASC
            """)
    List<Reply> findByBoardIdWithUser(@Param("boardId") Integer boardId);

    /**
     * 이전 수정 또는 삭제 기능에서는 수정은 더티 체킹 처리,
     * 삭제는 기본적으로 제공하는 em.remove() 메서드를 사용해서 처리했었다.
     * 지금은 직접 JQPL 쿼리를 선언해서 DELETE 처리하는 구문이라 다른 상황이다.
     *
     * @param boardId
     * @Query(...) <- JPA가 기본적으로 SELECT 쿼리로만 인식한다
     * insert,update,delete 는 jpa에게 select 쿼리가 아니라는것을 명시해줘야 한다.
     * 그 어노테이션이 @Modifying 이다
     */
    @Modifying
    @Query("""
            DELETE FROM Reply r WHERE r.board.id = :boardId
            """)
    void deleteByBoardId(@Param("boardId") Integer boardId);
} // end of class
