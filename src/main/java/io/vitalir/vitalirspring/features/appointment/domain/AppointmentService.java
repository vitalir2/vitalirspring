package io.vitalir.vitalirspring.features.appointment.domain;

import io.vitalir.vitalirspring.features.appointment.domain.request.AddAppointmentRequest;
import io.vitalir.vitalirspring.features.appointment.domain.request.ChangeAppointmentRequest;
import io.vitalir.vitalirspring.features.doctors.domain.MedicalSpecialty;
import io.vitalir.vitalirspring.features.user.domain.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    List<Appointment> getAppointmentsByUserId(long userId);

    Optional<Appointment> removeAppointmentByIds(long userId, long appointmentId);

    long addAppointment(User user, AddAppointmentRequest request);

    long changeAppointment(long userId, ChangeAppointmentRequest request);

    List<Appointment> getAppointmentsForCurrentUserByParams(
            User currentUser,
            LocalDateTime start,
            LocalDateTime end,
            MedicalSpecialty medicalSpecialty,
            Long doctorId
    );
}
