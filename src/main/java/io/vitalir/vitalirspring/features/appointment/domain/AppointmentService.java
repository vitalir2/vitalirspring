package io.vitalir.vitalirspring.features.appointment.domain;

import io.vitalir.vitalirspring.features.appointment.domain.request.AddAppointmentRequest;
import io.vitalir.vitalirspring.features.appointment.domain.request.ChangeAppointmentRequest;

import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    List<Appointment> getAppointmentsByUserId(long userId);

    Optional<Appointment> removeAppointmentByIds(long userId, long appointmentId);

    long addAppointment(AddAppointmentRequest request);

    long changeAppointment(long userId, ChangeAppointmentRequest request);
}
