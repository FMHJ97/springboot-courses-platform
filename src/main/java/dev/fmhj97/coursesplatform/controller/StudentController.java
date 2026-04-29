package dev.fmhj97.coursesplatform.controller;

import dev.fmhj97.coursesplatform.dto.StudentResponseDto;
import dev.fmhj97.coursesplatform.dto.StudentUpdateDto;
import dev.fmhj97.coursesplatform.entity.User;
import dev.fmhj97.coursesplatform.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    /**
     * Constructor
     * @param studentService
     */
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     *
     * @param q
     * @return
     */
    @GetMapping
    public ResponseEntity<List<StudentResponseDto>> getStudents(
            @RequestParam(required = false) String q
    ) {
        return ResponseEntity.ok(studentService.getStudents(q));
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDto> getStudentById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    /**
     *
     * @param id
     * @param dto
     * @param currentUSer
     * @return
     */
    @PatchMapping("/{id}")
    public ResponseEntity<StudentResponseDto> update(
            @PathVariable Long id,
            @RequestBody StudentUpdateDto dto,
            @AuthenticationPrincipal User currentUSer
    ) {
        return ResponseEntity.ok(studentService.update(id, dto, currentUSer));
    }

    /**
     *
     * @param id
     * @param currentUser
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id, @AuthenticationPrincipal User currentUser
    ) {
        studentService.delete(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}
