package example.dsg_be.domain.apply.domain;

import example.dsg_be.domain.teacher.domain.TeacherEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "apply_tbl")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public ApplyEntity(TeacherEntity teacher, MealType meal, String reason, LocalDate applyDate) {
        this.teacher = teacher;
        this.meal = meal;
        this.reason = reason;
        this.applyDate = applyDate;
    }
}
