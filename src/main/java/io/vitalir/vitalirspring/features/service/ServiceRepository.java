package io.vitalir.vitalirspring.features.service;

import java.util.Set;

public interface ServiceRepository {
    Set<Service> findAll();

    void save(Service service);

    Service getByTitle(String title);

    void removeByTitle(String title);
}