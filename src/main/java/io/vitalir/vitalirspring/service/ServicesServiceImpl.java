package io.vitalir.vitalirspring.service;

import lombok.RequiredArgsConstructor;

import java.util.Set;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServicesServiceImpl implements ServicesService {

    private final ServiceRepository serviceRepository;

    @Override
    public Set<Service> getServices() {
        return serviceRepository.findAll();
    }

    @Override
    public boolean addService(Service service) {
        if (validate(service)) {
            serviceRepository.save(service);
            return true;
        }
        return false;
    }

    @Override
    public Service removeService(String title) {
        var removedService =  serviceRepository.getByTitle(title);
        serviceRepository.removeByTitle(title);
        return removedService;
    }

    @Override
    public boolean changeService(Service service) {
        var oldService = serviceRepository.getByTitle(service.getTitle());
        if (oldService != null) {
            serviceRepository.save(service);
            return true;
        }
        return false;
    }

    private boolean validate(Service service) {
        return service.getTitle() != null;
    }
}
