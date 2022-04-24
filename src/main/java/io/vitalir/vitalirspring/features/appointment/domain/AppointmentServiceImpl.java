package io.vitalir.vitalirspring.features.appointment.domain;

import io.vitalir.vitalirspring.features.appointment.domain.exception.IllegalUserIdException;
import io.vitalir.vitalirspring.features.appointment.domain.exception.InvalidAppointmentIdException;
import io.vitalir.vitalirspring.features.appointment.domain.exception.InvalidDoctorIdException;
import io.vitalir.vitalirspring.features.appointment.domain.request.AddAppointmentRequest;
import io.vitalir.vitalirspring.features.appointment.domain.request.ChangeAppointmentRequest;
import io.vitalir.vitalirspring.features.doctors.domain.DoctorRepository;
import io.vitalir.vitalirspring.features.user.domain.UserRepository;

import java.util.List;
import java.util.Optional;

public class AppointmentServiceImpl implements AppointmentService {

    private final UserRepository userRepository;

    private final AppointmentRepository appointmentRepository;

    private final DoctorRepository doctorRepository;

    public AppointmentServiceImpl(UserRepository userRepository, AppointmentRepository appointmentRepository, DoctorRepository doctorRepository) {
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
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
            var foundAppointment = user.getAppointments()
                    .stream()
                    .filter(appointment -> appointment.getId() == appointmentId)
                    .findFirst();
            if (foundAppointment.isPresent()) {
                appointmentRepository.deleteById(appointmentId);
            }
            return foundAppointment;
        }
        throw new IllegalUserIdException();
    }

    @Override
    public long addAppointment(AddAppointmentRequest request) {
        var user = userRepository.getById(request.userId());
        if (user.isEmpty()) {
            throw new IllegalUserIdException();
        }
        var doctor = doctorRepository.findById(request.doctorId());
        if (doctor.isEmpty()) {
            throw new InvalidDoctorIdException();
        }
        var appointment = new Appointment(
                0,
                doctor.get(),
                user.get(),
                request.description(),
                request.date(),
                request.duration()
        );
        var result = appointmentRepository.save(appointment);
        return result.getId();
    }

    @Override
    public long changeAppointment(long userId, ChangeAppointmentRequest request) {
        var optionalAppointment = getAppointmentsByUserId(userId).stream()
                .filter(appointment -> appointment.getId() == request.appointmentId())
                .findFirst();
        if (optionalAppointment.isEmpty()) {
            throw new InvalidAppointmentIdException();
        }
        var optionalDoctor = doctorRepository.findById(request.doctorId());
        if (optionalDoctor.isEmpty()) {
            throw new InvalidDoctorIdException();
        }
        var newAppointment = new Appointment(
                0,
                optionalDoctor.get(),
                optionalAppointment.get().getUser(),
                request.description(),
                request.date(),
                request.duration()
        );
        appointmentRepository.save(newAppointment);
        return 0;
    }
}
