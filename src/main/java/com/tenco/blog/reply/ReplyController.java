package com.tenco.blog.reply;

import com.tenco.blog.user.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller // IoC
@RequiredArgsConstructor

public class ReplyController {

    private final ReplyService replyService;

    // 댓글 등록 기능 요청
    @PostMapping ("/reply/save")
    public String saveProc(ReplyRequest.SaveDTO saveDTO,
                           HttpSession session
                           ){

        // 1. 인증검사 ( 로그인 여부 확인 ) -> LoginInterceptor 처리
        User sessionUser = (User) session.getAttribute("sessionUser");

        // 2. 유효성 검사 (comment값 확인)
        saveDTO.validate();

        replyService.댓글작성(saveDTO,sessionUser.getId());

        // 해당 게시글에 댓글 작성 후 리다이렉션 처리(해당 게시글로)
        return "redirect:/board/" + saveDTO.getBoardId();
    } // end of saveProc

    // 댓글 삭제 기능 요청

    // mission? 댓글 수정 기능 요청

}
