package io.vitalir.vitalirspring.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    private static final String SUBJECT = "User Details";
    private static final String CLAIM = "email";
    private static final String APPLICATION_ISSUER = "VITALIRSPRING";
    private static final long EXPIRATION_TIME = 15 * 60 * 1_000;

    @Value("${jwt_secret}")
    private String secret;

    public String generateToken(String email) {
        return JWT.create()
                .withSubject(SUBJECT)
                .withClaim(CLAIM, email)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .withIssuer(APPLICATION_ISSUER)
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateTokenAndRetrieveSubject(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject(SUBJECT)
                .withIssuer(APPLICATION_ISSUER)
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim(CLAIM).asString();
    }
}
