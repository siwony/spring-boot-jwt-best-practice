package com.siwony.jwt.security.jwt.validator;

import com.siwony.jwt.security.jwt.provider.JwtTokenProvider;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenValidatorImpl implements JwtTokenValidator{

    private final String jwtSecret;

    public JwtTokenValidatorImpl(@Value("${jwt.secret}") String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    @Override
    public boolean validateToken(final String token) {
        final JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(JwtTokenProvider.getSecretKey(jwtSecret))
                .build();
        try{
            jwtParser.parseClaimsJws(token);
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
            return false;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
            return false;
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
            return false;
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
            return false;
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
            return false;
        }
        return true;
    }
}
