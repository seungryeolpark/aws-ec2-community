package com.example.demo.api;

import com.example.demo.dto.CommentDto;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Member;
import com.example.demo.service.CommentService;
import com.example.demo.service.etc.IpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CommentAPI {

    private final CommentService commentService;
    private final IpService ipService;

    /**
     * 댓글쓰기
     *
     * @param commentDto 클라이언트에서 받아온 댓글 입력 DTO
     * @param member         클라이언트 회원 정보
     * @param boardId        클라이언트가 현재 조회하는 board id
     */
    @PostMapping("/comment/{id}")
    public void postComment(
            @ModelAttribute CommentDto.get commentDto,
            @AuthenticationPrincipal Member member,
            @PathVariable("id") Long boardId,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        //로그인 했을 경우 닉네임 대입 안했을 경우 닉네임에 IP 추가
        getClientIP(member, commentDto, request);
        //comment 저장
        commentService.save(boardId, commentDto, null, member);
        //새로고침
        response.sendRedirect(request.getHeader("REFERER"));
    }

    /**
     * 대댓글 쓰기
     *
     * @param commentDto 클라이언트에서 받아온 댓글 입력 DTO
     * @param member         클라이언트 회원 정보
     * @param boardId        클라이언트가 현재 조회하는 board id
     * @param parentId       클라이언트가 현재 대댓글 달려하는 comment id
     */
    @PostMapping("/comment/{id}/{parentId}")
    public void postNestedComment(
            @ModelAttribute CommentDto.get commentDto,
            @AuthenticationPrincipal Member member,
            @PathVariable("id") Long boardId,
            @PathVariable("parentId") Long parentId,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        //로그인 했을 경우 회원 닉네임 대입 안했을 경우 닉네임에 IP 추가
        getClientIP(member, commentDto, request);
        //comment 저장
        commentService.save(boardId, commentDto, parentId, member);
        response.sendRedirect(request.getHeader("REFERER"));
    }

    @PutMapping("/comment/{commentId}")
    public void putComment(
            @AuthenticationPrincipal Member member,
            @PathVariable("commentId") Long commentId,
            @RequestBody CommentDto.get commentDto) throws Exception {
        Comment comment = commentService.findById(commentId);

        memberCheck(commentDto.getPassword(), member, comment);

        log.info("content={}", commentDto.getContent());
        commentService.putContentByComment(comment, commentDto.getContent());
    }

    /**
     * 댓글 삭제
     * @param member        클라이언트 맴버정보
     * @param commentId     댓글 id
     * @param commentDto    클라이언트에서 AJAX 를 통해 가져온 data
     * @throws Exception
     */
    @DeleteMapping("/comment/{commentId}")
    public void deleteNestedComment(
            @AuthenticationPrincipal Member member,
            @PathVariable("commentId") Long commentId,
            @RequestBody CommentDto.get commentDto) throws Exception {
        Comment comment = commentService.findById(commentId);
        log.info("password={}, password={}", commentDto.getPassword(), comment.getPassword());

        memberCheck(commentDto.getPassword(), member, comment);
        //comment 삭제
        commentService.deleteByComment(comment);
    }

    //API 로직 부분
    private void memberCheck(String password, @AuthenticationPrincipal Member member, Comment comment) throws Exception {
        if (Optional.ofNullable(comment.getMember()).isPresent()) {
            if (!member.getEmail().equals(comment.getMember().getEmail())) throw new Exception("이메일이 맞지 않습니다");
        } else {
            if (!password.equals(comment.getPassword())) throw new Exception("비밀번호가 맞지 않습니다");
        }
    }

    private void getClientIP(@AuthenticationPrincipal Member member,
                             CommentDto.get commentDto,
                             HttpServletRequest request) {
        if (Optional.ofNullable(member).isPresent()) {
            commentDto.setNickname(member.getNickname());
        } else {
            commentDto.setNickname(commentDto.getNickname() + "(" + ipService.getClientIP(request) + ")" );
        }
    }
}
