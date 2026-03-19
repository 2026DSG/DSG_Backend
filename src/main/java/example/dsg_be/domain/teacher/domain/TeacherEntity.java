package example.dsg_be.domain.teacher.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Entity
@Table(name = "teacher_tbl")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TeacherEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_id", nullable = false)
    private Long teacherId;

    @Column(name = "number")
    private Long number;

    @Column(name = "department", nullable = false)
    private String department;

    @Column(name = "position", nullable = false)
    private String position;

    @Column(name = "teacher_name", nullable = false)
    private String name;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_removed", nullable = false)
    private Boolean isRemoved;

    @Builder
    public TeacherEntity(Long number, String department, String position, String name) {
        this.number = number;
        this.department = department;
        this.position = position;
        this.name = name;
        this.createdAt = LocalDateTime.now();
        this.isRemoved = false;
    }

    public void update(String department, String position, Long number) {
        this.department = department;
        this.position = position;
        this.number = number;
        if(this.isRemoved) {
            this.isRemoved = false;
        }
    }

    public void changeStatus() {
        this.number = 0L;
        this.isRemoved = true;
    }
}
