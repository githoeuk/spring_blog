package com.tenco.blog.controller;

import com.tenco.blog.model.Board;
import com.tenco.blog.repository.BoardNativeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller // Ioc
@RequiredArgsConstructor //  DI

public class BoardController {

    // DI처리
    private final BoardNativeRepository boardNativeRepository;


    /**
     * 게시글 작성 화면 요청
     *
     * @return 페이지 반환
     * 주소설계 : http://localhost:8080/board/save-form
     */
    // 자원의 요청 , 화면의 요청
    @GetMapping("/board/save-form")
    public String saveForm() {
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
    public String saveProc(
            @RequestParam("username") String username,
            @RequestParam("title") String title,
            @RequestParam("content") String content) {

        log.info("username: " + username);
        log.info("title : " + title);
        log.info("content : " + content);

        // insert + 트랜잭션 처리
        boardNativeRepository.save(title, content, username);
        // redirect : 다시 URL 요청 해!
        // return "redirect:/";
        return "redirect:/";
    } // end of saveProc

    /**
     * 게시글 목록 화면 요청
     *
     * @return 페이지 반환
     * 주소설계 : http://localhost:8080
     */

    @GetMapping({"/", "index"})
    public String list(Model model) {

        List<Board> boardList = boardNativeRepository.findAll();
        model.addAttribute("boardList", boardList);

        return "board/list";
    } // end of list

    // 게시글 상세보기 화면 요청
    // http://localhost:8080/board/1
    @GetMapping("/board/{id}")
    public String detailPage(@PathVariable(name = "id") Integer id, Model model) {
        // 유효성 검사 , 인증 검사
        Board board = boardNativeRepository.findById(id);
        model.addAttribute("board", board);

        return "board/detail";
    } // end of detailPage

    // 게시글 제거
    @PostMapping("/board/{id}/delete")
    public String deleteProc(@PathVariable(name = "id") Integer id) {
        boardNativeRepository.deleteById(id);
        // PRG 패턴 (Post -> Redirect -> Get) 적용
        return "redirect:/";
    } // end of deleteProc

    //게시글 수정 시 --> 다시 사용자가 게시글 작성할 수 있도록 설계
    // http://localhost:8080/board/1/update-form
    @GetMapping("/board/{id}/update-form")
    public String updateFormPage(@PathVariable(name = "id") Integer id, Model model) {
        // 사용자에게 해당 게시물 내용을 보여줘야 한다.
        // 조회 기능 - 게시글 id로

        Board board = boardNativeRepository.findById(id);
        model.addAttribute("board", board);

        return "board/update-form";
    }

    // 게시글 수정
    @PostMapping("/board/{id}/update")
    public String updateProc(@PathVariable(name = "id") Integer id,
                             @RequestParam(name = "username") String username,
                             @RequestParam(name = "title") String title,
                             @RequestParam(name = "content") String content) {

        log.info("username : " + username);
        log.info("title : " + title);
        log.info("content : " + content);
        log.info("id : " + id);

        boardNativeRepository.updateById(username, title, content, id);

        // 게시글 수정 완료 --> 게시글 목록, 게시글 상세보기 화면
        // 리다이렉트는 뷰 리졸브 동작이 아닌 (내부 파일을 찾는게 아니라는 의미)
        // 그냥 개로운 HTTP Get 요청이다.
        return "redirect:/board/" + id;
    }

}
