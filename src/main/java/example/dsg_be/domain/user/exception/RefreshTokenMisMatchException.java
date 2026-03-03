package example.dsg_be.domain.user.exception;

import example.dsg_be.global.error.excpetion.BusinessException;
import example.dsg_be.global.error.excpetion.ErrorCode;

public class RefreshTokenMisMatchException extends BusinessException {
    public static final RefreshTokenMisMatchException EXCEPTION = new RefreshTokenMisMatchException();
    public RefreshTokenMisMatchException() {
        super(ErrorCode.Refresh_Token_Mis_Match_Exception);
    }
}
