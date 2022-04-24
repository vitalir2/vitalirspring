package io.vitalir.vitalirspring.features.appointment.domain.request;

import java.time.LocalDateTime;

public record ChangeAppointmentRequest(
        long appointmentId,
        long doctorId,
        LocalDateTime date,
        long duration,
        String description
) {
}
