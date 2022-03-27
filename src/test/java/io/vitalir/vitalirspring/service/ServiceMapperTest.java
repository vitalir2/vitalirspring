package io.vitalir.vitalirspring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ServiceMapperTest {

    private ServiceMapper serviceMapper;

    @BeforeEach
    void init() {
        serviceMapper = new ServiceMapper();
    }

    @Test
    void whenInputIsData_getDomain() {
        var data = new ServiceEntity("myTitle");
        var domainResult = serviceMapper.dataToDomain(data);
        assertThat(domainResult.title()).isEqualTo(data.getTitle());
    }
}
