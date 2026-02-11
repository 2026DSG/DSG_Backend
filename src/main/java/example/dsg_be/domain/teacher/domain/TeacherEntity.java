package example.dsg_be.domain.teacher.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "teacher_tbl")
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
