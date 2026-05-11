package com.tenco.blog.board;

import com.tenco.blog.util.MyDataUtil;
import lombok.Data;

// 사용자에게 보내는 DTO
/*
    게시글 응답 DTO
    Open Session In View가 false일 때
    트랜잭션이 종료되는 시점에 LAZY 로딩이 불가능하다.
    Service 단에서 필요한 데이터를 모두 조회 or 일부로 호출(트리거)해서 응답 DTO 변환해서 반환
    엔티티를 직접 반환하지 않고 (Controller, View) 서비스단에서 DTO 내려줄 예정(결합도 감소)
 */
public class BoardResponse {

    // 게시글 목록 응답DTO
    @Data
    public static class ListDTO {
        private Integer id; // 상세보기 버튼 - 키값
        private String title;
        private String username; // 평탄화 작업! : SSR 설계 시 권장 방법, CSR 설계 시는 계층 구조로 작업한다.
        private String createdAt;

        // 생성자 생성 - 서비스단에서 값을 받아서 ListDTO로 저장시켜야 함
        public ListDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            // 연관 관계 설정 시 - ballpointException 발생할 가능성이 있기때문에 방어적 코드로 작성해줘야 한다.
            if (board.getUser() != null) {
                this.username = board.getUser().getUsername();

            }
            if (board.getCreatedAt() != null) {
                // String으로 변환
                this.createdAt = MyDataUtil.timestampFormat(board.getCreatedAt());
            }
        }

    } // end of ListDTO

    // 게시글 상세 보기 DTO
    @Data
    public static class DetailDTO{

        private Integer id; // 게시글 수정 및 삭제 시 사용하는 키값
        private String title;
        private String content;
        private String username;
        private Integer userId; // User PK - 추후 사용
        private boolean isOwner;

        public DetailDTO(Board board){
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            // 연관 관계 설정 시 - Null-pointException 발생할 가능성이 있기때문에 방어적 코드로 작성해줘야 한다.
            if (board.getUser() != null) {
                this.username = board.getUser().getUsername();
                this.userId = board.getUser().getId();
            }
        }

        // 소유자 확인
        public boolean checkIsOwner(Integer sessionUserId){
            if (sessionUserId == null){
                return false;
            }
            if (sessionUserId.equals(this.userId)){
                return true;
            }else {
                return false;
            }
        }

    } // end of detailDTO


} // end of outer class
