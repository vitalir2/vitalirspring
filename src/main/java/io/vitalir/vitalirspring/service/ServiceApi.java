package io.vitalir.vitalirspring.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
                            description = "Successful response",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            examples = {
                                                    @ExampleObject(
                                                            name = "Successful response",
                                                            value = """
                                                                    [
                                                                    {
                                                                    "title": "Посещение гастроэнтеролога"
                                                                    }, {
                                                                    "title": "Гастроскопия"
                                                                    }
                                                                    ]"""
                                                    )
                                            }
                                    )
                            }
                    )
            })
    ResponseEntity<Set<Service>> getServices();

    @Operation(
            method = "POST",
            summary = "Добавление новой услуги в систему"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Successful adding"),
                    @ApiResponse(responseCode = "400", description = "Invalid service")
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
            summary = "Удалить существующую услугу по её названию"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful removal",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            examples = {
                                                    @ExampleObject(
                                                            name = "Successful response",
                                                            value = """
                                                                    {
                                                                    "title": "Hello, world!"
                                                                    }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Service wasn't found with that title"
                    )
            }
    )
    ResponseEntity<?> removeService(String title);

    @Operation(
            method = "PUT",
            description = "Изменение существующей услуги"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful changed",
                            content = {
                                    @Content(
                                            mediaType = "application/json"
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Service with that key is not found",
                            content = {
                                    @Content(
                                            mediaType = "application/json"
                                    )
                            }
                    )
            }
    )
    ResponseEntity<?> changeService(Service service);
}
