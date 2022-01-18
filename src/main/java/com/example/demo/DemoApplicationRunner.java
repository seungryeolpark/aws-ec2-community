//package com.example.demo;
//
//import com.example.demo.dto.MemberDto;
//import com.example.demo.entity.Member;
//import com.example.demo.entity.enums.Role;
//import com.example.demo.service.BoardService;
//import com.example.demo.service.CommentService;
//import com.example.demo.service.MemberService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//import org.thymeleaf.util.StringUtils;
//
//import java.util.UUID;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class DemoApplicationRunner implements ApplicationRunner {
//
//    private final MemberService memberService;
//
//    private final BoardService boardService;
//
//    private final CommentService commentService;
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//
//        if (memberService.findByEmail("admin@naver.com") == null) {
//            MemberDto.signup signupMember = new MemberDto.signup();
//            signupMember.setEmail("admin@naver.com");
//            signupMember.setPassword("admin");
//            signupMember.setNickname("admin");
//            signupMember.setRole(Role.ROLE_ADMIN);
//
//            memberService.save(signupMember);
//        }
////        SignupMemberDto signupMember = new SignupMemberDto();
////        signupMember.setEmail("warp125@naver.com");
////        signupMember.setPassword("admin");
////        signupMember.setNickname("admin");
////        signupMember.setRole(Role.ROLE_ADMIN);
////
////        Long memberId = memberService.save(signupMember);
////        Member member = memberService.findById(memberId);
//
////        for (int i = 0; i < 100; i++) {
////            String test = "테스트" + i;
////
////            PostBoardDto postBoard = PostBoardDto.builder()
////                    .title(test)
////                    .content(test)
////                    .password(test)
////                    .build();
////
////            Long boardId = boardService.save(postBoard, member, UUID.randomUUID().toString());
////
////            CommentDto commentDto = new CommentDto();
////
////            commentDto.setContent(test);
////            commentDto.setNickname(test);
////
////            if (i == 99) {
////                for (int j = 0; j < 120; j++) {
////                    commentDto.setContent("댓글" + j);
////                    commentDto.setNickname("댓글" + j);
////                    Long commentId = commentService.save(boardId, commentDto, null, member);
////                    if (j == 50) {
////                        for (int k = 0; k < 10; k++) {
////                            Long commentId2 = commentService.save(boardId, commentDto, commentId, member);
////                            if (k == 3 || k == 7) {
////                                commentService.save(boardId, commentDto, commentId2, member);
////                            }
////                        }
////                    }
////                }
////            }
////        }
//    }
//}
