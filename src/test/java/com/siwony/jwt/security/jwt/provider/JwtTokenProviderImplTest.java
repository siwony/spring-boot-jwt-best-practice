package com.siwony.jwt.security.jwt.provider;

import com.siwony.jwt.member.Member;
import com.siwony.jwt.security.model.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderImplTest {

    private final String jwtSecret = "testSecrettestSecrettestSecrettestSecrettestSecrettestSecrettestSecrettestSecrettestSecrettestSecrettestSecrettestSecret";
    private final long accessTokenExpiryInSec = 1;
    private final long refreshTokenExpiryInSec = 2;

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp(){
        this.jwtTokenProvider = new JwtTokenProviderImpl(jwtSecret, accessTokenExpiryInSec, refreshTokenExpiryInSec);
    }

    @Test
    void accessToken생성_후_email추출(){
        final CustomUserDetails customUserDetails = stubCustomUserDetails();
        final String accessToken = jwtTokenProvider.createAccessToken(customUserDetails);
        final String email = jwtTokenProvider.getMemberEmailFromAccessToken(accessToken);

        assertNotNull(accessToken);
        assertEquals(customUserDetails.getUsername(), email);
    }

    @Test
    void refreshToken생성_후_email이_추출안됨(){
        final String refreshToken = jwtTokenProvider.createRefreshToken();
        final String email = jwtTokenProvider.getMemberEmailFromAccessToken(refreshToken);

        assertNotNull(refreshToken);
        assertNull(email);
    }


    @Test
    void getAccessTokenExpiryDurationInMsTest(){
        assertEquals(this.accessTokenExpiryInSec * 1000, jwtTokenProvider.getAccessTokenExpiryDurationInMs());
    }

    @Test
    void getRefreshTokenTokenExpiryDurationInMsTest(){
        assertEquals(this.refreshTokenExpiryInSec * 1000, jwtTokenProvider.getRefreshTokenExpiryDurationInMs());
    }

    private CustomUserDetails stubCustomUserDetails(){
        Member member = Member.builder()
                .memberIdx(1L)
                .email("siwon@email.io")
                .build();
        return new CustomUserDetails(member);
    }
}