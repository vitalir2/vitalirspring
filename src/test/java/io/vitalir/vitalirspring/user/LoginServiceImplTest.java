package io.vitalir.vitalirspring.user;

import io.vitalir.vitalirspring.features.user.domain.login.LoginService;
import io.vitalir.vitalirspring.features.user.domain.login.LoginServiceImpl;
import io.vitalir.vitalirspring.features.user.domain.users.UserRepository;
import io.vitalir.vitalirspring.features.user.domain.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class LoginServiceImplTest extends UserFeatureTest {

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
        given(userRepository.getUserByEmail(VALID_EMAIL)).willReturn(Optional.of(VALID_USER));
        var expectedToken = jwtProvider.generateToken(VALID_EMAIL, Role.USER);

        var result = loginService.login(VALID_EMAIL, VALID_PASSWORD);

        assertTrue(result.isPresent());
        var loginResult = result.get();
        assertEquals(expectedToken, loginResult.jwt());
        verify(userDetailsService).loadUserByUsername(VALID_EMAIL);
    }

    @Test
    public void whenLoginWithNotExistingCredentials_returnEmpty() {
        given(userRepository.getUserByEmail(VALID_EMAIL)).willReturn(Optional.empty());

        var result = loginService.login(VALID_EMAIL, VALID_PASSWORD);

        assertTrue(result.isEmpty());
    }

    @Test
    public void whenLoginWithInvalidPassword_returnEmpty() {
        given(userRepository.getUserByEmail(VALID_EMAIL)).willReturn(Optional.of(VALID_USER));
        var invalidPassword = "kek";

        var result = loginService.login(VALID_EMAIL, invalidPassword);

        assertTrue(result.isEmpty());
    }
}
