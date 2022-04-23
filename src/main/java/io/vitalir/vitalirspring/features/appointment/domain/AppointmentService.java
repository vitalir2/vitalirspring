package io.vitalir.vitalirspring.features.appointment.domain;

import java.util.List;

public interface AppointmentService {
    List<Appointment> getAppointmentsByUserId(long userId);
}
