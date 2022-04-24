package io.vitalir.vitalirspring.features.appointment.domain;

import io.vitalir.vitalirspring.features.appointment.domain.exception.InvalidUserIdException;
import io.vitalir.vitalirspring.features.appointment.domain.exception.InvalidAppointmentIdException;
import io.vitalir.vitalirspring.features.appointment.domain.exception.InvalidDoctorIdException;
import io.vitalir.vitalirspring.features.appointment.domain.request.AddAppointmentRequest;
import io.vitalir.vitalirspring.features.appointment.domain.request.ChangeAppointmentRequest;
import io.vitalir.vitalirspring.features.doctors.domain.DoctorRepository;
import io.vitalir.vitalirspring.features.doctors.domain.MedicalSpecialty;
import io.vitalir.vitalirspring.features.user.domain.UserRepository;
import io.vitalir.vitalirspring.features.user.domain.model.User;

import java.time.LocalDateTime;
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
        throw new InvalidUserIdException();
    }

    @Override
    public Optional<Appointment> removeAppointmentByIds(long userId, long appointmentId) {
        if (userRepository.existsById(userId)) {
            var user = userRepository.getById(userId).orElse(null);
            if (user == null) {
                throw new InvalidUserIdException();
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
        throw new InvalidUserIdException();
    }

    @Override
    public long addAppointment(User user, AddAppointmentRequest request) {
        var doctor = doctorRepository.findById(request.doctorId());
        if (doctor.isEmpty()) {
            throw new InvalidDoctorIdException();
        }
        var appointment = new Appointment(
                0,
                doctor.get(),
                user,
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

    @Override
    public List<Appointment> getAppointmentsInInterval(
            User currentUser,
            LocalDateTime start,
            LocalDateTime end,
            MedicalSpecialty medicalSpecialty
    ) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Date start=" + start + " happens before end=" + end);
        }
        var filteredAppointments = currentUser.getAppointments()
                .stream()
                .filter(appointment -> happensInInterval(appointment, start, end));
        if (medicalSpecialty != null) {
            filteredAppointments = filteredAppointments
                    .filter(appointment -> appointment.getDoctor().getMedicalSpecialties().contains(medicalSpecialty));
        }
        return filteredAppointments.toList();
    }

    private boolean happensInInterval(Appointment appointment, LocalDateTime startDate, LocalDateTime endDate) {
        var appointmentStartDate = appointment.getStartDate();
        var appointmentEndDate = appointmentStartDate.plusMinutes(
                appointment.getDurationMinutes()
        );
        var appointmentStartDateInInterval = appointmentStartDate.isAfter(startDate);
        var appointmentEndDateInInterval = appointmentEndDate.isBefore(endDate);
        return appointmentStartDateInInterval && appointmentEndDateInInterval;
    }
}
