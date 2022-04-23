package io.vitalir.vitalirspring.features.appointment.domain;

import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    List<Appointment> getAppointmentsByUserId(long userId);

    Optional<Appointment> removeAppointmentByIds(long userId, long appointmentId);

    long addAppointment(AddAppointmentRequest request);
}
