package example.dsg_be.domain.user.exception;

import example.dsg_be.global.error.excpetion.BusinessException;
import example.dsg_be.global.error.excpetion.ErrorCode;

public class RefreshTokenNotFoundException extends BusinessException {
    public static final RefreshTokenNotFoundException EXCEPTION = new RefreshTokenNotFoundException();
    public RefreshTokenNotFoundException() {
        super(ErrorCode.Refresh_Token_Not_Found_Exception);
    }
}
