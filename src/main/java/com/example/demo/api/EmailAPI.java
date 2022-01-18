package com.example.demo.api;

import com.example.demo.dto.MailDto;
import com.example.demo.entity.Member;
import com.example.demo.service.etc.EmailCertificationService;
import com.example.demo.service.MemberService;
import com.example.demo.service.etc.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RequiredArgsConstructor
@RestController
public class EmailAPI {

    private final EmailCertificationService emailCertificationService;
    private final MailService mailService;
    private final MemberService memberService;

    @Value("${server.path}")
    private String serverPath;

    @PostMapping("/email")
    public void emailCheck(@RequestBody MailDto mailDto) throws Exception {
        if ( emailDuplicateCheck(mailDto.getAddress()) ) throw new Exception("이메일이 중복입니다");
    }

    @PostMapping("/email/auth")
    public void emailAuth(@RequestBody MailDto mailDto) throws Exception {
        String userEmail = mailDto.getAddress();
        if (emailDuplicateCheck(userEmail)) throw new Exception("이메일이 중복입니다");

        String authNo = createAuthNo();

        mailDto.setTitle("Community 인증번호입니다");
        mailDto.setMessage("인증번호는 " + authNo + " 입니다");

        emailCertificationService.save(userEmail, authNo);

        mailService.sendMail(mailDto);
    }

    @PostMapping("/email/findPassword")
    public void emailFindPassword(
            @RequestBody MailDto mailDto) throws Exception {
        String userEmail = mailDto.getAddress();
        if (!emailDuplicateCheck(userEmail)) throw new Exception("이메일이 없습니다");

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodeEmail = encoder.encode(mailDto.getAddress());
        String path = serverPath + "/login/edit/password?e=" + encodeEmail;

        mailDto.setTitle("Community 비밀번호 바꾸는 링크입니다");
        mailDto.setMessage(new StringBuffer()
                .append("<p>아래 링크를 클릭하시면 비밀번호 바꾸는 창으로 이동합니다</p>")
                .append("<a href='" + path + "' target='_blank'>비밀번호 바꾸기</a>")
                .toString());

        emailCertificationService.save(encodeEmail, userEmail);

        mailService.sendMail(mailDto);
    }

    //공통 코드 부분
    private boolean emailDuplicateCheck(String email) {
        Member member = memberService.findByEmail(email);

        if (Optional.ofNullable(member).isPresent()) {
            return true;
        }

        return false;
    }

    private String createAuthNo() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(100000,1000000));
    }

}
