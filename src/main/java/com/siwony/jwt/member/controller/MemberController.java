package com.siwony.jwt.member.controller;

import com.siwony.jwt.member.Member;
import com.siwony.jwt.member.dto.MemberDto;
import com.siwony.jwt.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    private ResponseEntity<Map<String, String>> join(@RequestBody MemberDto.Join joinDto){
        Member joinMember = memberService.create(joinDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of(
                        "email", joinMember.getEmail())
                );
    }

    @PostMapping("/login")
    private ResponseEntity<Void> login(@RequestBody MemberDto.Login login){
        MemberDto.Credential credential = memberService.login(login);
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(
                        httpHeaders -> {
                            httpHeaders.add(HttpHeaders.AUTHORIZATION, credential.getAccessToken());
                            httpHeaders.add("RefreshToken", credential.getRefreshToken());
                        }
                ).body(null);
    }
}
