package com.siwony.jwt.member.service;

import com.siwony.jwt.member.Member;
import com.siwony.jwt.member.dto.MemberDto;
import com.siwony.jwt.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member create(final MemberDto.Join joinDto) {
        Member joinMember = joinDto.toEntity(passwordEncoder.encode(joinDto.getPassword()));
        return memberRepository.save(joinMember);
    }

    @Override
    public MemberDto.Credential login(final MemberDto.Login loginDto) {
        final String email = loginDto.getEmail();
        final String password = passwordEncoder.encode(loginDto.getPassword());
        memberRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new IllegalArgumentException("email or password incorrect"));

        return new MemberDto.Credential(null, null); //TODO access/refresh token 생성후 반환 할 예정
    }
}
