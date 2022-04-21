package io.vitalir.vitalirspring.features.user.domain;

import io.vitalir.vitalirspring.features.user.domain.model.User;

import java.util.Optional;

public interface UserService {

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserById(long id);
}
