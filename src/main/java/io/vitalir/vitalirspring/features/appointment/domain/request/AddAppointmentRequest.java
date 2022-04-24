package io.vitalir.vitalirspring.features.appointment.domain.request;

import java.time.LocalDate;

public record AddAppointmentRequest(
        long userId,
        long doctorId,
        LocalDate date,
        long duration,
        String description
) {

}
