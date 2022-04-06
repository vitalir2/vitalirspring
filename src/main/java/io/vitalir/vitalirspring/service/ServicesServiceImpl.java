package io.vitalir.vitalirspring.service;

import lombok.RequiredArgsConstructor;

import java.util.Set;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServicesServiceImpl implements ServicesService {

    private final ServiceRepository serviceRepository;

    @Override
    public Set<Service> getServices() {
        return serviceRepository.getServices();
    }

    @Override
    public boolean addService(Service service) {
        if (validate(service)) {
            serviceRepository.addService(service);
            return true;
        }
        return false;
    }

    @Override
    public Service removeService(String title) {
        var removedService =  serviceRepository.getServiceByTitle(title);
        serviceRepository.removeService(title);
        return removedService;
    }

    private boolean validate(Service service) {
        return service.title() != null;
    }
}
