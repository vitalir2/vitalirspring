package io.vitalir.vitalirspring.features.user.domain;

import io.vitalir.vitalirspring.features.user.domain.model.Role;
import io.vitalir.vitalirspring.features.user.domain.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private static final long EXISTS_OR_INVALID_FORMAT = -1;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public long register(String email, String password) {
        var existingUser = userRepository.getUserByEmail(email);
        if (existingUser.isPresent()) {
            return EXISTS_OR_INVALID_FORMAT;
        }
        var newUser = new User(email, passwordEncoder.encode(password), Role.USER);
        userRepository.save(newUser);
        return newUser.getId();
    }
}
