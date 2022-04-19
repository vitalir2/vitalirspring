package io.vitalir.vitalirspring.security.jwt;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtVerifier jwtVerifier;
    private final JwtProvider jwtProvider;

    public JwtFilter(JwtProvider jwtProvider, JwtVerifier jwtVerifier) {
        this.jwtVerifier = jwtVerifier;
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String token = resolveToken(request);
        logger.info("Extracting token from HttpServletRequest: " + token);

        if (token != null && jwtVerifier.validateToken(token)) {
            Authentication auth = jwtProvider.getAuthentication(token);

            if (auth != null && !(auth instanceof AnonymousAuthenticationToken)) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);

    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtConstants.BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
