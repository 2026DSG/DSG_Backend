package example.dsg_be.domain.apply.repository;

import example.dsg_be.domain.apply.domain.ApplyEntity;
import example.dsg_be.domain.apply.domain.MealType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ApplyRepository extends JpaRepository<ApplyEntity, Long> {

    boolean existsByApplyDateAndMealType(LocalDate applyDate, MealType mealType);

    List<ApplyEntity> findAllByMealTypeOrderByCreatedAtDesc(MealType mealType);
}