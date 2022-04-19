package io.vitalir.vitalirspring.features.user.domain;

public interface RegistrationService {

    /**
     * @param email Email of a new user
     * @param password Password of a new user
     * @return -1 if invalid credentials or exists; otherwise - id of created user
     */
    long register(String email, String password);
}
