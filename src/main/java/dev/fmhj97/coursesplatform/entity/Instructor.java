package dev.fmhj97.coursesplatform.entity;

import dev.fmhj97.coursesplatform.entity.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Instructor extends User {

    @OneToMany(mappedBy = "instructor")
    private List<Course> courses;

    /**
     * JPA Constructor
     */
    protected Instructor() {}

    /**
     *
     * @param name
     * @param surname
     * @param dateOfBirth
     * @param email
     * @param password
     * @param role
     */
    public Instructor(String name, String surname, LocalDate dateOfBirth, String email, String password, Role role) {
        super(name, surname, dateOfBirth, email, password, role);
    }

    // --- Getters/Setters

    public List<Course> getCourses() {
        return courses;
    }
}
