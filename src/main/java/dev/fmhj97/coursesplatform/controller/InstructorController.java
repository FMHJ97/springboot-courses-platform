package dev.fmhj97.coursesplatform.controller;

import dev.fmhj97.coursesplatform.dto.InstructorResponseDto;
import dev.fmhj97.coursesplatform.dto.InstructorUpdateDto;
import dev.fmhj97.coursesplatform.entity.User;
import dev.fmhj97.coursesplatform.service.InstructorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/instructors")
@Tag(name = "Instructors", description = "Endpoints related to instructors")
@SecurityRequirement(name = "bearerAuth") // Same name as @SecurityScheme (SwaggerConfig.java)
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
    @Operation(summary = "Gel all instructors or search by name/surname")
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
    @Operation(summary = "Get instructor by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Instructor found"),
            @ApiResponse(responseCode = "404", description = "Instructor not found")
    })
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
    @Operation(summary = "Update instructor fields")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Instructor updated"),
            @ApiResponse(responseCode = "400", description = "Not allowed to update another instructor"),
            @ApiResponse(responseCode = "404", description = "Instructor not found"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
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
    @Operation(summary = "Delete instructor by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Instructor deleted"),
            @ApiResponse(responseCode = "400", description = "Not allowed to delete another instructor"),
            @ApiResponse(responseCode = "404", description = "Instructor not found")
    })
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser
    ) {
        instructorService.delete(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}
