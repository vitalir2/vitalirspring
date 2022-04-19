package io.vitalir.vitalirspring.features.user.domain;

import java.util.Optional;

public interface LoginService {
    Optional<String> login(String email, String password);
}
