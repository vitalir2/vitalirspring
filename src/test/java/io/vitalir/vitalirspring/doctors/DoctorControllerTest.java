package io.vitalir.vitalirspring.doctors;

import io.vitalir.vitalirspring.features.doctors.domain.DoctorService;
import io.vitalir.vitalirspring.features.doctors.presentation.DoctorController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DoctorController.class)
@ActiveProfiles("test")
public class DoctorControllerTest extends DoctorFeatureTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DoctorService doctorService;

    @Test
    public void whenGetAllDoctors_returnThem() throws Exception {
        given(doctorService.getAll()).willReturn(List.of(DOCTOR));
        var requestBuilder = get(DOCTORS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", equalTo((int) DOCTOR.getId())));
    }

    @Test
    void whenGetDoctorByIdWhichExists_returnIt() throws Exception {
        given(doctorService.getDoctorById(DOCTOR.getId())).willReturn(Optional.of(DOCTOR));
        var requestBuilder = get(DOCTORS_ENDPOINT + DOCTOR.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo((int) DOCTOR.getId())));
    }

    @Test
    void whenGetDoctorByIdWhichDoesNotExist_returnBadRequest() throws Exception {
        given(doctorService.getDoctorById(DOCTOR.getId())).willReturn(Optional.empty());
        var requestBuilder = get(DOCTORS_ENDPOINT + DOCTOR.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }
}