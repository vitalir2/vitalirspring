package io.vitalir.vitalirspring.service;

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
        given(serviceRepository.getServices()).willReturn(mockServices);

        var result = servicesService.getServices();

        assertEquals(mockServices, result);
    }

    @Test
    void whenAddService_callAddingServiceInRepository() {
        var addedService = new Service("Service one");

        servicesService.addService(addedService);

        verify(serviceRepository).addService(addedService);
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

        verify(serviceRepository, never()).addService(any());
    }

    @Test
    void whenRemoveExistingService_returnRemovedService() {
        var service = new Service("service");
        given(serviceRepository.getServiceByTitle(service.title())).willReturn(service);

        var result = servicesService.removeService(service.title());

        assertEquals(service, result);
    }

    @Test
    void whenRemoveNotExistingService_returnNull() {
        var service = new Service("service");

        var result = servicesService.removeService(service.title());

        assertThat(result).isNull();
    }
}
