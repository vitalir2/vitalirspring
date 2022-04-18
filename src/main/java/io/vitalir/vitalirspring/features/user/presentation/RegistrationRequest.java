package io.vitalir.vitalirspring.features.user.presentation;

public record RegistrationRequest(
        String email,
        String password
) {

}
