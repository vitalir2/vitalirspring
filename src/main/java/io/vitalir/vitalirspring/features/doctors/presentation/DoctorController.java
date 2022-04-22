package io.vitalir.vitalirspring.features.doctors.presentation;

import io.vitalir.vitalirspring.features.doctors.domain.Doctor;
import io.vitalir.vitalirspring.features.doctors.domain.DoctorService;
import io.vitalir.vitalirspring.features.doctors.domain.MedicalSpecialty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
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
    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable long id) {
        var doctor = doctorService.getDoctorById(id);
        if (doctor.isPresent()) {
            return ResponseEntity.ok(doctor.get());
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @Override
    @PostMapping
    public ResponseEntity<Long> addDoctor(Doctor doctor) {
        var optionalDoctorId = doctorService.addDoctor(doctor);
        if (optionalDoctorId.isPresent()) {
            return ResponseEntity
                    .created(URI.create("/api/v1/doctors/" + doctor.getId()))
                    .body(optionalDoctorId.get());
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Doctor> removeDoctorById(@PathVariable long id) {
        var doctor = doctorService.removeDoctorById(id);
        if (doctor.isPresent()) {
            return ResponseEntity.ok(doctor.get());
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<Doctor>> getDoctorsBySpecialization(MedicalSpecialty specialization) {
        return null;
    }

    @Override
    @PutMapping
    public ResponseEntity<Long> changeDoctor(@RequestBody Doctor changedDoctor) {
        var optionalDoctorId = doctorService.changeDoctor(changedDoctor);
        if (optionalDoctorId.isPresent()) {
            return ResponseEntity
                    .created(URI.create("/api/v1/doctors/" + changedDoctor.getId()))
                    .body(optionalDoctorId.get());
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
}
