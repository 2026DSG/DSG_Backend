package example.dsg_be.domain.teacher.exception;

import example.dsg_be.global.error.excpetion.BusinessException;
import example.dsg_be.global.error.excpetion.ErrorCode;

public class NotExcelFileException extends BusinessException {
    public static final NotExcelFileException Exception = new NotExcelFileException();
    public NotExcelFileException() {
        super(ErrorCode.Not_Excel_File_Exception);
    }
}
