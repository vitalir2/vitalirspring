package io.vitalir.vitalirspring.features.doctors.domain;

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

    // TODO: remove optional = true (set to false)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Doctor doctor;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private User user;

    @Column(name = "description", nullable = false)
    private String description = "";

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "duration", nullable = false)
    private long durationMillis;
}
