package io.vitalir.vitalirspring.features.doctors.presentation;

import io.vitalir.vitalirspring.features.doctors.domain.Doctor;
import io.vitalir.vitalirspring.features.doctors.domain.DoctorService;
import io.vitalir.vitalirspring.features.doctors.domain.MedicalSpecialty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Doctor>> getDoctors(
            @RequestParam(name = "specialty", required = false) MedicalSpecialty medicalSpecialty
    ) {
        if (medicalSpecialty != null) {
            return ResponseEntity.ok(doctorService.getDoctorsBySpecialty(medicalSpecialty));
        }
        return ResponseEntity.ok(doctorService.getAll());
    }

    @Override
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Doctor> getDoctorById(@PathVariable long id) {
        var doctor = doctorService.getDoctorById(id);
        if (doctor.isPresent()) {
            return ResponseEntity.ok(doctor.get());
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
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
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Doctor> removeDoctorById(@PathVariable long id) {
        var doctor = doctorService.removeDoctorById(id);
        if (doctor.isPresent()) {
            return ResponseEntity.ok(doctor.get());
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @Override
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
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
