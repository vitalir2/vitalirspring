package io.vitalir.vitalirspring.user;

import io.vitalir.vitalirspring.features.user.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    private static final String EMAIL = "g@gmail.com";
    private static final String PASSWORD = "1234";
    private static final User VALID_USER = new User(EMAIL, PASSWORD, Role.ADMIN);

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    public void init() {
        userService = new UserServiceImpl(userRepository);
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
}
