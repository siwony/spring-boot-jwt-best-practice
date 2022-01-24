package com.siwony.jwt.security.jwt.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * JWT token을 생성한다.
 */
@Component
public class JwtTokenProviderImpl {

    private final String secretKey;
    private final long accessTokenExpirationInSec;
    private final long refreshTokenExpirationInSec;

    public JwtTokenProviderImpl(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration.accessToken}") long accessTokenExpirationInSec,
            @Value("${jwt.expiration.refreshToken}") long refreshTokenExpirationInSec
    ){
        this.secretKey = secretKey;
        this.accessTokenExpirationInSec = accessTokenExpirationInSec;
        this.refreshTokenExpirationInSec = refreshTokenExpirationInSec;
    }

}
