package io.vitalir.vitalirspring.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import io.vitalir.vitalirspring.features.user.domain.model.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;

@Component
@Slf4j
public class JwtProvider {

    private static final String SUBJECT = "User Details";
    private static final String CLAIM = "email";
    private static final String CLAIM_AUTHORITIES = "authorities";
    private static final String APPLICATION_ISSUER = "VITALIRSPRING";
    private static final long EXPIRATION_TIME = 15 * 60 * 1_000;

    private final String secret;

    public JwtProvider(String secret) {
        this.secret = secret;
    }

    public String generateToken(String email, Role role) {
        return JWT.create()
                .withSubject(SUBJECT)
                .withClaim(CLAIM, email)
                .withArrayClaim(
                        CLAIM_AUTHORITIES,
                        role.getAuthorities()
                                .stream()
                                .map(GrantedAuthority::getAuthority)
                                .toArray(String[]::new)
                )
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .withIssuer(APPLICATION_ISSUER)
                .sign(Algorithm.HMAC256(secret));
    }

    public Authentication getAuthentication(String token) {
        var decodedJwt = JWT.decode(token);
        var claims = decodedJwt.getClaims();
        var subject = decodedJwt.getSubject();

        var authoritiesClaim = String.join(",", claims.get("authorities").asArray(String.class));

        Collection<? extends GrantedAuthority> authorities = authoritiesClaim.isBlank() ? AuthorityUtils.NO_AUTHORITIES
                : AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesClaim);

        User principal = new User(subject, "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
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
