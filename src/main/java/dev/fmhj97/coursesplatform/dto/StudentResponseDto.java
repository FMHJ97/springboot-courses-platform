package dev.fmhj97.coursesplatform.dto;

import dev.fmhj97.coursesplatform.entity.Student;
import dev.fmhj97.coursesplatform.entity.enums.Role;

import java.time.LocalDate;

public class StudentResponseDto {

    private final Long id;
    private final String name;
    private final String surname;
    private final LocalDate dateOfBirth;
    private final String email;
    private final Role role;

    /**
     * Constructor wirh args
     * @param id
     * @param name
     * @param surname
     * @param dateOfBirth
     * @param email
     * @param role
     */
    public StudentResponseDto(Long id, String name, String surname, LocalDate dateOfBirth, String email, Role role) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.role = role;
    }

    /**
     * Converts a Student entity into a StudentResponseDto.
     * @param student the Student entity to convert
     * @return a StudentResponseDto with the student's data.
     */
    public static StudentResponseDto from(Student student) {
        return new StudentResponseDto(
                student.getId(),
                student.getName(),
                student.getSurname(),
                student.getDateOfBirth(),
                student.getEmail(),
                student.getRole()
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
