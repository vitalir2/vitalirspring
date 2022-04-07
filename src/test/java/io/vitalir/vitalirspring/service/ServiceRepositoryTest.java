package io.vitalir.vitalirspring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ServiceRepositoryTest {

    private ServiceRepository serviceRepository;

    @Mock
    private ServiceDataSource serviceDataSource;

    @BeforeEach
    void init() {
        serviceRepository = new ServiceRepositoryImpl(
                serviceDataSource,
                new ServiceDataMapper()
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

    @Test
    void whenAddService_addMappedServiceInDataSource() {
        var service = new Service("Service 1");

        serviceRepository.addService(service);

        verify(serviceDataSource).save(eq(new ServiceEntity(service.title())));
    }

    @Test
    void whenGetServiceByTitleWhichExists_returnIt() {
        var service = new Service("Service");
        given(serviceDataSource.findById(service.title()))
                .willReturn(Optional.of(new ServiceEntity(service.title())));

        var result = serviceRepository.getServiceByTitle(service.title());

        assertThat(result).isEqualTo(service);
    }

    @Test
    void whenGetServiceByTitleWhichDoesNotExist_returnNull() {
        var service = new Service("Service");
        given(serviceDataSource.findById(service.title())).willReturn(Optional.empty());

        var result = serviceRepository.getServiceByTitle(service.title());

        assertThat(result).isEqualTo(null);
    }

    @Test
    void whenRemoveServiceByTitleWhichExists_returnIt() {
        var service = new Service("Service");
        given(serviceDataSource.findById(service.title()))
                .willReturn(Optional.of(new ServiceEntity(service.title())));

        var result = serviceRepository.removeService(service.title());

        assertThat(result).isEqualTo(service);
    }

    @Test
    void whenRemoveServiceByTitleWhichDoesNotExist_returnNull() {
        var service = new Service("Service");
        given(serviceDataSource.findById(service.title())).willReturn(Optional.empty());

        var result = serviceRepository.removeService(service.title());

        assertThat(result).isEqualTo(null);
    }

    @Test
    void whenRemoveServiceByTitleWhichExists_callDeleteFromDatasource() {
        var service = new Service("Service");
        given(serviceDataSource.findById(service.title()))
                .willReturn(Optional.of(new ServiceEntity(service.title())));

        var result = serviceRepository.removeService(service.title());

        verify(serviceDataSource).delete(eq(new ServiceEntity(result.title())));
    }
}
