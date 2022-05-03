package io.vitalir.vitalirspring.features.appointment;

import io.vitalir.vitalirspring.common.IntervalChecker;
import io.vitalir.vitalirspring.features.appointment.domain.AppointmentRepository;
import io.vitalir.vitalirspring.features.appointment.domain.AppointmentService;
import io.vitalir.vitalirspring.features.appointment.domain.AppointmentServiceImpl;
import io.vitalir.vitalirspring.features.doctors.domain.DoctorRepository;
import io.vitalir.vitalirspring.features.service.ServiceRepository;
import io.vitalir.vitalirspring.features.user.domain.users.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppointmentConfig {

    @Bean
    public AppointmentService appointmentService(
            UserRepository userRepository,
            AppointmentRepository appointmentRepository,
            DoctorRepository doctorRepository,
            ServiceRepository serviceRepository,
            IntervalChecker intervalChecker
    ) {
        return new AppointmentServiceImpl(userRepository,
                appointmentRepository,
                doctorRepository,
                serviceRepository,
                intervalChecker
        );
    }
}
