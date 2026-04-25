package dev.fmhj97.coursesplatform.service;

import dev.fmhj97.coursesplatform.dto.InstructorResponseDto;
import dev.fmhj97.coursesplatform.dto.InstructorUpdateDto;
import dev.fmhj97.coursesplatform.entity.Instructor;
import dev.fmhj97.coursesplatform.entity.User;
import dev.fmhj97.coursesplatform.entity.enums.Role;
import dev.fmhj97.coursesplatform.exception.DuplicateResourceException;
import dev.fmhj97.coursesplatform.exception.InvalidDataException;
import dev.fmhj97.coursesplatform.exception.ResourceNotFoundException;
import dev.fmhj97.coursesplatform.repository.InstructorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class InstructorService {

    private final InstructorRepository instructorRepository;

    /**
     * Constructor with args
     * @param instructorRepository
     */
    public InstructorService(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    /**
     *
     * @param id
     * @return
     */
    public InstructorResponseDto getInstructorById(Long id) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found with id: " + id));
        return InstructorResponseDto.from(instructor);
    }

    /**
     *
     * @param query
     * @return
     */
    public List<InstructorResponseDto> getInstructors(String query) {
        if (query != null && !query.isBlank()) {
            return instructorRepository.searchByNameSurname(query).stream()
                    .map(InstructorResponseDto::from)
                    .toList();
        }
        else {
            return instructorRepository.findAll().stream()
                    .map(InstructorResponseDto::from)
                    .toList();
        }
    }

    /**
     *
     * @param id
     */
    public void delete(Long id, User currentUser) {

        // If not admin, can only delete own profile
        if (currentUser.getRole() != Role.ADMIN && !currentUser.getId().equals(id)) {
            throw new InvalidDataException("You can only delete your own profile");
        }

        if (!instructorRepository.existsById(id))
            throw new ResourceNotFoundException("Instructor not found with id: " + id);

        instructorRepository.deleteById(id);
    }

    /**
     *
     * @param id
     * @param dto
     * @param currentUser
     * @return
     */
    public InstructorResponseDto update(
            Long id, InstructorUpdateDto dto, User currentUser
    ) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found with id: " + id));

        // If not admin, can only update own profile
        if (currentUser.getRole() != Role.ADMIN && !currentUser.getId().equals(instructor.getId())) {
            throw new InvalidDataException("You can only update your own profile");
        }

        if (dto.getName() != null && !dto.getName().isBlank()) {
            instructor.setName(dto.getName());
        }

        if (dto.getSurname() != null && !dto.getSurname().isBlank()) {
            instructor.setSurname(dto.getSurname());
        }

        if (dto.getDateOfBirth() != null) {
            if (!dto.getDateOfBirth().isBefore(LocalDate.now())) {
                throw new InvalidDataException("Date of birth cannot be in the future");
            }
            instructor.setDateOfBirth(dto.getDateOfBirth());
        }

        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            if (instructorRepository.existsByEmail(dto.getEmail()) &&
                    !instructor.getEmail().equals(dto.getEmail())) {
                throw new DuplicateResourceException("Email already exists in database: " + dto.getEmail());
            }
            instructor.setEmail(dto.getEmail());
        }

        if (dto.getRole() != null) {
            if (dto.getRole().equals(Role.ADMIN)) {
                throw new InvalidDataException("Cannot assign ADMIN role");
            }
            instructor.setRole(dto.getRole());
        }

        return InstructorResponseDto.from(instructorRepository.save(instructor));
    }
}
