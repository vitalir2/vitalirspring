package io.vitalir.vitalirspring.features.user.domain.model;

public record LoginResult(
        long userId,
        String jwt
) {

}
