package io.vitalir.vitalirspring.service;

import io.vitalir.vitalirspring.service.Service;
import io.vitalir.vitalirspring.service.ServiceController;
import io.vitalir.vitalirspring.service.ServicesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
}
