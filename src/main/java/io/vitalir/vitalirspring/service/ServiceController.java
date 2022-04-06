package io.vitalir.vitalirspring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/services")
@RequiredArgsConstructor
public class ServiceController implements ServiceApi {
    private final ServicesService servicesService;

    @Override
    @GetMapping
    public ResponseEntity<Set<Service>> getServices() {
        return ResponseEntity.ok(servicesService.getServices());
    }

    @Override
    @PostMapping
    public ResponseEntity<?> addService(@RequestBody Service service) {
        if (servicesService.addService(service)) {
            return ResponseEntity.ok().build();
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
}
