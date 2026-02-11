package example.dsg_be.domain.teacher.exception;

import example.dsg_be.global.error.excpetion.BusinessException;
import example.dsg_be.global.error.excpetion.ErrorCode;

public class NameCellIsEmptyException extends BusinessException {
    public static final NameCellIsEmptyException EXCEPTION = new NameCellIsEmptyException();
    public NameCellIsEmptyException() {
        super(ErrorCode.Name_Cell_Is_Empty_Exception);
    }
}
