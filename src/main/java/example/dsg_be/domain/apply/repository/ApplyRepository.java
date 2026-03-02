package example.dsg_be.domain.apply.repository;

import example.dsg_be.domain.apply.domain.ApplyEntity;
import example.dsg_be.domain.apply.domain.MealType;
import example.dsg_be.domain.teacher.domain.TeacherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface ApplyRepository extends JpaRepository<ApplyEntity, Long> {

    boolean existsByTeacherAndMealAndCreatedAtBetween(
            TeacherEntity teacher,
            MealType meal,
            LocalDateTime start,
            LocalDateTime end
    );

    List<ApplyEntity> findAllByMealOrderByCreatedAtDesc(MealType meal);

    List<ApplyEntity> findAllByCreatedAtBetweenOrderByCreatedAtAsc(
            LocalDateTime start,
            LocalDateTime end
    );
}
