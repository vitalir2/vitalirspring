package io.vitalir.vitalirspring.user;

import io.vitalir.vitalirspring.features.user.domain.LoginService;
import io.vitalir.vitalirspring.features.user.domain.LoginServiceImpl;
import io.vitalir.vitalirspring.features.user.domain.UserRepository;
import io.vitalir.vitalirspring.features.user.domain.model.Role;
import io.vitalir.vitalirspring.features.user.domain.model.User;
import io.vitalir.vitalirspring.security.jwt.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class LoginServiceImplTest {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private static final String EMAIL = "g@gmail.com";
    private static final String PASSWORD = "1234";
    private static final String HASHED_PASSWORD = passwordEncoder.encode(PASSWORD);
    private static final User VALID_USER = new User(EMAIL, HASHED_PASSWORD, Role.USER);
    private final JwtProvider jwtProvider = new JwtProvider("secret");
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserDetailsService userDetailsService;
    private LoginService loginService;

    @BeforeEach
    public void init() {
        loginService = new LoginServiceImpl(userRepository, userDetailsService, jwtProvider, passwordEncoder);
    }

    @Test
    public void whenLoginWithExistingCredentials_returnToken() {
        given(userRepository.getUserByEmail(EMAIL)).willReturn(Optional.of(VALID_USER));
        var expectedToken = jwtProvider.generateToken(EMAIL, Role.USER);

        var result = loginService.login(EMAIL, PASSWORD);

        assertTrue(result.isPresent());
        var loginResult = result.get();
        assertEquals(expectedToken, loginResult.jwt());
        verify(userDetailsService).loadUserByUsername(EMAIL);
    }

    @Test
    public void whenLoginWithNotExistingCredentials_returnEmpty() {
        given(userRepository.getUserByEmail(EMAIL)).willReturn(Optional.empty());

        var result = loginService.login(EMAIL, PASSWORD);

        assertTrue(result.isEmpty());
    }

    @Test
    public void whenLoginWithInvalidPassword_returnEmpty() {
        given(userRepository.getUserByEmail(EMAIL)).willReturn(Optional.of(VALID_USER));
        var invalidPassword = "kek";

        var result = loginService.login(EMAIL, invalidPassword);

        assertTrue(result.isEmpty());
    }
}
