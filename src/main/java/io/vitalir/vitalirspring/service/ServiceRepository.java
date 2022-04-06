package io.vitalir.vitalirspring.service;

import java.util.Set;

public interface ServiceRepository {
    Set<Service> getServices();

    void addService(Service service);

    Service getServiceByTitle(String title);

    void removeService(String title);
}
