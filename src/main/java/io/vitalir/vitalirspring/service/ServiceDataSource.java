package io.vitalir.vitalirspring.service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceDataSource extends CrudRepository<ServiceEntity, String> {
}
