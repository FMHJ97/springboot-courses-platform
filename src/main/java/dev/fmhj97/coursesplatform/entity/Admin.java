package dev.fmhj97.coursesplatform.entity;

import dev.fmhj97.coursesplatform.entity.enums.Role;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
public class Admin extends User {

    /**
     * JPA Constructor
     */
    protected Admin() {}

    /**
     * Constructor with args
     * @param name
     * @param surname
     * @param dateOfBirth
     * @param email
     * @param password
     * @param role
     */
    public Admin(String name, String surname, LocalDate dateOfBirth, String email, String password, Role role) {
        super(name, surname, dateOfBirth, email, password, role);
    }
}
