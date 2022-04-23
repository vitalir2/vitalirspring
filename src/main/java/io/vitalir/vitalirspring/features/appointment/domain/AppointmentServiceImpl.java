package io.vitalir.vitalirspring.features.appointment.domain;

import io.vitalir.vitalirspring.features.user.domain.UserRepository;

import java.util.List;
import java.util.Optional;

public class AppointmentServiceImpl implements AppointmentService {

    private final UserRepository userRepository;

    private final AppointmentRepository appointmentRepository;

    public AppointmentServiceImpl(UserRepository userRepository, AppointmentRepository appointmentRepository) {
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public List<Appointment> getAppointmentsByUserId(long userId) {
        if (userRepository.existsById(userId)) {
            return appointmentRepository.getAppointmentsByUserId(userId);
        }
        throw new IllegalUserIdException();
    }

    @Override
    public Optional<Appointment> removeAppointmentByIds(long userId, long appointmentId) {
        if (userRepository.existsById(userId)) {
            var user = userRepository.getById(userId).orElse(null);
            if (user == null) {
                throw new IllegalUserIdException();
            }
            for (Appointment appointment: user.getAppointments()) {
                if (appointment.getId() == appointmentId) {
                    appointmentRepository.deleteById(appointmentId);
                    return Optional.of(appointment);
                }
            }
            return Optional.empty();
        }
        throw new IllegalUserIdException();
    }
}
