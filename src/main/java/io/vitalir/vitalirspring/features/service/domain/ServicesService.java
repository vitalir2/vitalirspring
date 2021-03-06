package io.vitalir.vitalirspring.features.service.domain;

import java.util.Set;

public interface ServicesService {
    Set<Service> getServices();

    boolean addService(Service service);

    Service removeService(String title);

    boolean changeService(Service service);
}
