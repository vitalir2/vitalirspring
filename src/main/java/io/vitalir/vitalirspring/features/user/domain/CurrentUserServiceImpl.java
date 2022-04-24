package io.vitalir.vitalirspring.features.user.domain;

import io.vitalir.vitalirspring.features.user.domain.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CurrentUserServiceImpl implements CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> getCurrentUser() {
        var currentAuth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.getUserByEmail(currentAuth.getName());
    }

    @Override
    public Optional<Long> getCurrentUserId() {
        return getCurrentUser().map(User::getId);
    }
}
