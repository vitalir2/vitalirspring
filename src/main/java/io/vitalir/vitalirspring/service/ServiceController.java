package io.vitalir.vitalirspring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/services")
@RequiredArgsConstructor
public class ServiceController implements ServiceApi {
    private final ServicesService servicesService;

    @GetMapping
    public ResponseEntity<Set<Service>> getServices() {
        return ResponseEntity.ok(servicesService.getServices());
    }
}
