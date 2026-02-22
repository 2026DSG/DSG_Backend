package example.dsg_be.domain.apply.presentation.dto.response;

import example.dsg_be.domain.apply.domain.ApplyEntity;
import example.dsg_be.domain.apply.domain.MealType;
import example.dsg_be.domain.apply.domain.PaymentType;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class ApplyListResponse {

    private final Long id;
    private final MealType mealType;
    private final String reason;
    private final String staffName;
    private final String department;
    private final PaymentType paymentType;
    private final LocalDate applyDate;
    private final LocalDateTime createdAt;

    public ApplyListResponse(ApplyEntity apply) {
        this.id = apply.getId();
        this.mealType = apply.getMealType();
        this.reason = apply.getReason();
        this.staffName = apply.getStaffName();
        this.department = apply.getDepartment();
        this.paymentType = apply.getPaymentType();
        this.applyDate = apply.getApplyDate();
        this.createdAt = apply.getCreatedAt();
    }
}