package io.vitalir.vitalirspring.features.service.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.vitalir.vitalirspring.features.service.domain.Service;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.Set;

public interface ServiceApi {
    @Operation(
            method = "GET",
            summary = "Предоставляет все доступные услуги поликлиники"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful response"
                    )
            })
    ResponseEntity<Set<Service>> getServices();

    @Operation(
            method = "POST",
            summary = "Добавление новой услуги в систему",
            parameters = {
                    @Parameter(
                            in = ParameterIn.HEADER,
                            name = HttpHeaders.AUTHORIZATION,
                            example = "Bearer 'JWT'",
                            required = true
                    )
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Successful adding"),
                    @ApiResponse(responseCode = "400", description = "Invalid service"),
                    @ApiResponse(responseCode = "403", description = "Authorization is failed")
            }
    )
    ResponseEntity<?> addService(
            @RequestBody(
                    description = "Новая услуга",
                    required = true
            ) Service service
    );

    @Operation(
            method = "DELETE",
            summary = "Удалить существующую услугу по её названию",
            parameters = {
                    @Parameter(
                            in = ParameterIn.HEADER,
                            name = HttpHeaders.AUTHORIZATION,
                            example = "Bearer 'JWT'",
                            required = true
                    )
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful removal"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Service wasn't found with that title"
                    )
            }
    )
    ResponseEntity<?> removeService(String title);

    @Operation(
            method = "PUT",
            summary = "Изменение существующей услуги",
            parameters = {
                    @Parameter(
                            in = ParameterIn.HEADER,
                            name = HttpHeaders.AUTHORIZATION,
                            example = "Bearer 'JWT'",
                            required = true
                    )
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful changed"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Service with that key is not found"
                    )
            }
    )
    ResponseEntity<?> changeService(Service service);
}
