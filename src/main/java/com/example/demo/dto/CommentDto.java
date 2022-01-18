package com.example.demo.dto;

import com.example.demo.entity.Comment;
import com.example.demo.entity.enums.DeleteStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommentDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class get {
        private Long id;
        private Long depth;
        private String nickname;
        private String password;
        private String content;
        private String email;

        private DeleteStatus deleteStatus = DeleteStatus.Stored;

        private List<CommentDto.get> Children = new ArrayList<>();

        public void convertFrom(Comment comment) {
            // comment.getMember()가 null 일 경우 comment.getMember().getEmail()이 에러가 난다.
            // 그렇기 때문에 미리 String email 을 구할 필요가 있다.
            String email = ( Optional.ofNullable(comment.getMember()).isEmpty() ) ? null : comment.getMember().getEmail();

            this.id = comment.getId();
            this.depth = comment.getDepth();
            this.nickname = comment.getNickname();
            this.password = comment.getPassword();
            this.content = comment.getContent();
            this.email = email;
            this.deleteStatus = comment.getDeleteStatus();
        }
    }
}
