package com.example.demo.repository.impl;

import com.example.demo.dto.BoardDto;
import com.example.demo.dto.board.QBoardDto_getQuery;
import com.example.demo.entity.Board;
import com.example.demo.repository.custom.BoardRepositoryCustom;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.example.demo.entity.QBoard.*;

@RequiredArgsConstructor
@Slf4j
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<BoardDto.getQuery> getBoardList(Pageable pageable) {

        List<BoardDto.getQuery> content = Query_getBoardList(pageable);

        JPAQuery<Board> countQuery = Query_getBoardListCount();

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    private JPAQuery<Board> Query_getBoardListCount() {
        return queryFactory
                .select(board)
                .from(board);
    }

    private List<BoardDto.getQuery> Query_getBoardList(Pageable pageable) {
        return queryFactory
                .select(new QBoardDto_getQuery(
                        board.id,
                        board.title,
                        board.nickname,
                        board.createTime))
                .from(board)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(board.id.desc())
                .fetch();
    }
}
