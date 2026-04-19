package dev.fmhj97.coursesplatform.repository;

import dev.fmhj97.coursesplatform.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     *
     * @param email
     * @return
     */
    Optional<Student> findByEmail(String email);

    /**
     *
     * @param email
     * @return
     */
    Boolean existsByEmail(String email);

    /**
     * Searches for students whose name, surname, or full name contains the given query string.
     * The search is case-insensitive.
     * @param q the search query
     * @return a list of students matching the query
     */
    @Query("SELECT s FROM Student s WHERE " +
            "LOWER(s.name) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(s.surname) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(CONCAT(s.name, ' ', s.surname)) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<Student> searchByNameSurname(@Param("q") String q);
}
