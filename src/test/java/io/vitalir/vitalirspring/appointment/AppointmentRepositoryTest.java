package io.vitalir.vitalirspring.appointment;

import io.vitalir.vitalirspring.features.appointment.domain.Appointment;
import io.vitalir.vitalirspring.features.appointment.domain.AppointmentRepository;
import io.vitalir.vitalirspring.features.user.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class AppointmentRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private AppointmentRepository appointmentRepository;

    private Appointment appointment;

    private User user;

    @BeforeEach
    void initBeforeEach() {
        user = new User("", "");
        appointment = new Appointment(user);
    }

    @Test
    void whenGetAppointmentsByUserId_returnThem() {
        testEntityManager.persist(user);
        testEntityManager.persist(appointment);

        var result = appointmentRepository.getAppointmentsByUserId(user.getId());

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getId()).isEqualTo(appointment.getId());
    }

    @Test
    void whenDeleteAppointmentById_deleteIt() {
        testEntityManager.persist(appointment);

        appointmentRepository.deleteById(appointment.getId());

        assertThat(testEntityManager.find(Appointment.class, appointment.getId())).isNull();
    }
}
