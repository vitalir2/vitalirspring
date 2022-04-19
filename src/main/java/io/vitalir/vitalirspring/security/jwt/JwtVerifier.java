package io.vitalir.vitalirspring.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static io.vitalir.vitalirspring.security.jwt.JwtConstants.APPLICATION_ISSUER;
import static io.vitalir.vitalirspring.security.jwt.JwtConstants.SUBJECT;

@Component
@Slf4j
public class JwtVerifier {

    private final String secret;

    public JwtVerifier(String secret) {
        this.secret = secret;
    }

    public boolean validateToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject(SUBJECT)
                .withIssuer(APPLICATION_ISSUER)
                .build();
        try {
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            log.info("Invalid JWT: " + token);
            return false;
        }
    }
}
