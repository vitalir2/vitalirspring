package io.vitalir.vitalirspring.appointment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.vitalir.vitalirspring.common.JavaDateProvider;
import io.vitalir.vitalirspring.features.appointment.domain.Appointment;
import io.vitalir.vitalirspring.features.appointment.domain.request.ChangeAppointmentRequest;
import io.vitalir.vitalirspring.features.user.domain.model.Role;
import io.vitalir.vitalirspring.features.user.domain.model.User;
import io.vitalir.vitalirspring.security.jwt.JwtProvider;

import java.time.LocalDate;

public class AppointmentFeatureTest {

    protected static final long USER_ID = 1;

    protected static final long DOCTOR_ID = 2;

    protected static final Appointment APPOINTMENT = new Appointment();

    protected static final long APPOINTMENT_ID = APPOINTMENT.getId();

    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    protected static final JwtProvider JWT_PROVIDER = new JwtProvider("secret", new JavaDateProvider());

    protected static final String USER_EMAIL = "a@gmail.com";

    protected static final User USER = new User(
            USER_EMAIL,
            "12345",
            Role.USER
    );

    protected static final String BEARER_TOKEN = JWT_PROVIDER.generateToken(USER.getEmail(), USER.getRole());

    protected static final String BEARER_HEADER = "Bearer " + BEARER_TOKEN;

    protected static final ChangeAppointmentRequest CHANGE_APPOINTMENT_REQUEST = new ChangeAppointmentRequest(
            APPOINTMENT_ID,
            DOCTOR_ID,
            LocalDate.now(),
            1000 * 60,
            "String"
    );
}
