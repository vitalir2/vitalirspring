package io.vitalir.vitalirspring.features.appointment.domain;

public record AddAppointmentRequest(
        long userId,
        long doctorId,
        long date,
        long duration,
        String description
) {

}
