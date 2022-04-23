package io.vitalir.vitalirspring.features.doctors.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.vitalir.vitalirspring.common.HttpMethods;
import io.vitalir.vitalirspring.features.doctors.domain.Doctor;
import io.vitalir.vitalirspring.features.doctors.domain.MedicalSpecialty;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DoctorApi {

    @Operation(
            method = HttpMethods.GET,
            summary = "Получение всех докторов в системе",
            parameters = {
                    @Parameter(
                            name = "specialty",
                            in = ParameterIn.QUERY,
                            description = "Фильтрация по специальности"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "A successful response"
                    )
            }
    )
    ResponseEntity<List<Doctor>> getDoctors(
            MedicalSpecialty medicalSpecialty
    );

    @Operation(
            method = HttpMethods.GET,
            summary = "Получить доктора по его id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful response"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Doctor is not found"
                    )
            }
    )
    ResponseEntity<Doctor> getDoctorById(long id);

    @Operation(
            method = HttpMethods.POST,
            summary = "Добавить нового доктора в систему",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Successfully created"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid doctor model"
                    )
            }
    )
    ResponseEntity<Long> addDoctor(Doctor doctor);

    @Operation(
            method = HttpMethods.DELETE,
            summary = "Удалить доктора из системы по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful deleted"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Doctor was not found"
                    )
            }
    )
    ResponseEntity<Doctor> removeDoctorById(long id);

    @Operation(
            method = HttpMethods.GET,
            summary = "Поменять существующего доктора с таким же id",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Successful changed"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Doctor with this id does not exist or invalid doctor model"
                    )
            }
    )
    ResponseEntity<Long> changeDoctor(Doctor changedDoctor);
}
