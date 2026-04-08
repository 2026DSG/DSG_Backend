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

    // 동일 날짜, 동일 교직원 신청 여부
    boolean existsByTeacherAndApplyDate(TeacherEntity teacher, LocalDate applyDate);

    @EntityGraph(attributePaths = "teacher")
    List<ApplyEntity> findAllByMealAndApplyDateOrderByCreatedAtDesc(MealType meal, LocalDate applyDate);

    @EntityGraph(attributePaths = "teacher")
    List<ApplyEntity> findAllByCreatedAtBetweenOrderByCreatedAtAsc(
            LocalDateTime start,
            LocalDateTime end
    );
}
