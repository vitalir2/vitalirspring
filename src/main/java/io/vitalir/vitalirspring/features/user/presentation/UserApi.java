package io.vitalir.vitalirspring.features.user.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.vitalir.vitalirspring.common.HttpMethods;
import org.springframework.http.ResponseEntity;

public interface UserApi {

    @Operation(
            method = HttpMethods.GET,
            summary = "Предоставляет возможность получить пользователя по почте",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful response"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "User not found"
                    )
            }
    )
    ResponseEntity<?> getUserByEmail(String email);
}
