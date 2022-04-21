package io.vitalir.vitalirspring.features.doctors.presentation;

import io.vitalir.vitalirspring.features.doctors.domain.Doctor;
import io.vitalir.vitalirspring.features.doctors.domain.DoctorService;
import io.vitalir.vitalirspring.features.doctors.domain.Specialization;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doctors")
public class DoctorController implements DoctorApi {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @Override
    @GetMapping
    public ResponseEntity<List<Doctor>> getAll() {
        return ResponseEntity.ok(doctorService.getAll());
    }

    @Override
    public ResponseEntity<Doctor> getDoctorById() {
        return null;
    }

    @Override
    public ResponseEntity<Long> addDoctor(Doctor doctor) {
        return null;
    }

    @Override
    public ResponseEntity<Doctor> removeDoctorById(long id) {
        return null;
    }

    @Override
    public ResponseEntity<List<Doctor>> getDoctorsBySpecialization(Specialization specialization) {
        return null;
    }

    @Override
    public ResponseEntity<Doctor> changeDoctor(Doctor changedDoctor) {
        return null;
    }
}
