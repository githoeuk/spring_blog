package com.tenco.blog.board;

import com.tenco.blog.user.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller // Ioc
@RequiredArgsConstructor //  DI

public class BoardController {

    // DI처리
    private final BoardPersistRepository boardPersistRepository;

    /**
     * 게시글 작성 화면 요청
     *
     * @return 페이지 반환
     * 주소설계 : http://localhost:8080/board/save-form
     */
    // 자원의 요청 , 화면의 요청
    @GetMapping("/board/save-form")
    public String saveForm(HttpSession httpSession) {
        // 로그인 여부 체크 - 즉 로그인 한 사용자만 이 페이지 안에 들어 수 있음
        // 1. 인증 검사
        User sessionUser = (User) httpSession.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/login-form";
        }

        return "board/save-form";
    }

    // 작성한 게시글을 받아야 함

    /**
     * 게시글 작성 기능 요청
     *
     * @return 페이지 반환
     * 주소설계 : http://localhost:8080/board/save
     */

    //자원의 생성 ,기능의 요청 - 백그라운드에서 동작하는가?
    @PostMapping("/board/save")// - 알아서 구분 가능함
    public String saveProc(BoardRequest.SaveDTO saveDTO, HttpSession session) {

        log.info("===게시글 저장 요청====");
        // 이 요청 사용자가 로그인을했다면 로그인 정보를 세션 메모리에서 가져오면된다.
        // 1. 세션에서 로그인한 사용자 정보 가져오기
        // HttpSession으로 저장한 변수 호출 - 가져오기
        User sessionUser = (User) session.getAttribute("sessionUser");

        // 2. 로그인 여부 확인
        if (sessionUser == null) {
            return "redirect:/login-form";
        }

        try {
            //3. 로그인 된 사용자
            //3.1 유효성 검사
            saveDTO.validate();

            Board board = saveDTO.toEntity(sessionUser);
            boardPersistRepository.save(board);

            // redirect : 다시 URL 요청 해!
            // return "redirect:/";
            return "redirect:/";
        } catch (Exception e) {
            System.out.println("에러 발생 : " + e.getMessage());
            return "board/save-form";
        }


    } // end of saveProc

    /**
     * 게시글 목록 화면 요청
     *
     * @return 페이지 반환
     * 주소설계 : http://localhost:8080
     */

    @GetMapping({"/", "index"})
    public String list(Model model) {
        List<Board> boardList = boardPersistRepository.findAll();
        model.addAttribute("boardList", boardList);
        return "board/list";
    } // end of list

    // 게시글 상세보기 화면 요청
    // http://localhost:8080/board/1
    @GetMapping("/board/{id}")
    public String detailPage(@PathVariable(name = "id") Integer id, Model model) {
        // 유효성 검사 , 인증 검사
        Board board = boardPersistRepository.findById(id);
        // board는 연관 관계가 User엔티티와 ManyToOne 관계 설정이 되어 잇다.
        // 직접 쿼리 구문을 작성하지 않을 때 즉,
        // 엔티티매니저의 메서드로 객체를 조회 시
        // 자동으로 JOIN 구문을 호출해준다.
        // 단 Fetch전략에 따라 EAGER , LAZY 전략에따라
        // 한번에 다 조인해서 가져오거나 (EAGER)
        // 필요할 때 한번 더 요청할 수 있다 (LAZY)
        // 코드상에서 User에 정보를 요구 - lazy전략 시
        // System.out.println(board.getUser().getUsername());

        model.addAttribute("board", board);
        return "board/detail";
    } // end of detailPage

    // 게시글 삭제
    // 1. 로그인 여부 확인
    // 2. 삭제할 게시글이 본인이 작성한 게시글인지 확인 (권한 확인,인가 처리)
    // 3. 인가처리 후  삭제 진행
    @PostMapping("/board/{id}/delete")
    public String deleteProc(@PathVariable(name = "id") Integer id, HttpSession session) {
        //boardNativeRepository.deleteById(id);

        log.info("=== 게시글 삭제 요청 ===");
        //인증검사
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/login-form";
        }
        try {
            // 삭제할 게시글 조회 (권한 체크 = 인가 처리)
            Board board = boardPersistRepository.findById(id);
            if (board.getUser().getId() == sessionUser.getId()) {
                boardPersistRepository.deleteById(id);
            }


        } catch (Exception e) {
            return "redirect:/";
        }

        // PRG 패턴 (Post -> Redirect -> Get) 적용
        return "redirect:/";
    } // end of deleteProc

    //게시글 수정 시 --> 사용자에게 해당 게시물 내용을 보여줘야 한다.
    // http://localhost:8080/board/1/update-form
    @GetMapping("/board/{id}/update-form")
    public String updateFormPage(@PathVariable(name = "id") Integer id, Model model, HttpSession session) {

        // 인증 처리
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/login-form";
        }

        // 인가 처리
        // 조회 기능 - 게시글 id로
        Board board = boardPersistRepository.findById(id);
        if (sessionUser.getId() != board.getUser().getId()) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        model.addAttribute("board", board);

        return "board/update-form";
    } // end of updateFormPage

    // 게시글 수정
    @PostMapping("/board/{id}/update")
    // 메세지 컨버터 객체가 동작해서 자도으로 객체를 생성하고 값을 매핑해준다.(뷰 리졸브느낌)
    public String updateProc(@PathVariable(name = "id") Integer id, BoardRequest.UpdateDTO updateDTO
            , HttpSession session) {
        //@PathVariable - 경로 변수를 가져올 수 있다.

        // 인증 검사
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/login-form";
        }

        try {
            // 유효성 검사
            updateDTO.validate();
            // 인가 처리
            Board board = boardPersistRepository.findById(id);
            if (sessionUser.getId() != board.getUser().getId()) {
                throw new RuntimeException("수정할 권한이 없습니다.");
            }
            // 2, DAO 계층으로 전달
            boardPersistRepository.updateById(id, updateDTO);

        } catch (Exception e) {
            return "redirect:/board/" + id + "/update-form";
        }

        return "redirect:/board/" + id;
    } // end of updateProc

}
