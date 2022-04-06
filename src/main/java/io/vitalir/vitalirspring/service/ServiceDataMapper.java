package io.vitalir.vitalirspring.service;

import org.springframework.stereotype.Component;

@Component
public class ServiceDataMapper {
    Service dataToDomain(ServiceEntity serviceEntity) {
        return new Service(
                serviceEntity.getTitle()
        );
    }

    ServiceEntity domainToData(Service service) {
        return new ServiceEntity(
                service.title()
        );
    }
}
