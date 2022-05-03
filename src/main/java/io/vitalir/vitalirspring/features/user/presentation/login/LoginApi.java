package io.vitalir.vitalirspring.features.user.presentation.login;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.vitalir.vitalirspring.common.constants.HttpMethods;
import org.springframework.http.ResponseEntity;

public interface LoginApi {

    @Operation(
        method = HttpMethods.GET,
        summary = "Предоставляет возможность логина в систему"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful login",
                            headers = {
                                    @Header(
                                            name = "Authorization",
                                            required = true
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid user or password"
                    )
            }
    )
    ResponseEntity<Long> login(LoginRequest loginRequest);
}
