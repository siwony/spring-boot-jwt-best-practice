package com.siwony.jwt.security.jwt.validator;

import com.siwony.jwt.member.Member;
import com.siwony.jwt.security.jwt.provider.JwtTokenProvider;
import com.siwony.jwt.security.jwt.provider.JwtTokenProviderImpl;
import com.siwony.jwt.security.model.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class JwtTokenValidatorImplTest {

    private final String jwtSecret = "testSecretKeytestSecretKeytestSecretKeytestSecretKeytestSecretKeytestSecretKeytestSecretKeytestSecretKeytestSecretKeytestSecretKey";

    private JwtTokenValidator jwtTokenValidator;

    @BeforeEach
    void setUp(){
        jwtTokenValidator = new JwtTokenValidatorImpl(jwtSecret);
    }

    @Test
    void validationTokenTest(){
        log.info("=== given ===");
        final JwtTokenProvider jwtTokenProvider = new JwtTokenProviderImpl(jwtSecret, 10, 10);
        final CustomUserDetails stubCustomUserDetails = stubCustomUserDetails();
        final String accessToken = jwtTokenProvider.createAccessToken(stubCustomUserDetails);
        final String refreshToken = jwtTokenProvider.createRefreshToken();

        log.info("=== when ===");
        final boolean validateAccessToken = jwtTokenValidator.validateToken(accessToken);
        final boolean validateRefreshToken = jwtTokenValidator.validateToken(refreshToken);

        log.info("=== then ===");
        assertTrue(validateAccessToken);
        assertTrue(validateRefreshToken);
    }

    @Test
    void validationToken_에_만료된_토큰이_들어간다면(){
        log.info("=== given ===");
        final JwtTokenProvider jwtTokenProvider = new JwtTokenProviderImpl(jwtSecret, 0, 0);
        final CustomUserDetails stubCustomUserDetails = stubCustomUserDetails();
        final String accessToken = jwtTokenProvider.createAccessToken(stubCustomUserDetails);
        final String refreshToken = jwtTokenProvider.createRefreshToken();

        log.info("=== when ===");
        final boolean validateAccessToken = jwtTokenValidator.validateToken(accessToken);
        final boolean validateRefreshToken = jwtTokenValidator.validateToken(refreshToken);

        log.info("=== then ===");
        assertFalse(validateAccessToken);
        assertFalse(validateRefreshToken);
    }

    @Test
    void validationToken_에_이상한_문자열_혹은_빈문자열이_들어간다면(){
        log.info("=== given ===");
        final String strangeString = RandomString.make(25);
        final String emptyString = " ";
        final String nullString = null;

        log.info("=== when ===");
        final boolean validateStrangeString = jwtTokenValidator.validateToken(strangeString);
        final boolean validateEmptyString = jwtTokenValidator.validateToken(emptyString);
        final boolean validateNullString = jwtTokenValidator.validateToken(nullString);

        log.info("=== then ===");
        assertFalse(validateStrangeString);
        assertFalse(validateEmptyString);
        assertFalse(validateNullString);
    }

    @Test
    void validationToken_에_잘못된_JWT_토큰이_들어간다면(){
        log.info("=== given ===");
        final JwtTokenProvider jwtTokenProvider = new JwtTokenProviderImpl(jwtSecret, 100, 100);
        final CustomUserDetails stubCustomUserDetails = stubCustomUserDetails();
        final String accessTokenAddAniString = jwtTokenProvider.createAccessToken(stubCustomUserDetails) + RandomString.make(5);
        final String refreshTokenAddAniString = jwtTokenProvider.createRefreshToken() + RandomString.make(5);

        log.info("=== when ===");
        final boolean validateAccessToken = jwtTokenValidator.validateToken(accessTokenAddAniString);
        final boolean validateRefreshToken = jwtTokenValidator.validateToken(refreshTokenAddAniString);

        log.info("=== then ===");
        assertFalse(validateAccessToken);
        assertFalse(validateRefreshToken);
    }

    @Test
    void validationToken_에_지원하지않는_JWT_를_검증한다면(){
        log.info("=== given ===");
        final String hs384JWT = "eyJhbGciOiJIUzM4NCIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.bQTnz6AuMJvmXXQsVPrxeQNvzDkimo7VNXxHeSBfClLufmCVZRUuyTwJF311JHuh";

        log.info("=== when ===");
        final boolean validateUnsupportedJWT = jwtTokenValidator.validateToken(hs384JWT);

        log.info("=== then ===");
        assertFalse(validateUnsupportedJWT);
    }

    private CustomUserDetails stubCustomUserDetails(){
        Member member = Member.builder()
                .memberIdx(1L)
                .email("siwon@email.io")
                .build();
        return new CustomUserDetails(member);
    }

}