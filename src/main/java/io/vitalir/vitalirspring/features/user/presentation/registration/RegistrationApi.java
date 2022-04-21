package io.vitalir.vitalirspring.features.user.presentation.registration;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.vitalir.vitalirspring.common.HttpMethods;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public interface RegistrationApi {

    @Operation(
            method = HttpMethods.POST,
            summary = "Регистрация пользователя по почте и паролю"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Successful registration",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = Long.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Credentials validation failed or email already exists",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = Long.class
                                    )
                            )
                    )
            }
    )
    ResponseEntity<Long> register(RegistrationRequest registrationRequest);
}
