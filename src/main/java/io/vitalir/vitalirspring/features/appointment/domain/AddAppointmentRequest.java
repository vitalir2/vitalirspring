package io.vitalir.vitalirspring.features.appointment.domain;

import java.time.LocalDate;

public record AddAppointmentRequest(
        long userId,
        long doctorId,
        LocalDate date,
        long duration,
        String description
) {

}
