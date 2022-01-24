package com.siwony.jwt.security.jwt.validator;

public interface JwtTokenValidator {

    boolean validateToken(String token);

}
