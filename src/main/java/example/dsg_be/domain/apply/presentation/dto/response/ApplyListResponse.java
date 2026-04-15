package example.dsg_be.domain.apply.presentation.dto.response;

import example.dsg_be.domain.apply.domain.ApplyEntity;
import example.dsg_be.domain.apply.domain.MealType;
import lombok.Getter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class ApplyListResponse {

    private final Long applyId;
    private final Long teacherId;
    private final String teacherName;
    private final String department;
    private final String position;
    private final MealType meal;
    private final String reason;
    private final LocalDate applyDate;
    private final LocalDateTime createdAt;

    public ApplyListResponse(ApplyEntity applyEntity) {
        this.applyId = applyEntity.getApplyId();
        this.teacherId = applyEntity.getTeacher().getTeacherId();
        this.teacherName = applyEntity.getTeacher().getName();
        this.department = applyEntity.getTeacher().getDepartment();
        this.position = applyEntity.getTeacher().getPosition();
        this.meal = applyEntity.getMeal();
        this.reason = applyEntity.getReason();
        this.applyDate = applyEntity.getApplyDate();
        this.createdAt = applyEntity.getCreatedAt();
    }
}
