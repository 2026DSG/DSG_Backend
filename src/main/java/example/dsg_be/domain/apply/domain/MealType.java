package example.dsg_be.domain.apply.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import example.dsg_be.domain.apply.exception.InvalidMealTypeException;
import java.util.List;

public enum MealType {
    LUNCH,
    LUNCH_SELF,   // 개인 부담
    DINNER,       // 학교 부담
    DINNER_SELF;   // 개인 부담

    @JsonCreator
    public static MealType from(String value) {
        for (MealType mealType : MealType.values()) {
            if (mealType.name().equalsIgnoreCase(value)) {
                return mealType;
            }
        }
        throw InvalidMealTypeException.EXCEPTION;
    }

    // 같은 끼니(중식/석식)에 속하는 유형 목록
    public List<MealType> sameSlot() {
        return switch (this) {
            case LUNCH, LUNCH_SELF -> List.of(LUNCH, LUNCH_SELF);
            case DINNER, DINNER_SELF -> List.of(DINNER, DINNER_SELF);
        };
    }
}
