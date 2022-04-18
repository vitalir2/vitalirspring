package io.vitalir.vitalirspring.user;

import io.vitalir.vitalirspring.features.user.domain.UserRepository;
import io.vitalir.vitalirspring.features.user.domain.UserService;
import io.vitalir.vitalirspring.features.user.domain.UserServiceImpl;
import io.vitalir.vitalirspring.features.user.domain.model.Role;
import io.vitalir.vitalirspring.features.user.domain.model.User;
import io.vitalir.vitalirspring.security.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    private static final String EMAIL = "g@gmail.com";
    private static final String PASSWORD = "1234";
    private static final User VALID_USER = new User(EMAIL, PASSWORD, Role.ADMIN);

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDetailsService userDetailsService;

    private final JwtProvider jwtProvider = new JwtProvider("secret");

    private UserService userService;

    @BeforeEach
    public void init() {
        userService = new UserServiceImpl(userRepository, jwtProvider, userDetailsService);
    }

    @Test
    public void whenGetUserByEmailWhichExists_returnIt() {
        given(userRepository.getUserByEmail(EMAIL)).willReturn(Optional.of(VALID_USER));

        var result = userService.getUserByEmail(EMAIL);

        assertThat(result).isEqualTo(Optional.of(VALID_USER));
    }

    @Test
    public void whenGetUserByEmailWhichDoesNotExist_returnEmpty() {
        given(userRepository.getUserByEmail(EMAIL)).willReturn(Optional.empty());

        var result = userService.getUserByEmail(EMAIL);

        assertThat(result).isEqualTo(Optional.empty());
    }

    @Test
    public void whenLoginWithExistingCredentials_returnToken() {
        given(userRepository.getUserByEmail(EMAIL)).willReturn(Optional.of(VALID_USER));
        var expectedToken = jwtProvider.generateToken(EMAIL, Role.ADMIN);

        var result = userService.login(EMAIL, PASSWORD);

        assertTrue(result.isPresent());
        var jwt = result.get();
        assertEquals(expectedToken, jwt);
        verify(userDetailsService).loadUserByUsername(EMAIL);
    }

    @Test
    public void whenLoginWithNotExistingCredentials_returnEmpty() {
        given(userRepository.getUserByEmail(EMAIL)).willReturn(Optional.empty());

        var result = userService.login(EMAIL, PASSWORD);

        assertTrue(result.isEmpty());
    }
}
