package io.vitalir.vitalirspring.service;

import io.vitalir.vitalirspring.common.DataMapper;
import org.springframework.stereotype.Component;

@Component
class ServiceDataMapper implements DataMapper<ServiceEntity, Service> {
    @Override
    public ServiceEntity domainToDataModel(Service domainModel) {
        return new ServiceEntity(
                domainModel.title()
        );
    }

    @Override
    public Service dataToDomainModel(ServiceEntity dataModel) {
        return new Service(
                dataModel.getTitle()
        );
    }
}
