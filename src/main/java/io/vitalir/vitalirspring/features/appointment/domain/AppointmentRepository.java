package io.vitalir.vitalirspring.features.appointment.domain;

import java.util.List;

public interface AppointmentRepository {

    List<Appointment> getAppointmentsByUserId(long userId);

    void deleteById(long appointmentId);
}
