package io.vitalir.vitalirspring.features.user.domain;

import io.vitalir.vitalirspring.security.JwtProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public LoginServiceImpl(UserRepository userRepository, UserDetailsService userDetailsService, JwtProvider jwtProvider, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<String> login(String email, String password) {
        var optionalUser = userRepository.getUserByEmail(email);
        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }

        var user = optionalUser.get();
        if (passwordEncoder.matches(password, user.getPassword())) {
            userDetailsService.loadUserByUsername(email);
            var jwt = jwtProvider.generateToken(user.getEmail(), user.getRole());
            return Optional.of(jwt);
        }
        return Optional.empty();
    }
}
