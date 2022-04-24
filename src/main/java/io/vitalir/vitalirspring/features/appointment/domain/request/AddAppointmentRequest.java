package io.vitalir.vitalirspring.features.appointment.domain.request;

import java.time.LocalDateTime;

public record AddAppointmentRequest(
        long doctorId,
        long serviceId,
        LocalDateTime date,
        long duration
) {

}
