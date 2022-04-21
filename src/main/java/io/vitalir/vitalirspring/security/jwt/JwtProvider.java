package io.vitalir.vitalirspring.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.vitalir.vitalirspring.common.DateProvider;
import io.vitalir.vitalirspring.features.user.domain.model.Role;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static io.vitalir.vitalirspring.security.jwt.JwtConstants.*;

@Component
public class JwtProvider {

    @NonNull
    private final String secret;

    @NonNull
    private final DateProvider dateProvider;

    public JwtProvider(@NonNull String secret, @NonNull DateProvider dateProvider) {
        this.secret = secret;
        this.dateProvider = dateProvider;
    }

    public String generateToken(@NonNull String email, @NonNull Role role) {
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
                .withIssuedAt(dateProvider.getDate())
                .withExpiresAt(dateProvider.getDate(EXPIRATION_TIME, true))
                .withIssuer(APPLICATION_ISSUER)
                .sign(Algorithm.HMAC256(secret));
    }

    public Authentication getAuthentication(@NonNull String token) {
        var decodedJwt = JWT.decode(token);
        var claims = decodedJwt.getClaims();
        var subject = decodedJwt.getSubject();

        var authoritiesClaim = String.join(",", claims.get(CLAIM_AUTHORITIES).asArray(String.class));

        Collection<? extends GrantedAuthority> authorities = authoritiesClaim.isBlank() ? AuthorityUtils.NO_AUTHORITIES
                : AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesClaim);

        User principal = new User(subject, "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }
}
