package io.vitalir.vitalirspring.features.appointment.domain.request;

import java.time.LocalDate;

public record ChangeAppointmentRequest(
        long appointmentId,
        long doctorId,
        LocalDate date,
        long duration,
        String description
) {
}
