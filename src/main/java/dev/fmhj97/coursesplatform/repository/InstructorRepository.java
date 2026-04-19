package dev.fmhj97.coursesplatform.repository;

import dev.fmhj97.coursesplatform.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {

    /**
     *
     * @param email
     * @return
     */
    Optional<Instructor> findByEmail(String email);

    /**
     *
     * @param email
     * @return
     */
    Boolean existsByEmail(String email);

    /**
     * Searches for instructors whose name, surname, or full name contains the given query string.
     * The search is case-insensitive.
     * @param q the search query
     * @return a list of instructors matching the query
     */
    @Query("SELECT i FROM Instructor i WHERE " +
            "LOWER(i.name) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(i.surname) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(CONCAT(i.name, ' ', i.surname)) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<Instructor> searchByNameSurname(@Param("q") String q);

}
