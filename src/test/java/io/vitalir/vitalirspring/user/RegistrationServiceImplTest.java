package io.vitalir.vitalirspring.user;

import io.vitalir.vitalirspring.features.user.domain.registration.RegistrationService;
import io.vitalir.vitalirspring.features.user.domain.registration.RegistrationServiceImpl;
import io.vitalir.vitalirspring.features.user.domain.users.UserRepository;
import io.vitalir.vitalirspring.features.user.domain.model.Role;
import io.vitalir.vitalirspring.features.user.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceImplTest extends UserFeatureTest {

    @Mock
    private UserRepository userRepository;

    private RegistrationService registrationService;

    @BeforeEach
    public void init() {
        registrationService = new RegistrationServiceImpl(userRepository, passwordEncoder);
    }

    @Test
    public void whenRegisterWithValidAndNotExistingCredentials_returnTrue() {
        given(userRepository.getUserByEmail(VALID_EMAIL)).willReturn(Optional.empty());

        var result = registrationService.register(VALID_EMAIL, VALID_PASSWORD);

        assertNotEquals(-1, result);
        // Should be encrypted
        verify(userRepository, never()).save(new User(VALID_EMAIL, VALID_PASSWORD, Role.USER));
        verify(userRepository).save(new User(VALID_EMAIL, any(), Role.USER));
    }

    @Test
    public void whenRegisterWithExistingCredentials_returnFalse() {
        given(userRepository.getUserByEmail(VALID_EMAIL)).willReturn(Optional.of(VALID_USER));

        var result = registrationService.register(VALID_EMAIL, VALID_PASSWORD);

        assertEquals(-1, result);
        verify(userRepository, never()).save(new User(VALID_EMAIL, any(), Role.USER));
    }
}
