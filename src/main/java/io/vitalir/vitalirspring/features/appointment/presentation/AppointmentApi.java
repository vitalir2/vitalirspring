package io.vitalir.vitalirspring.features.appointment.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.vitalir.vitalirspring.common.HttpMethods;
import io.vitalir.vitalirspring.features.appointment.domain.request.AddAppointmentRequest;
import io.vitalir.vitalirspring.features.appointment.domain.Appointment;
import io.vitalir.vitalirspring.features.appointment.domain.request.ChangeAppointmentRequest;
import io.vitalir.vitalirspring.features.doctors.domain.MedicalSpecialty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
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
            parameters = {
                    @Parameter(
                            name = HttpHeaders.AUTHORIZATION,
                            in = ParameterIn.HEADER,
                            required = true,
                            description = "Bearer token"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "A successful response"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "An invalid userId or appointment does not exist"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    )
            }
    )
    ResponseEntity<Long> changeAppointmentByIds(ChangeAppointmentRequest request);


    @Operation(
            method = HttpMethods.GET,
            summary = "Получить все записи текущего клиента по промежутку времени",
            parameters = {
                    @Parameter(
                            name = "startDate",
                            description = "Начало промежутка времени",
                            required = true,
                            example = "2000-10-31T01:30:00.000-05:00",
                            in = ParameterIn.QUERY
                    ),
                    @Parameter(
                            name = "endDate",
                            description = "Конец промежутка времени",
                            required = true,
                            example = "2000-10-31T01:30:00.000-05:00",
                            in = ParameterIn.QUERY
                    ),
                    @Parameter(
                            name = "specialty",
                            description = "Специальность врача",
                            example = "cardiology",
                            in = ParameterIn.QUERY
                    ),
                    @Parameter(
                            name = "doctorId",
                            description = "Id врача",
                            in = ParameterIn.QUERY
                    ),
                    @Parameter(
                            name = HttpHeaders.AUTHORIZATION,
                            description = "Bearer token",
                            required = true,
                            in = ParameterIn.HEADER
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "A successful response"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid query params"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Invalid Authorization header"
                    )
            }
    )
    ResponseEntity<List<Appointment>> getAppointmentsForCurrentUserByParams(
            LocalDateTime startDate,
            LocalDateTime endDate,
            MedicalSpecialty medicalSpecialty,
            Long doctorId
    );
}
