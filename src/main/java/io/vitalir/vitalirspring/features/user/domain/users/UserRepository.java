package io.vitalir.vitalirspring.features.user.domain.users;

import io.vitalir.vitalirspring.features.user.domain.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> getUserByEmail(String email);

    Optional<User> getById(long id);

    User save(User user);

    boolean existsById(long id);
}
