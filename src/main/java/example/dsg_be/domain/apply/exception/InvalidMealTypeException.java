package example.dsg_be.domain.apply.exception;

import example.dsg_be.global.error.excpetion.BusinessException;
import example.dsg_be.global.error.excpetion.ErrorCode;

public class InvalidMealTypeException extends BusinessException {
    public static final InvalidMealTypeException EXCEPTION = new InvalidMealTypeException();
    public InvalidMealTypeException() {
        super(ErrorCode.Invalid_Meal_Type_Exception);
    }
}
