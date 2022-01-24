package com.siwony.jwt.member.service;

import com.siwony.jwt.member.Member;
import com.siwony.jwt.member.dto.MemberDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Nested
@SpringBootTest
@Transactional
class MemberServiceImplTest {

    @Autowired private MemberService memberService;
    @Autowired private PasswordEncoder passwordEncoder;

    @Test
    public void createTest(){
        log.info("=== given ===");
        final MemberDto.Join joinDto = MemberDto.Join.builder()
                .email("siwony.asdf@gmail.com")
                .password("siwonypassword")
                .name("siwony_")
                .phonenumber("01001000100")
                .build();

        log.info("=== when ===");
        Member createdMember = memberService.create(joinDto);

        log.info("=== then ===");
        assertEquals(joinDto.getEmail(), createdMember.getEmail());
        assertTrue(passwordEncoder.matches(joinDto.getPassword(), createdMember.getPassword()));
        assertEquals(joinDto.getName(), createdMember.getName());
        assertEquals(joinDto.getPhonenumber(), createdMember.getPhonenumber());
        assertEquals(Member.Role.CLIENT, createdMember.getRoles().get(0));
    }

    @Test
    public void loginTest(){
        log.info("=== given ===");
        final String EMAIL = "siwony.asdf@gmail.com";
        final String PASSWORD = "siwonypassword";

        final MemberDto.Join joinDto = MemberDto.Join.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .name("siwony_")
                .phonenumber("01001000100")
                .build();
        memberService.create(joinDto);

        final MemberDto.Login loginDto = new MemberDto.Login(EMAIL, PASSWORD);

        log.info("=== when ===");
        MemberDto.Credential credential = memberService.login(loginDto);

        log.info("=== then ===");
        assertEquals(null, credential.getAccessToken());
        assertEquals(null, credential.getRefreshToken());
    }

    @Test
    public void loginPassword가_틀렸을_때(){
        log.info("=== given ===");
        final String EMAIL = "siwony.asdf@gmail.com";
        final String PASSWORD = "siwonypassword";

        final MemberDto.Join joinDto = MemberDto.Join.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .name("siwony_")
                .phonenumber("01001000100")
                .build();
        memberService.create(joinDto);

        final MemberDto.Login loginDto = new MemberDto.Login(EMAIL, "ㅁㄴㅇㄹㅁㄴㄷㄹ");

        log.info("=== when, then===");
        assertThrows(IllegalArgumentException.class,
                () -> memberService.login(loginDto)
        );


    }

}