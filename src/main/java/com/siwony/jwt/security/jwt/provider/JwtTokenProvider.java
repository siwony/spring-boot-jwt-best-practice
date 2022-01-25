package com.siwony.jwt.security.jwt.provider;

import com.siwony.jwt.security.model.CustomUserDetails;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public interface JwtTokenProvider {

    String createAccessToken(CustomUserDetails customUserDetails);

    String createRefreshToken();

    String getMemberEmailFromAccessToken(String accessToken);

    long getAccessTokenExpiryDurationInMs();

    long getRefreshTokenExpiryDurationInMs();

    static SecretKey getSecretKey(String secretKey){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }
}
