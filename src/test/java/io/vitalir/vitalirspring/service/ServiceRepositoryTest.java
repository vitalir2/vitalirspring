package io.vitalir.vitalirspring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ServiceRepositoryTest {

    private ServiceRepository serviceRepository;

    @Mock
    private ServiceDataSource serviceDataSource;

    @BeforeEach
    void init() {
        serviceRepository = new ServiceRepositoryImpl(
                serviceDataSource,
                new ServiceMapper()
        );
    }

    @Test
    void whenGetServices_returnServices() {
        var entityServices = Set.of(
                new ServiceEntity("one"),
                new ServiceEntity("two")
        );
        given(serviceDataSource.findAll()).willReturn(entityServices);

        var result = new HashSet<>(serviceRepository.getServices());
        assertThat(result).hasSize(entityServices.size());

        var namesInit = entityServices.stream()
                .map(ServiceEntity::getTitle)
                .collect(Collectors.toSet());
        var namesResult = result.stream()
                .map(Service::title)
                .collect(Collectors.toSet());
        assertTrue(namesInit.containsAll(namesResult));
    }
}
