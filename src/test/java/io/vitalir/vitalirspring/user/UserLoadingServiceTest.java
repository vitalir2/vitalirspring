package io.vitalir.vitalirspring.user;

import io.vitalir.vitalirspring.features.user.domain.users.UserLoadingService;
import io.vitalir.vitalirspring.features.user.domain.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserLoadingServiceTest extends UserFeatureTest {

    @Mock
    private UserRepository userRepository;

    private UserDetailsService userDetailsService;

    @BeforeEach
    public void init() {
        userDetailsService = new UserLoadingService(userRepository);
    }

    @Test
    public void whenGetUserDetailsForExistingUser_returnIt() {
        given(userRepository.getUserByEmail(VALID_EMAIL)).willReturn(Optional.of(VALID_USER));

        var result = userDetailsService.loadUserByUsername(VALID_EMAIL);

        assertThat(result.getUsername()).isEqualTo(VALID_EMAIL);
        assertThat(result.getPassword()).isEqualTo(HASHED_PASSWORD);
    }

    @Test
    public void whenGetUserDetailsForNotExistingUser_returnNull() {
        given(userRepository.getUserByEmail(VALID_EMAIL)).willReturn(Optional.empty());

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(VALID_EMAIL));
    }
}
