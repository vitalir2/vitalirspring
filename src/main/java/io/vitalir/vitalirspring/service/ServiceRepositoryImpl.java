package io.vitalir.vitalirspring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class ServiceRepositoryImpl implements ServiceRepository {
    private final ServiceDataSource serviceDataSource;
    private final ServiceDataMapper serviceMapper;

    @Override
    public Set<Service> getServices() {
        var entities = serviceDataSource.findAll();
        Set<Service> result = new HashSet<>();
        for (ServiceEntity entity: entities) {
            result.add(serviceMapper.dataToDomainModel(entity));
        }
        return result;
    }

    @Override
    public void addService(Service service) {
        serviceDataSource.save(serviceMapper.domainToDataModel(service));
    }

    @Override
    public Service getServiceByTitle(String title) {
        return null;
    }

    @Override
    public void removeService(String title) {
    }
}
