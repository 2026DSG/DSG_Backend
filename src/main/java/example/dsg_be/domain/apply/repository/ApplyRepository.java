package example.dsg_be.domain.apply.repository;

import example.dsg_be.domain.apply.domain.ApplyEntity;
import example.dsg_be.domain.apply.domain.MealType;
import example.dsg_be.domain.teacher.domain.TeacherEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ApplyRepository extends JpaRepository<ApplyEntity, Long> {

    boolean existsByTeacherAndDate(TeacherEntity teacher, LocalDate date);

    @EntityGraph(attributePaths = "teacher")
    List<ApplyEntity> findAllByMealAndDateOrderByCreatedAtDesc(MealType meal, LocalDate date);

    @EntityGraph(attributePaths = "teacher")
    List<ApplyEntity> findAllByCreatedAtBetweenOrderByCreatedAtAsc(
            LocalDateTime start,
            LocalDateTime end
    );

    @EntityGraph(attributePaths = "teacher")
    List<ApplyEntity> findAllByDate(LocalDate date);
}
