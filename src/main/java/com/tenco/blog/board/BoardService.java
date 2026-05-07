package com.tenco.blog.board;

import com.tenco.blog._core.errors.Exception403;
import com.tenco.blog._core.errors.Exception404;
import com.tenco.blog.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/*
    서비스 레이어
    핵심 개념
    1. 서비스 레이어의 역할
    - 비즈니스 로직을 처리하는 계층 ( Controller와 Response 사이에서 중간 계층 담당)
    - 트랜잭션 관리
    - 여러 Repository를 조합해서 복잡한 비즈니스 로직 처리

    2. 계층 구조 (3Tire 아키텍쳐)
    - Controller -> Service ->  Repository -> DB

    3. @Service 어노테이션 사용
    - Spring에서 이 어노테이션을 확인하여 Bean(빈) 등록한다.
    - 빈이란? IoC를 통해 스프링이 올린 모든 객체들을 '빈'이라고 한다.
 */

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)

public class BoardService {

    private final BoardRepository boardRepository;

    /*
        게시글 목록 조회
        OSIV false 환경 대응 - 응답 DTO 설계
     */
    public List<BoardResponse.ListDTO> 게시글목록() {

        log.info("게시글 목록 조회 서비스");
        //2. jpa가 생성한 id가 아니라 우리가 생성한 findAllJoin을 사용해야 함(n+1문제 해결)
        List<Board> boardList = boardRepository.findAllJoinUser();
        log.info("게시글 목록 조회 완료 - 총 : {}", boardList.size());
        // 방법 - 메서드 참조 사용
        return boardList.stream()
                .map(BoardResponse.ListDTO::new)
                .collect(Collectors.toList());

    } // end of findAll

    /**
     * 게시글 상세 조회
     *
     * @param id (Board PK)
     * @return DetailDTO 처리(OSIV 대응)
     */
    public BoardResponse.DetailDTO 게시글상세조회(Integer id) {
        log.info("게시글 상세 조회 서비스");
        // N + 1 문제 해결을 위해 한번 Board,User를 가지고 옴
        Board boardEntity = boardRepository.findByIdJoinUser(id).orElseThrow(() -> {
            log.warn("게시글 조회 실패 - ID : {}", id);
            return new Exception404("해당 게시글을 찾을 수 없습니다.");
            // GlobalExceptionHandler가 Exception404를 찾아서 Exception404로 넘어감
        });
        log.info("게시글 조회 완료 - 제목 : {} , 작성자 ; {} ",
                boardEntity.getTitle(), boardEntity.getUser().getUsername());
        return new BoardResponse.DetailDTO(boardEntity);
    } // end of findById

    /**
     * 게시글 작성
     *
     * @param saveDTO     (사용자가 입력한 데이터)
     * @param sessionUser (세션메모리에서 가져온 사용자 정보)
     */
    @Transactional
    public void 게시글작성(BoardRequest.SaveDTO saveDTO, User sessionUser) {

        log.info("게시글 저장 서비스 시작 - 제목 : {}, 작성자 : {}",
                saveDTO.getTitle(), sessionUser.getUsername());

        Board board = saveDTO.toEntity(sessionUser);
        Board savedBoardEntity = boardRepository.save(board);

        log.info("게시글 저장 완료 - ID : {}, 제목 : {}",
                savedBoardEntity.getId(), savedBoardEntity.getTitle());

    } // end of save

    /**
     * 게시글 상세 화면 요청 (인가 처리)
     *
     * @param id          (Board PK)
     * @param sessionUser (로그인한 사용자 정보)
     * @return BoardEntity
     */
    public BoardResponse.DetailDTO 게시글상세화면및인가처리(Integer id, User sessionUser) {

        log.info("게시글 상세 화면 및 인가 확인");

        BoardResponse.DetailDTO detailDTO = 게시글상세조회(id);
        if (!detailDTO.getUserId().equals(sessionUser.getId())) {
            throw new Exception403("권한없음");
        }

        log.info("게시글 수정 조회 완료 - 제목 : {} , 작성자 ; {} ",
                detailDTO.getTitle(), detailDTO.getUsername());
        return detailDTO;
    } // end of findById


    /**
     * 게시글 수정 기능 처리
     *
     * @param id          (Board PK)
     * @param updateDTO   ( 시용자가..)
     * @param sessionUser (세션에서 ..)
     * @return
     */
    @Transactional
    public void 게시글수정(Integer id, BoardRequest.UpdateDTO updateDTO, User sessionUser) {


        log.info("게시글 수정 서비스");
        Board boardEntity = boardRepository.findByIdJoinUser(id).orElseThrow(() -> {
            throw new Exception404("해당 게시글을 찾을 수 없습니다.");
        });

        boardEntity.update(updateDTO);
        log.info("게시글 수정 완료 - ID : {}, 새 제목 : {} ",
                boardEntity.getTitle(),
                boardEntity.getTitle());
    } // end of updateById

    /**
     * 게시글 삭제 요청
     *
     * @param id          (Board PK)
     * @param sessionUser (세션메모리...)
     */
    @Transactional
    public void 게시글삭제(Integer id, User sessionUser) {

        log.info("게시글 삭제 요청 정보 - ID {}", id);
        Board boardEntity = boardRepository.findById(id).orElseThrow(
                () -> new Exception404("해당 게시물을 찾을 수 없습니다.")
        );
        boardEntity.isOwner(sessionUser.getId());
        boardRepository.deleteById(id);
        log.info("게시글 삭제 완료 - ID {}", id);

    } // end of deleteById


} // end of class
