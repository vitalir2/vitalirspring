package io.vitalir.vitalirspring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ServiceRepositoryImpl implements ServiceRepository {
    private final ServiceDataSource serviceDataSource;
    private final ServiceMapper serviceMapper;

    @Override
    public List<Service> getServices() {
        var entities = serviceDataSource.findAll();
        List<Service> result = new ArrayList<>();
        for (ServiceEntity entity: entities) {
            result.add(serviceMapper.dataToDomain(entity));
        }
        return result;
    }
}
