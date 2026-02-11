package example.dsg_be.domain.teacher.exception;

import example.dsg_be.global.error.excpetion.BusinessException;
import example.dsg_be.global.error.excpetion.ErrorCode;

public class TeacherNotExistException extends BusinessException {
    public static final BusinessException EXCEPTION = new TeacherNotExistException();
    public TeacherNotExistException() {
        super(ErrorCode.Teacher_Not_Exist_Exception);
    }
}
