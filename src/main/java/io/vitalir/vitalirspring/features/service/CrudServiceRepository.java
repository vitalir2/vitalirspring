package io.vitalir.vitalirspring.features.service;

import org.springframework.stereotype.Repository;

@Repository
public interface CrudServiceRepository extends
        org.springframework.data.repository.Repository<Service, String>, ServiceRepository {
}
