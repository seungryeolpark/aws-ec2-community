package com.example.demo.service;

import com.example.demo.dto.MemberDto;
import com.example.demo.entity.Authority;
import com.example.demo.entity.Member;
import com.example.demo.entity.Member_Authority;
import com.example.demo.repository.AuthorityRepository;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;

    @Value("${Role.User}")
    private String ROLE_USER;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        member.setRoles();

        return member;
    }

    public Member findById(Long id) throws Exception {
        return memberRepository.findById(id).orElseThrow(() -> new Exception("이메일을 찾을 수 없습니다"));
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElse(null);
    }

    @Transactional
    public void putNickname(Member member, String nickname) {
        member.setNickname(nickname);
    }

    @Transactional
    public void putPassword(Member member, String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        member.setPassword(encoder.encode(password));
    }

    @Transactional
    public void deleteMember(Member member) {
        memberRepository.delete(member);
    }

    /**
     * 회원정보 저장
     *
     * @param  signupMemberDto 회원정보가 들어있는 DTO
     * @return 저장되는 회원의 PK
     */
    @Transactional
    public Long save(MemberDto.signup signupMemberDto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        signupMemberDto.setPassword(encoder.encode(signupMemberDto.getPassword()));

        // 아이디 저장
        Member member = signupMemberDto.convertToMember();
        memberRepository.save(member);

        // 권한 저장
        Authority authority = signupMemberDto.convertToAuthority();
        authorityRepository.save(authority);

        // 관계 설정
        Member_Authority memberAuthority = Member_Authority.builder()
                .member(member).authority(authority).build();

        member.setMemberAuthorities(memberAuthority);

        return member.getId();
    }
}
