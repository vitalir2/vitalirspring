package io.vitalir.vitalirspring.user;

import io.vitalir.vitalirspring.features.user.domain.UserRepository;
import io.vitalir.vitalirspring.features.user.domain.UserService;
import io.vitalir.vitalirspring.features.user.domain.UserServiceImpl;
import io.vitalir.vitalirspring.features.user.domain.model.Role;
import io.vitalir.vitalirspring.features.user.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest extends UserFeatureTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    public void init() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    public void whenGetUserByEmailWhichExists_returnIt() {
        given(userRepository.getUserByEmail(VALID_EMAIL)).willReturn(Optional.of(VALID_USER));

        var result = userService.getUserByEmail(VALID_EMAIL);

        assertThat(result).isEqualTo(Optional.of(VALID_USER));
    }

    @Test
    public void whenGetUserByEmailWhichDoesNotExist_returnEmpty() {
        given(userRepository.getUserByEmail(VALID_EMAIL)).willReturn(Optional.empty());

        var result = userService.getUserByEmail(VALID_EMAIL);

        assertThat(result).isEqualTo(Optional.empty());
    }

    @Test
    public void whenGetUserByIdWhichExists_returnIt() {
        given(userRepository.getById(VALID_UID)).willReturn(Optional.of(VALID_USER));

        var result = userService.getUserById(VALID_UID);

        assertThat(result).isEqualTo(Optional.of(VALID_USER));
    }

    @Test
    public void whenGetUserByIdWhichDoesNotExist_returnEmpty() {
        given(userRepository.getById(VALID_UID)).willReturn(Optional.empty());

        var result = userService.getUserById(VALID_UID);

        assertThat(result).isEqualTo(Optional.empty());
    }
}
