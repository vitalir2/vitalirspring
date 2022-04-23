package io.vitalir.vitalirspring.doctors;

import io.vitalir.vitalirspring.features.doctors.domain.Doctor;

public class DoctorFeatureTest {

    protected static final String DOCTORS_ENDPOINT = "/api/v1/doctors/";
    protected static final String DOCTOR_NAME = "";
    protected static final Doctor DOCTOR = new Doctor("hello");
    protected static final Doctor INVALID_DOCTOR = new Doctor(null);
}
