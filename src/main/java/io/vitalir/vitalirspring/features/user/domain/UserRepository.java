package io.vitalir.vitalirspring.features.user.domain;

import io.vitalir.vitalirspring.features.user.domain.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> getUserByEmail(String email);

    User save(User user);
}
