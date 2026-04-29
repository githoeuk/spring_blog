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
        // 1. 매개 변수로 받은 board는 비영속상태
        // 영속상태란 - 아직 영속성 컨텍스트에 관리 되지 않고 있는 상태
        // 아직 데이터베이스와 연관없는 순수 java 객체일 뿐

        //em.createNativeQuery() -- 대신 사용할 문법
        em.persist(board); // insert 처리 완료
        // 2. 내부 상태
        // 이 board 객체를 영속성 컨텍스트에 넣어 둠(SQL 저장소에등록)
        // 영속성 컨텍스트에 들어가더라도 아직 DB에 실제 insert한 상태는 아님

        // 3. 트랜잭션 커밋 시점에 실제 DB에 접근해서 insert 구문이 수행된다.

        // 4. board 객체의 id 변수값을 1차 캐쉬에 map 구조로 보관 되어짐.

        // 1차 캐쉬에 들어간 이제 영속상태로 변경된 object 리턴한다.
        return board;
    } // end of save

    //게시글 목록 조회(JPQL를 사용해서)
    public List<Board> findAll() {
        // JPQL : Entity 객체를 대상으로 하는 객체지향 쿼리
        // Board는 Entity 클래스 명 , b는 별칭
        // 주의! 테이블 명이 아닌 클래스명(Entity) 사용

        String jpqlStr = "SELECT b FROM Board b ORDER BY b.id DESC";
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
    public void updateById(Integer id, BoardRequest.UpdateDTO updateDTO) {
        // 수정 시 주의사항
        // 조회를 먼저 해야한다.
        Board boardEntity = em.find(Board.class, id);
        // em.find() 호출 후 리턴 받은 board는 영속 상태이다.

        if (boardEntity == null) {
            throw new IllegalArgumentException("수정할 게시글을 찾을 수 없습니다." + id);
        }

//        // 엔티티 -> 테이블과 매핑되는 object는 updateDTO가 없음
//        // 우리가 관리하고자 하는 엔티티는 Board이다.
//        // em.persist(board); -> pk값인 id가 없기때문에 새로운 게시물로 생성되어 버린다..
//        // 그렇기에 boardEntity에 조회된 값들에다가 덮어쓰기를 통해 업데이트를 진행한다.
//        boardEntity.setUsername(board.getUsername());
//        boardEntity.setTitle(board.getTitle());
//        boardEntity.setContent(board.getContent());

        boardEntity.update(updateDTO); // 상태 변경

        // 변경 감지(dirty checking) 동작 됨.
        // 영속 컨텍스트에 관리 되어지는 객체(엔티티) 안에 조회 했을 때 기준으로 1차 캐쉬에 저장되어 짐
        // 추후  1차 캐쉬에 들어가 있는 객체의 (엔티티)의 변수값이 변경되었다면 자동으로 감지한다.
        //em.persist(boardEntity); -> 자동으로 변경되기에 사용할 필요가 없다.


        // 앞으로 수정 기능을 만들어 줄때 더티 체킹 동작으로 사용하자.

    } // end of updateById

} // end of class
