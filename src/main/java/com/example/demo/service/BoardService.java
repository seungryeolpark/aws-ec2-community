package com.example.demo.service;

import com.example.demo.dto.BoardDto;
import com.example.demo.entity.Board;
import com.example.demo.entity.Member;
import com.example.demo.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public Long save(BoardDto.post postBoardDto, Member member, String id) {
        String email = (Optional.ofNullable(member).isEmpty()) ? null : member.getEmail();

        if (Optional.ofNullable(member).isPresent()) postBoardDto.setNickname(member.getNickname());

        Board board = Board.builder()
                .email(email)
                .title(postBoardDto.getTitle())
                .uuid(id)
                .content(postBoardDto.getContent())
                .nickname(postBoardDto.getNickname())
                .password(postBoardDto.getPassword())
                .createTime(LocalDateTime.now())
                .member(member)
                .build();

        boardRepository.save(board);

        return board.getId();
    }

    public Board findById(Long id) {
        return boardRepository.findById(id).orElse(null);
    }

    public Page<BoardDto.getQuery> getBoardList(Pageable pageable) {
        return boardRepository.getBoardList(pageable);
    }

    @Transactional
    public void putBoard(BoardDto.post postBoardDto, Board board) {
        board.setTitle(postBoardDto.getTitle());
        board.setContent(postBoardDto.getContent());

        if (StringUtils.isEmpty(board.getEmail())) {
            board.setNickname(postBoardDto.getNickname());
        }
    }

    @Transactional
    public void deleteBoard(Board board) {
        boardRepository.deleteById(board.getId());
    }
}
