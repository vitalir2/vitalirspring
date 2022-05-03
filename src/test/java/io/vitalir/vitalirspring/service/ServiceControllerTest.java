package io.vitalir.vitalirspring.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vitalir.vitalirspring.common.constants.HttpEndpoints;
import io.vitalir.vitalirspring.features.service.domain.Service;
import io.vitalir.vitalirspring.features.service.presentation.ServiceController;
import io.vitalir.vitalirspring.features.service.domain.ServicesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = ServiceController.class)
@ActiveProfiles({"test"})
public class ServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServicesService servicesService;

    @Test
    public void getServicesReturnsNotEmptyContent() throws Exception {
        Service service = new Service("Health checking");
        Set<Service> services = Set.of(service);

        given(servicesService.getServices()).willReturn(services);

        var requestBuilder = get(HttpEndpoints.SERVICES_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(service.getTitle())));
    }

    @Test
    public void addNewValidServiceReturnsSuccess() throws Exception {
        var newService = new Service("Health checking");
        var objectMapper = new ObjectMapper();

        given(servicesService.addService(any())).willReturn(true);

        var requestBuilder = post(HttpEndpoints.SERVICES_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newService))
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    public void addNewInvalidServiceReturnsError() throws Exception {
        var newInvalidService = new Service(null);
        var objectMapper = new ObjectMapper();

        var requestBuilder = post(HttpEndpoints.SERVICES_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newInvalidService))
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addNewInvalidService_callServicesServiceMethodToValidate() throws Exception {
        var newInvalidService = new Service(null);
        var objectMapper = new ObjectMapper();

        var requestBuilder = post(HttpEndpoints.SERVICES_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newInvalidService))
                .with(csrf());
        mockMvc.perform(requestBuilder);

        verify(servicesService).addService(newInvalidService);
    }

    @Test
    public void whenDeleteExistingValidService_returnSuccess() throws Exception {
        var service = new Service("Service");

        given(servicesService.removeService(any())).willReturn(service);

        var requestBuilder = delete(
                HttpEndpoints.SERVICES_ENDPOINT + service.getTitle()
        )
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(service.getTitle())));
    }

    @Test
    public void whenDeleteNotExistingValidService_returnNotFound() throws Exception {
        var service = new Service("service 1");

        given(servicesService.removeService(any())).willReturn(null);
        var requestBuilder = delete(
                HttpEndpoints.SERVICES_ENDPOINT + service.getTitle()
        )
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenDeleteService_callServicesService() throws Exception {
        var service = new Service("service");

        var requestBuilder = delete(
                HttpEndpoints.SERVICES_ENDPOINT + service.getTitle()
        )
                .with(csrf());
        mockMvc.perform(requestBuilder);

        verify(servicesService).removeService(service.getTitle());
    }

    @Test
    public void whenChangeExistingService_returnTrue() throws Exception {
        var serviceAfter = new Service("service after");
        var objectMapper = new ObjectMapper();
        given(servicesService.changeService(serviceAfter)).willReturn(true);

        var requestBuilder = put(HttpEndpoints.SERVICES_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(serviceAfter))
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void whenChangeServiceWhichDoesNotExist_returnFalse() throws Exception {
        var serviceChanged = new Service("service changed");
        var objectMapper = new ObjectMapper();

        given(servicesService.changeService(serviceChanged)).willReturn(false);

        var requestBuilder = put(HttpEndpoints.SERVICES_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(serviceChanged))
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenChangeService_callServiceChange() throws Exception {
        var serviceChanged = new Service("service");
        var objectMapper = new ObjectMapper();

        var requestBuilder = put(HttpEndpoints.SERVICES_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(serviceChanged))
                .with(csrf());
        mockMvc.perform(requestBuilder);

        verify(servicesService).changeService(serviceChanged);
    }
}
