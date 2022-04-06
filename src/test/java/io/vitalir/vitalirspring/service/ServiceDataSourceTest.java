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
public class ServiceDataSourceTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ServiceDataSource serviceDataSource;

    @Test
    void injectedComponentIsNotNull() {
        assertThat(testEntityManager).isNotNull();
        assertThat(serviceDataSource).isNotNull();
    }

    @Test
    void whenGetAllServices_returnThem() {
        var testServices = new HashSet<>(Set.of(
                new ServiceEntity("Well-check"),
                new ServiceEntity("Patient appointment")
        ));
        for (ServiceEntity service: testServices) {
            testEntityManager.persist(service);
        }

        var result = serviceDataSource.findAll();
        assertThat(result).hasSize(testServices.size());
        for (ServiceEntity service: result) {
            testServices.remove(service);
        }
        assertThat(testServices).isEmpty();
    }

    @Test
    void whenAddingNewService_addIt() {
        var addedService = new ServiceEntity("Service");

        serviceDataSource.save(addedService);

        var findResult = testEntityManager.find(ServiceEntity.class, addedService.getTitle());
        assertThat(findResult).isNotNull();
        assertThat(findResult.getTitle()).isEqualTo(addedService.getTitle());
    }
}
