package example.dsg_be.domain.teacher.repository;

import example.dsg_be.domain.teacher.domain.TeacherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<TeacherEntity, Long> {
    List<TeacherEntity> findAllByIsRemovedFalse();

    @Query("SELECT MAX(t.number) FROM TeacherEntity t WHERE t.isRemoved = false")
    Optional<Long> findMaxNumberByIsRemovedFalse();

    boolean existsByName(String name);
    Optional<TeacherEntity> findByName(String name);
}
