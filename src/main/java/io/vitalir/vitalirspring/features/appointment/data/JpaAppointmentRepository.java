package io.vitalir.vitalirspring.features.appointment.data;

import io.vitalir.vitalirspring.features.appointment.domain.Appointment;
import io.vitalir.vitalirspring.features.appointment.domain.AppointmentRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaAppointmentRepository extends AppointmentRepository, CrudRepository<Appointment, Long> {

    @Override
    @Query("SELECT a FROM Appointment a WHERE a.user.id = :user_id")
    List<Appointment> getAppointmentsByUserId(@Param("user_id") long userId);
}
