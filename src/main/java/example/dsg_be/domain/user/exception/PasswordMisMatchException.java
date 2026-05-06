package example.dsg_be.domain.user.exception;

import example.dsg_be.global.error.excpetion.BusinessException;
import example.dsg_be.global.error.excpetion.ErrorCode;

public class PasswordMisMatchException extends BusinessException {
    public static final PasswordMisMatchException EXCEPTION = new PasswordMisMatchException();
    public PasswordMisMatchException() {
        super(ErrorCode.Password_Mis_Match_Exception);
    }
}
