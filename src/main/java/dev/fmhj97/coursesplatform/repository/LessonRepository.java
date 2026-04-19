package dev.fmhj97.coursesplatform.repository;

import dev.fmhj97.coursesplatform.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    /**
     *
     * @param id
     * @return
     */
    List<Lesson> findByCourseId(Long id);
}
