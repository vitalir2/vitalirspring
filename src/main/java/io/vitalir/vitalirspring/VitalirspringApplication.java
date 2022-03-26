package io.vitalir.vitalirspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VitalirspringApplication {

    public static void main(String[] args) {
        SpringApplication.run(VitalirspringApplication.class, args);
    }
}

/**
 * Plan for project:
 * - We have auth
 * - Every client has his appointments
 * - We have list of uslugs / doctors for observation
 * - Admin / Client
 * - Client can register an appointment with a doctor that has free time in that period
 * - Admin can add / remove / update uslugi / doctors from the website
 * - Client can change his appointment list (remove them but at least 1 day until the appointment, add)
 */
