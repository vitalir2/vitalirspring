package io.vitalir.vitalirspring.features.user;

import java.util.Optional;

public interface UserRepository {

    Optional<User> getUserByEmail(String email);
}
