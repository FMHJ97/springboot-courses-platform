package dev.fmhj97.coursesplatform.service;

import dev.fmhj97.coursesplatform.dto.InstructorResponseDto;
import dev.fmhj97.coursesplatform.dto.InstructorUpdateDto;
import dev.fmhj97.coursesplatform.entity.Instructor;
import dev.fmhj97.coursesplatform.entity.enums.Role;
import dev.fmhj97.coursesplatform.exception.DuplicateResourceException;
import dev.fmhj97.coursesplatform.exception.InvalidDataException;
import dev.fmhj97.coursesplatform.exception.ResourceNotFoundException;
import dev.fmhj97.coursesplatform.repository.InstructorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class InstructorServiceTest {

    @Mock
    private InstructorRepository instructorRepository;

    @InjectMocks
    private InstructorService instructorService;

    // --- Helper para crear instructores de prueba ---

    private Instructor buildInstructor(Long id, String email, Role role) {
        Instructor instructor = new Instructor(
                "John", "Doe", LocalDate.of(1990, 1, 1), email, "encoded", role
        );
        // Usamos reflexión para asignar el id (es privado y lo genera JPA)
        try {
            var field = instructor.getClass().getSuperclass().getDeclaredField("id");
            field.setAccessible(true);
            field.set(instructor, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return instructor;
    }

    // ------------------------------------------------
    // getInstructorById
    // ------------------------------------------------

    @Test
    void getInstructorById_whenExists_returnsDto() {
        Instructor instructor = buildInstructor(1L, "john@test.com", Role.INSTRUCTOR);
        given(instructorRepository.findById(1L)).willReturn(Optional.of(instructor));

        InstructorResponseDto result = instructorService.getInstructorById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("john@test.com");
    }

    @Test
    void getInstructorById_whenNotExists_throwsResourceNotFoundException() {
        given(instructorRepository.findById(99L)).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> instructorService.getInstructorById(99L));
    }

    // ------------------------------------------------
    // getInstructors
    // ------------------------------------------------

    @Test
    void getInstructors_withoutQuery_returnsAll() {
        Instructor i1 = buildInstructor(1L, "a@test.com", Role.INSTRUCTOR);
        Instructor i2 = buildInstructor(2L, "b@test.com", Role.INSTRUCTOR);
        given(instructorRepository.findAll()).willReturn(List.of(i1, i2));

        List<InstructorResponseDto> result = instructorService.getInstructors(null);

        assertThat(result).hasSize(2);
    }

    @Test
    void getInstructors_withQuery_returnsFiltered() {
        Instructor instructor = buildInstructor(1L, "john@test.com", Role.INSTRUCTOR);
        given(instructorRepository.searchByNameSurname("John")).willReturn(List.of(instructor));

        List<InstructorResponseDto> result = instructorService.getInstructors("John");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("John");
    }

    // ------------------------------------------------
    // delete
    // ------------------------------------------------

    @Test
    void delete_whenAdminDeletesOther_succeeds() {
        Instructor admin = buildInstructor(1L, "admin@test.com", Role.ADMIN);
        given(instructorRepository.existsById(2L)).willReturn(true);

        instructorService.delete(2L, admin);

        verify(instructorRepository).deleteById(2L);
    }

    @Test
    void delete_whenInstructorDeletesOwn_succeeds() {
        Instructor instructor = buildInstructor(1L, "john@test.com", Role.INSTRUCTOR);
        given(instructorRepository.existsById(1L)).willReturn(true);

        instructorService.delete(1L, instructor);

        verify(instructorRepository).deleteById(1L);
    }

    @Test
    void delete_whenInstructorDeletesOther_throwsInvalidDataException() {
        Instructor instructor = buildInstructor(1L, "john@test.com", Role.INSTRUCTOR);

        assertThrows(InvalidDataException.class,
                () -> instructorService.delete(2L, instructor));
    }

    @Test
    void delete_whenNotExists_throwsResourceNotFoundException() {
        Instructor admin = buildInstructor(1L, "admin@test.com", Role.ADMIN);
        given(instructorRepository.existsById(99L)).willReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> instructorService.delete(99L, admin));
    }

    // ------------------------------------------------
    // update
    // ------------------------------------------------

    @Test
    void update_whenInstructorUpdatesOwn_succeeds() {
        Instructor instructor = buildInstructor(1L, "john@test.com", Role.INSTRUCTOR);
        given(instructorRepository.findById(1L)).willReturn(Optional.of(instructor));
        given(instructorRepository.save(instructor)).willReturn(instructor);

        InstructorUpdateDto dto = new InstructorUpdateDto();
        dto.setName("Jane");

        InstructorResponseDto result = instructorService.update(1L, dto, instructor);

        assertThat(result.getName()).isEqualTo("Jane");
    }

    @Test
    void update_whenInstructorUpdatesOther_throwsInvalidDataException() {
        Instructor instructor = buildInstructor(1L, "john@test.com", Role.INSTRUCTOR);
        Instructor other = buildInstructor(2L, "other@test.com", Role.INSTRUCTOR);
        given(instructorRepository.findById(2L)).willReturn(Optional.of(other));

        InstructorUpdateDto dto = new InstructorUpdateDto();
        dto.setName("Hacker");

        assertThrows(InvalidDataException.class,
                () -> instructorService.update(2L, dto, instructor));
    }

    @Test
    void update_whenEmailAlreadyExists_throwsDuplicateResourceException() {
        Instructor instructor = buildInstructor(1L, "john@test.com", Role.INSTRUCTOR);
        given(instructorRepository.findById(1L)).willReturn(Optional.of(instructor));
        given(instructorRepository.existsByEmail("taken@test.com")).willReturn(true);

        InstructorUpdateDto dto = new InstructorUpdateDto();
        dto.setEmail("taken@test.com");

        assertThrows(DuplicateResourceException.class,
                () -> instructorService.update(1L, dto, instructor));
    }

    @Test
    void update_whenNotExists_throwsResourceNotFoundException() {
        Instructor admin = buildInstructor(1L, "admin@test.com", Role.ADMIN);
        given(instructorRepository.findById(99L)).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> instructorService.update(99L, new InstructorUpdateDto(), admin));
    }
}