package com.siwony.jwt.member.service;

import com.siwony.jwt.member.Member;
import com.siwony.jwt.member.dto.MemberDto;
import com.siwony.jwt.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 회원 서비스
 */
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원을 생성한다.
     * @return DB에 저장된 {@link Member}
     */
    @Override
    public Member create(final MemberDto.Join joinDto) {
        Member joinMember = joinDto.toEntity(passwordEncoder.encode(joinDto.getPassword()));
        return memberRepository.save(joinMember);
    }

    /**
     * 로그인을 수행한다.
     * @return 회원의 access/refresh token을 가지고 있는 {@link MemberDto.Credential}
     */
    @Override
    public MemberDto.Credential login(final MemberDto.Login loginDto) {
        final String email = loginDto.getEmail();
        final String password = passwordEncoder.encode(loginDto.getPassword());
        memberRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect email or password"));

        return new MemberDto.Credential(null, null); //TODO access/refresh token 생성후 반환 할 예정
    }
}
