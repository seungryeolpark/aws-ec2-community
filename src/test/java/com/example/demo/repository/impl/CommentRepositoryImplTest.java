package com.example.demo.repository.impl;

import com.example.demo.dto.board.PostBoardDto;
import com.example.demo.dto.comment.CommentDto;
import com.example.demo.entity.Board;
import com.example.demo.entity.Comment;
import com.example.demo.service.BoardService;
import com.example.demo.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentRepositoryImplTest {

//    @Autowired
//    BoardService boardService;
//
//    @Autowired
//    CommentService commentService;
//
//    @BeforeEach
//    public void before() {
//        //test 용 board 저장
//        PostBoardDto postBoardDto = new PostBoardDto();
//
//        postBoardDto.setTitle("board1");
//        postBoardDto.setContent("board1");
//        postBoardDto.setNickname("test1");
//        postBoardDto.setPassword("test1");
//
//        Long boardId1 = boardService.save(postBoardDto, null);
//
//        //test 용 comment 저장
//        for (int i = 0; i < 3; i++) {
//            CommentDto commentDto = new CommentDto();
//
//            commentDto.setContent("comment" + (i + 1));
//            commentDto.setNickname("test1");
//
//            commentService.save(boardId1, commentDto, null, null);
//        }
//    }
//
//    @Test
//    @DisplayName("Querydsl where int == long test")
//    public void test1() throws Exception {
//        //given
//        Board board = boardService.findById(1L);
//        //when
//        List<Comment> commentList = commentService.getCommentList(2, 1, board);
//        //then
//        assertThat(commentList.size()).isEqualTo(1);
//        assertThat(commentList.get(0).getId()).isEqualTo(2L);
//    }
}