package io.vitalir.vitalirspring.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles({"test"})
public class ServiceRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ServiceRepository serviceRepository;

    @Test
    void injectedComponentIsNotNull() {
        assertThat(testEntityManager).isNotNull();
        assertThat(serviceRepository).isNotNull();
    }

    @Test
    void whenGetAllServices_returnThem() {
        var testServices = new HashSet<>(Set.of(
                new Service("Well-check"),
                new Service("Patient appointment")
        ));
        for (Service service: testServices) {
            testEntityManager.persist(service);
        }

        var result = serviceRepository.findAll();
        assertThat(result).hasSize(testServices.size());
        for (Service service: result) {
            testServices.remove(service);
        }
        assertThat(testServices).isEmpty();
    }

    @Test
    void whenAddingNewService_addIt() {
        var addedService = new Service("Service");

        serviceRepository.save(addedService);

        var findResult = testEntityManager.find(Service.class, addedService.getTitle());
        assertThat(findResult).isNotNull();
        assertThat(findResult.getTitle()).isEqualTo(addedService.getTitle());
    }

    @Test
    void removeExistingService() {
        var service = new Service("Service");
        testEntityManager.persist(service);

        serviceRepository.removeByTitle(service.getTitle());

        assertThat(testEntityManager.find(Service.class, service.getTitle())).isNull();
    }
}
