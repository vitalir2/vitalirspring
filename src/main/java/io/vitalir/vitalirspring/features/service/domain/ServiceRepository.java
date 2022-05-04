package io.vitalir.vitalirspring.features.service.domain;

import java.util.Optional;
import java.util.Set;

public interface ServiceRepository {
    Set<Service> findAll();

    void save(Service service);

    Service getByTitle(String title);

    void removeByTitle(String title);

    Optional<Service> findById(long id);
}
