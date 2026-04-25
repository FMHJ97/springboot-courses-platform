package dev.fmhj97.coursesplatform.controller;

import dev.fmhj97.coursesplatform.dto.InstructorResponseDto;
import dev.fmhj97.coursesplatform.dto.InstructorUpdateDto;
import dev.fmhj97.coursesplatform.entity.User;
import dev.fmhj97.coursesplatform.service.InstructorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/instructors")
public class InstructorController {

    private final InstructorService instructorService;

    /**
     * Constructor with args
     * @param instructorService
     */
    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    /**
     *
     * @param q
     * @return
     */
    @GetMapping
    public ResponseEntity<List<InstructorResponseDto>> getInstructors(
            @RequestParam(required = false) String q
    ) {
        return ResponseEntity.ok(instructorService.getInstructors(q));
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<InstructorResponseDto> getInstructorById(@PathVariable Long id) {
        return ResponseEntity.ok(instructorService.getInstructorById(id));
    }

    /**
     *
     * @param id
     * @param dto
     * @return
     */
    @PatchMapping("/{id}")
    public ResponseEntity<InstructorResponseDto> update(
            @PathVariable Long id,
            @RequestBody InstructorUpdateDto dto,
            @AuthenticationPrincipal User currentUser // Authenticated user
            ) {
        return ResponseEntity.ok(instructorService.update(id, dto, currentUser));
    }

    /**
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser
    ) {
        instructorService.delete(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}
