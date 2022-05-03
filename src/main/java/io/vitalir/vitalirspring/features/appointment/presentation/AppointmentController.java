package io.vitalir.vitalirspring.features.appointment.presentation;

import io.vitalir.vitalirspring.common.constants.HttpEndpoints;
import io.vitalir.vitalirspring.features.appointment.domain.*;
import io.vitalir.vitalirspring.features.appointment.domain.exception.InvalidUserIdException;
import io.vitalir.vitalirspring.features.appointment.domain.exception.InvalidAppointmentIdException;
import io.vitalir.vitalirspring.features.appointment.domain.exception.InvalidDoctorIdException;
import io.vitalir.vitalirspring.features.appointment.domain.request.AddAppointmentRequest;
import io.vitalir.vitalirspring.features.appointment.domain.request.ChangeAppointmentRequest;
import io.vitalir.vitalirspring.features.doctors.domain.MedicalSpecialty;
import io.vitalir.vitalirspring.features.user.domain.CurrentUserService;
import io.vitalir.vitalirspring.features.user.domain.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(HttpEndpoints.APPOINTMENT_ENDPOINT)
@Slf4j
public class AppointmentController implements AppointmentApi {

    private final AppointmentService appointmentService;

    private final CurrentUserService currentUserService;

    public AppointmentController(AppointmentService appointmentService, CurrentUserService currentUserService) {
        this.appointmentService = appointmentService;
        this.currentUserService = currentUserService;
    }

    @Override
    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Appointment>> getAppointmentsByUserId(@PathVariable("userId") long userId) {
        var result = appointmentService.getAppointmentsByUserId(userId);
        return ResponseEntity.ok(result);
    }

    @Override
    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<Appointment> removeAppointmentByIds(
            @PathVariable long appointmentId
    ) {
        var user = getCurrentUserOrThrowUnauthorized();
        var result = appointmentService.removeAppointmentByIds(user.getId(), appointmentId);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @Override
    @PostMapping
    public ResponseEntity<Long> addAppointmentByIds(@RequestBody AddAppointmentRequest request) {
        var currentUser = getCurrentUserOrThrowUnauthorized();
        var result = appointmentService.addAppointment(currentUser, request);
        var createdLocation = HttpEndpoints.APPOINTMENT_ENDPOINT + currentUser.getId() +
                '/' + result;
        return ResponseEntity.created(URI.create(createdLocation)).body(result);
    }

    @Override
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> changeAppointmentByIds(
            @RequestBody ChangeAppointmentRequest request
    ) {
        var userId = currentUserService.getCurrentUserId();
        if (userId.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        var result = appointmentService.changeAppointment(userId.get(), request);
        return ResponseEntity.ok(result);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<Appointment>> getAppointmentsForCurrentUserByParams(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(name = "specialty", required = false) MedicalSpecialty medicalSpecialty,
            @RequestParam(name = "doctorId", required = false) Long doctorId
    ) {
        if (log.isDebugEnabled()) {
            log.debug("Got query params: startDate=" + startDate + ", endDate=" + endDate);
        }
        var currentUser = currentUserService.getCurrentUser();
        if (currentUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        var result = appointmentService.getAppointmentsForCurrentUserByParams(
                currentUser.get(),
                startDate,
                endDate,
                medicalSpecialty,
                doctorId
        );
        return ResponseEntity.ok(result);
    }

    @ExceptionHandler(
            value = {
                    InvalidDoctorIdException.class,
                    InvalidUserIdException.class,
                    InvalidAppointmentIdException.class,
                    IllegalArgumentException.class
            }
    )
    ResponseEntity<RuntimeException> handleException(RuntimeException exception) {
        return ResponseEntity.badRequest().body(exception);
    }

    private User getCurrentUserOrThrowUnauthorized() {
        var currentUser = currentUserService.getCurrentUser();
        if (currentUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return currentUser.get();
    }
}
