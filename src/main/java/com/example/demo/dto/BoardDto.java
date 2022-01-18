package com.example.demo.dto;

import com.example.demo.entity.Board;
import com.example.demo.entity.enums.DeleteStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BoardDto {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class delete {
        private String password;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class get {
        private Long id;
        private String email;
        private String title;
        private String content;
        private String nickname;
        private LocalDateTime createTime;

        public void convertFrom(Board board) {
            this.id = board.getId();
            this.email = board.getEmail();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.nickname = board.getNickname();
            this.createTime = board.getCreateTime();
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class post {
        @NotBlank
        private String title;

        @NotBlank
        private String content;

        @NotBlank
        private String uuid;

        @NotBlank
        private String nickname;

        @NotBlank
        private String password;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class image {
        private String id;
        private String url;
        private DeleteStatus deleteStatus;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class page {
        private int total;
        private int page;
    }

    // querydsl 부분
    @Data
    public static class getQuery {
        private Long boardId;
        private String title;
        private String nickname;
        private LocalDateTime createTime;

        @QueryProjection
        public getQuery(Long boardId, String title, String nickname, LocalDateTime createTime) {
            this.boardId = boardId;
            this.title = title;
            this.nickname = nickname;
            this.createTime = createTime;
        }
    }
}
