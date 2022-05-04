package io.vitalir.vitalirspring.features.service.data;

import io.vitalir.vitalirspring.features.service.domain.Service;
import io.vitalir.vitalirspring.features.service.domain.ServiceRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrudServiceRepository extends
        org.springframework.data.repository.Repository<Service, String>, ServiceRepository {
}
