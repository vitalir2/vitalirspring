package io.vitalir.vitalirspring.features.user.presentation.registration;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.vitalir.vitalirspring.common.HttpMethods;
import org.springframework.http.ResponseEntity;

public interface RegistrationApi {

    @Operation(
            method = HttpMethods.POST,
            summary = "Регистрация пользователя по почте и паролю"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful registration"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Credentials validation failed"
                    )
            }
    )
    ResponseEntity<?> register(RegistrationRequest registrationRequest);
}
