package dev.fmhj97.coursesplatform.security;

import dev.fmhj97.coursesplatform.entity.Admin;
import dev.fmhj97.coursesplatform.entity.Instructor;
import dev.fmhj97.coursesplatform.entity.Student;
import dev.fmhj97.coursesplatform.repository.AdminRepository;
import dev.fmhj97.coursesplatform.repository.InstructorRepository;
import dev.fmhj97.coursesplatform.repository.StudentRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final InstructorRepository instructorRepository;
    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;

    /**
     * Constructor
     * @param instructorRepository
     * @param studentRepository
     * @param adminRepository
     */
    public UserDetailsServiceImpl(
            InstructorRepository instructorRepository,
            StudentRepository studentRepository,
            AdminRepository adminRepository
    ) {
        this.instructorRepository = instructorRepository;
        this.studentRepository = studentRepository;
        this.adminRepository = adminRepository;
    }

    /**
     * Loads a user by their email address.
     * Searches first in instructors, then in students, and finally in admins.
     * @param email the email of the user to load
     * @return the UserDetails of the found user
     * @throws UsernameNotFoundException if no user is found with the given email
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Search in instructors first
        Optional<Instructor> instructor = instructorRepository.findByEmail(email);

        if (instructor.isPresent()) return instructor.get();

        // Search in students
        Optional<Student> student = studentRepository.findByEmail(email);

        if (student.isPresent()) return student.get();

        // Then in admins
        Optional<Admin> admin = adminRepository.findByEmail(email);

        if (admin.isPresent()) return admin.get();

        // If not found, throw exception
        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}
