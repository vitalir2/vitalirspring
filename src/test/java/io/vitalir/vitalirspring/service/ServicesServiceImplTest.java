package io.vitalir.vitalirspring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
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
}
