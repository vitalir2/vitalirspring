package io.vitalir.vitalirspring.features.user.domain;

import io.vitalir.vitalirspring.features.user.domain.model.User;
import io.vitalir.vitalirspring.security.JwtProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    public UserServiceImpl(UserRepository userRepository, JwtProvider jwtProvider, UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Override
    public Optional<String> login(String email, String password) {
        var optionalUser = userRepository.getUserByEmail(email);
        if (optionalUser.isPresent()) {
            var user = optionalUser.get();
            userDetailsService.loadUserByUsername(email);
            var jwt = jwtProvider.generateToken(user.getEmail(), user.getRole());
            return Optional.of(jwt);
        }
        return Optional.empty();
    }

    @Override
    public boolean register(String email, String password) {
        return false;
    }
}
