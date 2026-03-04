package example.dsg_be.domain.apply.exception;

import example.dsg_be.global.error.excpetion.BusinessException;
import example.dsg_be.global.error.excpetion.ErrorCode;

public class ApplyNotFoundException extends BusinessException {
    public static final ApplyNotFoundException EXCEPTION = new ApplyNotFoundException();
    public ApplyNotFoundException() {
        super(ErrorCode.Apply_Not_Found_Exception);
    }
}
