package example.dsg_be.domain.teacher.exception;

import example.dsg_be.global.error.excpetion.BusinessException;
import example.dsg_be.global.error.excpetion.ErrorCode;

public class NameAlreadyExistException extends BusinessException {
    public static final NameAlreadyExistException EXCEPTION = new NameAlreadyExistException();
    public NameAlreadyExistException() {
        super(ErrorCode.Name_Already_Exist_Exception);
    }
}
