package io.vitalir.vitalirspring.features.doctors.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
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
    @CollectionTable(name = "medical_specialties", joinColumns = @JoinColumn(name = "id"))
    @Enumerated(EnumType.STRING)
    private List<MedicalSpecialty> medicalSpecialties;

    public Doctor(String name) {
        this.id = 0;
        this.name = name;
        this.experienceYears = 0;
        this.medicalSpecialties = Collections.emptyList();
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
