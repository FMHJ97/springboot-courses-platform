package dev.fmhj97.coursesplatform.dto;

import dev.fmhj97.coursesplatform.entity.Instructor;
import dev.fmhj97.coursesplatform.entity.enums.Role;

import java.time.LocalDate;

public class InstructorResponseDto {

    private final Long id;
    private final String name;
    private final String surname;
    private final LocalDate dateOfBirth;
    private final String email;
    private final Role role;

    /**
     * Constructor with args
     * @param id
     * @param name
     * @param surname
     * @param dateOfBirth
     * @param email
     * @param role
     */
    public InstructorResponseDto(Long id, String name, String surname, LocalDate dateOfBirth, String email, Role role) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.role = role;
    }

    /**
     * Converts an Instructor entity into an InstructorResponseDto.
     * @param instructor the Instructor entity to convert
     * @return an InstructorResponseDto with the instructor's data
     */
    public static InstructorResponseDto from(Instructor instructor) {
        return new InstructorResponseDto(
                instructor.getId(),
                instructor.getName(),
                instructor.getSurname(),
                instructor.getDateOfBirth(),
                instructor.getEmail(),
                instructor.getRole()
        );
    }

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }
}
