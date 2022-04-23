package io.vitalir.vitalirspring.features.appointment.presentation;

import io.vitalir.vitalirspring.common.HttpEndpoints;
import io.vitalir.vitalirspring.features.appointment.domain.Appointment;
import io.vitalir.vitalirspring.features.appointment.domain.AppointmentService;
import io.vitalir.vitalirspring.features.appointment.domain.IllegalUserIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public ResponseEntity<Appointment> removeAppointmentByIds(long userId, long appointmentId) {
        return null;
    }

    @Override
    public ResponseEntity<Long> addAppointmentByIds(long userId, long doctorId, Appointment appointment) {
        return null;
    }

    @Override
    public ResponseEntity<Long> changeAppointmentByIds(long userId, Appointment appointment) {
        return null;
    }

    @ExceptionHandler(value = IllegalUserIdException.class)
    ResponseEntity<IllegalUserIdException> handleIllegalUserId(IllegalUserIdException exception) {
        return ResponseEntity.badRequest().body(exception);
    }
}
