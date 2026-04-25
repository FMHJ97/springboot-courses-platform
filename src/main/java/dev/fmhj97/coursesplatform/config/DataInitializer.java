package dev.fmhj97.coursesplatform.config;

import dev.fmhj97.coursesplatform.entity.Admin;
import dev.fmhj97.coursesplatform.entity.enums.Role;
import dev.fmhj97.coursesplatform.repository.AdminRepository;
import dev.fmhj97.coursesplatform.repository.InstructorRepository;
import dev.fmhj97.coursesplatform.repository.StudentRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer {

    private final AdminRepository adminRepository;
    private final InstructorRepository instructorRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor
     * @param adminRepository
     * @param instructorRepository
     * @param studentRepository
     * @param passwordEncoder
     */
    public DataInitializer(
            AdminRepository adminRepository,
            InstructorRepository instructorRepository,
            StudentRepository studentRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.adminRepository = adminRepository;
        this.instructorRepository = instructorRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Creates a default ADMIN user on application startup if none exists.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void init() {

        String adminEmail = "admin@coursesplatform.com";

        boolean adminExists = instructorRepository.existsByEmail(adminEmail) ||
                studentRepository.existsByEmail(adminEmail) || adminRepository.existsByEmail(adminEmail);

        if (!adminExists) {
            Admin admin = new Admin(
                    "Admin",
                    "Admin",
                    LocalDate.of(1997, 1, 5),
                    adminEmail,
                    passwordEncoder.encode("admin1234"),
                    Role.ADMIN
            );

            adminRepository.save(admin);
            System.out.println("Default ADMIN user created: " + adminEmail);
        }
    }
}
