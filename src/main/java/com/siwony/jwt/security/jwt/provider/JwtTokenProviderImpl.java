package com.siwony.jwt.security.jwt.provider;

import com.siwony.jwt.security.model.CustomUserDetails;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

/**
 * JWT token을 생성한다.
 */
@Component
public class JwtTokenProviderImpl implements JwtTokenProvider{

    private final String secretKey;
    private final long accessTokenExpirationInMs;
    private final long refreshTokenExpirationInMs;

    public JwtTokenProviderImpl(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration.accessToken}") long accessTokenExpirationInSec,
            @Value("${jwt.expiration.refreshToken}") long refreshTokenExpirationInSec
    ){
        this.secretKey = secretKey;
        this.accessTokenExpirationInMs = accessTokenExpirationInSec * 1000;
        this.refreshTokenExpirationInMs = refreshTokenExpirationInSec * 1000;
    }


    private JwtBuilder jwtBuilder(){
        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.secretKey));
        return Jwts.builder()
                .setSubject("https://github.com/siwony/spring-boot-jwt-best-practice")
                .signWith(secretKey, SignatureAlgorithm.HS256);
    }


    @Override
    public String createAccessToken(final CustomUserDetails customUserDetails) {
        final Date date = new Date();
        return jwtBuilder()
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(new Date(date.getTime() + accessTokenExpirationInMs))
                .setId(String.valueOf(customUserDetails.getMember().getMemberIdx()))
                .claim("email", customUserDetails.getUsername())
                .compact();
    }

    @Override
    public String createRefreshToken() {
        return null
    }

    @Override
    public String getMemberEmailFromJwt(String accessToken) {
        return null;
    }

    @Override
    public long getExpiryDuration() {
        return 0;
    }
}
