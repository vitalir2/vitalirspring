package io.vitalir.vitalirspring.features.doctors.domain;

import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public List<Doctor> getAll() {
        return doctorRepository.findAll();
    }

    @Override
    public Optional<Doctor> getDoctorById(long id) {
        return doctorRepository.findById(id);
    }

    @Override
    public Optional<Long> addDoctor(Doctor doctor) {
        if (validate(doctor)) {
            return Optional.of(doctorRepository.save(doctor).getId());
        }
        return Optional.empty();
    }

    private boolean validate(Doctor doctor) {
        return doctor.getName() != null;
    }
}
