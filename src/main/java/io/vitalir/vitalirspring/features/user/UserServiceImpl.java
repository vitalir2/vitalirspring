package io.vitalir.vitalirspring.features.user;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public Optional<User> getUserByEmail(String email) {
        return Optional.empty();
    }
}
