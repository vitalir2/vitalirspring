package io.vitalir.vitalirspring.service;

import org.springframework.stereotype.Component;

@Component
public class ServiceMapper {
    Service dataToDomain(ServiceEntity serviceEntity) {
        return new Service(
                serviceEntity.title
        );
    }
}
