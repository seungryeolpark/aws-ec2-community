package com.example.demo.api;

import com.example.demo.dto.MemberDto;
import com.example.demo.entity.Member;
import com.example.demo.service.etc.EmailCertificationService;
import com.example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ProfileAPI {

    private final MemberService memberService;

    private final EmailCertificationService emailCertificationService;

    @PutMapping("/info/nickname")
    public void putNickname(
            @AuthenticationPrincipal Member member,
            @RequestBody MemberDto.get memberDto) throws Exception {
        if (Optional.ofNullable(member).isEmpty()) throw new Exception("지금 로그인 상태가 아닙니다");
        if (memberDto.getNickname().equals("")) throw new Exception("입력한 닉네임이 없습니다");

        Member m = memberService.findById(member.getId());

        memberService.putNickname(m, memberDto.getNickname());
        member.setNickname(memberDto.getNickname());
    }

    @PutMapping("/info/password")
    public void putPassword(
            @AuthenticationPrincipal Member member,
            @RequestBody MemberDto.get memberDto,
            HttpServletRequest request) throws Exception {
        Member m;

        if (memberDto.getPassword().equals("")) throw new Exception("입력한 비밀번호가 없습니다");

        if (Optional.ofNullable(member).isPresent()) m = memberService.findById(member.getId());
        else {
            String email = emailCertificationService.findByEmail(memberDto.getEmail());
            m = memberService.findByEmail(email);
        }

        memberService.putPassword(m, memberDto.getPassword());
        if (Optional.ofNullable(member).isPresent()) request.logout();
    }

    @DeleteMapping("/info")
    public void deleteMember(
            @AuthenticationPrincipal Member member,
            HttpServletRequest request) throws Exception {
        if (Optional.ofNullable(member).isEmpty()) throw new Exception("지금 로그인 상태가 아닙니다");
        Member m = memberService.findById(member.getId());

        memberService.deleteMember(m);
        request.logout();
    }
}
