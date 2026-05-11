package com.tenco.blog.reply;

import com.tenco.blog._core.errors.Exception404;
import com.tenco.blog.board.Board;
import com.tenco.blog.board.BoardRepository;
import com.tenco.blog.user.User;
import com.tenco.blog.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


// Service 계층에서는 여러 Repository를 조합해서 비즈니스 규칙을 완성한다.
// 즉, 서비스 계층이 필요한 이유 중 하나이다.
@Service // IoC
@RequiredArgsConstructor // DI처리
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public Reply 댓글작성(ReplyRequest.SaveDTO saveDTO, Integer id) {

        // 1. 게시글 조회
        Board boardEntity = boardRepository.findById(saveDTO.getBoardId()).orElseThrow(() -> {
            throw new Exception404("해당 게시글을 찾을 수 없습니다.");
        });

        // 2. user의 id 로 사용자 조회
        User userEntity = userRepository.findById(id).orElseThrow(() -> {
            throw new Exception404("해당 사용자를 찾을 수 없습니다.");
        });

        // 3. 댓글 작성
        Reply reply  = saveDTO.toEntity(userEntity,boardEntity);
        replyRepository.save(reply);
        return reply;
    }



} // end of class
