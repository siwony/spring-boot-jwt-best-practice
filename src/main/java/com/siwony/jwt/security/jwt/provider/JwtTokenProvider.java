package com.siwony.jwt.security.jwt.provider;

import com.siwony.jwt.security.model.CustomUserDetails;

public interface JwtTokenProvider {

    String createAccessToken(CustomUserDetails customUserDetails);

    String createRefreshToken();

    String getMemberEmailFromJwt(String accessToken);

    long getExpiryDuration();
}
