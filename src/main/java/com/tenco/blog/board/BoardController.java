package com.tenco.blog.board;

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
    private final BoardNativeRepository boardNativeRepository;
    private final BoardPersistRepository boardPersistRepository;

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
    public String saveProc(BoardRequest.SaveDTO saveDTO) {
        // 이제 따로 작성해서 대입할 필요없이 saveDTO를 통해 만들어둔 객체를
        // 이용해서 저장이 가능하다.
        boardPersistRepository.save(saveDTO.toEntity());

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

    // 게시글 제거
    @PostMapping("/board/{id}/delete")
    public String deleteProc(@PathVariable(name = "id") Integer id) {
        //boardNativeRepository.deleteById(id);
        boardPersistRepository.deleteById(id);
        // PRG 패턴 (Post -> Redirect -> Get) 적용
        return "redirect:/";
    } // end of deleteProc

    //게시글 수정 시 --> 다시 사용자가 게시글 작성할 수 있도록 설계
    // http://localhost:8080/board/1/update-form
    @GetMapping("/board/{id}/update-form")
    public String updateFormPage(@PathVariable(name = "id") Integer id, Model model) {
        // 사용자에게 해당 게시물 내용을 보여줘야 한다.
        // 조회 기능 - 게시글 id로

        Board board = boardPersistRepository.findById(id);
        model.addAttribute("board", board);

        return "board/update-form";
    } // end of updateFormPage

    // 게시글 수정
    @PostMapping("/board/{id}/update")
    // 메세지 컨버터 객체가 동작해서 자도으로 객체를 생성하고 값을 매핑해준다.(뷰 리졸브느낌)
    public String updateProc(@PathVariable(name = "id") Integer id, BoardRequest.UpdateDTO updateDTO) {
        //@PathVariable - 경로 변수를 가져올 수 있다.

        // 유효성 검사 username,title,content 유효성 검사
        updateDTO.validate();

        // 2, DAO 계층으로 전달
        boardPersistRepository.updateById(id, updateDTO);


        return "redirect:/board/" + id;
    } // end of updateProc

}
