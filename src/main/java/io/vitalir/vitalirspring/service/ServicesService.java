package io.vitalir.vitalirspring.service;

import java.util.Set;

public interface ServicesService {
    Set<Service> getServices();

    boolean addService(Service service);

    Service removeService(String title);
}
