package io.vitalir.vitalirspring.common;

import io.vitalir.vitalirspring.features.doctors.domain.Doctor;
import io.vitalir.vitalirspring.features.doctors.domain.DoctorRepository;
import io.vitalir.vitalirspring.features.doctors.domain.MedicalSpecialty;
import io.vitalir.vitalirspring.features.user.domain.model.Role;
import io.vitalir.vitalirspring.features.user.domain.model.User;
import io.vitalir.vitalirspring.features.user.domain.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class CommandLineAppRunner implements CommandLineRunner {

    private static final String ADMIN_EMAIL = "admin@gmail.com";
    private static final String DEFAULT_PASSWORD = "password";

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final DoctorRepository doctorRepository;

    public CommandLineAppRunner(UserRepository userRepository, PasswordEncoder passwordEncoder, DoctorRepository doctorRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public void run(String... args) {
        var foundAdmin = userRepository.getUserByEmail(ADMIN_EMAIL);
        if (foundAdmin.isPresent()) {
            return;
        }
        String password = parsePassword(args);
        addAdmin(password);
        preInitDoctors();
    }

    private String parsePassword(String... args) {
        String password;
        try {
            password = args[0];
        } catch (ArrayIndexOutOfBoundsException exception) {
            password = DEFAULT_PASSWORD;
        }
        return password;
    }

    private void addAdmin(String password) {
        User admin = new User(
                ADMIN_EMAIL,
                passwordEncoder.encode(password),
                Role.ADMIN
        );
        userRepository.save(admin);
    }

    private void preInitDoctors() {
        for (var doctor: createInitialDoctors()) {
            doctorRepository.save(doctor);
        }
    }

    private static List<Doctor> createInitialDoctors() {
        var doctor1 = new Doctor();
        doctor1.setName("Henry Peterson");
        doctor1.setExperienceYears(10);
        doctor1.setMedicalSpecialties(Set.of(MedicalSpecialty.CARDIOLOGY));

        var doctor2 = new Doctor();
        doctor2.setName("Natalia Sherikhova");
        doctor2.setExperienceYears(13);
        doctor2.setMedicalSpecialties(Set.of(MedicalSpecialty.ENDOCRINOLOGY));

        var doctor3 = new Doctor();
        doctor3.setName("Julia Peterson");
        doctor3.setExperienceYears(4);
        doctor3.setMedicalSpecialties(Set.of(MedicalSpecialty.PULMONOLOGY));

        var doctor4 = new Doctor();
        doctor4.setName("Peter Barinov");
        doctor4.setExperienceYears(8);
        doctor4.setMedicalSpecialties(Set.of(MedicalSpecialty.GASTROENTEROLOGY));

        var doctor5 = new Doctor();
        doctor5.setName("Mark Wertov");
        doctor5.setExperienceYears(30);
        doctor5.setMedicalSpecialties(Set.of(MedicalSpecialty.ONCOLOGY, MedicalSpecialty.RHEUMATOLOGY));

        var doctor6 = new Doctor();
        doctor6.setName("George Narinov");
        doctor6.setExperienceYears(8);
        doctor6.setMedicalSpecialties(Set.of(MedicalSpecialty.PEDIATRICS));

        var doctor7 = new Doctor();
        doctor7.setName("Vitaly Vostov");
        doctor7.setExperienceYears(5);
        doctor7.setMedicalSpecialties(Set.of(MedicalSpecialty.GASTROENTEROLOGY));

        var doctor8 = new Doctor();
        doctor8.setName("Anastasia Orekhova");
        doctor8.setExperienceYears(16);
        doctor8.setMedicalSpecialties(Set.of(MedicalSpecialty.NEPHROLOGY));

        return List.of(
                doctor1,
                doctor2,
                doctor3,
                doctor4,
                doctor5,
                doctor6,
                doctor7,
                doctor8
        );
    }
}
