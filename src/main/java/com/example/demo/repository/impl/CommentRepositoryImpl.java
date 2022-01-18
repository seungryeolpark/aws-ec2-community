package com.example.demo.repository.impl;

import com.example.demo.entity.Board;
import com.example.demo.entity.Comment;
import com.example.demo.entity.QMember;
import com.example.demo.repository.custom.CommentRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.example.demo.entity.QComment.*;
import static com.example.demo.entity.QMember.*;

@Slf4j
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Comment> getCommentList(int id, int limit, Board board) {

        return queryFactory
                .selectFrom(comment)
                .leftJoin(comment.parentComment)
                .fetchJoin()
                .where(
                        comment.board.eq(board),
                        comment.id.goe(id)
                )
                .orderBy(
                        comment.groupId.asc(),
                        comment.id.asc()
                )
                .limit(limit)
                .fetch();
    }
}
