package io.vitalir.vitalirspring.features.user.presentation;

public record LoginRequest(
        String email,
        String password
) {

}
