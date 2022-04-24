package io.vitalir.vitalirspring.features.appointment.presentation;

import io.vitalir.vitalirspring.common.HttpEndpoints;
import io.vitalir.vitalirspring.features.appointment.domain.*;
import io.vitalir.vitalirspring.features.appointment.domain.exception.IllegalUserIdException;
import io.vitalir.vitalirspring.features.appointment.domain.exception.InvalidAppointmentIdException;
import io.vitalir.vitalirspring.features.appointment.domain.exception.InvalidDoctorIdException;
import io.vitalir.vitalirspring.features.appointment.domain.request.AddAppointmentRequest;
import io.vitalir.vitalirspring.features.appointment.domain.request.ChangeAppointmentRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(HttpEndpoints.APPOINTMENT_ENDPOINT)
public class AppointmentController implements AppointmentApi {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Override
    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Appointment>> getAppointmentsByUserId(@PathVariable("userId") long userId) {
        var result = appointmentService.getAppointmentsByUserId(userId);
        return ResponseEntity.ok(result);
    }

    @Override
    @DeleteMapping("/{userId}/{appointmentId}")
    public ResponseEntity<Appointment> removeAppointmentByIds(
            @PathVariable long userId,
            @PathVariable long appointmentId
    ) {
        var result = appointmentService.removeAppointmentByIds(userId, appointmentId);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @Override
    @PostMapping
    public ResponseEntity<Long> addAppointmentByIds(@RequestBody AddAppointmentRequest request) {
        var result = appointmentService.addAppointment(request);
        var createdLocation = HttpEndpoints.APPOINTMENT_ENDPOINT + request.userId() +
                '/' + result;
        return ResponseEntity.created(URI.create(createdLocation)).body(result);
    }

    @Override
    @PutMapping(value = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> changeAppointmentByIds(
            @PathVariable long userId,
            @RequestBody ChangeAppointmentRequest request
    ) {
        var result = appointmentService.changeAppointment(userId, request);
        return ResponseEntity.ok(result);
    }

    @ExceptionHandler(
            value = {InvalidDoctorIdException.class, IllegalUserIdException.class, InvalidAppointmentIdException.class}
    )
    ResponseEntity<RuntimeException> handleIllegalUserId(RuntimeException exception) {
        return ResponseEntity.badRequest().body(exception);
    }
}
