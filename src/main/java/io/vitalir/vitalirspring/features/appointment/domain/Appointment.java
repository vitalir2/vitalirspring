package io.vitalir.vitalirspring.features.appointment.domain;

import io.vitalir.vitalirspring.features.doctors.domain.Doctor;
import io.vitalir.vitalirspring.features.service.Service;
import io.vitalir.vitalirspring.features.user.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "duration", nullable = false)
    private long durationMinutes;

    public Appointment(User user) {
        this.id = 0;
        this.service = null;
        this.doctor = null;
        this.user = user;
        this.startDate = LocalDateTime.now();
        this.durationMinutes = 0;
    }
}
