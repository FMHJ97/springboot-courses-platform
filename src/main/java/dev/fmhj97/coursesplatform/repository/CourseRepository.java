package dev.fmhj97.coursesplatform.repository;

import dev.fmhj97.coursesplatform.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    /**
     *
     * @param instructorId
     * @return
     */
    List<Course> findByInstructorId(Long instructorId);

    /**
     *
     * @param title
     * @return
     */
    List<Course> findByTitleContainingIgnoreCase(String title);

    /**
     *
     * @param min
     * @param max
     * @return
     */
    List<Course> findByPriceBetween(BigDecimal min, BigDecimal max);

    /**
     *
     * @param title
     * @return
     */
    Boolean existsByTitleIgnoreCase(String title);
}
