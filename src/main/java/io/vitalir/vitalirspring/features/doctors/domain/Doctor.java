package io.vitalir.vitalirspring.features.doctors.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "experience")
    private int experienceYears;

    @Column(name = "specialties")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "doctor_specialties", joinColumns = @JoinColumn(name = "doctor_id"))
    @Enumerated(EnumType.STRING)
    private Set<MedicalSpecialty> medicalSpecialties = new HashSet<>();

    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    private Set<Appointment> appointments = new HashSet<>();

    public Doctor(long id, String name, int experienceYears, Set<MedicalSpecialty> medicalSpecialties) {
        this(id, name, experienceYears, medicalSpecialties, new HashSet<>());
    }
    public Doctor(String name, Set<MedicalSpecialty> medicalSpecialties) {
        this(0, name, 0, medicalSpecialties);
    }
    public Doctor(String name) {
        this(0, name, 0, Collections.emptySet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doctor doctor = (Doctor) o;
        return id == doctor.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
