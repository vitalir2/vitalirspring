package io.vitalir.vitalirspring.service;

import org.springframework.stereotype.Repository;

@Repository
public interface CrudServiceRepository extends
        org.springframework.data.repository.Repository<Service, String>, ServiceRepository {
}
