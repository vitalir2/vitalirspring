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
    public void addService(Service service) {
        serviceRepository.addService(service);
    }
}
