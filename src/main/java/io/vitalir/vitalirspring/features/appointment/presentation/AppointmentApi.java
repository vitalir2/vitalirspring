package io.vitalir.vitalirspring.features.appointment.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.vitalir.vitalirspring.common.HttpMethods;
import io.vitalir.vitalirspring.features.appointment.domain.AddAppointmentRequest;
import io.vitalir.vitalirspring.features.appointment.domain.Appointment;
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
                    )
            }
    )
    ResponseEntity<Appointment> removeAppointmentByIds(long userId, long appointmentId);

    // TODO @vitalir: Add handling for dates time
    @Operation(
            method = HttpMethods.POST,
            summary = "Добавить новую запись для пользователя с userId и доктора с doctorId",
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
                    )
            }
    )
    ResponseEntity<Long> addAppointmentByIds(AddAppointmentRequest request);

    ResponseEntity<Long> changeAppointmentByIds(long userId, Appointment appointment);
}
