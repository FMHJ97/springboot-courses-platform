package dev.fmhj97.coursesplatform.service;

import dev.fmhj97.coursesplatform.dto.StudentResponseDto;
import dev.fmhj97.coursesplatform.dto.StudentUpdateDto;
import dev.fmhj97.coursesplatform.entity.Student;
import dev.fmhj97.coursesplatform.entity.User;
import dev.fmhj97.coursesplatform.entity.enums.Role;
import dev.fmhj97.coursesplatform.exception.DuplicateResourceException;
import dev.fmhj97.coursesplatform.exception.InvalidDataException;
import dev.fmhj97.coursesplatform.exception.ResourceNotFoundException;
import dev.fmhj97.coursesplatform.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    /**
     * Constructor
     * @param studentRepository
     */
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     *
     * @param id
     * @return
     */
    public StudentResponseDto getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(("Student not found with id: " + id)));
        return StudentResponseDto.from(student);
    }

    /**
     *
     * @param query
     * @return
     */
    public List<StudentResponseDto> getStudents(String query) {
        if (query != null && !query.isBlank()) {
            return studentRepository.searchByNameSurname(query).stream()
                    .map(StudentResponseDto::from)
                    .toList();
        }
        else {
            return studentRepository.findAll().stream()
                    .map(StudentResponseDto::from)
                    .toList();
        }
    }

    /**
     *
     * @param id
     * @param currentUser
     */
    public void delete(Long id, User currentUser) {

        // If not admin, can only delete own profile
        if (currentUser.getRole() != Role.ADMIN && !currentUser.getId().equals(id)) {
            throw new InvalidDataException("You can only delete your own profile");
        }

        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student not found with id: " + id);
        }

        studentRepository.deleteById(id);
    }

    /**
     *
     * @param id
     * @param dto
     * @param currentUser
     * @return
     */
    public StudentResponseDto update(
            Long id, StudentUpdateDto dto, User currentUser
    ) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        // If not admin, can only update own profile
        if (currentUser.getRole() != Role.ADMIN && !currentUser.getId().equals(student.getId())) {
            throw new InvalidDataException("You can only update your own profile");
        }

        if (dto.getName() != null && !dto.getName().isBlank()) {
            student.setName(dto.getName());
        }

        if (dto.getSurname() != null && !dto.getSurname().isBlank()) {
            student.setSurname(dto.getSurname());
        }

        if (dto.getDateOfBirth() != null) {
            if (dto.getDateOfBirth().isAfter(LocalDate.now())) {
                throw new InvalidDataException(("Date of birth cannot be in the future"));
            }
            student.setDateOfBirth(dto.getDateOfBirth());
        }

        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            if (studentRepository.existsByEmail(dto.getEmail()) &&
                    !student.getEmail().equals(dto.getEmail())
            ) {
                throw new DuplicateResourceException("Email already exists in database: " + dto.getEmail());
            }
            student.setEmail(dto.getEmail());
        }

        return StudentResponseDto.from(studentRepository.save(student));
    }
}
