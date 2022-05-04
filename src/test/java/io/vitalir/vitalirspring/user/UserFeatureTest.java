package io.vitalir.vitalirspring.user;

import io.vitalir.vitalirspring.common.time.DateProvider;
import io.vitalir.vitalirspring.common.time.JavaDateProvider;
import io.vitalir.vitalirspring.features.user.domain.model.Role;
import io.vitalir.vitalirspring.features.user.domain.model.User;
import io.vitalir.vitalirspring.security.jwt.JwtProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserFeatureTest {

    protected static final String VALID_EMAIL = "g@gmail.com";
    protected static final String VALID_PASSWORD = "1324";
    protected static final long VALID_UID = 123L;
    protected static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    protected static final DateProvider dateProvider = new JavaDateProvider();
    protected static final String HASHED_PASSWORD = passwordEncoder.encode(VALID_PASSWORD);
    protected static final User VALID_USER = new User(VALID_EMAIL, HASHED_PASSWORD, Role.USER);
    protected static final JwtProvider jwtProvider = new JwtProvider("secret", dateProvider);
}
