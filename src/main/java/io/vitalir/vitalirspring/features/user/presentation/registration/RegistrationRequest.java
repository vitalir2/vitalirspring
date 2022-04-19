package io.vitalir.vitalirspring.features.user.presentation.registration;

public record RegistrationRequest(
        String email,
        String password
) {

}
