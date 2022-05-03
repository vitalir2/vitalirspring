package io.vitalir.vitalirspring.features.service.presentation;

import io.vitalir.vitalirspring.common.constants.HttpEndpoints;
import io.vitalir.vitalirspring.features.service.domain.Service;
import io.vitalir.vitalirspring.features.service.domain.ServicesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(HttpEndpoints.SERVICES_ENDPOINT)
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

    @Override
    @DeleteMapping("/{title}")
    public ResponseEntity<?> removeService(@PathVariable String title) {
        if (servicesService.removeService(title) != null) {
            return ResponseEntity.ok(List.of(new Service(title)));
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @Override
    @PutMapping
    public ResponseEntity<?> changeService(@RequestBody Service service) {
        if (servicesService.changeService(service)) {
            return ResponseEntity.ok(true);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
}
