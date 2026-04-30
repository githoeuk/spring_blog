package com.tenco.blog.board;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Ioc -  메모리에 올라감(제어 역전이기 때문에)
@RequiredArgsConstructor // DI 처리 시킴 - 어떤 역할?

public class BoardPersistRepository {

    // JPA 핵심 인터페이스
    // 영속성 컨텍스트(1차 캐시, sql저장소)를 관리하고 엔티티의 생명 주기를 제어
    // 즉 영속성 컨텍스트 적용
    //@Autowired // DI - 외부 의존 주입 - 어떤 의존성?

    private final EntityManager em; // 성능 개선이 조금 됨

    // DI -> 의존 주입 (외부에서 생성되어 있는 객체의 주소값을 주입 받음)
//    public BoardPersistRepository(EntityManager em){
//        this.em = em;
//    }


    // 게시글 저장
    @Transactional
    public Board save(Board board) {

        em.persist(board); // insert 처리 완료

        return board;
    } // end of save

    //게시글 목록 조회(JPQL를 사용해서)
    public List<Board> findAll() {
        // JPQL : Entity 객체를 대상으로 하는 객체지향 쿼리
        // Board는 Entity 클래스 명 , b는 별칭
        // 주의! 테이블 명이 아닌 클래스명(Entity) 사용


        // JOIN FETCH 사용 쿼리 변경
        // : Board와 연관된 데이터를 JOIN으로 한번에 가져오는 문법
        // N + 1 문제를 해결하는 정밀 제어

        String jpqlStr = "SELECT b FROM Board b JOIN FETCH b.user ORDER BY b.id DESC";
        List<Board> boardList = em.createQuery(jpqlStr, Board.class).getResultList();

        return boardList;

    } // end of findAll

    // 게시글 상세보기 요청(조회) (필수값 - 기본키로 조회)
    public Board findById(Integer id) {

        // 영속성 컨텍스를 사용하기 위해
        // 1. 엔티티 매니저에서 제공하는 메서드를 활용하는 방법
        Board board = em.find(Board.class, id);

        return board;
    } // end of findById

    // 게시글 삭제
    @Transactional
    public void deleteById(Integer id) {
        // 1.삭제하기 위해선 엔티티를 조회해야함
        Board board = em.find(Board.class, id);
        // 1.1 ㄴ조회가 되었기 때문에 board는 영속화가 된 상태이다.

        if (board == null) {
            throw new IllegalArgumentException("삭제할 게시글을 찾을 수 없습니다." + id);
        }

        // 조회가 되었다면 삭제할 데이터를 넣어주면 된다
        em.remove(board); //remove를 사용하기 위해서는 조건이 필요함
    } // end of deleteById

    @Transactional
    public Board updateById(Integer id, BoardRequest.UpdateDTO updateDTO) {
        // 수정 시 주의사항
        // 조회를 먼저 해야한다.
        Board boardEntity = em.find(Board.class, id);

        if (boardEntity == null) {
            throw new IllegalArgumentException("수정할 게시글을 찾을 수 없습니다." + id);
        }

        //Dirty checking
        boardEntity.update(updateDTO); // 상태 변경
        return boardEntity;
    } // end of updateById

} // end of class
