package io.vitalir.vitalirspring.features.appointment;

import io.vitalir.vitalirspring.features.appointment.domain.AppointmentRepository;
import io.vitalir.vitalirspring.features.appointment.domain.AppointmentService;
import io.vitalir.vitalirspring.features.appointment.domain.AppointmentServiceImpl;
import io.vitalir.vitalirspring.features.user.domain.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppointmentConfig {

    @Bean
    public AppointmentService appointmentService(
            UserRepository userRepository,
            AppointmentRepository appointmentRepository
    ) {
        return new AppointmentServiceImpl(userRepository, appointmentRepository);
    }
}
