package io.vitalir.vitalirspring.common;

import java.util.List;

public class HttpEndpoints {

    private HttpEndpoints() {

    }

    public static final List<String> SWAGGER_ENDPOINTS = List.of(
            "/swagger-ui/**",
            "/v3/api-docs/**"
    );
}
