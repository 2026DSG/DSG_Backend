package example.dsg_be.domain.apply.presentation.dto.response;

import example.dsg_be.domain.apply.domain.ApplyEntity;
import example.dsg_be.domain.apply.domain.MealType;
import lombok.Getter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class ApplyCreateResponse {

    private Long applyId;
    private Long teacherId;
    private String teacherName;
    private String department;
    private MealType meal;
    private String reason;
    private LocalDate applyDate;
    private LocalDateTime createdAt;

    public ApplyCreateResponse(ApplyEntity applyEntity) {
        this.applyId = applyEntity.getApplyId();
        this.teacherId = applyEntity.getTeacher().getTeacherId();
        this.teacherName = applyEntity.getTeacher().getName();
        this.department = applyEntity.getTeacher().getDepartment();
        this.meal = applyEntity.getMeal();
        this.reason = applyEntity.getReason();
        this.applyDate = applyEntity.getApplyDate();
        this.createdAt = applyEntity.getCreatedAt();
    }
}
