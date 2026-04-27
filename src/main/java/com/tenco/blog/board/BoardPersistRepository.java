package com.tenco.blog.board;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
    public Board save(Board board){
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
    }
}
