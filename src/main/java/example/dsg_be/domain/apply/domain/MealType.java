package example.dsg_be.domain.apply.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import example.dsg_be.domain.apply.exception.InvalidMealTypeException;

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
}
