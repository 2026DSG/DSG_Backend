package example.dsg_be.domain.teacher.presentation.dto.response;

import example.dsg_be.domain.teacher.domain.TeacherEntity;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class TeacherResponse {
    private Long id;
    private Long number;
    private String department;
    private String position;
    private LocalDateTime createdAt;
    private String name;

    public TeacherResponse(TeacherEntity teacherEntity) {
        this.id = teacherEntity.getTeacherId();
        this.number = teacherEntity.getNumber();
        this.department = teacherEntity.getDepartment();
        this.position = teacherEntity.getPosition();
        this.createdAt = teacherEntity.getCreatedAt();
        this.name = teacherEntity.getName();
    }
}
