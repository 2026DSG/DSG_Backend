package example.dsg_be.domain.apply.exception;

import example.dsg_be.global.error.excpetion.BusinessException;
import example.dsg_be.global.error.excpetion.ErrorCode;

public class AlreadyAppliedException extends BusinessException {
    public static final AlreadyAppliedException EXCEPTION = new AlreadyAppliedException();
    public AlreadyAppliedException() {
        super(ErrorCode.Already_Applied_Exception);
    }
}
