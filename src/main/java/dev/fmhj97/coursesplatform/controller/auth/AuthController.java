package dev.fmhj97.coursesplatform.controller.auth;

import dev.fmhj97.coursesplatform.dto.auth.AuthResponseDto;
import dev.fmhj97.coursesplatform.dto.auth.LoginRequestDto;
import dev.fmhj97.coursesplatform.dto.auth.RegisterRequestDto;
import dev.fmhj97.coursesplatform.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Constructor
     * @param authService
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Registers a new user and returns a JWT token.
     * @param dto the registration request containing user details and role
     * @return a JWT token for the newly registered user
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody @Valid RegisterRequestDto dto) {
        return ResponseEntity.ok(authService.register(dto));
    }

    /**
     * Authenticates a user and returns a JWT token.
     * @param dto the login request containing email and password
     * @return a JWT token for the authenticated user
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody @Valid LoginRequestDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }
}
