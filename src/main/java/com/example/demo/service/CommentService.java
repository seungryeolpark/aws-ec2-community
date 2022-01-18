package com.example.demo.service;

import com.example.demo.dto.CommentDto;
import com.example.demo.entity.Board;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Member;
import com.example.demo.entity.enums.DeleteStatus;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CommentService {

    private final BoardRepository boardRepository;

    private final CommentRepository commentRepository;

    public Comment findById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Transactional
    public void putContentByComment(Comment comment, String content) {
        comment.setContent(content);
    }

    @Transactional
    public void deleteByComment(Comment comment) {
        if (comment.getChildren().size() != 0) {
            comment.setDeleteStatus(DeleteStatus.Deleted);
        } else {
            commentRepository.deleteById(recursionDeleteComment(comment));
        }
    }

    private Long recursionDeleteComment(Comment comment) {
        Comment parentComment = comment.getParentComment();

        //부모가 1개이고 삭제 상태이면 삭제해도 문제 없기에 부모의 파라미터로 재귀한다.
        //만약 없거나 삭제상태가 아니라면 그 위의 댓글은 삭제할 할 수 없기에 전의 댓글들만 삭제한다.
        if (Optional.ofNullable(parentComment).isPresent()) {
            if (parentComment.getChildren().size() == 1 && parentComment.getDeleteStatus() == DeleteStatus.Deleted) {
                return recursionDeleteComment(parentComment);
            }
        }

        return comment.getId();
    }

    @Transactional
    public Long save(Long boardId, CommentDto.get commentDto, Long parentId, Member member) {
        Optional<Board> board = boardRepository.findById(boardId);
        //boardId 에 board 객체가 없다면 comment 를 저장할 수 없으니 종료한다.
        if (board.isEmpty()) return null;

        Optional<Comment> parentComment = Optional.empty();
        Long groupId = null;
        Long depth = 0L;
        //parentId 가 null 이 아닐 경우 parentComment 를 찾는다.
        //parentComment 가 존재하면 대댓글의 root(댓글)의 id 를 groupId 로 넣는다.
        if (parentId != null) {
            parentComment = commentRepository.findById(parentId);
            if (parentComment.isPresent()) {
                groupId = parentComment.get().getGroupId();
                depth = parentComment.get().getDepth();
                if (depth < 8) depth++;
            }
        }
        //댓글 저장
        Comment comment = Comment.builder()
                .groupId(groupId)
                .depth(depth)
                .nickname(commentDto.getNickname())
                .password(commentDto.getPassword())
                .content(commentDto.getContent())
                .createTime(LocalDateTime.now())
                .deleteStatus(DeleteStatus.Stored)
                .member(member)
                .board(board.get())
                .parentComment(parentComment.orElse(null))
                .build();

        commentRepository.save(comment);

        Long commentId = comment.getId();

        //대댓글이 아닐경우 자신의 id를 groupId에 대입
        if (parentId == null) {
            comment.setGroupId(commentId);
        }

        return commentId;
    }

    public List<Comment> getCommentList(int startId, int limit, Board board) {
        return commentRepository.getCommentList(startId, limit, board);
    }
}
