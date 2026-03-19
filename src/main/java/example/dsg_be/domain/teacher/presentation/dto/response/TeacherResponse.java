package example.dsg_be.domain.teacher.presentation.dto.response;

import example.dsg_be.domain.teacher.domain.TeacherEntity;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class TeacherResponse {
    private final Long id;
    private final Long number;
    private final String department;
    private final String position;
    private final LocalDateTime createdAt;
    private final String name;

    public TeacherResponse(TeacherEntity teacherEntity) {
        this.id = teacherEntity.getTeacherId();
        this.number = teacherEntity.getNumber();
        this.department = teacherEntity.getDepartment();
        this.position = teacherEntity.getPosition();
        this.createdAt = teacherEntity.getCreatedAt();
        this.name = teacherEntity.getName();
    }
}
