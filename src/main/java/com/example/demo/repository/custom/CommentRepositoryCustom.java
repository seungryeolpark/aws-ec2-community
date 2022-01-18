package com.example.demo.repository.custom;

import com.example.demo.entity.Board;
import com.example.demo.entity.Comment;

import java.util.List;

public interface CommentRepositoryCustom {

    List<Comment> getCommentList(int id, int limit, Board board);
}
