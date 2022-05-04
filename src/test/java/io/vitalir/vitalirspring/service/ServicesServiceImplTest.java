package io.vitalir.vitalirspring.service;

import io.vitalir.vitalirspring.features.service.domain.Service;
import io.vitalir.vitalirspring.features.service.domain.ServiceRepository;
import io.vitalir.vitalirspring.features.service.domain.ServicesService;
import io.vitalir.vitalirspring.features.service.domain.ServicesServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ServicesServiceImplTest {

    @Mock
    private ServiceRepository serviceRepository;

    private ServicesService servicesService;

    @BeforeEach
    void initBeforeEach() {
        servicesService = new ServicesServiceImpl(serviceRepository);
    }

    @Test
    void whenGetServices_returnThemFromRepo() {
        var mockServices = Set.of(
                new Service("service one"),
                new Service("service two")
        );
        given(serviceRepository.findAll()).willReturn(mockServices);

        var result = servicesService.getServices();

        assertEquals(mockServices, result);
    }

    @Test
    void whenAddService_callAddingServiceInRepository() {
        var addedService = new Service("Service one");

        servicesService.addService(addedService);

        verify(serviceRepository).save(addedService);
    }

    @Test
    void whenAddServiceSuccessful_returnTrue() {
        var addedService = new Service("Service one");

        var result = servicesService.addService(addedService);

        assertTrue(result);
    }

    @Test
    void whenAddServiceIsInvalid_returnFalse() {
        var addedService = new Service(null);

        var result = servicesService.addService(addedService);

        assertFalse(result);
    }

    @Test
    void whenAddServiceIsInvalid_doNotAddIt() {
        var addedService = new Service(null);

        servicesService.addService(addedService);

        verify(serviceRepository, never()).save(any());
    }

    @Test
    void whenRemoveExistingService_returnRemovedService() {
        var service = new Service("service");
        given(serviceRepository.getByTitle(service.getTitle())).willReturn(service);

        var result = servicesService.removeService(service.getTitle());

        assertEquals(service, result);
    }

    @Test
    void whenRemoveNotExistingService_returnNull() {
        var service = new Service("service");

        var result = servicesService.removeService(service.getTitle());

        assertThat(result).isNull();
    }

    @Test
    void whenChangeExistingService_returnTrue() {
        var service = new Service("service");
        given(serviceRepository.getByTitle(service.getTitle())).willReturn(service);

        var result = servicesService.changeService(service);

        assertTrue(result);
    }

    @Test
    void whenChangeNotExistingService_returnFalse() {
        var service = new Service("service");
        given(serviceRepository.getByTitle(service.getTitle())).willReturn(null);

        var result = servicesService.changeService(service);

        assertFalse(result);
    }

    @Test
    void whenChangeExistingService_callSaveNewService() {
        var service = new Service("service");
        given(serviceRepository.getByTitle(service.getTitle())).willReturn(service);

        servicesService.changeService(service);

        verify(serviceRepository).save(service);
    }
}
