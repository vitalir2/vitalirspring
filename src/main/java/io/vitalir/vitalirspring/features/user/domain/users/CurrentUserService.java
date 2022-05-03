package io.vitalir.vitalirspring.features.user.domain.users;

import io.vitalir.vitalirspring.features.user.domain.model.User;

import java.util.Optional;

public interface CurrentUserService {

    Optional<User> getCurrentUser();

    Optional<Long> getCurrentUserId();
}
