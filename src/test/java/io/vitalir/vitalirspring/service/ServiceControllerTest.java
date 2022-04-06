package io.vitalir.vitalirspring.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ServiceController.class)
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

        mockMvc.perform(get("/api/v1/services/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(service.title())));
    }

    @Test
    public void addNewValidServiceReturnsSuccess() throws Exception {
        var newService = new Service("Health checking");
        var objectMapper = new ObjectMapper();

        given(servicesService.addService(any())).willReturn(true);

        mockMvc.perform(
                        post("/api/v1/services")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(newService))
                )
                .andExpect(status().isOk());
    }

    @Test
    public void addNewInvalidServiceReturnsError() throws Exception {
        var newInvalidService = new Service(null);
        var objectMapper = new ObjectMapper();

        mockMvc.perform(
                        post("/api/v1/services")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(newInvalidService))
                )
                .andExpect(status().is(400));
    }

    @Test
    public void addNewInvalidService_callServicesServiceMethodToValidate() throws Exception {
        var newInvalidService = new Service(null);
        var objectMapper = new ObjectMapper();

        mockMvc.perform(
                post("/api/v1/services")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newInvalidService))
        );

        verify(servicesService).addService(newInvalidService);
    }

    @Test
    public void whenDeleteExistingValidService_returnSuccess() throws Exception {
        var service = new Service("Service");

        given(servicesService.removeService(any())).willReturn(true);

        mockMvc.perform(delete("/api/v1/services/" + service.title()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(service.title())));
    }

    @Test
    public void whenDeleteNotExistingValidService_returnNotFound() throws Exception {
        var service = new Service("service 1");

        given(servicesService.removeService(any())).willReturn(false);

        mockMvc.perform(delete("/api/v1/services/" + service.title()))
                .andExpect(status().is(404));
    }

    @Test
    public void whenDeleteService_callServicesService() throws Exception {
        var service = new Service("service");

        mockMvc.perform(delete("/api/v1/services/" + service.title()));

        verify(servicesService).removeService(service.title());
    }
}
