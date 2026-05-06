package com.tenco.blog.board;

import com.tenco.blog._core.errors.Exception403;
import com.tenco.blog._core.errors.Exception404;
import com.tenco.blog.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

// 서비스단을 사용하는 이유
// Control클래스와 많이 소통한다.
// 비즈니스 규칙 - 즉 개발회사 별 유효성 검사를 하는 곳이다.
// 객체지향 개념 - 단일 책임의 원칙 - SRP

// 서비스 계층은 @Service 어노데이션으로 Ioc처리
@Slf4j
@Service // IoC 처리
@RequiredArgsConstructor // DI 처리 Autowired는 final 선언 시 사용 불가능
@Transactional(readOnly = true) // Repository단에서 x -> service단에서 작업
// (readOnly = true) : 모든 메서드를 읽기 전용 트랜잭션으로 실행(ex : findAll, findById 등 조회에 적합)
// 성능 최적화(변경 감지(더티 체킹) 비활성화 됨)
// - 모든 기능을 더티체킹하면 프로그램이 무거워짐 즉 조회 시 데이터 수정 방지
// 구현 클래스 생성
public class BoardService {

    private final BoardRepository boardRepository;

    // BoardService의 핵심 비즈니스 기능
    // 게시글 저장
    // 게시글 목록
    // 게시글 상세보기
    // 게시글 수정

    // 게시글 저장
    // 데이터 수정이 필요하므로 깊은 트랜잭션 처리
    // 읽기 전용 트랜잭션 해제, 쓰기 전용 트랜잭션으로 변경)
    @Transactional
    public Board save(BoardRequest.SaveDTO saveDTO, User sessionUser) {
        // TODOList - service단에서 해야하는 작업
        // 1. 로그 기록 - 게시글 저장 요청 정보
        // 2. DTO를 Entity로 변환(작성자 정보 포함)
        // 3. 데이터베이스에 게시글 저장
        // 4. 저장 완료 로그 기록
        // 5. 저장된 게시글을 컨트롤러 단으로 반환

        // 1. 로그 기록 - 게시글 저장 요청 정보
        log.info("게시글 저장 서비스 시작 - 제목 : {}, 작성자 : {}",
                saveDTO.getTitle(), sessionUser.getUsername());

        // 2. DTO를 Entity로 변환(작성자 정보 포함)
        Board board = saveDTO.toEntity(sessionUser);
        // 비영속 상태

        // 3. 데이터베이스에 게시글 저장
        // boardRepository에서 자동으로 만들어진 save를 말하는것이다.
        Board savedBoardEntity = boardRepository.save(board);
        // 영속상태

        // 4. 저장 완료 로그 기록
        log.info("게시글 저장 완료 - ID : {}, 제목 : {}",
                savedBoardEntity.getId(), savedBoardEntity.getTitle());

        // 5. 저장된 게시글을 컨트롤러 단으로 반환
        return savedBoardEntity;

    } // end of save

    // 게시글 목록 조회
    public List<Board> findAll() {
        //TODOLIST
        // 1. 로그 기록 - 게시글 목록 조회
        // 2. 데이터베이스 접근해서 모든 게시글 목록을 조회
        // 3. 로그 기록 - (총 게시글 수)
        // 4. 조회된 게시글 목록을 컨트롤러로 반환

        log.info("게시글 목록 조회 서비스"); // 1

        //2. jpa가 생성한 id가 아니라 우리가 생성한 findAllJoin을 사용해야 함(n+1문제 해결)
        List<Board> boardList = boardRepository.findAllJoinUser();
        log.info("게시글 목록 조회 완료 - 총 : {}", boardList.size());
        return boardList;

    } // end of findAll

    // 게시글 상세보기
    public Board findById(Integer id) {
        //TODOLIST
        // 1. 로그 기록 - 게시글 상세 조회(id - pk키)
        // 2. 데이터베이스 접근해서 해당 id의 게시글 조회(작성자 정보 포함)
        // 3. 게시글이 존재하지 않을 경우 - Exception404로 예외 발생
        // 4. 조회 성공 시 로그 기록 (제목, 작성자 정보)
        // 5. 조회괸 게시글 컨트롤러 단으로 반환

        // 1
        log.info("게시글 상세 조회 서비스");

        // 2
        Board boardEntity = boardRepository.findByIdJoinUser(id).orElseThrow(() -> {
            // 3
            log.warn("게시글 조회 실패 - ID : {}",id);
            return new Exception404("해당 게시글을 찾을 수 없습니다.");
            // GlobalExceptionHandler가 Exception404를 찾아서 Exception404로 넘어감
        });

        // 4
        log.info("게시글 조회 완료 - 제목 : {} , 작성자 ; {} ",
                boardEntity.getTitle(),boardEntity.getUser().getUsername());

        // 5
        return boardEntity;
    } // end of findById

    // 게시글 수정
    @Transactional
    public Board updateById(Integer id,BoardRequest.UpdateDTO updateDTO, User sessionUser){
        //TODOLIST
        // 1. 로그 기록 - 게시글 수정 요청 정보(board  pk, 새 제목, 요청자)
        // 2. 수정하고자 하는 게시글 조회 (중간 삭제되는 경우도 있기 때문)
        // 3. 권한 확인 (인가 처리)
        // 4. 권한이 없다면 예외처리 (Exception 403)
        // 5. 더치 체킹으로 게시글 수정(JPA 영속성 컨텍스트 활용)
        // 6. 수정 완료 로그 기록
        // 7. 수정된 게시글 반환

        // 1
        log.info("게시글 수정 서비스");

        // 2
        Board boardEntity = findById(id);

        // 3 권한 확인
        boardEntity.isOwner(sessionUser.getId());

        //5
        // 영속화 되어 있었던 boardEntity의 내용을 변경
        boardEntity.update(updateDTO);

        //6
        log.info("게시글 수정 완료 - ID : {}, 새 제목 : {} ",
                boardEntity.getTitle(),
                boardEntity.getTitle());

        //7
        return boardEntity;
    } // end of updateById

    // 게시글 삭제 (권한 체크 포함)
    @Transactional
    public void deleteById(Integer id, User sessionUser){
        //TODOLIST
        // 1. 로그 기록 - 게시글 삭제 요청 정보(board  pk, 요청자)
        // 2. 삭제할려는 게시글 조회
        // 3. 권한 확인 (인가 처리) - 게시글 작성자와 삭제 요청자 동일한지 확인
        // 4. 권한이 없다면 예외처리 (Exception 403)
        // 5. 데이터베이스에서 게시글 삭제 실행
        // 6. 삭제 완료 로그 기록

        // 1
        log.info("게시글 삭제 요청 정보 - ID {}",id);

        // 2
        Board boardEntity = findById(id);

        // 3
        boardEntity.isOwner(sessionUser.getId());

        // 5
        boardRepository.deleteById(id);

        // 6
        log.info("게시글 삭제 완료 - ID {}",id);

    } // end of deleteById


} // end of class
