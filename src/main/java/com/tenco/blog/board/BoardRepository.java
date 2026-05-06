package com.tenco.blog.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/*
    Board 엔티티에 대한 JAP repository 인터페이스 (클래스 아님 !)
    게시글과 관련된 데이터 베이스 접근을 담당하게 됨
    기본적인 CRUD를 이미 제공하는 중
 */

// @Repository // IoC - 이제 굳이 명시할 필요 없음 - 스프링 부트 어노테이션을 상속받기 때문
// - JpaRepository 인터페이스에서 이미 상속받았기 때문에 선언 할 필요가 없다
// 하이버네이트가 우리가 제작한 인터페이스를 구현클래스로 작성해준다.
public interface BoardRepository extends JpaRepository<Board, Integer> {

    // 자동으로 만들어주는 기능들 - 인수는 PK값의 데이터형을 참조해서 만들어준다.
    // 1. 등록 및 수정 save(Board entity)
    // 2. 단건 조회 : findById(Integer id)
    // 3. 전체 조회 : findAll()
    // 4. 삭제 : deleteById(Integer id)
    // 5. 데이터 개수 : count()
    // 6. 존재 여부 확인 : existsByID(Integer id)

    // 1. 게시글 ID로 조회 시 사용자 정보도 함께 가져오기 ( fetch전략 - lazy전략 사용 중 )
    // lazy전략을 사용함으로 쿼리가 2번 발생 함(n+1 문제)
    // -> 성능을 위해 join쿼리로 수정할 수있음 -> JpaRepository의 제공하는 기능에서 수정
    @Query("""
            SELECT b FROM Board b join FETCH b.user WHERE b.id = :id
            """)
    Optional<Board> findByIdJoinUser(@Param("id") Integer id);
    // BoardPersisRepository에서 작성했던 - JpaRepository 버전

    // 2. 전체 게시글 조회
    // (단 한번에 작성자 정보도 조회 - 추가 기능(JpaRepository에는 없는 기능))
    // n+1 문제 해결
    @Query("""
            SELECT b FROM Board b join FETCH b.user ORDER BY b.id DESC 
            """)
    List<Board> findAllJoinUser();
    // BoardPersisRepository의 findByAll - JpaRepository 버전

    // 3. 데이터 수정은 (더티 체킹으로 처리)
    // 더티 체킹 - 1차 캐쉬에 없다면 조회 후 저장
    // -> 근데 1차 캐쉬에 있는 내용이 변경되었다면 동기화 처리해줌

} // end of class
