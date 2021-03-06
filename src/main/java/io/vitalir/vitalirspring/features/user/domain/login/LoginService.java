package io.vitalir.vitalirspring.features.user.domain.login;

import io.vitalir.vitalirspring.features.user.domain.model.LoginResult;

import java.util.Optional;

public interface LoginService {
    Optional<LoginResult> login(String email, String password);
}
