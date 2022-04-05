package io.vitalir.vitalirspring.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.util.Set;

public interface ServiceApi {
    @Operation(
            method = "GET",
            summary = "Предоставляет все доступные услуги поликлиники"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response", content = {
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
            })
    })
    ResponseEntity<Set<Service>> getServices();
}
