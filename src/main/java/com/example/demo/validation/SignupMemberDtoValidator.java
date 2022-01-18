package com.example.demo.validation;

import com.example.demo.dto.MemberDto;
import com.example.demo.entity.Member;
import com.example.demo.service.etc.EmailCertificationService;
import com.example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SignupMemberDtoValidator implements Validator {

    private final MemberService memberService;
    private final EmailCertificationService emailCertificationService;

    @Override
    public boolean supports(Class<?> aClass) {
        return MemberDto.signup.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        MemberDto.signup signupMemberDto = (MemberDto.signup) o;

        //이메일 중복 검증
        Member memberEmail = memberService.findByEmail(signupMemberDto.getEmail());
        if (Optional.ofNullable(memberEmail).isPresent()) {
            errors.rejectValue("emailAuth", "Duplicate");
        }

        //비밀번호, 비밀번호 확인 같은지 검증
        if (!signupMemberDto.getPassword().equals(signupMemberDto.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "required");
        }

        //인증번호 검증
        String emailCertificationNumber = emailCertificationService.findByEmail(signupMemberDto.getEmail());
        if (!signupMemberDto.getEmailAuth().equals(emailCertificationNumber)) {
            errors.rejectValue("emailAuth", "required");
        }
    }
}
