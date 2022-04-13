package io.vitalir.vitalirspring.user;

import io.vitalir.vitalirspring.features.user.User;
import io.vitalir.vitalirspring.features.user.UserDetailsServiceImpl;
import io.vitalir.vitalirspring.features.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private UserDetailsService userDetailsService;

    @BeforeEach
    public void init() {
        userDetailsService = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    public void whenGetUserDetailsForExistingUser_returnIt() {
        var email = "user@gmail.com";
        var user = new User(email, "1234");
        given(userRepository.getUserByEmail(email)).willReturn(Optional.of(user));

        var result = userDetailsService.loadUserByUsername(user.getEmail());

        assertThat(result.getUsername()).isEqualTo(user.getEmail());
        assertThat(result.getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    public void whenGetUserDetailsForNotExistingUser_returnNull() {
        var email = "user@gmail.com";
        var user = new User(email, "1234");
        given(userRepository.getUserByEmail(email)).willReturn(Optional.empty());

        var result = userDetailsService.loadUserByUsername(user.getEmail());

        assertThat(result).isNull();
    }
}
