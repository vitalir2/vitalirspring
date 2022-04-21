package io.vitalir.vitalirspring.security.jwt;

public class JwtConstants {

    public static final String BEARER_PREFIX = "Bearer ";
    public static final String SUBJECT = "User Details";
    public static final String CLAIM = "email";
    public static final String CLAIM_AUTHORITIES = "authorities";
    public static final String APPLICATION_ISSUER = "VITALIRSPRING";
    public static final long EXPIRATION_TIME = 15 * 60 * 1_000;
}
