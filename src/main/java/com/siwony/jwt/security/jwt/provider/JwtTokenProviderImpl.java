package com.siwony.jwt.security.jwt.provider;

import com.siwony.jwt.security.model.CustomUserDetails;
import io.jsonwebtoken.*;
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

    private SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.secretKey));
    }


    private JwtBuilder jwtBuilder(){
        return Jwts.builder()
                .setSubject("https://github.com/siwony/spring-boot-jwt-best-practice")
                .setIssuedAt(Date.from(Instant.now()))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256);
    }


    @Override
    public String createAccessToken(final CustomUserDetails customUserDetails) {
        final Date date = new Date();
        return jwtBuilder()
                .setExpiration(new Date(date.getTime() + accessTokenExpirationInMs))
                .setId(String.valueOf(customUserDetails.getMember().getMemberIdx()))
                .claim("email", customUserDetails.getUsername())
                .compact();
    }

    @Override
    public String createRefreshToken() {
        final Date date = new Date();
        return jwtBuilder()
                .setExpiration(new Date(date.getTime() + refreshTokenExpirationInMs))
                .compact();
    }

    private JwtParser createTokenParser(){
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build();
    }

    @Override
    public String getMemberEmailFromAccessToken(String accessToken) {
        final Jws<Claims> claimsJws = createTokenParser().parseClaimsJws(accessToken);
        return claimsJws.getBody().get("email", String.class);
    }

    @Override
    public long getExpiryDurationInMs() {
        return 0;
    }
}
