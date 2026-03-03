package example.dsg_be.domain.user.exception;

import example.dsg_be.global.error.excpetion.BusinessException;
import example.dsg_be.global.error.excpetion.ErrorCode;

public class UserNotFoundException extends BusinessException {
    public static final UserNotFoundException EXCEPTION = new UserNotFoundException();
    public UserNotFoundException() {
        super(ErrorCode.User_Not_Found_Exception);
    }
}
