package example.dsg_be.domain.teacher.exception;

import example.dsg_be.global.error.excpetion.BusinessException;
import example.dsg_be.global.error.excpetion.ErrorCode;

public class TeacherNotFoundException extends BusinessException {
    public static final TeacherNotFoundException EXCEPTION = new TeacherNotFoundException();
    public TeacherNotFoundException() {
        super(ErrorCode.Teacher_Not_Found_Exception);
    }
}
