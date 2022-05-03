package io.vitalir.vitalirspring.common.constants;

import java.util.List;

public class HttpEndpoints {

    private HttpEndpoints() {

    }

    public static final List<String> SWAGGER_ENDPOINTS = List.of(
            "/swagger-ui/**",
            "/v3/api-docs/**"
    );

    public static final String APPOINTMENT_ENDPOINT = "/api/v1/appointments/";

    public static final String SPRING_ACTUATOR_BASE = "/actuator/";

    public static final String SPRING_ACTUATOR_PATTERN = SPRING_ACTUATOR_BASE + "**";

    public static final String SPRING_ACTUATOR_HEALTH = SPRING_ACTUATOR_BASE + "health";

    public static final String SERVICES_ENDPOINT = "/api/v1/services/";

    public static final String DOCTORS_ENDPOINT = "/api/v1/doctors/";

    public static final String USERS_ENDPOINT = "/api/v1/users/";

    public static final String REGISTER_ENDPOINT = "/api/v1/register/";

    public static final String AUTH_ENDPOINT = "/api/v1/auth/";
}
