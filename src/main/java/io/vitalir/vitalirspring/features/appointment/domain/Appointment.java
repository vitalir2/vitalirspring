package io.vitalir.vitalirspring.features.appointment.domain;

import io.vitalir.vitalirspring.features.doctors.domain.Doctor;
import io.vitalir.vitalirspring.features.user.domain.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "description", nullable = false)
    private String description = "";

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "duration", nullable = false)
    private long durationMillis;

    public Appointment(User user) {
        this.id = 0;
        this.description = "";
        this.doctor = null;
        this.user = user;
        this.startDate = LocalDate.now();
        this.durationMillis = 0;
    }
}
