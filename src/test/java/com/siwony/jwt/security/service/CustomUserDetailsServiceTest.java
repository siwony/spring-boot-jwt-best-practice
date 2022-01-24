package com.siwony.jwt.security.service;

import com.siwony.jwt.member.Member;
import com.siwony.jwt.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@Slf4j
@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock private MemberRepository memberRepository;

    @InjectMocks private CustomUserDetailsService userDetailsService;

    @Test
    public void userDetailsServiceTest(){
        log.info("=== given ===");
        final var userDetailsMember = Member.builder()
                .memberIdx(1L)
                .email("siwon103305@gmail.com")
                .password("password")
                .name("siwony_")
                .phonenumber("01000000000")
                .roles(List.of(Member.Role.CLIENT))
                .build();
        final var roles = userDetailsMember.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.toList());

        given(memberRepository.findByEmail(userDetailsMember.getEmail()))
                .willReturn(Optional.of(userDetailsMember));

        log.info("=== when ===");
        final UserDetails result = userDetailsService.loadUserByUsername(userDetailsMember.getEmail());
        final List<String> resultOfRoles = result.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        log.info("=== then ===");
        assertEquals(userDetailsMember.getEmail(), result.getUsername());
        assertEquals(userDetailsMember.getPassword(), result.getPassword()); // 원래 비즈니스 로직에서는 PasswordEncoder.Matches()를 사용하지만, 이건 이 테스트의 관심사가 아님
        assertEquals(roles, resultOfRoles);
    }

}