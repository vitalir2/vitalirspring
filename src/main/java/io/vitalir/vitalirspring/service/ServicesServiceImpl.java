package io.vitalir.vitalirspring.service;

import java.util.List;

@org.springframework.stereotype.Service
public class ServicesServiceImpl implements ServicesService {
    @Override
    public List<io.vitalir.vitalirspring.service.Service> getServices() {
        return List.of(
                new Service("first"),
                new Service("second")
        );
    }
}
