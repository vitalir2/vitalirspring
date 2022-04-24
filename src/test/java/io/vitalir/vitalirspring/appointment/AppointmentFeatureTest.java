package io.vitalir.vitalirspring.appointment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.vitalir.vitalirspring.common.JavaDateProvider;
import io.vitalir.vitalirspring.features.appointment.domain.Appointment;
import io.vitalir.vitalirspring.features.appointment.domain.request.AddAppointmentRequest;
import io.vitalir.vitalirspring.features.appointment.domain.request.ChangeAppointmentRequest;
import io.vitalir.vitalirspring.features.doctors.domain.Doctor;
import io.vitalir.vitalirspring.features.doctors.domain.MedicalSpecialty;
import io.vitalir.vitalirspring.features.user.domain.model.Role;
import io.vitalir.vitalirspring.features.user.domain.model.User;
import io.vitalir.vitalirspring.security.jwt.JwtProvider;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public class AppointmentFeatureTest {

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

    protected static final long USER_ID = USER.getId();

    protected static final String BEARER_TOKEN = JWT_PROVIDER.generateToken(USER.getEmail(), USER.getRole());

    protected static final String BEARER_HEADER = "Bearer " + BEARER_TOKEN;

    protected static final AddAppointmentRequest ADD_APPOINTMENT_REQUEST = new AddAppointmentRequest(
            2,
            LocalDateTime.now(),
            1000 * 60 * 15,
            "A description"
    );
    protected static final ChangeAppointmentRequest CHANGE_APPOINTMENT_REQUEST = new ChangeAppointmentRequest(
            APPOINTMENT_ID,
            DOCTOR_ID,
            LocalDateTime.now(),
            1000 * 60,
            "String"
    );

    protected static final Doctor DOCTOR = new Doctor("", Set.of(MedicalSpecialty.ENDOCRINOLOGY));

    protected static final Appointment FIRST_APPOINTMENT = new Appointment(
            0L,
            DOCTOR,
            USER,
            "fid",
            LocalDateTime.of(2022, 3, 23, 14, 30),
            15
    );

    protected static final Appointment SECOND_APPOINTMENT = new Appointment(
            2L,
            DOCTOR,
            USER,
            "fid",
            LocalDateTime.of(2022, 2, 26, 15, 50),
            15
    );

    protected static final User USER_WITH_APPOINTMENTS = new User(
            "Hekker",
            "hackyou",
            Role.USER,
            Set.of(FIRST_APPOINTMENT, SECOND_APPOINTMENT)
    );
}
