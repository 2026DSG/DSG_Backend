package example.dsg_be.domain.apply.domain;

import example.dsg_be.domain.teacher.domain.TeacherEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "apply_tbl")
public class ApplyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apply_id", nullable = false)
    private Long applyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private TeacherEntity teacher;

    @Enumerated(EnumType.STRING)
    @Column(name = "meal", nullable = false)
    private MealType meal;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "apply_date", nullable = false)
    private LocalDate applyDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public ApplyEntity(TeacherEntity teacher, MealType meal, String reason, LocalDate applyDate) {
        this.teacher = teacher;
        this.meal = meal;
        this.reason = reason;
        this.applyDate = applyDate;
        this.createdAt = LocalDateTime.now();
    }
}
