package io.vitalir.vitalirspring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ServiceDataMapperTest {

    private ServiceDataMapper serviceMapper;

    @BeforeEach
    void init() {
        serviceMapper = new ServiceDataMapper();
    }

    @Test
    void whenInputIsData_getDomain() {
        var data = new ServiceEntity("myTitle");
        var domainResult = serviceMapper.dataToDomainModel(data);
        assertThat(domainResult.title()).isEqualTo(data.getTitle());
    }

    @Test
    void whenInputIsDomain_getData() {
        var domain = new Service("Service 1");
        var dataResult = serviceMapper.domainToDataModel(domain);
        assertThat(dataResult.getTitle()).isEqualTo(domain.title());
    }
}
