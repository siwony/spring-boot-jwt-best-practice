package com.siwony.jwt.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siwony.jwt.member.Member;
import com.siwony.jwt.member.dto.MemberDto;
import com.siwony.jwt.member.service.MemberService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Map;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired private MockMvc mvc;

    @Autowired private ObjectMapper mapper;

    @MockBean MemberService memberService;

    @SneakyThrows
    private void printRequestResponseResultHandler(MvcResult result) {
        log.info("request : \n{}", result.getRequest().getContentAsString());
        log.info("response: \n{}", result.getResponse().getContentAsString());
    }

    @SneakyThrows
    private String convertJson(Object obj){
        return mapper.writeValueAsString(obj);
    }

    @Test @DisplayName("회원가입 API 성공 테스트")
    @SneakyThrows
    public void joinApiTest() {
        log.info("=== given ===");
        final var url = "/member";
        final var joinDto = MemberDto.Join.builder()
                .email("siwon103305@gmail.com")
                .password("password")
                .name("정시원")
                .phonenumber("01036293839")
                .build();
        final var passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        given(memberService.create(joinDto))
                .willReturn(joinDto.toEntity(passwordEncoder.encode(joinDto.getPassword()), Member.Role.CLIENT));

        final var requestJson = convertJson(joinDto);
        final var responseJson = convertJson(
                Map.of("email", joinDto.getEmail())
        );

        log.info("=== when ===");
        final ResultActions actions = mvc.perform(
                post(url)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        log.info("=== then ===");
        actions
                .andExpect(status().isCreated())
                .andExpect(content().json(responseJson))
                .andDo(this::printRequestResponseResultHandler);
    }

    @Test @DisplayName("회원가입 API 성공 테스트")
    @SneakyThrows
    public void loginAPITest(){
        log.info("=== given ===");
        final var url = "/member/login";
        final var loginDto = new MemberDto.Login("siwon103305@gmail.com", "password1234");

        final var credentialDto = new MemberDto.Credential("accessToken", "refreshToken");
        given(memberService.login(loginDto))
                .willReturn(credentialDto);

        final var requestJson = convertJson(loginDto);

        log.info("=== when ===");
        final ResultActions actions = mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        );

        log.info("=== then ===");
        actions
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, credentialDto.getAccessToken()))
                .andExpect(header().string("RefreshToken", credentialDto.getRefreshToken()))
                .andDo(this::printRequestResponseResultHandler);
    }

}