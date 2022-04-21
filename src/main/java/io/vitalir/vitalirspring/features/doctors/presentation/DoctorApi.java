package io.vitalir.vitalirspring.features.doctors.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.vitalir.vitalirspring.common.HttpMethods;
import io.vitalir.vitalirspring.features.doctors.domain.Doctor;
import io.vitalir.vitalirspring.features.doctors.domain.Specialization;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DoctorApi {

    @Operation(
            method = HttpMethods.GET,
            summary = "Получение всех докторов в системе",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful response"
                    )
            }
    )
    ResponseEntity<List<Doctor>> getAll();

    ResponseEntity<Doctor> getDoctorById();

    ResponseEntity<Long> addDoctor(Doctor doctor);

    ResponseEntity<Doctor> removeDoctorById(long id);

    ResponseEntity<List<Doctor>> getDoctorsBySpecialization(Specialization specialization);

    ResponseEntity<Doctor> changeDoctor(Doctor changedDoctor);
}
