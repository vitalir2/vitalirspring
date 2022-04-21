package io.vitalir.vitalirspring.features.user.presentation.login;

public record LoginRequest(
        String email,
        String password
) {

}
