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
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@Controller // Ioc
@RequiredArgsConstructor //  DI

public class BoardController {

    private final BoardService boardService;

    /**
     * 게시글 작성 화면 요청
     *
     * @return 페이지 반환
     * 주소설계 : http://localhost:8080/board/save-form
     */
    // 자원의 요청 , 화면의 요청
    @GetMapping("/board/save-form")
    public String saveForm(HttpSession httpSession) {
        // 1. 인증 검사 -> LoginInterceptor에서 처리 중
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
        // 1. 인증 검사 - 인터셉터 처리됨

        // 3. save기능 요청(service단에 요청)
        User sessionUser = (User) session.getAttribute("sessionUser");
        // 2. 유효성 검사
        saveDTO.validate();

        boardService.게시글작성(saveDTO,sessionUser);
        // 화면에 반환
        return "redirect:/";
    } // end of saveProc

    /**
     * 게시글 목록 화면 요청
     *
     * @return 페이지 반환
     * 주소설계 : http://localhost:8080
     */
    // 게시글 목록 보기
    @GetMapping({"/", "index"})
    public String list(Model model) {
        List<BoardResponse.ListDTO> boardList = boardService.게시글목록();
        // OSIV 개념을 false로 설정했기 때문에 여기서 LAZY 요청을 하면 터져버린다.
        // boardList.get(0).getUser().getUsername();

        model.addAttribute("boardList", boardList); // 가방에 담아서 넘겨주기
        return "board/list";
    } // end of list

    // 게시글 상세보기 화면 요청
    // http://localhost:8080/board/1
    @GetMapping("/board/{id}")
    public String detailPage(@PathVariable(name = "id") Integer id, Model model) {

        BoardResponse.DetailDTO detailDTO = boardService.게시글상세조회(id);

        // 댓글 목록 조회 기능 필요 - TODO

        model.addAttribute("board", detailDTO);
        return "board/detail";
    } // end of detailPage

    // 게시글 삭제
    // 1. 로그인 여부 확인
    // 2. 삭제할 게시글이 본인이 작성한 게시글인지 확인 (권한 확인,인가 처리)
    // 3. 인가처리 후  삭제 진행
    @PostMapping("/board/{id}/delete")
    public String deleteProc(@PathVariable(name = "id") Integer id, HttpSession session) {

        //인증검사 - 로그인 인터셉터가 처리

        // service단에 삭제 요청
        User sessionUser = (User) session.getAttribute("sessionUser");
        boardService.게시글삭제(id,sessionUser); // 권한,인증 검사함

        return "redirect:/";
    } // end of deleteProc

    //게시글 수정 시 --> 사용자에게 해당 게시물 내용을 보여줘야 한다.
    // http://localhost:8080/board/1/update-form
    @GetMapping("/board/{id}/update-form")
    public String updateFormPage(@PathVariable(name = "id") Integer id, Model model, HttpSession session) {

        User sessionUser = (User)session.getAttribute("sessionUser");
        BoardResponse.DetailDTO detailDTO = boardService.게시글상세화면및인가처리(id,sessionUser);
        model.addAttribute("board", detailDTO);
        return "board/update-form";

    } // end of updateFormPage

    // 게시글 수정
    @PostMapping("/board/{id}/update")
    public String updateProc(@PathVariable(name = "id") Integer id, BoardRequest.UpdateDTO updateDTO
            , HttpSession session) {

        User sessionUser = (User) session.getAttribute("sessionUser");
        updateDTO.validate();
        boardService.게시글수정(id,updateDTO,sessionUser);

        return "redirect:/board/" + id;
    } // end of updateProc

}
