package io.vitalir.vitalirspring.features.user.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.vitalir.vitalirspring.common.HttpMethods;
import io.vitalir.vitalirspring.features.user.domain.model.User;
import org.springframework.http.MediaType;
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
    ResponseEntity<User> getUserByEmail(String email);

    @Operation(
            method = HttpMethods.GET,
            summary = "Получить пользователя по его id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful response",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = User.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "User not found"
                    )
            }
    )
    ResponseEntity<User> getUserById(long id);
}
