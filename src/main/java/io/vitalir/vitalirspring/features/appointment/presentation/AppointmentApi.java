package io.vitalir.vitalirspring.features.appointment.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.vitalir.vitalirspring.common.HttpMethods;
import io.vitalir.vitalirspring.features.appointment.domain.request.AddAppointmentRequest;
import io.vitalir.vitalirspring.features.appointment.domain.Appointment;
import io.vitalir.vitalirspring.features.appointment.domain.request.ChangeAppointmentRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AppointmentApi {

    @Operation(
            method = HttpMethods.GET,
            summary = "Получение всех записей для пользователя",
            parameters = {
                    @Parameter(
                            name = "userId",
                            in = ParameterIn.PATH,
                            description = "id пользователя",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "A successful response"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "An unexpected error"
                    )
            }
    )
    ResponseEntity<List<Appointment>> getAppointmentsByUserId(long userId);

    @Operation(
            method = HttpMethods.DELETE,
            summary = "Удалить запись с appointmentId для пользователя с userId",
            parameters = {
                    @Parameter(
                            name = HttpHeaders.AUTHORIZATION,
                            in = ParameterIn.HEADER,
                            description = "Bearer token",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "A successful response"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "An unexpected error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ResponseEntity.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    )
            }
    )
    ResponseEntity<Appointment> removeAppointmentByIds(long appointmentId);

    // TODO @vitalir: Add handling for dates time
    @Operation(
            method = HttpMethods.POST,
            summary = "Добавить новую запись для пользователя с userId и доктора с doctorId",
            parameters = {
                    @Parameter(
                            name = HttpHeaders.AUTHORIZATION,
                            in = ParameterIn.HEADER,
                            description = "Bearer token",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "A successful response",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = Long.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid userId or doctorId"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    )
            }
    )
    ResponseEntity<Long> addAppointmentByIds(AddAppointmentRequest request);

    @Operation(
            method = HttpMethods.PUT,
            summary = "Обновить существующую запись для пользователя по userId",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "A successful response"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "An invalid userId or appointment does not exist"
                    )
            }
    )
    ResponseEntity<Long> changeAppointmentByIds(long userId, ChangeAppointmentRequest request);
}
