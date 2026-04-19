package dev.fmhj97.coursesplatform.entity;

import dev.fmhj97.coursesplatform.entity.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Student extends User {

    @OneToMany(mappedBy = "student")
    private List<Enrollment> enrollments;

    /**
     * JPA Constructor
     */
    protected Student() {}

    /**
     * Constructor with args
     * @param name
     * @param surname
     * @param dateOfBirth
     * @param email
     * @param password
     * @param role
     */
    public Student(String name, String surname, LocalDate dateOfBirth, String email, String password, Role role) {
        super(name, surname, dateOfBirth, email, password, role);
    }

    // --- Getters/Setters


    public List<Enrollment> getEnrollments() {
        return enrollments;
    }
}
