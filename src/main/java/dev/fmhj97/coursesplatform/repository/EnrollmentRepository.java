package dev.fmhj97.coursesplatform.repository;

import dev.fmhj97.coursesplatform.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    /**
     *
     * @param studentId
     * @return
     */
    List<Enrollment> findByStudentId(Long studentId);

    /**
     *
     * @param courseId
     * @return
     */
    List<Enrollment> findByCourseId(Long courseId);

    /**
     *
     * @param start
     * @param end
     * @return
     */
    List<Enrollment> findByEnrollmentDateBetween(LocalDate start, LocalDate end);

    /**
     *
     * @param studentId
     * @param courseId
     * @return
     */
    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);

    /**
     *
     * @param studentId
     * @param courseId
     * @return
     */
    Boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
}
