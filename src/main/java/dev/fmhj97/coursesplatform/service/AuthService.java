package dev.fmhj97.coursesplatform.service;

import dev.fmhj97.coursesplatform.dto.auth.AuthResponseDto;
import dev.fmhj97.coursesplatform.dto.auth.LoginRequestDto;
import dev.fmhj97.coursesplatform.dto.auth.RegisterRequestDto;
import dev.fmhj97.coursesplatform.entity.Instructor;
import dev.fmhj97.coursesplatform.entity.Student;
import dev.fmhj97.coursesplatform.entity.enums.Role;
import dev.fmhj97.coursesplatform.exception.DuplicateResourceException;
import dev.fmhj97.coursesplatform.exception.InvalidDataException;
import dev.fmhj97.coursesplatform.repository.AdminRepository;
import dev.fmhj97.coursesplatform.repository.InstructorRepository;
import dev.fmhj97.coursesplatform.repository.StudentRepository;
import dev.fmhj97.coursesplatform.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final InstructorRepository instructorRepository;
    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    /**
     * Constructor
     * @param instructorRepository
     * @param studentRepository
     * @param adminRepository
     * @param passwordEncoder
     * @param jwtService
     * @param authenticationManager
     * @param userDetailsService
     */
    public AuthService(
            InstructorRepository instructorRepository,
            StudentRepository studentRepository,
            AdminRepository adminRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService
    ) {
        this.instructorRepository = instructorRepository;
        this.studentRepository = studentRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Registers a new user based on the provided role (INSTRUCTOR, STUDENT or ADMIN),
     * encodes the password, saves the user to the database, and returns a JWT token.
     * @param dto the registration request
     * @return an AuthResponseDto containing the JWT token
     */
    public AuthResponseDto register(RegisterRequestDto dto) {

        // Check if email already exists in instructors, students or admins
        if (instructorRepository.existsByEmail(dto.getEmail()) ||
            studentRepository.existsByEmail(dto.getEmail()) ||
            adminRepository.existsByEmail(dto.getEmail())
        ) {
            throw new DuplicateResourceException("Email already exists: " + dto.getEmail());
        }

        // Prevent users from registering as ADMIN
        if (dto.getRole() == Role.ADMIN) throw new InvalidDataException("Cannot register as ADMIN");

        // Encode the password before saving
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        // Create the user based on the role
        UserDetails userDetails;

        if (dto.getRole() == Role.INSTRUCTOR) {
            Instructor instructor = new Instructor(
                    dto.getName(), dto.getSurname(), dto.getDateOfBirth(), dto.getEmail(), encodedPassword, dto.getRole()
            );

            // New instructor added to our database
            userDetails = instructorRepository.save(instructor);
        }
        else {
            Student student = new Student(
                    dto.getName(), dto.getSurname(), dto.getDateOfBirth(), dto.getEmail(), encodedPassword, dto.getRole()
            );

            // New student added to our database
            userDetails = studentRepository.save(student);
        }

        // Generate and return the JWT token
        String token = jwtService.generateToken(userDetails);
        return new AuthResponseDto(token);
    }

    /**
     * Authenticates a user using their email and password.
     * If credentials are valid, generates and returns a JWT token.
     * @param dto the login request containing email and password
     * @return an AuthResponseDto containing the JWT token
     */
    public AuthResponseDto login(LoginRequestDto dto) {

        // Authenticate the user - throws exception if credentials are wrong
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        // Load the user from the database
        UserDetails userDetails = userDetailsService.loadUserByUsername(dto.getEmail());

        // Generate and return the JWT token
        String token = jwtService.generateToken(userDetails);
        return new AuthResponseDto(token);
    }
}
