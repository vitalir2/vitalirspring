package io.vitalir.vitalirspring.features.user.domain;

public interface RegistrationService {

    boolean register(String email, String password);
}
