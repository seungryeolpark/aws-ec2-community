package com.example.demo.controller;

import com.example.demo.dto.BoardDto;
import com.example.demo.dto.MemberDto;
import com.example.demo.entity.Board;
import com.example.demo.entity.Member;
import com.example.demo.service.etc.EmailCertificationService;
import com.example.demo.service.MemberService;
import com.example.demo.validation.SignupMemberDtoValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/login")
public class LoginController {

    private final MemberService memberService;
    private final SignupMemberDtoValidator signupMemberDtoValidator;
    private final EmailCertificationService emailCertificationService;

    @RequestMapping()
    public String loginPage(Model model) {
        model.addAttribute("errorMessage"); //demo.security.UserLoginFailHandler 로그인 실패시 에러 메시지 저장용
        return "login/login";
    }

    @RequestMapping(value = "/findPassword", method = RequestMethod.GET)
    public String findPassword() {
        return "login/findPassword";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signupPage(Model model) {
        model.addAttribute("signupMemberDto", new MemberDto.signup());
        return "login/signup";
    }

    /**
     * 회원 정보
     */
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public String loginInfo(
            Model model,
            @AuthenticationPrincipal Member member) throws Exception {
        Member m = memberService.findById(member.getId());

        List<BoardDto.get> boardList = convertList(m.getBoardList());
        List<BoardDto.get> latestBoardList = latestList(boardList, 10);

        model.addAttribute("boardDtoList", latestBoardList);
        return "login/info";
    }

    @RequestMapping(value = "/editPassword", method = RequestMethod.GET)
    public String editPassword() {
        return "login/editPassword";
    }

    @RequestMapping(value = "/leave", method = RequestMethod.GET)
    public String leave() { return "login/leave"; }

    /**
     * 회원가입
     */
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signup(
            @Validated @ModelAttribute("signupMemberDto") MemberDto.signup signupMemberDto,
            BindingResult bindingResult) throws Exception {

        signupMemberDtoValidator.validate(signupMemberDto, bindingResult);

        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            return "login/signup";
        }

        //검증 성공
        memberService.save(signupMemberDto);
        return "redirect:/login";
    }

    /**
     * 비밀번호 찾아서 바꾸기
     */
    @RequestMapping(value = "/edit/password", method = RequestMethod.GET)
    public String findPasswordReplace(
            @RequestParam String e,
            Model model) throws Exception {
        String email = emailCertificationService.findByEmail(e);
        memberService.findByEmail(email);

        model.addAttribute("email", e);

        return "login/editPassword";
    }

    //Controller 로직 부분
    //board 리스트 -> boardDto.get 리스트로 바꾸는 메서드
    private List<BoardDto.get> convertList(List<Board> boardList) {
        List<BoardDto.get> result = new ArrayList<>();

        for (Board board : boardList) {
            BoardDto.get tempDto = new BoardDto.get();
            tempDto.convertFrom(board);
            result.add(tempDto);
        }
        return result;
    }
    //boardDto.get 리스트에서 마지막 size 개 boardDto.get 객체 리스트로 가져오는 메서드
    private List<BoardDto.get> latestList(List<BoardDto.get> boardDtoList, int size) {
        List<BoardDto.get> result = new ArrayList<>();

        int index = boardDtoList.size()-1;
        for (int i=index; i>index-size; i--) {
            result.add(boardDtoList.get(i));
        }

        return result;
    }
}
