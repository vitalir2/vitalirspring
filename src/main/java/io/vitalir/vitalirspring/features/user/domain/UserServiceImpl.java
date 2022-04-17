package io.vitalir.vitalirspring.features.user.domain;

import io.vitalir.vitalirspring.features.user.domain.model.User;
import io.vitalir.vitalirspring.security.JwtProvider;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public UserServiceImpl(UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
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
            var jwt = jwtProvider.generateToken(user.getEmail(), user.getRole());
            return Optional.of(jwt);
        }
        return Optional.empty();
    }
}
