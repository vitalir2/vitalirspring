package io.vitalir.vitalirspring.user;

import io.vitalir.vitalirspring.features.user.domain.RegistrationService;
import io.vitalir.vitalirspring.features.user.domain.RegistrationServiceImpl;
import io.vitalir.vitalirspring.features.user.domain.UserRepository;
import io.vitalir.vitalirspring.features.user.domain.model.Role;
import io.vitalir.vitalirspring.features.user.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceImplTest {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private static final String EMAIL = "g@gmail.com";
    private static final String PASSWORD = "1234";
    private static final String HASHED_PASSWORD = passwordEncoder.encode(PASSWORD);
    private static final User VALID_USER = new User(EMAIL, HASHED_PASSWORD, Role.USER);

    @Mock
    private UserRepository userRepository;

    private RegistrationService registrationService;

    @BeforeEach
    public void init() {
        registrationService = new RegistrationServiceImpl(userRepository, passwordEncoder);
    }

    @Test
    public void whenRegisterWithValidAndNotExistingCredentials_returnTrue() {
        given(userRepository.getUserByEmail(EMAIL)).willReturn(Optional.empty());

        var result = registrationService.register(EMAIL, PASSWORD);

        assertTrue(result);
        // Should be encrypted
        verify(userRepository, never()).save(new User(EMAIL, PASSWORD, Role.USER));
        verify(userRepository).save(new User(EMAIL, any(), Role.USER));
    }

    @Test
    public void whenRegisterWithExistingCredentials_returnFalse() {
        given(userRepository.getUserByEmail(EMAIL)).willReturn(Optional.of(VALID_USER));

        var result = registrationService.register(EMAIL, PASSWORD);

        assertFalse(result);
        verify(userRepository, never()).save(new User(EMAIL, any(), Role.USER));
    }
}
