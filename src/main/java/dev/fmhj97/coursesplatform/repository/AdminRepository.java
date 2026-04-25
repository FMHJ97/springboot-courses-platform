package dev.fmhj97.coursesplatform.repository;

import dev.fmhj97.coursesplatform.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    /**
     *
     * @param email
     * @return
     */
    Optional<Admin> findByEmail(String email);

    /**
     *
     * @param email
     * @return
     */
    Boolean existsByEmail(String email);
}
