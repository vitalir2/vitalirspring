package io.vitalir.vitalirspring.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test-security")
public class ServiceControllerSecurityTest {

    private static final String SERVICES_URL = "/api/v1/services/";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenGetServices_permitForAnyUser() throws Exception {
        mockMvc.perform(get(SERVICES_URL))
                .andExpect(status().isOk());
    }

    @Test
    public void whenPostService_doesNotPermitForAnon() throws Exception {
        mockMvc.perform(post(SERVICES_URL).with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    public void whenPostService_doesNotPermitForUser() throws Exception {
        mockMvc.perform(post(SERVICES_URL).with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void whenPostService_permitForAdmin() throws Exception {
        mockMvc.perform(post(SERVICES_URL).with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenDeleteService_doesNotPermitForAnon() throws Exception {
        mockMvc.perform(delete(SERVICES_URL).with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    public void whenDeleteService_doesNotPermitForUser() throws Exception {
        mockMvc.perform(delete(SERVICES_URL).with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void whenDeleteService_permitForAdmin() throws Exception {
        mockMvc.perform(delete(SERVICES_URL + "kek/").with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenPutService_doesNotPermitForAnon() throws Exception {
        mockMvc.perform(put(SERVICES_URL).with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    public void whenPutService_doesNotPermitForUser() throws Exception {
        mockMvc.perform(put(SERVICES_URL).with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void whenPutService_permitForAdmin() throws Exception {
        mockMvc.perform(put(SERVICES_URL).with(csrf()))
                .andExpect(status().isBadRequest());
    }
}
