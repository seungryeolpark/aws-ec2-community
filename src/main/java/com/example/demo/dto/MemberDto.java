package com.example.demo.dto;

import com.example.demo.entity.Authority;
import com.example.demo.entity.Member;
import com.example.demo.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class MemberDto {

    @Data
    public static class get {
        private String email;
        private String nickname;
        private String password;
    }

    @Data
    public static class signup {
        @NotBlank
        @Email
        private String email;

        @NotBlank
        private String emailAuth;

        @NotBlank
        private String nickname;

        @NotBlank
        private String password;

        @NotBlank
        private String confirmPassword;
        private Role role = Role.ROLE_USER; // 회원가입은 무조건 USER 권한만

        // 객체변환 메서드
        public Member convertToMember() {
            return Member.builder()
                    .email(this.getEmail())
                    .password(this.getPassword())
                    .nickname(this.getNickname())
                    .build();
        }

        public Authority convertToAuthority() {
            return Authority.builder()
                    .role(this.getRole())
                    .build();
        }
    }
}
