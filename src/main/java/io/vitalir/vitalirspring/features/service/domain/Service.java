package io.vitalir.vitalirspring.features.service.domain;

import io.vitalir.vitalirspring.features.appointment.domain.Appointment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "price", nullable = false)
    // Rubbles
    private int price;

    @OneToMany(mappedBy = "service", fetch = FetchType.LAZY)
    private List<Appointment> appointments;

    public Service(String title) {
        this(0, title, 0, Collections.emptyList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Service entity = (Service) o;
        return Objects.equals(title, entity.title);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
